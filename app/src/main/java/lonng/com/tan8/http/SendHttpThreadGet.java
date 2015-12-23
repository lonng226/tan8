package lonng.com.tan8.http;

import android.os.Handler;
import android.os.Message;

public class SendHttpThreadGet extends Thread {

//	private Activity ac;
	private Handler handler;
	private String urlparams;
	private int what;

	public SendHttpThreadGet( Handler handler, String urlparams,
			int what) {
		// TODO Auto-generated constructor stub
//		this.ac = ac;
		this.handler = handler;
		this.urlparams = urlparams;
		this.what = what;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			String result = HttpEngine.executeGet(urlparams);
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
