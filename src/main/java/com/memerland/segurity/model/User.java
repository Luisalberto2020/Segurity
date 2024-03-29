package com.memerland.segurity.model;

import lombok.*;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private String name;
    private String password;
    private String discordID;
    private int money;
    private boolean op;
    private Coordenadas location;
    @Singular
    private List<Sale> sales;
    @Singular
    private List<Transfer> transfers;


}
