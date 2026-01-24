package com.authtest.atuthTest.exception;

public class ResourceNotFound extends RuntimeException{

    public ResourceNotFound(String message){
        super(message);
    }
    public ResourceNotFound(){
        this("Resource Not found!!!");
    }
}
