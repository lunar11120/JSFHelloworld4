/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsf4.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Google OAuth2 Configuration
 * @author Theam
 */
@Component("googleOauth2Config")
public class GoogleOauth2Config {
    
    private static final Logger LOGGER = Logger.getLogger(GoogleOauth2Config.class.getName());
    
    @Value("${google.oauth2.client.id}")
    private String clientId;
    
    @Value("${google.oauth2.client.secret}")
    private String clientSecret;
    
    @Value("${google.oauth2.redirect.uri}")
    private String redirectUri;
    
    @Value("${google.oauth2.authorization.uri}")
    private String authorizationUri;
    
    @Value("${google.oauth2.token.uri}")
    private String tokenUri;
    
    @Value("${google.oauth2.user.info.uri}")
    private String userInfoUri;
    
    @Value("${google.oauth2.scope:openid email profile}")
    private String scope;
    
    @Value("${google.oauth2.response.type:code}")
    private String responseType;
    
    private String debugInfo;
    
    @PostConstruct
    public void init() {
        LOGGER.log(Level.INFO, "GoogleOauth2Config initialized");
        generateDebugInfo();
    }
    
    private void generateDebugInfo() {
        StringBuilder debug = new StringBuilder();
        debug.append("Google OAuth2 Configuration Debug:\n");
        debug.append("=====================================\n");
        debug.append("Client ID: ").append(maskSensitiveData(clientId)).append("\n");
        debug.append("Client Secret: ").append(maskSensitiveData(clientSecret)).append("\n");
        debug.append("Redirect URI: ").append(redirectUri).append("\n");
        debug.append("Authorization URI: ").append(authorizationUri).append("\n");
        debug.append("Token URI: ").append(tokenUri).append("\n");
        debug.append("User Info URI: ").append(userInfoUri).append("\n");
        debug.append("Scope: ").append(scope).append("\n");
        debug.append("Response Type: ").append(responseType).append("\n");
        debug.append("=====================================\n");
        debug.append("Configuration Status: ").append(isConfigured() ? "READY ✓" : "INCOMPLETE ✗").append("\n");
        debug.append("Loaded at: ").append(new java.util.Date().toString());
        
        this.debugInfo = debug.toString();
        
        LOGGER.log(Level.INFO, "Google OAuth2 Config Debug Info Generated");
    }
    
    private String maskSensitiveData(String data) {
        if (data == null || data.length() <= 8) {
            return "****";
        }
        return data.substring(0, 4) + "****" + data.substring(data.length() - 4);
    }
    
    // Getters
    public String getClientId() {
        return clientId;
    }
    
    public String getClientSecret() {
        return clientSecret;
    }
    
    public String getRedirectUri() {
        return redirectUri;
    }
    
    public String getAuthorizationUri() {
        return authorizationUri;
    }
    
    public String getTokenUri() {
        return tokenUri;
    }
    
    public String getUserInfoUri() {
        return userInfoUri;
    }
    
    public String getScope() {
        return scope;
    }
    
    public String getResponseType() {
        return responseType;
    }
    
    public String getDebugInfo() {
        return debugInfo;
    }
    
    public boolean isConfigured() {
        return clientId != null && !clientId.isEmpty() &&
               clientSecret != null && !clientSecret.isEmpty() &&
               redirectUri != null && !redirectUri.isEmpty();
    }
}
