<!--
  To change this license header, choose License Headers in Project Properties.
  To change this template file, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sample title</title>
    </head>
    <body>
        <h1>Novamail</h1>
        <g:form action="checkMail">
            Provider <g:textField name="provider"/>
            <br/>
            Protocol <g:select from="${["imap","pop3"]}" name="protocol"/>
            <br/>
            Username <g:textField name="username"/>
            <br/>
            Password <g:textField name="password"/>
            
            <button>
                Check mail
            </button>
        </g:form>  

            <h2>Send mail</h2>
        <g:form action="sendMail">
            Provider <g:textField name="provider"/>
            Username <g:textField name="username"/>
            Password <g:textField name="password"/>
            <button>
                Send mail
            </button>    
        </g:form> 
    </body>
</html>
