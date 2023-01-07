package com.memerland.segurity.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.memerland.segurity.gsonSerializers.LocalDateTimeAdapterGson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Conexion {
    private String ip;
    private LocalDateTime fecha;
    private TipoConexion tipo;


    public String toJson(){
        return new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapterGson()).create().toJson(this);
    }

}

