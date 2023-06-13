package com.memerland.segurity.servlets;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.google.gson.Gson;
import com.memerland.segurity.Segurity;
import com.memerland.segurity.daos.WrapperproductDao;
import com.memerland.segurity.model.WrapperProduct;
import com.memerland.segurity.utils.Token;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class AddProductServlet implements Handler {

    @Override
    public void handle(Context ctx) throws Exception {
        

        try{
            String json = new String(Base64.getDecoder().decode( ctx.formParam("data")),StandardCharsets.UTF_8);
            WrapperProduct wrapperProduct = new Gson().fromJson(json, WrapperProduct.class);
            String token = ctx.header("Bearer");
            if (token == null) {
               
               
                ctx.redirect("/admin");
                
            }else {
               
                    Token token2 = Token.getToken(token);
                    if (!token2.isAdmin()) {
                        ctx.redirect("/admin");
                    }else {
                        if (wrapperProduct == null) {
                
                            Segurity.instance.getLogger().warning("Error al añadir el producto");
                            ctx.redirect("/admin");
                            
                        }else{
                            if (!wrapperProduct.getName().equals("") && wrapperProduct.getPrice()> 0 && !wrapperProduct.getProducts().isEmpty()){
                                WrapperproductDao wrapperproductDao = new WrapperproductDao();
                                wrapperproductDao.save(wrapperProduct);
                                wrapperproductDao.close();
                            }
                           
                        }
                    }
              
            }
          
          
           



        }catch(Exception e){
            
            Segurity.instance.getLogger().warning("Error al añadir el producto" + e);
            ctx.redirect("/admin");

        }
        ctx.redirect("/admin");
        
        

     

       
    }
    
}
