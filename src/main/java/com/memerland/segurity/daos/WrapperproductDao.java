package com.memerland.segurity.daos;

import com.memerland.segurity.model.WrapperProduct;
import com.memerland.segurity.mongo.BasicDao;

public class WrapperproductDao extends BasicDao<WrapperProduct,String> {

    public WrapperproductDao( ) {
        super("products", WrapperProduct.class);
        
    }
    
}
