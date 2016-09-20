package com.qianxun.service;

import java.security.SecureRandom;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.copycat.framework.nosql.RedisTemplate;
import org.copycat.framework.sql.SqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qianxun.model.User;

@Service
@Transactional
public class WebUserService extends SqlService<User, Long> {
	@Autowired
	private RedisTemplate redisTemplate;
	private SecureRandom random = new SecureRandom();

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

	public Long register(String name) {
		String salt = getRandom(32);
		String password = DigestUtils.sha512Hex("403" + salt);
		User user = new User();
		user.setUsername(name);
		user.setName(name);
		user.setPassword(password);
		user.setSalt(salt);
		user.setRegisterTime(System.currentTimeMillis());
		user.setActiveTime(System.currentTimeMillis());
		long uid = super.save(user);

		return uid;
	}
	private String getRandom(int length) {
		byte bytes[] = new byte[length];
		random.nextBytes(bytes);
		return Hex.encodeHexString(bytes);
	}
}