package lonng.com.tan8.http;

import android.os.Handler;
import android.os.Message;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Created by Administrator on 2016/4/12.
 */
public class SendHttpDelete extends Thread {


    private Handler handler;
    private String urlparams;
    private int what;


    public SendHttpDelete(Handler handler,String urlparams,int what) {
        this.handler = handler;
        this.urlparams = urlparams;
        this.what = what;
    }


    @Override
    public void run() {
        super.run();

        try {

            String result = sendHttpDelete(urlparams);
            Message ms = Message.obtain();
            ms.what = what;
            ms.obj = result;
            if (handler != null) {
                handler.sendMessage(ms);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private String sendHttpDelete(String url) {
        BufferedReader bufferedReader;
        String result = "";


        HttpClient httpclient = new DefaultHttpClient();


        HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(url);


// json 处理

        httpDelete.setHeader("Content-Type", "application/json; charset=UTF-8");//or addHeader();

        httpDelete.setHeader("X-Requested-With", "XMLHttpRequest");

//设置HttpDelete的请求参数


//        httpDelete.setEntity(new StringEntity("sdd", Charset.forName(org.apache.http.protocol.HTTP.UTF_8)));

        httpDelete.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);

        httpDelete.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);

        try {

            HttpResponse response = httpclient.execute(httpDelete);
            bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String string2 = "";
            StringBuffer stringBuffer = new StringBuffer();
            while ((string2 = bufferedReader.readLine()) != null)
            {
                stringBuffer.append(string2);
            }
            result = stringBuffer.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


}
