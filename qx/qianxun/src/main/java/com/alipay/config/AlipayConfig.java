package com.alipay.config;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {
	
	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = "2088711046018196";
	// 商户的私钥
	public static String key = "ob8s0kh3phoaspxxoxy65om53kq7dxsx";

	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	public static String email = "yniuchen@msn.com";
	

	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "D:\\";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
	
	// 签名方式 不需修改
	public static String sign_type = "MD5";
	
//	银行简码——纯借记卡渠道
//	表
//	10-7
//	银行简码——纯借记卡渠道
//	银行简码
//	银行名称
	public static final String CMB_DEBIT = "CMB-DEBIT";//招商银行
	public static final String CCB_DEBIT = "CCB-DEBIT";//中国建设银行
	public static final String ICBC_DEBIT = "ICBC-DEBIT";//中国工商银行
	public static final String COMM_DEBIT = "COMM-DEBIT";//交通银行
	public static final String GDB_DEBIT = "GDB-DEBIT";//广发银行
	public static final String BOC_DEBIT = "BOC-DEBIT ";//中国银行
	public static final String CEB_DEBIT = "CEB-DEBIT";//中国光大银行
	public static final String SPDB_DEBIT = "SPDB-DEBIT";//上海浦东发展银行
	public static final String PSBC_DEBIT = "PSBC-DEBIT";//	中国邮政储蓄银行
	public static final String BJBANK = "BJBANK";//	北京银行
	public static final String SHRCB = "SHRCB";//	上海农商银行
	public static final String WZCBB2C_DEBIT = "WZCBB2C-DEBIT";//	温州银行
	public static final String COMM = "COMM";//	交通银行
	public static final String CMBC = "CMBC";//	中国民生银行
	public static final String BJRCB = "BJRCB";//	北京农村商业银行
	public static final String SPA_DEBIT = "SPA-DEBIT";//	平安银行 

}
