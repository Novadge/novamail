package com.novadge.novamail

import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.mail.*
import javax.mail.Message.RecipientType
import javax.mail.search.FlagTerm
import javax.mail.search.ReceivedDateTerm
import javax.mail.search.*
import static org.springframework.http.HttpStatus.*
import grails.converters.JSON
import grails.transaction.Transactional
@Transactional//(readOnly = true)
class NovamailController {

def messagingService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    def index(){
        redirect action:'inbox'
    }
    def inbox(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        long unreadCount = MessageIn.countByStatus("Unread")
        
        respond MessageIn.list(params), model:[messageInCount: MessageIn.count(),unreadCount:unreadCount]
    }
    
    //String incomingMailServer, String store, String username, String password
    def refresh(){
       
       String mailProvider = "Gmail"//tenant.mailProvider
       String mailUsername = "Omasiri@gmail.com"//tenant.mailUsername
       String mailPassword = '$Money3.8'//tenant.mailPassword
       //SubjectTerm(java.lang.String pattern)
       //ReceivedDateTerm(int comparison, java.util.Date date) 
       //FromTerm(Address address)//InternetAddress(java.lang.String address) 
       //or NewsAddress(java.lang.String newsgroup) 
       //ComparisonTerm.EQ, GE, GT, LE, LT, NE
       //public SentDateTerm(int comparison,java.util.Date date)
       //ReceivedDateTerm(int comparison, java.util.Date date)
      // def flagTerm = new FlagTerm(new Flags(Flags.Flag.SEEN), false)
       def term = new ReceivedDateTerm(ComparisonTerm.GE,new Date()-2) // yesterday and today
       
       def m = messagingService?.getMessages(mailProvider,"imap",mailUsername,mailPassword,term,Folder.READ_WRITE)
       MessageIn msg = null
       m.each({
          msg = new MessageIn()
          messagingService.saveMessage(it,msg)     
       })
   
       redirect(action:'index')
    }
    
    def showIn(MessageIn messageIn){
        
        long unreadCount = MessageIn.countByStatus("Unread")
        messageIn.status = "Read"
        messageIn.save(flush:true)
        respond messageIn, model:[unreadCount:unreadCount],view:'show'
    }
    
    def showOut(MessageOut messageOut){
        
        respond messageOut,view:'show'
    }
    
    def compose(){
       respond new MessageOut(params),view:'compose' 
    }
    
    def saveMsgOut(MessageOut messageOut){
        
        String mailProvider = "Gmail"//tenant.mailProvider
        String mailUsername = "omasiri@gmail.com"//tenant.mailUsername
        String mailPassword = '$Money3.8'//tenant.mailPassword
        def email = params.recipients
        def props = [hostname:mailProvider,senders:mailUsername,username:mailUsername,password:mailPassword,recipients:email]
        messageOut.properties = props
        if (messageOut.hasErrors()) {
            respond messageOut.errors, view:'compose',model:[entity:provider]
            return
        }
        File[] attachments
        if(params?.attachment){
            def file = request.getFile("attachment")
            attachments.add(file)
        }
        messagingService.queueEmail(mailProvider,mailUsername,mailPassword,mailUsername,email,params?.subject, params.body,attachments)
        
        redirect action:'outbox'
    }
    
        
   
    
    
    @Transactional
    def delete(MessageIn messageInInstance) {
       print "inside delete" 
        if (messageInInstance == null) {
            notFound()
            return
        }
        
        messageInInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'messageIn.label', default: 'Message'), messageInInstance.id])
                redirect controller="novamail", action:'inbox'
            }
            '*'{ render status: NO_CONTENT }
        }
    }
    
    
    
        
    def outbox(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        
        respond MessageOut.list(params), model:[messageOutCount: MessageOut.count()]
    }
    
    def ajaxList(){
        
        
        def query = {
            or{
               rlike("senders", "${params.term}") 
               rlike("subject", "${params.term}")
               rlike("status", "${params.term}")
            }
            
            
            
                          
        projections { // good to select only the required columns.
                property("id")
                property("senders")
                property("subject")
            }
        }
       
        def msgList = MessageIn.createCriteria().list(query)
        
        def map = [:]
       // print rxnconsoList
        def ajaxList = []
        msgList.each({
                map = [:]       
                map.put('id',it[0])
                map.put('label',"${it[1]}")
                map.put('value',it[1])
//                  map.id = it.id
//                  map.label = it.LONG_COMMON_NAME
//                  map.value = it.LONG_COMMON_NAME
                
                ajaxList.add(map)
        })
    
    render ajaxList as JSON
    }
    
    def display(MessageIn messageInInstance){ 
        //print "inside display"
       // print messageInInstance.body
       // def messageInInstance = MessageIn.get(params.id)
         if (messageInInstance == null) {
            notFound()
            return
         }
            String content = messagingService.getMessageBody(messageInInstance)
            
            //def Document = recordInstance.scan.getBytes()
           // response.setContentType(messageInInstance?.body[0].contentType)
            //response.setContentLength(messageInInstance?.image.size())
            OutputStream out = response.getOutputStream()
            out.write(content.getBytes())
            out.close()
       
    }
    
    def download(Attachment attachmentInstance){
       // print "inside download"
        if (attachmentInstance == null) {
            notFound()
            return
        }
        
        response.setContentType( "application-xdownload")//response.setContentType("${documentInstance?.fileType}")
        response.setHeader("Content-Disposition", "attachment;filename=${attachmentInstance.name}")//response.setHeader("Content-disposition", "filename=${documentInstance.originalFilename}")
        OutputStream out = response.getOutputStream()
        out.write(attachmentInstance.data)
        out.close()

    }
    
    protected void notFound() {
            request.withFormat {
                form multipartForm {
                    flash.message = "Not found"//message(code: 'default.not.found.message', args: [message(code: 'document.label', default: 'Document'), params.id])
                    redirect action: "index", method: "GET"
                }
                '*'{ render status: NOT_FOUND }
            }
        }
}
