package com.youdao.dev.utils;


import com.youdao.dev.R;


import android.app.ProgressDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class CommUtils {
	/**
	 * 显示消息
	 * @param toastContent
	 * @param context
	 */
	public static void showMessage(String toastContent,Context context){
		LayoutInflater inflater =  LayoutInflater.from(context) ;
		// 根据指定的布局文件创建一个具有层级关系的View对象
		View layout = inflater.inflate(R.layout.toast, null);
		LinearLayout root = (LinearLayout) layout
				.findViewById(R.id.toast_layout_root);
		root.getBackground().setAlpha(120);// 0~255透明度值
		
		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(toastContent);
	
		Toast toast = new Toast(context);
		// 设置Toast的位置
		// toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		// toast.setGravity(Gravity.BOTTOM, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		// 让Toast显示为我们自定义的样子
		toast.setView(layout);
		toast.show();
	}


	public static ProgressDialog progressDialog;

	public static void showProgressDialog(Context context,String title,String message) {
		progressDialog = new ProgressDialog(context);
		progressDialog.setTitle(title);  //上传头像
		progressDialog.setMessage(message); //正在上传头像，请稍候...
		progressDialog.show();
	}

	public static void dissDialog() {
		if(progressDialog !=null && progressDialog.isShowing()){
			progressDialog.dismiss();
		}
	}

}
