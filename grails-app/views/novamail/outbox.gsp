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
        <div class="navj collapse in" role="navigation">
      <ul>
	<li><g:link controller="dashboard" action="index" class="dashboard" title="Dashboard"><g:message code="dashboard.label" default="Dashboard"/></g:link></li>
        
        <li><g:link class="email" action="index">Mail</g:link> </li>
      </ul>
    </div>
        
        
        <div class="row">
                   
            <div class="col-sm-3">
                 <g:render template="../novamail/panel"/>
                
                
            </div> 
            <div class="col-sm-7">
            <g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
		</g:if>
                
            <div class="panel panel-default">
                <div class="panel-heading">
                    <g:render template="../novamail/search"/>
                 </div>   
                <div class="panel-body">
		<table class="table">
		<thead>
                    <tr>
				
			<g:sortableColumn property="recipient" title="${message(code: 'messageOut.recipient.label', default: 'Recipient')}" />
			
				
			<g:sortableColumn property="subject" title="${message(code: 'messageOut.subject.label', default: 'Subject')}" />
					
			<th>Status</th>		
			<g:sortableColumn property="dateSent" title="${message(code: 'messageOut.dateSent.label', default: 'Date')}" />
					
		    </tr>
		</thead>
		<tbody>
                    
			<g:each in="${messageOutList}" status="i" var="messageOut">
                        	<tr class=" ${messageOut.status == 'Read'? '':'bold'}">
					
                                	<td><g:link action="showOut" id="${messageOut.id}">${fieldValue(bean: messageOut, field: "recipients")}</g:link></td>
					
					<td>${fieldValue(bean: messageOut, field: "subject")}</td>
					
					<td>${messageOut.status}</td>
                                        <td><g:formatDate date="${messageOut.dateSent}" style="MEDIUM" type="datetime"/>					
				</tr>
			</g:each>
		</tbody>
            </table>
            <div class="pagination">
		<g:paginate total="${messageOutCount ?: 0}" />
            </div>
            </div>
            </div>
          </div>
            <div class="col-sm-2">
                
            </div>
        </div>
    </body>
</html>
