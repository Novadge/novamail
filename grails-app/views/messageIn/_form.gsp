<%@ page import="com.novadge.novamail.MessageIn" %>



<div class="fieldcontain ${hasErrors(bean: messageIn, field: 'sender', 'error')} ">
	<label for="sender">
		<g:message code="messageIn.sender.label" default="Sender" />
		
	</label>
	<g:textField name="sender" value="${messageIn?.sender}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: messageIn, field: 'recipient', 'error')} required">
	<label for="recipient">
		<g:message code="messageIn.recipient.label" default="Recipient" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="recipient" required="" value="${messageIn?.recipient}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: messageIn, field: 'subject', 'error')} ">
	<label for="subject">
		<g:message code="messageIn.subject.label" default="Subject" />
		
	</label>
	<g:textField name="subject" value="${messageIn?.subject}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: messageIn, field: 'body', 'error')} required">
	<label for="body">
		<g:message code="messageIn.body.label" default="Body" />
		<span class="required-indicator">*</span>
	</label>
	<g:textArea name="body" cols="40" rows="5" maxlength="8192" required="" value="${messageIn?.body}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: messageIn, field: 'attachment', 'error')} ">
	<label for="attachment">
		<g:message code="messageIn.attachment.label" default="Attachment" />
		
	</label>
	<input type="file" id="attachment" name="attachment" />

</div>

<div class="fieldcontain ${hasErrors(bean: messageIn, field: 'status', 'error')} ">
	<label for="status">
		<g:message code="messageIn.status.label" default="Status" />
		
	</label>
	<g:select name="status" from="${messageIn.constraints.status.inList}" value="${messageIn?.status}" valueMessagePrefix="messageIn.status" noSelection="['': '']"/>

</div>

<div class="fieldcontain ${hasErrors(bean: messageIn, field: 'dateSent', 'error')} ">
	<label for="dateSent">
		<g:message code="messageIn.dateSent.label" default="Date Sent" />
		
	</label>
	<g:datePicker name="dateSent" precision="day"  value="${messageIn?.dateSent}" default="none" noSelection="['': '']" />

</div>

