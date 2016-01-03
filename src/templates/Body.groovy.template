package com.novadge.novamail

class Body {
    static belongsTo = [messageIn:MessageIn]
    String contentType
    String content

    static constraints = {
        contentType(nullable:true,blank:true)
        content(nullable:true,blank:true,maxSize:1024*1000)
    }
}
