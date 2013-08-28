package com.youdao.dev.plugin;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

/**
 *本地插件的帮助类，可以被AlarmRestoreOnBoot重复使用，也可以被MyPlugin使用
 *  见MyPlugin
 *  见AlarmRestoreOnBoot
 */
public class AlarmHelper {

    private static final String TAG = "AlarmHelper";
	private Context context;
	private SharedPreferences sharedPreferences ;

    public AlarmHelper(Context context) {
    	this.context = context;
    	sharedPreferences = context.getSharedPreferences(AlarmClock.PLUGIN_NAME, Context.MODE_PRIVATE) ;
    }

    /**
     * 添加闹钟根据ID不同增加不同的闹钟，如果ID相同后面的闹钟会替换掉前面的闹钟
     */
    public boolean addAlarm(String notificationId) {
    	
    	String args = sharedPreferences.getString(notificationId, null) ;  //取得保存alarm的ID所对应的JSON串
   JSONArray array = null ;
   try {
	 array = new JSONArray(args) ;
   } catch (JSONException e) {
		e.printStackTrace();
	}
	String content = array.optString(1) ;
	String date = array.optString(2) ;
	String interVal = array.optString(3) ;
	String alarmTitle = array.optString(4) ;
	Log.d(TAG, "保存的args转化成 is " + content+","+date+","+","+alarmTitle+","+interVal) ;

    
    final long parseDate = Long.parseLong(date) ;
    final long interval = Long.parseLong(interVal) ; //取得时间间隔
	//final long triggerTime = cal.getTimeInMillis();
	final Intent intent = new Intent(this.context, AlarmReceiver.class);
	//final int hour = cal.get(Calendar.HOUR_OF_DAY);  //取得时钟
	//final int min = cal.get(Calendar.MINUTE);        //取得分钟
	
	intent.setAction("" + notificationId);
//	intent.putExtra("parseDate", parseDate) ;
//	intent.putExtra("interval", interval) ;
//	intent.putExtra(AlarmReceiver.TITLE, alarmTitle);
//	intent.putExtra(AlarmReceiver.CONTENT, content);
	intent.putExtra(AlarmReceiver.NOTIFICATION_ID, notificationId) ;

	final PendingIntent sender = PendingIntent.getBroadcast(this.context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	//取得闹钟管理的服务
	final AlarmManager am = getAlarmManager();
	long now = System.currentTimeMillis() ;  //取得系统当前的时间
	
//Calendar systemcalendar = Calendar.getInstance() ;
////Log.d(TAG, "明年的今天是："+calendar.getTime()) ;
////calendar.setTimeInMillis(parseDate*1000) ; //设置传过来的时间戳
////calendar.add(Calendar.YEAR, +1) ; //加上一年
//Log.d(TAG, "格式化后的时间是："+getStandardTime(systemcalendar.getTimeInMillis())) ;
//
//Log.d(TAG, "系统时间是："+System.currentTimeMillis()) ;//calendar.getTimeInMillis(), System.currentTimeMillis()*365*24*60*60*1000, sender
//Log.d(TAG, "传过来的时间是："+parseDate*1000) ;
//
//Log.d(TAG, systemcalendar.get(Calendar.YEAR)+"年") ;
//Log.d(TAG, systemcalendar.get(Calendar.MONTH)+1+"月") ;
//Log.d(TAG, systemcalendar.get(Calendar.DATE)+"日") ;
//Log.d(TAG, systemcalendar.get(Calendar.DAY_OF_WEEK)-1+"") ;
//Log.d(TAG, systemcalendar.get(Calendar.HOUR)+"时") ;
//Log.d(TAG, systemcalendar.get(Calendar.MINUTE)+"分") ;

//传递来的时间戳
Calendar passtime = Calendar.getInstance()  ;
passtime.setTimeInMillis(parseDate*1000) ;
//Log.d(TAG, "----------------------------------\n") ;
//Log.d(TAG, passtime.get(Calendar.YEAR)+"年") ;
//Log.d(TAG, passtime.get(Calendar.MONTH)+1+"月") ;
//Log.d(TAG, passtime.get(Calendar.DATE)+"日") ;
//Log.d(TAG, passtime.get(Calendar.DAY_OF_WEEK)-1+"") ;
//Log.d(TAG, passtime.get(Calendar.HOUR)+"时") ;
//Log.d(TAG, passtime.get(Calendar.MINUTE)+"分") ;


/**
 * 每日重复，判断时和分，		
 */
	if(now >parseDate*1000){   //如果传过来的时间比系统时间小  要推后再响
		if(interval ==0){  //每日重复
			passtime.add(Calendar.DATE, 1) ;  //加一天
			am.setRepeating(AlarmManager.RTC_WAKEUP, passtime.getTimeInMillis(), 24*60*60*1000, sender) ; 
			Toast.makeText(context, "调用了大于的0了", 0).show() ;
		}else if(interval ==1){  // 每周重复
			passtime.set(Calendar.DAY_OF_MONTH,passtime.get(Calendar.DAY_OF_MONTH)+7);
			am.setRepeating(AlarmManager.RTC_WAKEUP, passtime.getTimeInMillis(), 24*60*60*1000*7, sender) ;
		}else if(interval == 2){  //每月重复
			passtime.set(Calendar.MONTH,passtime.get(Calendar.MONTH)+1);
			am.set(AlarmManager.RTC_WAKEUP, passtime.getTimeInMillis(), sender) ;

		}else if(interval == 3){  //每年重复
			passtime.set(Calendar.YEAR,passtime.get(Calendar.YEAR)+1);
			am.set(AlarmManager.RTC_WAKEUP, passtime.getTimeInMillis(), sender) ;
		}
	}
	else{
		if(interval ==0){  //每日重复
				am.setRepeating(AlarmManager.RTC_WAKEUP, parseDate*1000, 24*60*60*1000, sender) ;
		}
		else if(interval == 1){ //每周重复
				am.setRepeating(AlarmManager.RTC_WAKEUP, parseDate*1000, 24*60*60*1000*7, sender) ;
		}else if(interval == 2){//每月重复                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
				am.set(AlarmManager.RTC_WAKEUP, parseDate*1000, sender) ;
		}else if(interval == 3){ //每年重复
			am.set(AlarmManager.RTC_WAKEUP, parseDate*1000, sender) ;
			//am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), "一年后", sender);
		}else if(interval == 100){ //永不重复
			 am.set(AlarmManager.RTC_WAKEUP,  parseDate*1000, sender);
		}
	
	}
	return true;
    }
    
    /**
     * 取消闹钟（根据ID取消指定的闹钟）
     * @param notificationId  要取消的闹钟的ID
     * @return 是否取消成功
     */
    public boolean cancelAlarm(String notificationId) {
	/**
	 * 和创建一个Intent类似，确定通知中的ID和action动作中的相同，取得服务并取消它
	 */
	final Intent intent = new Intent(this.context, AlarmReceiver.class);
	intent.setAction("" + notificationId);  //要和增加闹钟中的action中的ID相同 

	final PendingIntent pi = PendingIntent.getBroadcast(this.context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	final AlarmManager am = getAlarmManager();

	try {
	    am.cancel(pi);
	} catch (Exception e) {
	    return false;
	}
	return true;
    }

    /**
     *取消所有的闹钟，参见MyPlugin中的取消类
     */
	public boolean cancelAll(SharedPreferences alarmSettings) {
		final Map<String, ?> allAlarms = alarmSettings.getAll();
		final Set<String> alarmIds = allAlarms.keySet();

		for (String alarmId : alarmIds) {
			// Log.d(LocalNotification.PLUGIN_NAME,
			// "Canceling notification with id: " + alarmId);
			String alarmInt = alarmId;
			cancelAlarm(alarmInt);
		}
		return true;
	}
	/**
	 * 取得闹钟服务的方法
	 * @return  返回一个闹钟的管理类
	 */
	private AlarmManager getAlarmManager() {
		final AlarmManager am = (AlarmManager) this.context
				.getSystemService(Context.ALARM_SERVICE);

		return am;
	}
	
	/**
	 * 格式化时间戳  只是用来测试的
	 * @param timestamp  
	 * @return
	 */
	
	public static String getStandardTime(long timestamp) {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
	Date date = new Date(timestamp);
	sdf.format(date);
	return sdf.format(date);
}
}
