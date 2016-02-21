package com.novadge.novamail

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@Mock([MessageOut,MessageIn])
@TestFor(MessagingService)
class MessagingServiceSpec extends Specification {

    static doWithConfig(c) {
        c.novamail.hostname = 'Gmail'
        c.novamail.username = 'testuser@gmail.com'
        c.novamail.password = 'password'
        c.novamail.from = 'testuser@gmail.com'
        
    }
    def setup() {
    }

    def cleanup() {
    }
    
void "test send email(Map map) " (){
        given:'App user provided email params without credentials'
            String subject = "Email subject"
            String to = "demo@email.com"
            String body = "body"
			String username = "user@x.com"
			String password = "password"
            service.sendEmail(['to':to,'subject':subject,'body':body,'username':username,'password':password])
        expect:"message is sent"
           
        and:""
                 
        
    }


    

//    void "test queueEmail(String hostname, String username, String password, String from, String to, String subject, String body,Map hostProps)" (){
//               
//    }
//    
//    
//    void "test sendHTMLEmail(String hostname, String username, String password, String from, String to, String subject, String body,Map hostProps)" () {
//        
//    }
//    
    void "test sendHTMLEmail(String to, String subject, String body)" () {
        when:
            String to = "recipient@test.com"
            String subject = "test"
            String body = "test body"
        then:
            assert service.sendHTMLEmail(to,subject,body) == true
    }
//    
//    void "test sendHTMLEmail(String to, String subject, String body,List<File> attachments)" () {
//        
//    }
//    
//    
//    void "test sendHTMLEmail(String hostname, String username, String password, String from, String to, String subject, String body, List<File> attachments,Map hostProps)" () {
//        
//    }
//
//    
//    void "test sendEmail(String hostname, String username, String password, String from, String to, String subject, String body,Map hostProps)" () {
//        
//    }
//    
//    
//    void "test sendEmail(String to, String subject, String body)" () {
//        
//    }
//     
//    void "test sendEmail(String to, String subject, String body,List<File> attachments)" () {
//        
//    }
//    
//    void "test sendEmail(String hostname, String username, String password, String from, String to, String subject, String body,List<File> attachments,Map hostProps)" () {
//        
//    }
//    
//    
//    void "test sendEmail(String hostname, String username, String password, String from, String to, String subject, String body,boolean html, List<File> attachments,Map hostProps)" () {
//        
//    }
//
//    
//    void "test doSendEmail(Map properties, boolean html, List<File> attachments,Map hostProps)" (){
//        
//            
//    }
//
//    
//    
//    void "test processMailQueue()" () {
//
//    }
//
//
//     void "test Message[] getMessages(String provider, String store,String username,String password,Map hostProps)" (){
//        
//                
//    }
//     
//    void "test Message[] getMessages(String provider, String store,String username,String password,SearchTerm term,Map hostProps)" (){
//        
//        
//    }
//    
//    void "test Message[] getMessages(String provider, String store,String username,String password,SearchTerm term,int folder_rw, Map hostProps)" (){
//       
//    }
//    
//    
//    void "test Message[] getMessages(SearchTerm term,int folder_rw)" (){
//        
//    }
//    
//    
//    void "test Message[] getMessages(SearchTerm term)" (){
//       
//    }
//    
//    
//    void "test Message[] getMessages()" (){
//              
//    }
//    
//    
//    void "test Message[] doGetMessages(Map emailProps, String store,SearchTerm term,int folder_rw, Map hostProps)" (){
//       
//        
//    }
//    
//  
//    void "test String getMessageBody(MessageOut message)" (){
//       
//    }
//    
//   
//    void "test String getMessageBody(MessageIn message,String prefFormat)" (){
//       
//    }
//    
//    
//    
//    
//    void "test String getMessageBody(MessageIn message)" (){
//        
//    }
//    
//    
//    void "test List<byte[]> getMessageAttachments(MessageIn message)" (){
//           
//    }
//    
//    
//    void "test saveMessages(Message[] messages)" (){
//       
//        
//    }
//    
//    
//    void "test saveMessage(Message message)" (){
//        
//                
//    }
//    
//    void "test saveMessage(Message message,MessageIn msg)" (){
//        
//        
//    }
//    
//    
//    void "test public MessageIn setParts(MessageIn novaMsg,Part part)" (){
//        
//    }
//   
//   void "test public MessageIn setTextPart(MessageIn novaMsg,Part part)" (){
//    
//   }
//   
//    void "test MessageIn setMultipart(MessageIn novaMsg,Part part)" (){
//       
//    }
//    
//    void "test public MessageIn setAttachment(MessageIn novaMsg,Part bp)" (){
//        
//    }
//    
//    
//    void "test private Map getSMTPProps(String hostName)" (){
//        
//    }
//    
//   
//    void "test private Map getPOP3Props(String hostName)" (){
//        
//    }
//    
//    void "test private Map getPOP3SProps(String hostName)" (){
//        
//    }
//    
//    
//    void "test private Map getIMAPSProps(String hostName)" (){
//        
//    }
//    
//    
//    void "test private Map getIMAPProps(String hostName)" (){
//        
//    }
//    
//    
//   void "test public String getText(Part p)" () {
//        
//    }
//    
    
}


