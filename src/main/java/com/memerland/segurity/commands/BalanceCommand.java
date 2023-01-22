package com.memerland.segurity.commands;

import com.memerland.segurity.daos.UserDao;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BalanceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player) {
            UserDao userDao = new UserDao();

            player.sendMessage("Tu balance es de: " + userDao.getMoney(player.getName()));
        }


        return true;
    }
}
