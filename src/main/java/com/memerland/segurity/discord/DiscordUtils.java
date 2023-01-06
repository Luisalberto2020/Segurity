package com.memerland.segurity.discord;

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class DiscordUtils {
    @Getter
    private static JDA jda;
    private static String token = System.getenv("DISCORD_TOKEN");

    public static  void init() {
        if (token == null) {
            throw new RuntimeException("No token found");
        }
        JDABuilder builder = JDABuilder.createDefault(token);
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

}
