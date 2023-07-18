package com.memerland.segurity.commands;

import com.google.common.hash.Hashing;
import com.memerland.segurity.daos.CodeDao;
import com.memerland.segurity.daos.UserDao;
import com.memerland.segurity.discord.DiscordUtils;
import com.memerland.segurity.model.Code;
import com.memerland.segurity.model.User;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class OlvidarCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player player) {
            if (args.length == 0) {
                UserDao userDao = new UserDao();
                Optional<User> user = userDao.findByName(player.getName());
                userDao.close();
                if (user.isPresent()) {
                    if (user.get().getDiscordID() != null) {
                        Code code = new Code(player.getName());
                        CodeDao codeDao = new CodeDao();
                        codeDao.save(code);
                        codeDao.close();
                        DiscordUtils.sendPrivateMessage(
                                user.get().getDiscordID(),
                                "Has solicitado un cambio de contraseña tu codigo de cambiar la contraseña"
                                        + " es: " + code.get_id());
                    }else {
                        Code code = new Code(player.getName());
                        CodeDao codeDao = new CodeDao();
                        codeDao.save(code);
                        codeDao.close();
                        player.sendMessage(ChatColor.GREEN + "Al no estar registrado en discord el codigo es " +  ChatColor.YELLOW + code.get_id());
                    }
                } else {
                    UserDao userDao1 = new UserDao();
                    if (userDao1.deleteByUserName(player.getName())) {
                        player.sendMessage(
                                ChatColor.GREEN + "Se ha borrado tu cuenta al no estar verificado por favor verifique su cuenta en discord"
                        );
                    } else {
                        player.sendMessage(ChatColor.RED + "No se ha podido borrar tu cuenta");
                    }
                }

            } else {
                if (args.length == 2) {
                    CodeDao codeDao = new CodeDao();
                    Optional<Code> code = Optional.empty();
                    try {
                        code = codeDao.findById(Integer.valueOf(args[0]));
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "El codigo debe ser un numero");
                    }
                    codeDao.close();
                    if (code.isPresent()) {
                        if (code.get().getName().equals(player.getName())) {
                            UserDao userDao = new UserDao();
                            userDao.updatePassword(player.getName(), Hashing.sha256().hashString(args[1],
                                    StandardCharsets.UTF_8).toString());
                            userDao.close();
                            player.sendMessage(ChatColor.GREEN + "Se ha cambiado tu contraseña");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "El codigo no es valido");
                    }
                }
            }
        }

            return true;
        }
    }

