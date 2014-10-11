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
        <title>${message(code:'novamail.compose.label',default:'Compose')} | ${message(code:'app.novamail.name',default:'Mail')} </title>
    </head>
    <body>
        <div class="navj collapse in" role="navigation">
            <ul>
                <li><g:link controller="dashboard" action="index" class="dashboard" title="Dashboard"><g:message code="dashboard.label" default="Dashboard"/></g:link></li>

                <li><g:link class="email" action="index">${message(code:'novamail.home.label',default:'Mail')}</g:link> </li>
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

                <g:hasErrors bean="${messageOutInstance}">
                    <ul class="alert alert-danger" role="alert">
                        <g:eachError bean="${messageOutInstance}" var="error">
                            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                            </g:eachError>
                    </ul>
                </g:hasErrors>    
                <div class="panel panel-default">
                    <g:form controller="novamail" action="saveMsgOut" enctype="multipart/form-data">
                        <div class="panel-heading">
                            <label for="recipients">
                                <g:message code="messageOutInstance.recipients.label" default="recipients" />

                            </label>
                            <g:textField class="form-control" name="recipients" value="${messageOutInstance?.recipients}"/>
                        </div>   
                        <div class="panel-body">
                            <div class="form-group ${hasErrors(bean: messageOutInstance, field: 'subject', 'has-error')} ">
                                <label for="subject">
                                    <g:message code="messageOutInstance.subject.label" default="Subject" />

                                </label>
                                <g:textField class="form-control" name="subject" value="${messageOutInstance?.subject}"/>
                            </div>
                            <div class="form-group ${hasErrors(bean: messageOutInstance, field: 'body', 'has-error')} ">
                                <label for="body">
                                    <g:message code="messageOutInstance.body.label" default="Body" />

                                </label>
                                <g:textArea class="form-control" name="body" value="${messageOutInstance?.body}"/>
                            </div>

                            <g:if test="${simpleDocument}">
                                <g:hiddenField name="document.id" value="${simpleDocument?.id}" />
                                <g:link class="" controller="document" action="display" id="${simpleDocument?.id}" name="Play">
                                    <span class="glyphicon glyphicon-paperclip"></span> ${simpleDocument?.originalName}
                                </g:link>
                            </g:if>
                            <div class="form-group ${hasErrors(bean: messageOutInstance, field: 'attachments', 'has-error')} ">
                                <label for="attachment">
                                    <g:message code="messageOutInstance.attachment.label" default="Attachment" />

                                </label>

                                <input type="file" name="attachment" />
                            </div>

                            <button class="btn btn-primary">
                                <span class="glyphicon glyphicon-send"></span> Send
                            </button>    


                        </div>
                    </g:form>
                </div>
            </div>
            <div class="col-sm-2">

            </div>
        </div>
    </body>
</html>
