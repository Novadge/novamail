package com.novadge.novamail

class MessageOut {
    String hostname // Host name eg. Gmail, Yahoo, Hotmail
    String username // Username for the account eg john@gmail.com
    String password // Password for the account eg *********
    String sender //
    String recipient
    String subject
    String body

    byte[] attachment
    // Users users // who made the transmission
    String status = 'Pending'
    Date dateCreated
    Date lastUpdated

    static constraints = {
        subject(nullable:true, blank:true)
        sender(nullable:true, blank:true)
        recipient(blank:false)
        body(blank:false)
        attachment(nullable:true, blank:true)
        //users(nullable:true, blank:true)
        status(nullable:true, blank:true, inList:["Sent", "Pending", "Not sent"])
    }
}
