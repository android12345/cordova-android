package com.youdao.dev.plugin;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.youdao.dev.JpushManager;
import com.youdao.dev.R;
import com.youdao.dev.utils.DeviceUtils;

/**
 * 得到用户uid，及设备信息，并上传服务器的插件　
 * 
 * @author JunJun
 * 
 */
public class GetApplicationInfo extends CordovaPlugin {

	private static final String GETINFO = "getApplicationInfo";


	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		final String uid = args.getString(0);

		if (action.equals(GETINFO)) {

			if (!("".equals(uid)) || uid != null) {
				postDatatoServer(uid);
				return true;
			}
		}

		return false;
	}

	/**
	 * 取得数据并上传到服务器
	 * 
	 * @param uid
	 */
	private void postDatatoServer(String uid) {
		
		final String uuid = DeviceUtils.getUUID(cordova.getActivity());
		final String app_id = cordova.getActivity().getResources()
				.getString(R.string.app_id);

		
		
		JpushManager.getInstance().sendUid(cordova.getActivity(), uid, uuid,app_id, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject arg0) {
				super.onSuccess(arg0);
				//Log.d("******************************:", arg0.toString()) ;
			}
		}) ;



	}
}
