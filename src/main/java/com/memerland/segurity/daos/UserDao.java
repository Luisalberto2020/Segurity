package com.memerland.segurity.daos;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.memerland.segurity.Segurity;
import com.memerland.segurity.Utils.Coordenadas;
import com.memerland.segurity.model.User;
import com.memerland.segurity.mongo.BasicDao;
import org.bson.BasicBSONObject;
import org.bson.Document;

import java.util.Optional;

public class UserDao extends BasicDao<User, String> {

    public UserDao() {
        super("users", User.class);
    }

    public Optional<User> findByName(String name) {
        Gson gson = new Gson();
        User user;
        try {
            user = gson.fromJson(database.getCollection(collectionName).find(new Document("name", name))
                    .first().toJson(), User.class);
        } catch (NullPointerException e) {
            user = null;
        }
        return Optional.ofNullable(user);

    }
    public boolean setIdDiscord(String name, String id) {


        try {
            database.getCollection(collectionName).updateOne(
                    new Document("name", name), new Document("$set", new Document("discordID", id))
            );
        } catch (NullPointerException e) {
            return false;
        }
        return true;

    }
    public Optional<User> findByNameAndPassword(String name, String password) {
        Gson gson = new Gson();
        User user;
        try {
            user = gson.fromJson(database.getCollection(collectionName).find(new Document("name", name)
                    .append("password", password)).first().toJson(), User.class
            );
        } catch (NullPointerException e) {
            user = null;
        }
        return Optional.ofNullable(user);
    }
    public boolean setOp(String name, boolean op) {

        try {
            database.getCollection(collectionName).updateOne(
                    new Document("name", name), new Document("$set", new Document("isOp", op))
            );
        } catch (NullPointerException e) {
            return false;
        }
        return true;

    }
    public boolean saveLocation(String name, Coordenadas location){
        try {
            database.getCollection(collectionName).updateOne(
                    new Document("name", name), new Document("$set", new Document("location",
                            new BasicBSONObject(
                                    "x",location.getX())
                                    .append("y",location.getY())
                                    .append("z",location.getZ())
                                    .append("world",location.getWorld())))

            );
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }
    public boolean deleteByUserName(String name){
        try {
            database.getCollection(collectionName).deleteOne(new Document("name", name));
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }
    public boolean updatePassword(String name, String password){
        try {
            database.getCollection(collectionName).updateOne(
                    new Document("name", name), new Document("$set", new Document("password", password))
            );
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

}

