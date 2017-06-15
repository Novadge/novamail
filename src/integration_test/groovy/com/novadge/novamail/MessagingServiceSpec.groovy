package com.novadge.novamail

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import org.springframework.beans.factory.annotation.Value

import grails.core.GrailsApplication
/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@Mock([])
@TestFor(MessagingService)
class MessagingServiceSpec extends Specification {


    static doWithConfig(c) {
        c.novamail.hostname= "gmail"//.getenv("CS_HOSTNAME")
        c.novamail.username= "noreply@novadge.com"//System.getenv("CS_USERNAME")
        c.novamail.password= "\$r95-b73gmail"//System.getenv("CS_PASSWORD")
        c.novamail.store= "imaps"//System.getenv("CS_STORE")
        c.novamail.hostProps = [
                ["host":'imap.gmail.com'],
                ["mail.imap.host":"imap.gmail.com"],
                ["mail.store.protocol": "imaps"],
                ["mail.imap.socketFactory.class": "javax.net.ssl.SSLSocketFactory"],
                ["mail.imap.socketFactory.fallback": "false"],
                ["mail.imaps.partialfetch":"false"],
                ["mail.mime.address.strict": "false"],
                ["mail.smtp.starttls.enable": "true"],
                ["mail.smtp.host": "smtp.gmail.com"],
                ["mail.smtp.auth": "true"],
                ["mail.smtp.socketFactory.port": "465"],
                ["mail.smtp.socketFactory.class": "javax.net.ssl.SSLSocketFactory"],
                ["mail.smtp.socketFactory.fallback": "false"]
        ]

    }

    def setup() {

    }

    def cleanup() {
    }

    void "test sendEmail(map.hostname, map.username, map.password, map.username, map.to, map.cc,map.bcc, map.subject, map.body,html, map.attachments,hostProps) "(){

        when:" send html is called with the correct parameters"
        Map map = service.getAccountDetails()
        assert map.keySet().size() > 0

         def result = service.sendEmail(map)
        then:"Send the email"
            assert result
    }


    
}


