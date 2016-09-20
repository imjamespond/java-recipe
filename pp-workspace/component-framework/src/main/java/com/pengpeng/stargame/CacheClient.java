package com.pengpeng.stargame;

//import net.spy.memcached.*;
//import net.spy.memcached.transcoders.SerializingTranscoder;
//import net.spy.memcached.transcoders.Transcoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * MemcachedClient的封装
 * <p>
 * 为何对MemcachedClient再进行一次封装？因为这个Client可以作为一个Service形式存在，由Spring管理，并且可以提供一些公共方法，
 * 比如CAS。
 * </p>
 */
public class CacheClient implements ICache {

    private static final Log logger = LogFactory.getLog(CacheClient.class);

    private static final int CACHE_DATA_EXPRIED_SECONDS = 60 * 60 * 24 * 7;
    private static final int DEFAULT_CACHE_OP_TIMEOUT = 2000;

//    static final SerializingTranscoder transcoder = new SerializingTranscoder();
//
//    {
//        transcoder.setCompressionThreshold(1024 * 512);
//    }
//
//    private MemcachedClient client;
//
//    public CacheClient() {
//        //this("192.168.0.1", 11222);
//    	this("192.168.10.10",11211);
//    }
//
//    /**
//     * 直接使用提供的 net.spy.memcached.MemcachedClient
//     */
//    public CacheClient(MemcachedClient mc) {
//        this.client = mc;
//    }
//
//    /**
//     * 提供一个类似 "192.168.0.1:11211;192.168.0.2:11200" 这样的串，使用逗号或冒号分隔各个服务器的地址
//     *
//     * @param urls 格式为 IP1:PORT1[;IP2:PORT2]...
//     */
//    public CacheClient(String urls) {
//        String[] addressStr = urls.split(",");
//        InetSocketAddress[] address = new InetSocketAddress[addressStr.length];
//        for (int i = 0; i < addressStr.length; i++) {
//            String[] addrPort = addressStr[i].split(":");
//            address[i] = new InetSocketAddress(addrPort[0], Integer.parseInt(addrPort[1]));
//        }
//        try {
//            this.client = new MemcachedClient(address);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public CacheClient(String address, int port) {
//        this(new InetSocketAddress(address, port));
//    }
//
//    public CacheClient(InetSocketAddress... address) {
//        try {
//            this.client = new MemcachedClient(new DefaultConnectionFactory() {
//                @Override
//                public long getOperationTimeout() {
//                    return DEFAULT_CACHE_OP_TIMEOUT;
//                }
//            }, Arrays.asList(address));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public MemcachedClient getClient() {
//        return client;
//    }
//
//    public boolean set(String key, Serializable value) {
//        try {
//            return setAsync(key, value).get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public boolean set(String key, Serializable value, Date expriedDate) {
//
//        try {
//            return setAsync(key, value, expriedDate).get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public boolean set(String key, Serializable value, int expirySec) {
//        try {
//            return setAsync(key, value, expirySec).get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public boolean add(String key, Serializable value) {
//        try {
//            return addAsync(key, value).get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public boolean add(String key, Serializable value, Date expriedDate) {
//        try {
//            return addAsync(key, value, expriedDate).get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public boolean add(String key, Serializable value, int expirySec) {
//        try {
//            return client.add(key, expirySec, value).get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    @SuppressWarnings("unchecked")
//    public <T extends Serializable> T get(String key) {
//        Object result = client.get(key);
//        return null == result ? null : (T) result;
//    }
//
//    @SuppressWarnings("unchecked")
//    public <T extends Serializable> T get(String key, T defaultValue) {
//        try {
//            T value = (T) client.get(key);
//            return value == null ? defaultValue : value;
//        } catch (Exception e) {
//            return defaultValue;
//        }
//    }
//
//    public boolean delete(String key) {
//        try {
//            return deleteAsync(key).get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    @SuppressWarnings("unchecked")
//    public <T extends Serializable> T remove(String key) {
//        T result = (T) get(key, null);
//        if (result != null) {
//            deleteAsync(key);
//        }
//        return result;
//    }
//
//    public boolean containsKey(String key) {
//        return client.get(key) != null;
//    }
//
//    // Batch Methods
//
//    public <T extends Serializable> Map<String, T> getBatch(String... keys) {
//        return client.getBulk(new CastTranscoder<T>(), keys);
//    }
//
//    public <T extends Serializable> Map<String, T> getBatch(Collection<String> keys) {
//        return client.getBulk(keys, new CastTranscoder<T>());
//
//    }
//
//    public <T extends Serializable> Collection<T> getValues(String... keys) {
//        return client.getBulk(new CastTranscoder<T>(), keys).values();
//
//    }
//
//    public <T extends Serializable> Collection<T> getValues(Collection<String> keys) {
//
//        return client.getBulk(keys, new CastTranscoder<T>()).values();
//
//    }
//
//    // Store a map in cache, remove them?
//
//    @SuppressWarnings("unchecked")
//    public Map<String, ? extends Serializable> getMap(String key) {
//        return (Map<String, Serializable>) client.get(key);
//    }
//
//    public boolean setMap(String key, Map<String, ? extends Serializable> value) {
//        try {
//            return client.set(key, CACHE_DATA_EXPRIED_SECONDS, value).get();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public boolean addMap(String key, Map<String, ? extends Serializable> value) {
//        try {
//            return client.add(key, CACHE_DATA_EXPRIED_SECONDS, value).get();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    // Async Methods
//
//    public Future<Boolean> setAsync(String key, Serializable value) {
//        return client.set(key, CACHE_DATA_EXPRIED_SECONDS, value);
//    }
//
//    public Future<Boolean> setAsync(String key, Serializable value, Date expriedDate) {
//        int remainSec = calcRemainSeconds(expriedDate);
//        return client.set(key, remainSec, value);
//    }
//
//    private int calcRemainSeconds(Date expriedDate) {
//        long remainMs = expriedDate.getTime() - System.currentTimeMillis();
//        if (remainMs <= 0)
//            return 1;    //expried at once
//        int remainSec = (int) (remainMs / 1000);
//        return remainSec;
//    }
//
//    public Future<Boolean> setAsync(String key, Serializable value, int expirySec) {
//        return client.set(key, expirySec, value);
//    }
//
//    public Future<Boolean> addAsync(String key, Serializable value) {
//        return client.add(key, CACHE_DATA_EXPRIED_SECONDS, value);
//    }
//
//    public Future<Boolean> addAsync(String key, Serializable value, Date expriedDate) {
//        int remainSec = calcRemainSeconds(expriedDate);
//        return client.add(key, remainSec, value);
//    }
//
//    public Future<Boolean> addAsync(String key, Serializable value, int expirySec) {
//        return client.add(key, expirySec, value);
//    }
//
//    public <T extends Serializable> Future<T> getAsync(String key) {
//        return client.asyncGet(key, new CastTranscoder<T>());
//    }
//
//    public Future<Boolean> deleteAsync(String key) {
//        return client.delete(key);
//    }
//
//    // CAS Methods - may be remove from this api
//
////    @SuppressWarnings("unchecked")
//    /*public <T extends Serializable> CASValue<T> gets(String key) {
//        return (CASValue<T>) client.gets(key);
//    }*/
//
//    public CASResponse cas(String key, long version, Serializable value) {
//        return client.cas(key, version, value);
//    }
//
//    @SuppressWarnings("unchecked")
//    public <T> T casUpdate(String key, T value, CASMutation<T> mutation) {
//        CASMutator<T> mutator = new CASMutator<T>(this.client, new Transcoder<T>() {
//            public T decode(CachedData d) {
//                return (T) transcoder.decode(d);
//            }
//
//            public CachedData encode(T o) {
//                return transcoder.encode(o);
//            }
//
//            public int getMaxSize() {
//                return CachedData.MAX_SIZE;
//            }
//
//            @Override
//            public boolean asyncDecode(CachedData data) {
//                return transcoder.asyncDecode(data);
//            }
//        });
//        try {
//            return mutator.cas(key, value, CACHE_DATA_EXPRIED_SECONDS, mutation);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    // Counter Methods - remove them
//
//    public void initCounter(String counterKey) {
//        delete(counterKey);
//        set(counterKey, "0");
//    }
//
//    public long getCounter(String counterKey) {
//        Serializable result = get(counterKey);
//        if (result == null)
//            return 0l;
//        return Long.parseLong(((String) result).trim());
//    }
//
//    public long incrAndGetCounter(String counterKey) {
//        return client.incr(counterKey, 1);
//    }
//
//    public long decrAndGetCounter(String counterKey) {
//        return client.decr(counterKey, 1);
//    }
//
//    public void shutDown() {
//        this.client.shutdown();
//    }
//
//    public void flushAll() {
//        this.client.flush();
//    }
//
//    //这个Transcoder唯一的作用是将Future里面的返回值转型
//
//    class CastTranscoder<T> implements Transcoder<T> {
//
//        @Override
//        public boolean asyncDecode(CachedData data) {
//            return transcoder.asyncDecode(data);
//        }
//
//        @SuppressWarnings("unchecked")
//        @Override
//        public T decode(CachedData data) {
//            return (T) transcoder.decode(data);
//        }
//
//        @Override
//        public CachedData encode(T object) {
//            return transcoder.encode(object);
//        }
//
//        @Override
//        public int getMaxSize() {
//            return transcoder.getMaxSize();
//        }
//
//    }

    @Override
    public boolean set(String key, Serializable value) {
        return false;  //TODO:方法需要实现
    }

    @Override
    public boolean set(String key, Serializable value, Date expriedDate) {
        return false;  //TODO:方法需要实现
    }

    @Override
    public boolean set(String key, Serializable value, int expirySec) {
        return false;  //TODO:方法需要实现
    }

    @Override
    public <T extends Serializable> T get(String key) {
        return null;  //TODO:方法需要实现
    }

    @Override
    public <T extends Serializable> Collection<T> getValues(Collection<String> keys) {
        return null;  //TODO:方法需要实现
    }

    @Override
    public <T extends Serializable> Map<String, T> getBatch(Collection<String> keys) {
        return null;  //TODO:方法需要实现
    }

    @Override
    public boolean delete(String key) {
        return false;  //TODO:方法需要实现
    }

    @Override
    public boolean add(String key, Serializable value) {
        return false;  //TODO:方法需要实现
    }

    @Override
    public boolean add(String key, Serializable value, Date expriedDate) {
        return false;  //TODO:方法需要实现
    }

    @Override
    public boolean add(String key, Serializable value, int expirySec) {
        return false;  //TODO:方法需要实现
    }
}

