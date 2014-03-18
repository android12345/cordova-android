package com.youdao.dev;

import android.app.Application;
/**
 * For developer startup JPush SDK  和取得　经纬度初始化
 * 
 * 一般建议在自定义 Application 类里初始化。也可以在主 Activity 里。
 */
public class YouDaoApplication extends Application {
	   private static final String TAG = "YouDaoApplication";
	   
		public static final String NOTIFY = "";
		public static final String PARTNER = "2088901482215543";
//											  2088901482215543
		public static final String SELLER = "yjp-huitian@163.com";
//											 2but9ku1z7r1vb0anr1bsdxcoravzoty
		// pem
//		public static final String RSA_PRIVATE = "MIICXAIBAAKBgQDE/tHBBVZFVfq1eYCgkYDguvPID2aeDyGA3dq9eld5OyIoUuBFf54Ue6ztbM6pqwT/G3lHdQR9yHN8iDMyFlG5YZZi5YAM+O/WAHlFaVs/D+22ikX9SgkuhDTIa3oY9Cr0qa/2ac92qSihNe3rZd47m5LwKVzCFbjKOXw0AaGBJwIDAQABAoGAeumvgGrPEEX8PtzHx+fhbNbZInuRgI9aTSifwdei2o5t2pETQ+nDfW5JUH6yV6TQYkgHHPHDUqQOgqYMo3TPme4utAJbbQkgDwHzWkKTeMf+ctC/+zSlt8sQgGTlfYdycQnmA217vCdQHWkm1qFxX4CgDQGIWuJMi624OGhMsQECQQDh8EhKc8YmX+Kk6ITB2AxV6J7xd85KAiGOYn4qRVONndnliM2idyfHDxu/7qejbWmM4MhIUTZYe6aBhuGG2rWnAkEA3zSy/kTE9OAyjTP7gpu2U6CRWctV18XQHyIItedIGjdupJSpFEegRR7+qdbSqtHT+rpCXSIUyn74JL3/N6NIgQJALuEKPthyvreacRkFoqrPNGwFvphbJyP2DnMFNoQX6ARel6+g6xrKGBVcbqB4DALBqcdiBw9CtW1CiRGRbcendQJBAMUUeP79pZI56yi2P1rhNim82toYKaGtbcgvetVycz2CuFTlVi0r2R1u8pjvr29Yef0mPLaTAxyQ43U9tX1g1QECQGya/wOtw0VSS7lvA1JhglCwjhca3CRMULo+u57iP3Rf/C8AQ/8d0G4I9f21Qe04whe6k+jCeroh8qOUK7K8TMY=";
		// pkcs8
		public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMT+0cEFVkVV+rV5gKCRgOC688gPZp4PIYDd2r16V3k7IihS4EV/nhR7rO1szqmrBP8beUd1BH3Ic3yIMzIWUblhlmLlgAz479YAeUVpWz8P7baKRf1KCS6ENMhrehj0KvSpr/Zpz3apKKE17etl3jubkvApXMIVuMo5fDQBoYEnAgMBAAECgYB66a+Aas8QRfw+3MfH5+Fs1tkie5GAj1pNKJ/B16Lajm3akRND6cN9bklQfrJXpNBiSAcc8cNSpA6CpgyjdM+Z7i60AlttCSAPAfNaQpN4x/5y0L/7NKW3yxCAZOV9h3JxCeYDbXu8J1AdaSbWoXFfgKANAYha4kyLrbg4aEyxAQJBAOHwSEpzxiZf4qTohMHYDFXonvF3zkoCIY5ifipFU42d2eWIzaJ3J8cPG7/up6NtaYzgyEhRNlh7poGG4YbatacCQQDfNLL+RMT04DKNM/uCm7ZToJFZy1XXxdAfIgi150gaN26klKkUR6BFHv6p1tKq0dP6ukJdIhTKfvgkvf83o0iBAkAu4Qo+2HK+t5pxGQWiqs80bAW+mFsnI/YOcwU2hBfoBF6Xr6DrGsoYFVxuoHgMAsGpx2IHD0K1bUKJEZFtx6d1AkEAxRR4/v2lkjnrKLY/WuE2Kbza2hgpoa1tyC961XJzPYK4VOVWLSvZHW7ymO+vb1h5/SY8tpMDHJDjdT21fWDVAQJAbJr/A63DRVJLuW8DUmGCULCOFxrcJExQuj67nuI/dF/8LwBD/x3Qbgj1/bVB7TjCF7qT6MJ6uiHyo5QrsrxMxg==";

		public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2nnJD5aqWuRVotcsVQE97dXs86I5wqsx0OcSNK9l1u5q+2Hl5fkVblQfM8RW6B66XZ5Wad7Gp0tSY9saIZbxjjFh2mbN2Htyla/oGjJvwPKlL9gdHrs3fFNQL2SzDhXwQte7l8Pf8fIBywkZ+bnfbZICyG0bqJQAIzJZQoe6f4QIDAQAB";

	    @Override
	    public void onCreate() {
	    	// Log.d(TAG, "onCreate");
	         super.onCreate();
	 		
//	         JPushInterface.setDebugMode(true); 	//设置开启日志,发布时请关闭日志
//	         
//	         JPushInterface.init(this);     		// 初始化 JPush
//	         JPushInterface.setAliasAndTags(this, DeviceUtils.getUUID(this), null) ;//极光设置别名
	         
	    }
		
}
