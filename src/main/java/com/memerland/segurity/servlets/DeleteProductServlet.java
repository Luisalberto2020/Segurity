package com.memerland.segurity.servlets;

import com.google.gson.Gson;
import com.memerland.segurity.daos.WrapperproductDao;
import com.memerland.segurity.model.WrapperDeleteProduct;
import com.memerland.segurity.utils.Token;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class DeleteProductServlet implements Handler {

    @Override
    public void handle(Context ctx) throws Exception {

        String body = ctx.body();
        if (body == null) {
            ctx.status(400);
            ctx.result("Faltan parametros");
        } else {
            Gson gson = new Gson();
            WrapperDeleteProduct wrapperDeleteProduct = gson.fromJson(body, WrapperDeleteProduct.class);
            if (wrapperDeleteProduct == null) {
                ctx.status(400);
                ctx.result("Faltan parametros");
            } else {
                if (wrapperDeleteProduct.getId() == null) {
                    ctx.status(400);
                    ctx.result("Faltan parametros");
                } else {
                   Token token = Token.getToken(wrapperDeleteProduct.getToken());
                   if (token == null) {
                       ctx.status(401);
                       ctx.result("Token invalido");
                   } else {
                    if(token.isAdmin()){
                      WrapperproductDao wrapperproductDao = new WrapperproductDao();
                        wrapperproductDao.deleteByName(wrapperDeleteProduct.getId());
                        wrapperproductDao.close();
                        ctx.status(200);
                    }else {
                        ctx.status(401);
                    }
                   }

                }
            }

        }

    }
}
