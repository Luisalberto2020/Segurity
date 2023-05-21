package com.memerland.segurity.servlets;


import java.util.Optional;

import com.memerland.segurity.daos.UserDao;
import com.memerland.segurity.model.User;
import com.memerland.segurity.utils.Token;
import com.memerland.segurity.utils.WebServer;
import com.memerland.segurity.utils.WebUtils;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class ProfileServlet implements Handler {

    @Override
    public void handle(Context ctx) throws Exception {
       Optional<Token> opToken =  WebUtils.checkToken(ctx);

       if(ctx.method().toString().equals("GET")){
       UserDao userDao = new UserDao();
         Optional<User> opUser = userDao.findByName(opToken.get().getName());
        userDao.close();
        if (opUser.isPresent()){
            org.thymeleaf.context.Context thymeleafContext = new org.thymeleaf.context.Context();
            thymeleafContext.setVariable("user",opUser.get() );
               ctx.html(WebServer.getTemplateEngine().process("profile", thymeleafContext));
        }else{
            ctx.removeCookie("token");
            ctx.redirect("/login");
        }
    }else{
        ctx.html("No se puede hacer eso");
    }


       
    }
    
}
