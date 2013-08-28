package com.youdao.dev.plugin;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.youdao.dev.R;
import com.youdao.dev.domain.AlarmOptions;

/**
 	在AlarmManager中设置的时间时执行这个广播接收者，发送一个通知在通知栏，手机振动和声音提醒
 * 
 */
public class AlarmReceiver extends BroadcastReceiver {
	

	public static final String TITLE = "ALARM_TITLE";
	//public static final String TICKER_TEXT = "ALARM_TICKER";
	
	public static final String CONTENT = "ALEARM_CONTENT";
	public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
	private static final String TAG = "AlarmReceiver";
	private SharedPreferences sharedPreferences ;
	private String content ;
	private String notificationTitle ;
	private String notificationSubText ;

	/* Contains time in 24hour format 'HH:mm' e.g. '04:30' or '18:23' */
//	public static final String HOUR_OF_DAY = "HOUR_OF_DAY";
//	public static final String MINUTE = "MINUTES";

	@Override
	public void onReceive(Context context, Intent intent) {
		
		sharedPreferences = context.getSharedPreferences(AlarmClock.PLUGIN_NAME, Context.MODE_PRIVATE) ;
		
		final Intent intent2 = new Intent(context, AlarmReceiver.class);
		
		Log.d("AlarmReceiver", "AlarmReceiver invoked!");
		//final SharedPreferences alarmSettings = context.getSharedPreferences(MyPlugin.PLUGIN_NAME, Context.MODE_PRIVATE);
		final Bundle bundle = intent.getExtras();
		final Object systemService = context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		

		// Retrieve notification details from the intent
//		final String content = bundle.getString(CONTENT);   //取得通知的内容
////Toast.makeText(context, "tickerText is " + content, 0).show() ;
//		final String notificationTitle = bundle.getString(TITLE);
//		final String notificationSubText = bundle.getString(CONTENT);
//		final long interval = bundle.getLong("interval") ;
//		final long parseDate = bundle.getLong("parseDate") ;
		int notificationId = Integer.parseInt(bundle.getString(NOTIFICATION_ID));
		if(notificationId!=0){
			try {
				JSONArray jsonArray = new JSONArray(sharedPreferences.getString(notificationId+"", null)) ;  //从sharedPreference中取出来
			//	Log.d(TAG, "json取出来的数据："+jsonArray) ;
				AlarmOptions alarmOptions = AlarmOptions.ParseJsonArray(jsonArray) ;
				content = alarmOptions.getAlarmContent() ;
				notificationTitle = alarmOptions.getAlarmTitle() ;
				notificationSubText = alarmOptions.getAlarmContent() ;
				final long interval = Long.valueOf(alarmOptions.getInterVal()) ;
				final long parseDate = Long.valueOf(alarmOptions.getDate()) ;
				
				
				intent2.setAction("" + notificationId+"");
				intent2.putExtra(AlarmReceiver.NOTIFICATION_ID, notificationId+"") ;
				intent2.putExtra("interval", interval) ;
				intent2.putExtra(TITLE, notificationTitle) ;
				intent2.putExtra(CONTENT, content) ;
				final PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent2, PendingIntent.FLAG_CANCEL_CURRENT);
				final AlarmManager am = getAlarmManager(context);
				if(interval==2){ //每月重复
					Calendar passtime = Calendar.getInstance()  ;
					passtime.set(Calendar.MONTH,passtime.get(Calendar.MONTH)+1);
					am.set(AlarmManager.RTC_WAKEUP, passtime.getTimeInMillis(), sender) ;
//	Log.d(TAG, "进到receiver中的闹钟时间是："+getStandardTime(passtime.getTimeInMillis()))	 ;			
				}if(interval==3){ //每年重复
					Calendar passtime = Calendar.getInstance()  ;
					passtime.set(Calendar.YEAR,passtime.get(Calendar.YEAR)+1);
					am.set(AlarmManager.RTC_WAKEUP, passtime.getTimeInMillis(), sender) ;
	//Log.d(TAG, "进到receiver中的闹钟时间3是："+getStandardTime(passtime.getTimeInMillis())) ;
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	
		final NotificationManager notificationMgr = (NotificationManager) systemService;
		final Notification notification = new Notification(R.drawable.icon, content,
				System.currentTimeMillis());
		
		notification.flags |= Notification.FLAG_AUTO_CANCEL; //通知点击完以后就消失了
//		Intent notificationIntent = new Intent(context,null) ;
//		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		notificationIntent.addFlags(Intent.FILL_IN_DATA);
//        notificationIntent.putExtra("content", content) ;

//	Toast.makeText(context, "内容 是："+content, 1).show() ;	
	//id区分不同的PendingIntent 以便点击的时候显示当前通知的信息
		final PendingIntent contentIntent = PendingIntent.getActivity(context, notificationId, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);//最后个参数要写成更新 ,不然跳转过去取不到数据
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.vibrate = new long[] { 0, 100, 200, 300 };
		//通知的标题和内容 
		notification.setLatestEventInfo(context, notificationTitle, notificationSubText, contentIntent);

		/*
		 * 在通知栏显示通知根据ID
		 */
		notificationMgr.notify(notificationId, notification);
		
		//Toast.makeText(context, "提醒的内容是："+content, 0).show();
	}
	private AlarmManager getAlarmManager(Context context) {
		final AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		return am;
	}
	public static String getStandardTime(long timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		Date date = new Date(timestamp);
		sdf.format(date);
		return sdf.format(date);
	}
}
