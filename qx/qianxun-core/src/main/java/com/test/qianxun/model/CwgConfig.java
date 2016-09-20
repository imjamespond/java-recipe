package com.test.qianxun.model;
@Deprecated
public class CwgConfig {

	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = "";
	// 商户的私钥
	public static String key = "";

	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	

	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "D:\\";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
	
	// 签名方式 不需修改
	public static String sign_type = "MD5";
	// 纯网关接口名称
	public static String service = "create_direct_pay_by_user";
	// 支付类型 1商品购买
	public static String payment_type = "1";
	// 服务器异步通知页面路径
	public static String notify_url = "http://商户网关地址/create_direct_pay_by_user-JAVA-UTF-8/notify_url.jsp";
	// 页面跳转同步通知页面路径
	public static String return_url = "http://商户网关地址/create_direct_pay_by_user-JAVA-UTF-8/return_url.jsp";
	// 卖家支付宝帐户
	public static String seller_email = "";
	// 默认支付方式
	public static String paymethod = "bankPay";
	// 商品名称
	public static String subject = "千寻用户充值";
}