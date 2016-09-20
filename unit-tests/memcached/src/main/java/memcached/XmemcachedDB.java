package com.james.memcached;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import net.rubyeye.xmemcached.CASOperation;
import net.rubyeye.xmemcached.GetsResponse;
import net.rubyeye.xmemcached.KeyIterator;
import net.rubyeye.xmemcached.KeyProvider;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

//@Service
public class XmemcachedDB {

	private static final Log log = LogFactory.getLog(XmemcachedDB.class);

	@Value("${memcached.host}")
	private String host;

	private static MemcachedClient memcachedClient;

	@PostConstruct
	public void init() {
		log.info("initializing XmemchacedDao...");
		// MemcachedClientBuilder builder = new
		// XMemcachedClientBuilder(AddrUtil.getAddresses("localhost:12000 localhost:12001"),new
		// int[]{1,3});
		// builder.setCommandFactory(new BinaryCommandFactory());//use binary
		// protocol
		MemcachedClientBuilder builder = new XMemcachedClientBuilder(
				AddrUtil.getAddresses(host));
		try {
			memcachedClient = builder.build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String get(String key) {
		try {
			String value = memcachedClient.get(key);
			log.info("key:" + key + " val:" + value);
			return value;
		} catch (MemcachedException e) {
			System.err.println("MemcachedClient operation fail");
			e.printStackTrace();
		} catch (TimeoutException e) {
			System.err.println("MemcachedClient operation timeout");
			e.printStackTrace();
		} catch (InterruptedException e) {
			// ignore
		}
		return null;
	}

	public void set(String key, String value) {
		try {
			memcachedClient.set(key, 0, value);

		} catch (MemcachedException e) {
			System.err.println("MemcachedClient operation fail");
			e.printStackTrace();
		} catch (TimeoutException e) {
			System.err.println("MemcachedClient operation timeout");
			e.printStackTrace();
		} catch (InterruptedException e) {
			// ignore
		}
	}

	public void delete(String key) {
		try {
			memcachedClient.delete(key);

		} catch (MemcachedException e) {
			System.err.println("MemcachedClient operation fail");
			e.printStackTrace();
		} catch (TimeoutException e) {
			System.err.println("MemcachedClient operation timeout");
			e.printStackTrace();
		} catch (InterruptedException e) {
			// ignore
		}

	}

	public Map<String, GetsResponse<String>> gets(Collection<String> keys) {
		try {
			Map<String, GetsResponse<String>> value = memcachedClient.gets(keys);
			return value;
		} catch (MemcachedException e) {
			System.err.println("MemcachedClient operation fail");
			e.printStackTrace();
		} catch (TimeoutException e) {
			System.err.println("MemcachedClient operation timeout");
			e.printStackTrace();
		} catch (InterruptedException e) {
			// ignore
		}
		return null;
	}

	public Long getCas(String key) {
		GetsResponse<String> result;
		try {
			result = memcachedClient.gets(key);
			if(null == result){
				return null;
			}
			long cas = result.getCas();
			return cas;
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MemcachedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void casSet(String key, String value, long cas) {
		try {
			// 尝试将key的值更新为value
			if (!memcachedClient.cas(key, 0, value, cas)) {
				log.error("cas error");
			}
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MemcachedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void tryCasSet(String key, final String value, long cas) {
		try {
			memcachedClient.cas(key, 0, new CASOperation<String>() {
				public int getMaxTries() {
					// Max retry times,If retry times is great than this
					// value,xmemcached will throw TimeoutException
					return Integer.MAX_VALUE;
				}

				@Override
				public String getNewValue(long cas, String preStr) {
					log.info("preStr:"+preStr);
					return value;
				}
			});
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MemcachedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getStats() {

		Map<InetSocketAddress, Map<String, String>> map;
		try {
			map = memcachedClient.getStats();
			Iterator<Entry<InetSocketAddress, Map<String, String>>> it = map
					.entrySet().iterator();
			while (it.hasNext()) {
				Entry<InetSocketAddress, Map<String, String>> entry = it.next();
				Map<String, String> value = entry.getValue();
				log.info("getAllKeys");
			}
		} catch (MemcachedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void close() {
		try {
			// close memcached client
			memcachedClient.shutdown();
		} catch (IOException e) {
			System.err.println("Shutdown MemcachedClient fail");
			e.printStackTrace();
		}
	}

}