package com.memerland.segurity.utils;


import com.google.common.reflect.ClassPath;
import com.memerland.segurity.Segurity;
import com.memerland.segurity.servlets.*;

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
    HandlerList handlers = new HandlerList();
    ResourceHandler resourceHandler = new ResourceHandler();

    resourceHandler.setDirectoriesListed(false);
    resourceHandler.setWelcomeFiles(new String[]{"index.html"});
    resourceHandler.setResourceBase(Segurity.instance.getClass().getResource("/assets").toString());
    ContextHandler staticContextHandler = new ContextHandler();
    staticContextHandler.setContextPath("/assets");
    staticContextHandler.setHandler(resourceHandler);
    handlers.addHandler(staticContextHandler);
    handlers.addHandler(addPages());
    server.setHandler(handlers);
    tymeleaf();
    server.start();



}

    private static ServletContextHandler addPages() {

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(new ServletHolder(new LoginServlet()), "");
        context.addServlet(new ServletHolder(new HomeServlet()), "/home");
        //context.addServlet(new ServletHolder(new AssetsServlet()), "/assets/*");
        context.addServlet(new ServletHolder(new LogoutServlet()), "/logout");




        return context;
    }

    private static void tymeleaf() {
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
