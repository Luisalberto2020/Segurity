package com.memerland.segurity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transfer {
    private String  payer;
    private String  receiver;
    private int amount;
    private LocalDateTime date;

    public Transfer(String payer, String receiver, int money) {
        this.payer = payer;
        this.receiver = receiver;
        this.amount = money;
        this.date = LocalDateTime.now();
    }
    public String getDateSpanishFormat(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", new Locale("es", "ES"));
        return date.format(formatter);
       
    }
}
