package com.memerland.segurity.servlets;

import java.util.ArrayList;
import java.util.Optional;

import com.memerland.segurity.Segurity;
import com.memerland.segurity.daos.UserDao;
import com.memerland.segurity.model.User;
import com.memerland.segurity.utils.Token;
import com.memerland.segurity.utils.WebServer;
import com.memerland.segurity.utils.WebUtils;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class TransferServlet implements Handler {

    @Override
    public void handle(Context ctx) throws Exception {

        String method = ctx.method().toString();

        Optional<Token> token = WebUtils.checkToken(ctx);
        if (token.isPresent()) {
            org.thymeleaf.context.Context context = new org.thymeleaf.context.Context();
            UserDao userDao = new UserDao();
            Optional<User> opUser = userDao.findByName(token.get().getName());
            ArrayList<String> users = (ArrayList<String>) userDao.getAllUserNames();
            userDao.close();
            context.setVariable("users", users);
            if (opUser.isPresent()) {
                User user = opUser.get();
                context.setVariable("user", user);
            }else {
                ctx.removeCookie("token");
                ctx.redirect("/login");
            }

            if ( method.equals("POST")){
                String username = ctx.formParam("username");
                String money = ctx.formParam("money");
                if (username != null && money != null){
                    UserDao userDao2 = new UserDao();
                    try {
                        userDao2.transferMoney(token.get().getName(),username ,Integer.parseInt(money));
                    }catch (Exception e){
                        context.setVariable("error", "Transferencia fallida");
                        Segurity.instance.getLogger().warning("Error al transferir dinero " + e);
                    }
                  
                   
                    }else{
                        context.setVariable("error", "Transferencia fallida");
                    }
            }


            render(ctx, context);
        } else {
            ctx.removeCookie("token");
            ctx.redirect("/login");
        }

   




    
}

    private void render(Context ctx,org.thymeleaf.context.Context context) {
     

       ctx.html(WebServer.getTemplateEngine().process("transfer", context));

    }
}
