package com.novadge.novamail

class MessageOut {
    String hostname
    String username
    String password
    String sender
    String recipient
    String subject
    String body
    
    byte[] attachment
   // Users users // who made the transmission
    String status = 'Pending'
    Date dateCreated
    Date lastUpdated
    static constraints = {
        subject(nullable:true,blank:true)
        sender(nullable:true,blank:true)
        recipient(nullable:false,blank:false)
        body(nullable:false,blank:false)
        attachment(nullable:true,blank:true)
        //users(nullable:true,blank:true)
        status(nullable:true,blank:true,inList:["Sent","Pending","Not sent"])
        
    }
}
