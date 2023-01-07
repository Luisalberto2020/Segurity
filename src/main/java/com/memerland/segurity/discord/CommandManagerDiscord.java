package com.memerland.segurity.discord;

import com.memerland.segurity.Segurity;
import com.memerland.segurity.daos.CodeDao;
import com.memerland.segurity.daos.UserDao;
import com.memerland.segurity.model.Code;
import com.memerland.segurity.model.User;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandManagerDiscord extends ListenerAdapter {
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<CommandData> comandos = new ArrayList<>();
        OptionData opcion = new OptionData(OptionType.STRING, "codigo", "Codigo para verificar", true);
        comandos.add(
                Commands.slash("verificar", "Verifica tu cuenta de discord con tu cuenta de minecraft")
                        .addOptions(opcion)
        );
        comandos.add(
                Commands.slash("login", "Logueate")

        );
        OptionData opcion2 = new OptionData(OptionType.BOOLEAN, "bloquear", "bloquear",true );
        comandos.add(
                Commands.slash("bloquear", "Registrate").addOptions(opcion2)


        );
        event.getGuild().updateCommands().addCommands(comandos).queue();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
       String command = event.getName();
       switch (command){
              case "verificar":
                  try {
                      int code = Integer.valueOf(event.getOption("codigo").getAsString());
                      CodeDao codeDao = new CodeDao();
                      Optional<Code> codeOptional = codeDao.findByCode(code);

                        if(codeOptional.isPresent()){
                            UserDao userDao = new UserDao();

                            if(userDao.setIdDiscord(codeOptional.get().getName(),event.getUser().getId())){
                                codeDao.delete(code);
                                event.reply("Cuenta verificada correctamente").setEphemeral(true).queue();

                            }else {
                                event.reply("No se encontro el usuario").setEphemeral(true).queue();
                            }

                        }else {
                            event.reply("Codigo incorrecto").setEphemeral(true).queue();
                        }
                      codeDao.close();
                  } catch (NumberFormatException e) {
                     event.reply("El codigo debe ser un numero").setEphemeral(true).queue();
                  }

                break;
                  case "login":
                      UserDao userDao = new UserDao();
                      try {
                          userDao.setLoginByDiscord(event.getUser().getId(), LocalDateTime.now());
                          event.reply("Logueado correctamente").setEphemeral(true).queue();
                          userDao.close();
                      } catch (Exception e) {
                            event.reply("Error al loguear").setEphemeral(true).queue();
                      }
                      break;
                      case "bloquear":
                          UserDao userDao1 = new UserDao();
                          boolean bloquear = event.getOption("bloquear").getAsBoolean();
                          try {
                                Optional<User> userOptional = userDao1.findByDiscord(event.getUser().getId());
                                if(userOptional.isPresent()){
                                    User user = userOptional.get();
                                    System.out.println(user);
                                    if(bloquear){
                                        Bukkit.getScheduler().runTask(Segurity.instance, () -> {
                                          Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"ban "+user.getName()+" bloqueado por discord");
                                        });
                                        event.reply("Cuenta bloqueada").setEphemeral(true).queue();


                                    }else {
                                        Bukkit.getScheduler().runTask(Segurity.instance, () -> {
                                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"pardon "+user.getName());
                                        });
                                        event.reply("Cuenta desbloqueada").setEphemeral(true).queue();
                                    }
                                }



                              userDao1.close();
                          } catch (Exception e) {
                                event.reply("Error al bloquear").setEphemeral(true).queue();
                          }
                          break;


       }
    }
}
