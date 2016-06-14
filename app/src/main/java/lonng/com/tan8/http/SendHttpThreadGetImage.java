package lonng.com.tan8.http;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import lonng.com.tan8.utils.ImageService;

public class SendHttpThreadGetImage extends Thread {

	private Context ac;
	private Handler handler;
	private String urlparams;
	private int what;

	public SendHttpThreadGetImage(Context ac,Handler handler, String urlparams,
								  int what) {
		// TODO Auto-generated constructor stub
		this.ac = ac;
		this.handler = handler;
		this.urlparams = urlparams;
		this.what = what;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {


			Bitmap result = ImageService.getImage(ac,urlparams);
			Log.i("tan8","url"+urlparams);
			Message ms = Message.obtain();
			ms.what = what;
			ms.obj = result;
			if (handler != null) {
				handler.sendMessage(ms);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
