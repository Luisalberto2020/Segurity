package com.memerland.segurity.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerConnected {
    public static Set<String> playersConnected = new HashSet<>();
    public static Map<String,Integer> intentosLogin = new HashMap<>();


    public static  int getAvailableSpaces(Inventory inventory) {
        int spacesAvailable = 0;
        
        ItemStack[] inventoryContent = inventory.getContents();
    
        for (ItemStack item : inventoryContent) {
            if (item == null) {
                spacesAvailable++;
            }
        }
    
        return spacesAvailable;
    }
}

