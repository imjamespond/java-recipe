package com.test.qianxun.web;

import java.util.List;

import org.copycat.framework.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianxun.model.Constant;
import com.qianxun.model.FlyUser;
import com.qianxun.model.UserWealthPersist;
import com.qianxun.service.FlyUserService;
import com.qianxun.service.UserWealthService;
import com.test.qianxun.model.Contact;
import com.test.qianxun.model.Exchange;
import com.test.qianxun.service.ContactService;
import com.test.qianxun.service.ExchangeService;
import com.test.qianxun.service.SessionService;
import com.test.qianxun.service.UserService;

@Controller
@RequestMapping(value = "/exchange")
public class ExchangeController {
	@Autowired
	private SessionService sessionService;
	@Autowired
	private ExchangeService exchangeService;
	@Autowired
	private ContactService contactService;
	@Autowired
	private UserService userService;
	@Autowired
	private FlyUserService flyService;
	@Autowired
	private UserWealthService wealthService;
	private int limit = 20;

	@RequestMapping("/list")
	public String list(Model model) {
		return list(1, model);
	}

	@RequestMapping("/list/{number}")
	public String list(@PathVariable int number, Model model) {
		long uid = Long.parseLong(sessionService.getUid());
		Page page = new Page(number, limit);
		List<Exchange> list = exchangeService.listByUid(uid, page);
		model.addAttribute("page", page);
		model.addAttribute("list", list);
		model.addAttribute("type", "exchange");
		return "user/exchange";
	}
	
	@RequestMapping("/index")
	public String index( Model model) {
		String username = sessionService.getUsername();
		String strUid = sessionService.getUid();
		if(null == strUid){
			return "error";
		}
		long uid = Long.parseLong(strUid);
		UserWealthPersist uw = wealthService.get(uid);
		FlyUser fu = flyService.get(uid);
		if(fu!=null&&uw!=null){
			model.addAttribute("username", username);
			model.addAttribute("nickname", fu.getNickname());
			model.addAttribute("credit", uw.getCredit());
			model.addAttribute("type", "exchange");
			return "user/exchange_index";
		}
		
		return "error";
	}
	
	@RequestMapping("/form")
	public String form( Model model) {
		String strUid = sessionService.getUid();
		if(null == strUid){
			return "error";
		}
		long uid = Long.parseLong(strUid);
		Contact contact = contactService.get(uid);
		String name = "", mobile="", postcode="", address="",hasContact="false";
		if(contact!=null){
			name=contact.getName();
			mobile=contact.getMobile();
			postcode=contact.getPostcode();
			address=contact.getAddress();
			hasContact="true";
		}
		
		model.addAttribute("name",name);
		model.addAttribute("mobile", mobile);
		model.addAttribute("postcode", postcode);
		model.addAttribute("address", address);
		model.addAttribute("hasContact", hasContact);
		return "user/exchange_form";
	}

	/**
	 * 兑换
	 * @param num数量
	 * @param type类型 参照Constant
	 * @param item 1为京东购物卡, 
	 * @param invoice京东单号
	 * @param remark备注
	 * @return
	 */
	@RequestMapping("/exchange")
	@ResponseBody
	public String exchange( 
			@RequestParam("num") Integer num,
			@RequestParam("type") Integer type,
			@RequestParam("item") Integer item,
			@RequestParam("invoice") String invoice,
			@RequestParam("remark") String remark){
		
		if(num==null||type==null||item==null){
			return "parameter-invalid";
		}
		
		String strUid = sessionService.getUid();
		if(null == strUid){
			return "need-login";
		}
		long uid = Long.parseLong(strUid);
		
		//check user's credts
		FlyUser fu = flyService.get(uid);
		if(null == fu){
			return "fly-user-invalid";
		}
		
		int affect = 0;
		int cost = 0;
		String cause = "";
		if(item==1){
			cost = 100;
			cause = "兑换100元京东卡";
		}
		//check cost
		if(cost==0){
			return "cost-nothing";
		}
		//check type
		if(type==0){
			return "exchange-nothing";
		}
		if(type==Constant.TYPE_CREDIT){
			UserWealthPersist wealth = wealthService.get(fu.getId());
			affect = wealthService.reduceCredit(wealth, cost, cause);
			if(affect==0){
				return "insurfficient-credit";
			}
		}

		
		Exchange ex = new Exchange();
		ex.setNum(num);
		ex.setUid(uid);
		ex.setType(type);
		ex.setItem(item);
		ex.setInvoice(invoice);
		ex.setRemark(remark);
		ex.setTime(System.currentTimeMillis());
		exchangeService.save(ex);
		return "ok";
	}
	
	
	/**
	 * 联系方式
	 * @param address
	 * @param email
	 * @param name
	 * @param mobile
	 * @return
	 */
	@RequestMapping("/contact/create")
	@ResponseBody
	public String createContact(
			@RequestParam("address") String address,
			@RequestParam("postcode") String postcode,
			@RequestParam("email") String email,
			@RequestParam("name") String name,
			@RequestParam("mobile") String mobile){
		String strUid = sessionService.getUid();
		if(null == strUid){
			return "failed";
		}
		long uid = Long.parseLong(strUid);
		Contact c = new Contact();
		c.setUid(uid);
		c.setAddress(address);
		c.setPostcode(postcode);
		c.setEmail(email);
		c.setName(name);
		c.setMobile(mobile);
		contactService.save(uid, c);
		return "ok";
	}
	/**
	 * 修改联系方式
	 * @param address
	 * @param email
	 * @param name
	 * @param mobile
	 * @return
	 */
	@RequestMapping("/contact/update")
	@ResponseBody
	public String updateContact(
			@RequestParam("address") String address,
			@RequestParam("postcode") String postcode,
			@RequestParam("email") String email,
			@RequestParam("name") String name,
			@RequestParam("mobile") String mobile){
		String strUid = sessionService.getUid();
		if(null == strUid){
			return "failed";
		}
		long uid = Long.parseLong(strUid);
		Contact c = new Contact();
		c.setUid(uid);
		c.setAddress(address);
		c.setPostcode(postcode);
		c.setEmail(email);
		c.setName(name);
		c.setMobile(mobile);
		contactService.update(c);
		return "ok";
	}
}