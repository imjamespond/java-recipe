package com.test.qianxun.service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.copycat.framework.Page;
import org.copycat.framework.nosql.RedisTemplate;
import org.copycat.framework.sql.SqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Jedis;

import com.test.qianxun.model.Session;
import com.test.qianxun.model.User;

@Service
@Transactional
public class UserService extends SqlService<User, Long> {
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private SessionService sessionService;
	public static final String kRandomKey = "random";
	private String captchaKey = "captcha";
	private String validateKey = "validate";
	private static int gExpireSecs = 360;
	private SecureRandom random = new SecureRandom();

	public List<User> listUserWithLimit(long period, int limit) {
		String sql = "select * from web_users where accountstate = 1 and appletime < ? limit ?";
		return sqlTemplate.queryForList(sql, User.class, period, limit);
	}

	public List<User> listUserByAccountState(int accountState, Page page) {
		int total = this.countUserByAccountState(accountState);
		page.setTotal(total);
		String sql = "select * from web_users where accountstate = ? offset ? limit ?";
		return sqlTemplate.queryForList(sql, User.class, accountState,
				page.getOffset(), page.getLimit());
	}

	public int countUserByAccountState(int accountState) {
		String sql = "select count(*) from web_users where accountstate = ?";
		return sqlTemplate.queryForInt(sql, accountState);
	}

	public int countFakeUserByPeriod(long period) {
		String sql = "select count(*) from web_users where accountstate = 1 and appletime < ?";
		return sqlTemplate.queryForInt(sql, period);
	}

	public User findByEmail(String email) {
		String sql = "select * from web_users where email = ?";
		return sqlTemplate.queryForObject(sql, User.class, email);
	}

	public User findByUsername(String username) {
		String sql = "select * from web_users where username = ?";
		return sqlTemplate.queryForObject(sql, User.class, username);
	}

	private String getRandom(int length) {
		byte bytes[] = new byte[length];
		random.nextBytes(bytes);
		return Hex.encodeHexString(bytes);
	}

	public void putValidate(String validate, String sid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("validate", validate);
		redisTemplate.hmset(validateKey + sid, map);
	}

	public Map<String, String> getValidate(String sid) {
		return redisTemplate.hmget(validateKey + sid);
	}
	
	public void putValidator(String validator, String sid) {
		redisTemplate.hset(validateKey , sid, validator);
	}
	public String geValidator(String sid) {
		return redisTemplate.hget(validateKey, sid);
	}
	public void delValidate(String sid) {
		redisTemplate.del(validateKey + sid);
	}
	public void putTouchCaptcha(String captcha, String sid) {
		String key = "touchcaptcha"+sid;
		Jedis jedis = redisTemplate.getResource();	
		try {
			jedis.set(key, captcha);
			jedis.expire(key, gExpireSecs);
		} finally {
			redisTemplate.returnResource(jedis);
		}
	}
	public String getTouchCaptcha(String sid) {
		String key = "touchcaptcha"+sid;
		Jedis jedis = redisTemplate.getResource();	
		try {
			return jedis.get(key);
		} finally {
			redisTemplate.returnResource(jedis);
		}
	}

	public Long register(User user, String sid) {
		String salt = getRandom(32);
		String password = user.getPassword();
		password = DigestUtils.sha512Hex(password + salt);
		user.setPassword(password);
		user.setSalt(salt);
		user.setRegisterTime(System.currentTimeMillis());
		user.setActiveTime(System.currentTimeMillis());
		long uid = super.save(user);

		if(sid != null){
			Session session = new Session();
			session.setId(sid);
			session.setUid(Long.toString(uid));
			session.setUsername(user.getUsername());
			session.setBind(user.getEmail()==null?"0":"1");
			sessionService.put(session);
		}

		return uid;
	}

	public String getRandom(String username) {
		String value = getRandom(32);
		redisTemplate.hset(kRandomKey, username, value);
		return value;
	}

	public User auth(String username, String password, String sid) {
		User user = this.findByUsername(username);
		if (user == null) {
			return null;
		}
		String value = redisTemplate.hget(kRandomKey, username);
		value = DigestUtils.sha512Hex(user.getPassword()
				+ DigestUtils.sha512Hex(value));
		if (value.equals(password)) {
			redisTemplate.hdel(kRandomKey, username);
			user.setActiveTime(System.currentTimeMillis());
			super.update(user);
			Session session = new Session();
			session.setId(sid);
			session.setUid(Long.toString(user.getId()));
			session.setUsername(user.getUsername());
			session.setBind(user.getEmail()==null?"0":"1");
			sessionService.put(session);
			return user;
		}
		return null;
	}

	public String getCaptcha(String key) {
		String value = getRandom(3);
		redisTemplate.hset(captchaKey, key, value);
		return value;
	}

	public boolean checkCaptcha(String key, String captcha) {
		String value = redisTemplate.hget(captchaKey, key);
		if (value != null && value.equals(captcha)) {
			return true;
		}
		return false;
	}

	public void delCaptcha(String key) {
		redisTemplate.hdel(captchaKey, key);
	}

	public void changePassword(long uid, String password) {
		String salt = getRandom(32);
		password = DigestUtils.sha512Hex(password + salt);
		User user = super.get(uid);
		user.setSalt(salt);
		user.setPassword(password);
		super.update(user);
	}
}