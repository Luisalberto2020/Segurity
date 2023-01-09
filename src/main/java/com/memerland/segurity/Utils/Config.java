package com.memerland.segurity.Utils;


import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Config {
    public static Location spawnLocation;

    public static void loadConfig(JavaPlugin plugin){
       File file = new File(plugin.getDataFolder(), "config.json");
         if(file.exists()){
              ConfigFile configFile = ConfigFile.fromJson(file);
              if(spawnLocation != null){
                  spawnLocation = configFile.getSpawnLocation().toLocation();
              }

         }
    }
}
