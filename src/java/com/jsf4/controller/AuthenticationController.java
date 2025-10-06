/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jsf4.controller;

import com.jsf4.service.GoogleService;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Theam
 */
@ManagedBean(name=AuthenticationController.CONTROLLER_NAME)
@Component
@Scope(value = "session")
public class AuthenticationController implements Serializable {
    
    public static final String CONTROLLER_NAME="authenticationController";
    private String username;
    private String password;

    public AuthenticationController() {
    }
    
    public void login(){
        System.out.println("invoking LOGIN ---------------------");
        System.out.println("username="+username+"   password="+password);
        GoogleService service = new GoogleService();
        System.out.println("service.getCLIENT_ID() = "+service.getCLIENT_ID());
    }
    
    public String showMessage(){
        return "This is Authentication Controller check";
    }    

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
    
    
}
