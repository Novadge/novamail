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
                    <div class="panel-heading">
                        <g:render template="../novamail/search"/>
                    </div>   
                    <div class="panel-body">
                        <table class="table">
                            <thead>
                                <tr>

                                    <g:sortableColumn property="senders" title="${message(code: 'messageIn.sender.label', default: 'Sender')}" />


                                    <g:sortableColumn property="subject" title="${message(code: 'messageIn.subject.label', default: 'Subject')}" />

                                    <th></th>		
                                        <g:sortableColumn property="dateReceived" title="${message(code: 'messageIn.dateReceived.label', default: 'Date')}" />

                                </tr>
                            </thead>
                            <tbody>

                                <g:each in="${messageInList}" status="i" var="messageIn">
                                    <tr class="${(i % 2) == 0 ? 'even' : 'odd'} ${messageIn.status == 'Read'? '':'bold'}">

                                        <td><g:link action="showIn" id="${messageIn.id}">${fieldValue(bean: messageIn, field: "senders")}</g:link></td>

                                        <td>${fieldValue(bean: messageIn, field: "subject")}</td>


                                        <td><g:if test="${messageIn?.attachments}"><span class="glyphicon glyphicon-paperclip"> </span> </g:if></td>
                                        <td><g:formatDate date="${messageIn.dateReceived}" style="MEDIUM" type="datetime"/>					
                                    </tr>
                                </g:each>
                            </tbody>
                        </table>
                        <div class="pagination">
                            <g:paginate total="${messageInCount ?: 0}" />
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-sm-2">

            </div>
        </div>

        <script>
            $(document).ready(function() {
            $("#search").autocomplete({
            source: function(request, response){
            $.ajax({
                        url: "${createLink(controller:'novamail', action:'ajaxListInbox')}", // remote datasource
            data: request,
            success: function(data){
            response(data); // set the response
            },
            error: function(){ // handle server errors
            $.jGrowl("Unable to retrieve Patient list", {
            theme: 'ui-state-error ui-corner-all'   
            });
            }
            });
            },
            minLength: 2, // triggered only after minimum 2 characters have been entered.
            select: function(event, ui) { // event handler when user selects a company from the list.

                      var url = "${createLink(controller:'novamail',action:'showIn')}";
            var suf = "/"+ui.item.id

            window.location.replace(url+suf);


            }

            });

            });
        </script>

    </body>
</html>
