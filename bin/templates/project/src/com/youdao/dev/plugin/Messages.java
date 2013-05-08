package com.youdao.dev.plugin;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youdao.dev.R;


/**
 * 显示消息的Toast插件
 * @author fengxue
 *
 */
public class Messages extends CordovaPlugin {
	
	private final static String SHOWTOAST = "showMsg" ;
	
	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		//取得js传过来的消息内容
		final String toastContent = args.getString(0) ;
		if(SHOWTOAST.equals(action)){
			showToast(callbackContext,toastContent) ;
			return true ;
		}
		return false; 
	}
	/**
	 * 显示提醒内容的方法
	 * @param callbackContext
	 */
	private void showToast(CallbackContext callbackContext,String toastContent) {
		if(!("".equals(toastContent))){
			LayoutInflater inflater = this.cordova.getActivity()
					.getLayoutInflater();
			// 根据指定的布局文件创建一个具有层级关系的View对象
			View layout = inflater.inflate(R.layout.toast, null);
			LinearLayout root = (LinearLayout) layout
					.findViewById(R.id.toast_layout_root);
			root.getBackground().setAlpha(120);// 0~255透明度值
			
			TextView text = (TextView) layout.findViewById(R.id.text);
			text.setText(toastContent);

			Toast toast = new Toast(this.cordova.getActivity()
					.getApplicationContext());
			// 设置Toast的位置
			// toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			// toast.setGravity(Gravity.BOTTOM, 0, 0);
			toast.setDuration(Toast.LENGTH_LONG);
			// 让Toast显示为我们自定义的样子
			toast.setView(layout);
			toast.show();
		}else{
			callbackContext.error("清输入要显示的信息内容!") ;
		}
	}
}
