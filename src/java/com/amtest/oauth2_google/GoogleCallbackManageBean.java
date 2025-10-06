package com.amtest.oauth2_google;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@ManagedBean(name = GoogleCallbackManageBean.CONTROLLER_NAME)
@Component
@Scope(value = "session")
public class GoogleCallbackManageBean {
    
    public static final String CONTROLLER_NAME = "googleCallbackManageBean";

    private String username;
    private String password;
    private boolean isAuthen; 
    private boolean remember;   
    private final Gson gson = new Gson();
    // Session user object
    private User sessionUser;
    
    // User details from OAuth2
    private GoogleUser googleUser;
    private String userId;
    private String userEmail;
    private String userName;
    private boolean emailVerified;
    private String userPicture;
    private String accessToken;
    private String loginStatus = "Not logged in";

    public GoogleCallbackManageBean() {
         isAuthen = Boolean.FALSE;
    }

    //test pass seamless sso jdk1.8   run glassfish 3.1.2  java 1.6
    public String getCodeID_Google() {
        System.out.println("[GoogleCallbackManageBean] ** listener invoking ---------"); 
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String code = facesContext.getExternalContext().getRequestParameterMap().get("code");
        System.out.println("Authorization Code: " + code);
        
        if (code == null || code.isEmpty()) {
            loginStatus = "Authorization code not found";
            return null;
        }
    
        String clientId = "469397192090-gjevgcrhmrtuuiajm7amdu9vo79v3bk1.apps.googleusercontent.com";
        String clientSecret = "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";
        String redirectUri = "http://localhost:8080/seamless/callback.jsf";
        try {
            // Exchange code for access token using HttpURLConnection
            URL tokenUrl = new URL("https://oauth2.googleapis.com/token");
            String urlParameters = "code=" + code
                    + "&client_id=" + clientId
                    + "&client_secret=" + clientSecret
                    + "&redirect_uri=" + redirectUri
                    + "&grant_type=authorization_code";
            byte[] postData = urlParameters.getBytes("UTF-8");
            
            System.out.println("getToken with POST :"+urlParameters);
          
            HttpURLConnection tokenConn = (HttpURLConnection) tokenUrl.openConnection();
            tokenConn.setRequestMethod("POST");
            tokenConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            tokenConn.setDoOutput(true);
            tokenConn.getOutputStream().write(postData);
            BufferedReader tokenIn = new BufferedReader(new InputStreamReader(tokenConn.getInputStream()));
            StringBuilder tokenResp = new StringBuilder();
            String tokenLine;
            while ((tokenLine = tokenIn.readLine()) != null) {
                tokenResp.append(tokenLine);
                System.out.println("Token Detail:"+tokenLine);
            }
            tokenIn.close();
            tokenConn.disconnect();
            
            // Parse access_token using Gson
            String tokenJson = tokenResp.toString();
            JsonObject tokenObject = gson.fromJson(tokenJson, JsonObject.class);
            accessToken = tokenObject.get("access_token").getAsString();
            
            if (accessToken == null) {
                throw new Exception("Access token not found");
            }
            
            System.out.println("Access Token obtained successfully");
            
            // Get user info
            URL userUrl = new URL("https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken);
            
            System.out.println("Fetching user info from: " + userUrl);
            
            HttpURLConnection userConn = (HttpURLConnection) userUrl.openConnection();
            userConn.setRequestMethod("GET");
            BufferedReader userIn = new BufferedReader(new InputStreamReader(userConn.getInputStream()));
            StringBuilder userContent = new StringBuilder();
            String userLine;
            while ((userLine = userIn.readLine()) != null) {
                userContent.append(userLine);
                System.out.println("User Info Response: " + userLine);
            }
            userIn.close();
            userConn.disconnect();
            
            // Parse user info using Gson
            googleUser = gson.fromJson(userContent.toString(), GoogleUser.class);
            
            // Store user details in session variables
            userId = googleUser.getId();
            userEmail = googleUser.getEmail();
            userName = googleUser.getName();
            emailVerified = googleUser.getVerified_email();
            userPicture = googleUser.getPicture();
            isAuthen = true;
            loginStatus = "Successfully logged in";
            
            // Create session User object
            sessionUser = new User(userId, userEmail, emailVerified, userName);
            
            // Store user in HTTP session as well
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                    .getExternalContext().getSession(true);
            session.setAttribute("sessionUser", sessionUser);
            
            System.out.println("[GSON] - Parsed user with Gson 2.8.9:");
            System.out.println("ID: " + userId);
            System.out.println("Email: " + userEmail);
            System.out.println("Name: " + userName);
            System.out.println("Email Verified: " + emailVerified);
            System.out.println("Picture: " + userPicture);
            
            // Redirect to success page
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.redirect("success.xhtml");  
            FacesContext.getCurrentInstance().responseComplete(); 
            
        } catch (Exception e) {
            e.printStackTrace();
            loginStatus = "Login failed: " + e.getMessage();
            isAuthen = false;
            
            try {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.redirect("index.xhtml?error=oauth_failed");
                FacesContext.getCurrentInstance().responseComplete();
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
            }
        }           
        
        return code;
    }    
    
    public void loginByServlet(ExternalContext context) {
        try {
            context = FacesContext.getCurrentInstance().getExternalContext();
            context.redirect("index.xhtml");
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public String logout() {
        // Clear all user data
        googleUser = null;
        userId = null;
        userEmail = null;
        userName = null;
        emailVerified = false;
        userPicture = null;
        accessToken = null;
        isAuthen = false;
        loginStatus = "Logged out successfully";
        
        return "index?faces-redirect=true";
    }
    
    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isIsAuthen() {
        return isAuthen;
    }

    public void setIsAuthen(boolean isAuthen) {
        this.isAuthen = isAuthen;
    }

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }

    public GoogleUser getGoogleUser() {
        return googleUser;
    }

    public void setGoogleUser(GoogleUser googleUser) {
        this.googleUser = googleUser;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }
}