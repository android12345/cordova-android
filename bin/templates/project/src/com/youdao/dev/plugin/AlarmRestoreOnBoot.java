package com.youdao.dev.plugin;



import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.youdao.dev.domain.AlarmOptions;

/**
 * 设置重启时广播接收者，如果alarms在重启的情况 下丢失了，要重新注册alarms
 */
public class AlarmRestoreOnBoot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
	final String pluginName = AlarmClock.PLUGIN_NAME;
	  Log.d(pluginName, "AlarmRestoreOnBoot: Successfully restored alarms upon reboot");
	//从SharedPreference中获得闹钟的信息
	final SharedPreferences alarmSettings = context.getSharedPreferences(pluginName, Context.MODE_PRIVATE);
	final Map<String, ?> allAlarms = alarmSettings.getAll();
	final Set<String> alarmIds = allAlarms.keySet();

	/*
	 * 重新注册每一个闹钟在闹钟管理器中，从SharedPreference中拿
	 */
	for (String alarmId : alarmIds) {
	    try {
		final AlarmHelper alarm = new AlarmHelper(context);
		final JSONArray alarmDetails = new JSONArray(alarmSettings.getString(alarmId, ""));

		final AlarmOptions options = AlarmOptions.ParseJsonArray(alarmDetails);

		//final boolean daily = options.isRepeatDaily();
		final String id = options.getNotificataionID();
		final String content = options.getAlarmTitle();
		final String date = options.getDate();
		final String title = options.getAlarmTitle();
		final String interval = options.getInterVal() ;
		
		alarm.addAlarm(id) ;
	    } catch (JSONException e) {
		Log.d(pluginName,
			"AlarmRestoreOnBoot: Error while restoring alarm details after reboot: " + e.toString());
	    }

	    Log.d(pluginName, "AlarmRestoreOnBoot: Successfully restored alarms upon reboot");
	}
    }
}
