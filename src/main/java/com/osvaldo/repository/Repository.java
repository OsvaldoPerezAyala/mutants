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
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


@ApplicationScoped
public class Repository {
    private static final Logger LOGGER = Logger.getLogger(Repository.class.getName());
    private static MongoClient mongoClient;

    @ConfigProperty(name = "connection_chain")
    String connectionChain;

    /*public Repository(String connectionChain) {
        Repository.connectionChain = connectionChain;
    }*/

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
            mongoClient = MongoClients.create(settings);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error to connect: ", e);
        }
    }

    public void insertDNA(final MongoClient mongoClient, final JSONObject dna) {
        MongoDatabase database = mongoClient.getDatabase("cafeDB");
        MongoCollection<Document> dnaCollection = database.getCollection("dna");

        dnaCollection.insertOne(Document.parse(dna.toString()));
        LOGGER.log(Level.INFO, "Inserted");
    }

    public JSONObject getStats(final MongoClient mongoClient) {

        MongoDatabase database = mongoClient.getDatabase("cafeDB");
        MongoCollection<Document> dnaCollection = database.getCollection("dna");

        Document query = new Document();
        query.put("is_mutant", true);
        var mutants = dnaCollection.countDocuments(query);

        Document queryNoMutants = new Document();
        queryNoMutants.put("is_mutant", false);
        var humans = dnaCollection.countDocuments(queryNoMutants);

        var summary = new JSONObject();

        summary.put("count_mutant_dna", mutants);
        summary.put("count_human_dna", humans);
        double ratio = mutants != 0 && humans != 0 ? Double.parseDouble(mutants + "") / Double.parseDouble(humans + "") : 0d;
        summary.put("ratio", ratio);
        return summary;
    }

}
