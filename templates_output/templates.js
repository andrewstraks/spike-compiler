; window["_spike_templates"] = {} 
; window["_spike_templates"]["templates_input/firstTemplate.partial.html"] = function($local) { 
; var html = "" 
; html += '' 
; html += '<div>' 
; html += '    <h1> '+$local.pageTitle+' </h1>' 
; html += '' 
;      if($local.person && $local.person.hidden == false) { 
; html += '' 
; html += '        <p onclick="$local.click('+"'"+$local.person.id+"'"+')">Name: '+$local.person.name+'</p>' 
; html += '        <p>Surname: '+$local.person.surname+'</p>' 
; html += '' 
;         if(app.service.Cookies.hasCookie('SESSION')) { 
; html += '        <p>Warning</p>' 
;          } 
; html += '' 
; html += '        '+ app.partial.include(app.partial.Menu, $local.person) +'' 
; html += '' 
;      } 
; html += '' 
; html += '</div>' 
; return html 
; } 
; window["_spike_templates"]["templates_input/normal.html"] =  "<button type=\"button\" class=\"btn btn-info btn-lg\" data-toggle=\"modal\" data-target=\"#myModal\">Open Modal</button>" + 
 "" + 
 "<div id=\"myModal\" class=\"modal fade\" role=\"dialog\">" + 
 "    <div class=\"modal-dialog\">" + 
 "" + 
 "        <div class=\"modal-content\">" + 
 "            <div class=\"modal-header\">" + 
 "                <button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>" + 
 "                <h4 class=\"modal-title\">Modal Header</h4>" + 
 "            </div>" + 
 "            <div class=\"modal-body\">" + 
 "                <p>Some text in the modal.</p>" + 
 "            </div>" + 
 "            <div class=\"modal-footer\">" + 
 "                <button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Close</button>" + 
 "            </div>" + 
 "        </div>" + 
 "" + 
 "    </div>" + 
 "</div>" ; 
; window["_spike_templates"]["templates_input/secondTemplate.partial.html"] = function($local) { 
; var html = "" 
; html += '' 
; html += '<div>' 
; html += '    <h1> '+$local.pageTitle+' </h1>' 
; html += '' 
; html += '    '+ app.component.Menu.getCategories($local.pageTitle) +'' 
; html += '' 
;      $.each($local.users, function(user, i) { 
; html += '' 
; html += '        <p>Id: '+user.id+'; Name: '+user.name+'</p>' 
; html += '' 
; html += '        '+ $local.pageTitle +'  '+ $local.pageName +'  '+ $local.author +'' 
; html += '' 
;     }); 
; html += '' 
; html += '</div>' 
; return html 
; } 
