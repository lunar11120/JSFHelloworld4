/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amtest.oauth2_google;

import com.amtest.config.Google_Oauth2Config;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import javax.faces.bean.ManagedBean;

import java.io.*;
import java.net.*;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@ManagedBean(name = GoogleAuthenticationController.CONTROLLER_NAME)
@Component
@SessionScoped
public class GoogleAuthenticationController implements Serializable{
    
    public static final String CONTROLLER_NAME = "googleAuthenticationController";

    // Fill these with your Google OAuth2 credentials
    private static final String CLIENT_ID = "469397192090-gjevgcrhmrtuuiajm7amdu9vo79v3bk1.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final String REDIRECT_URI = "http://localhost:8080/seamless/callback.jsf";
    private static final String AUTH_URI = "https://accounts.google.com/o/oauth2/auth";
    private static final String TOKEN_URI = "https://oauth2.googleapis.com/token";
    private static final String USERINFO_URI = "https://www.googleapis.com/oauth2/v2/userinfo";    
    
    private final Gson gson = new Gson();
    private String userName;
    private String userId;
    private String userEmail;
    private boolean emailVerified;
    
    private boolean isAuthen; 
    private boolean userLoggedIn; 
    private String userPicture;
    private GoogleUser googleUser;
    private User sessionUser;

    public GoogleAuthenticationController() {
         isAuthen = Boolean.FALSE;
    }

     public void login() throws UnsupportedEncodingException, IOException{       
         isAuthen = Boolean.TRUE;
         System.out.println("[authenticationController] --- Invoking login isAuthen = "+isAuthen);
         
        String clientId = CLIENT_ID;
        String redirectUri = REDIRECT_URI;
        String authUrl = AUTH_URI +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&scope=openid%20email%20profile" +
                "&access_type=online";      
        
        
        System.out.println("Go to this URL: " + authUrl);      
        
        Google_Oauth2Config g2 = new Google_Oauth2Config();
        g2.init();
        System.out.println(g2.toString());
        
        FacesContext.getCurrentInstance().getExternalContext().redirect(authUrl);
         
     }    
     
    // Call this in oauth2callback.xhtml page managed bean
    public void handleCallback() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        Map<String, String> params = ctx.getExternalContext().getRequestParameterMap();
        String code = params.get("code");
        System.out.println("[authenticationController] >>>  handleCallback  code = "+code);
        if (code != null) {
            try {
                // Exchange code for access token
                String token = getToken(code);
                // Get user info
                googleUser = getUserInfo(token);
                // Here, parse userInfo JSON and set as session attributes     
                System.out.println("User info: " + googleUser.toString());
                
                //check context path
                System.out.println("ctx request path : " + ctx.getExternalContext().getRequestContextPath());
                
                 // Extract username from userInfo JSON (simple approach)
                //this.userName = extractName(userInfo);
                this.userName = "CGZION goole user";
                
                // Store user details in session variables
                this.userId = googleUser.getId();
                this.userEmail = googleUser.getEmail();
                this.userName = googleUser.getName();    
                this.emailVerified = googleUser.getVerified_email();
                userPicture = googleUser.getPicture();
                
                // Create session User object
                sessionUser = new User(userId, userEmail, emailVerified , userName);           
                

                // Optionally, store in session (not needed if bean is @SessionScoped)
                HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
                session.setAttribute("userName", this.userName);
                session.setAttribute("sessionUser", sessionUser);
                
                System.out.println("[GSON] - Parsed user with Gson 2.8.9:");
                System.out.println("ID: " + userId);
                System.out.println("Email: " + userEmail);
                System.out.println("Name: " + userName);
                System.out.println("Email Verified: " + emailVerified);
                System.out.println("Picture: " + userPicture);                

                // Redirect to googleuse.xhtml
                //ctx.getExternalContext().redirect("googleuse.xhtml");     
                ctx.getExternalContext().redirect(ctx.getExternalContext().getRequestContextPath() + "/faces/googleuse.xhtml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getToken(String code) throws IOException, Exception {
        String urlParameters =
                "code=" + code +
                "&client_id=" + CLIENT_ID +
                "&client_secret=" + CLIENT_SECRET +
                "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, "UTF-8") +
                "&grant_type=authorization_code";
        URL url = new URL(TOKEN_URI);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.getOutputStream().write(urlParameters.getBytes("UTF-8"));
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String inputLine;
  
           while ((inputLine = in.readLine()) != null) {
               sb.append(inputLine);
               System.out.println(inputLine);           
           }
           in.close();

            // Parse access_token using Gson
            String tokenJson = sb.toString();
            JsonObject tokenObject = gson.fromJson(tokenJson, JsonObject.class);
            String accessToken = tokenObject.get("access_token").getAsString();
            if (accessToken == null) { throw new Exception("Access token not found"); }    
            
        return accessToken;    

    }

    private GoogleUser getUserInfo(String accessToken) throws IOException {

        URL url = new URL(USERINFO_URI + "?access_token=" + accessToken);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine);
        }
        userLoggedIn = Boolean.TRUE;
        in.close();      
        // Parse user info using Gson
        GoogleUser googleUserInfo = gson.fromJson(sb.toString(), GoogleUser.class);
                
        return googleUserInfo;       
    }   

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isUserLoggedIn() {
        return userLoggedIn;
    }

    public void setUserLoggedIn(boolean userLoggedIn) {
        this.userLoggedIn = userLoggedIn;
    }

    public String getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }

    public User getSessionUser() {
        return sessionUser;
    }

    public void setSessionUser(User sessionUser) {
        this.sessionUser = sessionUser;
    }
    
    
}
