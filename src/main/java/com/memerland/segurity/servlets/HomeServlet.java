package com.memerland.segurity.servlets;

import java.util.Optional;

import com.memerland.segurity.daos.UserDao;
import com.memerland.segurity.model.User;
import com.memerland.segurity.utils.Token;
import com.memerland.segurity.utils.WebServer;
import com.memerland.segurity.utils.WebUtils;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class HomeServlet implements Handler {

    @Override
    public void handle(Context context) throws Exception {
        Optional<Token> token = WebUtils.checkToken(context);
        if (token.isPresent()) {
            UserDao userDao = new UserDao();
            Optional<User> opUser = userDao.findByName(token.get().getName());
            userDao.close();
            if (opUser.isPresent()) {
                User user = opUser.get();
                org.thymeleaf.context.Context ctx = new org.thymeleaf.context.Context();
                ctx.setVariable("user", user);
                context.html(WebServer.getTemplateEngine().process("home", ctx));

            }else {
                context.removeCookie("token");
                context.redirect("/login");
            }

        }
    }

}
