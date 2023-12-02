package com.example.demodatabasepj.exception.transfer;

public class DuplicatedTransferException extends RuntimeException{
    public DuplicatedTransferException(String msg){
        super(msg);
    }
}
