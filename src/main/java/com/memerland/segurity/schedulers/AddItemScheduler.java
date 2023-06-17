package com.memerland.segurity.schedulers;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.memerland.segurity.Segurity;
import com.memerland.segurity.daos.UserDao;
import com.memerland.segurity.model.Product;
import com.memerland.segurity.model.WrapperProduct;
import com.memerland.segurity.utils.PlayerConnected;

import lombok.Getter;



public class AddItemScheduler extends BukkitRunnable {

    @Getter
    private WrapperProduct product;

    @Getter
    private String player;

    @Getter
    private int amount;

    public AddItemScheduler(WrapperProduct product, String player, int amount) {
        this.product = product;
        this.player = player;
        this.amount = amount;
    }


    @Override
    public void run() {
    
        Inventory inventory = Bukkit.getPlayer(player).getInventory();
        if (PlayerConnected.getAvailableSpaces(inventory) >= product.getProducts().size() * amount) {
          
          for (int i = 0; i < amount; i++) {
            for (Product item : product.getProducts()) {

               ItemStack itemStack = new ItemStack( Material.values()[Integer.valueOf(item.getId())], item.getQuantity());
                inventory.addItem(itemStack);
            }
          }

        } else {
            Bukkit.getPlayer(player).sendMessage(Color.RED + "No tienes espacio en el inventario para comprar " + amount + " " + product.getName() + "s");
            UserDao userDao = new UserDao();
            try{
                
                userDao.addMoney(player, product.getPrice() * amount);
               
            }catch(Exception e){
                Segurity.instance.getLogger().
                warning("Error al devolver el dinero de " 
                + player + " por falta de espacio en el inventario de " + product.getPrice() * amount);
            }finally{
                userDao.close();
            } 
        }
    }
    
}
