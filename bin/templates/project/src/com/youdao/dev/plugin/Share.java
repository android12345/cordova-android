package com.youdao.dev.plugin;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import com.youdao.dev.domain.ShareInfo;
import com.youdao.dev.utils.ShareUtil;

/**
 * @author junjun 分享功能的插件
 */
public class Share extends CordovaPlugin {

	public final String SHARE = "share";
	ShareUtil shareUtil =  null;

	// private static String uMengID ;

	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {

		final ShareInfo shareInfo = ShareInfo.parse(args);

		if (SHARE.equals(action)) {
			try {

				Runnable runnable = new Runnable() {

					@Override
					public void run() {
						if (shareUtil == null){
							shareUtil = new ShareUtil();
						}
						shareUtil.share(cordova.getActivity(), shareInfo);
					}
				};
				cordova.getActivity().runOnUiThread(runnable); // 在UI线程运行

			} catch (Exception e) {
				e.printStackTrace();
			}

			// Log.d(TAG, "projectName is :"+projectName) ;
			callbackContext.success("准备开始分享!");
			return true;
		}
		return false;
	}
}
