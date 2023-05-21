package com.memerland.segurity.servlets;

import java.util.Optional;

import com.memerland.segurity.Errors.TokenException;
import com.memerland.segurity.daos.UserDao;
import com.memerland.segurity.discord.DiscordUtils;
import com.memerland.segurity.utils.Token;
import com.memerland.segurity.utils.WebUtils;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class CodeDiscordServlet implements Handler {

    @Override
    public void handle(Context ctx) throws Exception {
    
        Optional<Token> token = WebUtils.checkToken(ctx);
        if(token.isPresent()) {
            
            Token token2 = token.get();
            UserDao userDao = new UserDao();
            //TODO SAVE IN DATABASE CODE
            userDao.findByName(token2.getName()).ifPresent(user -> {
                
                    DiscordUtils.sendPrivateMessage(user.getDiscordID(),"El codigo es " +((int)Math.random()*1000) );
                
            });
            userDao.close();

            
        }
        else {
            ctx.redirect("/login");
        }
    }
     
}
