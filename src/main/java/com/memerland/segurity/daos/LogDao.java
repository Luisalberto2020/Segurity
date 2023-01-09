package com.memerland.segurity.daos;

import com.memerland.segurity.model.Log;
import com.memerland.segurity.mongo.BasicDao;
import org.bson.Document;

public class LogDao extends BasicDao<Log,String> {

    public LogDao() {
        super("logs", Log.class);
    }

    @Override
    public void save(Log log) {
        database.getCollection(collectionName).insertOne(new Document()
                .append("player",log.getPlayer())
                .append("command",log.getCommand())
                .append("date",log.getDate()));
    }

}

