package com.amtest.oauth2_github;

import com.amtest.config.Github_Oauth2Config;
import com.amtest.oauth2_google.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@ManagedBean(name = "githubAuthenticationController")
@SessionScoped
@Component
public class GithubAuthenticationController implements Serializable{
    
    private static final Logger LOGGER = Logger.getLogger(GithubAuthenticationController.class.getName());
    
    @Value("${github.oauth2.clientId}")
    private String clientId;
    
    @Value("${github.oauth2.clientSecret}")
    private String clientSecret;
    
    @Autowired
    private Github_Oauth2Config githubConfig;
    
    private final Gson gson = new Gson();
    
    // GitHub user information
    private String githubUserId;
    private String githubUserLogin;
    private String githubUserName;
    private String githubUserEmail;
    private String githubUserAvatarUrl;
    private String githubUserBio;
    private String githubUserLocation;
    private String githubUserCompany;
    private String githubUserBlog;
    private Integer githubUserPublicRepos;
    private Integer githubUserFollowers;
    private Integer githubUserFollowing;
    
    // Authentication state
    private boolean isAuthenticated = false;
    private String accessToken;
    private String loginStatus = "Not logged in";
    private String errorMessage;
    
    //session user 
    private String userId;    
    private String userName;  
    private User sessionUser;
    
    public GithubAuthenticationController() {
        LOGGER.info("GithubAuthenticationController initialized");
    }
    
    /**
     * Start GitHub OAuth2 login process
     */
    public String login() {
        try {
            LOGGER.info("Starting GitHub OAuth2 login process");
            LOGGER.info("[AM 101][Github-site] TEST info =================== ");
            System.out.println("value spring : "+clientId+" : "+clientSecret);
       
    

            
            if (githubConfig == null) {
                throw new Exception("GitHub OAuth2 configuration not found");
            }
            
            if (!githubConfig.isConfigValid()) {
                throw new Exception("GitHub OAuth2 configuration is invalid");
            }
            
            // Log configuration for debugging
            System.out.println("show log - config github ==============");
            LOGGER.info("GitHub Config Debug:\n" + githubConfig.getDebugInfo());
            
            // Build authorization URL
            String state = generateState(); // Simple state for CSRF protection
            String scope = "user:email"; // Request user profile and email access
            
            String authUrl = githubConfig.getAuthorizationUri() + 
                    "?client_id=" + URLEncoder.encode(githubConfig.getClientId(), "UTF-8") +
                    "&redirect_uri=" + URLEncoder.encode(githubConfig.getRedirectUri(), "UTF-8") +
                    "&scope=" + URLEncoder.encode(scope, "UTF-8") +
                    "&state=" + URLEncoder.encode(state, "UTF-8");
            
            // Store state in session for verification
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                    .getExternalContext().getSession(true);
            session.setAttribute("github_oauth_state", state);
            
            System.out.println("github config = "+githubConfig.toString());
            System.out.println("URL:"+authUrl);
            
            LOGGER.info("Redirecting to GitHub authorization URL: " + authUrl);
            
            // Redirect to GitHub
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect(authUrl);
            FacesContext.getCurrentInstance().responseComplete();
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during GitHub login initiation", e);
            errorMessage = "Login initiation failed: " + e.getMessage();
            loginStatus = "Login failed";
        }
        
        return null;
    }
    
    /**
     * Handle GitHub OAuth2 callback
     */
    public String handleCallback() {
        try {
            LOGGER.info("Handling GitHub OAuth2 callback");
            System.out.println("[controller] invoking handleCallback -------------");
           
            
            FacesContext facesContext = FacesContext.getCurrentInstance();
            String code = facesContext.getExternalContext().getRequestParameterMap().get("code");
            String state = facesContext.getExternalContext().getRequestParameterMap().get("state");
            String error = facesContext.getExternalContext().getRequestParameterMap().get("error");
            
            System.out.println("CODE:"+code);

            if (error != null) {
                throw new Exception("GitHub OAuth2 error: " + error);
            }
            
            if (code == null || code.isEmpty()) {
                throw new Exception("Authorization code not received");
            }
              System.out.println("[controller] start oauth2 method -------------");
              
            // Exchange code for access token
            accessToken = "SKisus78ss99s000ss";
            System.out.println("[controller] access token = "+accessToken);
            LOGGER.log(Level.SEVERE, ">>>>> begin create mock user github =========");
            
            
                // Create session User object
                userId = "40698895059";
                userName = "CGZion ZA";
                sessionUser = new User(userId , userName);           
                
                
                System.out.println("[GSON] - Parsed user with Gson 2.8.9:");
                System.out.println("ID: " + userId);
                System.out.println("Name: " + userName);
                
            // Redirect to success page or dashboard
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect("githubsuccess.jsf?github_login=success");
            FacesContext.getCurrentInstance().responseComplete();                
              
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during GitHub callback handling", e);
            System.out.println("github Habdle error ==========================");
            
        }
        
        return null;
    }
    
     private String getToken(String code) throws IOException, Exception {
         
        System.out.println(" get token -------------------------");
        
        String urlParameters =
                "code=" + code +
                "&client_id=" + githubConfig.getClientId() +
                "&client_secret=" + githubConfig.getClientSecret() +
                "&redirect_uri=" + URLEncoder.encode(githubConfig.getRedirectUri(), "UTF-8") +
                "&grant_type=authorization_code";
        
         System.out.println("[getToken] - urlParameters = "+urlParameters);
        
        URL url = new URL(githubConfig.getTokenUri());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.getOutputStream().write(urlParameters.getBytes("UTF-8"));
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String inputLine;
         System.out.println("[controller] get token >>>> CODE = "+code);
           while ((inputLine = in.readLine()) != null) {
               sb.append(inputLine);
               System.out.println("-------------------------------------------");
               System.out.println(inputLine);       
               System.out.println("-------------------------------------------");
           }
           in.close();

           
            String tokenJson = sb.toString();
            System.out.println("parse accesstoken >>> jsonString = "+tokenJson);
            
            String accessTokenLoc = null;
            int atIdx = tokenJson.indexOf("\"access_token\":");
            int atIdx2 = tokenJson.indexOf("access_token");
            System.out.println("atIdx = "+atIdx);
            System.out.println("atIdx2 = "+atIdx2);
            int start = 13;
            int end = 53;
              accessTokenLoc = tokenJson.substring(start, end);
                System.out.println("start = "+start+"  end = "+end+  ">>  accesstoken = "+accessTokenLoc);
            
            if (accessTokenLoc == null) {
                throw new Exception("Access token not found");
            }

            if (accessTokenLoc == null) { throw new Exception("Access token not found"); }    
            
        return accessTokenLoc;    

    }   
    
    /**
     * Exchange authorization code for access token using Gson
     */
    private String exchangeCodeForToken(String code) throws Exception {
        //check method TokenExchange run pass
        System.out.println("[controller] exchangeCodeForToken ---------------------------------");
        
        URL tokenUrl = new URL(githubConfig.getTokenUri());
        
        String urlParameters = "client_id=" + URLEncoder.encode(githubConfig.getClientId(), "UTF-8") +
                "&client_secret=" + URLEncoder.encode(githubConfig.getClientSecret(), "UTF-8") +
                "&code=" + URLEncoder.encode(code, "UTF-8") +
                "&redirect_uri=" + URLEncoder.encode(githubConfig.getRedirectUri(), "UTF-8");
        
        byte[] postData = urlParameters.getBytes("UTF-8");
        
        LOGGER.info("Requesting access token from GitHub using Gson");
        System.out.println("urlParameters = "+urlParameters);
        
        HttpURLConnection tokenConn = (HttpURLConnection) tokenUrl.openConnection();
        tokenConn.setRequestMethod("POST");
        tokenConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        tokenConn.setRequestProperty("Accept", "application/json");
        tokenConn.setRequestProperty("User-Agent", "JSF-GitHub-OAuth2-Client/1.0");
        tokenConn.setRequestProperty("Content-Length", String.valueOf(postData.length));
        tokenConn.setDoOutput(true);
        tokenConn.setDoInput(true);
        
        tokenConn.getOutputStream().write(postData);
        tokenConn.getOutputStream().flush();
        tokenConn.getOutputStream().close();
        
        int responseCode = tokenConn.getResponseCode();
        LOGGER.info("Token endpoint response code: " + responseCode);
        System.out.println("285--------------------------------------------------");
        
        BufferedReader tokenIn;
        if (responseCode >= 200 && responseCode < 300) {
            System.out.println("288 -------------------------------------------------------");
            tokenIn = new BufferedReader(new InputStreamReader(tokenConn.getInputStream()));
        } else {
            tokenIn = new BufferedReader(new InputStreamReader(tokenConn.getErrorStream()));
        }
        
        StringBuilder tokenResp = new StringBuilder();
        String tokenLine;
        while ((tokenLine = tokenIn.readLine()) != null) {
            tokenResp.append(tokenLine);
            System.out.println(tokenLine);
        }
        tokenIn.close();
        tokenConn.disconnect();
        
        if (responseCode != 200) {
            throw new Exception("Token request failed with HTTP " + responseCode + ": " + tokenResp.toString());
        }
        
        // Parse token response using Gson
        String tokenJson = tokenResp.toString();
        LOGGER.info("[GSON] Token response parsed: " + tokenJson);
        
        JsonObject tokenObject = gson.fromJson(tokenJson, JsonObject.class);
        
        if (tokenObject.has("error")) {
            String errorDesc = tokenObject.has("error_description") ? 
                tokenObject.get("error_description").getAsString() : 
                tokenObject.get("error").getAsString();
            throw new Exception("GitHub token error: " + errorDesc);
        }
        
        if (!tokenObject.has("access_token")) {
            throw new Exception("Access token not found in response: " + tokenJson);
        }
        
        return tokenObject.get("access_token").getAsString();
    }
    
    /**
     * Get user information from GitHub API using Gson
     */
    private void getUserInfo(String accessToken) throws Exception {
        URL userUrl = new URL(githubConfig.getUserInfoUri());
        
        LOGGER.info("Fetching user info from GitHub API using Gson");
        
        HttpURLConnection userConn = (HttpURLConnection) userUrl.openConnection();
        userConn.setRequestMethod("GET");
        userConn.setRequestProperty("Authorization", "Bearer " + accessToken);
        userConn.setRequestProperty("Accept", "application/vnd.github.v3+json");
        userConn.setRequestProperty("User-Agent", "JSF-GitHub-OAuth2-Client/1.0");
        
        int userResponseCode = userConn.getResponseCode();
        LOGGER.info("User info endpoint response code: " + userResponseCode);
        
        BufferedReader userIn;
        if (userResponseCode >= 200 && userResponseCode < 300) {
            userIn = new BufferedReader(new InputStreamReader(userConn.getInputStream()));
        } else {
            userIn = new BufferedReader(new InputStreamReader(userConn.getErrorStream()));
        }
        
        StringBuilder userContent = new StringBuilder();
        String userLine;
        while ((userLine = userIn.readLine()) != null) {
            userContent.append(userLine);
        }
        userIn.close();
        userConn.disconnect();
        
        if (userResponseCode != 200) {
            throw new Exception("User info request failed with HTTP " + userResponseCode + ": " + userContent.toString());
        }
        
        // Parse user info using Gson
        String userJson = userContent.toString();
        LOGGER.info("[GSON] User info response parsed: " + userJson);
        
        JsonObject userObject = gson.fromJson(userJson, JsonObject.class);
        
        // Extract user information using Gson
        githubUserId = getJsonString(userObject, "id");
        githubUserLogin = getJsonString(userObject, "login");
        githubUserName = getJsonString(userObject, "name");
        githubUserEmail = getJsonString(userObject, "email");
        githubUserAvatarUrl = getJsonString(userObject, "avatar_url");
        githubUserBio = getJsonString(userObject, "bio");
        githubUserLocation = getJsonString(userObject, "location");
        githubUserCompany = getJsonString(userObject, "company");
        githubUserBlog = getJsonString(userObject, "blog");
        githubUserPublicRepos = getJsonInteger(userObject, "public_repos");
        githubUserFollowers = getJsonInteger(userObject, "followers");
        githubUserFollowing = getJsonInteger(userObject, "following");
        
        LOGGER.info("[GSON] GitHub user info parsed successfully for user: " + githubUserLogin);
    }
    
    /**
     * Logout from GitHub OAuth2
     */
    public String logout() {
        LOGGER.info("Logging out GitHub user: " + githubUserLogin);
        
        // Clear all user data
        githubUserId = null;
        githubUserLogin = null;
        githubUserName = null;
        githubUserEmail = null;
        githubUserAvatarUrl = null;
        githubUserBio = null;
        githubUserLocation = null;
        githubUserCompany = null;
        githubUserBlog = null;
        githubUserPublicRepos = null;
        githubUserFollowers = null;
        githubUserFollowing = null;
        
        accessToken = null;
        isAuthenticated = false;
        loginStatus = "Logged out successfully";
        errorMessage = null;
        
        // Clear session
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
        if (session != null) {
            session.removeAttribute("github_oauth_state");
        }
        
        return "index?faces-redirect=true";
    }
    
    // Helper methods for Gson JSON parsing
    private String generateState() {
        return String.valueOf(System.currentTimeMillis() + Math.random());
    }
    
    private String getJsonString(JsonObject jsonObject, String key) {
        return jsonObject.has(key) && !jsonObject.get(key).isJsonNull() ? 
                jsonObject.get(key).getAsString() : null;
    }
    
    private Integer getJsonInteger(JsonObject jsonObject, String key) {
        return jsonObject.has(key) && !jsonObject.get(key).isJsonNull() ? 
                jsonObject.get(key).getAsInt() : null;
    }
    
    // Method to display config for debugging
    public String getConfigDisplay() {
        if (githubConfig == null) {
            return "GitHub configuration not loaded";
        }
        return githubConfig.getDebugInfo();
    }
    
    // Getters for JSF
    public Github_Oauth2Config getGithubConfig() {
        return githubConfig;
    }
    
    public String getGithubUserId() {
        return githubUserId;
    }
    
    public String getGithubUserLogin() {
        return githubUserLogin;
    }
    
    public String getGithubUserName() {
        return githubUserName;
    }
    
    public String getGithubUserEmail() {
        return githubUserEmail;
    }
    
    public String getGithubUserAvatarUrl() {
        return githubUserAvatarUrl;
    }
    
    public String getGithubUserBio() {
        return githubUserBio;
    }
    
    public String getGithubUserLocation() {
        return githubUserLocation;
    }
    
    public String getGithubUserCompany() {
        return githubUserCompany;
    }
    
    public String getGithubUserBlog() {
        return githubUserBlog;
    }
    
    public Integer getGithubUserPublicRepos() {
        return githubUserPublicRepos;
    }
    
    public Integer getGithubUserFollowers() {
        return githubUserFollowers;
    }
    
    public Integer getGithubUserFollowing() {
        return githubUserFollowing;
    }
    
    public boolean isAuthenticated() {
        return isAuthenticated;
    }
    
    public String getAccessToken() {
        return accessToken;
    }
    
    public String getLoginStatus() {
        return loginStatus;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public User getSessionUser() {
        return sessionUser;
    }

    public void setSessionUser(User sessionUser) {
        this.sessionUser = sessionUser;
    }
    
    
}