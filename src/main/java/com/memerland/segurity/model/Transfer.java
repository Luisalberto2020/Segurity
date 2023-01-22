package com.memerland.segurity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transfer {
    private String  payer;
    private String  receiver;
    private int amount;
    private LocalDateTime date;

    public Transfer(String pagador, String recibidor, int money) {
        this.payer = pagador;
        this.receiver = recibidor;
        this.amount = money;
        this.date = LocalDateTime.now();
    }
}
