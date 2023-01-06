package com.memerland.segurity.gsonSerializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.memerland.segurity.model.Code;

import java.lang.reflect.Type;

public class CodeSerializer implements JsonSerializer<Code> {

    @Override
    public JsonElement serialize(Code src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        //jsonObject.addProperty("_id",src.getCode());
        jsonObject.addProperty("name",src.getName());
        return jsonObject;
    }
}
