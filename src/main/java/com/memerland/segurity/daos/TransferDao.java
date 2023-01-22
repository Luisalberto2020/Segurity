package com.memerland.segurity.daos;

import com.memerland.segurity.model.Transfer;
import com.memerland.segurity.mongo.BasicDao;

public class TransferDao extends BasicDao<Transfer, String> {

    public TransferDao() {
        super("transfers", Transfer.class);
    }


}
