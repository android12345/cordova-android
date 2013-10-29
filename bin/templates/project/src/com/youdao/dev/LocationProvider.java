package com.youdao.dev;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import com.youdao.dev.utils.CommUtils;
import com.youdao.dev.utils.DeviceUtils;

public class LocationProvider {
	public static final String TAG = "LocationProvider";

	private static LocationClient mLocationClient = null;

	private static Context context;
	
	private SharedPreferences preferences = null ;

	public LocationProvider(Context context) {
		super();
		LocationProvider.context = context;
		
		 preferences = context.getSharedPreferences("push_user", context.MODE_PRIVATE) ;
	}

	/**
	 * 设置参数和开启获取定位
	 */
	public void startLocation() {
		BDLocationListener listener = new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				/*
				 * if (location.getCity() == null) { int type =
				 * mLocationClient.requestLocation(); }
				 */

				// Log.d("*************************:", location.getLatitude()
				// + "," + location.getLongitude());

				final String app_id = context.getResources().getString(
						R.string.app_id);
				String baiduuserid = preferences.getString("user_id", "") ;
				
				
				Toast.makeText(context, "123"+baiduuserid, 0).show() ;
				
				
				// Toast.makeText(context, "appid is "+app_id, 0).show() ;
				BaidupushManager.getInstance().baiduPushSendData(context, "51",
						baiduuserid, "",
						CommUtils.getVersionCode(context),
						CommUtils.getAndroidSDKVersion(),
						location.getLatitude() + "",
						location.getLongitude() + "",
						CommUtils.getProvidersName(context),
						CommUtils.getPhoneBrand(),
						new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(JSONObject arg0) {
								super.onSuccess(arg0);
								Log.d("**********************************************", arg0.toString());
							}
							@Override
							public void onFailure(Throwable arg0, String arg1) {
								// TODO Auto-generated method stub
								super.onFailure(arg0, arg1);
								Log.d("**********************************************error", arg1.toString());
							}
						});

				stopListener();
			}

			@Override
			public void onReceivePoi(BDLocation arg0) {
				// return
			}
		};

		mLocationClient = new LocationClient(context);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型为bd09ll
		option.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先
		option.setProdName("youdao"); // 设置产品线名称
		option.disableCache(true);
		
		mLocationClient.setLocOption(option);
		mLocationClient.registerLocationListener(listener);

		mLocationClient.start();// 将开启与获取位置分开，就可以尽量的在后面的使用中获取到位置
	}

	/**
	 * 停止，减少资源消耗
	 */
	public void stopListener() {
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.stop();
			mLocationClient = null;
		}
	}

	/**
	 * 更新位置
	 */
	public void updateListener() {
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.requestLocation();
		}
	}

}
