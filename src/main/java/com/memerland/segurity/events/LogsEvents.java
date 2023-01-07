package com.memerland.segurity.events;

import com.memerland.segurity.daos.UserDao;
import com.memerland.segurity.model.Conexion;
import com.memerland.segurity.model.TipoConexion;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.LocalDateTime;

public class LogsEvents implements Listener {

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UserDao userDao = new UserDao();
        Conexion conexion = Conexion.builder()
                .ip(player.getAddress().getAddress().getHostAddress())
                .tipo(TipoConexion.FIN)
                .fecha(LocalDateTime.now())
                .build();
        userDao.addConexion(player.getName(),conexion);



    }
}
