package com.memerland.segurity.Utils;

import com.memerland.segurity.model.User;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerState {
    public static void connect(Player player){
        player.setGameMode(GameMode.SPECTATOR);
        if (Config.spawnLocation != null) {
            player.teleport(Config.spawnLocation);
        }
        player.setOp(false);
        PotionEffect effect = new PotionEffect(PotionEffectType.BLINDNESS,Integer.MAX_VALUE, 255);
        player.addPotionEffect(effect);

    }
    public static void login(Player player, User user){
        player.setGameMode(GameMode.SURVIVAL);
        if(user.getLocation() != null){
            player.teleport(user.getLocation().toLocation());
        }
        player.setOp(user.isOp());
        player.removePotionEffect(PotionEffectType.BLINDNESS);


    }
}
