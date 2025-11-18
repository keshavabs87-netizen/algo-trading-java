package com.personal.algo.order.model;

public class KiteSession {
    private String accessToken;
    private String publicToken;

    public KiteSession(String accessToken, String publicToken) {
        this.accessToken = accessToken;
        this.publicToken = publicToken;
    }

    public String getAccessToken() { return accessToken; }
    public String getPublicToken() { return publicToken; }
}
