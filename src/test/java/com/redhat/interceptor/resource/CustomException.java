package com.redhat.interceptor.resource;

public class CustomException extends RuntimeException {

    public CustomException() {
        super("This is a custom Exception");
    }
}
