novamail
========
Description
The Novamail plug-in provides e-mail sending capabilities to a Grails application. It is also capable of sending emails asynchronously by using a scheduled Job.

Usage

Inject messagingService into your class

<em>def messagingService</em>

messagingService is a Grails service that provides a single method called sendEmail that takes parameters.
Please note that 'sendEmail()' takes the following parameters :

String hostname, String username, String password, String from, String to, String subject, String body, File attachment)
.
Where ......


hostName: name of the host eg Gmail, Hotmail, Yahoo

username: email username eg John@example.com

password: email password eg *

from: eg john@gmail.com

to: eg doe@gmail.com

subject: "Your email subject"

body: "The body of your message"

file: File object (optional)



An example usage can be seen below.
<code>
Class yourControllerOrService{

  def messagingService
  
  def yourMethod(){	
  
    messagingService.sendEmail(
    
      "Gmail",
      
      "john@gmail.com",
      
      "password",
      
      "john@gmail.com",
      
      "recipient@gmail.com",
      
      "email subject",
      
      "email body"
      
    )
    
  }
  
}</code>
