package com.qianxun.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qianxun.service.ExchangeToWebService;
import com.qianxun.service.LoggerPersistService;
import com.qianxun.service.PaymentPersistService;
import com.test.qianxun.service.GiftService;
import com.test.qianxun.service.SessionService;
import com.test.qianxun.service.UserService;

@Controller
@RequestMapping(value = "/gm_flying")
public class GmFlyingController {
	@Autowired
	private SessionService sessionService;
	@Autowired
	private LoggerPersistService loggerService;
	@Autowired
	private PaymentPersistService payService;
	@Autowired
	private GiftService giftService;
	@Autowired
	private UserService userService;
	@Autowired
	private ExchangeToWebService exchangeToWebService;
	//private int size = 20;

	/**
	 * 增加
	 * @param num
	 * @param users[abc_def_ghi]
	 * @return
	 */
	@RequestMapping("/charm/{users}/{num}")
	public String payList(@PathVariable String users,@PathVariable int num) {

		return "ok";
	}
}