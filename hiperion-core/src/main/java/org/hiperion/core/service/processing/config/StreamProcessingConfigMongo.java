package org.hiperion.core.service.processing.config;

import com.google.common.collect.Sets;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.apache.log4j.Logger;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.core.service.processing.ProcessingActionType;
import org.hiperion.core.service.repository.MongoDbConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: iobestar
 * Date: 23.05.13.
 * Time: 20:59
 */
public class StreamProcessingConfigMongo implements StreamProcessingConfig {

    private final static Logger LOGGER = Logger.getLogger(StreamProcessingConfigMongo.class);

    private static final String COLLECTION_NAME = "processing-config";
    private static final String STREAM_ID_FIELD = "streamId";
    private static final String ACTION_ID_FIELD = "actionId";

    private final DBCollection processingConfigCollection;

    private final ConcurrentHashMap<ProcessingActionType, Set<String>> enabledStreamIds;

    public StreamProcessingConfigMongo(MongoDbConfiguration mongoDbConfiguration) throws HiperionException {
        this.processingConfigCollection = mongoDbConfiguration.getSystemDbCollection(COLLECTION_NAME);
        this.enabledStreamIds = new ConcurrentHashMap<ProcessingActionType, Set<String>>();
        load();
    }

    private void load() {
        DBCursor dbCursor = processingConfigCollection.find();
        while (dbCursor.hasNext()){
            DBObject dbObject = dbCursor.next();
            String actionId = (String)dbObject.get(ACTION_ID_FIELD);
            String streamId = (String)dbObject.get(STREAM_ID_FIELD);
            enabledStreamIds.putIfAbsent(ProcessingActionType.getProcessingActionType(actionId),
                    Sets.newSetFromMap(new ConcurrentHashMap<String, Boolean>()));
            enabledStreamIds.get(ProcessingActionType.getProcessingActionType(actionId)).add(streamId);
        }
    }

    @Override
    public boolean isProcessingEnabled(String streamId, ProcessingActionType processingActionType) {
        if (!enabledStreamIds.containsKey(processingActionType)) {
            return false;
        }

        Set<String> enabledStreamSet = enabledStreamIds.get(processingActionType);
        return enabledStreamSet.contains(streamId);
    }

    @Override
    public void enableProcessingFor(String streamId, ProcessingActionType processingActionType) {
        if (isProcessingEnabled(streamId, processingActionType)) {
            return;
        }
        Set<String> streamIds = enabledStreamIds.get(processingActionType);
        if(null == streamIds){
            enabledStreamIds.put(processingActionType, Sets.newSetFromMap(new ConcurrentHashMap<String, Boolean>()));
        }
        enabledStreamIds.get(processingActionType).add(streamId);

        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put(STREAM_ID_FIELD,streamId);
        basicDBObject.put(ACTION_ID_FIELD,processingActionType.getValue());
        processingConfigCollection.save(basicDBObject);
    }

    @Override
    public void disableProcessingFor(String streamId, ProcessingActionType processingActionType) {

        if (!isProcessingEnabled(streamId,processingActionType)) {
            return;
        }

        Set<String> streamIds = enabledStreamIds.get(processingActionType);
        streamIds.remove(streamId);

        BasicDBObject query = new BasicDBObject();
        query.put(STREAM_ID_FIELD, streamId);
        query.put(ACTION_ID_FIELD, processingActionType.getValue());
        DBCursor cursor = processingConfigCollection.find(query);
        if (cursor.hasNext()) {
            processingConfigCollection.remove(cursor.next());
        }
    }
}
