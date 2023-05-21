package com.memerland.segurity.servlets;

import com.memerland.segurity.Segurity;
import com.memerland.segurity.Errors.TokenException;
import com.memerland.segurity.daos.UserDao;
import com.memerland.segurity.utils.Token;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class ProfileUpdateServlet implements Handler {

    @Override
    public void handle(Context ctx) throws Exception {
        String username = ctx.formParam("username");
        String discord = ctx.formParam("discord");
        String token = ctx.cookie("token");
       

        if (username == null || discord == null) {

            ctx.status(400);

            ctx.result("Faltan parametros");
        } else {
            try {
                Token token2 = Token.getToken(token);
                UserDao userDao = new UserDao();
                userDao.updateProfile(token2.getName(), username, discord);
                userDao.close();
                ctx.status(200);

                ctx.redirect("/profile");

            } catch (TokenException e) {
                ctx.status(401);
                ctx.result("Token invalido");
            }

        }
    }

}
