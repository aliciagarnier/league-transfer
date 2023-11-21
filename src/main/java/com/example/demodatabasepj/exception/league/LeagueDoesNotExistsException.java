package com.example.demodatabasepj.exception.league;

public class LeagueDoesNotExistsException extends RuntimeException{

    public LeagueDoesNotExistsException(String msg){
        super(msg);
    }
}
