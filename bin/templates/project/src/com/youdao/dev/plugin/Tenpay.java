package com.youdao.dev.plugin;

import java.util.HashMap;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.tenpay.android.oneclickpay.open.IPayCallback;
import com.youdao.dev.YouDaoApplication;
import com.youdao.dev.utils.HttpUtil;
import com.youdao.dev.utils.RSAUtil;
import com.youdao.dev.utils.SignUtil;
import com.youdao.dev.utils.XmlUtil;
import com.youdao.dev.utils.XmlUtil.ParseException;

/**
 * 腾讯支付接口
 * @author rome
 *
 */
public class Tenpay extends CordovaPlugin {

	public static final String ACTION = "tenpay";
	private static final String TAG = "Tenpay";
	
	private  String mTokenId;
	private  Handler mHandler;
	private ProgressDialog mReadingProgress;
	
	public CallbackContext mCallbackContext;
	/**
	 * 全局句柄，用于处理回调事件
	 */
	private  Handler handler = new Handler(Looper.getMainLooper()){
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
			case 0:
				// 处理成功
				// 时间戳单位为秒
				String timestamp = Long.toString(System.currentTimeMillis()/1000);
				StringBuilder sb = new StringBuilder();
				
				
				
				sb.append("bargainor_id=");
				sb.append(YouDaoApplication.TENPARTNER);
				sb.append("&timestamp=");
				sb.append(timestamp);
				sb.append("&token_id=");
				sb.append(mTokenId);
				sb.append("&user_id=");
				sb.append(YouDaoApplication.TENSELLER);
				
				String src = sb.toString();
				Log.d(TAG, "the src string to be signed = " + src);
				
				String sign = RSAUtil.sign(src, YouDaoApplication.RSA_PRIVATE);		
				Log.d(TAG, "the sign = " + sign);

				// 构造回调函数
				IPayCallback callback = new IPayCallback() {

					public void onPayCallback(final String retCode, final String retBundle) {
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								// 最终的数据处理
								mCallbackContext.success();
								new AlertDialog.Builder(cordova.getActivity())
								.setTitle("提示")
								.setMessage("返回码： " + retCode + " , 返回信息： " + retBundle)
								.setPositiveButton(
										"确定",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int whichButton) {}
										}).create().show();
								
							}
						});
					}
				};

				// 去一键支付
				com.tenpay.android.oneclickpay.open.Tenpay.pay(cordova.getActivity() , YouDaoApplication.TENPARTNER , 
						YouDaoApplication.TENSELLER , mTokenId,
						timestamp, 
						sign,
						callback);
				break;

			default:
				break;
			}
		};
		
	} ;
	
	/**
	 * 入口方法
	 */
	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		this.mCallbackContext = callbackContext;
		if (ACTION.equals(action)) {
			executePay(callbackContext , args);
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
	public void executePay(CallbackContext callbackContext , JSONArray args) {
		if(args.length() < 4){
			callbackContext.error("缺少参数");
			return;
		}
		
		if( Double.isNaN(args.optDouble(2))){
			callbackContext.error("价格必须是数字");
			return;
		}
		
		if( YouDaoApplication.TENNOTIFY.equals("")){
			callbackContext.error("必须配置验证地址");
			return;
		}
		String title = args.optString(0);
		String body = args.optString(1);
		double price = args.optDouble(2)*100;
		String orderno = args.optString(3);
		//getOutTradeNo();
		
		Log.d(TAG, "####" + title + body + price + orderno);
		// 组合字符串
		getOrderInfo(
				title,
				body,
				""+Math.floor(price),
				orderno);
//				getOutTradeNo());
		
		
		
		
//		String info = orderInfo + "&sign=" + "\"" + sign + "\"" + "&"
//				+ getSignType();
//		
//		Intent intent = new Intent();
//		intent.setPackage(this.cordova.getActivity().getPackageName());
//		intent.setAction("com.alipay.mobilepay.android");
//		intent.putExtra("order_info", info);
		// startActivityForResult(intent, 0);

//		cordova.startActivityForResult(this, intent, 0);
//		callbackContext.success();
	}
	
	 
	/**
	 * 获得订单信息
	 * @param subject
	 * @param body
	 * @param price
	 * @param orderno
	 * @return
	 */
	public void getOrderInfo(String subject, String body, String price,
			String orderno) {
		// 注意：按协议文档要求，商户应用最好通过自己后台访问财付通后台支付初始化cgi接口得到财付通订单token_id
		// 生成订单号token_id的操作，建议放至商户的服务器端来完成，以保护商户签名key不被反编译盗取

		// 1.拼一个生成订单的Http请求串(参见开发指南文档里协议说明)
		// 2.对请求串生成签名
		// 3.将请求串加上签名，发送http GET请求得到财付通侧返回的订单号token_id
		String url ="http://cl.tenpay.com/cgi-bin/wappayv2.0/wappay_init.cgi?";
		StringBuffer reqBuf = new StringBuffer();

		// 版本号,固定为2.0
		reqBuf.append("ver=2.0");

		// 请求来源，固定填211
		reqBuf.append("&sale_plat=211");

		// 值为1或2，“ 1” 表示UTF-8, “2 ”表示GB2312, 默认为1
		reqBuf.append("&charset=1"); // 可选

		// 银行类型:财付通支付填0
		reqBuf.append("&bank_type=0");

		// 商品描述,32个字符以内
		reqBuf.append("&desc=");
		reqBuf.append(Uri.encode(subject));

		// 此商户号为财付通自助测试商户号， 请替换为商户自己的商户号
		reqBuf.append("&bargainor_id=");
		reqBuf.append(YouDaoApplication.TENPARTNER);

		// 商户侧的订单号，测试时请保证每次用不一样的，否则重复下单会失败
		reqBuf.append("&sp_billno=");
		reqBuf.append(orderno);
//		reqBuf.append(Integer.toString((int) (Math.random() * 10000)));

		// 订单总金额,以分为单位,不允许包含任何字、符号
		 
		reqBuf.append("&total_fee=1");
//		reqBuf.append(price);
		 

		// 商戶接收财付通通知的URL,需给绝对路径，255字符内,格式如:http://wap.tenpay.com/tenpay.asp

		reqBuf.append("&notify_url=");
		reqBuf.append(YouDaoApplication.TENNOTIFY);
		
		

		// 商户附加信息,可做扩展参数，255字符内,在支付成功后原样返回给notify_url
		reqBuf.append("&attach=1");
//		reqBuf.append(Uri.encode(body));

		// 订单生成时间，格式为yyyymmddhhmmss，如2009年12月25日9点10分10秒表示为20091225091010。
		// 时区为GMT+8 beijing。该时间取自商户服务器
		// reqBuf.append("&time_start=20120809174510"); //可选

		// 订单失效时间，格式为yyyymmddhhmmss，如2009年12月25日9点10分10秒表示为20091225091010。
		// 时区为GMT+8 beijing。该时间取自商户服务器
		// reqBuf.append("&time_expire=20120810174510"); //可选

		// 用户(买方)的财付通帐户(QQ 或EMAIL)。
//		reqBuf.append("&purchaser_id="); // 可选


		// 此签名key为财付通自助测试商户号的签名key, 请替换为商户自己的
		String key = YouDaoApplication.TENKEY;
		
		// 对请求串生成签名
		String sign = SignUtil.getSign(key, reqBuf.toString());

		// 联网请求财付通支付网关，得到订单号token_id，后续拿这个token_id去支付
		showProgressDialog("正在提交支付...");
		Log.d("erik", "url = " + url + reqBuf.toString() + "&sign="
				+ sign);
		new GetOrderTokenTask().execute(url + reqBuf.toString()
				+ "&sign=" + sign);
		

//		return reqBuf.toString();
	}

	  
	/**
	 * 显示进度条
	 * @param msgId
	 */
	public void showProgressDialog(String msgId) {
		if (mReadingProgress == null || !mReadingProgress.isShowing()) {
			mReadingProgress = new ProgressDialog(this.cordova.getActivity());
			if (mReadingProgress != null) {
				mReadingProgress.setMessage(msgId);
				mReadingProgress.setIndeterminate(true);
				mReadingProgress.setCancelable(false);
				mReadingProgress.show();
			}
		}
	}
	/**
	 * 隐藏进度条
	 */
	public void dismissProgressDialog() {
		if (mReadingProgress != null) {
			mReadingProgress.dismiss();
		}
	}
	
	/**
	 * 提交网络接口信息
	 * @author rome
	 *
	 */
	private class GetOrderTokenTask extends AsyncTask<String, String, String> {
		String mMsg = "";
		@Override
		protected String doInBackground(String... params) {
			String token_id = null;
			String request = params[0];

			HttpUtil httpUtil = new HttpUtil(cordova.getActivity());
			Bundle data = httpUtil.doGet(request);

			if (data.getInt(HttpUtil.HTTP_STATUS) == HttpStatus.SC_OK) {
				byte[] ret = data.getByteArray(HttpUtil.HTTP_BODY);
				if (ret != null) {
					String recieved = new String(ret);
					Log.d("erik", "doInBackground, recieved = " + recieved);

					try {
						HashMap<String, String> dataMap = XmlUtil
								.parse(recieved);
						token_id = dataMap.get("token_id");
						if(token_id == null){
							mMsg = dataMap.get("err_info");
						}
						Log.d("erik", "doInBackground, token_id = " + token_id);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}

			return token_id;
		}

		@Override
		protected void onProgressUpdate(String... progress) {

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dismissProgressDialog();
			if (result != null) {
				Log.d("erik", "onPostExecute, result = " + result);
				mTokenId = result;
				handler.sendMessage(Message.obtain(handler, 0, null));
//				Toast.makeText(GetOrderActivity.this, "成功生成订单，请点击支付按钮！",
//						Toast.LENGTH_LONG).show();
			} else {
//				Toast.makeText(cordova.getActivity(), mMsg,
//						Toast.LENGTH_LONG).show();
				mCallbackContext.error(mMsg);
			}

		}
	}


}
