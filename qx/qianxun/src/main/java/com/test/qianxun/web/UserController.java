package com.test.qianxun.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.codec.digest.DigestUtils;
import org.copycat.framework.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.qianxun.model.FlyUser;
import com.qianxun.model.LoggerPersist;
import com.qianxun.model.PaymentPersist;
import com.qianxun.service.FlyUserService;
import com.qianxun.service.LoggerPersistService;
import com.qianxun.service.PaymentPersistService;
import com.qianxun.util.PaymentUtil;
import com.test.qianxun.model.Gift;
import com.test.qianxun.model.Match;
import com.test.qianxun.model.Message;
import com.test.qianxun.model.User;
import com.test.qianxun.service.GiftService;
import com.test.qianxun.service.MatchService;
import com.test.qianxun.service.MessageService;
import com.test.qianxun.service.SessionService;
import com.test.qianxun.service.UserService;
import com.test.qianxun.util.ImageUtils;

@Controller
@RequestMapping(value = "/user")
public class UserController {
	@Value("#{properties['cookie.domain']}")
	private String cookieDomain;
	@Value("#{properties['temp.folder']}")
	private String tempFolder;
	@Value("#{properties['upload.folder']}")
	private String uploadFolder;
	@Value("#{properties['main.http']}")
	private String mainUrl;
	@Autowired
	private UserService userService;
	@Autowired
	private SessionService sessionService;

	@Autowired
	private PaymentPersistService paymentService;
	@Autowired
	private GiftService giftService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private FlyUserService flyService;

	@Autowired
	private LoggerPersistService loggerService;
	@Autowired
	private MatchService lmService;
	
	private String passwordRegex = "^[A-Za-z0-9]{6,16}$";
	private String identityRegex = "(^\\d{18}$)|(^\\d{17}(X|x)$)";
	private String nameRegex = "^[\u4e00-\u9fa5]{2,}$";
	private String blankRegex = "^\\s*$";

	private int limit = 20;

	public void loadUser(Model model) {
		long uid = Long.parseLong(sessionService.getUid());
		User user = userService.get(uid);
		//user.setRose(flyService.getRoseInGame(uid));
		//user.setApple(flyService.getAppleInGame(uid));
		user.setNickname(flyService.getNicknameInGame(uid));
		model.addAttribute("user", user);
	}

	@RequestMapping("/index")
	public String index(Model model) {
		loadUser(model);
		model.addAttribute("type", "index");
		model.addAttribute("mainUrl", mainUrl);
		return "user/index";
	}
	
	@RequestMapping("/index/{update}")
	public String index1(Model model,@PathVariable int update) {
		loadUser(model);
		model.addAttribute("type", "index");
		model.addAttribute("update", true);
		model.addAttribute("mainUrl", mainUrl);
		return "user/index";
	}

	@RequestMapping("/success")
	public String success(Model model) {
		String username = sessionService.getUsername();
		model.addAttribute("username", username);
		return "user/success";
	}

	@RequestMapping("/bind")
	public String bind(Model model) {
		loadUser(model);
		model.addAttribute("type", "bind");
		return "user/bind";
	}

	@RequestMapping("/fcm")
	public String fcm(Model model) {
		loadUser(model);
		model.addAttribute("type", "fcm");
		return "user/fcm";
	}

	@RequestMapping(value = "/identity", method = RequestMethod.POST)
	@ResponseBody
	public String identity(@RequestParam("name") String name,
			@RequestParam("identity") String identity) {
		if (Pattern.matches(blankRegex, name) || name == null || name.isEmpty()) {
			return "1";
		} else if (!Pattern.matches(nameRegex, name)) {
			return "2";
		} else if (Pattern.matches(blankRegex, identity) || identity == null
				|| identity.isEmpty()) {
			return "3";
		} else if (!Pattern.matches(identityRegex, identity)) {
			return "4";
		} else {
			long uid = Long.parseLong(sessionService.getUid());
			User user = userService.get(uid);
			user.setName(name);
			user.setIdentity(identity);
			user.setState(1);
			userService.update(user);
			return "5";
		}
	}

	@RequestMapping("/password")
	public String password(Model model) {
		loadUser(model);
		model.addAttribute("type", "password");
		return "user/password";
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	@ResponseBody
	public String changePassword(@RequestParam("newp") String newp,
			@RequestParam("oldp") String oldp,
			@RequestParam("repeatp") String repeatp) {
		long uid = Long.parseLong(sessionService.getUid());
		User user = userService.get(uid);
		if (oldp == null || oldp.isEmpty() || Pattern.matches(blankRegex, oldp)) {
			return "1";
		} else if (newp == null || newp.isEmpty()
				|| Pattern.matches(blankRegex, newp)) {
			return "2";
		} else if (repeatp == null || repeatp.isEmpty()
				|| Pattern.matches(blankRegex, repeatp)) {
			return "3";
		} else if (!Pattern.matches(passwordRegex, oldp)) {
			return "4";
		} else if (!Pattern.matches(passwordRegex, newp)) {
			return "5";
		} else if (!Pattern.matches(passwordRegex, repeatp)) {
			return "6";
		} else if (!newp.equals(repeatp)) {
			return "7";
		} else if (!DigestUtils.sha512Hex(oldp + user.getSalt()).equals(
				user.getPassword())) {
			return "8";
		} else {
			userService.changePassword(uid, newp);
			return "9";
		}
	}

	@RequestMapping("/payDetail")
	public String payDetail(Model model) {
		return payDetail(model, 1);
	}

	@RequestMapping("/payDetail/{number}")
	public String payDetail(Model model, @PathVariable int number) {
		loadUser(model);
		long uid = Long.parseLong(sessionService.getUid());
		Page page = new Page(number, limit);
		List<PaymentPersist> chargeList = paymentService.listByUid(uid, page);
		model.addAttribute("type", "payDetail");
		model.addAttribute("page", page);
		model.addAttribute("chargeList", chargeList);
		return "user/payDetail";
	}

	@RequestMapping("/rose")
	public String rose(Model model) {
		loadUser(model);
		List<Gift> giftList = giftService.listAll();
		model.addAttribute("giftList", giftList);
		model.addAttribute("type", "rose");
		return "user/rose";
	}

	@RequestMapping("/avatar/{state}")
	public String avatar(Model model, @PathVariable String state) {
		loadUser(model);
		model.addAttribute("name", "default.jpg");
		model.addAttribute("width", 400);
		model.addAttribute("height", 400);
		model.addAttribute("type", "avatar");
		if (state.equals("1")) {
			model.addAttribute("success", false);
		} else {
			model.addAttribute("success", true);
		}
		return "user/avatar";
	}

	@RequestMapping(value = "/avatar", method = RequestMethod.POST)
	public String upload(@RequestParam("avatar") MultipartFile multipartFile,
			Model model) {
		loadUser(model);
		model.addAttribute("name", "default.jpg");
		model.addAttribute("width", 400);
		model.addAttribute("height", 400);
		if (multipartFile != null) {
			if (multipartFile.getSize() > 2097152) {
				model.addAttribute("message", "请上传小于2M的JPG格式图片");
				model.addAttribute("type", "avatar");
				model.addAttribute("success", false);
				return "user/avatar";
			}
			String type = multipartFile.getContentType();
			if (type.equals("image/jpeg")) {
				String name = System.currentTimeMillis() + ".jpg";
				File path = new File(tempFolder + "/avatar");
				if (!path.exists()) {
					path.mkdirs();
				}
				try {
					File file = new File(path, name);
					multipartFile.transferTo(file);
					BufferedImage bis = ImageIO.read(file);
					int width = bis.getWidth();
					int height = bis.getHeight();
					if (width < 300 || height < 300) {
						model.addAttribute("message",
								"请上传尺寸大于 300 x 300 的JPG图片");
						model.addAttribute("type", "avatar");
						model.addAttribute("success", false);
						return "user/avatar";
					} else {
						model.addAttribute("width", 400);
						model.addAttribute("height", 400 * height / width);
						model.addAttribute("name", name);
					}
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				model.addAttribute("success", false);
				model.addAttribute("message", "请上传JPG格式图片");
				model.addAttribute("type", "avatar");
				return "user/avatar";
			}
		}
		model.addAttribute("success", false);
		model.addAttribute("type", "avatar");
		return "user/avatar";
	}

	@RequestMapping(value = "/reduce", method = RequestMethod.POST)
	public String reduce(@RequestParam("name") String name,
			@RequestParam("x") int x, @RequestParam("y") int y,
			@RequestParam("length") int length, Model model) {
		File file = new File(tempFolder + "/avatar", name);
		String id = sessionService.get().getUid();
		try {
			BufferedImage bis = ImageIO.read(file);
			bis.getWidth();
			float scala = bis.getWidth() / 400f;
			x = (int) (scala * x);
			y = (int) (scala * y);
			length = (int) (scala * length);
			File path = new File(uploadFolder + "/avatar");
			if (!path.exists()) {
				path.mkdirs();
			}
			ImageUtils.resize(bis, x, y, length, uploadFolder + "/avatar", id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/user/avatar/2";
	}

	@RequestMapping("/message")
	public String message(Model model) {
		return message(model, 1);
	}

	@RequestMapping("/message/{number}")
	public String message(Model model, @PathVariable int number) {
		loadUser(model);
		long uid = Long.parseLong(sessionService.getUid());
		Page page = new Page(number, limit);
		List<Message> messageList = messageService.listByUid(uid, page);
		model.addAttribute("messageList", messageList);
		for (Message message : messageList) {
			message.setState(1);
			messageService.update(message);
		}
		model.addAttribute("type", "message");
		return "user/message";
	}

	@RequestMapping("/agree")
	public String vip(Model model) {
		return "user/agree";
	}

	/*@RequestMapping("/payinfo")
	public String payinfo(Model model) {
		loadUser(model);
		model.addAttribute("pay", false);
		model.addAttribute("type", "pay");
		return "pay/payinfo";
	}

	@RequestMapping(value = "/payindex", method = { RequestMethod.GET, RequestMethod.POST })
	public String pay(Model model, @RequestParam String account, @RequestParam int fee, @RequestParam int paytype) {
		loadUser(model);
		String orderid = java.util.UUID.randomUUID().toString();
		model.addAttribute("account", account);
		model.addAttribute("fee", fee);
		model.addAttribute("paytype", paytype);
		model.addAttribute("orderid", orderid);
		model.addAttribute("pay", false);
		model.addAttribute("type", "pay");
		return "pay/payindex";
	}*/
	
	@RequestMapping("/pay/index")
	public String pay_index() {
		return "user/payIndex";
	}

	@RequestMapping(value = "/pay/info", method = { RequestMethod.GET, RequestMethod.POST })
	public String pay_info(Model model, @RequestParam String account,
			@RequestParam int fee, @RequestParam int paytype,
			@RequestParam String nickname) {
		String orderid = PaymentUtil.generateOrderIdStr("1");
		model.addAttribute("account", account);
		model.addAttribute("fee", fee);
		model.addAttribute("paytype", paytype);
		model.addAttribute("orderid", orderid);
		model.addAttribute("nickname", nickname);
		return "user/payInfo";
	}

	@RequestMapping(value = "/pay/message", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String pay_message(Model model, @RequestParam String username,
			@RequestParam String nickname, @RequestParam String subject,
			@RequestParam double payment) {
		model.addAttribute("username", username);
		model.addAttribute("nickname", nickname);
		model.addAttribute("subject", subject);
		model.addAttribute("payment", payment);
		return "user/payMessage";
	}

	@RequestMapping("/payname")
	@ResponseBody
	public String payname(@RequestParam String account) {
		String nickname;
		FlyUser p = flyService.getByName(account);
		if(p == null){
			nickname = "null_nickname";
		}else{
			nickname = p.getNickname();
		}
		return nickname;
	}



	
	/**
	 * 比赛积分
	 * @param num
	 * @param model
	 * @return
	 */
	@RequestMapping("/record")
	public String record(Model model) {
		return record(1, model);
	}
	@RequestMapping("/record/{num}")
	public String record(@PathVariable int num, Model model) {
		Page page = new Page(num, 20);
		long uid = Long.parseLong(sessionService.getUid());
		List<Match> recList = lmService.listByUid(uid, page);		
		model.addAttribute("recList", recList);
		model.addAttribute("page", page);
		model.addAttribute("type", "record");
		return "user/record";
	}
	/**
	 * 比赛日志
	 * @param num
	 * @param model
	 * @return
	 */
	@RequestMapping("/integral")
	public String integral(Model model) {
		return integral(1, model);
	}
	@RequestMapping("/integral/{num}")
	public String integral(@PathVariable int num, Model model) {
		Page page = new Page(num, 20);
		long uid = Long.parseLong(sessionService.getUid());
		List<LoggerPersist> integralList = loggerService.listExchangByUid(uid, page);
		model.addAttribute("integralList", integralList);
		model.addAttribute("page", page);
		model.addAttribute("type", "integral");
		return "user/integral";
	}
	

}