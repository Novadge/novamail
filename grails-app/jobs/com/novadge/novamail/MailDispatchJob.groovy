package com.novadge.novamail

class MailDispatchJob {

    def messagingService

    static triggers = {
        simple repeatInterval: 50000L // execute job once in 50 seconds
    }

    void execute() {
        messagingService.processMailQueue()
    }
}
