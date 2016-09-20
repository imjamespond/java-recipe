package com.qianxun.web;

import java.util.List;

import org.copycat.framework.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qianxun.model.FlyUser;
import com.qianxun.service.FlyUserService;
import com.qianxun.service.JobService;
import com.test.qianxun.service.ContactService;

@Controller
@RequestMapping(value = "/flyuser")
public class FlyUserController {
	@Autowired
	private FlyUserService flyUserService;
	@Autowired
	private ContactService contactService;
	@Autowired
	private JobService jobService;
	private int limit = 50;

	
	@RequestMapping("/list")
	public String list(Model model) {
		return list(1, model);
	}

	@RequestMapping("/list/{number}")
	public String list(@PathVariable int number, Model model) {
		Page page = new Page(number, limit);
		List<FlyUser> list = flyUserService.list(page);
		model.addAttribute("list", list);
		model.addAttribute("page", page);
		model.addAttribute("pageUrl", "/flyuser/list");
		return "flyuser/list";
	}
	
	@RequestMapping("/listByLogindate")
	public String listByLogindate(Model model) {
		return listByLogindate(1, model);
	}
	
	@RequestMapping("/listByLogindate/{number}")
	public String listByLogindate(@PathVariable int number, Model model) {
		Page page = new Page(number, limit);
		List<FlyUser> list = flyUserService.listByLogindate(page);
		model.addAttribute("list", list);
		model.addAttribute("page", page);
		model.addAttribute("pageUrl", "/flyuser/listByLogindate");
		return "flyuser/list";
	}
	
	@RequestMapping("/listByTotaldays")
	public String listByTotaldays(Model model) {
		return listByTotaldays(1, model);
	}
	
	@RequestMapping("/listByTotaldays/{number}")
	public String listByTotaldays(@PathVariable int number, Model model) {
		Page page = new Page(number, limit);
		List<FlyUser> list = flyUserService.listByTotaldays(page);
		model.addAttribute("list", list);
		model.addAttribute("page", page);
		model.addAttribute("pageUrl", "/flyuser/listByTotaldays");
		return "flyuser/list";
	}
	
	@RequestMapping("/listByTotaltime")
	public String listByTotaltime(Model model) {
		return listByTotaltime(1, model);
	}
	
	@RequestMapping("/listByTotaltime/{number}")
	public String listByTotaltime(@PathVariable int number, Model model) {
		Page page = new Page(number, limit);
		List<FlyUser> list = flyUserService.listByTotaltime(page);
		model.addAttribute("list", list);
		model.addAttribute("page", page);
		model.addAttribute("pageUrl", "/flyuser/listByTotaltime");
		return "flyuser/list";
	}

	@RequestMapping("/listByConsecutive")
	public String listByConsecutive(Model model) {
		return listByConsecutive(1, model);
	}
	
	@RequestMapping("/listByConsecutive/{number}")
	public String listByConsecutive(@PathVariable int number, Model model) {
		Page page = new Page(number, limit);
		List<FlyUser> list = flyUserService.listByConsecutive(page);
		model.addAttribute("list", list);
		model.addAttribute("page", page);
		model.addAttribute("pageUrl", "flyuser/listByConsecutive");
		return "flyuser/list";
	}
}