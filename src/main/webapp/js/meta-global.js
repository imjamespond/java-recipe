function MessageBox(msg, dlg){
	alert(msg);
}
function Reload(url){
	if(url) window.location.href = url;
	else window.location.reload();
}
function GetUrlParts(url) {
    var a = document.createElement('a');
    a.href = url;
    return {
        href: a.href,
        host: a.host,
        hostname: a.hostname,
        port: a.port,
        pathname: a.pathname,
        protocol: a.protocol,
        hash: a.hash,
        search: a.search
    };
}
// ***js基础封装***//
// 通过class获取元素
function GetClass(oParent, sClass){
    var oP = arguments.length == 1? document : oParent;
    var aEle=oP.getElementsByTagName('*');
    var aResult=[];
    var re = new RegExp("\\b" + sClass + "\\b");
    var i=0;
    
    for(i=0;i<aEle.length;i++)
    {
        if(re.test(aEle[i].className))
        {
            aResult.push(aEle[i]);
        }
    }
    
    return aResult;
}

//***js实用封装***//
//cookie相关方法
var CookieUtil = {
	//设置cookie
	SetCookie: function(name, value, iDay){
		var oDate = new Date();
		oDate.setDate(oDate.getDate + iDay);
		document.cookie = name + "=" + value + ",expires =" + oDate;
	},
	//获取cookie
	GetCookie: function(name){
		var arr = document.cookie.split(";"); //注意是分号加一个空格
		var i = 0;
		for(i=0;i<arr.length;i++){
			var arr2 = arr[i].split("=");
			if(arr2[0] == name){
				return arr2[1];
			}
		}
		return "";
	},
	//删除cookie
	RemoveCookie: function(name){
		this.setCookie(name,"1",-1); //过期时间设置为-1，意思就是昨天就过期了
	}
}

//***jQuery通用封装***//
//倒计时
var CountDown = function(num,callback){
	var s = num + 1;
	function reduce(){
		s = s - 1;
		setTimeout(function(){
			if(s>0){
				reduce();
			}else{
				callback();
			}
		},1000);
	}
	return reduce();
}