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

	// private static String uMengID ;
	private ShareUtil shareUtil = null;

	@Override
	public boolean execute(String action, JSONArray args,
			final CallbackContext callbackContext) throws JSONException {

		final ShareInfo shareInfo = ShareInfo.parse(args);

		if (SHARE.equals(action)) {
			try {

				Runnable runnable = new Runnable() {

					@Override
					public void run() {
						if (shareUtil == null)
							shareUtil = new ShareUtil(callbackContext);
					
						shareUtil.share(cordova.getActivity(), shareInfo,callbackContext);
						// 清理自定义平台的数据
						shareUtil.clearCustomPlatforms() ;
				
					}
				};
				cordova.getActivity().runOnUiThread(runnable); // 在UI线程运行

			} catch (Exception e) {
				e.printStackTrace();
			}

			return true;
		}
		return false;
	}
}
