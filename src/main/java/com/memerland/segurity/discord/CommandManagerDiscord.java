package com.memerland.segurity.discord;

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
import org.jetbrains.annotations.NotNull;

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


       }
    }
}
