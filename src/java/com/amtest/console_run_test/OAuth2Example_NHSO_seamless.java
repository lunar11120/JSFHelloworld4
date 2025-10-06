package com.amtest.console_run_test;

/**
 *
 * @author Juckkapun.l
 */
import java.io.*;
import java.net.*;

public class OAuth2Example_NHSO_seamless {
    public static void main(String[] args) throws Exception {
        
        // Dev Site seamless
        String clientId = "seamless";
        String redirectUri = "http://192.168.202.100:28081/seamless/callback";
        
        //String clientId = "seamless";
        //String redirectUri = "https://eclaimadju.nhso.go.th/seamless";  
        
        /*        
        Test  Zone 
        scope = openid, proﬁle, roles (and +your scopes) 
        clientId = eclaim  
        client-secret = WcPbEDsERLfQc1kpenCWBy790XTVV01i
        Valid redirect URIs 
        http://192.168.202.95:28080/webComponent/*         
        */        
         
        
        //String clientId = "eclaim";
        //String redirectUri = "https://eclaim.nhso.go.th/webComponent";

        //seamless dev secret
        String clientSecret = "mtb40SgzNU1KNEGRGzwrRZTwxDsxP6h5"; 
        //eclaim dev secret
        
        //String clientId = "eclaim";
        //String redirectUri = "http://192.168.202.95:28080/webComponent/callback";           
        //String clientSecret = "WcPbEDsERLfQc1kpenCWBy790XTVV01i"; 
        
        String tokenUrl = "https://tiam.nhso.go.th/realms/nhso/protocol/openid-connect/token";
        String authUrl = "https://tiam.nhso.go.th/realms/nhso/protocol/openid-connect/auth"
                + "?response_type=code"
                + "&client_id=" + URLEncoder.encode(clientId, "UTF-8")
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8")
                + "&scope=openid&state=testam01";
        
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
            byte[] postData = parameters.getBytes("UTF-8");

            System.out.println("show parameters = "+parameters);         
           
            URL url = new URL(tokenUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.getOutputStream().write(postData);
        

            BufferedReader tokenIn = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder tokenResp = new StringBuilder();
            String tokenLine;

            while ((tokenLine = tokenIn.readLine()) != null) {
                tokenResp.append(tokenLine);
                System.out.println("Token Detail:"+tokenLine);
            }
            tokenIn.close();
            conn.disconnect();    

             // Parse access_token from tokenResp (simple string search, not robust JSON parsing)
            String tokenJson = tokenResp.toString();
            String accessToken = null;
            int atIdx = tokenJson.indexOf("\"access_token\":");
            
            System.out.println("");
            System.out.println("-----------------------------------------------------");
            if (atIdx != -1) {
                int start = tokenJson.indexOf('"', atIdx + 16) + 1;
                start = 17;
                int end = tokenJson.indexOf('"', start);               
                accessToken = tokenJson.substring(start, end);
                System.out.println(tokenJson.substring(1, 125));
                System.out.println(tokenJson.substring(2357, 2400));
                System.out.println(tokenJson.substring(2401, 2472));
            }
            if (accessToken == null) {
                throw new Exception("Access token not found");
            }    
            
            System.out.println("");
            System.out.println("This is accessToken");
            System.out.println(accessToken.substring(1, 200));
            System.out.println("");            
            System.out.println("USE access token call API with GET method ------");
            System.out.println("GET : https://tiam.nhso.go.th/realms/nhso/protocol/openid-connect/userinfo");
            // 4. USE access token call API with GET method
            //API endpoint
            String apiUrl = "https://tiam.nhso.go.th/realms/nhso/protocol/openid-connect/userinfo";
            URL url3 = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url3.openConnection();

            // Set the request method (GET, POST, PUT, DELETE, etc.)
            connection.setRequestMethod("GET"); 
            // Set the Authorization header with the Bearer token
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            
              int responseCode = connection.getResponseCode();
              System.out.println("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                System.out.println("Response Body: " + content.toString());
            } else {
                System.out.println("API call failed.");
            }          
       
 
    }
}

