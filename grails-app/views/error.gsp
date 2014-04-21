<!doctype html>
<html>
	<head>
		<title><g:message code="error.label" default="Error Page"/> | <g:message code="default.appName.label"/></title>
		<meta name="layout" content="mainPublic">
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'errors.css')}" type="text/css">
	</head>
	<body>
	<g:set var="exceptionMsg" value="${renderException(exception:exception)}"/>
           <div class="container">
      <div class="col-sm-8">
          
            <img class="hidden-xs col-sm-5" src="${resource(dir:'images',file:'robot.png')}" alt="Notice"/>
          <div class="col-sm-6 light">
            <header>
              <h3><g:message code="error.label" default="Error Page"/></h3>
            </header>
                       
                        <p>
                          <g:link class="btn btn-lg btn-info btn-lg btn-block" controller="novadge" action="reportError" params="['message':exceptionMsg,'messageType':'ReportAnIssue']">Report it</g:link>
                        
                        </p>
                        <br/>
                        <p>
                          <a class="btn btn-lg btn-block btn-success" href="${createLink(uri: '/')}" title="${message(code:'appName.label',default:'CarePlus+')}">Go home</a>
                     
                        </p>
                       
                      
          </div>
        </div>
         
      
    </div>
          <div class="container">
		<!-<g:renderException exception="${exception}" />-->
          </div>
         
	</body>
</html>