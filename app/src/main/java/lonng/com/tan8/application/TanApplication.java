package lonng.com.tan8.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import lonng.com.tan8.Entity.User;
import lonng.com.tan8.LoginActivity;
import lonng.com.tan8.MainActivity;
import lonng.com.tan8.R;
import lonng.com.tan8.http.SendHttpThreadMime;
import lonng.com.tan8.utils.CommonUtils;
import lonng.com.tan8.utils.SharePrefUtil;

public class TanApplication extends Application {

	// 默认存放图片的路径
	public final static String DEFAULT_SAVE_IMAGE_PATH = Environment.getExternalStorageDirectory() + File.separator + "tan8" + File.separator + "Images"
			+ File.separator;

	public static int mKeyBoardH = 0;
	public static int mKeyBoardH2 = 0;

	private static Context mContext;
	//是否是登录状态
	public static boolean isLogin;

	public static User curUser = new User();

	public static int SCREENWITH = 0;
	public static int SCREENHEIGHT = 0;

	@Override
	public void onCreate() {
		super.onCreate();
//		login();
		mContext = getApplicationContext();
		initImageLoader();


	}

	public static Context getContext() {
		return mContext;
	}

	/**
	 * 初始化imageLoader
	 */
	private void initImageLoader() {
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.color.bg_no_photo)
				.showImageOnFail(R.color.bg_no_photo).showImageOnLoading(R.color.bg_no_photo).cacheInMemory(true)
				.cacheOnDisk(true).build();

		File cacheDir = new File(DEFAULT_SAVE_IMAGE_PATH);
		ImageLoaderConfiguration imageconfig = new ImageLoaderConfiguration.Builder(this)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheSize(50 * 1024 * 1024)
				.diskCacheFileCount(200)
				.diskCache(new UnlimitedDiskCache(cacheDir))
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.defaultDisplayImageOptions(options).build();

		ImageLoader.getInstance().init(imageconfig);
	}


	private void login() {

		Map<String, String> map = new HashMap<String, String>();
		String uid_ = SharePrefUtil.getString(this,CommonUtils.UID,"-1");
		if (uid_.equals("-1")){
			return ;
		}
		map.put("uname",SharePrefUtil.getString(this,CommonUtils.ACCOUNT,"-1"));
		map.put("userpassword",SharePrefUtil.getString(this,CommonUtils.PWD,"-1"));

		new SendHttpThreadMime(CommonUtils.LOGINURL, null, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				String result = (String) msg.obj;
				Log.i("tan8","login:"+result);
				if (result == null || result.equals("")||!result.contains("uid")) {
					return;
				}
				String uid = "",uname = "",headiconUrl="";
				try {
					JSONObject json = new JSONObject(result);
					if (json.has("uid")){
						uid = json.getString("uid");
						if (uid.equals("-1")){
							Toast.makeText(TanApplication.this, "登录失败", Toast.LENGTH_SHORT).show();
							return;
						}
					}
					if (json.has("uname")){
						uname = json.getString("uname");
					}
					if (json.has("uprofile")){
						if(!json.getString("uprofile").equals("")){
							headiconUrl = json.getString("uprofile");
						}
					}

					//存入首选项
					TanApplication.isLogin = true;
					TanApplication.curUser .setUserId(uid);
					TanApplication.curUser.setUserNickname(uname);
					TanApplication.curUser.setHeadiconUrl(headiconUrl);


				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, map, 0, null).start();

	}
}
