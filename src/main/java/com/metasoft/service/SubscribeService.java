package com.metasoft.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.keymobile.dataSharingMgr.interfaces.resource.TableModel;
import com.metasoft.model.RestReturnInfo;
import com.metasoft.model.exception.RestInvokeException;

@Service
public class SubscribeService {
	
	private Logger logger = LoggerFactory.getLogger(SubscribeService.class);
	public static final int selectFlag = 1;
	public static final int unSelectFlag = 0;
	@Value("${sub.url}")
	private String subscriptionAddr;
	
	public void setSubscriptionAddr(String subscriptionAddr) {
		this.subscriptionAddr = subscriptionAddr;
	}
	
	public String getSubscriptionAddr() {
		return subscriptionAddr;
	}
	
	public void subscribeTableModel(String userId, TableModel tableModel) throws RestInvokeException {
		String url = getSubscriptionAddr()+"/mymodel/save";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		paramMap.put("tmodelId", tableModel.getTableModelId());
		paramMap.put("tmodelName", tableModel.getName());
		paramMap.put("dataCycle", tableModel.getCycle());
		sendPostRequest(url, paramMap);
	}
	
	public List<FavoriteIndicator> getFavoriteIndicatorValues(String userId) throws RestInvokeException {
		String url = getSubscriptionAddr()+"/mymodel/getSelectValue";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		
		String valuesStr = sendGetRequest(url, paramMap);
		List<FavoriteIndicator> values = new Gson().fromJson(valuesStr, new TypeToken<List<FavoriteIndicator>>(){}.getType());
		return values;
	}
	
	public List<FavoriteIndicator> getDefaultFavoriteIndicatorValues() throws RestInvokeException {
		String url = getSubscriptionAddr()+"/mymodel/getDefaultSelectValue";
		Map<String, Object> paramMap = new HashMap<>();
		String valuesStr = sendGetRequest(url, paramMap);
		List<FavoriteIndicator> values = new Gson().fromJson(valuesStr, new TypeToken<List<FavoriteIndicator>>(){}.getType());
		return values;
	}
	
	public static  class FavoriteIndicator {
		public String cycle;
		public String date;
		public String name;
		public String unit;
		public String value;
		public String type; // amount area peoplenum percentage
		
	}
	
	
	public static void main(String[] args) throws RestInvokeException  {
		//新增
		SubscribeService service = new SubscribeService();
		TableModel model = new TableModel("", "");
		model.setTableModelId("tableModel=50");
		model.setName("测试模型2");
//		service.selectSubscribeTableModel("user=1", model);
		service.subscribeTableModel("user=1", model);
		List<String> users = new ArrayList<>();
		users.add("user=1");
		users.add("user=2");
		users.add("user=3");
		service.getFavoriteIndicatorValues("user=1");
		service.getUserSubTableModelds("user=1");
		
//		//查询
//		List<String> tmodels = service.getUserSubTableModelIds("user=1");
//		System.out.println(tmodels);
		
//		List<String> tmodels = service.getUserSelectTableModelIds("user=1");
//		System.out.println(tmodels);
		
		Map<String, UserSubTableModel> subTableModels = service.getUserSubTableModelds("user=1");
		System.out.println(subTableModels);
//		//取消
//		service.unSubscribeKPI("user=1", "tableModel=1");
	}
	
	public void unSubscribeTableModel(String userId, String tableModelId) throws RestInvokeException {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		paramMap.put("tmodelId", tableModelId);
		String url = getSubscriptionAddr()+"/mymodel/delete";
		sendGetRequest(url, paramMap);
	}
	
	public void selectSubscribeTableModel(String userId, TableModel tableModel) throws RestInvokeException {
		setTableModelSelectedByState(userId, tableModel, 1);
	}
	
	public void unSelectSubscribeTableModel(String userId, TableModel tableModel) throws RestInvokeException {
		setTableModelSelectedByState(userId, tableModel, 0);
	}
	
	private void setTableModelSelectedByState(String userId, TableModel tableModel, int selectFlag) throws RestInvokeException {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		paramMap.put("tmodelId", tableModel.getTableModelId());
		paramMap.put("tmodelId", tableModel.getTableModelId());
		paramMap.put("tmodelName", tableModel.getName());
		paramMap.put("dataCycle", tableModel.getCycle());
		paramMap.put("selectFlag", selectFlag);
		String url = getSubscriptionAddr()+"/mymodel/save";
		sendPostRequest(url, paramMap);
	}
	
	public List<String> getUserSubTableModelIds(String userId) throws RestInvokeException {
		String url = getSubscriptionAddr()+"/mymodel";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		String tableModelIdsStr = sendGetRequest(url, paramMap);
		List<String> tableModelIds = new Gson().fromJson(tableModelIdsStr, new TypeToken<List<String>>(){}.getType());
		return tableModelIds;
	}
	
	public Integer countAllSubTableModel() throws RestInvokeException {
		String url = getSubscriptionAddr()+"/mymodel/getCount";
		Map<String, Object> paramMap = new HashMap<>();
		String tableModelIdsStr = sendGetRequest(url, paramMap);
		Integer count = new Gson().fromJson(tableModelIdsStr, new TypeToken<Integer>(){}.getType());
		return count;
	}
	
	public Map<String, UserSubTableModel> getUserSubTableModelds(String userId) throws RestInvokeException {
		String url = getSubscriptionAddr()+"/mymodel/listModels";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		String tableModelsStr = sendGetRequest(url, paramMap);
		List<UserSubTableModel> tableModelIds = new Gson().fromJson(tableModelsStr, new TypeToken<List<UserSubTableModel>>(){}.getType());
		Map<String, UserSubTableModel> userTModels = new HashMap<>();
		for (UserSubTableModel tmodel : tableModelIds) {
			String mId = tmodel.id.tmodelId;
			String uId = tmodel.id.tmodelId;
			int selectFlag = tmodel.selectFlag;
			userTModels.put(mId, new UserSubTableModel(uId, mId, selectFlag));
		}
		return userTModels;
	}
	
	public static class UserSubTableModel {
		
		public UserSubTableModel(String uId, String mId, int selectFlag) {
			this.id.userId = uId;
			this.id.tmodelId = mId;
			this.selectFlag = selectFlag;
		}
		public userSubTableEmbId id = new userSubTableEmbId();
		public String tmodelId;
		public Integer selectFlag;
	}
	
	public static class userSubTableEmbId {
		public String userId;
		public String tmodelId;
	}
	
	private String sendGetRequest(String url, Map<String, Object> paramMap) throws RestInvokeException {
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
			URL realUrl = new URL(url+param);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
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
		RestReturnInfo restReturnInfo = new Gson().fromJson(result, new TypeToken<RestReturnInfo>(){}.getType());
		if (!restReturnInfo.isSuccess()) {
			logger.error(restReturnInfo.getErrMsg());
			throw new RestInvokeException(restReturnInfo.getErrMsg());
		}
		return new Gson().toJson(restReturnInfo.getData());
	}
	
	private String sendPostRequest(String url, Map<String, Object> paramMap) throws RestInvokeException {
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
		RestReturnInfo restReturnInfo = new Gson().fromJson(result, new TypeToken<RestReturnInfo>(){}.getType());
		if (!restReturnInfo.isSuccess()) {
			logger.error(restReturnInfo.getErrMsg());
			throw new RestInvokeException(restReturnInfo.getErrMsg());
		}
		return new Gson().toJson(restReturnInfo.getData());
	}
	

	public void checkTableModelIsValidInSub(String userId, List<TableModel> tableModels) throws RestInvokeException {
		List<String> subTmodelIds = getUserSubTableModelIds(userId);
		List<String> privilegeTmodelIds = new ArrayList<>();
		String url = getSubscriptionAddr()+"/mymodel/delete";
		for (TableModel extTableModel : tableModels) {
			privilegeTmodelIds.add(extTableModel.getTableModelId());
		}
		for (String subTmodelId : subTmodelIds) {
			if (!privilegeTmodelIds.contains(subTmodelId)) {
				Map<String, Object> paramMap = new HashMap<>();
				paramMap.put("userId", userId);
				paramMap.put("tmodelId", subTmodelId);;
				sendGetRequest(url, paramMap);
			}
		}
		
	}

}
