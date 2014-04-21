package com.novadge.novamail

import javax.mail.*;
import javax.mail.search.*


import com.sun.mail.imap.*
import javax.activation.DataHandler

import javax.mail.Message.RecipientType
class MessagingService {
    
    //-----------------------------------------------------------------------
    //-----------internal email sending -------------------------
    /**
     * Sends emails.
     *
     * @params : Map containing email attributes such as
     * hostName: name of the host eg Gmail
     * username: email username
     * password: email password
     * from:
     * to:
     * subject:
     * body:
     * file: File object (optional)
     */
    boolean sendEmail(String hostname, String username, String password, String from, String to, String subject, String body) {
        sendEmail(hostname, username, password, from, to, subject, body, null)
    }

    
    //-----------------------------------------------------------------------
    //-----------internal email sending -------------------------
    /**
     * Sends emails.
     *
     * @params : email attributes such as
     * hostName: name of the host eg Gmail, Hotmail, Yahoo, etc
     * username: email username
     * password: email password
     * from:
     * to:  
     * subject:
     * body:
     * file: File object (optional)
     */
    boolean sendEmail(String hostname, String username, String password, String from, String to, String subject, String body, File attachment) {
        doSendEmail([
            from: from,
            to: to,
            subject: subject,
            body: body,
            hostName: hostname,
            username: username,
            password: password,
        ], attachment)
    }

    //-----------------------------------------------------------------------
    //-----------internal email sending -------------------------
    /**
     * Sends emails.
     *
     * @params : Map containing email attributes such as
     * hostName: name of the host eg Gmail
     * username: email username
     * password: email password
     * from:
     * to:
     * subject:
     * body:
     * file: File object (optional)
     */
    private boolean doSendEmail(Map properties, File attachment) {
        //def tenant = utilityService.getUserTenant()
        def hostProps = getSMTPProps(properties.hostName)
        //log.debug "Inside message service obj ${properties}"
        
       def postman = new PostMan()

        try {
            if (attachment == null) {
                postman.sendHTMLEmail(properties, hostProps)
            }
            else {
                postman.sendEmail(properties, hostProps, attachment)
            }

            return true
        }
        catch (e) {
//            log.errorEnabled e.message, e
            log.debug e.message
            return false
        }
    }

    
    /**
     * Retrive and attempt to send out 'pending' and 'not sent' emails
     * from db message_out.
     */
    void processMailQueue() {
        //log.debug "inside message process queue"
        def pendingMsgs = MessageOut.where { status == 'Pending' || status == 'Not sent' }.list()

        pendingMsgs.each {

            String subject = "${it.subject}"
            String to = "${it?.recipient}"

            if (it?.attachment != null) {
                if (sendEmail(it?.hostname, it?.username, it?.password, it?.sender, it?.recipient, it?.subject, it?.body, it?.attachment)) {
                    it.status = "Sent"
                    it.save()
                }
            }
            else{
               // log.debug "Sending Message....complete..."

                if (sendEmail(it?.hostname, it?.username, it?.password, it?.sender, it?.recipient, it?.subject, it?.body)) {
                    it.status = "Sent"
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
     Message[] getMessages(String provider, String store,String username,String password){
        Map emailProps = [:]
        emailProps.hostName = provider; emailProps.username = username; emailProps.password = password;
        return doGetMessages(emailProps,store,null)
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
    Message[] getMessages(String provider, String store,String username,String password,SearchTerm term){
        Map emailProps = [:]
        emailProps.hostName = provider; emailProps.username = username; emailProps.password = password;
        return doGetMessages(emailProps,store,term)
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
    Message[] doGetMessages(Map emailProps, String store,SearchTerm term){
        def host = ""
        def hostProps = [:]
        log.debug "Store is ${store}"
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
        def postman = new PostMan(emailProps,hostProps)
           
        if(term != null){
            log.debug "search term is set as ${term}"
           return postman.getInbox(term) 
        }
        else{
            log.debug "No search term is set"
            return postman.getAllInbox()
        }
        
    }
    
    /*
     * Get message body in a prefered format 
     * @params messageIn object and prefered format
     * message: message from which to extract body
     * prefFormat: format in which to return message body
     * note: many messages will contain text/plain and text/html formats
     * where both are available this method will return the first in the list
     */
    String getMessageBody(MessageIn message,String prefFormat){
        if(!prefFormat){
           return message?.body[0]?.content 
        }
        int num = message.body?.size() // get number of items in message body
        if(num >1){
            message.body.each{
                if(it.contentType =~ prefFormat){
                    return it.content
                }
            }
           // return messageIn.body[0].content // return available content
        }
        return message.body[0].content// return the first available content
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
        message.attachments.each{
            bytes.add(it.data)
        }
        return bytes        
    }
    
    
    /**
     * Persist an array of messages to the database
     * @Params: An array of messages
     */
    def saveMessages(Message[] messages){
        def body, senders, recipients,subject,dateSent
        def msg
        Map props = [:]
        
        messages.each({ // iterate over all messages
            
            msg = new MessageIn() // create a new message object
            props =[contentType:it.getContentType(),senders:it.getFrom()[0].toString(),recipients:it.getRecipients(RecipientType.TO).toString(),subject:it.getSubject(),dateSent:it.getSentDate(),dateReceived:it.getReceivedDate(),status:"Unread"]
            msg.properties = props // assign properties to the message object
            msg = setParts(msg,it) // set the body and attachment parts of the message
            log.debug msg.getErrors()
            MessageIn.withNewSession{ // save message with new session because ... 
                //for some strange reason it does not save with current session (probably because of download delay...;)
               msg.save(flush:true) 
            }
            
        })
        
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
    
    
    
    
    
    /**
     * This class checks the message against defined criteria
     */
    public boolean criteriaMatch(Message message,String sender){
        
    }
    
    /*
     * Retrieve SMTP properties
     * @params: name of the host eg. Gmail, Hotmail, Yahoo
     * hostName: name of the host
     * */
    private Map getSMTPProps(String hostName){
        def hostProps =  [:]
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

            case "Other":
            break
        }
        return hostProps
    }
    
    /*
     * Retrieve Pop3 properties
     * @params: name of the host eg. Gmail, Hotmail, Yahoo
     * hostName: name of the host
     * */
    private getPOP3Props(String hostName){
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

            case "Other":
                
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
        String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
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

            case "Other":
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
        String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        def hostProps =  [:]
        switch(hostName) {
            case "Gmail":
            //log.debug "Provider is gmail"
                hostProps = [
                    "Host":"imap.gmail.com",
                    "mail.imap.host":"imap.gmail.com",
                    "mail.store.protocol": "imaps",
                    "mail.imap.socketFactory.class":SSL_FACTORY,
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
                    "mail.imap.socketFactory.class":SSL_FACTORY,
                    "mail.imap.socketFactory.fallback": "false",
                    "mail.imaps.partialfetch": "false"
                ]
            break

            case "Yahoo" :
             //log.debug "Provider is yahoo"
                hostProps = [:]
            break

            case "Other":
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

            case "Other":
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
