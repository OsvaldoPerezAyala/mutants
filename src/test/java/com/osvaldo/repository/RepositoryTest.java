package com.osvaldo.repository;

import com.mongodb.client.MongoCollection;
import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import org.bson.Document;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.wildfly.common.Assert;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.osvaldo.utils.Constants.IS_MUTANT;

@Mock
@ApplicationScoped
class RepositoryTest extends Repository {
    private static final Logger LOGGER = Logger.getLogger(Repository.class.getName());

    @Override
    public void insertDNA(final MongoCollection<Document> dnaCollection, final Document document) {
        LOGGER.log(Level.INFO, "Inserted");
    }

    @Override
    public long countRecords(final MongoCollection<Document> dnaCollection, final Document query) {

        long counterDNA;
        if (query.getBoolean(IS_MUTANT)) {
            counterDNA = 4;
        } else {
            counterDNA = 6;
        }
        return counterDNA;
    }

}