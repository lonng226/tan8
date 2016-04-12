package lonng.com.tan8.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.DecimalFormat;

import lonng.com.tan8.BankActivity;
import lonng.com.tan8.MainActivity;
import lonng.com.tan8.page.Czxpage;

public class CommonUtils {


	//首选项中的key
	//用户id
	public static String UID = "uid";
	//账号
	public static String ACCOUNT = "account";
	//密码
	public static String PWD = "pwd";

	//HOST
	public static String HTTPHOST = "http://120.24.16.24/tanqin/forum.php";
	//发送帖子的url
	public static String POST_SENDIVTATION = "http://120.24.16.24/tanqin/forum.php";

	//获取帖子列表
	public static String GET_INVATATIONLIST = "http://120.24.16.24/tanqin/forum.php";

	//获取file，图片，视频
	public static String GET_FILS = "http://120.24.16.24/tanqin";

	//注册
	public static String REGISTERURL = "http://120.24.16.24/tanqin/user.php?action=register";

	//登录
	public static String LOGINURL = "http://120.24.16.24/tanqin/user.php?action=login";

	//点赞
	public static String NEWUP = "http://120.24.16.24/tanqin/forum.php?action=newup";

    //评论
	public static String NEWCOMMENT = "http://120.24.16.24/tanqin/forum.php?action=newcomment";

	//设置头像
	public static String HEADICON = "http://120.24.16.24/tanqin/user.php?action=createheadportrait";

	//删除帖子
	public static String DELTETIE = "http://120.24.16.24/tanqin/forumtest.php";
	
	/**
	 * 检测网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable() && mNetworkInfo.isConnected();
			}
		}

		return false;
	}
	
	/**
	 * 检测Sdcard是否存在
	 * 
	 * @return
	 */
	public static boolean isExitsSdcard() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}


	public static String getPath(){
		return Environment.getExternalStorageDirectory().getPath()+"/community";
	}

	public  static String timeToString(int mss){

		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (mss % (1000 * 60)) / 1000;

		StringBuffer sb = new StringBuffer();
		if (hours > 0){
			sb.append(hours+":");
		}
		if (minutes >= 10){
			sb.append(minutes +":");
		}else if (minutes > 0 && minutes < 10){
			sb.append("0"+minutes+":");
		}else{
			sb.append("00:");
		}

		if (seconds >= 10){
			sb.append(seconds+"");
		}else if (seconds <10 && seconds >0){
			sb.append("0"+seconds);
		}else{
			sb.append("00");
		}

		return  sb.toString();
	}

	public static String sizeToString(int size){
		float size_ = Float.valueOf(size+"") /1024/1024;
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
		return df.format(size_)+"M";
	}

	public static void showSoftInput(Context context, View view,int isActivity){

		if(isActivity ==0){
			((MainActivity)context).getMenuBarView().setVisibility(View.GONE);
		}else if(isActivity == 1){
			((BankActivity)context).getRe().setVisibility(View.GONE);
		}
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		//imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}

	public static void hideSoftInput(Context context, View view,int isActivity){
		if (isActivity == 0){
			((MainActivity)context).getMenuBarView().setVisibility(View.VISIBLE);
		}else if(isActivity == 1) {
			((BankActivity)context).getRe().setVisibility(View.VISIBLE);
		}
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
	}

	public static boolean isShowSoftInput(Context context){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		//获取状态信息
		return imm.isActive();//true 打开
	}



}
