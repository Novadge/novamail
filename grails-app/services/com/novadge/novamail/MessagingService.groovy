package com.novadge.novamail

import grails.core.GrailsApplication

import javax.mail.*;
import javax.mail.search.*


import com.sun.mail.imap.*
import javax.activation.DataHandler
import static grails.async.Promises.*
import javax.mail.Message.RecipientType
class MessagingService {
   def grailsApplication
    //-----------------------------------------------------------------------
    //-----------internal email sending -------------------------

    
    /**
     * Retrieve message config from grails config
     * @returns Map: a map of config attributes
     * 
     **/
    Map getAccountDetails(){
        Map map = [:]
        Map hostConfig = [:]
        map.hostname = grailsApplication.config.novamail.hostname
        map.username = grailsApplication.config?.novamail?.username
        map.password = grailsApplication.config?.novamail?.password
        map.from = grailsApplication.config.novamail?.username
        List hostProps = grailsApplication.config?.novamail?.hostProps

        for(Map item : hostProps){
            hostConfig += item
        }
        map.hostProps = hostConfig

        return map
    }
    
    /**
     * Send email
     * @param Map map A map containing the requirements for sending an email
     * @param map.hostProps
     * @param map.username (optional: defaults to conf attribute)
     * @param map.password (optional: defaults to conf attribute)
     * @param map.hostname (optional: defaults to conf attribute)
     * @param map.to : Recipients address
     * @param map.cc : carbon copy
     * @param map.bcc : blind carbon copy
     * @param map.subject : Message subject
     * @param map.body : Message body
     * @param map.html (optional: defaults to true)
     * @param map.attachments : A list of file objects
     * @param map.hostProps: Map of host properties eg: ["mail.imap.host":"imap.gmail.com"] (optional: defaults to conf attribute)
     * @return boolean
     **/
    boolean sendEmail(Map map) {
        def props = getAccountDetails()// look for config details
        // use parameter host properties else use properties defined in config
        Map hostProps = map.hostProps? map.hostProps:props.hostProps
        // use custom account credentials else use properties defined in config
        
        boolean html = map?.html != null? map.html:true
                
        if(!map.username || !map.password){//get credentials from conf file
           return sendEmail(props.hostname, props.username, props.password, props.username, map.to, map.cc,map.bcc,map.subject, map.body,html, map.attachments,hostProps)
        }
        else{// use user provided credentials

           return sendEmail(map.hostname, map.username, map.password, map.username, map.to, map.cc,map.bcc, map.subject, map.body,html, map.attachments,hostProps) 
        }
        
    }

    
    
        
    /**
     * Sends emails.
     *
     * @params : email attributes such as
     * from: The sender
     * to: The recipient
     * subject: Your message subject
     * body: The body of your message
     * @returns boolean
     **/
    boolean sendHTMLEmail(String to, String subject, String body) throws Exception{

        List<File> attachments = []// empty list of attachments
        sendHTMLEmail(to,subject,body,attachments)
    }

    /**
     * Sends emails.
     *
     * @params : email attributes such as
     * from: The sender
     * to: The recipient
     * subject: Your message subject
     * body: The body of your message
     * attachments: A list of File objects 
     */
    
    boolean sendHTMLEmail(String to, String subject, String body,List<File> attachments) throws Exception{
        Map map = getAccountDetails()

        sendHTMLEmail(map.hostname, map.username, map.password, map.from, to,"","", subject, body, attachments,map.hostProps)
    }
    
    
    
    /**
     * Sends emails.
     *
     * @params : email attributes such as
     * hostName: name of the host eg Gmail
     * username: email username
     * password: email password
     * from: The sender
     * to: comma separated list of recipient email addresses
     * cc: comma separated list of email addresses to send a carbon copy
     * bcc: comma separated list of email addresses to send a blind carbon copy
     * subject: Your message subject
     * body: The body of your message
     * attachments: A list of File object (optional)
     * hostProps: Map of host properties eg: ["mail.imap.host":"imap.gmail.com"]
     */
    
    boolean sendHTMLEmail(String hostname, String username, String password, String from, String to, String cc, String bcc,String subject, String body, List<File> attachments,Map hostProps) throws Exception{

        sendEmail(hostname, username, password, from, to, cc, bcc, subject, body,true, attachments,hostProps)
    }

    
    /**
     * Sends emails.
     *
     * @params : email attributes such as
     * @param hostName: name of the host eg Gmail
     * @param username: email username
     * @param password: email password
     * @param from: senders email address
     * @param to: a comma separated list of recipients email address
     * @param cc: a comma separated list of email address to send a carbon copy
     * @param bcc: a comma separated list of email addresses to send a blind copy
     * @param subject: email subject
     * @param body: email body
     * @param hostProps: Map of host properties eg: ["mail.imap.host":"imap.gmail.com"]
     */
    boolean sendEmail(String hostname, String username, String password, String from, String to, String cc, String bcc,String subject, String body,Map hostProps) throws Exception{
        sendEmail(hostname, username, password, from, to,cc,bcc, subject, body,false, null,hostProps)
    }
    
    /**
     * Sends emails.
     * @param to: recipients email address
     * @param subject: email subject
     * @param body: email body
     **/
    boolean sendEmail(String to, String subject, String body) throws Exception {
        List<File> attachments = []
        sendEmail(to, subject, body, attachments)
    }
     
    
    /**
     * Sends emails.
     * @param to: recipients email address
     * @param subject: email subject
     * @param body: email body
     * @param attachments: A list of File objects 
     */
    boolean sendEmail(String to, String subject, String body,List<File> attachments) throws Exception{
        Map map = getAccountDetails()
        Map hostProps = map.hostProps//grailsApplication.config.novamail.hostProps

        sendEmail(map.hostname, map.username, map.password, map.from, to,"","", subject, body,false, attachments,hostProps)
    }
    
    
    
    
    
    /**
     * Sends emails.
     * @param hostName: name of the host eg Gmail
     * @param username: email username
     * @param password: email password
     * @param from: senders email address
     * @param to: a comma separated list of recipients email address
     * @param cc: a comma separated list of email address to send a carbon copy
     * @param bcc: a comma separated list of email addresses to send a blind copy
     * @param subject: email subject
     * @param body: email body
     * @param attachments: A list of File objects 
     * @param hostProps: Map of host properties eg: ["mail.imap.host":"imap.gmail.com"]
     */
    boolean sendEmail(String hostname, String username, String password, String from, String to, String cc, String bcc,String subject, String body,List<File> attachments,Map hostProps) throws Exception {
        sendEmail(hostname, username, password, from, to, cc, bcc, subject, body,false, attachments,hostProps)
    }
    
    
    /**
     * Sends email.
     * @param hostname: name of the host eg Gmail, Hotmail, Yahoo, etc
     * @param username: email username
     * @param password: email password
     * @param from: senders email address
     * @param to: a comma separated list of recipients email address
     * @param cc: a comma separated list of email address to send a carbon copy
     * @param bcc: a comma separated list of email addresses to send a blind copy
     * @param subject: email subject
     * @param body: email body
     * @param attachments: a list of File objects (optional)
     * @param hostProps: Map of host properties eg: ["mail.imap.host":"imap.gmail.com"]
     */
    boolean sendEmail(String hostname, String username, String password, String from, String to, String cc,String bcc, String subject, String body,boolean html, List<File> attachments,Map hostProps) throws Exception{
        
        doSendEmail([
            from: from,
            to: to,
            cc: cc,
            bcc:bcc,
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
     * properties: Map containing email attibutes such as hostname, username, password, from, to, subject,body
     * hostname: name of the host eg Gmail
     * username: email username
     * password: email password
     * from: senders email address
     * to: recipients email address
     * subject: email subject
     * body: email body
     * @param attachments: a list of File objects (optional)
     * @param hostProps: Map of host properties eg: ["mail.imap.host":"imap.gmail.com"]
     */
    private boolean doSendEmail(Map properties, boolean html, List<File> attachments,Map hostProps) throws Exception{

        if(!hostProps){
            hostProps = grailsApplication.config.novamail.hostProps // try to get from custom config
        }
        
        if(hostProps?.keySet().size() == 0){ // if user did not declare custom settings
            
            hostProps = getSMTPProps(properties.hostName)
        }
        
        //log.debug "Inside message service obj ${properties}"

        PostMan postman = new PostMan()
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
    
    }

    


    
    
    
    
    /**
     * Receive emails. 
     * @param provider: name of the host eg Gmail,yahoo,hotmail,etc
     * @param store: IMAP,POP
     * @param username: john@yahoo.com
     * @param password: *************   
     * @param hostProps: Map of host properties eg: ["mail.imap.host":"imap.gmail.com"]
     */
     Message[] getMessages(String provider,String username,String password,Map hostProps){

        return getMessages(provider,username,password,null,hostProps)
                
    }
        
    
    /**
     * Receive emails.
     * @param provider: name of the host eg Gmail,yahoo,hotmail,etc
     * @param store: IMAP,POP
     * @param username: john@yahoo.com
     * @param password: *************
     * @param term: search term used to specify which messages to retrieve 
     * @param hostProps: Map of host properties eg: ["mail.imap.host":"imap.gmail.com"]    
     */
    Message[] getMessages(String provider, String username,String password,SearchTerm term,Map hostProps){

        return getMessages(provider,username,password,term,Folder.READ_ONLY,hostProps)
        
    }
    
    /**
     * Receive emails. 
     * @param provider: name of the host eg Gmail,yahoo,hotmail,etc
     *
     * @param username: john@yahoo.com
     * @param password: *************
     * @param term: search term used to specify which messages to retrieve 
     * @param folder_rw: Folder.READ_ONLY, Folder.READ_WRITE  
     * @param hostProps: Map of host properties eg: ["mail.imap.host":"imap.gmail.com"] 
     */
    Message[] getMessages(String provider, String username,String password,SearchTerm term,int folder_rw, Map hostProps){

        Map emailProps = [:]
        emailProps.hostName = provider; emailProps.username = username; emailProps.password = password;

        return doGetMessages(emailProps,term,folder_rw,hostProps)
    }
    
    
    /**
     * Receive emails. 
     * @param term: search term used to specify which messages to retrieve 
     * @param folder_rw: Folder.READ_WRITE, Folder.READ_ONLY, etc    
     */
    Message[] getMessages(SearchTerm term,int folder_rw){
        

        String provider = grailsApplication.config.novamail.hostname
        String username = grailsApplication.config.novamail.username
        String password = grailsApplication.config.novamail.password
        Map hostProps = grailsApplication.config.novamail.hostProps
        return getMessages(provider,username,password,term,folder_rw,hostProps)
    }
    
    
    /**
     * Receive emails.
     * @param term: search term used to specify which messages to retrieve 
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
    
    
    /**
     * Retrieve messages from email account based on search term
     * @params: , store , and search term
     * @param emailProps: Map containing email account properties  
     * @param store : (imap,pop) 
     * @param term : search term.
     *  GT, EQ,GE,LE,NE for ComparisonTerm || 
     *  ReceivedDateTerm eg new ReceivedDateTerm(ComparisonTerm.GT,new Date()-1)
     *  SentDateTerm eg new SentDateTerm(ComparisonTerm.GT,new Date()-1)
     *  SubjectTerm(java.lang.String pattern) 
     *  FromTerm(Address address), 
     *  RecipientTerm(Message.RecipientType type, Address address)
     *  TO, CC and BCC for Message.RecipientType
     * @param folder_rw: Folder.READ_WRITE, Folder.READ_ONLY, etc 
     * @param hostProps: Map of host properties eg: ["mail.imap.host":"imap.gmail.com"]
     **/
    Message[] doGetMessages(Map emailProps,SearchTerm term,int folder_rw, Map hostProps){
        
        if(!hostProps){
            throw new Exception("No host properties configured") // try to get from custom config
        }

        PostMan postman = new PostMan(emailProps,hostProps)

        if(term){ // if search term is specified and folder flag is set...
           log.debug "search term is set as ${term}"
           return postman.getInbox(term,folder_rw)
        }
        else{
            log.debug "No search term is set"

            return postman.getAllInbox()
        }
        
    }
    

    

    
    


    
    
    /**
     * Persist an array of messages to the database
     * @param messages: An array of messages
     */
    def saveMessages(Message[] messages){
       messages.each({ // iterate over all messages
           saveMessage(it)            
       })
        
    }
    
    

    
    /**
     * Retrieve predefined SMTP properties
     * @params: name of the host eg. Gmail, Hotmail, Yahoo
     * @param hostName: name of the host
     **/
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
    
    /**
     * Retrieve predefined Pop3 properties
     * @params: name of the host eg. Gmail, Hotmail, Yahoo
     * @param hostName: name of the host
     **/
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

            default: // override with user defined settings
                hostProps = grailsApplication.config.novamail.hostProps
            break
        }
        return hostProps
    }
    
    /**
     * Retrieve POP3s properties
     * @params: name of the host eg. Gmail, Hotmail, Yahoo
     * @param hostName: name of the host
     **/
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
    
    /**
     * Retrieve IMAPS properties
     * @params: name of the host eg. Gmail, Hotmail, Yahoo
     * @param hostName: name of the host
     **/
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
    
    
    /**
     * Retrieve Imap properties
     * @params: name of the host eg. Gmail, Hotmail, Yahoo
     * @param hostName: name of the host
     **/
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
     * @param p: message object from which we want to extract 'body' text
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
