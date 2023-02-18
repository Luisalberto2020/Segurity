package com.memerland.segurity.utils;


import com.memerland.segurity.Segurity;
import com.memerland.segurity.servlets.HomeServlet;
import com.memerland.segurity.servlets.LoginServlet;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

import org.eclipse.jetty.servlet.ServletHolder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;



public class WebServer {
    private static Server server;
    private static  ClassLoaderTemplateResolver templateResolver;

public static void startServer() throws Exception {

    Server server = new Server(80);
    ForwardedRequestCustomizer customizer = new ForwardedRequestCustomizer();
    customizer.setForwardedHeader("X-Forwarded-For");
    customizer.setForwardedForHeader("X-Forwarded-For");
    customizer.setForwardedProtoHeader("X-Forwarded-Proto");
    customizer.setForwardedHostHeader("X-Forwarded-Host");
    customizer.setForwardedServerHeader("X-Forwarded-Server");

    HttpConfiguration config = new HttpConfiguration();
    config.addCustomizer(customizer);
    ServerConnector connector = new ServerConnector(server, new HttpConnectionFactory(config));
    connector.setPort(80);


    server.setConnectors(new Connector[]{connector});
    server.setHandler(addPages());
    tymeleaf();
    server.start();



}

    private static HandlerList addPages() {
        HandlerList handlers = new HandlerList();
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{"index.html"});
        resourceHandler.setResourceBase(Segurity.instance.getClass().getClassLoader().getResource("assets").toString());
        resourceHandler.setCacheControl("max-age=3600,public");

        ContextHandler contextHandler = new ContextHandler();
        contextHandler.setContextPath("/assets");
        contextHandler.setHandler(resourceHandler);


        context.setContextPath("/login");
        context.addServlet(new ServletHolder(new LoginServlet()), "");

        ServletContextHandler homeContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        homeContext.setContextPath("/home");
        homeContext.addServlet(new ServletHolder(new HomeServlet()), "");

        handlers.addHandler(context);
        handlers.addHandler(contextHandler);
        handlers.addHandler(homeContext);

        return handlers;
    }

    private static void tymeleaf() {
        templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);

    }
    public  static TemplateEngine getTemplateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }




}
