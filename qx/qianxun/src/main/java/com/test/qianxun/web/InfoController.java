package com.test.qianxun.web;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;
import org.copycat.framework.nosql.RedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Jedis;

import com.google.common.collect.ImmutableMap;
import com.qianxun.model.Constant;
import com.qianxun.model.FlyUser;
import com.qianxun.model.job.DateJob;
import com.qianxun.model.job.SigninRecordJob;
import com.qianxun.service.FlyUserService;
import com.qianxun.service.JobService;
import com.qianxun.service.SpringService;
import com.qianxun.util.JsonLite;
import com.test.qianxun.model.Session;
import com.test.qianxun.model.User;
import com.test.qianxun.service.GameService;
import com.test.qianxun.service.SessionService;
import com.test.qianxun.service.UserService;
import com.test.qianxun.util.JsonUtils;

@Controller
@RequestMapping(value = "/info")
public class InfoController {
	// private String usernameRegex = "^(?!\\d+$)[a-z0-9]{6,20}$";
	// private String passwordRegex = "^[A-Za-z0-9]{6,16}$";
	private String blankRegex = "^\\s*$";

	@Value("#{properties['cookie.domain']}")
	private String cookieDomain;

	@Autowired
	private UtillityController utillityController;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private FlyUserService flyUserService;
	@Autowired
	private GameService gameService;
	@Autowired
	private UserService userService;
	@Autowired
	private JobService jobService;
	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 返回登陆信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/")
	@ResponseBody
	public String info(HttpServletRequest request) {
		JsonLite json = new JsonLite();
		Object obj = request.getAttribute("session");
		if (null != obj && obj instanceof Session) {
			Session session = (Session) obj;
			json.appendKeyValue("signined", "1");
			json.appendKeyValue("username", session.getUsername());
			json.appendKeyValue("userid", session.getUid());
			json.appendKeyValue("bind", session.getBind());
		} else {
			json.appendKeyValue("signined", "0");
		}
		// 首页游戏投票排名
		// sb.append(",\"rankInfo\":").append(rank());
		// 约人
		json.appendValString("date", getDate());

		return json.convert2String();
	}

	@RequestMapping(value = "/date/{date}")
	// , method = RequestMethod.POST
	@ResponseBody
	public String date(HttpServletRequest request, @PathVariable String date) {
		String suid = sessionService.getUid();
		if (suid == null) {
			return "need-signin";
		}
		long uid = Long.parseLong(suid);

		// FIXME 检测游戏用户存在,是否可以读redis
		FlyUser fu = flyUserService.get(uid);
		if (null == fu) {
			return "need-signin-flight";
		}
		if (null == fu.getNickname()) {
			return "need-signin-flight";
		}

		jobService.produce(new DateJob(uid, date));

		return "ok";
	}

	public String rank() {
		RedisTemplate redisTemplate = SpringService.getBean(RedisTemplate.class);
		Jedis jedis = redisTemplate.getResource();
		String rs = jedis.get(Constant.GAME_RANK);
		redisTemplate.returnResource(jedis);
		return rs;
	}

	@RequestMapping(value = "/date")
	@ResponseBody
	public String getDate() {
		RedisTemplate redisTemplate = SpringService.getBean(RedisTemplate.class);
		Jedis jedis = redisTemplate.getResource();
		List<String> list = jedis.lrange(Constant.GAME_DATE, 0, 50);
		redisTemplate.returnResource(jedis);
		JsonLite json = new JsonLite(JsonLite.Type.Bracket);
		for (String date : list) {
			json.appendNodeString(date);
		}
		return json.convert2String();
	}

	@RequestMapping("/mobilegame")
	public String mobilegame() {
		return "mobilegame";
	}

	@RequestMapping(value = "/getSalt", method = RequestMethod.POST)
	@ResponseBody
	public String getSalt(@RequestParam("username") String username) {
		//username = username.toLowerCase();
		User user = userService.findByUsername(username);
		if (user == null) {
			return "1";
		}
		String salt = user.getSalt();
		String random = userService.getRandom(username);
		Map<String, String> map = ImmutableMap.of("salt", salt, "random", random);
		return JsonUtils.toJson(map);
	}

	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	@ResponseBody
	public String auth(@RequestParam("username") String username, @RequestParam("password") String password,
			@RequestHeader(value = "X-Real-IP", defaultValue = "127.0.0.1") String ip, HttpServletRequest request,
			HttpServletResponse response) {
		String sid = request.getSession().getId();
		//username = username.toLowerCase();

		User user = userService.findByUsername(username);
		if (user == null) {
			return "1";
		}
		String value = redisTemplate.hget(UserService.kRandomKey, username);
		value = DigestUtils.sha512Hex(user.getPassword() + DigestUtils.sha512Hex(value));
		if (value.equals(password)) {
			redisTemplate.hdel(UserService.kRandomKey, username);
			
			Session session = new Session();
			session.setId(sid);
			session.setUid(Long.toString(user.getId()));
			session.setUsername(user.getUsername());
			session.setBind(user.getEmail() == null ? "0" : "1");
			sessionService.put(session);

			// UserIpRecord userIpRecord =
			// userIpRecordSerivce.findByUidAndIp(user.getId(), ip);
			// if(userIpRecord == null){
			// userIpRecord = new UserIpRecord();
			// userIpRecord.setFrequency(1);
			// userIpRecord.setIp(ip);
			// userIpRecord.setUid(user.getId());
			// this.userIpRecordSerivce.save(userIpRecord);
			// }else{
			// userIpRecord.setFrequency(userIpRecord.getFrequency() + 1);
			// this.userIpRecordSerivce.update(userIpRecord);
			// }
			user.setActiveTime(System.currentTimeMillis());
			jobService.produce(new SigninRecordJob(user, ip));

			Cookie cookie = new Cookie(Constant.COOKIE_NAME, sid);
			cookie.setDomain(cookieDomain);
			cookie.setPath("/");
			cookie.setHttpOnly(false);
			response.addCookie(cookie);

			return "2";
		}
		return "0";
	}

	@RequestMapping("/login")
	public String login(@RequestParam(value = "referer", required = false) String referer, Model model) {
		if (referer == null || referer.isEmpty() || referer.equals("none") || referer.contains("/user/login")
				|| referer.contains("/user/register")) {
			return "user/login";
		} else {
			model.addAttribute("referer", referer);
			return "user/login";
		}
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	@ResponseBody
	public String logout() {
		sessionService.invalidate();
		return "1";
	}

	@RequestMapping("/register")
	public String register(HttpServletRequest request,Model model) {
		utillityController.touchCaptcha(request, model);
		return "user/register";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public String save(@RequestHeader(value = "X-Real-IP", defaultValue = "127.0.0.1") String ip,
			@RequestParam("repassword") String repassword, User user,
			Model model, HttpServletRequest request, HttpServletResponse response) {
		String username = user.getUsername();
		String passwrod = user.getPassword();
		User u = userService.findByUsername(user.getUsername());
		String id = request.getSession().getId();

		//传统输入验证码
		/*
		if (null != validate && validate.length() > 0) {
			Map<String, String> map = userService.getValidate(id);
			String rvalidate = map.get("validate").toLowerCase();
			validate = validate.toLowerCase();
			if (!rvalidate.equals(validate)) {
				userService.delValidate(id);
				return "6";
			}
		} else */
		//点击验证码
		String touchCaptcha = userService.getTouchCaptcha(id);
		if (null == touchCaptcha) {
			//return "6";
		}else if(touchCaptcha.indexOf("ok")<0){
			return "6";
		}

		if (Pattern.matches(blankRegex, username) || username == null || username.isEmpty()) {
			return "1";
		} else if (Pattern.matches(blankRegex, passwrod) || passwrod == null || passwrod.isEmpty()) {
			return "2";
			// } else if (Pattern.matches(blankRegex, repassword)
			// || repassword == null || repassword.isEmpty()) {
			// return "8";
		} else if (u != null) {
			return "3";
		} else if (username.length() > 20 || username.length() < 6) {
			return "4";
		} else if (passwrod.length() > 20 || passwrod.length() < 6) {
			return "4";
			// } else if (!Pattern.matches(usernameRegex, username)) {
			// return "4";
			// } else if (!Pattern.matches(passwordRegex, passwrod)) {
			// return "5";
			// } else if (!Pattern.matches(passwordRegex, repassword)) {
			// return "9";
			// } else if (!passwrod.equals(repassword)) {
			// return "10";

		} else {
			userService.register(user, id);
			userService.delValidate(id);

			jobService.produce(new SigninRecordJob(user, "+" + ip));

			Cookie cookie = new Cookie(Constant.COOKIE_NAME, id);
			cookie.setDomain(cookieDomain);
			cookie.setPath("/");
			cookie.setHttpOnly(false);
			response.addCookie(cookie);

			return "7";
		}
	}

	@RequestMapping("/findpsw")
	public String findpsw() {
		return "user/findpsw";
	}

	private static final long OFFSET = DateUtils.MILLIS_PER_DAY - 5400 * 1000l;

	@RequestMapping("/rank")
	@ResponseBody
	public String match(HttpServletRequest request, HttpServletResponse response) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis() - OFFSET);
		String year = String.valueOf(c.get(Calendar.YEAR));
		String day = String.valueOf(c.get(Calendar.DAY_OF_YEAR));

		Jedis jedis = redisTemplate.getResource();
		String rs = jedis.get("match-rank:" + year + "-" + day);
		redisTemplate.returnResource(jedis);
		// Map<String, String> map =
		// redisTemplate.hmget("match-my-rank:"+year+"-"+day);
		// for(Entry<String, String> entry : map.entrySet()){
		// System.out.println(entry.getKey()+" - > "+entry.getValue());
		// }
		JsonLite jl = new JsonLite(JsonLite.Type.Brace);
		jl.appendKeyValue("date", String.valueOf(c.getTimeInMillis()));
		jl.appendValString("rank", rs);
		return jl.convert2String();
	}

}
