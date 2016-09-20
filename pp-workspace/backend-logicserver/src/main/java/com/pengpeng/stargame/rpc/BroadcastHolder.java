package com.pengpeng.stargame.rpc;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: mql
 * Date: 13-5-12
 * Time: 下午3:25
 */
public class BroadcastHolder {
	private static final ThreadLocal<List> responseContext = new ThreadLocal<List>();
    /**
     * 当前线程 一些状态的 记录
     * type：1表示有新的任务   2表示有任务进度发生了改变  3表示任务完成
     */
    private static final ThreadLocal<Map<Integer,Integer>> status=  new ThreadLocal<Map<Integer,Integer>>();

	public static void add(Object obj) {
        if  (responseContext.get()==null){
            responseContext.set(new ArrayList());
        }
		responseContext.get().add(obj);
	}

	public static  List get() {
		return responseContext.get();
	}

	public static void clear() {
		responseContext.remove();
        status.remove();
	}

    public static void putStatus(int type){
        if  (status.get()==null){
            status.set(new HashMap<Integer,Integer>());
        }
        status.get().put(type,type);
    }

    public static boolean containStatus(int type){
        if  (status.get()==null){
            status.set(new HashMap<Integer,Integer>());
        }
        return status.get().containsKey(type);
    }

}
