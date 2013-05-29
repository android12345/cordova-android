package com.youdao.dev.plugin;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;


import com.loopj.android.http.JsonHttpResponseHandler;
import com.youdao.dev.JpushManager;
import com.youdao.dev.LocationProvider;
import com.youdao.dev.R;
import com.youdao.dev.domain.LocationBean;
import com.youdao.dev.utils.CommUtils;
import com.youdao.dev.utils.DeviceUtils;


/**
 * 得到用户uid，及设备信息，并上传服务器的插件　
 * @author JunJun
 *
 */
public class GetApplicationInfo extends CordovaPlugin {
	
	
	private static final String GETINFO = "getApplicationInfo" ;
	
	private LocationProvider provider = null ;
	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		final String uid = args.getString(0) ;
		
		if(action.equals(GETINFO)){
			
			if(!("".equals(uid)) || uid != null){
				postDatatoServer(uid) ;
				return true ;
			}
		}
		
		
		return false ;
	}
	/**
	 * 取得数据并上传到服务器
	 * @param uid
	 */
	private void postDatatoServer(String uid) {
		provider = new LocationProvider(cordova.getActivity()) ;
		LocationBean station = provider.getLocation() ;
		
		final String uuid = DeviceUtils.getUUID(cordova.getActivity()) ;
		final String appversion = CommUtils.getVersionCode(cordova.getActivity()) ;
		final String sdkversion = CommUtils.getAndroidSDKVersion() ;
		
		
		final String operators = CommUtils.getProvidersName(cordova.getActivity()) ;
		final String phonebrands = CommUtils.getPhoneBrand() ;
		final String latitude = station.getLatitude() ;
		final String longitude = station.getLongitude() ;
		final String app_id = cordova.getActivity().getResources().getString(R.string.appid) ;
		
		JpushManager.getInstance().jpushSendData(cordova.getActivity(), app_id, uuid, uid, appversion, sdkversion, latitude, longitude, operators, phonebrands, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray object) {
				super.onSuccess(object);
				
			}
		}) ;

	}
}
