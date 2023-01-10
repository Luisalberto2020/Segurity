package com.memerland.segurity.discord;

import com.memerland.segurity.Segurity;
import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class DiscordUtils {
    @Getter
    private static JDA jda;
    private static  final String LOGS_CHANNEL_ID = System.getenv("LOGS_CHANNEL_ID");
    public static  final String CHAT_MC_ID = System.getenv("CHAT_MC_ID");
    public static final  String TOKEN = System.getenv("DISCORD_TOKEN");

    public static  void init() {
        if (TOKEN == null) {
            throw new RuntimeException("No token found");
        }
        JDABuilder builder = JDABuilder.createDefault(TOKEN);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.addEventListeners(new CommandManagerDiscord());
        builder.enableIntents(GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MESSAGES,GatewayIntent.MESSAGE_CONTENT
                ,GatewayIntent.GUILD_MEMBERS);

        jda = builder.build();

    }

    public static boolean sendPrivateMessage(String id, String message) {
        try {
            jda.retrieveUserById(id).queue(user -> user.openPrivateChannel().queue(privateChannel ->
                    privateChannel.sendMessage(message).queue()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static void disconnected() {
        jda.shutdown();
    }

    public static void sendLogMessage(String message) {
        try {
            jda.getTextChannelById(LOGS_CHANNEL_ID).sendMessage(message).queue();
        } catch (Exception e) {
            Segurity.instance.getLogger().warning("Error al mandar el logs puede ser que no este activo todavia el bot");
            Segurity.instance.getLogger().warning(e.getMessage());

        }
    }
    public static  void sendChatMessage(String player,String message) {
        try {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Mensaje de "+player);
            embedBuilder.setDescription(message);
           jda.getTextChannelById(CHAT_MC_ID).sendMessageEmbeds(embedBuilder.build()).queue();


        } catch (Exception e) {
            Segurity.instance.getLogger().warning("Error al mandar el chat puede ser que no este activo todavia el bot");
            Segurity.instance.getLogger().warning(e.getMessage());

        }
    }

}
