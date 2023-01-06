package com.memerland.segurity.model;

import com.memerland.segurity.Utils.Coordenadas;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private String name;
    private String password;
    private String discordID;
    private boolean op;
    private Coordenadas location;
}
