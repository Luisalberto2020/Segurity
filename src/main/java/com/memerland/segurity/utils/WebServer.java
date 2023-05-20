package com.memerland.segurity.utils;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.memerland.segurity.servlets.HomeServlet;
import com.memerland.segurity.servlets.LoginServlet;

import io.javalin.Javalin;

public class WebServer {
    private static Javalin server;
    private static  ClassLoaderTemplateResolver templateResolver;

public static void startServer() throws Exception {

    Javalin app = Javalin.create();
    app.routes(() -> {
        app.get("/", ctx -> ctx.redirect("/login"));
        app.get("/home", new HomeServlet());
         app.get("/login", new LoginServlet());
        app.post("/login", new LoginServlet());
        /*app.get("/logout", new LogoutServlet());
        app.get("/assets/*", new AssetsServlet()); */
    });

    thymeleaf();
    app.start(80);



}



    private static void thymeleaf() {
        templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);

    }
    public  static TemplateEngine getTemplateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }




}
