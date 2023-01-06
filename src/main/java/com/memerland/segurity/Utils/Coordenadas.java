package com.memerland.segurity.Utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Coordenadas {
    public  double x;
    public  double y;
    public  double z;
    public  String world;

    public Location toLocation(){
        return new Location(Bukkit.getServer().getWorld(world), x, y, z);
    }
    public static Coordenadas fromLocation(Location location){
        return Coordenadas.builder()
                .x(location.getX())
                .y(location.getY())
                .z(location.getZ())
                .world(location.getWorld().getName())
                .build();
    }

}
