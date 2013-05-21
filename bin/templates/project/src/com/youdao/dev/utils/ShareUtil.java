package com.youdao.dev.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.platformtools.Util;
import com.umeng.socialize.bean.CustomPlatform;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;

import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.OnCustomPlatformClickListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.media.UMediaObject.MediaType;
import com.youdao.dev.R;

/**
 * @author junjun
 * 分享的工具类，集成友盟的分享功能和自定义的平台（如：微信，朋友圈）
 */
public class ShareUtil {
	private static IWXAPI api;
	private static final int THUMB_SIZE = 150;
	private static final String TAG = "ShareUtil";
	private Context context ;
	private  UMSocialService controller ;
	private static String wxAppID ;
	//private static String uMengID ;
	/**
	 * 创建和注册AppID到微信
	 * @param context
	 */
	public  void createWXAPI(Context context,String wxAppID){
		//api = WXAPIFactory.createWXAPI(context, "wx79474d05acb6a73b");
		api = WXAPIFactory.createWXAPI(context, wxAppID);
		ShareUtil.wxAppID = wxAppID ;
		//api.registerApp("wx79474d05acb6a73b") ;
		api.registerApp(wxAppID) ;
		api.handleIntent(((Activity) context).getIntent(), new IWXAPIEventHandler() {
			@Override
			public void onResp(BaseResp arg0) {
				SendAuth.Resp resp = (SendAuth.Resp) arg0;
				System.out.println(resp.userName);
			}

			@Override
			public void onReq(BaseReq arg0) {}
		});
	}
	
//	public static void  createUmeng(Activity context,String uMengID){
//		SocializeConstants.APPKEY = uMengID;
//		//ShareUtil.uMengID = uMengID ;
//	}
	/**
	 * 解决严格模式 采用异步
	 */
	public   Handler UIHandler = new Handler(Looper.getMainLooper()){
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
			case 1:
				Bitmap bitmap = (Bitmap) msg.obj ;
				controller.setShareImage(new UMImage (
						context, bitmap));
				break;

			default:
				break;
			}
		};
	};
	/**
	 * 友盟的分享功能，支持微信
	 * @param context
	 */
	public  void share(final Context context,String ShareText,final String imageUrl){
		createWXAPI(context, context.getResources().getString(R.string.weixin_key)) ;
		this.context = context ;
		//Log.d(TAG, "当前线程弹出："+Thread.currentThread() );
		SocializeConfig config = new SocializeConfig();
		
		CustomPlatform mWXPlatform = new CustomPlatform("微信", R.drawable.weixin_icon);
		addWxClickListener(context, mWXPlatform,ShareText,imageUrl,false);
		CustomPlatform mWXCircle = new CustomPlatform("朋友圈", R.drawable.wxcircel);
		addWxClickListener(context,mWXCircle,ShareText,imageUrl,true) ;
		
		config.addCustomPlatform(mWXPlatform) ;  //添加微信功能到友盟
		config.addCustomPlatform(mWXCircle);	//添加朋友圈功能到友盟
		 controller = UMServiceFactory
				.getUMSocialService("Android", RequestType.SOCIAL);
		//更新config
		controller.setConfig(config);
		//预设置分享内容
		if(ShareText!=null){
			controller
			.setShareContent(ShareText);
		}
		//设置图片
			if(imageUrl != null){
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						Bitmap bitmap = AsyncImageLoader.getBitmap(imageUrl) ;
						UIHandler.sendMessage(Message.obtain(UIHandler, 1, bitmap));
					}
				}).start() ;
				
		}
		//	if(ShareUtil.uMengID !=null){
				controller.openShare(context, false);
		//	}
		SnsPostListener mSnsListener = new SnsPostListener() {

			@Override
			public void onStart() {
				
				CommUtils.showMessage("开始分享", context) ;
			}

			@Override
			public void onComplete(SHARE_MEDIA arg0, int arg1,
					SocializeEntity arg2) {
				
				if(arg1==200){
				CommUtils.showMessage("分享成功", context) ;
				}else {
					String eMsg = "";
					if (arg1 == -101)
						eMsg = "没有授权";
					Toast.makeText(	context,
									"分享失败[" + arg1 + "] " + eMsg,
									Toast.LENGTH_SHORT).show();
				}

			}
		};
		controller.registerListener(mSnsListener);
	}
	/**
	 * 添加微信或朋友圈点击事件
	 * @param context
	 * @param customPlatform
	 * @param flag
	 */
	private void addWxClickListener(final Context context,
			CustomPlatform customPlatform,final String text,final String imageUrl,final boolean flag) {
		customPlatform.clickListener = new OnCustomPlatformClickListener() {  //微信分享按钮监听事件
			@Override
			public void onClick(CustomPlatform customPlatform,
								String shareContent,
								UMediaObject shareImage) {
				if(ShareUtil.wxAppID !=null){
				checkInstallwx(context);

/*	微信和朋友圈支持的图文分享－－－url分享，以后有 需求 可以用上		
 * final String des = "Weixin";
				//Toast.makeText(context,"静态变量是："+ShareUtil.wxAppID, 0).show() ;
				boolean sendReq = sendByWX(api, shareContent, shareImage, flag);
				if(sendReq){//发送分享统计信息给Umeng
					UMSocialService anaService = UMServiceFactory.getUMSocialService(des, RequestType.ANALYTICS);
					UMShareMsg shareMsg = new UMShareMsg();
					shareMsg.setMediaData(UMRichMedia.toUMRichMedia(shareImage));
					shareMsg.text = shareContent;
					anaService.postShareByCustomPlatform(context, null, "wxtimeline", shareMsg, null);
			}
 * 
 */
				wxShareText(text) ;
				
				if(imageUrl != null){
					wxShareImageThread(imageUrl);
				}
				if(text!=null && imageUrl!=null){
					wxShareImageThread(imageUrl);
				}

				}else{
					CommUtils.showMessage("你还没有注册微信，清先注册", context) ;
				}
			}

			


		};
	}
	/**
	 * 微信分享图片的线程 解决严格模式 不能在主线程中运行的错误
	 * @param imageUrl
	 */
	private void wxShareImageThread(final String imageUrl) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				wxShareImage(imageUrl) ;
			}
		}).start() ;
	}
	
	/**
	 * 分享内容和图片方法  包括微信和朋友圈
	 * @param api
	 * @param shareContent  分享内容
	 * @param shareImage    分享图片
	 * @param toCircle
	 * @return
	 * 这是一个url分享  可以图文分享  ， 以后可能会用上
	 */
	private  boolean sendByWX(final IWXAPI api, String shareContent,
			UMediaObject shareImage, boolean toCircle) {
		WXWebpageObject webpage = new WXWebpageObject();
		//为什么需要填写url? 当前使用的微信SDK不支持图文分享，使用图文分享必须转成URL分享，所以需要填写一个URL
		webpage.webpageUrl = "http://youbar.appmars.com";
		WXMediaMessage msg = new WXMediaMessage(webpage);
		//msg.title = "仙仙360";
		msg.description = shareContent;

		if (shareImage != null && shareImage.getMediaType() == MediaType.IMAGE) {
			byte[] b = shareImage.toByte();
			if (b != null) {
				Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
				Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE,
						THUMB_SIZE, true);
				bmp.recycle();
				msg.thumbData = Util.bmpToByteArray(thumbBmp, true); // 设置缩略图
			}
		}

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;
		req.scene = toCircle ? SendMessageToWX.Req.WXSceneTimeline
				: SendMessageToWX.Req.WXSceneSession;
		boolean sendReq = api.sendReq(req);
		return sendReq;
	}

	private static String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	/**
	 * 检查是否安装了微信应用
	 * @param context
	 */
	private static void checkInstallwx(final Context context) {
		if (!api.isWXAppInstalled()) {
			CommUtils.showMessage("你还没有安装微信", context) ;
			return;
		} else if (!api.isWXAppSupportAPI()) {
			CommUtils.showMessage("你安装的微信版本不支持当前API", context) ;

			return;
		}
	}
	/**
	 * 微信和朋友圈分享内容
	 * @param text
	 */
	private void wxShareText(String text){
		if (text == null || text.length() == 0) {

			return;
		}
		
		// 初始化一个WXTextObject对象
		WXTextObject textObj = new WXTextObject();
		textObj.text = text;

		// 用WXTextObject对象初始化一个WXMediaMessage对象
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		// 发送文本类型的消息时，title字段不起作用
		// msg.title = "Will be ignored";
		msg.description = text;

		// 构造一个Req
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
		req.message = msg;
		
		// 调用api接口发送数据到微信
		api.sendReq(req);
	}
	/**
	 * 微信和朋友圈分享图片
	 * @param imageUrl
	 */
	private void wxShareImage(String imageUrl){
		WXImageObject imgObj = new WXImageObject();
		imgObj.imageUrl = imageUrl;
		
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObj;

		Bitmap bmp;
		try {
			bmp = BitmapFactory.decodeStream(new URL(imageUrl).openStream());
			Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
			bmp.recycle();
			msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
			
			SendMessageToWX.Req req = new SendMessageToWX.Req();
			req.transaction = buildTransaction("img");
			req.message = msg;
			api.sendReq(req);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
