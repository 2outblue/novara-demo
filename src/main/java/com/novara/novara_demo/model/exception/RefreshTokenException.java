package com.novara.novara_demo.model.exception;

public class RefreshTokenException extends RuntimeException{

    public RefreshTokenException() {
        super("Invalid refresh token.");
    }
    public RefreshTokenException(String message) {
        super(message);
    }
}
