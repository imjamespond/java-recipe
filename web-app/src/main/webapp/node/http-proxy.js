var httpProxy = require('http-proxy');
httpProxy.createProxyServer({target:'http://qy1:8080'}).listen(8000); 