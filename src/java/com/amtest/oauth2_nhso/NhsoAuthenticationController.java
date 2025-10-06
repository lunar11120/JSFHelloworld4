

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amtest.oauth2_nhso;

import com.google.gson.Gson;
import java.io.Serializable;
import java.net.URLEncoder;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@ManagedBean(name = "nhsoAuthenticationController")
@SessionScoped
@Component
public class NhsoAuthenticationController implements Serializable{
    
    @Value("${nhso.oauth2.clientId}")
    private String clientId;
    
    @Value("${nhso.oauth2.clientSecret}")
    private String clientSecret;    
    
    @Value("${nhso.oauth2.redirectUri}")
    private String redirectUri;   
    
    @Value("${nhso.oauth2.authorizationUri}")
    private String authorizationUri;
    
    
    
     private final Gson gson = new Gson();
 
     public NhsoAuthenticationController() {
         System.out.println("NhsoAuthenticationController initialized ==========");
         System.out.println("NhsoAuthenticationController check redirectURI : "+redirectUri);
    }    
     
    /**
     * Start GitHub OAuth2 login process
     */
    public String login() {
        try {
            System.out.println("[NhsoAuthenticationController] login =====================");
            System.out.println("value spring : "+clientId+" : "+redirectUri);
       
            String authUrl = authorizationUri
                    + "?response_type=code"
                    + "&client_id=" + URLEncoder.encode(clientId, "UTF-8")
                    + "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8")
                    + "&scope=openid&state=testam01";     

             System.out.println("authUrl  = "+authUrl );
             System.out.println("[log][info] Redirecting to GitHub authorization URL: " + authUrl);
            
            
            // Redirect to NHSO tiam
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect(authUrl);
            FacesContext.getCurrentInstance().responseComplete();
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        return null;
    }     
     
     //callbacknhso
    
}//NhsoAuthenticationController