package com.osvaldo.exception;

public class CustomizedException extends Exception {
    private String message;


    public CustomizedException() {
        super();
    }

    public CustomizedException(String message) {
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
