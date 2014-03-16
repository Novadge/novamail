package com.novadge.novamail

import javax.activation.DataHandler
import javax.activation.DataSource
import javax.activation.FileDataSource
import javax.mail.Authenticator
import javax.mail.BodyPart
import javax.mail.Folder
import javax.mail.Message
import javax.mail.Multipart
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

    Properties props = new Properties()
    Authenticator auth
    Session session
    Store store
    Folder inbox
    Message[] message

    PostMan() {
    }

    PostMan(String incomingMailServer, String store, String username, String password) {
        log.debug "Executing post man"
        //this.setProperties("mail.pop3.host", incomingMailServer)
        //this.setProperties("mail.store.protocol", "pop3")
        auth = new NovadgeAuthenticator(username, password)// not yet useful
        session = Session.getDefaultInstance(props, auth) // instantiate the session object for javamail
        getStore(store)// eg pop3, imap, etc
        storeConnect(incomingMailServer, username, password)
        //storeConnect()
        getInbox()
    }

    void storeConnect(String host, String username, String password) {
        try {
            log.debug "trying to connect to store......\n"
            store.connect(host, username, password)
        }
        catch(e) {
            log.error "Unable to connect to store because $e.message", e
        }
    }

    void getStore(String string) {
        try {
            log.debug "trying to get store......\n"
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
            message = inbox.getMessages()
        }
        catch (e) {
            log.error "Unable to get inbox because $e.message", e
        }
    }

    Message[] getMessages() {
        //message[0].getS
        // message[0].getReceivedDate()
        return message
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
