package com.example.demodatabasepj.exception.club;

public class ClubDoesNotExistsException extends RuntimeException{

    public ClubDoesNotExistsException(String msg){
       super(msg);
    }
}
