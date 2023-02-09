package com.memerland.segurity.utils;

import com.google.gson.Gson;
import com.memerland.segurity.model.Coordenadas;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfigFile {
   private Coordenadas spawnLocation;
   public  String toJson(){
       return new Gson().toJson(this);
   }

   public static ConfigFile fromJson(File file){
       try(BufferedReader b = new BufferedReader(new FileReader(file))){
           return new Gson().fromJson(b, ConfigFile.class);
       } catch (IOException ignored) {

       }
       return null;
   }

}
