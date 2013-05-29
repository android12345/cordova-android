package com.youdao.dev;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.youdao.dev.domain.LocationBean;
import com.youdao.dev.utils.CommUtils;
import com.youdao.dev.utils.DeviceUtils;

public class LocationProvider {
	public static final String TAG = "LocationProvider";

	private static LocationClient mLocationClient = null; 
	 
    private static LocationBean station = new LocationBean(); 
    private  MyBDListener listener = new MyBDListener(); 
 
    private static  Context context; 
 
    public LocationProvider(Context context) { 
        super(); 
        LocationProvider.context = context; 
    } 
 
    /**
     * 设置参数和开启获取定位
     */
    public void startLocation() { 
        mLocationClient = new LocationClient(context); 
        LocationClientOption option = new LocationClientOption(); 
        option.setOpenGps(true); // 打开gps 
        option.setCoorType("bd09ll"); // 设置坐标类型为bd09ll 
        option.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先 
        option.setProdName("youdao"); // 设置产品线名称 
        option.disableCache(true);	
        mLocationClient.setLocOption(option); 
        mLocationClient.registerLocationListener(listener); 
        mLocationClient.start();//将开启与获取位置分开，就可以尽量的在后面的使用中获取到位置 
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
     * 更新位置并保存到SItude中 
     */ 
    public void updateListener() { 
        if (mLocationClient != null && mLocationClient.isStarted()) { 
            mLocationClient.requestLocation(); 
        } 
    } 
 
    /** 
     * 获取经纬度信息 
     *  
     * @return 
     */ 
    public LocationBean getLocation() { 
        return station; 
    } 
 
    private  class MyBDListener implements BDLocationListener { 
 
        @Override 
        public void onReceiveLocation(BDLocation location) { 
            if (location.getCity() == null) { 
                int type = mLocationClient.requestLocation(); 
            } 
            
            station.setLatitude( location.getLatitude() + ""); 
            station.setLongitude(location.getLongitude() + "") ;
            
         //   Log.d("*************************:", location.getLatitude()+","+location.getLongitude()) ;
            
            final String app_id = context.getResources().getString(R.string.appid) ;
            //Toast.makeText(context, "appid is "+app_id, 0).show() ;
            JpushManager.getInstance().jpushSendData(context, app_id, DeviceUtils.getUUID(context), "", CommUtils.getVersionCode(context), CommUtils.getAndroidSDKVersion(), location.getLatitude() + "", location.getLongitude() + "", CommUtils.getProvidersName(context), CommUtils.getPhoneBrand(), new JsonHttpResponseHandler(){
            	@Override
            	public void onSuccess(JSONObject arg0) {
            		super.onSuccess(arg0);
            	//	   Log.d("@@@@@@@@@@@@@@@@:", "13222222222222222222222222222222222") ;
            		Log.d("@@@@@@@@@@@@@@@@@@@@@@@@:", arg0.toString()) ;
            	}
            }) ;
        } 
 
        @Override 
        public void onReceivePoi(BDLocation arg0) { 
            // return 
        } 
    } 
}
