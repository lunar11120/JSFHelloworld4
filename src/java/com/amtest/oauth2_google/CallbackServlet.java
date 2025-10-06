package com.amtest.oauth2_google;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//@WebServlet("/callback")
public class CallbackServlet extends HttpServlet {
    
    private final Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        if (code == null) {
            response.sendRedirect("index.jsp");
            return;
        }
        String clientId = "469397192090-gjevgcrhmrtuuiajm7amdu9vo79v3bk1.apps.googleusercontent.com";
        String clientSecret = "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";
        String redirectUri = "http://localhost:8080/seamless/callback";
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
            
            // Parse access_token using Gson with dedicated class
            String tokenJson = tokenResp.toString();
            GoogleTokenResponse tokenResponse = gson.fromJson(tokenJson, GoogleTokenResponse.class);
            String accessToken = tokenResponse.getAccessToken();
            
            if (accessToken == null) {
                throw new Exception("Access token not found");
            }
            
            // Get user info
            URL userUrl = new URL("https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken);
            
            System.out.println("get Access Token : "+userUrl);
            
            HttpURLConnection userConn = (HttpURLConnection) userUrl.openConnection();
            userConn.setRequestMethod("GET");
            BufferedReader userIn = new BufferedReader(new InputStreamReader(userConn.getInputStream()));
            StringBuilder userContent = new StringBuilder();
            String userLine;
            while ((userLine = userIn.readLine()) != null) {
                userContent.append(userLine);
                System.out.println(userLine);
            }
            userIn.close();
            userConn.disconnect();
            
            // Parse user info using Gson
            GoogleUser user2 = gson.fromJson(userContent.toString(), GoogleUser.class);
            System.out.println("[GSON] - parse with Gson 2.8.9----");
            System.out.println("ID: " + user2.getId());
            System.out.println("Email: " + user2.getEmail());
            System.out.println("Verified: " + user2.getVerified_email());
            System.out.println("Name: " + user2.getName());            
            
            String id = user2.getId();
            String email = user2.getEmail();
            boolean verifiedEmail = user2.getVerified_email();
            String name = user2.getName();
            User user = new User(id, email, verifiedEmail, name);
            request.setAttribute("user", user);
            request.getRequestDispatcher("WEB-INF/views/success.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("index.jsp");
        }
    }
}