// configuration for plugin testing - will not be included in the plugin zip

log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'
}
//
//novadge{
//    app{
//        name=""
//    }
//    email{
//        support=""
//        info=""
//    }
//}
//novamail{
//    hostname="Gmail"
//    username="omasiri@novadge.com"
//    password="\$r95-b73Money38"
//    store="imap"
//    
//    hostProps = [
//                    "Host":"imap.gmail.com",
//                    "mail.imap.host":"imap.gmail.com",
//                    "mail.store.protocol": "imaps",
//                    "mail.imap.socketFactory.class": "javax.net.ssl.SSLSocketFactory",
//                    "mail.imap.socketFactory.fallback": "false",
//                    "mail.imaps.partialfetch": "false",
//        
//                    "mail.smtp.starttls.enable": "true",
//                    "mail.smtp.host": "smtp.gmail.com",
//                    "mail.smtp.auth": "true",
//                    "mail.smtp.socketFactory.port": "465",
//                    "mail.smtp.socketFactory.class": "javax.net.ssl.SSLSocketFactory",
//                    "mail.smtp.socketFactory.fallback": "false"
//                    ]
//  
//}



// Uncomment and edit the following lines to start using Grails encoding & escaping improvements

/* remove this line 
// GSP settings
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside null
                scriptlet = 'none' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        filteringCodecForContentType {
            //'text/html' = 'html'
        }
    }
}
remove this line */
