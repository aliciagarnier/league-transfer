package com.example.demodatabasepj.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Club {

    private int ID_club;
    private String name;
    private String stadium;
    private double mv;


    public Club(String name){
        this.name = name;
    }

}
