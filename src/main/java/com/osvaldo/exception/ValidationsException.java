package com.osvaldo.exception;

public class ValidationsException extends Exception {
    private String message;

    public ValidationsException() {
        super();
    }

    public ValidationsException(String message) {
        super();
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
