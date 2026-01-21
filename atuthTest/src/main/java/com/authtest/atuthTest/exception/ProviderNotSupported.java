package com.authtest.atuthTest.exception;

public class ProviderNotSupported extends RuntimeException{
    public ProviderNotSupported(String message){
        super(message);
    }
    public ProviderNotSupported(){
        super("The current provided provider is not supported and may be added in future");
    }
}
