package lonng.com.tan8.application;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;

import lonng.com.tan8.Entity.User;
import lonng.com.tan8.R;

public class TanApplication extends Application {

	// 默认存放图片的路径
	public final static String DEFAULT_SAVE_IMAGE_PATH = Environment.getExternalStorageDirectory() + File.separator + "tan8" + File.separator + "Images"
			+ File.separator;

	public static int mKeyBoardH = 0;

	private static Context mContext;
	//是否是登录状态
	public static boolean isLogin;

	public static User curUser = new User();



	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		initImageLoader();
	}

	public static Context getContext(){
		return mContext;
	}

	/** 初始化imageLoader */
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

}
