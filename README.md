novamail
========

[![Build Status](https://travis-ci.org/Omasiri/novamail.svg?branch=master)](https://travis-ci.org/Omasiri/novamail)
<h2>Description</h2>

The Novamail plug-in provides e-mail sending and receiving capabilities to a Grails application. It is also capable of sending emails asynchronously by using a scheduled Job.

<h2>Configuration</h2>

Add your email provider properties to grails configuration file: Example
Assuming you want to add config for a gmail account for 'john@gmail.com' then add the following to your grails config file.

<code>

    novamail{

        hostname="Gmail"
        username="john@gmail.com"
        password="blahblahblah"
        store="imap"
        hostProps = [

                    "mail.imap.host":"imap.gmail.com",
                    "mail.store.protocol": "imaps",
                    "mail.imap.socketFactory.class": "javax.net.ssl.SSLSocketFactory",
                    "mail.imap.socketFactory.fallback": "false",
                    "mail.imaps.partialfetch": "false",
                    "mail.smtp.starttls.enable": "true",
                    "mail.smtp.host": "smtp.gmail.com",
                    "mail.smtp.auth": "true",
                    "mail.smtp.socketFactory.port": "465",
                    "mail.smtp.socketFactory.class": "javax.net.ssl.SSLSocketFactory",
                    "mail.smtp.socketFactory.fallback": "false"

                    ]
    }


</code>

<h3>Side note </h3>
Novamail will try to use predefined host props for some popular email providers if you do not provide hostProps

<h2>Usage</h2>

Inject messagingService into your class

<code>def messagingService</code>

<em>messagingService</em> is a Grails service that provides a single method called sendEmail that takes parameters.
Please note that 'sendEmail()' is overloaded 'see http://en.wikipedia.org/wiki/Function_overloading' and can take various variations of parameters. 

<br/>
One simple form is:
<code>
sendEmail(String to, String subject, String body)
</code>

Where ......


to: Email recipient eg recipient@gmail.com

subject: "Your email subject"

body: "The body of your message"

<h2>Example</h2>

An example usage can be seen below.

<code>

    Class YourController{
    
        def messagingService
        
        def yourMethod(){
            ....
            messagingService.sendEmail("recipient@gmail.com","email subject","email body")
        
        }
    
    }

</code>


novamail with NovamailService
==============================

<h2>Requirements</h2>

To use the <code>novamailService</code>, you need to declare a map with the required variables. These are, 
 <code>hostname, username, password, from, to, subject, body, html, attachments, hostProps</code>. <br />
 
<code>hostname, username, password, from, to, subject, body</code> are string variables. <code>html</code> is boolean that defaults
to <code>true</code>, <code>attachments</code> is a List of type File (for file attachments) and is optional, 
while <code>hostProps</code> is a map of host properties (see above). <br />

If <code>hostname, username, password, from, hostProps</code> have been set in the Config.groovy file, they do not have to be
added to your map passed to the <code>novamailService</code>. <code>html</code> defaults to <code>true</code> so that can be 
omitted as well except when set explicitly (your choice). <br />

<h2>Example Usage</h2>

<code>
    
    Class MyController {
        def novamailService
        
        def myMethod() {
            ...
            def map = [to: "recepeitn@gmail.com", subject: "Hello there!", body: "Just to test out awesome Novamail"]
            novamailService.sendEmail(map) // Call the novamailService sendEmail method passing in the map
        }
    }
    
</code>