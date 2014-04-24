package com.novadge.novamail

class MailDispatchJob {
def messagingService

    static triggers = {
        simple repeatInterval: 50000L // execute job once in 50 seconds
    }

    void execute() {
        log.debug "Processing mail queue"
        messagingService.processMailQueue()
    }
}