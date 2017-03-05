package com.spike.security

import java.util.concurrent.ThreadLocalRandom

/**
 * Created by Dawid on 2017-03-04.
 */
class SecurityCompiler {

    def secure(String body) {

        String output = '(function() { '

        output += 'window._r_fn=function(){var a;window._c_ip?(a="%"+app.util.System.bindStringParams(app.config.securityHeaderWithIP,{ip:window._c_ip}),a+="%c\\n"+app.config.securityText):(a="%"+app.config.securityHeaderWithoutIP,a+="%c\\n"+app.config.securityText),window._qpl.log(a,"color: RED; font-size:20px;","color:blue;font-size:14px;")},window._qpl=window.console,window._c_ip=null,window.console={log:function(){window._r_fn()}},window.alert=window._r_fn;var xmlHttp=new XMLHttpRequest;xmlHttp.onreadystatechange=function(){4==xmlHttp.readyState&&200==xmlHttp.status&&(window._c_ip=JSON.parse(xmlHttp.responseText).ip)},xmlHttp.open("GET","http://jsonip.com/",!0),xmlHttp.send(null);'

        output += ' '+body + ' })();'

        return output

    }

}
