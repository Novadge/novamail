package com.novadge.novamail
import javax.mail.*;
import javax.mail.search.* 

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
     * Process Mail queue and Sends emails.
     * Retrieve and send out pending and unsent mails.
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
    
     Message[] getMessages(String provider, String store,String username,String password){
        Map emailProps = [:]
        emailProps.hostName = provider; emailProps.username = username; emailProps.password = password;
        return doGetMessages(emailProps,store)
    }
    
    Message[] getMessages(String provider, String store,String username,String password,Date begin,Date end){
        Map emailProps = [:]
        emailProps.hostName = provider; emailProps.username = username; emailProps.password = password;
        return doGetMessages(emailProps,store)
    }
    
    Message[] doGetMessages(Map emailProps, String store){
        def host = ""
        def hostProps = [:]
        print "Store is ${store}"
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
        return postman.getMessages()
    }
    
   
    
    
    
    
    /**
     * This class checks the message against defined criteria
     */
    public boolean criteriaMatch(Message message,String sender){
        
    }
    
    private getSMTPProps(String hostName){
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
    
    
    private getPOP3Props(String hostName){
        def hostProps =  [:]
        switch(hostName) { 
            case "Gmail":
            print "Provider is gmail"
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
    
    private getPOP3SProps(String hostName){
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
    
    private getIMAPSProps(String hostName){
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
                    "mail.imap.socketFactory.fallback": "false"
                    ]
            break

            case "Hotmail":
             //log.debug "Provider is hotmail"
                hostProps = [
                    "Host":" imap-mail.outlook.com",
                    "mail.imap.host":" imap-mail.outlook.com",
                    "mail.store.protocol": "imaps",
                    "mail.imap.socketFactory.class":SSL_FACTORY,
                    "mail.imap.socketFactory.fallback": "false"
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
    
    private getIMAPProps(String hostName){
        def hostProps =  [:]
        switch(hostName) {
            case "Gmail":
            //log.debug "Provider is gmail"
                hostProps = [
                    "Host":"imap.gmail.com",
                    "mail.imap.host":"imap.gmail.com",
                    "mail.store.protocol": "imaps"//,
                    
                    
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
                    "mail.store.protocol": "imaps"//,
                ]
            break

            case "Other":
            break
        }
        return hostProps
    }
    
        
    
    
}
