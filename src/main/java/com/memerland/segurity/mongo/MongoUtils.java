package com.memerland.segurity.mongo;

import com.memerland.segurity.Segurity;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;


public class MongoUtils {
    private static String uri = System.getenv("MONGO_URI");
    @Getter
    protected MongoClient client;
    @Getter
    protected MongoDatabase database;

    public static void createDatabase() {
        MongoClient client = MongoClients.create(uri);
        try {
            MongoDatabase database = client.getDatabase("segurity");
            database.createCollection("products");
            database.createCollection("transfers");
            database.createCollection("users");
            database.createCollection("codes");
        } catch (Exception e) {
            Segurity.instance.getLogger().info("Database already exists");
        } finally {
            client.close();
        }

    }

    public MongoUtils() {
       
        client = MongoClients.create(uri);
        database = client.getDatabase("segurity");
    }

    public void close() {
        client.close();
    }

}
