package com.osvaldo.repository;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Singleton;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


@Singleton
public class Repository {
    private static final Logger LOGGER = Logger.getLogger(Repository.class.getName());
    private static MongoClient mongoClient;

    @ConfigProperty(name = "connection_chain")
    String connectionChain;

    /**
     * This method provides the mongo client connection to insert o get data from MongoDB
     *
     * @return mongo client connection
     */
    public synchronized MongoClient getMongoClient() {
        if (Objects.isNull(mongoClient)) {
            connect();
        }
        return mongoClient;
    }

    /**
     * This method build the configuration to do connection to MongoDB
     */
    private void connect() {
        LOGGER.log(Level.INFO, "Connecting...");
        try {
            ConnectionString connectionString = new ConnectionString(connectionChain);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .serverApi(ServerApi.builder()
                            .version(ServerApiVersion.V1)
                            .build())
                    .build();
            Repository.mongoClient = MongoClients.create(settings);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error to connect: ", e);
        }
    }

    /**
     * This method insert dna´s person and the determination result if it´s human or mutant
     *
     * @param dnaCollection MongoDB collection connection
     * @param document      JSONObject who contains the dna array and if it´s from mutant or human
     **/
    public void insertDNA(final MongoCollection<Document> dnaCollection, final Document document) {
        try {
            dnaCollection.insertOne(document);
            LOGGER.log(Level.INFO, "Inserted");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error toInsert: ", e);
        }
    }

    /**
     * This method get and calculate stats of dna from database
     *
     * @param dnaCollection MongoDB collection connection
     * @return counter of records by filter
     */
    public long countRecords(final MongoCollection<Document> dnaCollection, final Document query) {
        try {
            return dnaCollection.countDocuments(query);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error get stats: ", e);
        }
        return 0;
    }

}
