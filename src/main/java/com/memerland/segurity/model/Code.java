package com.memerland.segurity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Code {
    private int _id;
    private String name;

    public Code(String name) {
       this.name = name;
       _id = (int) (Math.random() * 100000);
    }
}
