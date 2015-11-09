package com.novadge.novamail

import javax.mail.Authenticator
import javax.mail.PasswordAuthentication

/**
 * @author Omasiri
 */
class NovadgeAuthenticator extends Authenticator {

    String username = ''
    String password = ''

    NovadgeAuthenticator(String username, String password) {
        this.username = username
        this.password = password
    }

    PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password)
    }
}
