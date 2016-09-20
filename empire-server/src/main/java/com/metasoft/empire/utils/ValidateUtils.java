package com.metasoft.empire.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.metasoft.empire.common.GeneralException;
import com.metasoft.empire.common.constant.ErrorCodes;

/**
 * Auto initialize(reset()) when use
 * @author james
 *
 */

public class ValidateUtils {
	private static final Logger logger = LoggerFactory.getLogger(ValidateUtils.class);
	private static final int CHINESE_MAX = 40959;
	private static final int CHINESE_MIN = 19968;
	private static final int LOWER_MAX = 122;
	private static final int LOWER_MIN = 97;
	private static final int NUMBER_MAX = 57;
	private static final int NUMBER_MIN = 48;
	private static final int UPPER_MAX = 90;
	private static final int UPPER_MIN = 65;
	private static final Set<String> regKeywords = new HashSet<String>();

	private static final Set<String> talkKeywords = new HashSet<String>();
	public static final String REG_KEYWORDS_FILE = "/reg_keywords.txt";
	public static final String TALK_KEYWORDS_FILE = "/talk_keywords.txt";

	public static void checkChars(String content) throws GeneralException {
		for (int i = 0; i < content.length(); i++) {
			int cp = content.charAt(i);
			if (((cp < NUMBER_MIN) || (cp > NUMBER_MAX)) && ((cp < UPPER_MIN) || (cp > UPPER_MAX)) && ((cp < LOWER_MIN) || (cp > LOWER_MAX))
					&& ((cp < CHINESE_MIN) || (cp > CHINESE_MAX)) && (cp != 46) && (cp != 45)) {
				throw new GeneralException(ErrorCodes.ILLEGAL_CHAR, "ILLEGAL_CHAR");
			}
		}
	}

	public static void checkRegKeyword(String content) throws GeneralException {
		for (String keyword : regKeywords)
			if (content.indexOf(keyword) != -1)
				throw new GeneralException(ErrorCodes.ILLEGAL_WORD, keyword );
	}

	public static void checkTalkKeyword(String content) throws GeneralException {
		for (String keyword : talkKeywords)
			if (content.indexOf(keyword) != -1)
				throw new GeneralException(ErrorCodes.ILLEGAL_WORD, "ILLEGAL_WORD" );
	}

	public static String replaceTalkKeyword(String content) {
		for (String keyword : talkKeywords) {
			if (content.toLowerCase().indexOf(keyword) != -1) {
				String rep = "";
				for(int i=0; i<keyword.length(); i++){
					rep+="*";
				}
				content = content.replaceAll(keyword, rep);
			}
		}
		return content;
	}

	public static void checkLength(String content, int minLen, int maxLen) throws GeneralException {
		int len = getStringLength(content);
		if ((len < minLen) || (len > maxLen))
			throw new GeneralException(ErrorCodes.ILLEGAL_WORK_LENGTH, "ILLEGAL_WORD_LENGTH");
	}

	public static int getStringLength(String content) {
		int len = 0;
		if (content == null) {
			return len;
		}
		for (int i = 0; i < content.length(); i++) {
			char c = content.charAt(i);
			if (c >= '一')
				len += 2;
			else {
				len++;
			}
		}

		return len;
	}

	private static void init() {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					ValidateUtils.class.getResourceAsStream(REG_KEYWORDS_FILE), Charset.forName("UTF-8")));
			String keyword;
			while ((keyword = in.readLine()) != null)
				if ((!keyword.trim().equals("")) && (!keyword.startsWith("#"))){
					regKeywords.add(keyword.toLowerCase());
					//logger.debug(keyword.toLowerCase());
				}
		} catch (Exception ex) {
			logger.error("classpath:/reg_keywords.txt not exists", ex);
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException ex) {
				logger.error(ex.getMessage());
			}
		}

		in = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					ValidateUtils.class.getResourceAsStream(TALK_KEYWORDS_FILE), Charset.forName("UTF-8")));
			String keyword;
			while ((keyword = in.readLine()) != null)
				if ((!keyword.trim().equals("")) && (!keyword.startsWith("#"))){
					talkKeywords.add(keyword.toLowerCase());
					//logger.debug(keyword.toLowerCase());
				}
		} catch (Exception ex) {
			logger.error("classpath:/talk_keywords.txt not exists", ex);
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException ex) {
				logger.error(ex.getMessage());
			}
		}
	}

	public static void reset() {
		regKeywords.clear();
		talkKeywords.clear();
		init();
	}

	static {
		reset();
	}
}