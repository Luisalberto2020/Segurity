package com.memerland.segurity.servlets;

import java.util.Optional;

import org.bukkit.Bukkit;

import org.bukkit.scheduler.BukkitScheduler;

import com.memerland.segurity.Segurity;
import com.memerland.segurity.Errors.EconomyException;
import com.memerland.segurity.Errors.TokenException;
import com.memerland.segurity.daos.UserDao;

import com.memerland.segurity.model.User;
import com.memerland.segurity.model.WrapperProduct;
import com.memerland.segurity.schedulers.AddItemScheduler;
import com.memerland.segurity.utils.PlayerConnected;
import com.memerland.segurity.utils.Token;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class BuyItemServlet implements Handler {

    @Override
    public void handle(Context ctx) throws Exception {
        String token = ctx.header("Bearer");
        if (token == null) {
            ctx.status(401);
            return;
        } else {
            try {
                Token token1 = Token.getToken(token);

                UserDao userDao = new UserDao();
                Optional<User> opUser = userDao.findByName(token1.getName());
                userDao.close();

                if (opUser.isPresent()) {

                    User user = opUser.get();

                    if (PlayerConnected.playersConnected.contains(user.getName())) {

                        String itemName = ctx.formParam("itemName");
                        String quantity = ctx.formParam("quantity");
                        if (itemName == null || quantity == null) {
                            ctx.status(400);
                            return;
                        }

                        int quantityInt = Integer.parseInt(quantity);
                        if(quantityInt <= 0){
                            ctx.status(400);
                            return;
                        }
                        UserDao userDao2 = new UserDao();
                        try {




                           Optional<WrapperProduct> opProduct = userDao2.buy(itemName, quantityInt, user.getName());
                           if(opProduct.isPresent()){
                               
                            new AddItemScheduler(opProduct.get(), token1.getName(), quantityInt).runTaskAsynchronously(Segurity.instance);
                            ctx.status(200);
                           }else{
                               ctx.status(400);
                           }

                           

                            

                        } catch (EconomyException e) {
                            ctx.status(400);
                            ctx.result(e.getMessage());
                            
                        } finally {
                            userDao2.close();
                        }
                    }else{
                        ctx.status(401);
                    }

                }
            } catch (TokenException | NumberFormatException e) {
                ctx.status(401);
            }
        }
    }
}
