package com.novadge.novamail
import javax.mail.internet.MimeMultipart
import javax.mail.Multipart
import javax.mail.Address

class MessageIn {
    static hasMany = [body:Body,attachments:Attachment]
    String senders/**/
    //Subclasses: InternetAddress, NewsAddress 
    String recipients/**/
    String subject 
    //Collection bodyStrings
    String status = 'Unread'
    String contentType
    Date dateSent
    Date dateReceived
    Date dateCreated
    Date lastUpdated

    static constraints = {
        
        senders(nullable:true,blank:true,maxSize:1024 * 100 * 10)
        recipients(nullable:true,blank:true,maxSize:1024 * 100 * 10)
        subject(nullable:true,blank:true)
        body(nullable:true,blank:true)
        attachments(nullable:true,blank:true)
        status(nullable:true,blank:true,inList:["Read","Unread"])
        dateSent(nullable:true,blank:true)
        dateReceived(nullable:true,blank:true)
        contentType(nullable:true,blank:true)
    }
    
    static mapping = {
        sort dateReceived: 'desc'
    }
    
    
    
    
}
