package com.memerland.segurity.servlets;

import java.util.Optional;

import org.bukkit.Material;

import com.memerland.segurity.daos.UserDao;
import com.memerland.segurity.utils.Token;
import com.memerland.segurity.utils.WebServer;
import com.memerland.segurity.utils.WebUtils;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class AdminServlet implements Handler {

    @Override
    public void handle(Context ctx) throws Exception {
        Optional<Token> opToken = WebUtils.checkTokenAdmin(ctx);
        UserDao userDao = new UserDao();
        if (opToken.isPresent()) {
            if (opToken.get().isAdmin()) {
                org.thymeleaf.context.Context thymeleafContext = new org.thymeleaf.context.Context();
                userDao.findByName(opToken.get().getName()).ifPresent(user -> {
                    thymeleafContext.setVariable("user", user);
                    thymeleafContext.setVariable("products", Material.values());
                    userDao.close();
                    ctx.html(WebServer.getTemplateEngine().process("admin", thymeleafContext));
                });
            } else {
                ctx.removeCookie("token");
                ctx.redirect("/login");
            }
        } else {
            ctx.removeCookie("token");
            ctx.redirect("/login");
        }
      

      

    }

}
