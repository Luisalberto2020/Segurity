package com.memerland.segurity;

import com.memerland.segurity.Utils.Config;
import com.memerland.segurity.commands.*;
import com.memerland.segurity.discord.DiscordUtils;
import com.memerland.segurity.events.SegurityEvents;
import com.memerland.segurity.mongo.MongoUtils;
import org.bukkit.plugin.java.JavaPlugin;

public final class Segurity extends JavaPlugin {
    public static Segurity instance;
    @Override
    public void onEnable() {
        instance = this;
        MongoUtils.createDatabase();
        DiscordUtils.init();
        Config.loadConfig(this);
        getCommand("login").setExecutor(new LoginCommand());
       getCommand("register").setExecutor(new RegisterCommand());
       getCommand("op2").setExecutor(new OpCommand());
       getCommand("spawnSegurity").setExecutor(new SpawnCommand());
       getCommand("olvidar").setExecutor(new OlvidarCommand());
         getServer().getPluginManager().registerEvents(new SegurityEvents(), this);


        

        getLogger().info("Segurity is enabled");
        // Plugin startup logic




    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        DiscordUtils.disconnected();
    }
}
