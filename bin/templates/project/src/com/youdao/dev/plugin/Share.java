
package com.youdao.dev.plugin;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import com.youdao.dev.domain.ShareInfo;
import com.youdao.dev.utils.ShareUtil;


/**
 * @author junjun
 * 分享功能的插件
 */
public class Share extends CordovaPlugin {
	
	public static final String REGISTERUM = "registerUmeng" ; 
	public static final String REGISTERWX = "registerWeixin" ;
	public static final String SHARE = "share" ;
	
	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		
		ShareInfo shareInfo = ShareInfo.parse(args) ;
		
		if(REGISTERWX.equals(action)){//如果是注册微信的事件
			String wxAppID = args.getString(0) ;  //取得js传过来的微信的id
			registerWx(wxAppID,callbackContext) ;
			return true ;
		}else if(SHARE.equals(action)){
			ShareInfo(shareInfo,callbackContext) ;
			return true ;
		}else if(REGISTERUM.equals(action)){
			
		}
		return false ;
	}

	/**
	 * @param shareInfo 要分享的对象 
	 * @param callbackContext   回调
	 */
	private void ShareInfo(final ShareInfo shareInfo, CallbackContext callbackContext) {
		try {
//			//以下就是获得mainfext.xml里面包的主Activity的名称		
//			 PackageManager pm = this.cordova.getActivity().getPackageManager(); // 获得PackageManager对象  
//				PackageInfo info = pm.getPackageInfo(this.cordova.getActivity().getPackageName(), 0);
//				String pkg = info.packageName ;
//			//	 Toast.makeText(this.cordova.getActivity(), pkg+"", 1).show() ;
//				   Intent it = new Intent(Intent.ACTION_MAIN,null);
//		           it.setPackage(pkg);//pkg为包名
//		           it.addCategory(Intent.CATEGORY_LAUNCHER);
//		           ComponentName ac = it.resolveActivity(pm);//mPackageManager为PackageManager实例
//		           	//得到主Activity的名字
//		           String mainActivityName =ac.getClassName() ;
//		     //   Toast.makeText(this.cordova.getActivity(), classname+"", 1).show() ;
//			
//			//用反射调用
//			Class c = Class.forName(mainActivityName);
//			Method m = c.getMethod("ShareInfo", ShareInfo.class);
//			m.invoke(c,shareInfo);

			Runnable runnable = new Runnable() {
				
				@Override
				public void run() {
					ShareUtil shareUtil = new ShareUtil() ;
					shareUtil.share(cordova.getActivity(),shareInfo.getShareText(), shareInfo.getShareImageUrl()) ;
				}
			};
			cordova.getActivity().runOnUiThread(runnable);  //在UI线程运行
			
		} catch (Exception e) {
			e.printStackTrace() ;
		}
	
		//Log.d(TAG, "projectName is :"+projectName) ;
		callbackContext.success("准备开始分享!") ;
	}

	/**
	 * @param wxAppID   注册微信的appID
	 * @param callbackContext
	 */
	private void registerWx(String wxAppID, CallbackContext callbackContext) {
		if(wxAppID==null || wxAppID.equals("")){ //如果key不为空的话那就注册微信
			callbackContext.error("必须传入微信的appkey") ;
		}else{
			ShareUtil.createWXAPI(cordova.getActivity(), wxAppID) ;
			callbackContext.success("注册微信成功") ;
			
		}
	}

	
}
