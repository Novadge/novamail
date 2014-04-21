
<%@ page import="com.novadge.novamail.MessageIn" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'messageIn.label', default: 'MessageIn')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-messageIn" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-messageIn" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list messageIn">
			
				<g:if test="${messageIn?.senders}">
				<li class="fieldcontain">
					<span id="sender-label" class="property-label"><g:message code="messageIn.sender.label" default="Sender" /></span>
					
						<span class="property-value" aria-labelledby="sender-label"><g:fieldValue bean="${messageIn}" field="senders"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${messageIn?.recipients}">
				<li class="fieldcontain">
					<span id="recipient-label" class="property-label"><g:message code="messageIn.recipient.label" default="Recipient" /></span>
					
						<span class="property-value" aria-labelledby="recipient-label"><g:fieldValue bean="${messageIn}" field="recipients"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${messageIn?.subject}">
				<li class="fieldcontain">
					<span id="subject-label" class="property-label"><g:message code="messageIn.subject.label" default="Subject" /></span>
					
						<span class="property-value" aria-labelledby="subject-label"><g:fieldValue bean="${messageIn}" field="subject"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${messageIn?.body}">
				<li class="fieldcontain">
					<span id="body-label" class="property-label"><g:message code="messageIn.body.label" default="Body" /></span>
					
						<span class="property-value" aria-labelledby="body-label">${messageIn?.body.encodeAsHTML()}</span>
					
				</li>
				</g:if>
			
				<g:if test="${messageIn?.attachments}">
				<li class="fieldcontain">
					<span id="attachment-label" class="property-label"><g:message code="messageIn.attachment.label" default="Attachment" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${messageIn?.status}">
				<li class="fieldcontain">
					<span id="status-label" class="property-label"><g:message code="messageIn.status.label" default="Status" /></span>
					
						<span class="property-value" aria-labelledby="status-label"><g:fieldValue bean="${messageIn}" field="status"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${messageIn?.dateSent}">
				<li class="fieldcontain">
					<span id="dateSent-label" class="property-label"><g:message code="messageIn.dateSent.label" default="Date Sent" /></span>
					
						<span class="property-value" aria-labelledby="dateSent-label"><g:formatDate date="${messageIn?.dateSent}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${messageIn?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="messageIn.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${messageIn?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${messageIn?.lastUpdated}">
				<li class="fieldcontain">
					<span id="lastUpdated-label" class="property-label"><g:message code="messageIn.lastUpdated.label" default="Last Updated" /></span>
					
						<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${messageIn?.lastUpdated}" /></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:messageIn, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${messageIn}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
