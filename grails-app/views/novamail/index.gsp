<!--
  To change this license header, choose License Headers in Project Properties.
  To change this template file, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

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
                <g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
		</g:if>
		<table>
		<thead>
                    <tr>
				
			<g:sortableColumn property="senders" title="${message(code: 'messageIn.sender.label', default: 'Sender')}" />
			
			<g:sortableColumn property="recipients" title="${message(code: 'messageIn.recipient.label', default: 'Recipient')}" />
				
			<g:sortableColumn property="subject" title="${message(code: 'messageIn.subject.label', default: 'Subject')}" />
					
			<g:sortableColumn property="attachments" title="${message(code: 'messageIn.attachment.label', default: 'Attachment')}" />
					
			<g:sortableColumn property="status" title="${message(code: 'messageIn.status.label', default: 'Status')}" />
			<th>Body</th>		
		    </tr>
		</thead>
		<tbody>
			<g:each in="${messageInList}" status="i" var="messageIn">
                        	<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
                                	<td><g:link action="show" id="${messageIn.id}">${fieldValue(bean: messageIn, field: "senders")}</g:link></td>
					
					<td>${fieldValue(bean: messageIn, field: "recipients")}</td>
					
					<td>${fieldValue(bean: messageIn, field: "subject")}</td>
					
					
					
					<td><g:if test="${messageIn?.attachments}">${messageIn.attachments}</g:if></td>
					
					<td>${fieldValue(bean: messageIn, field: "status")}</td>
					<td>${messageIn.body}</td>
				</tr>
			</g:each>
		</tbody>
            </table>
            <div class="pagination">
		<g:paginate total="${messageInCount ?: 0}" />
            </div>
            </div>
            <div style="width: 10%; float: right; margin: 1%; padding:5px; background: lightblue;">
                
            </div>
        </div>
    </body>
</html>
