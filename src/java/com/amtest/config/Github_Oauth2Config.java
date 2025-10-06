package com.amtest.config;

import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * GitHub OAuth2 Configuration
 * @author Theam
 */
@ManagedBean(name = "github_Oauth2Config")
@Component
public class Github_Oauth2Config {
    
    private static final Logger LOGGER = Logger.getLogger(Github_Oauth2Config.class.getName());
    
    @Value("${github.oauth2.clientId}")
    private String clientId;
    
    @Value("${github.oauth2.clientSecret}")
    private String clientSecret;
    
    @Value("${github.oauth2.authorizationUri}")
    private String authorizationUri;
    
    @Value("${github.oauth2.tokenUri}")
    private String tokenUri;
    
    @Value("${github.oauth2.userInfoUri}")
    private String userInfoUri;
    
    @Value("${github.oauth2.userNameAttribute}")
    private String userNameAttribute;
    
    @Value("${github.oauth2.redirectUri}")
    private String redirectUri;
    
    // Constructor
    public Github_Oauth2Config() {
        LOGGER.info("GitHub OAuth2 Config initialized");
        LOGGER.info("Github_Oauth2Config --- start init =============");
    }
    
    // Getters
    public String getClientId() {
        return clientId;
    }
    
    public String getClientSecret() {
        return clientSecret;
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
    
    public String getUserNameAttribute() {
        return userNameAttribute;
    }
    
    public String getRedirectUri() {
        return redirectUri;
    }
    
    // Helper method to get masked client secret for logging
    public String getMaskedClientSecret() {
        if (clientSecret == null || clientSecret.length() < 8) {
            return "****";
        }
        return clientSecret.substring(0, 4) + "****" + clientSecret.substring(clientSecret.length() - 4);
    }
    
    // Validation method
    public boolean isConfigValid() {
        return clientId != null && !clientId.isEmpty() &&
               clientSecret != null && !clientSecret.isEmpty() &&
               authorizationUri != null && !authorizationUri.isEmpty() &&
               tokenUri != null && !tokenUri.isEmpty() &&
               userInfoUri != null && !userInfoUri.isEmpty() &&
               redirectUri != null && !redirectUri.isEmpty();
    }
    
    // Debug information
    public String getDebugInfo() {
        StringBuilder debug = new StringBuilder();
        debug.append("GitHub OAuth2 Configuration:\n");
        debug.append("Client ID: ").append(clientId != null ? clientId.substring(0, Math.min(8, clientId.length())) + "..." : "Not Set").append("\n");
        debug.append("Client Secret: ").append(getMaskedClientSecret()).append("\n");
        debug.append("Authorization URI: ").append(authorizationUri).append("\n");
        debug.append("Token URI: ").append(tokenUri).append("\n");
        debug.append("User Info URI: ").append(userInfoUri).append("\n");
        debug.append("User Name Attribute: ").append(userNameAttribute).append("\n");
        debug.append("Redirect URI: ").append(redirectUri).append("\n");
        debug.append("Config Valid: ").append(isConfigValid() ? "YES" : "NO");
        return debug.toString();
    }
    
    @Override
    public String toString() {
        return "Github_Oauth2Config{" +
                "clientId='" + (clientId != null ? clientId.substring(0, Math.min(8, clientId.length())) + "..." : "null") + '\'' +
                ", authorizationUri='" + authorizationUri + '\'' +
                ", tokenUri='" + tokenUri + '\'' +
                ", userInfoUri='" + userInfoUri + '\'' +
                ", userNameAttribute='" + userNameAttribute + '\'' +
                ", redirectUri='" + redirectUri + '\'' +
                '}';
    }
}