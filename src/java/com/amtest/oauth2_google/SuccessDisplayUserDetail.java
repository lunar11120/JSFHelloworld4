/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amtest.oauth2_google;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@ManagedBean(name=SuccessDisplayUserDetail.CONTROLLER_NAME)
@Component
@SessionScoped
public class SuccessDisplayUserDetail implements Serializable{
    public static final String CONTROLLER_NAME="successDisplayUserDetail";
    
    public String UserName;
    public String GoogleID;

    

    @PostConstruct
    public void init() {
        UserName = "CGZion ZA 1012";
        GoogleID = "42011411 XDFG658-Gx-Google ID";
        System.out.println("success display init >>>>> UserName = "+UserName + "  GoogleID = "+GoogleID);
    } 

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getGoogleID() {
        return GoogleID;
    }

    public void setGoogleID(String GoogleID) {
        this.GoogleID = GoogleID;
    }


    
     public String navigateToIndex() {
        return "index?faces-redirect=true";
    }   
    
    
}
