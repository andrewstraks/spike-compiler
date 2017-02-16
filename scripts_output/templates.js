; window["_spike_templates"] = {} 
; window["_spike_templates"]["input\firstTemplate"] = function($local) { 
; var html = "" 
 html += '<div>' 
 html += '    <h1> '+$local.pageTitle+' </h1>' 
 html += '' 
;      if($local.person && $local.person.hidden == false) { 
 html += '' 
 html += '        <p onclick="$local.click('+"'"+$local.person.id+"'"+')">Name: '+$local.person.name+'</p>' 
 html += '        <p>Surname: '+$local.person.surname+'</p>' 
 html += '' 
;      } 
 html += '' 
 html += '</div>' 
; return html 
; } 
; window["_spike_templates"]["input\secondTemplate"] = function($local) { 
; var html = "" 
 html += '<div>' 
 html += '    <h1> '+$local.pageTitle+' </h1>' 
 html += '' 
;      $.each($local.users, function(user, i) { 
 html += '' 
 html += '        <p>Id: '+user.id+'; Name: '+user.name+'</p>' 
 html += '' 
;     }); 
 html += '' 
 html += '</div>' 
; return html 
; } 
