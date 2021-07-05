package com.rest.api.advice.exception;

public class EmailSigninFailedException extends RuntimeException {
    public EmailSigninFailedException() { super(); }
    public EmailSigninFailedException(String msg) { super(msg); }
    public EmailSigninFailedException(String msg, Throwable t) {
        super(msg, t);
    }
}