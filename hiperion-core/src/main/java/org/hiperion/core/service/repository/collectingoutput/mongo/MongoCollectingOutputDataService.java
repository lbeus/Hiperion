package org.hiperion.core.service.repository.collectingoutput.mongo;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.util.JSON;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.hiperion.common.exception.EntityAlreadyExist;
import org.hiperion.common.exception.EntityDoesNotExist;
import org.hiperion.connector.model.DataField;
import org.hiperion.connector.model.enums.DataType;
import org.hiperion.core.service.collecting.CollectingOutput;
import org.hiperion.core.service.repository.collectingoutput.CollectingOutputDataService;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 24.04.13.
 * Time: 19:44
 */
public class MongoCollectingOutputDataService implements CollectingOutputDataService {

    private final static Logger LOGGER = Logger.getLogger(MongoCollectingOutputDataService.class);

    private DBCollection dbCollection;
    private Gson gson;

    public MongoCollectingOutputDataService(DBCollection dbCollection) {
        this.dbCollection = dbCollection;
        this.gson = new Gson();
    }

    @Override
    public void save(CollectingOutput source) throws EntityAlreadyExist {
        try {
            DBObject collectingOutputObject = (DBObject) JSON.parse(gson.toJson(source));
            dbCollection.insert(collectingOutputObject);
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    @Override
    public void delete(String key) {
        try {
            BasicDBObject query = new BasicDBObject();
            query.put("_id", new ObjectId(key));
            DBCursor cursor = dbCollection.find(query);
            if (cursor.hasNext()) {
                dbCollection.remove(cursor.next());
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    @Override
    public CollectingOutput findById(String id) throws EntityDoesNotExist {
        try {
            BasicDBObject query = new BasicDBObject();
            query.put("_id", new ObjectId(id));
            DBCursor cursor = dbCollection.find(query);
            if (cursor.hasNext()) {
                return createCollectingOutput(cursor.next());
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return null;
    }

    @Override
    public List<CollectingOutput> findAll() {
        List<CollectingOutput> result = new LinkedList<CollectingOutput>();
        try {
            DBCursor cursor = dbCollection.find();
            while (cursor.hasNext()) {
                DBObject current = cursor.next();
                CollectingOutput collectingOutput = createCollectingOutput(current);
                result.add(collectingOutput);
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return result;
    }

    @Override
    public void delete(Set<String> ids) {
        for (String id : ids) {
            delete(id);
        }
    }

    @Override
    public Collection<CollectingOutput> findAll(long fromTimestamp, long toTimestamp) {
        List<CollectingOutput> result = new LinkedList<CollectingOutput>();
        try {
            BasicDBObject query = new BasicDBObject();
            query.put("timestamp", new BasicDBObject("$gte", fromTimestamp));
            query.put("timestamp", new BasicDBObject("$lte", toTimestamp));

            DBCursor cursor = dbCollection.find(query);

            while (cursor.hasNext()) {
                DBObject current = cursor.next();
                CollectingOutput collectingOutput = createCollectingOutput(current);
                result.add(collectingOutput);
            }
        } catch (Exception e) {
            LOGGER.warn(e);
        }
        return result;
    }

    private CollectingOutput createCollectingOutput(DBObject dbObject) {
        ObjectId id = (ObjectId) dbObject.get("_id");
        long timestamp = (Long) (dbObject.get("timestamp"));

        BasicDBList objects = (BasicDBList) dbObject.get("dataFields");
        HashMap<String, DataField> dataFields = new HashMap<String, DataField>();
        for (Object basicDBObject : objects) {
            DBObject dataFieldObject = (DBObject) basicDBObject;
            String name = (String) dataFieldObject.get("name");
            DataType dataType = DataType.valueOf((String) dataFieldObject.get("dataType"));
            String value = (String) dataFieldObject.get("value");
            long dataFieldTimestamp = (Long) dataFieldObject.get("dataFieldTimestamp");

            DataField dataField = new DataField(name, dataType, value, dataFieldTimestamp);
            dataFields.put(name, dataField);
        }

        ImmutableMap<String, DataField> immutableDataFields = ImmutableMap.copyOf(dataFields);
        CollectingOutput collectingOutput = new CollectingOutput(id.toString(),
                dbCollection.getName(), timestamp, immutableDataFields);
        return collectingOutput;
    }

}
