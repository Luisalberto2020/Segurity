package com.memerland.segurity.servlets;

import com.memerland.segurity.Segurity;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.Nullable;


import java.io.IOException;
import java.io.InputStream;

public class AssetsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) {
            resp.sendError(404);
            return;
        }
        if(path.contains("..")){
            resp.sendError(404);
            return;
        }
        @Nullable InputStream file = Segurity.instance.getClass().getResourceAsStream("/assets" + path);
        if (file == null) {
            resp.sendError(404);
            return;
        }
        resp.setContentType(getContentType(path));
        file.transferTo(resp.getOutputStream());

    }

    private String getContentType(String path) {

        if(path.endsWith(".css")){
            return "text/css";
        }
        if(path.endsWith(".js")){
            return "text/javascript";
        }
        if(path.endsWith(".png")){
            return "image/png";
        }
        if(path.endsWith(".jpg")){
            return "image/jpg";
        }
        if(path.endsWith(".jpeg")){
            return "image/jpeg";
        }
        if(path.endsWith(".svg")){
            return "image/svg+xml";
        }
        if(path.endsWith(".ico")){
            return "image/x-icon";
        }
        if(path.endsWith(".ttf")){
            return "font/ttf";
        }
        if(path.endsWith(".woff")){
            return "font/woff";
        }
        if(path.endsWith(".woff2")){
            return "font/woff2";
        }
        if(path.endsWith(".eot")){
            return "font/eot";
        }
        if(path.endsWith(".otf")){
            return "font/otf";
        }
        if (path.contains("favicon")) {
            return "image/x-icon";
        }
        return "text/plain";
    }
}
