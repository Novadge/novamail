/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.novadge.novamail
import java.util.*;
import javax.mail.*;
/**
 *
 * @author Omasiri
 */


    /*
     * Authenticator obj
     */

    public class NovadgeAuthenticator extends Authenticator {
        String username,password;
        
        NovadgeAuthenticator(){
            this.username = "";
            this.password ="";
        }
        NovadgeAuthenticator(String username,String password){
            this.username = username;
            this.password = password;
        }
        
        public void setUsername(String username){
            this.username = username;
        }
        
        public void setPassword(String password){
            this.password = password;
        }
        
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }

    }

