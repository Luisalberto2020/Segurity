package com.memerland.segurity.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WrapperProduct {
    private String name;
    @Singular
    private List<Product> products;
    private int price;
    private int sell;
}
