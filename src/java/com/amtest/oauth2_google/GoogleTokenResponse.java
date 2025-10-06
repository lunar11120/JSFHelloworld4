// filepath: e:\project1\JSFHelloworld4\src\java\com\amtest\oauth2_google\GoogleTokenResponse.java
package com.amtest.oauth2_google;

import com.google.gson.annotations.SerializedName;

public class GoogleTokenResponse {
    @SerializedName("access_token")
    private String accessToken;
    
    @SerializedName("expires_in")
    private int expiresIn;
    
    @SerializedName("refresh_token")
    private String refreshToken;
    
    @SerializedName("scope")
    private String scope;
    
    @SerializedName("token_type")
    private String tokenType;
    
    @SerializedName("id_token")
    private String idToken;
    
    // Default constructor
    public GoogleTokenResponse() {}
    
    // Getters and Setters
    public String getAccessToken() {
        return accessToken;
    }
    
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    
    public int getExpiresIn() {
        return expiresIn;
    }
    
    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
    
    public String getRefreshToken() {
        return refreshToken;
    }
    
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    public String getScope() {
        return scope;
    }
    
    public void setScope(String scope) {
        this.scope = scope;
    }
    
    public String getTokenType() {
        return tokenType;
    }
    
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
    
    public String getIdToken() {
        return idToken;
    }
    
    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
    
    @Override
    public String toString() {
        return "GoogleTokenResponse{" +
                "accessToken='" + accessToken + '\'' +
                ", expiresIn=" + expiresIn +
                ", refreshToken='" + refreshToken + '\'' +
                ", scope='" + scope + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", idToken='" + idToken + '\'' +
                '}';
    }
}