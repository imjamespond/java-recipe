package com;

import java.io.*;
import java.text.*;
import java.util.*;

public class StringUtils {

	/**
	 * Returns a specified value while the original value is a 'null' value. And
	 * return the original value if the value is not 'null'.
	 * 
	 * @param originalValue
	 *            The original value.
	 * @param defaultValue
	 *            The value to be assigned to the null value.
	 * @param isNullString
	 *            indicate where the original value will be a string defined as
	 *            "null" or not.
	 * @return
	 */
	public static String nullValue(String originalValue, String defaultValue,
			boolean isNullString) {
		if (originalValue == null) {
			return defaultValue;
		}

		if (isNullString == true && originalValue.equalsIgnoreCase("null")) {
			return defaultValue;
		}

		return originalValue;
	}

	public static String join(Set<?> set, String sep) {
		String retval = "";

		Object[] arr = set.toArray();
		for (int i = 0; i < arr.length; i++) {
			Object obj = arr[i];

			if (obj instanceof String == false) {
				continue;
			}

			if (retval.equals("")) {
				retval = (String) obj;
			} else {
				retval += sep + (String) obj;
			}
		}

		return retval;
	}

	public static String join(List<?> list, String sep) {
		String retval = "";

		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);

			if (obj instanceof String == false) {
				continue;
			}

			if (retval.equals("")) {
				retval = (String) obj;
			} else {
				retval += sep + (String) obj;
			}
		}

		return retval;
	}

	public static String joinInt(List<?> list, String sep) {
		String retval = "";

		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);

			if (obj instanceof Integer == false) {
				continue;
			}
			Integer iobj = (Integer) obj;

			if (retval.equals("")) {
				retval = "" + iobj.intValue();
			} else {
				retval += sep + iobj.intValue();
			}
		}

		return retval;
	}

	public static String join(Object[] l,String sep){
		StringBuffer sb = new StringBuffer();

		if (l == null) {
			return "";
		}

		for (int i = 0; i < l.length; i++) {
			if (i == 0) {
				sb.append(String.valueOf(l[i]));
			} else {
				sb.append(sep);
				sb.append(String.valueOf(l[i]));
			}
		}

		return sb.toString();
	}
	
//	public static String join(String[] list, String sep) {
//		StringBuffer sb = new StringBuffer();
//
//		if (list == null) {
//			return "";
//		}
//
//		for (int i = 0; i < list.length; i++) {
//			if (i == 0) {
//				sb.append(list[i]);
//			} else {
//				sb.append(sep);
//				sb.append(list[i]);
//			}
//		}
//
//		return sb.toString();
//	}

	public static String join(String[] arr, String sep, String beforeStr,
			String afterStr) {
		String retval = "";

		if (arr == null) {
			return "";
		}

		for (int i = 0; i < arr.length; i++) {

			if (retval.equals("")) {
				retval = beforeStr + arr[i] + afterStr;
			} else {
				retval += sep + beforeStr + arr[i] + afterStr;
			}
		}

		return retval;
	}

	public static String[] split(String instr, String sep) {
		return split(instr, sep, -1);
	}

	// TODO implement the handling for maxToken
	public static String[] split(String instr, String sep, int maxToken) {
		ArrayList<String> list;

		list = new ArrayList<String>();

		// For xxxx sep xxxx sep xxx sep xxxxx
		// | | |
		// 1st idx 2nd idx 3rd idx ......

		int start = 0;
		int seplen = sep.length();
		while (true) {
			int idx = instr.indexOf(sep, start);

			if (idx < 0) {
				break;
			}

			String str = instr.substring(start, idx);

			list.add(str);

			start = idx + seplen;
		}

		if (start < instr.length()) {
			String str = instr.substring(start);

			list.add(str);
		}

		String arr[] = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr[i] = list.get(i);
		}

		return arr;
	}

	public static int[] indexOf(String instr, String[] str, int start) {
		int idx = -1;
		int match = -1;
		for (int i = 0; i < str.length; i++) {
			int myidx = instr.indexOf(str[i], start);
			// System.out.println("DEBUG: " + instr + " " + str[i] + "[" + myidx
			// + "]");
			if (myidx >= 0 && (myidx < idx || idx == -1)) {
				match = i;
				idx = myidx;
			}
		}

		return new int[] { match, idx };
	}

	public static String[] split(String instr, String[] sep) {
		return split(instr, sep, -1, false);
	}

	public static String[] split(String instr, String[] sep, int maxToken) {
		return split(instr, sep, maxToken, false);
	}

	public static String[] split(String instr, String[] sep, boolean removeEmpty) {
		return split(instr, sep, -1, removeEmpty);
	}

	public static String[] split(String instr, String[] sep, int maxToken,
			boolean removeEmpty) {
		ArrayList<String> list;

		list = new ArrayList<String>();

		// For xxxx sep xxxx sep xxx sep xxxxx
		// | | |
		// 1st idx 2nd idx 3rd idx ......

		int start = 0;

		while (true) {
			int[] ret = indexOf(instr, sep, start);
			int match = ret[0];
			int idx = ret[1];

			if (idx < 0) {
				break;
			}

			String str = instr.substring(start, idx);

			if (removeEmpty) {
				// Only add if the string is not null
				str = str.trim();
				if (str.equals("") == false) {
					list.add(str);
				}
			} else {
				list.add(str);
			}

			start = idx + sep[match].length();

			if (maxToken > 0 && list.size() >= maxToken - 1) {
				break;
			}
		}

		if (start < instr.length()) {
			String str = instr.substring(start);
			if (removeEmpty) {
				// Only add if the string is not null
				str = str.trim();
				if (str.equals("") == false) {
					list.add(str);
				}
			} else {
				list.add(str);
			}
		}

		String arr[] = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr[i] = list.get(i);
		}

		return arr;
	}

	public static String replace(String instr, String findStr, String replaceStr) {
		// Algoithm : split using findStr and join them using replaceStr
		// E.g Replace all "x" with "--" in String "x0x2x3x"
		// After Split: 0 2 3
		// When joining
		// 1. Check instr.startsWith(findStr) : YES=> append replaceStr
		// 2. loop the array of splitted string
		// 3. Check instr.endsWith(findStr) : YES=> append replaceStr

		String[] list = split(instr, findStr);

		String outstr = "";

		if (instr.startsWith(findStr)) {
			outstr += replaceStr;
		}

		for (int i = 0; i < list.length; i++) {
			if (i != 0) {
				outstr += replaceStr;
			}

			outstr += list[i];
		}

		if (instr.endsWith(findStr)) {
			outstr += replaceStr;
		}

		return outstr;
	}

	public static String leftPad(int inval, char chr, int maxlen) {
		return pad(String.valueOf(inval), chr, maxlen, 2);
	}

	public static String leftPad(String instr, char chr, int maxlen) {
		return pad(instr, chr, maxlen, 2);
	}

	public static String rightPad(int inval, char chr, int maxlen) {
		return pad(String.valueOf(inval), chr, maxlen, 1);
	}

	public static String rightPad(String instr, char chr, int maxlen) {
		return pad(instr, chr, maxlen, 1);
	}

	public static String pad(String instr, char chr, int maxlen, int direction) {
		if (instr == null) {
			instr = "";
		}

		int strlen = instr.length();

		if (strlen > maxlen) { // No room to do any padding
			return instr;
		}

		// Left Padding
		// 1. Find num of char to pad
		// 2. Loop the
		int padsize = maxlen - strlen;

		String padstr = repeat(chr, padsize);

		if (direction == 1) { // 1 = Right
			return instr + padstr;
		} else { // Else = Left
			return padstr + instr;
		}
	}

	public static String repeat(char c, int times) {
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < times; i++) {
			buf.append(c);
		}

		return buf.toString();
	}

	public static String repeat(String str, int times) {
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < times; i++) {
			buf.append(str);
		}

		return buf.toString();
	}

	public static String[] sortStringArray(String[] arr) {

		if (arr == null || arr.length == 0) {
			return new String[0];
		}

		// Create the sorted set
		SortedSet<String> set = new TreeSet<String>();

		// Add elements to the set
		for (int i = 0; i < arr.length; i++) {
			set.add(arr[i]);
		}

		// Create an array containing the elements in a set (in this case a
		// String array).
		// The elements in the array are in order.
		return set.toArray(new String[set.size()]);
	}

	public static String[] insertStringToList(String[] list, String str) {
		if (list == null) {
			return null;
		}

		if (str == null) {
			return list;
		}

		String[] newlist = new String[list.length + 1];

		newlist[0] = str;

		for (int i = 1; i <= list.length; i++) {
			newlist[i] = list[i - 1];
		}

		return newlist;
	}

	public static boolean isLetterDigit(String str) {
		if (str.length() == 0) {
			return true; // kwok: should be true or false?
		}
		return str.matches("^[A-Za-z0-9]+$");
	}

	public static Hashtable<String, String> stringArrayToHash(String[] data, String[] cols) {
		Hashtable<String, String> h = new Hashtable<String, String>();

		if (data == null || cols == null) {
			return h;
		}

		int dataLen = data.length;
		int colLen = cols.length;

		for (int i = 0; i < colLen; i++) {
			String dataVal = "";
			String col = cols[i];
			if (i >= 0 && i < dataLen) {
				dataVal = data[i];
			}
			h.put(col, dataVal);
		}

		return h;
	}

	/**
	 * Append one hash table to another
	 * 
	 * 
	 * @param mainHash
	 * @param addHash
	 */
	public static void appendHash(Hashtable<String, String> mainHash, Hashtable<?, ?> addHash) {
		if (mainHash == null || addHash == null) {
			return;
		}

		for (Enumeration<?> e = addHash.keys(); e.hasMoreElements();) {
			String name = (String) e.nextElement();
			String data = (String) addHash.get(name);

			mainHash.put(name, data);
		}
	}

	public static String toAsciiUnicodeString(String str) {
		if (str == null) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char a = str.charAt(i);

			if (a < 0x80) { // Normal ascii
				if (a == '&') {
					sb.append("&amp;");
				} else {
					sb.append(a);
				}
			} else {
				sb.append("&#x");
				sb.append(Integer.toHexString(a));
				sb.append(";");
			}
		}
		return sb.toString();
	}

	/**
	 * @param parameter
	 * @param i
	 * @return
	 */
	public static long longValue(String str, long defaultVal) {
		if (str == null) {
			return defaultVal;
		}

		try {
			return Long.parseLong(str);
		} catch (NumberFormatException e) {
			return defaultVal;
		}
	}

	public static int intValue(String str, int defaultVal) {
		if (str == null) {
			return defaultVal;
		}

		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return defaultVal;
		}
	}

	public static float floatValue(String str, float defaultVal) {
		if (str == null) {
			return defaultVal;
		}

		try {
			return Float.parseFloat(str);
		} catch (NumberFormatException e) {
			return defaultVal;
		}
	}

	public static double doubleValue(String str, double defaultVal) {
		if (str == null) {
			return defaultVal;
		}

		try {
			return Float.parseFloat(str);
		} catch (NumberFormatException e) {
			return defaultVal;
		}
	}

	public static String toCodedString(String str) {
		return toCodedString(str, true);
	}

	public static String toCodedString(String str, boolean conv) {
		if (str == null) {
			return "";
		}

		String specialChar = "&\"\\<>";
		String specialStr[] = { "&amp;", "&quot;", "&apos;", "&lt;", "&gt;" };

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char a = str.charAt(i);

			if (a < 0x80) { // Normal ascii
				if (conv == false) {
					sb.append(a);
					continue;
				}

				int idx = specialChar.indexOf(a);
				if (idx >= 0 && idx < specialStr.length) {
					sb.append(specialStr[idx]);
				} else {
					sb.append(a);
				}
			} else {
				sb.append("&#x");
				sb.append(Integer.toHexString((int) a));
				sb.append(";");
			}
		}
		return sb.toString();
	}

	public static String getMessage(String resourcesClass, Locale locale,
			String key) {
		return getMessage(resourcesClass, locale, key, (String) null);
	}

	public static String getMessage(String resourcesClass, Locale locale,
			String key, String defaultVal) {
		String message = "";
		try {
			// Load the resource bundle
			ResourceBundle bundle = ResourceBundle.getBundle(resourcesClass,
					locale);
			// Get the message template
			message = bundle.getString(key);
		} catch (Exception e) {
			message = "#" + key + "#";
			message = (defaultVal == null) ? message : defaultVal;
		}

		return (message == null) ? "" : message;
	}

	public static String getMessage(String resourcesClass, Locale locale,
			String key, String[] args) {
		String message = "";
		try {
			// Load the resource bundle
			ResourceBundle bundle = ResourceBundle.getBundle(resourcesClass,
					locale);

			// Get the message template
			message = bundle.getString(key);
		} catch (Exception e) {
			message = "#" + key + "#";
		}

		// Format the message using the message and the arguments
		String formatmsg = MessageFormat.format(message, (Object[])args);

		return (formatmsg == null) ? "" : formatmsg;
	}

	public static String getProperty(String resClass, String key) {
		String message = "";
		try {
			// Load the resource bundle
			ResourceBundle bundle = ResourceBundle.getBundle(resClass);

			// Get the message template
			message = bundle.getString(key);
		} catch (Exception e) {
			return "";
		}

		return message;
	}

	public static int getIntProperty(String resClass, String key, int defValue) {
		try {
			// Load the resource bundle
			ResourceBundle bundle = ResourceBundle.getBundle(resClass);

			// Get the message template
			String valStr = bundle.getString(key);

			try {
				return Integer.parseInt(valStr);
			} catch (NumberFormatException e) {
				return defValue;
			}
		} catch (Exception e) {
			return defValue;
		}
	}

	public static String toBig5String(String str) {
		byte[] bytes = new byte[0];

		try {
			bytes = str.getBytes("8859_1");
		} catch (UnsupportedEncodingException e) {
			return "";
		}

		try {
			return new String(bytes, "8859_1");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			return "";
		}
	}

	public static String getCurrentMonthStr(String format) {
		format = (format == null) ? "yyyyMM" : format;

		SimpleDateFormat f = new SimpleDateFormat(format);

		return f.format(new Date());
	}

	public static String getDateTimeStr(long time, String format) {
		SimpleDateFormat f = new SimpleDateFormat(format);

		return f.format(new Date(time));
	}

	public static String getDateTimeStr(long time) {
		String format = "yyyy-MM-dd HH:mm:ss";

		SimpleDateFormat f = new SimpleDateFormat(format);

		return f.format(new Date(time));
	}

	public static byte[] stringToUTFByte(String str) {
		try {
			return str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new byte[0];
		}
	}

	public static byte[] stringToUTF(String str) {
		// defence for null pointer
		if (str == null || "".equals(str.trim())) {
			return null;
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		byte[] array = null;

		try {
			dos.writeUTF(str);
			array = baos.toByteArray();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (dos != null) {
				try {
					dos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return array;
	}

	/**
	 * convert byte array to hex string, negative safe after 2005-06-15, null
	 * safe
	 */
	public static String byteToHex(byte pixel[]) {
		if (pixel == null) {
			return ""; // defensive
		}

		String result = "";
		// print the array
		for (int i = 0; i < pixel.length; i++) {
			int value;
			value = pixel[i];

			// this is the key point:
			if (value < 0) {
				value += 256;
			}

			String str = Integer.toHexString(value).toUpperCase();
			if (str.length() < 2) {
				str = "0" + str;
			}

			result += str;
		}

		return result;
	}

	public static String byteToHex(byte pixel[], String sep) {
		if (pixel == null) {
			return ""; // defensive
		}

		String result = "";
		// print the array
		for (int i = 0; i < pixel.length; i++) {
			int value;
			value = pixel[i];

			// this is the key point:
			if (value < 0) {
				value += 256;
			}

			String str = Integer.toHexString(value).toUpperCase();
			if (str.length() < 2) {
				str = "0" + str;
			}

			if (result.length() > 0) {
				result += sep + str;
			} else {
				result += str;
			}
		}

		return result;
	}

	public static String ipIntToStr(int val) {
		int seg0 = val & 0x000000ff;
		int seg1 = (val >> 8) & 0x000000ff;
		int seg2 = (val >> 16) & 0x000000ff;
		int seg3 = (val >> 24) & 0x000000ff;

		return "" + seg3 + "." + seg2 + "." + seg1 + "." + seg0;
	}

	public static int ipStrToInt(String str) {
		String[] token = str.split("\\.");

		if (token.length < 4) {
			// System.out.println("DEBUG!!! Invalid IP Token: " + str);
			return 0;
		}

		int seg0 = intValue(token[0], 0) & 0x000000ff;
		int seg1 = intValue(token[1], 0) & 0x000000ff;
		int seg2 = intValue(token[2], 0) & 0x000000ff;
		int seg3 = intValue(token[3], 0) & 0x000000ff;

		int retval = (seg0 << 24) | (seg1 << 16) | (seg2 << 8) | seg3;

		// System.out.println("IP: " + str + " >> " + retval);

		return retval;
	}

	/**
	 * Convert a UTF to String
	 * 
	 * @param inbytes
	 *            UTF to be converted
	 * @return a String representing the UTF
	 */
	public static String UTFToString(byte[] inbytes) {
		if (inbytes == null)
			return null;
		ByteArrayInputStream bais = new ByteArrayInputStream(inbytes);
		DataInputStream dis = new DataInputStream(bais);
		String retStr = null;

		try {
			retStr = dis.readUTF();
		} catch (Exception ex) {
		} finally {
			try {
				dis.close();
				dis = null;
			} catch (Exception ex) {
			}
			try {
				bais.close();
				bais = null;
			} catch (Exception ex) {
			}
		}
		return retStr;
	}

	/**
	 * Converts the input hex string to binary. Each character in return string
	 * represents to hexadecimal number in input string.
	 * 
	 * @param src
	 *            the string to be converted
	 */
	public static byte[] hexToByte(String src) {
		if (src == null) {
			return new byte[0];
		}

		int strLen = src.length();

		if (strLen % 2 == 1) { // Odd Case
			strLen += 1;
			src += "0";
		}

		int byteSize = strLen / 2;

		byte[] b = new byte[byteSize];

		int offset = 0;
		for (int i = 0; i < byteSize; i++, offset += 2) {
			b[i] = (byte) Integer.parseInt(src.substring(offset, offset + 2),
					16);
		}

		return b;
	}

	public static String hashToString(HashMap<?, ?> hash) {
		StringBuffer sb = new StringBuffer();

		if (hash == null) {
			return "null hash";
		}

		Iterator<?> it = hash.keySet().iterator();

		while (it.hasNext()) {
			String key = (String) it.next();
			sb.append(key);
			sb.append("\t");

			Object obj = hash.get(key);
			if (obj instanceof Integer) {
				Integer intObj = (Integer) obj;
				sb.append("" + intObj.intValue());
			} else {
				sb.append(obj.toString());
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	public static String hashToString(Hashtable<?, ?> hash) {
		StringBuffer sb = new StringBuffer();

		if (hash == null) {
			return "null hash";
		}

		Enumeration<?> en = hash.keys();

		while (en.hasMoreElements()) {
			Object key = (Object) en.nextElement();
			if (key instanceof Integer) {
				sb.append(((Integer) key).intValue());
			} else {
				sb.append(key);
			}
			sb.append("\t");

			Object obj = hash.get(key);
			if (obj instanceof Integer) {
				Integer intObj = (Integer) obj;
				sb.append("" + intObj.intValue());
			} else {
				sb.append(obj.toString());
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	public static String utfByteToString(byte[] inbytes) {
		if (inbytes == null)
			return null;
		ByteArrayInputStream bais = new ByteArrayInputStream(inbytes);
		DataInputStream dis = new DataInputStream(bais);
		String retStr = null;

		try {
			retStr = dis.readUTF();
		} catch (Exception ex) {
		} finally {
			try {
				dis.close();
				dis = null;
			} catch (Exception ex) {
			}
			try {
				bais.close();
				bais = null;
			} catch (Exception ex) {
			}
		}
		return retStr;
	}

	/**
	 * Convert byte data to UTF string similar to din =
	 * ByteArrayInputStream(data); return din.readUTF();
	 * 
	 * @param data
	 * @return
	 */
	public static String byteToString(byte[] data) {
		return byteToString(data, "utf-8");
	}

	public static String byteToString(byte[] data, String encoding) {
		try {
			return new String(data, encoding);
		} catch (UnsupportedEncodingException e) {
			return new String(data);
		}
	}

	/**
	 * @param cmd
	 * @param string
	 * @return
	 */
	public static String[] specialSplit(String cmd, String sep, int nTokens) {
		ArrayList<String> list = new ArrayList<String>();

		String[] tmp = cmd.split(sep);
		for (int i = 0; i < tmp.length; i++) {
			String str = tmp[i].trim();
			if (str.length() == 0) {
				continue;
			}

			list.add(str);
		}

		for (int i = tmp.length; i <= nTokens; i++) {
			list.add("");
		}

		String[] splited = new String[list.size()];
		list.toArray(splited);

		return splited;
	}

	/**
	 * Convert decode the UTF string which are encoding by hexstring Example:
	 * 64656667 => "ABCD"
	 * 
	 * @return decoded UTF string
	 */
	public static String decodeUTFHexString(String hexStr) {

		// Getting the msg from the data (encoded in UTF)
		byte[] bytedata = StringUtils.hexToByte(hexStr);
		String msg = "";

		// Getting the message
		// Quick exist if there is not softsync upward (client->server) data??
		DataInputStream din = new DataInputStream(new ByteArrayInputStream(
				bytedata));
		try {
			return din.readUTF();
		} catch (IOException e) {
			return "";
		} finally {
			try {
				din.close();
			} catch (Exception e1) {
			}
		}
	}

	public static int[] strArrayToInt(String str, String sep) {
		String[] tmp = str.split(sep);
		int[] tmpArr = new int[tmp.length];

		int numInt = 0;
		for (int i = 0; i < tmp.length; i++) {
			String s = tmp[i].trim();
			try {
				int intVal = Integer.parseInt(s);
				tmpArr[numInt] = intVal;
				numInt++;
			} catch (NumberFormatException e) {
				continue;
			}
		}

		int[] retArr = new int[numInt];
		for (int i = 0; i < numInt; i++) {
			retArr[i] = tmpArr[i];
		}

		return retArr;
	}

	public static String intArrayToString(int[] arr, String sep) {
		if (arr == null) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			if (i != 0) {
				sb.append(sep);
			}
			sb.append(String.valueOf(arr[i]));
		}

		return sb.toString();
	}

	/**
	 * @param arg
	 * @param defaultVal
	 *            default value
	 * @return
	 */
	public static int intValueFromBinary(String arg, int defaultVal) {
		try {
			return Integer.parseInt(arg, 2);
		} catch (NumberFormatException e) {
			return defaultVal;
		}
	}

	public static int countWord(String name) {
		boolean chi = false;
		boolean after = true;
		// after=true: means the last word is completed (just after
		// total_word++)
		int total_word = 0;

		if (name == null)
			return 0; // for safety

		name = name.trim();

		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) >= 128 && !chi) {
				// this character is the first byte of chinese
				// some chinese character, it's second byte is also
				// >= 128, therefore use !chi to filter it
				chi = true;
				after = false;
			} else if (chi == true) {
				// this character is the second byte of chinese
				total_word++;
				chi = false;
				after = true;
			} else if (after == true && name.charAt(i) == ' ') {
				// here: chi always false
				continue;
			} else if (after == true && name.charAt(i) != ' ') {
				after = false;
			} else if (name.charAt(i) == ' ') {
				total_word++;
				after = true;
			}
		}
		if (after == false)
			total_word++;
		return total_word;
	}

	/*
	 * Dump the result to String, this will be a non-formated string. e.g.
	 * (char)EA (char)61 (char)06 => "EA6106"
	 */

	public static String dump2String(char data[], int size) {
		if (data.length < size)
			return "ERROR";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < size; i++) {
			String h = Integer.toHexString(data[i]).toUpperCase();
			if (h.length() == 1) {
				sb.append("0" + h);
			} else {
				sb.append(h);
			}
		}
		return sb.toString();
	}

	public static String dump2String(char data[]) {
		return dump2String(data, data.length);
	}

	public static String dump2String(byte data[]) {
		return dump2String(data, data.length);
	}

	public static String dump2String(byte data[], int size) {
		if (data.length < size)
			return "ERROR";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < size; i++) {
			String h = Integer.toHexString(unsign(data[i])).toUpperCase();
			if (h.length() == 1) {
				sb.append("0" + h);
			} else {
				sb.append(h);
			}
		}
		return sb.toString();
	}

	/*
	 * pad zero to the string until the length of string = total
	 */

	public static String padZero(String src, int total) {
		String longZero = "0000000000000000"; // 16 0s

		src = longZero + src;
		if (src.length() >= total) {
			return src.substring(src.length() - total);
		} else {
			while (src.length() < total) {
				src = longZero + src;
			}
			return src.substring(src.length() - total);
		}

	}

	/*
	 * count the number of words in a string One English word (e.g. "apple") is
	 * counted as 1 word. One chinese character (double byte) is counted as 1
	 * word. e.g. "�� ABCD EFG" -> 3 " �H��7��" -> 4 "23B is a bus to sham shui
	 * bo" -> 8
	 */
	public static int unsign(byte in) {
		return (in & 0xff);
	}

	public static void main(String[] args) {

		// String inputStr = "小猪"; // java unicode
		String inputStr = "tester1";
		byte[] data = StringUtils.stringToUTFByte(inputStr);
		String inputHexStr = StringUtils.byteToHex(data);

		System.out.println(inputStr + " inputHexStr=" + inputHexStr);

		// inputHexStr = "E5B08FE78CAA";
		inputHexStr = "74657374657231";
		data = StringUtils.hexToByte(inputHexStr.trim());
		String outputStr = StringUtils.byteToString(data, "utf-8");

		System.out.println(outputStr);

	}

	/*
	 * A String isEmpty means it is null or it is ""
	 */
	public static boolean isEmpty(String src) {
		if (src == null || "".equals(src))
			return true;
		else
			return false;
	}

	/*
	 * The date should in yyyy-mm-dd format
	 */
	public static boolean isDate(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			ParsePosition pos = new ParsePosition(0);
			Date out = format.parse(date, pos);
			if (out == null)
				return false;
			else
				return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static Date stringToDate(String date) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			ParsePosition pos = new ParsePosition(0);
			Date out = format.parse(date, pos);
			if (out == null)
				return null;
			else
				return out;
		} catch (Exception e) {
			return null;
		}
	}
}
