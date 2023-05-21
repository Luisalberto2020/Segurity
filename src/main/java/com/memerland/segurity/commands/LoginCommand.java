package com.memerland.segurity.commands;

import com.google.common.hash.Hashing;
import com.memerland.segurity.Segurity;
import com.memerland.segurity.utils.PlayerConected;
import com.memerland.segurity.utils.PlayerState;
import com.memerland.segurity.daos.UserDao;
import com.memerland.segurity.model.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


import java.util.Optional;

public class LoginCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player player) {
            if (args.length == 1) {
                UserDao userDao = new UserDao();
                Optional<User> user = userDao.findByNameAndPassword(player.getName(),args[0]);
                userDao.close();
                if(user.isPresent()) {
                    if (user.get().getDiscordID() != null) {
                        PlayerState.login(player,user.get());
                        PlayerConected.playersConected.add(player.getName());
                        Segurity.instance.getLogger().info(  "<"+player.getName() + "> se ha conectado correctamente");

                        player.sendMessage(ChatColor.GREEN + "Bienvenido " + ChatColor.YELLOW + player.getName());
                        PlayerConected.intentosLogin.remove(player.getName());
                    }else {
                        player.sendMessage(ChatColor.RED + "No estas verificado en discord");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Contraseña incorrecta");
                    PlayerConected.intentosLogin.put(player.getName(), PlayerConected.intentosLogin.getOrDefault(player.getName(), 0) + 1);
                    Segurity.instance.getLogger().warning(  "<"+player.getName() + "> ha intentado conectarse con contraseña incorrecta");
                    if (PlayerConected.intentosLogin.get(player.getName()) ==  3) {
                        player.sendMessage(ChatColor.RED + "Has intentado conectarte 3 veces con contraseña incorrecta si lo vuelvbes a intentar 2 veces mas seras baneado por ip");
                        Segurity.instance.getLogger().warning(  "<"+player.getName() + "> ha intentado conectarse 3 veces con contraseña incorrecta");
                    }
                    if (PlayerConected.intentosLogin.get(player.getName()) >=  5) {
                       Segurity.instance.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ban " +player.getName() + " baneado por intentos de login fallidos");
                        Segurity.instance.getLogger().warning(  "<"+player.getName() + "> ha sido baneado por intentar conectarse con contraseña incorrecta");
                    }
                }

            } else {
                sender.sendMessage(ChatColor.RED+"Uso: /login  <password>");
            }
        }

        return true;
    }
}
