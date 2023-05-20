package com.memerland.segurity.servlets;



import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import com.memerland.segurity.Segurity;
import com.memerland.segurity.daos.UserDao;
import com.memerland.segurity.model.User;
import com.memerland.segurity.utils.Token;
import com.memerland.segurity.utils.WebServer;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class LoginServlet implements Handler {

    @Override
    public void handle(Context context) throws Exception {

     
      

        if (context.method().toString().equals("POST")) {
            String username = context.formParam("username");
            String password = context.formParam("password");
            UserDao userDao = new UserDao();
            Optional<User> opuser = userDao.findByNameAndPassword(username, password);
            userDao.close();
            if(opuser.isPresent()){
                User user = opuser.get();
                Token token = new Token(String.valueOf((int)(Math.random()*10000)),user.getName(),user.isOp(), LocalDateTime.now()
                .plus(1, ChronoUnit.HOURS));
                Segurity.instance.getLogger().info("Token generado: "+token.toStringFormat());
                context.cookie("token", token.toStringFormat(), 3600);
                context.redirect("/home");
                
                
            }else {
                org.thymeleaf.context.Context ctx = new org.thymeleaf.context.Context();
                ctx.setVariable("error", "Usuario o contraseña incorrectos");
                context.html(WebServer.getTemplateEngine().process("login", ctx));
            }



               
            
        }else{
            org.thymeleaf.context.Context ctx = new org.thymeleaf.context.Context();
        
        context.html(WebServer.getTemplateEngine().process("login", ctx));
        }

     
       
    }
    
}
