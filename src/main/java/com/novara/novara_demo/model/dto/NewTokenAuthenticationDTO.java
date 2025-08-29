package com.novara.novara_demo.model.dto;


public class NewTokenAuthenticationDTO {

    private String refreshToken;
    private String jwt;

    public NewTokenAuthenticationDTO() {
    }

    public NewTokenAuthenticationDTO(String refreshToken, String jwt) {
        this.refreshToken = refreshToken;
        this.jwt = jwt;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public NewTokenAuthenticationDTO setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getJwt() {
        return jwt;
    }

    public NewTokenAuthenticationDTO setJwt(String jwt) {
        this.jwt = jwt;
        return this;
    }
}
