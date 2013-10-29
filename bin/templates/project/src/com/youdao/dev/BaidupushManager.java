package com.youdao.dev;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.youdao.dev.utils.HttpClientUtils;
import com.youdao.dev.utils.ServicesHolder;

/**
 * 向服务器发送极光有关信息的api  用的是百度云推送
 *
 */
public class BaidupushManager {

	private static BaidupushManager instance;
	
	private BaidupushManager(){} ;
	public static BaidupushManager getInstance() {
		if (instance == null)
			instance = new BaidupushManager();
		return instance;
	}
	
	/**
	 * 
	 * @param context
	 * @param appid 应用ID
	 * @param uuid    手机的唯一标识 
	 * @param uid　用户ID
	 * @param appversion 应用版本
	 * @param sdkversion　SDK版本　
	 * @param latitude　纬度
	 * @param longitude 精度
	 * @param operators 运营间
	 * @param phonebrands　手机品牌
	 * @param bd_udid　这里换成百度的返回的userid 
	 * @param handler
	 */
	public void baiduPushSendData(Context context, String appid, String uuid,String uid ,String appversion,String sdkversion,String latitude,String longitude,String operators,String  phonebrands,String bd_udid,
			AsyncHttpResponseHandler handler) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("caid", appid) ;
		params.put("udid", uuid) ;
		params.put("cuid", uid) ;
		params.put("versions", appversion) ;
		params.put("sys_versions", sdkversion) ;
		params.put("latitude", latitude) ;
		params.put("longitude", longitude) ;
		params.put("operator", operators) ;
		params.put("brand", phonebrands) ;
		params.put("bd_udid", bd_udid) ;
		
		HttpClientUtils.post(ServicesHolder.api(ServicesHolder.PUSH_ANDROID_ADD), params, context, handler) ;

	}
	
	/**
	 * 给服务器传用户id
	 * @param context
	 * @param cuid
	 * @param uuid 手机的唯一标识
	 * @param appid 应用ID
	 * @param handler
	 */
	public void sendUid(Context context ,String cuid,String uuid,String appid ,AsyncHttpResponseHandler handler){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cuid", cuid) ;
		params.put("udid", uuid) ;
		params.put("caid", appid) ;
		HttpClientUtils.post(ServicesHolder.api(ServicesHolder.PUSH_ANDROID_ADD), params, context, handler) ;
	}
}
