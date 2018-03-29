package com.metasoft.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PortalUserInfoUtil {

	private static Log log = LogFactory.getLog(PortalUserInfoUtil.class);
	
	private static String portalUrl;
	private static Integer msAppid;
	static {
		InputStream fi = null;
		Properties props = new Properties();
		try {
			fi = PortalUserInfoUtil.class.getResourceAsStream("/application.properties");
			props.load(fi);
			portalUrl = props.getProperty("userinfo.url");
			msAppid = Integer.parseInt(props.getProperty("ms.appid"));
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			if (fi != null) {
				try {
					fi.close();
				} catch (IOException e) {
				}
			}
		}
	}
		
	/**获取用户信息
	 * @param userId
	 * @return return Map<String, String> m ,Key值为areas 区域权限串, appAccount 应用账号, appPsw密码, psw 门户密码
	 */
	public static PortalUserExt getUserInfo(String userId){
		
		String url = portalUrl + "?appId=" + msAppid + "&userId=" + userId;
		Map<String, String> extM = (Map<String, String>) readObject(url);
		return new PortalUserExt(extM.get("appAccount"), extM.get("appPsw"), extM.get("areas"), extM.get("psw"));
	}
	
		public static class PortalUserExt {
		
		public PortalUserExt (String appAccount, String appPsw, String areas, String psw) {
			this.areas = areas;
			this.appAccount = appAccount;
			this.appPsw = appPsw;
			this.psw = psw;
		}
		
		private String areas;
		private String appAccount;
		private String appPsw;
		private String psw;
		
		public String getAreas() {
			return areas;
		}
		
		public String getAppAccount() {
			return appAccount;
		}
		public String getAppPsw() {
			return appPsw;
		}
		
		public String getPsw() {
			return psw;
		}
	}
	private static Object readObject(String url) {
		HttpURLConnection connection = null;
		ObjectInputStream in = null;
		try {
			URL getUrl = new URL(url);
			connection = (HttpURLConnection) getUrl.openConnection();
			connection.connect();
			try {
				in = new ObjectInputStream(connection.getInputStream());
				return in.readObject();
			} catch (Exception e) {
				log.error("反序列化错误", e);
				e.printStackTrace();
			}
		} catch (Exception e) {
			log.error("获取远程数据失败" + url, e);
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;
	}
}
