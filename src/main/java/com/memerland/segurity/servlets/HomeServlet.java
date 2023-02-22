package com.memerland.segurity.servlets;

import com.memerland.segurity.Errors.TokenException;

import com.memerland.segurity.Segurity;
import com.memerland.segurity.daos.UserDao;
import com.memerland.segurity.model.User;
import com.memerland.segurity.utils.Token;

import com.memerland.segurity.utils.WebServer;
import com.memerland.segurity.webUtils.WebUtiils;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.Context;

import java.io.IOException;

import java.util.Arrays;
import java.util.Optional;

public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        boolean validToken = false;
        try {
            Optional<Token> token = WebUtiils.verifyToken(req);
            if(token.isPresent()){
                UserDao userDao = new UserDao();
                Optional<User> user = userDao.findByName(token.get().getName());
                userDao.close();
                if(user.isPresent()){
                    validToken = true;
                    context.setVariable("username",user.get().getName());

                    context.setVariable("money",user.get().getMoney());
                }else {
                    Arrays.stream(req.getCookies()).filter(cookie -> cookie.getName().equals("token")).
                            forEach(cookie -> cookie.setMaxAge(0));
                }

                resp.getWriter().println(WebServer.getTemplateEngine().process("home", context));
            }

        } catch (TokenException e) {
            Segurity.instance.getLogger().warning(e.getMessage());
            Arrays.stream(req.getCookies()).filter(cookie -> cookie.getName().equals("token")).forEach(cookie -> cookie.setMaxAge(0));

        }
        if(!validToken){
            resp.sendRedirect("/login");
        }

    }
}
