package com.youdao.dev;

import com.baidu.frontia.FrontiaApplication;

 /**
 * For developer startup JPush SDK  和取得　经纬度初始化
 * 
 * 一般建议在自定义 Application 类里初始化。也可以在主 Activity 里。
 */
public class YouDaoApplication extends FrontiaApplication {
    private static final String TAG = "YouDaoApplication";
    
   public static final String NOTIFY = "";
   public static final String PARTNER = "";
   public static final String SELLER = "";

   
   public static final String TENNOTIFY = "";
   public static final String TENPARTNER = "";
   public static final String TENKEY = "";
   public static final String TENSELLER = "";

   // pem
//   public static final String RSA_PRIVATE = "";
   // pkcs8
   public static final String RSA_PRIVATE = "";

   public static final String RSA_PUBLIC = "";

   
     @Override
     public void onCreate() {
       // Log.d(TAG, "onCreate");
          super.onCreate();
     
//          JPushInterface.setDebugMode(true);   //设置开启日志,发布时请关闭日志
//          
//          JPushInterface.init(this);         // 初始化 JPush
//          JPushInterface.setAliasAndTags(this, DeviceUtils.getUUID(this), null) ;//极光设置别名
          
     }
   
}