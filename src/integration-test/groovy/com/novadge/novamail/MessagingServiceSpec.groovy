package com.novadge.novamail

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import org.springframework.beans.factory.annotation.Value
/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@Mock([])
@TestFor(MessagingService)
class MessagingServiceSpec extends Specification {

    @Value('${novamail_username}')
    String username

    @Value('${novamail_password}')
    String password

    @Value('${novamail_subject}')
    String subject

    @Value('${novamail_to}')
    String to

    @Value('${novamail_body}')
    String body



//    def doWithConfig(c) {
//        c.novamail.username = username
//        c.novamail.password = password
//        c.novamail.from = username
//
//    }

    def setup() {
    }

    def cleanup() {
    }

//    void "test sendHTMLEmail(String to, String subject, String body)"(){
//
//        when:" send html is called with the correct parameters"
//         def result = service.sendHTMLEmail("support@novadge.com","Test from novamail","It works!")
//        then:"Send the email"
//            assert result
//    }


    
}


