package com.qianxun.util;

import java.util.concurrent.atomic.AtomicInteger;

public class PaymentUtil {
	private static AtomicInteger atomInt = new AtomicInteger();
	/**
	 * @param token 服务器唯一序号
	 * @return
	 */
	public static String generateOrderIdStr(String token){
		long now = System.currentTimeMillis();
		int i = atomInt.incrementAndGet();
		return String.format("%s%d%05d", token, now, i%100000);//5位自增尾数,不足以0填充
	}
	
	public static void main(String[] args) throws Exception {
		for(int i = 0; i<100000; i++){
			String id = PaymentUtil.generateOrderIdStr("1");
			if(i%1000>0)continue;
			System.out.println(id);
		}
	}
}