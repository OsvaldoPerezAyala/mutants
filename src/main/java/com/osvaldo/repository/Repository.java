package com.osvaldo.repository;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Singleton;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.osvaldo.utils.Constants.*;


@Singleton
public class Repository {
    private static final Logger LOGGER = Logger.getLogger(Repository.class.getName());
    private static MongoClient mongoClient;

    @ConfigProperty(name = "connection_chain")
    String connectionChain;


    public synchronized MongoClient getMongoClient() {
        if (Objects.isNull(mongoClient)) {
            connect();
        }
        return mongoClient;
    }

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

    public void insertDNA(final MongoClient mongoClient, final JSONObject dna) {
        try {
            MongoDatabase database = mongoClient.getDatabase(CAFE_DB);
            MongoCollection<Document> dnaCollection = database.getCollection(DNA);

            dnaCollection.insertOne(Document.parse(dna.toString()));
            LOGGER.log(Level.INFO, "Inserted");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error toInsert: ", e);
        }
    }

    public JSONObject getStats(final MongoClient mongoClient) {

        var summary = new JSONObject();
        try {
            MongoDatabase database = mongoClient.getDatabase(CAFE_DB);
            MongoCollection<Document> dnaCollection = database.getCollection(DNA);

            Document query = new Document();
            query.put(IS_MUTANT, true);
            var mutants = dnaCollection.countDocuments(query);

            Document queryNoMutants = new Document();
            queryNoMutants.put(IS_MUTANT, false);
            var humans = dnaCollection.countDocuments(queryNoMutants);

            summary.put(COUNT_MUTANT_DNA, mutants);
            summary.put(COUNT_HUMAN_DNA, humans);
            double ratio = mutants != 0 && humans != 0 ? Double.parseDouble(mutants + "") / Double.parseDouble(humans + "") : 0d;
            summary.put(RATIO, ratio);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error get stats: ", e);
        }
        return summary;
    }

}
