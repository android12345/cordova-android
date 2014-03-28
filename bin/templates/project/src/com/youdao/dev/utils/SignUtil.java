package com.youdao.dev.utils;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.net.Uri;
import android.util.Log;

public class SignUtil {	

	/**
	 * 得到签名值 
	 * @param key         签名key
	 * @param reqParams   以key1=value1&key2=value2& ... 拼接后的参数串，可不排序
	 * @return            签名值
	 */
	public static String getSign(String key, String reqParams){
		
		HashMap<String, String> map =  getMd5Map(reqParams);
		String req  = sortConstructVars(map);		
		String sign = getMD5(req + "&key=" + key);
		
		return sign;
	}	
	
	private static HashMap<String, String> getMd5Map(String params) {
		HashMap<String, String> map = new HashMap<String, String>();
		String[] s_map = params.split("&");
		for (int i = 0; i < s_map.length; i++) {
			int index = s_map[i].indexOf("=");
			if (index > 0 && index < s_map[i].length() - 1) {
				map.put(s_map[i].substring(0, index),
						Uri.decode(s_map[i].substring(index + 1)));
			}
		}
		return map;
	}
	
	private static String sortConstructVars(HashMap<String, String> orig_map) {
		String s = new String();
		Map.Entry entry;
		Iterator itr;
		boolean first_param;
		List list = new LinkedList(orig_map.entrySet());
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getKey().toString())
						.compareTo(((Map.Entry) (o2)).getKey().toString());
			}
		});

		first_param = true;

		for (itr = list.iterator(); itr.hasNext();) {
			entry = (Map.Entry) itr.next();
			if ((entry.getKey().toString().length() > 0)
					&& (entry.getValue().toString().length() > 0)) {
				if (first_param) {
					s = entry.getKey().toString();
					s += "=";
					s += entry.getValue().toString();
					first_param = false;
				} else {
					s += "&";
					s += entry.getKey().toString();
					s += "=";
					s += entry.getValue().toString();
				}
			}

		}

		return s;
	}
	
	private static String getMD5(String string) {
		String s = null;
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(string.getBytes("utf-8"));
			byte tmp[] = md.digest();
			char str[] = new char[16 * 2];
			int k = 0;
			for (int i = 0; i < 16; i++) {
				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			s = new String(str);
		} catch (Exception e) {
		}
		return s;
	}
	
}