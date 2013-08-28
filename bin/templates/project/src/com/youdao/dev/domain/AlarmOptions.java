
package com.youdao.dev.domain;

import org.json.JSONArray;

/**
 * @author junjun
 *	闹钟的数据操作   操作的javascript的参数，把javascript中传过来的数组解析并封装成一个对象
 */
public class AlarmOptions {
	
	private String alarmTitle ;  //通知的标题
	private String alarmContent ; //通知的内容
	private String notificataionID ; //通知的ID
	private String date ;             //通知的时间
	private String interVal ; //提醒 的时间间隔
	
	/**
	 * @return the alarmTitle
	 */
	public String getAlarmTitle() {
		return alarmTitle;
	}

	/**
	 * @return the alarmContent
	 */
	public String getAlarmContent() {
		return alarmContent;
	}

	/**
	 * @return the notificataionID
	 */
	public String getNotificataionID() {
		return notificataionID;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	
	/**
	 * @return the interVal
	 */
	public String getInterVal() {
		return interVal;
	}

	/**
	 * @param interVal the interVal to set
	 */
	public void setInterVal(String interVal) {
		this.interVal = interVal;
	}

	// ["1",  "这是一个内容", "1362347820","这是标题"])
	//把传javaScript传过来的数据封装到对象中
	public static AlarmOptions ParseJsonArray(JSONArray optionsArr){
		AlarmOptions alarmOptions = null ;
		try {
			alarmOptions = new AlarmOptions() ;
			alarmOptions.notificataionID = optionsArr.optString(0) ;
			alarmOptions.alarmContent = optionsArr.optString(1) ;
			alarmOptions.date = optionsArr.optString(2) ;
			alarmOptions.interVal = optionsArr.optString(3) ;
			alarmOptions.alarmTitle = optionsArr.optString(4) ;
			return alarmOptions ;
		} catch (Exception e) {
			e.printStackTrace() ;
			return null ;
		}
	}
}
