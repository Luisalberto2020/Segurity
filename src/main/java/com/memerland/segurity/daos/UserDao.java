package com.memerland.segurity.daos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.memerland.segurity.Segurity;
import com.memerland.segurity.Utils.Coordenadas;
import com.memerland.segurity.gsonSerializers.LocalDateTimeAdapterGson;
import com.memerland.segurity.model.Conexion;
import com.memerland.segurity.model.User;
import com.memerland.segurity.mongo.BasicDao;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Updates;
import org.bson.BasicBSONObject;
import org.bson.Document;

import java.time.LocalDateTime;
import java.util.Optional;

public class UserDao extends BasicDao<User, String> {

    public UserDao() {
        super("users", User.class);
    }

    public Optional<User> findByName(String name) {
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapterGson()).create();
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
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapterGson()).create();
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
    public void addConexion(String name, Conexion conexion){
        try {
            database.getCollection(collectionName).updateOne(
                    new Document("name", name), new Document("$push", new Document("conexiones", new BasicBSONObject(
                            "ip",conexion.getIp())
                            .append("fecha",conexion.getFecha().format(LocalDateTimeAdapterGson.formatter))
                            .append("tipo",conexion.getTipo().toString())))

            );
        } catch (NullPointerException e) {
            Segurity.instance.getLogger().warning("Error al añadir conexion a la base de datos");
        }
    }

    public void setLoginByDiscord(String discordID,LocalDateTime fecha){
        try {
            database.getCollection(collectionName).updateMany(
                    new Document("discordID", discordID), new Document("$set", new Document("acesso", fecha.toString()))
            );
        } catch (NullPointerException e) {
            Segurity.instance.getLogger().warning("Error al añadir conexion a la base de datos");
        }
    }

    public boolean puedeLogeatde(String name){
        boolean can = false;

        try {
            Document doc = database.getCollection(collectionName).find(new Document("name", name))
                    .projection(Projections.include("acesso")).first();
            LocalDateTime acesso = LocalDateTime.parse(doc.getString("acesso"));
            if(acesso.isAfter(LocalDateTime.now().minusMinutes(2))){
                can = true;
            }
        } catch (NullPointerException e) {
            Segurity.instance.getLogger().warning("Error al añadir conexion a la base de datos");
        }

        return can;
    }

    public Optional<User> findByDiscord(String idDiscord){
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapterGson()).create();
            User user;
            try {
                user = gson.fromJson(database.getCollection(collectionName).find(new Document("discordID", idDiscord))
                        .first().toJson(), User.class);
            } catch (NullPointerException e) {
                user = null;
            }
            return Optional.ofNullable(user);
        } catch (JsonSyntaxException e) {
            return Optional.empty();
        }


    }
    public void deleteAcesso(String name){
        try {
            database.getCollection(collectionName).updateOne(
                    new Document("name", name), Updates.unset("acesso")
            );
        } catch (NullPointerException e) {
            Segurity.instance.getLogger().warning("Error al añadir conexion a la base de datos");
        }
    }

}

