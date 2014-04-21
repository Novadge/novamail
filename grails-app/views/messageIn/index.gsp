
<%@ page import="com.novadge.novamail.MessageIn" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'messageIn.label', default: 'MessageIn')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-messageIn" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-messageIn" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="sender" title="${message(code: 'messageIn.sender.label', default: 'Sender')}" />
					
						<g:sortableColumn property="recipient" title="${message(code: 'messageIn.recipient.label', default: 'Recipient')}" />
					
						<g:sortableColumn property="subject" title="${message(code: 'messageIn.subject.label', default: 'Subject')}" />
					
						<g:sortableColumn property="body" title="${message(code: 'messageIn.body.label', default: 'Body')}" />
					
						<g:sortableColumn property="attachment" title="${message(code: 'messageIn.attachment.label', default: 'Attachment')}" />
					
						<g:sortableColumn property="status" title="${message(code: 'messageIn.status.label', default: 'Status')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${messageInList}" status="i" var="messageIn">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${messageIn.id}">${fieldValue(bean: messageIn, field: "sender")}</g:link></td>
					
						<td>${fieldValue(bean: messageIn, field: "recipient")}</td>
					
						<td>${fieldValue(bean: messageIn, field: "subject")}</td>
					
						<td>${fieldValue(bean: messageIn, field: "body")}</td>
					
						<td>${fieldValue(bean: messageIn, field: "attachment")}</td>
					
						<td>${fieldValue(bean: messageIn, field: "status")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${messageInCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
