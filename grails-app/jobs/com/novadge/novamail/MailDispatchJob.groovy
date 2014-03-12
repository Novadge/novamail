package com.novadge.novamail



class MailDispatchJob {
    def messagingService
    static triggers = {
      simple repeatInterval: 50000l // execute job once in 5 seconds
    }

    def execute() {
        // execute job
        messagingService.processMailQueue()
    }
}
