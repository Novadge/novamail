<!--
  To change this license header, choose License Headers in Project Properties.
  To change this template file, choose Tools | Templates
  and open the template in the editor.
-->
<%@ page import="com.novadge.novamail.MessageIn" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>


<html>
    <head>
        <meta name="layout" content="main">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Novamail</title>
    </head>
    <body>
        <h1>Novamail</h1>
        <div style="width: 100%;">
            <div style="width: 98%; margin:1%; padding:5px; background: lightblue;">
                <g:link action="refresh">Refresh</g:link>
                <g:link action="index">mail index</g:link> 
                 
            </div>
        
            <div style="width: 20%; float: left; margin: 1%; padding:5px; background: lightblue;">
                <g:link action="compose">Compose</g:link>
                <ul id="panel">
                    <li><g:link action="inbox">Inbox</g:link></li>
                    <li><g:link action="sent">Sent</g:link></li>
                </ul>
            </div> 
            
            <div style="width: 63%; float: left; margin: 1% auto 1% auto; padding:5px; background: whitesmoke;">
                
                    <iframe seamless srcdoc="${content}" sandbox style="width:100%; height:400px;">
                        
                    </iframe>
                   
            </div>
            <div style="width: 10%; float: right; margin: 1%; padding:5px; background: lightblue;">
                
            </div>
        </div>
    </body>
</html>
