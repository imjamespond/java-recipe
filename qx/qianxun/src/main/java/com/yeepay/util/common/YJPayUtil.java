package com.yeepay.util.common;

import java.io.IOException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.entity.RespondJson;
import com.yeepay.util.encrypt.AES;
import com.yeepay.util.encrypt.EncryUtil;
import com.yeepay.util.encrypt.RSA;
import com.yeepay.util.httpclient.HttpClient4Util;
import com.yeepay.util.httpclient.HttpParameter;
import com.yeepay.util.httpclient.HttpResp;

public class YJPayUtil {

	private static Log log = LogFactory.getLog(YJPayUtil.class);

	private ResourceBundle resb1 = ResourceBundle.getBundle("payapi");

	// 从配置文件读取易宝分配的公钥
	private String yibaoPublicKey = resb1
			.getString("payapi.paytest_yibao_publickey");

	// 从配置文件读取商户自己的私钥
	private String merchantPrivateKey = resb1
			.getString("payapi.paytest_merchant_privatekey");

	/**
	 * 请求一键支付接口
	 * 
	 * @param requestURL
	 *            完整的请求地址
	 * @param data
	 *            将业务数据加密后的密文
	 * @param encryptkey
	 *            将加密key加密后的密文
	 * @param merchantaccount
	 *            商户账户号
	 * @param post
	 *            是否发送post请求
	 * @return
	 * @throws IOException
	 */
	public String payAPIRequest(String requestURL, String data,
			String encryptkey, String merchantaccount, boolean post)
			throws IOException {
		// 请求支付API接口，支付API接口的请求的参数相同，所以使用了一个公共方法
		log.info("请求地址为：" + requestURL);

		HttpClient4Util util = null;
		if (requestURL.indexOf("https") >= 0) {
			util = new HttpClient4Util(30000, true);
		} else {
			util = new HttpClient4Util(30000, false);
		}

		HttpParameter httpParameter = new HttpParameter();
		if (merchantaccount != null) {
			httpParameter.add("merchantaccount", merchantaccount);
		}
		if (data != null) {
			httpParameter.add("data", data);
		}
		if (encryptkey != null) {
			httpParameter.add("encryptkey", encryptkey);
		}

		HttpResp httpResp = new HttpResp();
		if (post) {
			httpResp = util.doPost(requestURL, httpParameter, "utf-8");
		} else {
			httpResp = util.doGet(requestURL, httpParameter, "utf-8");
		}
		return httpResp.getText("utf-8");
	}

	/**
	 * 请求一键支付接口，主要用于请求获取清算对账数据接口，请求超时时间设置为1分钟，以免在下载大记录（如10万条消费记录）时超时
	 * 
	 * @param requestURL
	 *            完整的请求地址
	 * @param data
	 *            将业务数据加密后的密文
	 * @param encryptkey
	 *            将加密key加密后的密文
	 * @param merchantaccount
	 *            商户账户号
	 * @param post
	 *            是否发送post请求
	 * @return
	 * @throws IOException
	 */
	public String payAPIClearDataRequest(String requestURL, String data,
			String encryptkey, String merchantaccount, boolean post)
			throws IOException {
		// 请求支付API接口，支付API接口的请求的参数相同，所以使用了一个公共方法
		log.info("请求地址为：" + requestURL);

		HttpClient4Util util = null;
		if (requestURL.indexOf("https") >= 0) {
			util = new HttpClient4Util(60000, true);
		} else {
			util = new HttpClient4Util(60000, false);
		}

		HttpParameter httpParameter = new HttpParameter();
		if (merchantaccount != null) {
			httpParameter.add("merchantaccount", merchantaccount);
		}
		if (data != null) {
			httpParameter.add("data", data);
		}
		if (encryptkey != null) {
			httpParameter.add("encryptkey", encryptkey);
		}

		HttpResp httpResp = new HttpResp();
		if (post) {
			httpResp = util.doPost(requestURL, httpParameter, "utf-8");
		} else {
			httpResp = util.doGet(requestURL, httpParameter, "utf-8");
		}
		return httpResp.getText("utf-8");
	}

	/**
	 * 将一键支付返回的结果进行验证，并解密
	 * 
	 * @param ybResult
	 * @return
	 */
	public String checkYbResult(String ybResult) {
		if (ybResult == null || ybResult.equals("")) {
			return null;
		}
		if (ybResult.indexOf("error") < 0) {
			try {

				// 将易宝返回的字符串转为json对象，并通过解密获取明文处理结果
				RespondJson rj = JSONObject.parseObject(ybResult,
						RespondJson.class);
				String yb_encryptkey = rj.getEncryptkey();
				String yb_data = rj.getData();

				// 对易宝返回的结果进行验签
				boolean passSign = EncryUtil.checkDecryptAndSign(yb_data,
						yb_encryptkey, yibaoPublicKey, merchantPrivateKey);

				if (passSign) {
					// 验签通过
					String yb_aeskey = RSA.decrypt(yb_encryptkey,
							merchantPrivateKey);
					log.info("易宝支付返回结果中使用的aeskey为：" + yb_aeskey);
					String payresult_view = AES.decryptFromBase64(yb_data,
							yb_aeskey);
					log.info("易宝支付接口请求的结果（aes解密后的明文）：" + payresult_view);
					return payresult_view;

				} else {
					log.info("验签未通过！");
					return "验证未通过!";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return ybResult;
		}
		return null;
	}

	/**
	 * 将一键支付返回的清算接口结果进行验证，并解密
	 * 
	 * @param ybResult
	 * @return
	 */
	public String checkYbClearResult(String ybResult) {
		if (ybResult == null || ybResult.equals("")) {
			return null;
		}
		if (ybResult.indexOf("error") < 0) {
			try {
				if (ybResult.indexOf("data") >= 0) {
					return this.checkYbResult(ybResult);
				} else {
					return ybResult;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return ybResult;
		}
		return null;
	}

}
