package com.memerland.segurity.servlets;

import java.util.Optional;

import com.memerland.segurity.daos.UserDao;
import com.memerland.segurity.daos.WrapperproductDao;
import com.memerland.segurity.utils.Token;
import com.memerland.segurity.utils.WebServer;
import com.memerland.segurity.utils.WebUtils;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class ShopServlet implements Handler{

    @Override
    public void handle(Context ctx) throws Exception {
     Optional<Token> token = WebUtils.checkToken(ctx);
    if (token.isPresent()) {
        Token token2 = token.get();
        UserDao userDao = new UserDao();
        userDao.findByName(token2.getName()).ifPresent(user -> {
            org.thymeleaf.context.Context thymeleafContext = new org.thymeleaf.context.Context();
            thymeleafContext.setVariable("user", user);
            userDao.close();

            WrapperproductDao wrapperproductDao = new WrapperproductDao();
            thymeleafContext.setVariable("products", wrapperproductDao.findAll());
            wrapperproductDao.close();
            ctx.html(WebServer.getTemplateEngine().process("shop", thymeleafContext));
            
           
        });
    } else {
        ctx.redirect("/login"); 

     
    }
}
    
}
