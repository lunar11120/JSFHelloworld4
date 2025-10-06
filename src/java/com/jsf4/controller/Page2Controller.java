package com.jsf4.controller;


import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



@Component
@ManagedBean(name = "page2Controller")
@ViewScoped
public class Page2Controller {
    
    @Value("${test_001:Property not found}")
    private String propertyValue;
    
    @Value("${test_002:Property not found}")
    private String propertyValue_test002;    
    
    private String imagePath;
    
    @PostConstruct
    public void init() {
        System.out.println("[page2 - init] == check properties value : "+propertyValue+" : "+propertyValue_test002);
        imagePath = "image/001.jpg"; // Path relative to resources folder
    }
    
    public String getPropertyValue() {
        return propertyValue;
    }
    
    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getPropertyValue_test002() {
        return propertyValue_test002;
    }

    public void setPropertyValue_test002(String propertyValue_test002) {
        this.propertyValue_test002 = propertyValue_test002;
    }
    
    
    public String getImagePath() {
        return imagePath;
    }
    
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
    public String navigateToIndex() {
        return "index?faces-redirect=true";
    }
}