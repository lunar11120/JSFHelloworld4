package com.jsf4.controller;


import com.jsf4.config.GoogleOauth2Config;
import javax.annotation.PostConstruct;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
@ManagedBean(name = "indexController", eager = true)
@RequestScoped
public class IndexController {
    
    private static final Logger LOGGER = Logger.getLogger(IndexController.class.getName());
    
    @Autowired
    private GoogleOauth2Config googleOauth2Config;
    
    private String welcomeMessage = "Welcome to Maven TestJSF22 Application!";
    private String oauthDebugInfo;
    private boolean showDebug = false;
    
    @PostConstruct
    public void init() {
        LOGGER.log(Level.INFO, "IndexController initialized");
        loadOAuthDebugInfo();
    }
    
    private void loadOAuthDebugInfo() {
        if (googleOauth2Config != null) {
            oauthDebugInfo = googleOauth2Config.getDebugInfo();
            LOGGER.log(Level.INFO, "OAuth Debug Info loaded in IndexController");
        } else {
            oauthDebugInfo = "GoogleOauth2Config not available";
            LOGGER.log(Level.WARNING, "GoogleOauth2Config is null");
        }
    }
    
    public String getWelcomeMessage() {
        return welcomeMessage;
    }
    
    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }
    
    public String getOauthDebugInfo() {
        return oauthDebugInfo;
    }
    
    public boolean isShowDebug() {
        return showDebug;
    }
    
    public void toggleDebug() {
        showDebug = !showDebug;
        LOGGER.log(Level.INFO, "Debug mode toggled to: " + showDebug);
    }
    
    public boolean isOAuthConfigured() {
        return googleOauth2Config != null && googleOauth2Config.isConfigured();
    }
    
    public String getOAuthStatus() {
        if (isOAuthConfigured()) {
            return "OAuth2 Configuration: LOADED ✓";
        } else {
            return "OAuth2 Configuration: NOT CONFIGURED ✗";
        }
    }
    
    public String navigateToHome() {
        return "home?faces-redirect=true";
    }
    
    public String navigateToPage2() {
        return "page2?faces-redirect=true";
    }
}