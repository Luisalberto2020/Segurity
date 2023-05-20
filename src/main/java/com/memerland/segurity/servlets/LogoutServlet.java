package com.memerland.segurity.servlets;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class LogoutServlet implements Handler {

    @Override
    public void handle(Context ctx) throws Exception {
        ctx.removeCookie("token");
        ctx.redirect("/login");
    }
    
}
