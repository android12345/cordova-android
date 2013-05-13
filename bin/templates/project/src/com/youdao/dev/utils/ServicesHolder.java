package com.youdao.dev.utils;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

/*
 * 服务器api地址的管理类
 * 根据运行的环境和模块，返回不同的api地址
 * http://api.ent.appmars.com  /v1    /tinyurl/build?url=$&level=1-5/5
 * 
 * http://api1.ent.appmars.com
 * http://api2.ent.appmars.com
 * http://api.ent.appmars.com
 * 
 * http://dev.ent.appmars.com
 * 
 * http://test.ent.appmars.com
 * 
 * 
 */

public class ServicesHolder {
//	public static final int FRIEND_TIME_ONLINE = 1;
//	public static final int USERS_SHOW = 2;
//	public static final int DOMAIN_SHOW = 3;
	public static final int APP_ADD = 1 ;
	public static final int GETAPP_INFO = 2 ;

	public static int DEVELOPMENT = 0;
	public static int PRODUCTION = 1;
	public static int TEST = 2;
	static int environment = 1;
	public static Map<Integer, String> env;
	public static Map<Integer, String> mapmodule = null;
	static {

		env = new HashMap<Integer, String>();
		//env.put(DEVELOPMENT, "https://api.weibo.com/2");
		//env.put(PRODUCTION, "https://api.weibo.com/2");
		env.put(DEVELOPMENT, "http://cloud.appmars.com") ;
		env.put(PRODUCTION, "http://cloud.appmars.com") ;
		env.put(TEST, "http://test.ent.appmars.com");
		

		mapmodule = new HashMap<Integer, String>();
//		mapmodule.put(FRIEND_TIME_ONLINE, "/statuses/friends_timeline.json");
//		mapmodule.put(USERS_SHOW, "/users/show.json");
//		mapmodule.put(DOMAIN_SHOW, "/users/domain_show.json");
		
		mapmodule.put(APP_ADD, "/cloud/1/app_add") ;
		mapmodule.put(GETAPP_INFO, "/cloud/1/app_info_get") ;
		

	}

	/*
	 * environment决定返回那个服务器 module决定返回那个模块
	 */
	public static String api(int module) {
		String path;
		path = (String) env.get(environment) + (String) mapmodule.get(module);
		Log.d("path", path);
		return path;
	}

//	public static String push() {
//		if (environment == PRODUCTION) {
//			return "http://push.appmars.com/sub?id=1";
//		} else {
//			return "http://dev.push.appmars.com/sub?id=1";
//		}
//
//	}

	public static void setEnvironment(int env) {
		environment = env;
	}
}
