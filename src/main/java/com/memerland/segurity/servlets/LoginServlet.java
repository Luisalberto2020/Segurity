package com.memerland.segurity.servlets;

import com.google.common.hash.Hashing;
import com.memerland.segurity.Errors.TokenException;
import com.memerland.segurity.Segurity;
import com.memerland.segurity.daos.UserDao;
import com.memerland.segurity.model.User;
import com.memerland.segurity.utils.Token;
import com.memerland.segurity.utils.WebServer;
import com.memerland.segurity.webUtils.WebUtiils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Optional<Token> token = WebUtiils.verifyToken(req);
            if(token.isPresent()) {
                resp.sendRedirect("/home");
            }
        } catch (TokenException e) {
           Segurity.instance.getLogger().warning(e.getMessage());
        }

        Context context = new Context();
        resp.getWriter().println(WebServer.getTemplateEngine().process("login", context));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        if (req.getParameter("username") != null && req.getParameter("password") != null) {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            UserDao userDao = new UserDao();
            Optional<User> user = userDao.findByNameAndPassword(username,  Hashing.sha256().hashString(password,
                    StandardCharsets.UTF_8).toString());
            userDao.close();
            if (user.isPresent()){
                Token token = new Token(String.valueOf((int)(Math.random()*10000)),user.get().getName(),user.get().isOp(), LocalDateTime.now()
                        .plus(1, ChronoUnit.HOURS));
                resp.addCookie(new Cookie("token", token.toStringFormat()));
                resp.sendRedirect("/home");
            }else{
              context.setVariable("error", "Usuario o contraseña incorrectos");
            }

        } else {
           context.setVariable("error", "Usuario o contraseña incorrectos");
        }



        resp.getWriter().println(WebServer.getTemplateEngine().process("login", context));
    }
}
