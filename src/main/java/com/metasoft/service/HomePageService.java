package com.metasoft.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.metasoft.model.Document;
import com.metasoft.model.Notice;
import com.metasoft.sso.client.util.SsoHelper;

@Service
public class HomePageService {
	
	private String serverRoot;
	
	@PostConstruct
	public void init() {
		serverRoot = SsoHelper.getServerRoot();
	}
	
	public List<Document> listDocs() {
		String url = serverRoot+"/docinfo/alldoc";
		String result = getResultStrByURL(url);
		Gson gson = new Gson();
		List<Document> docs = gson.fromJson(result, new TypeToken<List<Document>>(){}.getType());
		for (Document doc: docs) {
			String downloadUrl = serverRoot+"/docinfo/downdoc?id="+doc.getId();
			doc.setUrl(downloadUrl);
		}
		return docs;
	}

	private String getResultStrByURL(String url) {
		InputStream in = getURLInputStream(url, 3000);
		BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
		String line = "";
		String result = "";
		try {
			while ((line = buffer.readLine()) != null) {
				result += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				buffer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public List<Notice> listNotices() {
		String url = serverRoot+"/notice/top5notices";
		String result = getResultStrByURL(url);
		Gson gson = new Gson();
		List<Notice> notices = null;
		try {
			notices = gson.fromJson(result, new TypeToken<List<Notice>>(){}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (Notice notice: notices) {
			String detailURL = serverRoot+"/notice/detail?id="+notice.getNoticeId();
			notice.setPublishTime(getDateStrFromDateInt(notice.getPublishTime()));
			notice.setUpdateTime(getDateStrFromDateInt(notice.getUpdateTime()));
			notice.setDetailURL(detailURL);
		}
		return notices;
	}
	
	
	
	private String getDateStrFromDateInt(String time) {
		String dateStr = "";
		if (!StringUtils.isEmpty(time)) {
			Long dateInt = 0L;
			dateInt = Long.valueOf(time);
			Date date = new Date(dateInt);
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
			dateStr = formatter.format(date);
		}
		return dateStr;
	}

	private  InputStream getURLInputStream(String url, Integer timeout) {
		HttpURLConnection connection = null;
		InputStream in = null;
		try {
			URL getUrl = new URL(url);
			connection = (HttpURLConnection) getUrl.openConnection();
			connection.setConnectTimeout(timeout);
			connection.connect();
			in = connection.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return in;
	}

}
