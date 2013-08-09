package com.youdao.dev.wxapi;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.youdao.dev.R;
import com.youdao.dev.plugin.Share;


/**
 * 微信分享成功与否的回调     可以在分享成功以后干一些事情
 * @author fengxue
 *
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
	
    private IWXAPI api;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        String wxAppID = this.getResources().getString(R.string.weixin_key);
		if (wxAppID != null && !wxAppID.equals("") && !wxAppID.equals("wxkey")) {
			api = WXAPIFactory.createWXAPI(this, wxAppID);
			api.registerApp(wxAppID);
		}

        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
		//	Toast.makeText(WXEntryActivity.this, "ii", 0).show() ;
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
		//	Toast.makeText(WXEntryActivity.this, "hh", 0).show() ;
			break;
		default:
			break;
		}
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK://分享成功 回调
			Share.callbackContext.success("success") ;
			
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL://进入分享界面，没有分享，退出时调用
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			break;
		default:
			break;
		}
		
		this.finish();  
	}
	
}