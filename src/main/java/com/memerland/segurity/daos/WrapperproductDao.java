package com.memerland.segurity.daos;

import java.util.Optional;
import org.bson.Document;

import com.google.gson.Gson;
import com.memerland.segurity.model.WrapperProduct;
import com.memerland.segurity.mongo.BasicDao;

public class WrapperproductDao extends BasicDao<WrapperProduct,String> {

    public WrapperproductDao( ) {
        super("products", WrapperProduct.class);
        
    }

    public Optional<WrapperProduct> findByName(String itemName) {
        Document doc = database.getCollection(collectionName).find(new Document("name", itemName)).first();
        return Optional.ofNullable(new Gson().fromJson(doc.toJson(), WrapperProduct.class));

        
    }
    
}
