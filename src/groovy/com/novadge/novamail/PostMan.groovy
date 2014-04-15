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
     *  host : mail.pop3.host
     *  store : pop3, imap, etc 
     */
    PostMan(String incomingMailServer, String store, String username, String password) {
        //throw new UnsupportedOperationException("Not yet implemented");
        System.out.println("Executing post man");
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
    
    PostMan(Map emailProps,Map props){
        properties = new Properties()
        // Setup mail server
        props.each { key, value ->
            
            if(key == 'Host'){           
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

    void storeConnect(String host, String username, String password) {
        try {
            log.debug "trying to connect to store......at ${host}\n"
            store.connect(host, username, password)
        }
        catch(e) {
            log.error "Unable to connect to store because $e.message", e
        }
    }

    void getStore(String string) {
        try {
            log.debug "trying to get store......${string}\n"
            store = session.getStore(string)
        }
        catch (e) {
            log.error e.message, e
        }
    }

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
     * Used to retrive unseen emails from inbox
    */
    void getUnseenInbox(){
        // search for all "unseen" messages
        try{
            inbox = store.getFolder("INBOX")
            inbox.open(Folder.READ_ONLY)
//            Flags seen = new Flags(Flags.Flag.SEEN);
//            FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
           // messages = inbox.search(unseenFlagTerm);
           messages =  inbox.getMessages()
        }
        catch(Exception e){
           log.error "Unable to get inbox because $e.message", e 
        }
        
    }

    Message[] getMessages() {
       
        getInbox() 
        return messages
    }
    
    Message[] getMessages(SearchTerm term) {
       try {
            log.debug "trying to get inbox......\n"
            
            inbox = store.getFolder("INBOX")
            inbox.open(Folder.READ_ONLY)
            messages = inbox.search(term);
        }
        catch (e) {
            log.error "Unable to get inbox because $e.message", e
        }
         
        return messages
    }

    void sendEmail(Map emailProps, Map props) {
        doSendEmail emailProps, props, false, null
    }

    void sendHTMLEmail(Map emailProps, Map props) {
        // log.debug "trying to send html email with ${emailProps}"
        doSendEmail emailProps, props, true, null
    }

    void sendEmail(Map emailProps, Map props, File file) {
        doSendEmail emailProps, props, false, file
    }

    private void doSendEmail(Map emailProps, Map props, boolean html, File file) {

        Properties properties = System.getProperties()

        // Setup mail server
        props.each { key, value ->
            properties.setProperty(key,value)
        }

        Authenticator auth = new NovadgeAuthenticator(emailProps.username, emailProps.password)// not yet useful
        Session session = Session.getDefaultInstance(properties, auth)

        MimeMessage message = new MimeMessage(session)
        message.setFrom(new InternetAddress(emailProps.from))

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailProps.to))

        message.setSubject("${emailProps.subject}")

        if (html) {
            // Send the actual HTML message, as big as you like
            message.setContent(emailProps.body, TEXT_HTML)
        }
        else if (file != null) {
            BodyPart messageBodyPart = new MimeBodyPart()

            // messageBodyPart.setText("${emailProps.body}")
            messageBodyPart.setContent(emailProps.body, TEXT_HTML)

            Multipart multipart = new MimeMultipart()

            multipart.addBodyPart(messageBodyPart)

            // Part two is attachment
            messageBodyPart = new MimeBodyPart()
            //TODO: Find way to set file path
            DataSource source = new FileDataSource(file)
            messageBodyPart.setDataHandler(new DataHandler(source))
            messageBodyPart.setFileName(file.name)
            multipart.addBodyPart(messageBodyPart)

            message.setContent(multipart)
        }
        else {
            message.setText("${emailProps.body}")
        }

        Transport.send(message)
        log.debug "Sent message successfully...."
    }
}
