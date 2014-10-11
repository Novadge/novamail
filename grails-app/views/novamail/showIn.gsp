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
        <title>${message(code:'novamail.inbox.label',default:'Inbox')} | ${message(code:'app.novamail.name',default:'Novamail')} </title>
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

                <div class="panel panel-default">
                    <div class="panel-heading">${fieldValue(bean: messageInInstance, field: "senders")}</div>
                    <div class="panel-body">

                        <iframe seamless="true" sandbox="" width="100%" height="400" srcdoc="${body?.encodeAsHTML()}">
                        Your browser does not support iframes. 
                        </iframe>
                        <ul class="list-unstyled list-inline">
                            <g:each in="${messageInInstance?.attachments}" var="att">
                                <li>
                                    <g:link controller="novamail" action="download" id="${att.id}"><span class="glyphicon glyphicon-paperclip"></span> ${att?.name}</g:link>

                                </li>

                            </g:each>
                        </ul>
                        <br/>
                        <div class="">
                            <g:form action="delete" method="DELETE">
                                <g:hiddenField name="id" value="${messageInInstance?.id}"/>
                                <button class="btn btn-danger">
                                    <span class="glypicon glyphicon-remove"></span> Delete
                                </button>
                            </g:form>
                        </div>    
                    </div>
                </div>
            </div>
            <div class="col-sm-2">

            </div>
        </div>
    </body>
</html>
