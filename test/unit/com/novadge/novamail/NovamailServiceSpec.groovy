package com.novadge.novamail

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(NovamailService)
class NovamailServiceSpec extends Specification {
    /*static doWithConfig(c) {
        c.novamail.hostname = 'Gmail'
        c.novamail.username = 'testuser@gmail.com'
        c.novamail.password = 'password'
        c.novamail.from = 'testuser@gmail.com'
    }*/

    def setup() {
    }

    def cleanup() {
    }

    void "send email using novamailService"() {
        when: "a map of variables is created and novamailService is defined"
            def map = [to: "receiver", subject: "subject", body: "body"]
            def novamailService

        then: "ensure that sendEmail() returns true"
            assert novamailService.sendEmail(map) == true
    }
}
