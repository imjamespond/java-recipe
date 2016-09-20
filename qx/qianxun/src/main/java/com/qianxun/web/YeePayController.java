package com.qianxun.web;

import java.util.ResourceBundle;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.yeepay.URIList.PayAPIURIList;
import com.yeepay.util.common.RandomUtil;
import com.yeepay.util.encrypt.AES;
import com.yeepay.util.encrypt.EncryUtil;
import com.yeepay.util.encrypt.RSA;

/**
 * PC网页收银台调用示例(返回pc收银台)
 * 
 * @author junqinghuang
 * 
 */
@Controller
@RequestMapping(value = "/yeepay")
public class YeePayController {

	private ResourceBundle resb1 = ResourceBundle.getBundle("payapi");

	// 从配置文件读取易宝分配的公钥
	private String yibaoPublicKey = resb1.getString("payapi.paytest_yibao_publickey");

	// 从配置文件读取商户自己的私钥
	private String merchantPrivateKey = resb1.getString("payapi.paytest_merchant_privatekey");

	// 商户自己随机生成的AESkey
	private String merchantAesKey = RandomUtil.getRandom(16);

	// 商户账户编号
	private String merchantaccount = resb1.getString("payapi.paytest_merchantaccount");

	// 从配置文件读取支付API接口URL前缀
	private String urlPrefix = resb1.getString("payapi.payweb_urlprefix");
	
	@RequestMapping("/")
	public String yeepay() {
		return "pay/yeepay/index";
	}

	@RequestMapping(value = "/to_pc_pay")
	public ModelAndView home(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String default_orderid = "12345" + System.currentTimeMillis() / 1000;
		String default_identityid = "user_" + Math.random() * 99900;
		modelAndView.addObject("default_orderid", default_orderid);
		modelAndView.addObject("default_identityid", default_identityid);
		modelAndView.addObject("default_ip", this.getRemoteIP(request));
		modelAndView.addObject("default_userua", request.getHeader("user-agent"));
		modelAndView.setViewName("pay/yeepay/index");
		return modelAndView;
	}

	/**
	 * 
	 * PC网页支付
	 * 
	 */
	@RequestMapping(value = "/pc_pay")
	public ModelAndView pcpay(Model model, @RequestParam("pay_amount") int amount,
			@RequestParam("pay_orderid") String orderid, @RequestParam("pay_orderexpdate") int orderexpdate,
			@RequestParam("pay_identityid") String identityid, @RequestParam("pay_identitytype") int identitytype,
			@RequestParam("pay_userip") String userip, @RequestParam("pay_terminalid") String terminalid,
			@RequestParam("pay_terminaltype") int terminaltype,
			@RequestParam("pay_productcatalog") String productcatalog,
			@RequestParam("pay_productname") String productname,
			@RequestParam("pay_productdesc") String productdesc, @RequestParam("pay_userua") String userua,
			@RequestParam("pay_callbackurl") String callbackurl,
			@RequestParam("pay_fcallbackurl") String fcallbackurl, HttpServletRequest request) throws Exception {

		int currency = 156;
		long i = System.currentTimeMillis() / 1000;
		System.out.println("time:" + i);
		Integer transtime = (int) i;

		TreeMap<String, Object> map = new TreeMap<String, Object>();
		map.put("merchantaccount", merchantaccount);
		map.put("amount", amount);
		map.put("currency", currency);
		map.put("identityid", identityid);
		map.put("identitytype", identitytype);
		map.put("orderid", orderid);
		map.put("terminalid", terminalid);
		map.put("terminaltype", terminaltype);
		map.put("productcatalog", productcatalog);
		map.put("productdesc", productdesc);
		map.put("productname", productname);
		map.put("transtime", transtime);
		map.put("userip", userip);
		map.put("callbackurl", callbackurl);
		map.put("fcallbackurl", fcallbackurl);
		map.put("userua", userua);
		map.put("paytypes", "1|2");// 1为借记卡2为贷记卡，1|2表示支持借记卡和贷记卡
		map.put("orderexpdate", orderexpdate);// 订单过期时间，如60分钟

		// 生成RSA签名
		String sign = EncryUtil.handleRSA(map, merchantPrivateKey);

		map.put("sign", sign);

		// 生成data
		String info = JSON.toJSONString(map);
		System.out.println("业务数据明文：" + info);
		String data = AES.encryptToBase64(info, merchantAesKey);
		System.out.println("含有签名的业务数据密文data:" + data);

		// 使用RSA算法将商户自己随机生成的AESkey加密
		String encryptkey = RSA.encrypt(merchantAesKey, yibaoPublicKey);
		System.out.println("encryptkey:" + encryptkey);

		String mobilePayUrl = urlPrefix + PayAPIURIList.PCWEB_PAY.getValue();

		// 浏览器重定向,发送get方式请求，访问一键支付网页支付地址
		// return "redirect:" + mobilePayUrl + "?" + "merchantaccount="
		// + URLEncoder.encode(merchantaccount, "UTF-8") + "&data="
		// + URLEncoder.encode(data, "UTF-8") + "&encryptkey="
		// + URLEncoder.encode(encryptkey, "UTF-8");

		// 通过页面发送post请求，访问一键支付网页支付地址
		// 使用post方式访问一键支付网页支付地址，可以防止被钓鱼，所以请尽量使用post方式
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("mobilePayUrl", mobilePayUrl);
		modelAndView.addObject("merchantaccount", merchantaccount);
		modelAndView.addObject("data", data);
		modelAndView.addObject("encryptkey", encryptkey);
		modelAndView.setViewName("pay/yeepay/to_yijian_mobile_pay");
		return modelAndView;

	}

	public String getRemoteIP(HttpServletRequest request) {
		if (request.getHeader("x-forwarded-for") == null) {
			return request.getRemoteAddr();
		}
		return request.getHeader("x-forwarded-for");
	}
}

/*
 merchantaccount 	商户账户编号 	string 	√ 	
orderid 	商户订单id 	string 	√ 	商户生成的唯一订单号，最长50位，不能含特殊符号
transtime 	交易发生时间 	int 	√ 	时间戳，例如：1361324896，精确到秒
currency 	币种 	int 	
	默认为156人民币
amount 	支付金额 	int 	√ 	以"分"为单位的整型，必须大于零
productcatalog 	商品类别码 	string 	√ 	详见商品类别码表
productname 	商品名称 	string 	√ 	最长50位，出于风控考虑，请按下面的格式传递值：应用-商品名称，如“诛仙-3阶成品天琊”，此商品名在发送短信校验的时候会发给用户，所以描述内容不要加在此参数中，以提高用户的体验度
productdesc 	商品描述 	string 	
	最长200位
identityid 	用户标识 	string 	√ 	最长50位，商户生成的用户账号唯一标识
identitytype 	用户标识类型 	int 	√ 	详见用户标识类型码表
terminaltype 	终端标识类型 	int 	
	0、IMEI；1、MAC；2、UUID；3、OTHER
terminalId 	终端标识ID 	string 	
	terminalId如果有值则terminaltype也必须有值
userip 	用户ip地址 	string 	√ 	用户支付时使用的网络终端IP
userua 	用户终端设备UA 	string 	
	用户使用的移动终端的UA信息
callbackurl 	后台回调地址 	string 	√ 	用来通知商户支付结果（页面回调以及后台通知均使用该接口）
fcallbackurl 	页面回调地址 	string 	
	用来通知商户支付结果，前后台回调地址的回调内容相同。用户在网页支付成功页面，点击“返回商户”时的回调地址
paytypes 	支付方式（信用卡or借记卡） 	string 	
	1为借记卡；2为信用卡；信用卡、借记卡都支持则传1|2；不传参视为两种方式都支持
orderexpdate 	订单有效期 	int 	
	单位：分钟，例如：60，表示订单有效期为60分钟
sign 	签名 	string 	√ 	商户使用自己生成的RSA私钥对除参数”sign”以外的其他参数进行字母排序后串成的字符串进行签名
 */
 