package org.copycat.framework.nosql;

import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisTemplate {
	private JedisPool pool;

	public RedisTemplate(JedisPoolConfig config, String host, int port,
			int timeout, String password) {
		pool = new JedisPool(config, host, port, timeout, password);
	}

	public void close() {
		pool.destroy();
	}

	public void rpush(byte[] key, byte[] bytes) {
		Jedis jedis = pool.getResource();
		try {
			jedis.rpush(key, bytes);
		} finally {
			pool.returnResource(jedis);
		}
	}

	public String hget(String key, String field) {
		Jedis jedis = pool.getResource();
		try {
			return jedis.hget(key, field);
		} finally {
			pool.returnResource(jedis);
		}
	}

	public void hset(String key, String field, String value) {
		Jedis jedis = pool.getResource();
		try {
			jedis.hset(key, field, value);
		} finally {
			pool.returnResource(jedis);
		}
	}

	public void hmset(String key, Map<String, String> map) {
		Jedis jedis = pool.getResource();
		try {
			jedis.hmset(key, map);
		} finally {
			pool.returnResource(jedis);
		}
	}

	public Map<String, String> hmget(String key) {
		Jedis jedis = pool.getResource();
		try {
			return jedis.hgetAll(key);
		} finally {
			pool.returnResource(jedis);
		}
	}

	public void hdel(String key,String field){
		Jedis jedis = pool.getResource();
		try {
			jedis.hdel(key, field);
		} finally {
			pool.returnResource(jedis);
		}
	}

	public void del(String key) {
		Jedis jedis = pool.getResource();
		try {
			jedis.del(key);
		} finally {
			pool.returnResource(jedis);
		}
	}

	public Jedis getResource() {
		return pool.getResource();
	}

	public void returnResource(Jedis jedis) {
		pool.returnResource(jedis);
	}
}
