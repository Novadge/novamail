package com.novadge.novamail

class Attachment {
    static belongsTo =[message:MessageIn]
    String name
    byte[] data

    static constraints = {
        name(nullable:true,blank:true)
        data(nullable:false,blank:false,maxSize:1024 * 10* 10)
    }
}
