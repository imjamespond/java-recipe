package com.qianxun.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qianxun.model.Manager;
import com.qianxun.service.ManagerService;

@Controller
@RequestMapping(value = "/")
public class IndexController {
	@Autowired
	private ManagerService managerService;

	@RequestMapping("/")
	public String get(Model model) {
		Manager manager = managerService.findByEmail("admin@qianxunyouxi.com");
		if (manager == null) {
			manager = new Manager();
			manager.setEmail("admin@qianxunyouxi.com");
			manager.setPassword("123789abcd");
			manager.setRoles("user,manager");
			manager.setUsername("admin");
			managerService.save(manager);
		}
		return "index";
	}

}
