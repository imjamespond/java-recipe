package com.qianxun.web;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipayNotify;
import com.qianxun.model.FlyUser;
import com.qianxun.model.PaymentPersist;
import com.qianxun.service.FlyUserService;
import com.qianxun.service.PaymentPersistService;
import com.test.qianxun.model.User;
import com.test.qianxun.service.SessionService;
import com.test.qianxun.service.UserService;

@Controller
@RequestMapping(value = "/alipay")
public class AlipayController {
	@Autowired
	private UserService userService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private PaymentPersistService paymentPsService;
	@Autowired
	private FlyUserService flyUserPsService;


	@RequestMapping(value = "/ali", method = { RequestMethod.GET, RequestMethod.POST })
	public String ali() {

		return "pay/alipay";
	}

	@RequestMapping(value = "/bank", method = { RequestMethod.GET, RequestMethod.POST })
	public String bank(HttpServletRequest request, HttpServletResponse response) {

		request.setAttribute("WIDdefaultbank", AlipayConfig.BJBANK);
		return "pay/bank";
	}

	@RequestMapping(value = "/alipayapi", method = { RequestMethod.GET, RequestMethod.POST })
	public String alipayapi(HttpServletRequest request, HttpServletResponse response) {
		
		User user = userService.findByUsername(request.getParameter("account"));
		if(null == user){
			request.setAttribute("message", "请先从网站注册并登录");
			return "error";
		}
		long uid = user.getId();
		FlyUser flyUser = flyUserPsService.get(uid);
		if(null == flyUser){
			request.setAttribute("message", "您还没有游戏帐号");
			return "error";
		}
		
		String payment = (String)request.getParameter("WIDtotal_fee");

		PaymentPersist payp = new PaymentPersist();
		payp.setUid(uid);
		payp.setCreateTime(System.currentTimeMillis());
		payp.setDescription("");
		payp.setPayment(Integer.valueOf(payment));
		payp.setOrderId(request.getParameter("orderid"));
		payp.setType(1);

		/*long id = 0l;*/
		try {
			paymentPsService.save(payp);
		} catch (DuplicateKeyException e) {
			request.setAttribute("message","订单号重复");
			return "error";
		} catch (DataAccessException e) {
			e.printStackTrace();
		}

		request.setAttribute("WIDsubject", payp.getOrderId());
		request.setAttribute("WIDout_trade_no", request.getParameter("orderid"));
		return "pay/alipayapi";
	}

	@RequestMapping(value = "/bankapi", method = { RequestMethod.GET, RequestMethod.POST })
	public String bankapi(HttpServletRequest request, HttpServletResponse response) {
		
		User user = userService.findByUsername(request.getParameter("account"));
		if(null == user){
			request.setAttribute("message", "请先从网站注册并登录");
			return "error";
		}
		long uid = user.getId();
		FlyUser flyUser = flyUserPsService.get(uid);
		if(null == flyUser){
			request.setAttribute("message", "您还没有游戏帐号");
			return "error";
		}
		
		String payment = (String)request.getParameter("WIDtotal_fee");

		PaymentPersist payp = new PaymentPersist();
		payp.setUid(uid);
		payp.setCreateTime(System.currentTimeMillis());
		payp.setDescription("");
		payp.setPayment(Integer.valueOf(payment));
		payp.setOrderId(request.getParameter("orderid"));
		payp.setType(1);

		/*long id = 0l;*/
		try {
			paymentPsService.save(payp);
		} catch (DuplicateKeyException e) {
			request.setAttribute("message","订单号重复");
			return "error";
		} catch (DataAccessException e) {
			e.printStackTrace();
		}

		request.setAttribute("WIDsubject", payp.getOrderId());
		request.setAttribute("WIDout_trade_no", request.getParameter("orderid"));
		return "pay/bankapi";
	}

	/**
	 * 用户支付完成后从支付宝页面跳转到本页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/return_url", method = { RequestMethod.GET, RequestMethod.POST })
	public String returnUrl(HttpServletRequest request, HttpServletResponse response, Model model) {
		try {

			// 获取支付宝GET过来反馈信息
			Map<String, String> params = new HashMap<String, String>();
			Map<?, ?> requestParams = request.getParameterMap();
			for (Iterator<?> iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");

				params.put(name, valueStr);
			}

			// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			// 商户订单号
			//String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
			String subject = request.getParameter("subject");
			if(subject != null){
				subject = new String(subject.getBytes("ISO-8859-1"), "UTF-8");
			}else{
				return "user/payMessage";
			}
			
			// 支付宝交易号
			String trade_no = request.getParameter("trade_no");
			if(trade_no != null){
				trade_no = new String(trade_no.getBytes("ISO-8859-1"), "UTF-8");
			}else{
				return "user/payMessage";
			}		
			
			// 交易状态
			String trade_status = request.getParameter("trade_status");
			if(trade_status != null){
				trade_status = new String(trade_status.getBytes("ISO-8859-1"), "UTF-8");
			}else{
				return "user/payMessage";
			}
			
			// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

			// 计算得出通知验证结果
			boolean verify_result = AlipayNotify.verify(params);


			if (verify_result) {// 验证成功
				// ////////////////////////////////////////////////////////////////////////////////////////
				// 请在这里加上商户的业务逻辑程序代码

				// ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
				PaymentPersist pp;
				if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
					// 判断该笔订单是否在商户网站中已经做过处理
					// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					// 如果有做过处理，不执行商户的业务程序
					pp = paymentPsService.payForGems(subject,trade_no);
				}else{
					pp = paymentPsService.payForGems(subject,trade_no);
				}
				
				long uid = pp.getUid();
				double payment = pp.getPayment();
				FlyUser flyUser = flyUserPsService.get(uid);
				String username = flyUser.getUsername();
				String nickname = flyUser.getNickname();

				// 该页面可做页面美工编辑
				// out.println("验证成功<br />");
				// ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
				model.addAttribute("state", 0);
				model.addAttribute("username", username);
				model.addAttribute("nickname", nickname);
				model.addAttribute("subject", subject);
				model.addAttribute("payment", payment);
				// ////////////////////////////////////////////////////////////////////////////////////////
			} else {
				// 该页面可做页面美工编辑
				// out.println("验证失败");
				model.addAttribute("state", 1);
				model.addAttribute("subject", subject);
			}
			
			return "user/payMessage";

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "error";
	}

	/**
	 * 支付宝server通知我方server
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/notify_url", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String notifyUrl(HttpServletRequest request, HttpServletResponse response) {
		try {

			//获取支付宝POST过来反馈信息
			Map<String,String> params = new HashMap<String,String>();
			Map<?, ?> requestParams = request.getParameterMap();
			for (Iterator<?> iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
				params.put(name, valueStr);
			}

			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			//商户订单号

			//String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
			String subject = request.getParameter("subject");
			if(subject != null){
				subject = new String(subject.getBytes("ISO-8859-1"), "UTF-8");
			}else{
				return "error";
			}
			//支付宝交易号

			String trade_no = request.getParameter("trade_no");
			if(trade_no != null){
				trade_no = new String(trade_no.getBytes("ISO-8859-1"), "UTF-8");
			}else{
				return "error";
			}

			//交易状态
			String trade_status = request.getParameter("trade_status");
			if(trade_status != null){
				trade_status = new String(trade_status.getBytes("ISO-8859-1"), "UTF-8");
			}else{
				return "error";
			}

			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

			if(AlipayNotify.verify(params)){//验证成功
				//////////////////////////////////////////////////////////////////////////////////////////
				//请在这里加上商户的业务逻辑程序代码

				//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

				if(trade_status.equals("TRADE_FINISHED")){
					//判断该笔订单是否在商户网站中已经做过处理
					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					//如果有做过处理，不执行商户的业务程序
					paymentPsService.payForGems(subject,trade_no);
					//注意：
					//该种交易状态只在两种情况下出现
					//1、开通了普通即时到账，买家付款成功后。
					//2、开通了高级即时到账，从该笔交易成功时间算起，过了签约时的可退款时限（如：三个月以内可退款、一年以内可退款等）后。
				} else if (trade_status.equals("TRADE_SUCCESS")){
					//判断该笔订单是否在商户网站中已经做过处理
					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					//如果有做过处理，不执行商户的业务程序
					paymentPsService.payForGems(subject,trade_no);
					//注意：
					//该种交易状态只在一种情况下出现——开通了高级即时到账，买家付款成功后。
				}

				//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

				return "success";	//请不要修改或删除

				//////////////////////////////////////////////////////////////////////////////////////////
			}else{//验证失败
				return "verify_fail";
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "fail";
		//return "pay/notify_url";
	}
	
	@RequestMapping(value = "/test/{id}", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String test(HttpServletRequest request, HttpServletResponse response,@PathVariable String id) {
		paymentPsService.payForGems(id,"test alipay 123456");
		return "success";
	}
}