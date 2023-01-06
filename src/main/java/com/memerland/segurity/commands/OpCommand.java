package com.memerland.segurity.commands;

import com.memerland.segurity.daos.UserDao;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class OpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player player) {
            if (player.isOp()) {
                if (args.length == 2) {
                    boolean op = args[1].equals("true");
                    Player target = player.getServer().getPlayer(args[0]);
                    if (target != null) {
                        target.setOp(op);
                        UserDao userDao = new UserDao();
                        userDao.setOp(target.getName(), op);
                        userDao.close();
                        player.sendMessage(ChatColor.GREEN + "Has puesto op a " + ChatColor.GREEN + target.getName());

                    } else {
                        player.sendMessage(ChatColor.RED + "El jugador no esta conectado");
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Solo los jugadores pueden ejecutar este comando");
            }



        }
        return true;
    }
}
