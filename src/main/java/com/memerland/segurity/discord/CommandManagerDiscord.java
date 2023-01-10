package com.memerland.segurity.discord;

import com.memerland.segurity.Segurity;
import com.memerland.segurity.daos.CodeDao;
import com.memerland.segurity.daos.LogDao;
import com.memerland.segurity.daos.UserDao;
import com.memerland.segurity.model.Code;
import com.memerland.segurity.model.Log;
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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandManagerDiscord extends ListenerAdapter {
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<CommandData> commands = new ArrayList<>();
        OptionData opcion = new OptionData(OptionType.INTEGER, "codigo", "Codigo para verificar", true);
        commands.add(
                Commands.slash("verificar", "Verifica tu cuenta de discord con tu cuenta de minecraft")
                        .addOptions(opcion)
        );
        commands.add(
                Commands.slash("login", "Logueate")

        );
        OptionData opcion2 = new OptionData(OptionType.BOOLEAN, "bloquear", "bloquear", true);
        commands.add(
                Commands.slash("bloquear", "Registrate").addOptions(opcion2)


        );
        OptionData opcion3 = new OptionData(OptionType.STRING, "comando",
                "comando de minecraft a ejecutar", true);
        commands.add(
                Commands.slash("ejecutar", "Ejecuta un comando de minecraft").addOptions(opcion3)
        );
        commands.add(
                Commands.slash("ip", "Da la ip del servidor")
        );


        event.getGuild().updateCommands().addCommands(commands).queue();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        switch (command) {
            case "verificar":
                verificar(event);
                break;
            case "login":
                login(event);
                break;
            case "bloquear":
                block(event);
                break;
            case "ejecutar":
                run(event);
                break;
            case "ip":
                ip(event);
                break;


        }
    }

    private  void ip(SlashCommandInteractionEvent event) {
        try {
            URL url = new URL("https://api.ipify.org");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            String ip = new String(con.getInputStream().readAllBytes());
            event.reply("La ip del servidor es: " + ip).queue();
        } catch (IOException e) {
            Segurity.instance.getLogger().info("Error al obtener la ip");
            event.reply("Error al obtener la ip").queue();
        }

    }

    private  void run(@NotNull SlashCommandInteractionEvent event) {
        UserDao userDao2 = new UserDao();
        try {
            Optional<User> userOptional = userDao2.findByDiscord(event.getUser().getId());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.isOp()) {
                    String comando = event.getOption("comando").getAsString();
                    Bukkit.getScheduler().runTask(Segurity.instance, () -> {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), comando);
                    });
                    LogDao logDao = new LogDao();
                    Log log = Log.builder()
                            .player(user.getName())
                            .command(comando)
                            .date(LocalDateTime.now())
                            .build();
                    logDao.save(log);
                    logDao.close();
                    event.reply("Comando ejecutado").setEphemeral(true).queue();
                } else {
                    event.reply("No eres admin").setEphemeral(true).queue();
                }
            } else {
                event.reply("No se encontro el usuario").setEphemeral(true).queue();
            }
            userDao2.close();
        } catch (Exception e) {
            event.reply("Error al ejecutar el comando").setEphemeral(true).queue();
        }
    }

    private  void block(@NotNull SlashCommandInteractionEvent event) {
        UserDao userDao1 = new UserDao();
        boolean bloquear = event.getOption("bloquear").getAsBoolean();
        try {
            Optional<User> userOptional = userDao1.findByDiscord(event.getUser().getId());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                System.out.println(user);
                if (bloquear) {
                    Bukkit.getScheduler().runTask(Segurity.instance, () -> {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ban " + user.getName() + " bloqueado por discord");
                    });
                    event.reply("Cuenta bloqueada").setEphemeral(true).queue();


                } else {
                    Bukkit.getScheduler().runTask(Segurity.instance, () -> {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pardon " + user.getName());
                    });
                    event.reply("Cuenta desbloqueada").setEphemeral(true).queue();
                }
            }


            userDao1.close();
        } catch (Exception e) {
            event.reply("Error al bloquear").setEphemeral(true).queue();
        }
    }

    private  void login(@NotNull SlashCommandInteractionEvent event) {
        UserDao userDao = new UserDao();
        try {
            userDao.setLoginByDiscord(event.getUser().getId(), LocalDateTime.now());
            event.reply("Logueado correctamente").setEphemeral(true).queue();
            userDao.close();
        } catch (Exception e) {
            event.reply("Error al loguear").setEphemeral(true).queue();
        }
    }

    private  void verificar(@NotNull SlashCommandInteractionEvent event) {
        int code = event.getOption("codigo").getAsInt();
        CodeDao codeDao = new CodeDao();
        Optional<Code> codeOptional = codeDao.findByCode(code);

        if (codeOptional.isPresent()) {
            UserDao userDao = new UserDao();

            if (userDao.setIdDiscord(codeOptional.get().getName(), event.getUser().getId())) {
                codeDao.delete(code);
                event.reply("Cuenta verificada correctamente").setEphemeral(true).queue();

            } else {
                event.reply("No se encontro el usuario").setEphemeral(true).queue();
            }

        } else {
            event.reply("Codigo incorrecto").setEphemeral(true).queue();
        }
        codeDao.close();
    }
}
