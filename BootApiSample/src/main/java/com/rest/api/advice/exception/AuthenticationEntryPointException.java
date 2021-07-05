package com.rest.api.advice.exception;

public class AuthenticationEntryPointException extends RuntimeException {
    public AuthenticationEntryPointException() { super(); }
    public AuthenticationEntryPointException(String msg) { super(msg); }
    public AuthenticationEntryPointException(String msg, Throwable t) {
        super(msg, t);
    }
}