package com.memerland.segurity.mongo;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.memerland.segurity.Segurity;
import lombok.NoArgsConstructor;
import org.bson.Document;

import java.util.Optional;


public class BasicDao <T,ID> extends MongoUtils {
    protected String collectionName;
    private Class<T> type;

    public BasicDao(String collectionName, Class<T> type) {
        super();
        this.collectionName = collectionName;
        this.type = type;

    }
    public void createCollection() {
        database.createCollection(collectionName);
    }

    public void save(T t) {
        Gson gson = new Gson();
        String json = gson.toJson(t);
        database.getCollection(collectionName).insertOne(Document.parse(json));
    }

    public void delete(ID id) {
        database.getCollection(collectionName).deleteOne(new Document("_id", id));
    }

    public Optional<T> findById(ID id) {
        Document document = database.getCollection(collectionName).find(new Document("_id", id)).first();
        Gson gson = new Gson();
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
        Gson gson = new Gson();
        String json = gson.toJson(t);
        database.getCollection(collectionName).replaceOne(
                new Document("_id", ((Document) Document.parse(json).get("_id")).get("$oid")), Document.parse(json)
        );
    }


}
