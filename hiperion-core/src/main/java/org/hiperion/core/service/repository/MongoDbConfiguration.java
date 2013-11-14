package org.hiperion.core.service.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.hiperion.common.exception.HiperionException;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 08.04.13.
 * Time: 19:26
 */
public class MongoDbConfiguration {

    public final static String HOST = System.getProperty("mongodb.host");
    public final static int PORT = Integer.parseInt(System.getProperty("mongodb.port"));

    public final static String SYSTEM_DB = System.getProperty("hiperion.core.system.dbname");
    public final static String COLLECTING_DB = System.getProperty("hiperion.core.collecting.dbname");

    private MongoClient mongoClient;
    private DB systemDb;
    private DB collectingDb;

    public MongoDbConfiguration() throws Exception {
        initDbs();
    }

    private void initDbs() throws Exception {
        if (null == mongoClient) {
            mongoClient = new MongoClient(HOST, PORT);
            mongoClient.getDatabaseNames();
        }
        systemDb = mongoClient.getDB(SYSTEM_DB);
        collectingDb = mongoClient.getDB(COLLECTING_DB);
    }

    public DBCollection getCollectingDbCollection(String collectionName) throws HiperionException {
        Set<String> collectionNames = getCollectingCollectionNames();
        if (!collectionNames.contains(collectionName)) {
            createCollectingDbCollection(collectionName);
        }
        return collectingDb.getCollection(collectionName);
    }

    public Set<String> getCollectingCollectionNames() {
        Set<String> collectionNames = collectingDb.getCollectionNames();
        collectionNames.remove("system.indexes");
        return collectionNames;
    }

    public DBCollection getSystemDbCollection(String collectionName) throws HiperionException {
        Set<String> collectionNames = systemDb.getCollectionNames();
        if (!collectionNames.contains(collectionName)) {
            createSystemDbCollection(collectionName);
        }
        return systemDb.getCollection(collectionName);
    }

    private void createCollectingDbCollection(String collectionName) throws HiperionException {
        collectingDb.createCollection(collectionName, null);
        DBCollection createdCollection = collectingDb.getCollection(collectionName);
        createdCollection.createIndex(new BasicDBObject("timestamp", 1));
    }

    private void createSystemDbCollection(String collectionName) throws HiperionException {
        systemDb.createCollection(collectionName, null);
    }
}
