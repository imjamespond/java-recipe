package com.metasoft.util;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.metasoft.model.RestReturnInfo;
import com.metasoft.model.exception.RestInvokeException;

public class HttpClientUtils {
	
	static Logger log = LoggerFactory.getLogger(HttpClientUtils.class);
	
	public static String sendGetRequest(String url, Map<String, Object> paramMap) throws RestInvokeException {
		String line = "";
		String result = "";
		BufferedReader buffer = null;
		try {
			String param = "";
			for (Entry<String, Object> entry : paramMap.entrySet()) {
				if (entry.getValue() != null)
				param += entry.getKey() + "=" + java.net.URLEncoder.encode(entry.getValue().toString(), "UTF-8")+"&";
			}
			param = "?" + param;
			log.info("sendGetRequest url = {"+ url + param + "}");
			URL realUrl = new URL(url+param);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			connection.setConnectTimeout(3000);
			 // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
			buffer = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
			while ((line = buffer.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			throw new RestInvokeException(e);
		} finally {
			try {
				if (buffer != null)
					buffer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		RestReturnInfo  restReturnInfo = new Gson().fromJson(result, new TypeToken<RestReturnInfo>(){}.getType());
		if (!restReturnInfo.isSuccess()) {
			throw new RestInvokeException(restReturnInfo.getErrMsg());
		}
		return new Gson().toJson(restReturnInfo.getData());
	}
	
	public static String sendPostRequest(String url, Map<String, Object> paramMap) throws RestInvokeException {
		String line = "";
		String result = "";
		BufferedReader buffer = null;
		PrintWriter out = null;
		try {
			String param = "";
			for (Entry<String, Object> entry : paramMap.entrySet()) {
				if (entry.getValue() != null)
				param += entry.getKey() + "=" + java.net.URLEncoder.encode(entry.getValue().toString(), "UTF-8")+"&";
			}
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			// 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
			buffer = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			while ((line = buffer.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			throw new RestInvokeException(e);
		} finally {
			try {
				if (out != null)
					out.close();
				if (buffer != null)
				buffer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		RestReturnInfo  restReturnInfo = new Gson().fromJson(result, new TypeToken<RestReturnInfo>(){}.getType());
		if (!restReturnInfo.isSuccess()) {
			throw new RestInvokeException(restReturnInfo.getErrMsg());
		}
		return new Gson().toJson(restReturnInfo.getData());
	}
	
}
