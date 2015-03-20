package com.novadge.novamail

import javax.mail.*;
import javax.mail.search.*


import com.sun.mail.imap.*
import javax.activation.DataHandler

import javax.mail.Message.RecipientType
class MessagingService {
   def grailsApplication 
    //-----------------------------------------------------------------------
    //-----------internal email sending -------------------------

    
    /**
     * Retrieve message config from grails config
     * @returns Map: a map of config attributes
     * 
     * */
    Map getAccountDetails(){
        Map map = [:]
        map.hostname = grailsApplication.config.novamail.hostname
        map.username = grailsApplication.config?.novamail?.username
        map.password = grailsApplication.config?.novamail?.password
        map.from = grailsApplication.config.novamail?.username
        map.hostProps = grailsApplication.config?.novamail?.hostProps
        return map
    }
    
    
    
    /**
     * Sends emails with quarts job.
     *
     * @params : email attributes
     * from:
     * to:
     * subject:
     * body:
     */
    def queueEmail(String to, String subject, String body){
        queueEmail(to,subject,body,[])
        
    }
    
    /**
     * Sends emails with quarts job.
     *
     * @params : Map containing email attributes such as
     * from:
     * to:
     * subject:
     * body:
     * attachments: A list of File objects (optional)
     */
    def queueEmail(String to, String subject, String body,List<File> attachments){
        def map = getAccountDetails()
        queueEmail(map.hostname,map.username,map.password,map.from,to,subject,body.toString(),attachments,map.hostProps)
                
    }
    
    /**
     * Sends emails with quarts job.
     *
     * @params : email attributes
     * hostName: name of the host eg Gmail
     * username: email username
     * password: email password
     * from:
     * to:
     * subject:
     * body:
     * hostProps: Map of host properties eg: ["mail.imap.host":"imap.gmail.com"] 
     */
    def queueEmail(String hostname, String username, String password, String from, String to, String subject, String body,Map hostProps){
        queueEmail(hostname,username,password,from,to,subject,body,null,hostProps)        
    }
    
    /**
     * Sends emails with quarts job.
     *
     * @params : Map containing email attributes such as
     * hostName: name of the host eg Gmail
     * username: email username
     * password: email password
     * from:
     * to:
     * subject:
     * body:
     * attachments: A list of File objects (optional)
     * hostProps: Map of host properties eg: ["mail.imap.host":"imap.gmail.com"] 
     */
    def queueEmail(String hostname, String username, String password, String from, String to, String subject, String body,List<File> attachments,Map hostProps){
        def messageOut = new MessageOut(hostname:hostname,username:username,password:password,senders:from,recipients:to,subject:subject,body:body.toString(),hostProperties:hostProps)
        queueEmail(messageOut,attachments)        
        
    }
    
    /**
     * Sends emails with quarts job.
     *
     * @params : 
     * messageOut: messageOut object
     * attachments: A list of File objects (optional)
     * 
     */
    def queueEmail(MessageOut messageOut, List<File> attachments){
        
        messageOut = addAttachments(messageOut,attachments)        
        messageOut.save(flush:true)
        
    }
    
    /**
     * Sends emails with quarts job.
     *
     * @params : 
     * messageOut: messageOut object
     * 
     * 
     */
    def queueEmail(MessageOut messageOut){
        queueEmail(messageOut,[])
        
    }
    
    /*Add attachments to a message
     * @Params
     * messageOut: Message object to which attachments will be added
     * attachments: List of file objects to attach
     * 
     * */
    private MessageOut addAttachments(MessageOut messageOut, List<File> attachments){
        Attachment attachment = null
        FileInputStream fis = null
        attachments?.each{
            attachment = new Attachment(name:it.getName(),data:it.getBytes())
            messageOut.addToAttachments(attachment)
            // delete the file
        }
        return messageOut
    }
    
    
    
    
    
    
    
    
    
    /**
     * Sends emails.
     *
     * @params : email attributes such as
     * hostName: name of the host eg Gmail
     * username: email username
     * password: email password
     * from:
     * to:
     * subject:
     * body:
     * hostProps: Map of host properties eg: ["mail.imap.host":"imap.gmail.com"] 
     */
    boolean sendHTMLEmail(String hostname, String username, String password, String from, String to, String subject, String body,Map hostProps) throws Exception{
         List<File> attachments = []// empty list of attachments
        sendEmail(hostname, username, password, from, to, subject, body,true, attachments,hostProps)
        
    }
    
     /**
     * Sends emails.
     *
     * @params : email attributes such as
     * from:
     * to:
     * subject:
     * body:
     * 
     */
    boolean sendHTMLEmail(String to, String subject, String body) throws Exception{
        log.debug "trying to send inside messaging service 178"
        List<File> attachments = []// empty list of attachments
        sendHTMLEmail(to,subject,body,attachments)
    }
    
    /**
     * Sends emails.
     *
     * @params : email attributes such as
     * from:
     * to:
     * subject:
     * body:
     * attachments: A list of File objects 
     */
    boolean sendHTMLEmail(String to, String subject, String body,List<File> attachments) throws Exception{
        def map = getAccountDetails()
        log.debug map
        def hostProps = map.hostProps//grailsApplication.config.novamail.hostProps
        log.debug "messaging service with hostprops ${hostProps}"
        sendHTMLEmail(map.hostname, map.username, map.password, map.from, to, subject, body, attachments,hostProps)
    }
    
    /**
     * Sends emails.
     *
     * @params : email attributes such as
     * hostName: name of the host eg Gmail
     * username: email username
     * password: email password
     * from:
     * to:
     * subject:
     * body:
     * attachments: A list of File object (optional)
     * hostProps: Map of host properties eg: ["mail.imap.host":"imap.gmail.com"]
     */
    boolean sendHTMLEmail(String hostname, String username, String password, String from, String to, String subject, String body, List<File> attachments,Map hostProps) throws Exception{
        log.debug "line 215"
        sendEmail(hostname, username, password, from, to, subject, body,true, attachments,hostProps)
    }

    /**
     * Sends emails.
     *
     * @params : email attributes such as
     * hostName: name of the host eg Gmail
     * username: email username
     * password: email password
     * from:
     * to:
     * subject:
     * body:
     * hostProps: Map of host properties eg: ["mail.imap.host":"imap.gmail.com"]
     */
    boolean sendEmail(String hostname, String username, String password, String from, String to, String subject, String body,Map hostProps) throws Exception{
        sendEmail(hostname, username, password, from, to, subject, body,false, null,hostProps)
    }
    
    /**
     * Sends emails.
     *
     * @params :email attributes such as
     * from:
     * to:
     * subject:
     * body:
     * 
     */
    boolean sendEmail(String to, String subject, String body) throws Exception {
        List<File> attachments = []
        sendEmail(to, subject, body, attachments)
    }
     
    
    /**
     * Sends emails.
     *
     * @params : email attributes such as
     * from:
     * to:
     * subject:
     * body:
     * attachments: A list of File objects 
     */
    boolean sendEmail(String to, String subject, String body,List<File> attachments) throws Exception{
        def map = getAccountDetails()
        def hostProps = map.hostProps//grailsApplication.config.novamail.hostProps
//        log.debug map
//        log.debug hostProps
        sendEmail(map.hostname, map.username, map.password, map.from, to, subject, body,false, attachments,hostProps)
    }
    
    
    /**
     * Sends emails.
     *
     * @params : email attributes such as
     * hostName: name of the host eg Gmail
     * username: email username
     * password: email password
     * from:
     * to:
     * subject:
     * body:
     * attachments: A list of File objects 
     * hostProps: Map of host properties eg: ["mail.imap.host":"imap.gmail.com"]
     */
    boolean sendEmail(String hostname, String username, String password, String from, String to, String subject, String body,List<File> attachments,Map hostProps) throws Exception {
        sendEmail(hostname, username, password, from, to, subject, body,false, attachments,hostProps)
    }
    
    //-----------------------------------------------------------------------
    
    /**
     * Sends emails.
     *
     * @params : email attributes such as
     * hostName: name of the host eg Gmail, Hotmail, Yahoo, etc
     * username: email username
     * password: email password
     * from: Sender address
     * to:  Recipient address
     * subject: Message subject
     * body: Message body
     * attachments: a list of File objects (optional)
     * hostProps: Map of host properties eg: ["mail.imap.host":"imap.gmail.com"]
     */
    boolean sendEmail(String hostname, String username, String password, String from, String to, String subject, String body,boolean html, List<File> attachments,Map hostProps) throws Exception{
        
        doSendEmail([
            from: from,
            to: to,
            subject: subject,
            body: body,
            hostName: hostname,
            username: username,
            password: password,
        ],html, attachments,hostProps)
    }

    //-----------------------------------------------------------------------
    
    /**
     * Sends emails.
     *
     * @params : Map containing email attributes such as
     * properties: Map containing email attibutes such as hostName, username, password, from, to, subject,body
     * hostName: name of the host eg Gmail
     * username: email username
     * password: email password
     * from:
     * to:
     * subject:
     * body:
     * attachments: a list of File objects (optional)
     * hostProps: Map of host properties eg: ["mail.imap.host":"imap.gmail.com"]
     */
    private boolean doSendEmail(Map properties, boolean html, List<File> attachments,Map hostProps) throws Exception{
        
        if(!hostProps){
            hostProps = grailsApplication.config.novamail.hostProps // try to get from custom config
        }
        
        if(hostProps?.keySet().size() == 0){ // if user did not declare custom settings
            
            hostProps = getSMTPProps(properties.hostName)
        }
        
        //log.debug "Inside message service obj ${properties}"
        
       def postman = new PostMan()
       
            if (!attachments) {
                if(html){
                    log.debug"sending html email without attachments 353"
                    postman.sendHTMLEmail(properties, hostProps)
                }
                else{
                    log.debug "sending email without attachments"
                   postman.sendEmail(properties, hostProps) 
                }
                
            }
            else {
                if(html){
                    log.debug "sending html email with attachments"
                    postman.sendHTMLEmail(properties, hostProps, attachments)
                }
                else{
                    log.debug "sending email with attachmentss"
                   postman.sendEmail(properties, hostProps, attachments) 
                }
                
            }

            return true
    
    }

    
    /**
     * Retrive and attempt to send out 'pending' and 'not sent' emails
     * from db message_out.
     */
    void processMailQueue() {
        log.debug "inside message process queue"
        def pendingMsgs = MessageOut.where { status == 'Pending' || status == 'Not sent' }.list()
        
        pendingMsgs.each {

            String subject = "${it.subject}"
            String to = "${it?.recipients}"

            if (!it?.attachments) {
                
                int num = it?.attachments.size()
                List<File> files = []
                
                it.attachments.each{att -> // all attachments
                   log.debug "creating files"
                   File f = new File("${att?.name}")
                   def fout = new FileOutputStream(f)
                   fout.write(att.data)
                   files.add(f) // extract data
                   
                }
                
               //(String hostname, String username, String password, String from, String to, String subject, String body)
                if(sendHTMLEmail(it?.hostname, it?.username, it?.password, it?.senders, it?.recipients, it?.subject, it?.body, files,it.hostProperties)){
                    it.status = "Sent"
                    it.dateSent = new Date()
                    it.save()  
                }
                 
            }
            else{
               // log.debug "Sending Message....complete..."
               //(String hostname, String username, String password, String from, String to, String subject, String body)
               if(sendHTMLEmail(it?.hostname, it?.username, it?.password, it?.senders, it?.recipients, it?.subject, it?.body,it.hostProperties)){
                    it.status = "Sent"
                    it.dateSent = new Date()
                    it.save()
               }
               
                 
            }
            
        }
    }

    
    
    /**
     * Sends text messages.
     * Work in progress
     * We want to integrate this with some kind of text messaging service
     */
    def sendSms(obj) {
        log.debug "Sending SMS Reminder"
    }
    
    /**
     * Receive emails.
     *
     * @params : 
     * provider: name of the host eg Gmail,yahoo,hotmail,etc
     * store: IMAP,POP
     * username: john@yahoo.com
     * password: *************     
     */
     Message[] getMessages(String provider, String store,String username,String password,Map hostProps){
        return getMessages(provider,store,username,password,null,hostProps)
                
    }
        
    
    /**
     * Receive emails.
     *
     * @params : 
     * provider: name of the host eg Gmail,yahoo,hotmail,etc
     * store: IMAP,POP
     * username: john@yahoo.com
     * password: *************
     * term: search term used to specify which messages to retrieve     
     */
    Message[] getMessages(String provider, String store,String username,String password,SearchTerm term,Map hostProps){
        
        return getMessages(provider,store,username,password,term,Folder.READ_ONLY,hostProps)
        
    }
    
    /**
     * Receive emails.
     *
     * @params : 
     * provider: name of the host eg Gmail,yahoo,hotmail,etc
     * store: IMAP,POP
     * username: john@yahoo.com
     * password: *************
     * term: search term used to specify which messages to retrieve 
     * folder_rw. Folder.READ_ONLY, Folder.READ_WRITE    
     */
    Message[] getMessages(String provider, String store,String username,String password,SearchTerm term,int folder_rw, Map hostProps){
        
        Map emailProps = [:]
        emailProps.hostName = provider; emailProps.username = username; emailProps.password = password;
        return doGetMessages(emailProps,store,term,folder_rw,hostProps)
    }
    
    
    /**
     * Receive emails.
     *
     * @params : 
     * term: search term used to specify which messages to retrieve 
     * folder_rw: Folder.READ_WRITE, Folder.READ_ONLY, etc    
     */
    Message[] getMessages(SearchTerm term,int folder_rw){
        
        String store = grailsApplication.config.novamail.store
        String provider = grailsApplication.config.novamail.hostname
        String username = grailsApplication.config.novamail.username
        String password = grailsApplication.config.novamail.password
        Map hostProps = grailsApplication.config.novamail.hostProps
        return getMessages(provider,store,username,password,term,folder_rw,hostProps)
    }
    
    
    /**
     * Receive emails.
     *
     * @params : 
     * term: search term used to specify which messages to retrieve 
     *   
     */
    Message[] getMessages(SearchTerm term){
        
       return getMessages(term,Folder.READ_ONLY)
        
    }
    
    
    /**
     * Receive all emails.
     *    
     */
    Message[] getMessages(){
        return getMessages(null)        
    }
    
    
    /*
     * Retrieve messages from email account based on search term
     * @params: , store , and search term
     * emailProps: Map containing email account properties  
     * store : (imap,pop) 
     * term : search term.
     * GT, EQ,GE,LE,NE for ComparitsonTerm || 
     * ReceivedDateTerm eg new ReceivedDateTerm(ComparisonTerm.GT,new Date()-1)
     * SentDateTerm eg new SentDateTerm(ComparisonTerm.GT,new Date()-1)
     * SubjectTerm(java.lang.String pattern) 
     * FromTerm(Address address), 
     * RecipientTerm(Message.RecipientType type, Address address)
     * TO, CC and BCC for Message.RecipientType
     * 
     **/
    Message[] doGetMessages(Map emailProps, String store,SearchTerm term,int folder_rw, Map hostProps){
        
        if(!hostProps){
            hostProps = grailsApplication.config.novamail.hostProps // try to get from custom config
        }
        
        log.debug "Store is ${store}"
        
        if(hostProps?.keySet().size()==0 ){// if user has not declared custom hostProps
            // try to get host props from novamail
            log.debug "did not find custom properties"
            switch(store){
                case 'pop3':

                    hostProps = getPOP3Props(emailProps.hostName)

                    break

                case 'pop3s':
                    hostProps = getPOP3SProps(emailProps.hostName)

                    break 

                case 'imap':
                    hostProps = getIMAPProps(emailProps.hostName)
                    break

                case 'imaps':
                    hostProps = getIMAPSProps(emailProps.hostName)
                    break


            }
        }
        
        def postman = new PostMan(emailProps,hostProps)
        if(term){ // if search term is specified and folder flag is set...
           log.debug "search term is set as ${term}"
           return postman.getInbox(term,folder_rw)
        }
        else{
            log.debug "No search term is set"
            return postman.getAllInbox()
        }
        
    }
    
    /*
     * Get message body 
     * @param: messageOut object
     * note: many messages will contain text/plain and text/html formats
     * where both are available this method will return the first in the list
     */
    String getMessageBody(MessageOut message){
       // log.debug "message = ${message}"
       if(!message.body){return "NO CONTENT"}
        
        return message.body// return the first available content
    }
    
    /*
     * Get message body in a prefered format 
     * messageIn object and prefered format
     * @param message: message from which to extract body
     * @param prefFormat: format in which to return message body
     * note: many messages will contain text/plain and text/html formats
     * where both are available this method will return the first in the list
     */
    String getMessageBody(MessageIn message,String prefFormat){
       // log.debug "message = ${message}"
       if(!message.body){return "NO CONTENT"}
        if(prefFormat == null){
           return message?.body[0]?.content 
        }
        //log.debug message.body
        int num = message?.body?.size() // get number of items in message body
        if(num >1){
           // log.debug "message has more than one body"
            message.body.each{
                if(it.contentType =~ prefFormat){
                    return it?.content
                }
            }
           // return messageIn.body[0].content // return available content
        }
        return message.body[0]?.content// return the first available content
    }
    
    
    
    /*
     * Get message body 
     * @params message object
     * message: message from which to extract body
     * note: many messages will contain text/plain and text/html formats
     * where both are available this method will return the first in the list
     */
    String getMessageBody(MessageIn message){
        return getMessageBody(message,null)
    }
    
    
    /*
     * Get message attachments in a prefered format 
     * @params messageIn object
     * message: message from which to extract attachments     
     */
    List<byte[]> getMessageAttachments(MessageIn message){
        List<byte[]> bytes = []
        message?.attachments.each{
            bytes.add(it?.data)
        }
        return bytes        
    }
    
    
    /**
     * Persist an array of messages to the database
     * @Params: An array of messages
     */
    def saveMessages(Message[] messages){
                
       messages.each({ // iterate over all messages
           saveMessage(it)            
       })
        
    }
    
    
    /**
     * Persist a message to the database
     * @Params: A message
     */
    def saveMessage(Message message){
        
        MessageIn msg = null;
        Map props = [:]
        msg = new MessageIn() // create a new message object
        Message it = message // reference handle... too lazy to do anything more... ;)
        props =[contentType:it.getContentType(),senders:it.getFrom()[0].toString(),recipients:it.getRecipients(RecipientType.TO).toString(),subject:it.getSubject(),dateSent:it.getSentDate(),dateReceived:it.getReceivedDate(),status:"Unread"]
        msg.properties = props // assign properties to the message object
        msg = setParts(msg,it) // set the body and attachment parts of the message
        MessageIn.withNewSession{ // save message with new session because ... 
                //for some strange reason it does not save with current session (probably because of download delay...;)
           
            msg.save(flush:true) 
        }
        
    }
    
    
    /**
     * Persist a message to the database
     * @Params: A message and a domain class
     * message: incoming javamail message
     * messageIn: local database store object
     */
    def saveMessage(Message message,MessageIn msg){
        
        Map props = [:]
        
        Message it = message // reference handle... too lazy to do anything more... ;)
        props =[contentType:it.getContentType(),senders:it.getFrom()[0].toString(),recipients:it.getRecipients(RecipientType.TO).toString(),subject:it.getSubject(),dateSent:it.getSentDate(),dateReceived:it.getReceivedDate(),status:"Unread"]
        msg.properties = props // assign properties to the message object
        msg = setParts(msg,it) // set the body and attachment parts of the message
        MessageIn.withNewSession{ // save message with new session because ... 
                //for some strange reason it does not save with current session (probably because of download delay...;)
             //log.debug "message is valid: ${msg.validate()}" 
            
            if(msg.save(flush:true)){
                //log.debug "saved ;)"
            } 
        }
        
    }
    
    /*
     * This method is used to separtate the body and attachment parts of 
     * a Part(javax.mail.Message) object and bind it to a com.novamail.MessageIn
     * Object for database persistence.
     * @Params: com.novamail.MessageIn object, javax.mail.Message 
     * novaMsg: The object to which you want to bind incomming message properties
     * part: Message object from which we want to extract properties
     * 
     * */
    public MessageIn setParts(MessageIn novaMsg,Part part){
        log.debug "content type ${part.getContentType()} "
        if (part.isMimeType("text/*")) {
            return setTextPart(novaMsg,part)
        }

        if (part.isMimeType("multipart/*")) {
            return setMultipart(novaMsg,part)
        }
    }
   
   /*
     * This method is used to extract and bind text from incoming message
     * Part(javax.mail.Message) to a com.novamail.MessageIn
     * Object for database persistence.
     * @Params: com.novamail.MessageIn object, javax.mail.Message 
     * novaMsg: The object to which you want to bind incoming message text
     * part: Message object from which we want to extract properties
     * 
     * */
   public MessageIn setTextPart(MessageIn novaMsg,Part part){
       log.debug "text part begin"
       String content = getText(part)
       novaMsg.addToBody(new Body(contentType:part.getContentType(),content:content))//addToBody(new Body(fileType:part.getContentType(),body:content))
       return novaMsg
   }
   
    /*
     * This method is used to extract and bind text and attachments from incoming
     * messages Part(javax.mail.Message) to a com.novamail.MessageIn
     * Object for database persistence.
     * @Params: com.novamail.MessageIn object, javax.mail.Message 
     * novaMsg: The object to which you want to bind incomming message properties
     * part: Message object from which we want to extract properties
     * 
     * */
    public MessageIn setMultipart(MessageIn novaMsg,Part part){
        log.debug "multipart begin...."
        if (part.isMimeType("multipart/*")) {
            
            Multipart mp = (Multipart)part.getContent();
            
            for (int i = 0; i < mp.getCount(); i++){ // go through all body parts
                log.debug "processing part ${i} of ${mp.getCount()}"
                Part bp = mp.getBodyPart(i);
                String contentType = bp.getContentType()
                String disp = bp.getDisposition();
                if (disp?.equalsIgnoreCase(Part.ATTACHMENT)){ // if this part is an attachment ...
                    log.debug "part ${i} of type ${contentType} is an attachment"
                   novaMsg = setAttachment(novaMsg,bp)                   
                }
                else{
                    log.debug "part ${i} of type ${contentType} is not an attachment"
                    def content = getText(part)
                    def body = new Body(contentType:contentType,content:content)
                    log.debug body.getErrors()
                    novaMsg.addToBody(body)
                }
                
            }
            //return setBody(novaMsg,)
        }
        log.debug "multipart ...end\n\n\n"
        return novaMsg
    }
    
    /*
     * This method is used to extract and bind attachements from incoming 
     * messages(javax.mail.Message)objects to a com.novamail.MessageIn
     * Object for database persistence.
     * @Params: com.novamail.MessageIn object, javax.mail.Message 
     * novaMsg: The object to which you want to bind incomming message properties
     * part: Message object from which we want to extract properties
     * 
     * */
    public MessageIn setAttachment(MessageIn novaMsg,Part bp){
        log.debug "attachment begin..."
        log.debug bp.getContentType()
        
        InputStream input = bp.getInputStream()
        try{
            def attachment = new Attachment(name:bp.getFileName(),data:input.getBytes())
            log.debug "Attachment errors = ${attachment.getErrors()} " 
            novaMsg.addToAttachments(attachment)
        }
        catch(Exception ex){
            log.debug "Exception occured...${ex.toString()}"
        }
        log.debug "attachment .. end\n\n\n"
        return novaMsg
    }
    
    
    
    
    
    
    
    /*
     * Retrieve predefined SMTP properties
     * @params: name of the host eg. Gmail, Hotmail, Yahoo
     * hostName: name of the host
     * */
    private Map getSMTPProps(String hostName){
        //def hostProps = grailsApplication.config.novamail.hostProps
        def hostProps = [:]
        switch(hostName) {
            case "Gmail":
            //log.debug "Provider is gmail"
                hostProps = [
                    "mail.smtp.starttls.enable": "true",
                    "mail.smtp.host": "smtp.gmail.com",
                    "mail.smtp.auth": "true",
                    "mail.smtp.socketFactory.port": "465",
                    "mail.smtp.socketFactory.class": "javax.net.ssl.SSLSocketFactory",
                    "mail.smtp.socketFactory.fallback": "false"]
            break

            case "Hotmail":
             //log.debug "Provider is hotmail"
                hostProps = [
                    "mail.smtp.host": "smtp.live.com",
                    "mail.smtp.starttls.enable": "true",
                    "mail.smtp.port":"587"]
            break

            case "Yahoo" :
             //log.debug "Provider is yahoo"
                hostProps = [
                    "mail.smtp.host": "smtp.correo.yahoo.es",
                    "mail.smtp.auth": "true",
                    "mail.smtp.socketFactory.port": "465",
                    "mail.smtp.socketFactory.class": "javax.net.ssl.SSLSocketFactory",
                    "mail.smtp.socketFactory.fallback":"false"]
            break

            default: // overide with user defined settings
                hostProps = [:]
            break
        }
        return hostProps
    }
    
    /*
     * Retrieve predefined Pop3 properties
     * @params: name of the host eg. Gmail, Hotmail, Yahoo
     * hostName: name of the host
     * */
    private Map getPOP3Props(String hostName){
        def hostProps =  [:]
        switch(hostName) { 
            case "Gmail":
            log.debug "Provider is gmail"
                hostProps = [
                    "Host":"pop3.gmail.com",
                    "mail.store.protocol": "pops",
                    "mail.pop3.auth":"true",
                    "mail.pop3.port":"995"
            ]
            break

            case "Hotmail":
             //log.debug "Provider is hotmail"
                hostProps = [
                    "Host": "pop-mail.outlook.com",
                    "mail.store.protocol": "pops",
                    "mail.pop3.auth":"true",
                    "mail.pop3.port":"995"
            ]
            break

            case "Yahoo" :
             //log.debug "Provider is yahoo"
                hostProps = [
                    "Host": "plus.pop.mail.yahoo.com",
                    "mail.store.protocol": "pops",
                    "mail.pop3.auth":"true",
                    "mail.pop3.port":"995"
                    
            ]
            break

            default: // overide with user defined settings
                hostProps = grailsApplication.config.novamail.hostProps
            break
        }
        return hostProps
    }
    
    /*
     * Retrieve POP3s properties
     * @params: name of the host eg. Gmail, Hotmail, Yahoo
     * hostName: name of the host
     * */
    private Map getPOP3SProps(String hostName){
        
        
        def hostProps =  [:]
        switch(hostName) {
            case "Gmail":
            //log.debug "Provider is gmail"
                hostProps = [
                    "Host":"pop3.gmail.com",
                    "mail.pop3.host":"pop3.gmail.com",
                    "mail.store.protocol": "pop3s"//,
                    
            ]
            break

            case "Hotmail":
             //log.debug "Provider is hotmail"
                hostProps = [:]
            break

            case "Yahoo" :
             //log.debug "Provider is yahoo"
                hostProps = [:]
            break

            default: // overide with user defined settings
                hostProps = grailsApplication.config.novamail.hostProps
            break
        }
        return hostProps
    }
    
    /*
     * Retrieve IMAPS properties
     * @params: name of the host eg. Gmail, Hotmail, Yahoo
     * hostName: name of the host
     * */
    private Map getIMAPSProps(String hostName){
        
        def hostProps =  [:]
        switch(hostName) {
            case "Gmail":
            //log.debug "Provider is gmail"
                hostProps = [
                    "Host":"imap.gmail.com",
                    "mail.imap.host":"imap.gmail.com",
                    "mail.store.protocol": "imaps",
                    "mail.imap.socketFactory.class": "javax.net.ssl.SSLSocketFactory",
                    "mail.imap.socketFactory.fallback": "false",
                    "mail.imaps.partialfetch": "false"
                    ]
            break

            case "Hotmail":
             //log.debug "Provider is hotmail"
                hostProps = [
                    "Host":" imap-mail.outlook.com",
                    "mail.imap.host":" imap-mail.outlook.com",
                    "mail.store.protocol": "imaps",
                    "mail.smtp.socketFactory.class": "javax.net.ssl.SSLSocketFactory",
                    "mail.imap.socketFactory.fallback": "false",
                    "mail.imaps.partialfetch": "false"
                ]
            break

            case "Yahoo" :
             //log.debug "Provider is yahoo"
                hostProps = [:]
            break

            default: // overide with user defined settings
                hostProps = grailsApplication.config.novamail.hostProps
            break
        }
        return hostProps
    }
    
    
    /*
     * Retrieve Imap properties
     * @params: name of the host eg. Gmail, Hotmail, Yahoo
     * hostName: name of the host
     * */
    private Map getIMAPProps(String hostName){
        def hostProps =  [:]
        switch(hostName) {
            case "Gmail":
            //log.debug "Provider is gmail"
                hostProps = [
                    "Host":"imap.gmail.com",
                    "mail.imap.host":"imap.gmail.com",
                    "mail.store.protocol": "imaps",
                    "mail.imaps.partialfetch": "false"
                    ]
            break

            case "Hotmail":
             //log.debug "Provider is hotmail"
                hostProps = [
                    
                ]
            break

            case "Yahoo" :
             //log.debug "Provider is yahoo"
                hostProps = [
                    "Host":"imap.mail.yahoo.com",
                    "mail.imap.host":"imap.mail.yahoo.com",
                    "mail.store.protocol": "imaps",
                    "mail.imaps.partialfetch": "false"
                ]
            break

            default: // overide with user defined settings
                hostProps = grailsApplication.config.novamail.hostProps
            break
        }
        return hostProps
    }
    
    
     

    /**
     * Return the primary text content of the message.
     * @Params: javax.mail.Message p
     * p: message object from which we want to extract 'body' text
     */
   public String getText(Part p) throws
                MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = null
            try{
                s = (String)p.getContent();
            }
            catch(UnsupportedEncodingException uex){
                InputStream is = p.getInputStream();
                s = is.getText()
            }
            //textIsHtml = p.isMimeType("text/html");
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart)p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getText(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null)
                        return s;
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }

        return null;
    }
    
    
    
    
        
    
    
}
