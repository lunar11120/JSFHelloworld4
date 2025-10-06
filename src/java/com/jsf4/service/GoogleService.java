/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsf4.service;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@ManagedBean(name = "googleService")
@ViewScoped
public class GoogleService {
    
    @Value("${GOOGLE.CLIENT_ID:Property not found}")
    private String CLIENT_ID;
    
    @PostConstruct
    public void init() {
        System.out.println("[googleService - init] == check properties value : "+CLIENT_ID);
       
    }

    public String getCLIENT_ID() {
        return CLIENT_ID;
    }

    public void setCLIENT_ID(String CLIENT_ID) {
        this.CLIENT_ID = CLIENT_ID;
    }
    
    
    
    
}
