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



An example usage can be seen below.<br>
<code>
Class yourControllerOrService{<br>
  def messagingService<br>
  def yourMethod(){<br>
    messagingService.sendEmail(<br>
      "Gmail",<br>
      "john@gmail.com",<br>
      "password",<br>
      "john@gmail.com",<br>
      "recipient@gmail.com",<br>
      "email subject",<br>
      "email body"<br>
    )<br>
  }<br>
}
</code>
