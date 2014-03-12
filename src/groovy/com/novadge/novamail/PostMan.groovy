/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.novadge.novamail

/**
 *
 * @author Omasiri
 */


import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

class PostMan{
    Properties props;
    Authenticator auth;
    Session session;
    Store store;
    Folder inbox;
    javax.mail.Message[] message;
    
 
    public PostMan(){
        
    }
     PostMan(String incomingMailServer, String store, String username, String password) {
        //throw new UnsupportedOperationException("Not yet implemented");
        System.out.println("Executing post man");
        props = new Properties();
        //this.setProperties("mail.pop3.host", incomingMailServer);
        //this.setProperties("mail.store.protocol","pop3");
        auth = new NovadgeAuthenticator(username,password);// not yet useful
        session = Session.getDefaultInstance(props, auth); // instantiate the session object for javamail
        getStore(store);// eg pop3, imap, etc
        storeConnect(incomingMailServer,username,password);
        //storeConnect();
        getInbox();
    }
    
    
     public void storeConnect(String host,String username,String password){
        try{
            System.out.println("trying to connect to store......\n");
            store.connect(host, username, password);
        }
        catch(Exception ex){
            System.out.println("Unable to connect to store because" + ex.toString());
        }

    }
     public void getStore(String string){
        try{
            System.out.println("trying to get store......\n");
            store = session.getStore(string);
        }
        catch(Exception ex){

        }

    }
    
    public void getInbox(){
        try{
            System.out.println("trying to get inbox......\n");
            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            message = inbox.getMessages();
            
        }
        catch(Exception ex){
            System.out.println("Unable to get inbox because "+ ex.toString());
        }

    }
    
    /**
     * Returns an array of message objects
     * @return
     */
    public javax.mail.Message[] getMessages(){

        //message[0].getS
       // message[0].getReceivedDate();
        return message;

    }
    
   public void sendEmail(Map emailProps,Map props){    
      
        
      // Get system properties
      Properties properties = System.getProperties();

        
      // Setup mail server
      props.each{key,value ->
         properties.setProperty(key,value) 
      }
      //print "Inside postman props = ${props}"

      Authenticator auth = new NovadgeAuthenticator(emailProps.username,emailProps.password);// not yet useful
      // Get the default Session object.
      Session session = Session.getDefaultInstance(properties,auth);

      
         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(emailProps.from));

         // Set To: header field of the header.
         message.addRecipient(javax.mail.Message.RecipientType.TO,
                                  new InternetAddress(emailProps.to));

         // Set Subject: header field
         message.setSubject("${emailProps.subject}");

         // Now set the actual message
         message.setText("${emailProps.body}");

         // Send message
         Transport.send(message);
         System.out.println("Sent message successfully....");
           
           
   }
  
   
   
   public void sendHTMLEmail(Map emailProps,Map props){  

     // print "trying to send html email with ${emailProps}"

      // Get system properties
      Properties properties = System.getProperties();

        props.each{key,value ->
         properties.setProperty(key,value) 
      }
     
       Authenticator auth = new NovadgeAuthenticator(emailProps.username,emailProps.password);// not yet useful
      // Get the default Session object.
      //print auth
      Session session = Session.getDefaultInstance(properties,auth);

     
         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(emailProps.from));

         // Set To: header field of the header.
         message.addRecipient(javax.mail.Message.RecipientType.TO,
                                  new InternetAddress(emailProps.to));

         // Set Subject: header field
         message.setSubject("${emailProps.subject}");

         // Send the actual HTML message, as big as you like
         //(text, "text/html; charset=utf-8");
         message.setContent(emailProps.body,"text/html; charset=utf-8");

         // Send message
        // print "Sending message ......"
         Transport.send(message);
         System.out.println("Sent message successfully....");
         
           
   }
   
   
   public void sendEmail(Map emailProps,Map props,File file)
   {
      
      
      // Get system properties
      Properties properties = System.getProperties();

      // Setup mail server
      props.each{key,value ->
         properties.setProperty(key,value) 
      }


      // Get the default Session object.
      Authenticator auth = new NovadgeAuthenticator(emailProps.username,emailProps.password);// not yet useful
      // Get the default Session object.
      Session session = Session.getDefaultInstance(properties,auth);

         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);

        
         // Set From: header field of the header.
         message.setFrom(new InternetAddress(emailProps.from));

         // Set To: header field of the header.
         message.addRecipient(javax.mail.Message.RecipientType.TO,
                                  new InternetAddress(emailProps.to));

         // Set Subject: header field
         message.setSubject("${emailProps.subject}");

         // Create the message part 
         BodyPart messageBodyPart = new MimeBodyPart();

         // Fill the message
        // messageBodyPart.setText("${emailProps.body}");
         messageBodyPart.setContent(emailProps.body,"text/html; charset=utf-8");

         // Create a multipar message
         Multipart multipart = new MimeMultipart();

         // Set text message part
         multipart.addBodyPart(messageBodyPart);

         // Part two is attachment
         messageBodyPart = new MimeBodyPart();
         //TODO: Find way to set file path
         String filename = file.getName();
         DataSource source = new FileDataSource(file);
         messageBodyPart.setDataHandler(new DataHandler(source));
         messageBodyPart.setFileName(filename);
         multipart.addBodyPart(messageBodyPart);

         // Send the complete message parts
         message.setContent(multipart);

         // Send message
         Transport.send(message);
         System.out.println("Sent message successfully....");
        
        
   }
   
  

   
   
}

