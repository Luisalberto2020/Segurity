package com.memerland.segurity.commands;

import com.google.common.hash.Hashing;
import com.memerland.segurity.daos.CodeDao;
import com.memerland.segurity.daos.UserDao;
import com.memerland.segurity.model.Code;
import com.memerland.segurity.model.User;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public class RegisterCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {


        if (sender instanceof Player player) {
            if (args.length == 2) {

                if (args[0].equals(args[1])) {
                    UserDao userDao = new UserDao();
                    if(userDao.findByName(player.getName()).isEmpty()) {
                        userDao.save(User.builder()
                                .name(player.getName())
                                .password(Hashing.sha256().hashString(args[0], StandardCharsets.UTF_8).toString())
                                .op(false)

                                .build());
                        userDao.close();
                        CodeDao codeDao = new CodeDao();
                        Code code = new Code(player.getName());
                        codeDao.save(code);
                        codeDao.close();
                        player.sendMessage(ChatColor.GREEN + "Registrado correctamente por favor verifique en discord con el código " + ChatColor.YELLOW + code.get_id());
                    } else {
                        player.sendMessage(ChatColor.RED + "Ya estas registrado");
                    }



                } else {
                    player.sendMessage(ChatColor.RED + "No coinciden las contraseñas");
                }

            }else {
                player.sendMessage(ChatColor.RED +"Uso: /register <password>  <password>");
            }
        }else {
            sender.sendMessage(ChatColor.RED + "Solo los jugadores pueden ejecutar este comando");
        }
        return true;
    }
}
