package com.youdao.dev.plugin;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import com.youdao.dev.utils.DeviceUtils;

/**
 * 
 * @author junjun
 * 获取设备的UUID
 *
 */
public class GetUUID extends CordovaPlugin {
	
	public static final String GETUUID = "getUUID" ;

	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		
		if(GETUUID.equals(action)){
			getUUID(callbackContext) ;
			return true ;
		}
		
		return false ;
	}

	private void getUUID(CallbackContext callbackContext) {
		
		String uuid = DeviceUtils.getUUID(this.cordova.getActivity()) ;
		if(uuid !=null){
			callbackContext.success("设备的UUID是："+uuid) ;
		}else{
			callbackContext.error("获取设备UUID失败!") ;
		}
		
	}
}
