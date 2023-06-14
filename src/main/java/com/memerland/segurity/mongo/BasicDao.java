package com.memerland.segurity.mongo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.memerland.segurity.Segurity;
import com.memerland.segurity.gsonSerializers.LocalDateTimeAdapterGson;
import org.bson.Document;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class BasicDao <T,ID> extends MongoUtils {
    protected String collectionName;
    private final Class<T> type;

    public BasicDao(String collectionName, Class<T> type) {
        super();
        this.collectionName = collectionName;
        this.type = type;

    }
    public void createCollection() {
        database.createCollection(collectionName);
    }

    public void save(T t) {
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapterGson()).create();
        String json = gson.toJson(t);
        database.getCollection(collectionName).insertOne(Document.parse(json));
    }

    public void delete(ID id) {
        database.getCollection(collectionName).deleteOne(new Document("_id", id));
    }

    public Optional<T> findById(ID id) {
        Document document = database.getCollection(collectionName).find(new Document("_id", id)).first();
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapterGson()).create();
        Segurity.instance.getLogger().info("Document: " + document.toJson());

        T t = null;
        try {
            t=gson.fromJson(document.toJson(), type);
        } catch (NullPointerException e) {
           t = null;
        }


        return Optional.ofNullable(t);
    }
    public void update(T t) {
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapterGson()).create();
        String json = gson.toJson(t);
        database.getCollection(collectionName).replaceOne(
                new Document("_id", ((Document) Document.parse(json).get("_id")).get("$oid")), Document.parse(json)
        );
    }
    public List<T> findAll() {
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapterGson()).create();
        List<Document> documents = database.getCollection(collectionName).find().into(new java.util.ArrayList<>());
        ArrayList<T> list = new ArrayList<>();
        for (Document document : documents) {
            list.add(gson.fromJson(document.toJson(), type));
        }
        return list;
    }


}
