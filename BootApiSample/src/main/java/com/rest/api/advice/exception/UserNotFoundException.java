package com.rest.api.advice.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() { super(); }
    public UserNotFoundException(String msg) { super(msg); }
    public UserNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}