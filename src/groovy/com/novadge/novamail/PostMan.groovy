package com.novadge.novamail

import javax.activation.DataHandler
import javax.activation.DataSource
import javax.activation.FileDataSource
import javax.mail.Authenticator
import javax.mail.BodyPart
import javax.mail.Folder
//import javax.mail.Flags.Flag
import javax.mail.Message
import javax.mail.Multipart
import javax.mail.search.*
import javax.mail.Session
import javax.mail.Store
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

/**
 * @author Omasiri
 */
class PostMan {

    private static final String TEXT_HTML = 'text/html; charset=utf-8;'

    Properties properties //= new Properties()
    Authenticator auth
    Session session
    Store store
    Folder inbox
    Message[] messages

    PostMan() {
    }

    /*
     *  Instantiate the Postman object with state properties 
     *  @params : State properties for postman
     *  incomingMailServer : eg imap.gmail.com
     *  store : pop3, imap, etc 
     *  username: account username eg john@doe.com
     *  password: account password eg **********
     */
    PostMan(String incomingMailServer, String store, String username, String password) {
        //throw new UnsupportedOperationException("Not yet implemented");
        System.out.log.debugln("Executing post man");
        props = new Properties();
        //this.setProperties("mail.pop3.host", incomingMailServer);
        //this.setProperties("mail.store.protocol","pop3");
        auth = new NovadgeAuthenticator(username,password);// not yet useful
        session = Session.getDefaultInstance(props, auth); // instantiate the session object for javamail
        getStore(store);// eg pop3, imap, etc
        storeConnect(incomingMailServer,username,password);
        //storeConnect();
        getInbox();
    }
    
    /*
     * Instantiate the postman with state properties 
     * @Params: maps containing email body, email host properties
     * props: map of email properties: eg
     * emailProps: Map containing email credentials eg 
     * [from:'',subject:'',to:'',body:'',username:'',password:'*****']
     * 
     */
    PostMan(Map emailProps,Map props){
        properties = new Properties()
        // Setup mail server
        props.each { key, value ->
            
            if(key == 'Host'){ 
                //do nothing :(
            }
            else{
               log.debug "setting ${key} to ${value}"
               properties.setProperty(key,value) 
            }
            
        }
        auth = new NovadgeAuthenticator(emailProps.username, emailProps.password)
        session = Session.getInstance(properties, auth)
        getStore(props["mail.store.protocol"])// eg pop3, imap, etc
        storeConnect(props["Host"],emailProps.username, emailProps.password)
    }

    
    /*
     * Connect to email server store 
     * @params : host , username and password
     * host: email server host property
     * username: username for email account eg john@doe.com
     * password: password to access email account eg *******
     **/
    void storeConnect(String host, String username, String password) {
        try {
            log.debug "trying to connect to store......at ${host}\n"
            store.connect(host, username, password)
        }
        catch(e) {
            log.error "Unable to connect to store because $e.message", e
        }
    }

    /*
     * Retrieve email store from email server
     * @params :store type
     * string: imap,pop
     **/
    void getStore(String string) {
        try {
            log.debug "trying to get store......${string}\n"
            store = session.getStore(string)
        }
        catch (e) {
            log.error e.message, e
        }
    }

    /*
     * Retrieve messages from email server
     * and store them in the message array
     **/
    void getInbox() {
        try {
            log.debug "trying to get inbox......\n"
            
            inbox = store.getFolder("INBOX")
            inbox.open(Folder.READ_ONLY)
            messages =  inbox.getMessages()
        }
        catch (e) {
            log.error "Unable to get inbox because $e.message", e
        }
    }
    
    

    /*
     * Get messages from inbox
     * @params: search term and folder flag
     * term: search criteria for retrieving messages
     * folder_rw: used to specify whethere message objects would be readable 
     * or writable --1 or 2 for Folder.READ_ONLY and Folder.READ_WRITE respectively
     * 
     */
    Message[] getInbox(SearchTerm term, int folder_rw) {
        try {
            log.debug "trying to get inbox......\n"
            inbox = store.getFolder("INBOX")
            if(folder_rw){
                inbox.open(folder_rw)
            }
                        
            if(term){
               messages = inbox.search(term); 
            }
            else{
               messages =  inbox.getMessages() 
            }
            
        }
        catch (e) {
            log.error "Unable to get inbox because $e.message", e
        }
         
        return messages
    }
    
    /*
     * Get messages from inbox
     * @params: search term
     * term: search criteria for retrieving messages
     * see javax.mail.search documentation for more information 
     */
    Message[] getInbox(SearchTerm term) {
         return getInbox(term,Folder.READ_ONLY)      
    }
    
    /*
     * Get messages from inbox
     * 
     * see javax.mail.search documentation for more information 
     */
    Message[] getAllInbox() {
         return getInbox(null,Folder.READ_ONLY) 
    }


    /*
     * Send email as text
     * @Params: maps containing email credentials, email server properties
     * props: map of email properties: eg
     * emailProps: Map containing email credentials eg 
     * [from:'',subject:'',to:'',body:'',username:'',password:'*****']
     * 
     */
    void sendEmail(Map emailProps, Map props) {
        doSendEmail emailProps, props, false, null
    }

    /*
     * Send email as html 
     * @Params: maps containing email credentials, email properties and file
     * props: map of email properties: eg
     * emailProps: Map containing email credentials eg 
     * [from:'',subject:'',to:'',body:'',username:'',password:'*****']
     * 
     */
    void sendHTMLEmail(Map emailProps, Map props, List<File> files) {
        // log.debug "trying to send html email with ${emailProps}"
        doSendEmail emailProps, props, true, files
    }
    
    /*
     * Send email as html 
     * @Params: maps containing email credentials
     * props: map of email properties: eg
     * emailProps: Map containing email credentials eg 
     * [from:'',subject:'',to:'',body:'',username:'',password:'*****']
     * 
     */
    void sendHTMLEmail(Map emailProps, Map props) {
        // log.debug "trying to send html email with ${emailProps}"
        doSendEmail emailProps, props, true, null
    }

    /*
     * Send email 
     * @Params: maps containing email credentials, email properties and file
     * props: map of email properties: eg
     * emailProps: Map containing email credentials eg 
     * [from:'',subject:'',to:'',body:'',username:'',password:'*****']
     * file: file object to send as attachment
     */    
    void sendEmail(Map emailProps, Map props, List<File> files) {
        doSendEmail emailProps, props, false, files
    }

    
    /*
     * Send email 
     * @Params: maps containing email credentials, email properties and file
     * props: map of email properties: eg
     * emailProps: Map containing email credentials eg 
     * [from:'',subject:'',to:'',body:'',username:'',password:'*****']
     * html: true or false
     * file: file object to send as attachment
     */
    private void doSendEmail(Map emailProps, Map props, boolean html, List<File> files) {
        log.debug "inside postman with ${files}"
        Properties properties = System.getProperties()
        log.debug "setting properties"
        // Setup mail server
        props.each { key, value ->
            
            properties.setProperty(key,value)
        }
        log.debug "creating auth"
        Authenticator auth = new NovadgeAuthenticator(emailProps.username, emailProps.password)// not yet useful
        Session session = Session.getDefaultInstance(properties, auth)
        log.debug "created session"
        MimeMessage message = new MimeMessage(session)
        message.setFrom(new InternetAddress(emailProps.from))

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailProps.to))

        message.setSubject("${emailProps.subject}")
        
        if (html) {
            // Send the actual HTML message, as big as you like
            message.setContent(emailProps.body, TEXT_HTML)
        }
        else {
            message.setText("${emailProps.body}")
        }
        
        if (files) {
            BodyPart messageBodyPart = new MimeBodyPart()
            log.debug "adding attachments"
            
            messageBodyPart.setContent(emailProps.body, TEXT_HTML)

            Multipart multipart = new MimeMultipart()

            multipart.addBodyPart(messageBodyPart)
            log.debug "mail has attachments"
            // Part two is attachment
            files.each({file ->
                log.debug "attaching inside postman"
                messageBodyPart = new MimeBodyPart()
                //TODO: Find way to set file path
                DataSource source = new FileDataSource(file)
                messageBodyPart.setDataHandler(new DataHandler(source))
                messageBodyPart.setFileName(file.name)
                multipart.addBodyPart(messageBodyPart)
                
            })
            
            message.setContent(multipart)
        }
        

        Transport.send(message)
        print "Sent message successfully...."
    }
    
    
    
}
