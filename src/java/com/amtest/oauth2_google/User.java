package com.amtest.oauth2_google;

import java.io.Serializable;


public class User implements Serializable {
    private String id;
    private String email;
    private boolean verifiedEmail;
    private String name;

    public User() {}

    public User(String id, String email, boolean verifiedEmail, String name) {
        this.id = id;
        this.email = email;
        this.verifiedEmail = verifiedEmail;
        this.name = name;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isVerifiedEmail() { return verifiedEmail; }
    public void setVerifiedEmail(boolean verifiedEmail) { this.verifiedEmail = verifiedEmail; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    
}
