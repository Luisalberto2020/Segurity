package com.memerland.segurity.commands;

import com.memerland.segurity.Segurity;
import com.memerland.segurity.utils.Config;
import com.memerland.segurity.utils.ConfigFile;
import com.memerland.segurity.model.Coordenadas;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player) {
            if(player.isOp()){
                Config.spawnLocation = player.getLocation();
                File file = new File(Segurity.instance.getDataFolder(), "config.json");
                File folder = Segurity.instance.getDataFolder();
                if(!folder.exists()){
                    folder.mkdir();
                }
                if(file.exists() && !file.delete()){
                        Segurity.instance.getLogger().warning("No se ha podido borrar el archivo config.json");
                    }

                try(BufferedWriter b =new BufferedWriter (new FileWriter(file))){

                    ConfigFile configFile = ConfigFile.builder()
                            .spawnLocation(Coordenadas.fromLocation(player.getLocation())).build();
                    b.write(configFile.toJson());
                    player.sendMessage(ChatColor.GREEN+"Se ha establecido el spawn correctamente");
                } catch (IOException e) {
                    e.printStackTrace();
                    player.sendMessage(ChatColor.RED+"No se ha podido establecer el spawn");
                }



            }else {
                player.sendMessage(ChatColor.RED+"Solo los adminnistradores pueden ejecutar este comando");
            }
        }else {
            sender.sendMessage(ChatColor.RED+ "Solo los jugadores pueden ejecutar este comando");
        }


        return true;
    }
}
