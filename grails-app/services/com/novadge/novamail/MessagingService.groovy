package com.novadge.novamail

import grails.transaction.Transactional

@Transactional
class MessagingService {
    
    
    def grailsApplication
   // grails.gsp.PageRenderer groovyPageRenderer
   // def springSecurityService

         
    def sendEmail(String hostname,String username,String password,String from,String to,String subject,String body){
        def map = [:]
        map.from = from 
        map.to = to
        map.subject = subject 
        map.body = body
        map.hostName = hostname
        map.username = username
        map.password = password
        sendEmail(map)
    }
    def sendEmail(String hostname,String username,String password,String from,String to,String subject,String body,File attachment){
        def map = [:]
        map.from = from
        map.to = to
        map.subject = subject 
        map.body = body
        map.hostName = hostname
        map.username = username
        map.password = password
        
        sendEmail(map,attachment)
    }

    
    

       //-----------------------------------------------------------------------
    //-----------internal email sending -------------------------
    /*
     *This code block is used to send emails 
     * @params : Map containing email attributes such as
     * hostName: name of the host eg Gmail
     * username: email username
     * password: email password
     * from: 
     * to:
     * subject:
     * body:
     * 
     *
     */
    def sendEmail(Map obj){
        //def tenant = utilityService.getUserTenant()
        def hostProps = [:]
        //print "Inside message service obj ${obj}"
        switch(obj.hostName){
            case "Gmail" :
            //print "Provider is gmail"
            
                hostProps = ["mail.smtp.starttls.enable":"true","mail.smtp.host":"smtp.gmail.com","mail.smtp.auth":"true",  
                "mail.smtp.socketFactory.port":"465",
                "mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
                "mail.smtp.socketFactory.fallback":"false"]
            break;
            
            case "Hotmail" :
             //print "Provider is hotmail"
                hostProps = ["mail.smtp.host":"smtp.live.com","mail.smtp.starttls.enable":"true", 
                "mail.smtp.port":"587"]
            break;
            
            case "Yahoo" :
             //print "Provider is yahoo"
                hostProps = ["mail.smtp.host":"smtp.correo.yahoo.es","mail.smtp.auth":"true",
                       "mail.smtp.socketFactory.port":"465",
                       "mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
                       "mail.smtp.socketFactory.fallback":"false"  ]
            break;
            
            case "Other" :
            //print "Provider is other"
            hostProps =[:]
            break;
            
        }
        
        
       def postman = new PostMan()
       
        try{
            postman.sendHTMLEmail(obj,hostProps)
            
            return true
        }
        catch (Exception mex) {
                mex.printStackTrace();
                
                //print "${mex.toString()}"
                return false
        }
         
       
        
    }
    
    
    
      
    
    /*
     *This code block is used to send emails 
     * @params : Map containing email attributes such as
     * hostName: name of the host eg Gmail
     * username: email username
     * password: email password
     * from: 
     * to:
     * subject:
     * body:
     * file: File object
     *
     */
    def sendEmail(Map obj,File file){
        //def tenant = utilityService.getUserTenant()
        def hostProps = [:]
        //print "Inside message service obj ${obj}"
        switch(obj.hostName){
            case "Gmail" :
            //print "Provider is gmail"
            
                hostProps = ["mail.smtp.starttls.enable":"true","mail.smtp.host":"smtp.gmail.com","mail.smtp.auth":"true",  
                "mail.smtp.socketFactory.port":"465","mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
                "mail.smtp.socketFactory.fallback":"false"]
            break;
            
            case "Hotmail" :
             //print "Provider is hotmail"
                hostProps = ["mail.smtp.host":"smtp.live.com","mail.smtp.starttls.enable":"true", 
                "mail.smtp.port":"587"]
            break;
            
            case "Yahoo" :
             //print "Provider is yahoo"
                hostProps = ["mail.smtp.host":"smtp.correo.yahoo.es","mail.smtp.auth":"true",
                       "mail.smtp.socketFactory.port":"465",
                       "mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
                       "mail.smtp.socketFactory.fallback":"false"  ]
            break;
            
            case "Other" :
            //print "Provider is other"
            hostProps =[:]
            break;
            
        }
        
        
       def postman = new PostMan()
       
        try{
            postman.sendEmail(obj,hostProps,file)
            //print "Inside try block... trying to send message"
            return true
        }
        catch (Exception mex) {
                mex.printStackTrace();
                
                print "${mex.toString()}"
                return false
        }
         
       
        
    }
    
    
    def processMailQueue(){
        //print "inside message process queue"
        def pendingMsgs = MessageOut.findAll("From MessageOut m where m.status =:stat1 or m.status=:stat2",[stat1:'Pending',stat2:'Not sent'])
       
        def tenant = null
        
        pendingMsgs.each({
            
            def subject = "${it.subject}" 
            
            def to = "${it?.recipient}" 
            
            
            if(it?.attachment != null){
                if(sendEmail(it?.hostname,it?.username,it?.password,it?.sender,it?.recipient,it?.subject,it?.body,it?.attachment)){
                    it.status = "Sent"
                    it.save()
                }
            }
            else{
               // print "Sending Message....complete..."
               
                if(sendEmail(it?.hostname,it?.username,it?.password,it?.sender,it?.recipient,it?.subject,it?.body)){
                    it.status = "Sent"
                    it.save() 
                }
                
                
            } 
               
        })
        
    }
    
    def sendSms(obj){
        print "Sending SMS Reminder"
    }
    
    
    
    

}
