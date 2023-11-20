package com.example.demodatabasepj.exceptions.club;

public class ClubDoesNotExistsException extends RuntimeException{

    public ClubDoesNotExistsException(String msg){
       super(msg);
    }
}
