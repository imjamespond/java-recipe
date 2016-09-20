/**
 * 后台专用 - 公用函数
 * User: jinli.yuan
 * Date: 12-8-22
 * Time: 上午11:08
 * To change this template use File | Settings | File Templates.
 */

/* json 返回的状态码 */
var success_pp = 100;
var unknown_pp = 101;
var error_pp = 102;
var param_error_pp = 103;
var unlogin_pp = 104;
var not_found_pp = 105;
var locked_pp = 106;
var unidentified_pp = 107;
var fail_pp = 108;

var success_pp_msg = "操作成功";
var unknown_pp_msg = "未知";
var error_pp_msg = "程序异常，请联系管理员";
var param_error_pp_msg ="传递的参数不正确";
var unlogin_pp_msg = "未登录";
var not_found_pp_msg = "未找到记录";
var locked_pp_msg = "当前记录被锁定,无法操作";
var unidentified_pp_msg = "不一致";
var fail_pp_msg = "操作失败";

/**
 * json To Object
 */
function jsonToObject(obj) {
    var json = obj;
    if($.isBlank(json)){
        return null;
    }
    if(typeof (json) != "object"){
        json = eval("("+json+")");
    }
    return json;
}

/**
 * 对Action返回的WebResult状态进行处理
 * @param json WebResult
 * @return callbackType
 */
function jsonCallback(json){
    return jsonCallback(json,true);
}

/**
 * 对Action返回的WebResult状态进行处理
 * @param json WebResult
 * @param successFlag 成功是否弹出，成功提示 , true : 提示 ， flase ：不提示
 * @return callbackType
 */
function jsonCallback(json,successFlag){
    if(json ==null){
        showAlert('Error - jsonCallback','返回的JSON为空.');
        return null;
    }

    if(successFlag ==null){
        successFlag = true;
    }

    if(typeof (json) != "object"){
        json = eval("("+json+")");
    }

    var status = json.status;
    var message = json.message;
    var callbackType = json.callbackType;

    if($.isBlank(status)){
        showAlert('Error','WebResult status == null');
        return null;
    }

    switch (status){
        case success_pp :
            if(successFlag){
                showAlert(status,getMessage(message,success_pp_msg));
            }
            break;
        case unknown_pp :
            showAlert(status,getMessage(message,unknown_pp_msg));
            break;
        case error_pp :
            showAlert(status,getMessage(message,error_pp_msg));
            break;
        case param_error_pp :
            showAlert(status,getMessage(message,param_error_pp_msg));
            break;
        case unlogin_pp :
            showAlert(status,getMessage(message,unlogin_pp_msg));
            break;
        case not_found_pp :
            showAlert(status,getMessage(message,not_found_pp_msg));
            break;
        case locked_pp :
            showAlert(status,getMessage(message,locked_pp_msg));
            break;
        case unidentified_pp :
            showAlert(status,getMessage(message,unidentified_pp_msg));
            break;
        case fail_pp :
            showAlert(status,getMessage(message,fail_pp_msg));
            break;
        default :
            showAlert(status,getMessage(message,status));
            break;
    }

    return callbackType;
}

/**
 * 提示信息
 * @param msg 自定义信息
 * @param default_msg 默认信息
 */
function getMessage(msg,default_msg) {
    if(!$.isBlank(msg)){
        return msg;
    }
    return default_msg;
}

/**
 * alert弹窗
 * @param title
 * @param msg
 */
function showAlert(title,msg) {
    $.messager.alert(title,msg);
    return ;
}

/**
 * 居中 top值
 * @param height
 * @return {Number}
 */
function getCenterTop(height){
    if($.isBlank(height)){
        return 0;
    }
    return ($(window).height()/2 - parseInt(height)/2)
}

/**
 * 居中 left值
 * @param width
 * @return {Number}
 */
function getCenterLeft(width){
    if($.isBlank(width)){
        return 0;
    }
    return (($(window).width())/2 - (parseInt(width)/2) + document.documentElement.scrollTop );
}

/**
 * 初始化 弹出window
 * @param objId
 * @param width
 * @param height
 * @param title
 */
function showInitWindow(objId,width,height,title){
    $('#'+objId).window({
        width:width,
        height:height,
        title : title,
        top : getCenterTop(height),
        left :getCenterLeft(width),
        collapsible : false,
        minimizable : false,
        modal:true
    });
}

/**
 * 打开window
 * @param objId
 */
function showWindow(objId){
    $('#'+objId).window("open");
}

/**
 * 关闭windos
 * @param objId
 */
function closeWindos(objId){
    $('#'+objId).window("close");
}

/** 显示指定大小的图片 */
function act_resize_img(imgObj, rectWidth, rectHeight, fixIeBug){
	try	{
		if(!fixIeBug) fixIeBug = true;
		//修正在IE运行下的问题
		if((imgObj.width==0 || imgObj.height==0) && fixIeBug) {
			var timer = setInterval(function(){act_resize_img(imgObj, rectWidth, rectHeight, false);clearInterval(timer);}, 1000);	
			return;
		}
		var x = imgObj.width>rectWidth ? rectWidth : imgObj.width;
		var y = imgObj.height>rectHeight ? rectHeight : imgObj.height;
		var scale = imgObj.width/imgObj.height;
	
		if( x>y*scale) {
			imgObj.width	= Math.floor(y*scale);
			imgObj.height	= y;
		} else {
			imgObj.width	= x;
			imgObj.height	= Math.floor(x/scale);
		}
		imgObj.style.width = imgObj.width+"px";
		imgObj.style.height = imgObj.height+"px";

		if (typeof(imgObj.onload)!='undefined'){
			imgObj.onload=null;
		}
	} catch(err) {	
	}
}
