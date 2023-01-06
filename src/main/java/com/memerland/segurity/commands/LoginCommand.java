package com.memerland.segurity.commands;

import com.google.common.hash.Hashing;
import com.memerland.segurity.Utils.PlayerConected;
import com.memerland.segurity.Utils.PlayerState;
import com.memerland.segurity.daos.UserDao;
import com.memerland.segurity.model.User;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class LoginCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player player) {
            if (args.length == 1) {
                UserDao userDao = new UserDao();
                Optional<User> user = userDao.findByNameAndPassword(
                        player.getName(), Hashing.sha256().hashString(args[0], StandardCharsets.UTF_8).toString()
                );
                userDao.close();
                if(user.isPresent()) {
                    if (user.get().getDiscordID() != null) {
                        PlayerState.login(player,user.get());
                        PlayerConected.playersConected.add(player.getName());
                        player.sendMessage(ChatColor.GREEN + "Bienvenido " + ChatColor.YELLOW + player.getName());
                    }else {
                        player.sendMessage(ChatColor.RED + "No estas verificado en discord");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Contraseña incorrecta");
                }

            } else {
                sender.sendMessage(ChatColor.RED+"Uso: /login  <password>");
            }
        }

        return true;
    }
}
