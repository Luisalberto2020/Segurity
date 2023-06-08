package com.memerland.segurity.utils;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.memerland.segurity.Segurity;
import com.memerland.segurity.servlets.HomeServlet;
import com.memerland.segurity.servlets.LoginServlet;
import com.memerland.segurity.servlets.LogoutServlet;
import com.memerland.segurity.servlets.NotFoundServlet;
import com.memerland.segurity.servlets.ProfileServlet;
import com.memerland.segurity.servlets.ProfileUpdateServlet;
import com.memerland.segurity.servlets.TransferServlet;

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
        app.get("/logout", new LogoutServlet());
        app.get("/profile", new ProfileServlet());
        app.post("/profile", new ProfileServlet());
        

        app.post("/actions/profile/update", new ProfileUpdateServlet());


        app.get("/transfer", new TransferServlet());
        app.post("/transfer", new TransferServlet());
        
       
        app.error(404, new NotFoundServlet());
        
    });
    app.updateConfig(config -> {
        config.staticFiles.add(staticFiles -> {
            staticFiles.hostedPath = "/assets";                   // change to host files on a subpath, like '/assets'
            staticFiles.directory = "/assets";              // the directory where your files are located
    
   
        });
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
