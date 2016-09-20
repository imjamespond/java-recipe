package com.test.qianxun.web;

import java.security.SecureRandom;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianxun.util.JsonLite;
import com.qianxun.util.RandomUtils;
import com.test.qianxun.model.Session;
import com.test.qianxun.model.User;
import com.test.qianxun.service.MailService;
import com.test.qianxun.service.SessionService;
import com.test.qianxun.service.UserService;
import com.test.qianxun.util.JsonUtils;
import com.test.qianxun.util.ValidateUtils;

/**
 * @author james
 *
 */
@Controller
@RequestMapping(value = "/utillity")
public class UtillityController {
	private String emailRegex = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
	private String blankRegex = "^\\s*$";
	private SecureRandom random = new SecureRandom();
	@Autowired
	private UserService userService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private MailService mailSerivce;
	
	
	/**
	 * 获得图片验证码
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getRegValidate")
	public void getRegValidate(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("image/jpeg");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expire", 0);
		ValidateUtils randomValidateCode = new ValidateUtils(userService);
		randomValidateCode.getRandcode(request, response);
	}
	@RequestMapping("/getTouchCaptcha")
	public void getTouchValidate(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("image/jpeg");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expire", 0);
		
		String sid = request.getSession().getId();
		String json = userService.getTouchCaptcha(sid);
		Validator[] jlist;
		try {
			jlist = JsonUtils.toObject(Validator[].class, json);
			for(Validator v:jlist){
				int vx = Integer.valueOf(v.x);
				int vy = Integer.valueOf(v.y);
				ValidateUtils.getTouchImage(request, response, v.getCaptcha(), vx, vy);
				break;//only one needed
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	@RequestMapping("/touchCaptcha")
	public void touchCaptcha(HttpServletRequest request,Model model) {
		int rand = RandomUtils.nextInt(ValidateUtils.gRandString.length()-1);
		int x=0,y=0;
		y = RandomUtils.nextInt(ValidateUtils.gFontSize*1, ValidateUtils.gHeight);
		x = RandomUtils.nextInt(0, ValidateUtils.gWidth-ValidateUtils.gFontSize);
		String captcha = ValidateUtils.gRandString.substring(rand, rand+1);
		JsonLite jBraket = new JsonLite(JsonLite.Type.Bracket);
        //选中的字
        JsonLite jBrace = new JsonLite();
		jBrace.appendKeyValue("y", String.valueOf(y));
		jBrace.appendKeyValue("x", String.valueOf(x));
		jBrace.appendKeyValue("captcha", captcha);
        jBraket.appendNodeString(jBrace.convert2String());
        //System.out.println(jBraket.convert2String());
        String sid = request.getSession().getId();
        userService.putTouchCaptcha(jBraket.convert2String(), sid);
        
        model.addAttribute("captcha", captcha);
		//return "test_validator";
	}
	@RequestMapping("/testCaptcha")
	@ResponseBody
	public String testCaptcha(HttpServletRequest request,
			@RequestParam("x") int x,
			@RequestParam("y") int y) {
		String sid = request.getSession().getId();
		String json = userService.getTouchCaptcha(sid);
		Validator[] jlist;
		if(null!=json){
		try {
			jlist = JsonUtils.toObject(Validator[].class, json);
			for(Validator v:jlist){
				int vx = Integer.valueOf(v.x);
				int vy = Integer.valueOf(v.y);
				if(x<vx||x>vx+ValidateUtils.gFontSize){
					return "wrong-x";
				}
				if(y>vy||y<vy-ValidateUtils.gFontSize){
					return "wrong-y";
				}
			}
			userService.putTouchCaptcha("ok", sid);
			return "ok";
		} catch (Exception e) {
		}
		}
		return "wrong";
	}
	/**
	 * 验证并绑定邮箱
	 * @param email
	 * @param captcha
	 * @return
	 */
	@RequestMapping(value = "/checkCaptcha", method = RequestMethod.POST)
	@ResponseBody
	public String checkCaptcha(HttpServletRequest request,
			@RequestParam("email") String email,
			@RequestParam("captcha") String captcha) {
		if (Pattern.matches(blankRegex, email) || email == null
				|| email.isEmpty()) {
			return "1";
		} else if (!Pattern.matches(emailRegex, email)) {
			return "2";
		} else if (Pattern.matches(blankRegex, captcha) || captcha == null
				|| captcha.isEmpty()) {
			return "3";
		} else if (userService.findByEmail(email) != null) {
			return "6";
		} else if (userService.checkCaptcha(email, captcha)) {
			//long uid = Long.parseLong(sessionService.getUid());
			//更新session
			Object obj = request.getAttribute("session");
			if(null != obj && obj instanceof Session){
				Session session = (Session) obj;
				
				User user = userService.get(Long.parseLong(session.getUid()));
				user.setEmail(email);
				userService.update(user);
				userService.delCaptcha(email);
			
				session.setBind("1");
				sessionService.put(session);
			}
			
			return "4";
		} else {
			return "5";
		}
	}
	
	/**
	 * 验证并重置密码
	 * @param email
	 * @param captcha
	 * @return
	 */
	@RequestMapping(value = "/checkPswCaptcha", method = RequestMethod.POST)
	@ResponseBody
	public String checkPswCaptcha(@RequestParam("email") String email,
			@RequestParam("captcha") String captcha) {
		if (Pattern.matches(blankRegex, email) || email == null
				|| email.isEmpty()) {
			return "1";
		} else if (!Pattern.matches(emailRegex, email)) {
			return "2";
		} else if (Pattern.matches(blankRegex, captcha) || captcha == null
				|| captcha.isEmpty()) {
			return "3";
		} else if (userService.findByEmail(email) == null) {
			return "6";
		} else if (userService.checkCaptcha(email, captcha)) {
			User user = userService.findByEmail(email);
			long uid = user.getId();
			String newp = getRandom(3);
			userService.changePassword(uid, newp);
			String information = "尊敬的用户，您好: 系统已将您的密码重置为:"+newp+"，您登陆后可以在个人信息中修改密码";
			this.mailSerivce.send("千寻游戏", email, "千寻游戏-找回密码", information);
			userService.delCaptcha(email);
			return newp;
		} else {
			return "5";
		}
	}
	
	/**
	 * 绑定邮箱:发送验证码到邮箱
	 * @param email
	 * @return
	 */
	@RequestMapping(value = "/getCaptcha", method = RequestMethod.POST)
	@ResponseBody
	public String getCaptcha(@RequestParam("email") String email) {
		if (Pattern.matches(blankRegex, email) || email == null
				|| email.isEmpty()) {
			return "1";
		} else if (!Pattern.matches(emailRegex, email)) {
			return "2";
		} else if (userService.findByEmail(email) != null) {
			return "4";
		} else {
			String captcha = userService.getCaptcha(email);
			String information = "尊敬的用户，您好: 您申请了绑定邮箱，此次操作的验证码为：" + captcha;
			this.mailSerivce.send("千寻游戏", email, "千寻游戏-绑定邮箱", information);
			return "3";
		}
	}
	
	/**
	 * 重置密码:发送验证码到邮箱
	 * @param email
	 * @return
	 */
	@RequestMapping(value = "/getPswCaptcha", method = RequestMethod.POST)
	@ResponseBody
	public String getPswCaptcha(@RequestParam("email") String email) {
		if (Pattern.matches(blankRegex, email) || email == null
				|| email.isEmpty()) {
			return "1";
		} else if (!Pattern.matches(emailRegex, email)) {
			return "2";
		} else if (userService.findByEmail(email) == null) {
			return "4";
		} else {
			String captcha = userService.getCaptcha(email);
			String information = "尊敬的用户，您好: 您申请了找回密码，此次操作的验证码为：" + captcha;
			this.mailSerivce.send("千寻游戏", email, "千寻游戏-找回密码", information);
			return "3";
		}
	}

	private String getRandom(int length) {
		byte bytes[] = new byte[length];
		random.nextBytes(bytes);
		return Hex.encodeHexString(bytes);
	}
}
class Validator{
	public String x;
	public String y;
	public String captcha;
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	public String getCaptcha() {
		return captcha;
	}
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
	
}
