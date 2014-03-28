package com.novadge.novamail

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
        def hostProps
        //log.debug "Inside message service obj ${properties}"
        switch(properties.hostName) {
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
            //log.debug "Provider is other"
                hostProps = [:]
        }

       def postman = new PostMan()

        try {
            if (file == null) {
                postman.sendHTMLEmail(properties, hostProps)
            }
            else {
                postman.sendEmail(properties, hostProps, attachment)
            }

            return true
        }
        catch (e) {
            log.errorEnabled e.message, e
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
}
