package com.blog.app.model.payload.auth;

import org.springframework.beans.factory.annotation.Value;

import java.util.StringJoiner;

public class TokenResponse {
    private String accessToken;
    private String tokenType = "Bearer";


    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", TokenResponse.class.getSimpleName() + "[", "]")
                .add("accessToken='" + accessToken + "'")
                .add("tokenType='" + tokenType + "'")
                .toString();
    }
}
