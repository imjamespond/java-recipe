package service;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisTemplate2 {
	private JedisPool pool;

	public RedisTemplate2(GenericObjectPoolConfig config, String host, int port,
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
	
	public void lpush(byte[] key, byte[] bytes) {
		Jedis jedis = pool.getResource();
		try {
			jedis.lpush(key, bytes);
		} finally {
			pool.returnResource(jedis);
		}
	}
	
	public List<byte[]> brpop(byte[] key) {
		Jedis jedis = pool.getResource();
		List<byte[]> list = null;
		try {
			list = jedis.brpop(5000,key);
		} finally {
			pool.returnResource(jedis);
		}
		return list;
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
