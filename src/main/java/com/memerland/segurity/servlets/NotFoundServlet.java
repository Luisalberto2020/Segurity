package com.memerland.segurity.servlets;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class NotFoundServlet implements Handler {

    @Override
    public void handle(Context ctx) throws Exception {
        ctx.status(404);
        ctx.render("/assets/html/404.html");
    }
    
}
