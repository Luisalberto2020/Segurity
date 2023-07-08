package com.memerland.segurity.servlets;

import com.memerland.segurity.utils.WebServer;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class NotFoundServlet implements Handler {

    @Override
    public void handle(Context ctx) throws Exception {
        ctx.status(404);
       ctx.html(WebServer.getTemplateEngine().process("404", new org.thymeleaf.context.Context()));
    }
    
}
