package com.test.qianxun.service;

import java.util.HashMap;
import java.util.Map;

import org.copycat.framework.nosql.RedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.test.qianxun.model.Session;

@Component
public class SessionService {
	private ThreadLocal<String> sessionIdLocal = new ThreadLocal<String>();
	@Autowired
	private RedisTemplate redisTemplate;
	private String key = "session:";

	public Session get(String id) {
		if (id == null) {
			return null;
		}
		Map<String, String> map = redisTemplate.hmget(key + id);
		if (!map.isEmpty()) {
			Session session = new Session();
			session.setId(id);
			session.setUid(map.get("uid"));
			session.setUsername(map.get("uname"));
			session.setBind(map.get("bind"));
			return session;
		}
		return null;
	}

	public boolean isSame(String uid) {
		String id = this.getUid();
		if (id == null) {
			return false;
		}
		return id.equals(uid);
	}

	/**
	 * 获取当前登录用户的会话
	 * 
	 * @return
	 */
	public Session get() {
		String id = sessionIdLocal.get();
		return this.get(id);
	}

	/**
	 * 获取当前登录用户的ID
	 * 
	 * @return
	 */
	public String getUid() {
		String id = sessionIdLocal.get();
		if (id == null) {
			return null;
		}
		return this.get(id).getUid();
	}

	/**
	 * 获取当前登录用户的用户名
	 * 
	 * @return
	 */
	public String getUsername() {
		String id = sessionIdLocal.get();
		if (id == null) {
			return null;
		}
		return this.get(id).getUsername();
	}

	/**
	 * 获取当前登录用户的会话ID
	 * 
	 * @return
	 */
	public String getSessionId() {
		return sessionIdLocal.get();
	}

	public void bind(String id) {
		sessionIdLocal.set(id);
	}

	public void unbind() {
		sessionIdLocal.remove();
	}

	public void put(Session session) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", session.getUid());
		map.put("uname", session.getUsername());
		map.put("bind", session.getBind());
		redisTemplate.hmset(key + session.getId(), map);
	}

	public void invalidate() {
		String id = sessionIdLocal.get();
		redisTemplate.del(key + id);
	}

}