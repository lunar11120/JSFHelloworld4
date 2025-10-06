package com.amtest.console_run_test;
/**
 *
 * @author Juckkapun.l
 */
import java.io.*;
import java.net.*;

public class Oauth2Example {
    public static void main(String[] args) throws Exception {
        
        // Dev Site seamless
        String clientId = "469397192090-gjevgcrhmrtuuiajm7amdu9vo79v3bk1.apps.googleusercontent.com";
        String redirectUri = "http://localhost:8080/seamless/";
        
        //String clientId = "seamless";
        //String redirectUri = "https://eclaimadju.nhso.go.th/seamless";  
        
        //String clientId = "eclaim";
        //String redirectUri = "http://192.168.202.95:28080/webComponent";            
        
        //String clientId = "eclaim";
        //String redirectUri = "https://eclaim.nhso.go.th/webComponent";

      
        String clientSecret = "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";  
        String tokenUrl = "https://oauth2.googleapis.com/token";
        String authUrl = "https://accounts.google.com/o/oauth2/auth" +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&scope=openid%20email%20profile" +
                "&access_type=online";   
        
   // ส่งแบบนี้แล้วไป production ได้
   //  https://iam.nhso.go.th/realms/nhso/protocol/openid-connect/auth?response_type=code&client_id=seamless&redirect_uri=https%3A%2F%2Feclaimadju.nhso.go.th%2Fseamless&scope=openid&state=testam01       
    
  //อันนี้ส่ง dev seamless ได้ 
  // https://tiam.nhso.go.th/realms/nhso/protocol/openid-connect/auth?client_id=seamless&redirect_uri=http%3A%2F%2F192.168.202.100%3A28081%2Fseamless&response_type=code&scope=openid&state=testam01        
 

        System.out.println("Go to this URL: " + authUrl);


        //  redirected back with a code:
        //    e.g., http://your-redirect-uri/?code=AUTH_CODE
        // 4%2F mean 4/
        System.out.print("Enter the authorization code: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String code = br.readLine();

        
            // 3. Exchange the code for an access token:
            String parameters =
            "code=" + URLEncoder.encode(code, "UTF-8") +
            "&client_id=" + URLEncoder.encode(clientId, "UTF-8") +
            "&client_secret=" + URLEncoder.encode(clientSecret, "UTF-8") +
            "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8") +
            "&grant_type=authorization_code";

             System.out.println("show parameters = "+parameters);  
           
        URL url = new URL(tokenUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
        writer.write(parameters);
        writer.flush();
        writer.close();

        int responseCode = conn.getResponseCode();
        System.out.println("Response Code: " + responseCode);        

        // Useful for debugging:
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
            conn.getErrorStream() != null ? conn.getErrorStream() : conn.getInputStream()
        ));
        String line;
        while ((line = bufferedReader .readLine()) != null) {
            System.out.println(line);
        }
        br.close();        
       
 
    }
}