var express = require('express');
var request = require('request'); 
//https://github.com/request/request#proxies[Cookies are disabled by default ]
request = request.defaults({jar: true})
var path = require('path');
var app = express();
var router = express.Router();

const kPrefix = "/data-center"
var URL,JS_Path,Html_Path = null
CSS_Path = path.join(__dirname, '../css') 
JS_Path = path.join(__dirname, '../js') 
Html_Path = path.join(__dirname, '../html') 
if(process.argv.length>1 && process.argv[2]){
  URL = process.argv[2]+kPrefix
  if(process.argv.length>2 && process.argv[3]){
    JS_Path = path.join(process.cwd(), process.argv[3])
  } 
}else
  URL = "http://qy1:8080"+kPrefix
console.log(URL)
console.log(JS_Path)

// simple logger for this router's requests
// all requests to this router will first hit this middleware
router.use(function(req, res, next) {
  console.log('%s %s', req.method, req.url);//, req.path);
  next();
});

// this will only be invoked if the path ends in /bar
router.use('/foobar', function(req, res, next) {
  res.send('foo bar');
});

// always invoked
router.use(function(req, res, next) {
  var url = URL + req.url
  var cookie = req.headers.cookie
  if(req.method=="GET")
    request({url: url, method: "GET"}).pipe(res);
  else if(req.method=="POST")
    req.pipe(request.post(url, {form:req.body})).pipe(res);
});

app.use(kPrefix+"/css", express.static(CSS_Path));
app.use(kPrefix+"/js", express.static(JS_Path));
app.use(kPrefix+"/html", express.static(Html_Path));
app.use(kPrefix, router);

var server = app.listen(3000, function () {
  var host = server.address().address;
  var port = server.address().port;

  console.log('app listening at http://%s:%s', host, port);
});