package com.youdao.dev.plugin;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.widget.Toast;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

public class CheckVersion extends CordovaPlugin {
	
	private final static String UPDATE = "checkVersion" ;

	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		if(UPDATE.equals(action)){
			
			
			update(callbackContext) ;
			return true ;
		}
		return false ;
	}

	private void update(final CallbackContext callbackContext) {
		
		UmengUpdateAgent.update(this.cordova.getActivity());
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus,
					UpdateResponse updateInfo) {
				
				switch (updateStatus) {
				case 0: // has update
					UmengUpdateAgent.showUpdateDialog(cordova.getActivity(),
							updateInfo);
					break;
				case 1: // has no update
					Toast.makeText(cordova.getActivity(), "没有新版本",
							Toast.LENGTH_SHORT).show();
					callbackContext.success("没有新版本") ;
					break;
				case 2: // none wifi
					Toast.makeText(cordova.getActivity(),
							"没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT)
							.show();
					callbackContext.success("没有wifi连接， 只在wifi下更新") ;
					break;
				case 3: // time out
					Toast.makeText(cordova.getActivity(), "超时",
							Toast.LENGTH_SHORT).show();
					callbackContext.success("超时") ;
					break;
				}
			}
		});
	}
}
