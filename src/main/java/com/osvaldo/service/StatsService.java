package com.osvaldo.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.osvaldo.repository.Repository;
import org.bson.Document;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static com.osvaldo.utils.Constants.*;

@ApplicationScoped
public class StatsService {

    @Inject
    Repository repository;

    /**
     * This method allows get dna stats using repository class
     *
     * @return JSONObject contains summary stats, count of dna mutants, count of dna humans, and ratio.
     */
    public JSONObject getDNAStats() {

        var summary = new JSONObject();

        MongoDatabase database = repository.getMongoClient().getDatabase(CAFE_DB);
        MongoCollection<Document> dnaCollection = database.getCollection(DNA);

        Document queryMutants = new Document();
        queryMutants.put(IS_MUTANT, true);

        var humans = repository.countRecords(dnaCollection, queryMutants);

        Document queryHumans = new Document();
        queryHumans.put(IS_MUTANT, false);

        var mutants = repository.countRecords(dnaCollection, queryHumans);

        summary.put(COUNT_MUTANT_DNA, mutants);
        summary.put(COUNT_HUMAN_DNA, humans);
        double ratio = mutants != 0 && humans != 0 ? Double.parseDouble(mutants + "") / Double.parseDouble(humans + "") : 0d;
        summary.put(RATIO, ratio);
        return summary;
    }

}
