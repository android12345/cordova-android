package com.youdao.dev.utils;


import android.util.Log;
import android.util.SparseArray;

import com.youdao.dev.BuildConfig;

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

	public static final int APP_ADD = 1;
	public static final int GETAPP_INFO = 2;
	public static final int PUSH_ANDROID_ADD = 3;

	public static int DEBUGE = 0;
	public static int RELEASE = 1;
	public static int TEST = 2;
	static int environment = 1;
	public static SparseArray<String> env;
	public static SparseArray<String> mapmodule = null;

	static {
		
		env = new SparseArray<String>();

		//env.put(DEVELOPMENT, "http://cloud.appmars.com");
	//	env.put(DEBUGE, "http://cloud.appmars.com");
		env.put(DEBUGE, "http://115.28.36.217");
		env.put(RELEASE, "http://115.28.36.217") ;
		// env.put(TEST, "http://test.ent.appmars.com");

		mapmodule = new SparseArray<String>();

		mapmodule.put(APP_ADD, "/cloud/1/app_add");
		mapmodule.put(GETAPP_INFO, "/cloud/1/app_info_get");
		mapmodule.put(PUSH_ANDROID_ADD, "/cloud/1/push_android_add");

	}

	/*
	 * environment决定返回那个服务器 module决定返回那个模块
	 */
	public static String api(int module) {
		String path;
	//	Log.d("BuildConfig.DEBUG", BuildConfig.DEBUG+"");
		if(BuildConfig.DEBUG){
			path = env.get(DEBUGE) + mapmodule.get(module);	
	//		Log.d("DEBUGE path", path);
		}else{
			path = env.get(RELEASE) + mapmodule.get(module);
	//		Log.d(" RELEASE path", path);
		}
		//Toast.makeText(context, "ddd:"+path, 0).show() ;
	//	Log.d("path", path);
		return path;
	}

	// public static String push() {
	// if (environment == PRODUCTION) {
	// return "http://push.appmars.com/sub?id=1";
	// } else {
	// return "http://dev.push.appmars.com/sub?id=1";
	// }
	//
	// }

	public static void setEnvironment(int env) {
		environment = env;
	}
}
