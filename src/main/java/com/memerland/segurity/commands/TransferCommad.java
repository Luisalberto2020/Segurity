package com.memerland.segurity.commands;

import com.memerland.segurity.Errors.EconomyException;
import com.memerland.segurity.Segurity;
import com.memerland.segurity.daos.UserDao;
import com.memerland.segurity.model.User;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TransferCommad implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player){
            if (args.length == 2){
                UserDao userDao = new UserDao();
                Optional<User> userOptional = userDao.findByName(args[0]);
                if (userOptional.isPresent()){
                    try {
                        userDao.transferMoney(player.getName(), args[0], Integer.parseInt(args[1]));
                        player.sendMessage(ChatColor.GREEN + "Transferencia realizada con exito");
                    } catch (EconomyException e) {
                        player.sendMessage(ChatColor.RED + e.getMessage());
                        Segurity.instance.getLogger().warning( "//"+e.getMessage());
                    }catch (NumberFormatException e){
                        player.sendMessage(ChatColor.RED + "El valor a transferir debe ser un numero");
                    }
                    userDao.close();
                }



                }



        }else {
            sender.sendMessage("Solo los jugadores pueden ejecutar este comando");
        }
        return true;
    }
}
