
package com.youdao.dev.plugin;

import java.util.Map;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.youdao.dev.domain.AlarmOptions;

/**
 * @author junjun
 *	生日提醒的PhoneGap插件
 */
public class AlarmClock extends CordovaPlugin {
	
	private AlarmHelper alarm = null;
	
	public static final String PLUGIN_NAME = "LocalNotification";

	private static final String TAG = "AlarmClock";
	
	private boolean flag = false ;  //标记是增加闹钟，还是修改闹钟
	
	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		
		alarm = new AlarmHelper(this.cordova.getActivity());
		AlarmOptions alarmOptions = AlarmOptions.ParseJsonArray(args) ;
		
		String id = alarmOptions.getNotificataionID() ;     //取得提醒ID
		String content = alarmOptions.getAlarmContent() ;   //取得提醒内容
		String date = alarmOptions.getDate() ;              //取得提醒执行时间戳
		String title = alarmOptions.getAlarmTitle() ;       //取得通知标题
		String interVal = alarmOptions.getInterVal() ;      //取得时间间隔
		
//		Toast.makeText(cordova.getActivity(), "id是："+id, 0).show() ;
		if("addAlarmClock".equals(action)){ //增加闹钟
			persistAlarm(id, args);
			this.add(id, content,  date, title,interVal,callbackContext);
			return true ;
		}else if("delete".equals(action)){//删除指定的闹钟
			unpersistAlarm(id) ;
			cancelNotification(id, callbackContext) ;
			return true ;
		}else if("modifyAlarmClock".equals(action)){ //修改闹钟的方法,把指定ID的闹钟删除再添加一个闹钟即可
			flag = true ;
			unpersistAlarm(id) ;
			cancelNotification(id, callbackContext) ;
			persistAlarm(id, args);
			this.add(id, content,  date, title,interVal,callbackContext);
			return true ;
		}else if("deleteManyAlarmClock".equalsIgnoreCase(action)){//删除一个或多个闹钟
			this.cancelAppointNotification(args, callbackContext) ;
			return true ;
		}else if("searchAlarmClock".equalsIgnoreCase(action)){
			getAllNotification(callbackContext) ;
			return true ;
		}
		return false ;
	}
	/**
	 * @param id      提醒的ID
	 * @param content 提醒的内容
	 * @param date    提醒的时间
	 * @param title   提醒的标题
	 */
	private void add(String id, String content,  String date,String title,String interVal,CallbackContext callbackContext) {
		boolean result = alarm.addAlarm(id);
		if(result){
			if(flag){
				callbackContext.success("修改闹铃成功!");
				flag = false ;
			}else{
				callbackContext.success("增加闹铃成功!"); //（success就是一个回调方法，即javascript端function(winParam) 中的winParam的值）
			}
		}else{
			if(flag){
				callbackContext.success("修改闹铃失败!");
				flag = false ;
			}else{
				callbackContext.success("增加闹铃成功!"); //（success就是一个回调方法，即javascript端function(winParam) 中的winParam的值）
			}
			
		}
		
	}
	/**
	 * 删除闹钟
	 * @param notificationId   要删除闹钟的ID
	 * @param callbackContext  回调方法
	 */
	public void cancelNotification(String notificationId,CallbackContext callbackContext) {
		
	//	Toast.makeText(cordova.getActivity(), "删除的id："+notificationId,0).show();
		boolean result = alarm.cancelAlarm(notificationId);
		if (result) {
			if(flag){
				
			}else{
				callbackContext.success("删除闹铃成功!"); 
			}
		} else {
			if(flag){
				
			}else{
				callbackContext.success("删除闹铃成功!"); 
			}
			
		}
	}
	/**
	 * 删除指定的闹钟，传进来的参数是一个数组(里面是闹钟的ID)
	 */
	public  void cancelAppointNotification(JSONArray args,CallbackContext callbackContext){
		for (int i = 0; i < args.length(); i++) {  //遍历把传进来的ID的闹钟全部删除
			 String id = args.optString(i) ;
			 unpersistAlarm(id) ;
			 cancelNotification(id, callbackContext) ; 
		}
	}
	/**
	 * 查询所有闹钟
	 * @param callbackContext
	 */
	public void getAllNotification(CallbackContext callbackContext){
		final SharedPreferences alarmSettings = this.cordova.getActivity().getSharedPreferences(AlarmClock.PLUGIN_NAME, Context.MODE_PRIVATE);
		final Map<String, ?> allAlarms = alarmSettings.getAll();
		callbackContext.success(allAlarms.toString());
	//	Log.d(TAG, "查询所有数据："+allAlarms.toString()) ;
	}
    /**
     * 取消所有的通知  插件调用这个方法的时候
     */
    public void cancelAllNotifications(CallbackContext callbackContext) {
	/*
		Android中可以取消特定的闹钟，AlarmManager中没有取消所有的方法，所以我们可以循环中SharedPreference中的所有的闹钟来删除它
	 */
	final SharedPreferences alarmSettings = this.cordova.getActivity().getSharedPreferences(PLUGIN_NAME, Context.MODE_PRIVATE);
	final boolean result = alarm.cancelAll(alarmSettings);

		if (result) {
			callbackContext.success("删除所有闹钟成功!");
		} else {
			callbackContext.success("删除所有闹钟失败!");
		}
	}
	
	/*
	 * 保存alarm在SharedPreference中   键是id，值是js传过来的jsonArray
	 */
	private boolean persistAlarm(String alarmId, JSONArray optionsArr) {
			final Editor alarmSettingsEditor = this.cordova.getActivity().getSharedPreferences(PLUGIN_NAME, Context.MODE_PRIVATE).edit();
			alarmSettingsEditor.putString(alarmId, optionsArr.toString());
			return alarmSettingsEditor.commit();
	}
	/**
	 * 从sharedPreference中删除指定的闹钟
	 * @param alarmId  要删除的闹钟的ID
	 * @return
	 */
	private boolean unpersistAlarm(String alarmId) {
		final Editor alarmSettingsEditor = this.cordova.getActivity()
				.getSharedPreferences(PLUGIN_NAME, Context.MODE_PRIVATE).edit();
		alarmSettingsEditor.remove(alarmId);
		return alarmSettingsEditor.commit();
	}

    /**
     * 取消所有的保存在SharedPreference中的闹钟
     */
	private boolean unpersistAlarmAll() {
		final Editor alarmSettingsEditor = this.cordova.getActivity()
				.getSharedPreferences(PLUGIN_NAME, Context.MODE_PRIVATE).edit();
		alarmSettingsEditor.clear();
		return alarmSettingsEditor.commit();
	}
}
