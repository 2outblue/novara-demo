package com.novara.novara_demo.model.exception;

public class ExpiredRefreshTokenException extends RefreshTokenException{
    public ExpiredRefreshTokenException() {
        super("Expired refresh token.");
    }
    public ExpiredRefreshTokenException(String message) {
        super(message);
    }
}
