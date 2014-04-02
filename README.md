novamail
========
The novamail plugin is a grails application plugin that adds email functionality to grails applications.

To install this plugin add "compile ':novamail:0.1'" ( without double quotes ) to your buildConfig file and 
execute 'grails n2-quickstart' to initialize novamail.

An easy way to start using the plugin is to Add 'def messagingService' to your controller or service and use messagingService.sendEmail().
Please note that 'sendEmail()' takes the following parameters : String hostname, String username, String password, String from, String to, String subject, String body, File attachment).

Where ......

hostName: name of the host eg Gmail, Hotmail, Yahoo

username: email username eg John@example.com

password: email password eg *********

from: eg john@gmail.com

to: eg doe@gmail.com

subject: "Your email subject"

body: "The body of your message"

file: File object (optional)
