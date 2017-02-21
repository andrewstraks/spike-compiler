; window["_spike_templates"] = {} 
; window["_spike_templates"]["templates_input/firstTemplate.partial.html"] = function($local) { 
; var html = "" 
; html += '' 
; html += '<div>' 
; html += '    <h1> '+$local.pageTitle+' </h1>' 
; html += '' 
;      if($local.person && $local.person.hidden == false) { 
; html += '' 
; html += '        <p spike-event="click" spike-event-click="app.partial.Menu.click('+"'"+$local.person.id+"'"+')">Name: '+$local.person.name+'</p>' 
; html += '        <p spike-translation="m_tranlsation_1 ">Surname: '+$local.person.surname+'</p>' 
; html += '' 
;         if(app.service.Cookies.hasCookie('SESSION')) { 
; html += '        <p>Warning</p>' 
;          } 
; html += '' 
; html += '        <div spike-translation="'+$local.name+'"></div>' 
; html += '        '+ app.partial.include($menu, $local.person) +'' 
; html += '' 
; html += '        <select spike-event="change" spike-event-change="app.partial.Menu.change()" >' 
; html += '            <option>xxx</option>' 
; html += '        </select>' 
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
 "                <h4 spike-translation=\"m_tranlsation_title\" class=\"modal-title\" >m_tranlsation_title class=\"modal-title\"</h4>" + 
 "            </div>" + 
 "            <div class=\"modal-body\">" + 
 "                <p>Some text in the modal.</p>" + 
 "            </div>" + 
 "            <div class=\"modal-footer\">" + 
 "                <button  spike-translation=\"m_tranlsation_title\"  type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\" > sad dClose</button>" + 
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
; html += '    <h2 spike-translation="m_tranlsation_5"' 
; html += '    data-id="2">m_tranlsation_5</h2>' 
; html += '' 
; html += '</div>' 
; return html 
; } 
