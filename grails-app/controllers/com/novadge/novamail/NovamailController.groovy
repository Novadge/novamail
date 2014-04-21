package com.novadge.novamail
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.mail.*
import javax.mail.Message.RecipientType
import javax.mail.search.FlagTerm
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
class NovamailController {
def messagingService
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond MessageIn.list(params), model:[messageInCount: MessageIn.count()]
    }
    
    //String incomingMailServer, String store, String username, String password
    def refresh(){
        def flagTerm = new FlagTerm(new Flags(Flags.Flag.SEEN), false)
       def m = messagingService?.getMessages("Gmail","imap","omasiri@gmail.com",'$Money3.8',flagTerm)
       //Multipart mp// = (Multipart) msg.getContent();
       //BodyPart bp //= mp.getBodyPart(0);
       def contentType
       def content
       def txt
       MessageIn k
       
       messagingService.saveMessages(m)
        
   
         redirect(action:'index')
    }
    
    def show(MessageIn messageIn){
        
        int num = messageIn.body?.size()
        if(num >1){
            messageIn.body.each{
                if(it.contentType =~ "TEXT/HTML"){
                    return it.content
                }
            }
           [content:messageIn.body[0].content]
        }
        [content:messageIn.body[0].content]
    }
    
    def compose(){
        
    }
    
    def inbox(){
        
    }
    
    def sent(){
        
    }
   
    

}
