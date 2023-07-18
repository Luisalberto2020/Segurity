package com.memerland.segurity;

import com.memerland.segurity.utils.BackupUtils;
import com.memerland.segurity.utils.Config;
import com.memerland.segurity.utils.PlayerConnected;
import com.memerland.segurity.utils.WebServer;
import com.memerland.segurity.commands.*;
import com.memerland.segurity.daos.UserDao;
import com.memerland.segurity.discord.DiscordUtils;
import com.memerland.segurity.events.ChatsEvents;
import com.memerland.segurity.events.SegurityEvents;
import com.memerland.segurity.model.Coordenadas;
import com.memerland.segurity.mongo.MongoUtils;
import com.memerland.segurity.schedulers.BackupSchedule;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Segurity extends JavaPlugin {
    public static Segurity instance;

    @Override
    public void onEnable() {
        instance = this;
        MongoUtils.createDatabase();
        DiscordUtils.init();
        Config.loadConfig(this);
        addCommads();
        registryEvents();
        try {
            WebServer.startServer();
        } catch (Exception e) {
           getLogger().warning("Error starting web server" + e.getMessage());
        }
        BackupUtils.createFolderBackup();
       

        new BackupSchedule().runTaskTimer(this, 0, 20*3600*2);


    }

    private void registryEvents() {
        getServer().getPluginManager().registerEvents(new SegurityEvents(), this);
        getServer().getPluginManager().registerEvents(new ChatsEvents(), this);
        getLogger().info("Segurity is enabled");
    }

    private void addCommads() {
        getCommand("login").setExecutor(new LoginCommand());
        getCommand("register").setExecutor(new RegisterCommand());
        getCommand("op2").setExecutor(new OpCommand());
        getCommand("spawnSegurity").setExecutor(new SpawnCommand());
        getCommand("olvidar").setExecutor(new OlvidarCommand());
        getCommand("balance").setExecutor(new BalanceCommand());
        getCommand("transferir").setExecutor(new TransferCommad());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        DiscordUtils.disconnected();
        UserDao userDao = new UserDao();
        for (String playerName : PlayerConnected.playersConnected){
            Player player = getServer().getPlayer(playerName);
            userDao.saveLocation(playerName,Coordenadas.fromLocation(player.getLocation()));

        }
        userDao.close();

    }
}
