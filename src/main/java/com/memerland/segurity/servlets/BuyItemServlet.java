package com.memerland.segurity.servlets;

import java.util.Optional;

import org.bukkit.Bukkit;

import org.bukkit.scheduler.BukkitScheduler;


import com.memerland.segurity.Errors.EconomyException;
import com.memerland.segurity.Errors.TokenException;
import com.memerland.segurity.daos.UserDao;

import com.memerland.segurity.model.User;

import com.memerland.segurity.utils.PlayerConected;
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

                    if (PlayerConected.playersConected.contains(user.getName())) {

                        String itemName = ctx.pathParam("item");
                        String quantity = ctx.pathParam("quantity");
                        if (itemName == null || quantity == null) {
                            ctx.status(400);
                            return;
                        }

                        int quantityInt = Integer.parseInt(quantity);
                        UserDao userDao2 = new UserDao();
                        try {
                            userDao2.buy(itemName, quantityInt, user.getName());
                            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

                           

                            ctx.status(200);

                        } catch (EconomyException e) {
                            ctx.status(400);
                            ctx.result(e.getMessage());
                            return;
                        } finally {
                            userDao2.close();
                        }
                    }

                }
            } catch (TokenException | NumberFormatException e) {
                ctx.status(401);
            }
        }
    }
}
