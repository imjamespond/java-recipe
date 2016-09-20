package com.qianxun.web;

import java.util.List;

import org.copycat.framework.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianxun.model.ExchangeJob;
import com.qianxun.model.FlyUser;
import com.qianxun.service.FlyUserService;
import com.qianxun.service.JobService;
import com.qianxun.util.RandomUtils;
import com.test.qianxun.model.Contact;
import com.test.qianxun.model.Exchange;
import com.test.qianxun.service.ContactService;
import com.test.qianxun.service.ExchangeService;
import com.test.qianxun.util.JsonUtils;

@Controller
@RequestMapping(value = "/exchange")
public class ExchangeController {
	@Autowired
	private FlyUserService flyUserService;
	@Autowired
	private ExchangeService exchangeService;
	@Autowired
	private ContactService contactService;
	@Autowired
	private JobService jobService;
	private int limit = 50;


	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public String update(@RequestParam("id") Long id, 
			@RequestParam("uid") Long uid, 
			@RequestParam("invoice") String invoice,
			@RequestParam("remark") String remark){
		if(exchangeService.updateInvoice(id,uid,invoice,remark)>0){
			return "ok";
		}
		return "failed";
	}
	
	@RequestMapping("/remove/{id}")
	public String remove(@PathVariable long id) {
		exchangeService.delete(id);
		return "redirect:/game/list";
	}
	
	@RequestMapping("/list")
	public String list(Model model) {
		return list(1, model);
	}

	@RequestMapping("/list/{number}")
	public String list(@PathVariable int number, Model model) {
		Page page = new Page(number, limit);
		List<Exchange> list = exchangeService.listAll(page);
		model.addAttribute("list", list);
		model.addAttribute("page", page);
		return "exchange/list";
	}

	/**
	 * ?type=1&invoice=123456&remark=foo bar&uid=123
	 */
	@RequestMapping("/create")
	@ResponseBody
	public String create(@RequestParam("uid") Long uid, 
			@RequestParam("type") Integer type,
			@RequestParam("invoice") String invoice,
			@RequestParam("remark") String remark){
		long begin = System.currentTimeMillis();
		
		Exchange ex = new Exchange();
		ex.setNum(1);
		ex.setUid(uid);
		ex.setType(type);
		ex.setInvoice(invoice);
		ex.setRemark(remark);
		ex.setTime(begin);
		
		//exchangeService.save(ex);
		jobService.produce(new ExchangeJob(ex));
		
		return "ok";
	}

	@RequestMapping("/contact/{uid}")
	@ResponseBody
	public String contact(@PathVariable long uid) {
		Contact c = contactService.get(uid);
		if(null!=c){
			return JsonUtils.toJson(c);
		}
		return "data unavailable";
	}

	/**
	 *?uid=123&address=5th avenue&email=geek_man@gmail.com&mobile=1234567890&name=geek_man
	 */
	@RequestMapping("/contact/create")
	@ResponseBody
	public String createContact(@RequestParam("uid") Long uid, 
			@RequestParam("address") String address,
			@RequestParam("email") String email,
			@RequestParam("name") String name,
			@RequestParam("mobile") String mobile){
		Contact c = new Contact();
		c.setUid(uid);
		c.setAddress(address);
		c.setEmail(email);
		c.setName(name);
		c.setMobile(mobile);
		contactService.save(uid, c);
		return "ok";
	}
	
	@RequestMapping("/debug/exchange/add/{num}")
	@ResponseBody
	public String debugExchange(@PathVariable int num,
			@RequestParam("uid") Long uid, 
			@RequestParam("type") Integer type,
			@RequestParam("invoice") String invoice,
			@RequestParam("remark") String remark){
		List<FlyUser> list = flyUserService.list();		
		
		long begin = System.currentTimeMillis();
		//FIXME debug
		for(int i=0;i<num;i++){
			int index = RandomUtils.nextInt(list.size());
			create(list.get(index).getId(),type,invoice,remark);
		}
		long duration = System.currentTimeMillis()-begin;
		return "cost:"+duration;
	}
	@RequestMapping("/debug/exchange/num")
	@ResponseBody
	public String debugExchangeNum(){
		return String.valueOf(jobService.getJobNum());
	}
}