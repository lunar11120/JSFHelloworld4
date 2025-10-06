/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsf4.controller;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;


@ManagedBean(name = "navigationController", eager = true)
@RequestScoped
public class NavigationController implements Serializable{
       
       public String goHome() {
            return "index";
       }  
        public String moveToPage2() {
            return "page2";
       }  
        public String moveToPage3() {
            return "page3";
       }    
        public String moveToSuccessTest() {
            return "success";
       }           
        
        
        public String moveToViewBookshop() {
            return "views/book_shop";
       }         
        
        
}


