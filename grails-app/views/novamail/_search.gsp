<div class="input-group">
  

  <g:textField class="form-control" name="q" id="search" value="${params.q}" placeholder=""/>        

  <span class="input-group-btn">
    <button class="btn btn-primary" id="submit" type="submit">
      <span class="glyphicon glyphicon-search"></span>
    </button>
  </span>

</div>
                          <script>
                  $(document).ready(function() {
                    $("#search").autocomplete({
                      source: function(request, response){
                      $.ajax({
                        url: "${createLink(controller:'novamail', action:'ajaxList')}", // remote datasource
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
                      
                      var url = "${createLink(controller:'novamail',action:'show')}";
                      var suf = "/"+ui.item.id
                      
                        window.location.replace(url+suf);
                                               

                      }
                      
                    });
                    
                    });
                  </script>
