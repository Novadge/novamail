package com.novadge.novamail
import javax.mail.*
class NovamailController {
def messagingService
    def index() { }
    
    //String incomingMailServer, String store, String username, String password
    def checkMail(){
        def m = messagingService?.getMessages(params?.provider,params?.protocol,params?.username,params.password)
        [messages:m] 
    }

}
