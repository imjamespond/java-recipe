package com.qianxun.web;

import java.io.File;
import java.util.List;

import org.copycat.framework.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianxun.model.LoggerPersist;
import com.qianxun.service.FlyUserService;
import com.qianxun.service.LoggerPersistService;
import com.qianxun.util.UrlConnectionUtils;
import com.test.qianxun.model.SigninRecord;
import com.test.qianxun.model.User;
import com.test.qianxun.service.SigninRecordService;
import com.test.qianxun.service.UserService;
import com.test.qianxun.util.JsonUtils;

@Controller
@RequestMapping(value = "/user")
class UserController {
	@Autowired
	private UserService userService;
	//@Autowired
	//private UserIpRecordService userIpRecordService;
	@Autowired
	private FlyUserService flyService;
	@Value("#{properties['upload.folder']}")
	private String uploadFolder;
	private int limit = 20;
	
	@Autowired
	private LoggerPersistService loggerService;
	@Autowired
	private SigninRecordService signinRecordService;
	
	@RequestMapping("/list")
	public String list(Model model) {
		return list(1, model);
	}

	@RequestMapping("/list/{number}")
	public String list(@PathVariable int number, Model model) {
		Page page = new Page(number, limit);
		//List<User> userList = userService.listUserByAccountState(1, page);
		List<User> userList = userService.list(page);
		model.addAttribute("userList", userList);
		model.addAttribute("page", page);
		return "user/list";
	}

	@RequestMapping("/deleteAvatar/{id}")
	public String deleteAvatar(@PathVariable long id) {
		File file = new File(uploadFolder + "/avatar/" + id + ".jpg");
		if (file.exists() && file.isFile()) {
			file.delete();
		}
		return "redirect:/user/list";
	}
	
	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable long id) {
		userService.delete(id);
		return "redirect:/user/list";
	}
	
	@RequestMapping("/reset/{id}")
	public String reset(@PathVariable long id) {
		userService.changePassword(id, "123123");
		return "redirect:/user/list";
	}

	@RequestMapping("/loggerlist")
	public String loggerlist(Model model) {
		return loggerlist(1, model);
	}
	@RequestMapping("/loggerlist/{number}")
	public String loggerlist(@PathVariable int number, Model model) {
		Page page = new Page(number, limit);
		List<LoggerPersist> list = loggerService.list(page);
		model.addAttribute("list", list);
		model.addAttribute("page", page);
		return "logger/list";
	}
	@RequestMapping("/signinlist")
	public String signinlist(Model model) {
		return signinlist(1, model);
	}
	@RequestMapping("/signinlist/{number}")
	public String signinlist(@PathVariable int number, Model model) {
		Page page = new Page(number, limit);
		List<SigninRecord> list = signinRecordService.list(page);
		model.addAttribute("list", list);
		model.addAttribute("page", page);
		return "logger/signinlist";
	}
//	@RequestMapping(value = "/listUsersByIp/{number}")
//	public String ListUsersByIp(String ip, @PathVariable int number, Model model) {
//		Page page = new Page(number, limit);
//		List<UserIpRecord> records = this.userIpRecordService
//				.listByIp(ip, page);
//		List<User> userList = new ArrayList<User>();
//		for (UserIpRecord record : records) {
//			long uid = record.getUid();
//			User user = userService.get(uid);
//			String nickname = flyService.getNicknameInGame(uid);
//			user.setNickname(nickname);
//			userList.add(user);
//		}
//		model.addAttribute("userList", userList);
//		return "";
//	}
	@RequestMapping("/get/{uid}")
	@ResponseBody
	public String getuid(@PathVariable long uid) {
		User c = userService.get(uid);
		if(null!=c){
			return JsonUtils.toJson(c);
		}
		return "data unavailable";
	}
	@RequestMapping("/getip")
	@ResponseBody
	public String getip(@RequestParam("ip") String ip) {
		if(ip.indexOf(' ')==0){
			ip = ip.substring(1, ip.length());
		}
		return UrlConnectionUtils.getUrlContents("http://ip.taobao.com/service/getIpInfo.php?ip="+ip);
	}
}