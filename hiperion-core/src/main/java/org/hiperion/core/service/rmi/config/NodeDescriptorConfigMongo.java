package org.hiperion.core.service.rmi.config;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.core.service.repository.MongoDbConfiguration;

import java.util.LinkedList;
import java.util.List;

/**
 * User: iobestar
 * Date: 17.05.13.
 * Time: 17:19
 */
public class NodeDescriptorConfigMongo implements NodeDescriptorConfig {

    private static final Logger LOGGER = Logger.getLogger(NodeDescriptor.class);

    private static final String COLLECTION_NAME = "nodes";

    private final DBCollection nodesCollection;

    private final Gson gson;

    public NodeDescriptorConfigMongo(MongoDbConfiguration mongoDbConfiguration) throws HiperionException {
        this.nodesCollection = mongoDbConfiguration.getSystemDbCollection(COLLECTION_NAME);
        this.gson = new Gson();
    }

    @Override
    public void addNodeDescriptor(NodeDescriptor nodeDescriptor) {
        NodeDescriptor existingNodeDescriptor = findById(nodeDescriptor.getNodeId());
        if (null != existingNodeDescriptor) {
            removeNodeDescriptor(nodeDescriptor.getNodeId());
        }
        DBObject collectingOutputObject = (DBObject) JSON.parse(gson.toJson(nodeDescriptor));
        nodesCollection.insert(collectingOutputObject);
    }

    @Override
    public NodeDescriptor findById(String nodeId) {
        try {
            BasicDBObject query = new BasicDBObject();
            query.put("nodeId", nodeId);
            DBCursor cursor = nodesCollection.find(query);
            if (cursor.hasNext()) {
                return createNodeDescriptor(cursor.next());
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return null;
    }

    @Override
    public void removeNodeDescriptor(String nodeId) {
        try {
            BasicDBObject query = new BasicDBObject();
            query.put("nodeId", nodeId);
            DBCursor cursor = nodesCollection.find(query);
            if (cursor.hasNext()) {
                nodesCollection.remove(cursor.next());
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    @Override
    public List<NodeDescriptor> findAll() {
        List<NodeDescriptor> result = new LinkedList<NodeDescriptor>();
        try {
            DBCursor cursor = nodesCollection.find();
            while (cursor.hasNext()) {
                DBObject current = cursor.next();
                NodeDescriptor nodeDescriptor = createNodeDescriptor(current);
                result.add(nodeDescriptor);
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return result;
    }

    private NodeDescriptor createNodeDescriptor(DBObject nodeDescriptoDbObject) {
        String nodeId = (String) nodeDescriptoDbObject.get("nodeId");
        String hostname = (String) nodeDescriptoDbObject.get("hostname");
        int port = (Integer) nodeDescriptoDbObject.get("port");
        String servicePath = (String) nodeDescriptoDbObject.get("servicePath");

        return new NodeDescriptor(nodeId, hostname, port, servicePath);
    }
}
