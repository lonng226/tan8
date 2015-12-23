package lonng.com.tan8.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import java.text.DecimalFormat;

public class CommonUtils {


	//发送帖子的url
	public static String SENDIVTATION = "http://120.24.16.24/tanqin/forum.php";

	//获取帖子列表
	public static String GETLIST_INVATATION = "http://120.24.16.24/tanqin/forum.php";


	
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
	
}
