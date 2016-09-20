package com.qianxun.web;

import java.util.List;

import org.copycat.framework.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qianxun.model.LoggerPersist;
import com.qianxun.model.PaymentPersist;
import com.qianxun.service.ExchangeToWebService;
import com.qianxun.service.LoggerPersistService;
import com.qianxun.service.PaymentPersistService;
import com.test.qianxun.model.Match;
import com.test.qianxun.service.GiftService;
import com.test.qianxun.service.MatchService;
import com.test.qianxun.service.SessionService;
import com.test.qianxun.service.UserService;

@Controller
@RequestMapping(value = "/logger")
public class LoggerController {
	@Autowired
	private SessionService sessionService;
	@Autowired
	private LoggerPersistService loggerService;
	@Autowired
	private MatchService lmService;
	@Autowired
	private PaymentPersistService payService;
	@Autowired
	private GiftService giftService;
	@Autowired
	private UserService userService;
	@Autowired
	private ExchangeToWebService exchangeToWebService;
	private int size = 20;


	@RequestMapping("/list")
	public String list(Model model) {
		return list(1, model);
	}

	/**
	 * 钻石,玫瑰,充值日志,比赛积分
	 * @param num
	 * @param model
	 * @return
	 */
	@RequestMapping("/list/{num}")
	public String list(@PathVariable int num, Model model) {
		Page page = new Page(num, size);
		List<LoggerPersist> loggerList = loggerService.list(page);
		model.addAttribute("loggerList", loggerList);
		model.addAttribute("page", page);
		model.addAttribute("type", "logger");
		return "logger/list";
	}
	
	@RequestMapping("/mlist")
	public String mlist(Model model) {
		return list(1, model);
	}
	@RequestMapping("/mlist/{num}")
	public String mlist(@PathVariable int num, Model model) {
		Page page = new Page(num, size);
		List<Match> loggerList = lmService.listAll(page);
		model.addAttribute("loggerList", loggerList);
		model.addAttribute("page", page);
		model.addAttribute("type", "logger");
		return "logger/mlist";
	}
	
	@RequestMapping("/paylist")
	public String payList(Model model) {
		return payList(1, model);
	}

	/**
	 * 充值单据日志
	 * @param num
	 * @param model
	 * @return
	 */
	@RequestMapping("/paylist/{num}")
	public String payList(@PathVariable int num, Model model) {
		Page page = new Page(num, size);
		List<PaymentPersist> loggerList = payService.listPay(page);
		model.addAttribute("loggerList", loggerList);
		model.addAttribute("page", page);
		model.addAttribute("type", "logger");
		return "pay/list";
	}
}