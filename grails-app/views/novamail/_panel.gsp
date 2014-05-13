<div class="panel panel-default">
                    
                    <div class="panel-body">
                        
                            <g:link class="btn btn-default btn-block" action="refresh"><span class="glypicon glyphicon-refresh"> </span> ${message(code:'novamail.refresh.label',default:'Refresh')}</g:link>
                            <g:link class="btn btn-primary btn-block" action="compose"><span class="glypicon glyphicon-edit"> </span> ${message(code:'novamail.compose.label',default:'Compose')}</g:link>
                        <br/>
                         <div class="btn-group btn-group-justified">
                             <g:link class="btn btn-default btn-block" action="outbox"><span class="glypicon glyphicon-transfer"> </span> ${message(code:'novamail.outbox.label',default:'Outbox')}</g:link>
                        <g:link class="btn btn-default" action="inbox"><span class="glypicon glyphicon-inbox"> </span> ${message(code:'novamail.inbox.label',default:'Inbox')} <span class="badge">${unreadCount}</span></g:link>
                           </div>
                    </div>
                </div>