package com.amtest.console_run_test;


import java.io.*;
import java.net.*;
import java.util.*;

public class OAuth2TokenRequest {
    public static void main(String[] args) throws Exception {
        String tokenEndpoint = "https://provider.com/oauth/token";
        String clientId = "YOUR_CLIENT_ID";
        String clientSecret = "YOUR_CLIENT_SECRET";
        String redirectUri = "YOUR_REDIRECT_URI";
        String code = "AUTHORIZATION_CODE_RECEIVED";

        // Prepare URL and parameters
        URL url = new URL(tokenEndpoint);
        String urlParameters =
            "grant_type=authorization_code"
            + "&client_id=" + URLEncoder.encode(clientId, "UTF-8")
            + "&client_secret=" + URLEncoder.encode(clientSecret, "UTF-8")
            + "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8")
            + "&code=" + URLEncoder.encode(code, "UTF-8");

        // Open connection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // Send parameters
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        out.writeBytes(urlParameters);
        out.flush();
        out.close();

        // Read response
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Output the token response
        System.out.println("Response: " + response.toString());
    }
}
