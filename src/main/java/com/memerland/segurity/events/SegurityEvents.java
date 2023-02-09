package com.memerland.segurity.events;

import com.memerland.segurity.model.Coordenadas;
import com.memerland.segurity.utils.PlayerConected;
import com.memerland.segurity.utils.PlayerState;
import com.memerland.segurity.daos.UserDao;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;



public class SegurityEvents implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UserDao userDao = new UserDao();
        if (userDao.puedeLogeatde(player.getName())) {
            player.sendMessage(ChatColor.GREEN + "Bienvenido " + ChatColor.YELLOW + player.getName());
            PlayerConected.playersConected.add(player.getName());
            userDao.deleteAcesso(player.getName());

        } else {
            PlayerState.connect(player);
            player.sendMessage(ChatColor.YELLOW+ "Debes logearte para poder jugar");
        }
        userDao.close();

    }
    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UserDao userDao = new UserDao();
        userDao.saveLocation(player.getName(), Coordenadas.fromLocation(player.getLocation()));
        userDao.close();
        PlayerConected.playersConected.remove(player.getName());

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(!PlayerConected.playersConected.contains(player.getName())) {
           event.setCancelled(true);
        }

    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(!PlayerConected.playersConected.contains(player.getName())) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void playerOpenInvetory(InventoryOpenEvent event) {
        HumanEntity player = event.getPlayer();
        if(!PlayerConected.playersConected.contains(player.getName())) {
            event.setCancelled(true);
        }

    }
    @EventHandler
    public void CommandExecution(PlayerCommandPreprocessEvent event){
        Player player = event.getPlayer();
        if(!PlayerConected.playersConected.contains(player.getName())) {
            if(!event.getMessage().startsWith("/login" ) && !event.getMessage().startsWith("/register") && !event.getMessage().startsWith("/olvidar")){
                player.sendMessage(ChatColor.RED + "Debes logearte para poder ejecutar este comando");
                event.setCancelled(true);
            }



        }else {
            if (event.getMessage().startsWith("/login") || event.getMessage().startsWith("/register") || event.getMessage().startsWith("/olvidar")) {
              player.sendMessage(ChatColor.RED + "Ya estas logeado");
                event.setCancelled(true);
            }
        }


    }


}
