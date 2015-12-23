package lonng.com.tan8.http;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.util.List;
import java.util.Map;

public class SendHttpThreadMime extends Thread {

	private Activity ac;
	private Handler handler;
	private Map<String, String> urlparams;
	private int what;
	private String url;
	private Map<String,File> files;

	public SendHttpThreadMime(String url,Activity ac, Handler handler, Map<String, String> urlparams,
			int what,Map<String,File> files) {
		// TODO Auto-generated constructor stub
		this.ac = ac;
		this.handler = handler;
		this.urlparams = urlparams;
		this.what = what;
		this.url = url;
		this.files = files;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			String result = HttpEngine.uploadSubmit(url, urlparams, files);
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
