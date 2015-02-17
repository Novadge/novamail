package com.novadge.novamail

import grails.transaction.Transactional

@Transactional
class NovamailService {

    def grailsApplication // GrailsApplication instance to get variables defined in Config.groovy

    String hostname = grailsApplication.config.novamail.hostname.toString() // The hostname (defaults to set value in config)
    String username = grailsApplication.config.novamail.username.toString() // The username (defaults to set value in config)
    String password = grailsApplication.config.novamail.password.toString() // The password (defaults to set value in config)
    String from = grailsApplication.config.novamail.username.toString() // From sender (defaults to set value in config)
    String to
    String subject
    String body
    boolean html = true // Send as HTML (defaults to true)
    List<File> attachments
    Map hostProps = grailsApplication.config.novamail.hostProps // The host properties map (defaults to set value in config)

    def messagingService // MessagingService instance written by Omasiri
    

    /**
     * A simple method to call the sendEmail() method written by Omasiri.
     * Method simply calls the method through its messagingService
     *
     * @param Map map A map containing the requirements for sending an email
     *
     * @return boolean
     */
    def boolean sendEmail(Map map) {
        def hostname = map.hostname ? map.hostname : hostname // use default hostname if none is inputted
        def username = map.username ? map.username : username // use default username if none is inputted
        def password = map.password ? map.password : password // user default password if none is inputted
        def from = map.from ? map.from : from // use default sender is none is inputted
        def html = map.html ? map.html : html // use default html boolean if none is inputted
        def hostProps = map.hostProps ? map.hostProps : hostProps // use default hostProps if none is inputted

        boolean response = messagingService.sendEmail(
                hostname, username, password, from, map.to, map.subject, map.body, html, map.attachments, hostProps
        )

        if (response == true)
            return true
        else
            return false
    }

}
