package com.osvaldo.service;

import com.osvaldo.repository.Repository;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class StatsService {

    @Inject
    Repository repository;

    public JSONObject getDNAStats(){

        return repository.getStats(repository.getMongoClient());
    }

}
