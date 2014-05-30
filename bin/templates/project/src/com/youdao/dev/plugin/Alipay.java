package com.youdao.dev.plugin;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.alipay.android.app.encrypt.Rsa;
import com.youdao.dev.YouDaoApplication;

public class Alipay extends CordovaPlugin {

	public static final String ACTION = "alipay";
	private static final String TAG = "Alipay";
	
	private CallbackContext mCallbackContext;
	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		mCallbackContext = callbackContext;

		if (ACTION.equals(action)) {
			pay(callbackContext , args);
			return true;
		}

		return false;
	}

	/**
	 * get the out_trade_no for an order. 获取外部订单号
	 * 
	 * @return
	 */
//	private String getOutTradeNo() {
//		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
//				Locale.getDefault());
//		Date date = new Date();
//		String key = format.format(date);
//
//		java.util.Random r = new java.util.Random();
//		key = key + r.nextInt();
//		key = key.substring(0, 15);
//		return key;
//	}

	// 点击事件
	public void pay(CallbackContext callbackContext , JSONArray args) {
		if(args.length() < 4){
			callbackContext.error("缺少参数");
			return;
		}
		
		if( Double.isNaN(args.optDouble(2))){
			callbackContext.error("价格必须是数字");
			return;
		}
		String title = args.optString(0);
		String body = args.optString(1);
		double price = args.optDouble(2);
		String orderno = args.optString(3);
		//getOutTradeNo();
		
		Log.d(TAG, "####" + title + body + price + orderno);
		
		String orderInfo = getOrderInfo(
				title,
				body,
				""+price,
				orderno);
//				getOutTradeNo());
		
		String signType = getSignType();
		String sign = sign(signType, orderInfo);
		try {
			sign = URLEncoder.encode(sign, "UTF-8");
			Log.d(TAG , sign);
		} catch (UnsupportedEncodingException e) {
		}
		String info = orderInfo + "&sign=" + "\"" + sign + "\"" + "&"
				+ getSignType();
		
		Intent intent = new Intent();
		intent.setPackage(this.cordova.getActivity().getPackageName());
		intent.setAction("com.alipay.mobilepay.android");
		intent.putExtra("order_info", info);

		cordova.startActivityForResult(this, intent, 0);

	}
	
	// 返回程序
	@Override
	public void onActivityResult(int requestCode, int result, Intent data) {
		super.onActivityResult(requestCode, result, data);
		Log.d(TAG, "income onActivityResult");

		String action = data.getAction();
		String resultStatus = data.getStringExtra("resultStatus");
		String memo = data.getStringExtra("memo");
		String resultString = data.getStringExtra("result");
		
		if(resultStatus != null && resultStatus.equals("9000")){
			mCallbackContext.success();
		}else{
			Toast.makeText(
					this.cordova.getActivity(),
					 memo + " = " + resultString, Toast.LENGTH_LONG).show();
			mCallbackContext.error(memo);
		}
	}

	// 获得订单信息
	public String getOrderInfo(String subject, String body, String price,
			String orderno) {
		String orderInfo = "partner=" + "\"" + YouDaoApplication.PARTNER + "\"";
		orderInfo += "&";
		orderInfo += "seller_id=" + "\"" + YouDaoApplication.SELLER + "\"";
		orderInfo += "&";
		orderInfo += "out_trade_no=" + "\"" + orderno + "\"";
		orderInfo += "&";
		orderInfo += "subject=" + "\"" + subject + "\"";
		orderInfo += "&";
		orderInfo += "body=" + "\"" + body + "\"";
		orderInfo += "&";
		orderInfo += "total_fee=" + "\"" + price + "\"";

		if (!YouDaoApplication.NOTIFY.equals("")) {
			orderInfo += "&";
			orderInfo += "notify_url=" + "\"" + YouDaoApplication.NOTIFY + "\"";
		}

		// 接口名称， 定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型，定值
		orderInfo += "&payment_type=\"1\"";

		// 字符集，默认utf-8
		orderInfo += "&_input_charset=\"utf-8\"";

		// 超时时间 ，默认30分钟.
		// 设置未付款交易的超时时间，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		// 该功能需要联系支付宝配置关闭时间。
		orderInfo += "&it_b_pay=\"30m\"";

		// 商品展示网址,客户端可不加此参数
//		orderInfo += "&show_url=\"m.alipay.com\"";

		// verify(sign, orderInfo);
		Log.d(TAG , orderInfo);

		return orderInfo;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param signType
	 *            签名方式
	 * @param content
	 *            待签名订单信息
	 * @return
	 */
	public String sign(String signType, String content) {
		return Rsa.sign(content, YouDaoApplication.RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 * @return
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

}
