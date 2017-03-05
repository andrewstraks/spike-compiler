
    window['_r_fn'] = function(){

        var _text;
        if(window['_c_ip']){
            _text = "%"+app.util.System.bindStringParams(app.config.securityHeaderWithIP, { ip: window['_c_ip']});
            _text += "%c\n"+app.config.securityText;
        }else{
            _text = "%"+app.config.securityHeaderWithoutIP;
            _text += "%c\n"+app.config.securityText;
        }

        window['_qpl'].log(_text, "color: RED; font-size:20px;", "color:blue;font-size:14px;");
    };

    window['_qpl'] = window['console'];
    window['_c_ip'] = null;
    window['console'] = {
        log: function(){
            window['_r_fn']();
        }
    };
    window['alert'] =  window['_r_fn'];

    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200){
            window['_c_ip'] = JSON.parse(xmlHttp.responseText).ip;
        }
    }
    xmlHttp.open("GET", 'http://jsonip.com/', true); // true for asynchronous
    xmlHttp.send(null);
