package com.memerland.segurity.events;

import com.memerland.segurity.discord.DiscordUtils;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class ChatsEvents implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        DiscordUtils.sendChatMessage(event.getPlayer().getName(),event.getMessage());

    }
}
