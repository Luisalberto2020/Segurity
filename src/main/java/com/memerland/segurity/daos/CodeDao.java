package com.memerland.segurity.daos;

import com.memerland.segurity.model.Code;
import com.memerland.segurity.mongo.BasicDao;

import java.util.Optional;

public class CodeDao extends BasicDao<Code,Integer> {

    public CodeDao() {
        super("codes", Code.class);
    }


    public Optional<Code> findByCode(int code) {
        return findById(code);
    }
}
