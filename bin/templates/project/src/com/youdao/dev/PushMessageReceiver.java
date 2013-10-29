package com.youdao.dev;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.baidu.android.pushservice.PushConstants;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.youdao.dev.utils.CommUtils;
import com.youdao.dev.utils.DeviceUtils;

/**
 * 推送过来的消息处理广播 
 * @author fengxue
 *
 */

public class PushMessageReceiver extends BroadcastReceiver{

	private static final String TAG = "PushMessageReceiver";
	
	private SharedPreferences preferences ;
	
	@Override
	public void onReceive(Context context, Intent intent) {
	
		preferences = context.getSharedPreferences("push_user", context.MODE_PRIVATE) ;
		
		
		if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) { //这里是获取消息
			//获取消息内容
			String message = intent.getExtras().getString(
					PushConstants.EXTRA_PUSH_MESSAGE_STRING);

		} else if (intent.getAction().equals(PushConstants.ACTION_RECEIVE)) {
			//处理绑定等方法的返回数据
			//PushManager.startWork()的返回值通过PushConstants.METHOD_BIND得到
			//获取方法
			final String method = intent
					.getStringExtra(PushConstants.EXTRA_METHOD);
			//方法返回错误码。若绑定返回错误（非0），则应用将不能正常接收消息。
			//绑定失败的原因有多种，如网络原因，或access token过期。
			//请不要在出错时进行简单的startWork调用，这有可能导致死循环。
			//可以通过限制重试次数，或者在其他时机重新调用来解决。
			
			final int errorCode = intent
					.getIntExtra(PushConstants.EXTRA_ERROR_CODE,
							PushConstants.ERROR_SUCCESS);
			//返回内容  {"response_params":{"appid":"1617351","channel_id":"4115602416134181365","user_id":"850061862126314360"},"request_id":2028010507}


			final String content = new String(
					intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT));
			
			
			//把返回的json信息解析出来。
			JSONObject jsonObject = null ;
			try {
				 jsonObject = new JSONObject(content) ;
				 JSONObject json = jsonObject.optJSONObject("response_params") ;
				 String userid = json.optString("user_id") ;
					
					  
				if(userid!=null){
				 Editor editor = preferences.edit() ;
				 editor.putString("user_id", userid) ;
				 editor.commit() ;
				}
				 
				 final String app_id = context.getResources().getString(
							R.string.app_id);
					BaidupushManager.getInstance().baiduPushSendData(context, app_id,
							DeviceUtils.getUUID(context), "",
							CommUtils.getVersionCode(context),
							CommUtils.getAndroidSDKVersion(),
							"",
							 "",
							CommUtils.getProvidersName(context),
							CommUtils.getPhoneBrand(), userid,
							new JsonHttpResponseHandler() {
								@Override
								public void onSuccess(JSONObject arg0) {
									super.onSuccess(arg0);
								}
								@Override
								public void onFailure(Throwable arg0, String arg1) {
									// TODO Auto-generated method stub
									super.onFailure(arg0, arg1);
								}
							});
				 
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Log.d(TAG, "onMessage: content: " + content);
			
		//可选。通知用户点击事件处理
		} else if (intent.getAction().equals(
				PushConstants.ACTION_RECEIVER_NOTIFICATION_CLICK)) {
			Log.d(TAG, "intent=" + intent.toUri(0));
			
			Intent aIntent = new Intent();
			aIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			aIntent.setClass(context, DevActivity.class);
			
			String content = intent
					.getStringExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT);
			
			String str = intent.getStringExtra(PushConstants.EXTRA_EXTRA) ;
					
			/**
			 * str里面的数据   服务器返回的 
			 * {"data":[{"createtime":"2013-09-03 17:12:58","userid":"1","id":
			 * "18","winning":"2","code":"732","eventid":"1"},{"createtime":
			 * "2013-09-02 14:44:17"
			 * ,"userid":"1","id":"10","winning":"2","code":
			 * "456","eventid":"1"}]}
			 * 
			 * 
			 * 
			 * intent.putExtra("ticket_code", user.getUserid()
							+ baEvents.getBaEvents().get(0).getId()
							+ ticketsInfo.getCode());
							
			{"data":[{"createtime":"2013-10-17 11:35:00","userid":"266481","id":"2930","winning":"2","code":"804","eventid":"1"}]}

			 */
			//保存服务端推送下来的附加字段。这是个 JSON 字符串。
			//String extra = intent.getStringExtra(PushConstants.EXTRA_EXTRA) ;
			
			JSONObject jsonObject ;
			
			String urivaules = null ;
			
			try {
				jsonObject = new JSONObject(str) ;
				urivaules = jsonObject.optString("uri") ;
				aIntent.putExtra("uri", urivaules) ;
				context.startActivity(aIntent);
				
				
				Log.d("======================uri", urivaules) ;
						
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			
		//	Log.d("********************", str.toString()+"intent:"+intent.getSerializableExtra("yaobaevent")) ;
		//	aIntent.putExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT, content);
			
			
			

		}
		
	}


}
