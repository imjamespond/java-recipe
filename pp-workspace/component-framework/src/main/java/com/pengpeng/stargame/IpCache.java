package com.pengpeng.stargame;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component
public class IpCache {
	private final Log logger =LogFactory.getLog(super.getClass());;
	private ConcurrentMap<String, Serializable> cache;

	public IpCache() {
		this.cache = new ConcurrentHashMap<String,Serializable>();
	}

	public boolean set(String ip, long expireDate) {
		boolean result = false;
		if (System.currentTimeMillis() - expireDate < 0L) {
			this.cache.put(ip, Long.valueOf(expireDate));
			result = true;
		}
		return result;
	}

	public boolean get(String ip) {
		boolean result = false;
		Long expireDate = (Long) this.cache.get(ip);
		if (null != expireDate) {
			if (System.currentTimeMillis() - expireDate.longValue() < 0L)
				result = true;
			else {
				this.cache.remove(ip);
			}
		}
		return result;
	}
}