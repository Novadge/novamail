package com.novadge.novamail

class MessageOut{

    static hasMany = [attachments:Attachment]
    String hostname // Host name eg. Gmail, Yahoo, Hotmail
    String username // Username for the account eg john@gmail.com
    String password // Password for the account eg *********
    String senders //
    String recipients
    String subject
    String body

    //byte[] attachment
    // Users users // who made the transmission
    String status = 'Pending'
    Date dateSent
    Date dateCreated
    Date lastUpdated

    static constraints = {
        subject(nullable:true,blank:true)
        senders(nullable:false,blank:false)
        recipients(nullable:false,blank:false,email:true)
        body(nullable:false,blank:false,maxSize:1024 * 1000 * 1000)
        attachments(nullable:true,blank:true)
       
        status(nullable:true,blank:true,inList:["Sent","Pending","Not sent"])
        hostname(nullable:true,blank:true)
        username(nullable:true,blank:true)
        password(nullable:true,blank:true)
        dateSent(nullable:true,blank:true)
    }
    
    static mapping = {
        sort dateSent: 'desc'
    }
}
