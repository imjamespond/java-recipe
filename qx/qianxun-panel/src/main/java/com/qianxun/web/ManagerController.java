package com.qianxun.web;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.copycat.framework.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.qianxun.model.Manager;
import com.qianxun.service.ManagerService;
import com.qianxun.service.RoleService;


@Controller
@RequestMapping("/manager")
public class ManagerController{
	@Autowired
	ManagerService managerService;
	@Autowired
	RoleService roleService;
	@Autowired
	private UserValidator userValidator;
	private int limit = 20;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) {
		model.addAttribute("manager", new Manager());
		return "manager/login";
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model) {
		return list(1, model);
	}
	
	@RequestMapping(value = "/list/{number}", method = RequestMethod.GET)
	public String list(@PathVariable int number, Model model) {
		Page page = new Page(number, limit);
		List<Manager> managerList = managerService.list(page);
		model.addAttribute("managerList", managerList);
		model.addAttribute("page", page);
		return "manager/list";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@ModelAttribute("manager") Manager manager, HttpServletRequest request, BindingResult result,
			Model model) {
		userValidator.validateManagerLogin(manager, result);
		if (result.hasErrors()) {
			return "manager/login";
		}
		String email = manager.getEmail();
		String password = manager.getPassword();
		manager = managerService.findByEmail(email);
		if(manager == null){
			result.rejectValue("email", "manager.not.exist");
			return "manager/login";
		}
		if(managerService.authenticate(email, password)==0){
			result.rejectValue("password", "manager.password.match.invalid");
			return "manager/login";
		}
		HttpSession session = request.getSession();
		session.setAttribute("id", Long.toString(manager.getId()));
		this.authorize(request, manager.getUsername(), manager.getRoles());
		return "redirect:/";
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request){
		HttpSession session = request.getSession();
		session.invalidate();
		return "redirect:/";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(Model model){
		Manager manager = new Manager();
		model.addAttribute("manager", manager);
		return "manager/create";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Manager manager){
		managerService.save(manager);
		return "redirect:/manager/list";
	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable long id,Model model){
		Manager manager = managerService.get(id);
		model.addAttribute("manager", manager);
		return "manager/edit";
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Manager manager,HttpServletRequest request){
		managerService.update(manager);
		HttpSession session = request.getSession();
		int mana = (Integer)session.getAttribute("manager");
		if(mana == 1){
			return "redirect:/manager/list";
		}else{
			return "index";
		}
	}
	
	@RequestMapping(value = "/change/{id}", method = RequestMethod.GET)
	public String change(@PathVariable String id){
		return "manager/change";
	}
	
	@RequestMapping(value = "/password", method = RequestMethod.POST)
	public String password(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPasswordf") String newPasswordf, @RequestParam("newPasswords") String newPasswords,
			HttpServletRequest request,Model model) {
		HttpSession session = request.getSession();
		long id = (Long)session.getAttribute("id");
		Manager manager = managerService.get(id);
		if(newPasswordf.equals("") || oldPassword.equals("") || newPasswords.equals("")){
			request.setAttribute("error", "密码不能为空");
			return "manager/change";
		}else if(!newPasswordf.equals(newPasswords)){
			request.setAttribute("error", "两次输入的新密码不同");
			return "manager/change";
		}
		if (DigestUtils.md5Hex(DigestUtils.md5Hex(oldPassword) + manager.getSalt()).equals(manager.getPassword())) {
			managerService.updatePasswored(manager.getId(), newPasswordf);
			model.addAttribute("message", "密码修改成功");
			return "redirect:/manager/success";
		} else {
			request.setAttribute("error", "旧密码输入错误");
			return "manager/change";
		}
	}
	
	@RequestMapping(value = "/success", method = RequestMethod.GET)
	public String success(Model model){
		model.addAttribute("message", "密码修改成功");
		return "message";
	}
	
	@RequestMapping(value = "/abandon/{id}", method = RequestMethod.GET)
	public String abandon(@PathVariable long id, Model model){
		Manager manager = managerService.get(id);
		manager.setState(0);
		managerService.update(manager);
		return "redirect:/manager/list";
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable long id, Model model){
		Manager manager = managerService.get(id);
		managerService.delete(manager.getId());
		return "redirect:/manager/list";
	}
	
	public void authorize(HttpServletRequest request,String username,String roles){
		HttpSession session = request.getSession();
		String[] keys = roles.split(",");
		if(Arrays.<String> asList(keys).contains("user")){
			session.setAttribute("user", 1);
		}
		if(Arrays.<String> asList(keys).contains("manager")){
			session.setAttribute("manager", 1);
		}
		if(Arrays.<String> asList(keys).contains("stage")){
			session.setAttribute("stage", 1);
		}
		if(Arrays.<String> asList(keys).contains("game")){
			session.setAttribute("game", 1);
		}
		session.setAttribute("username", username);
	}

}