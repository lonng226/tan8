package lonng.com.tan8.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import lonng.com.tan8.R;
import lonng.com.tan8.application.TanApplication;

/**
 * Created by Administrator on 2016/6/7.
 */
public class ImageService {

    public static Bitmap getImage(Context context ,String path) throws IOException {
        Log.i("tan8","imagepath:"+path);

        if (path.equals(CommonUtils.GET_FILS)){
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        }

        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");   //设置请求方法为GET
        conn.setReadTimeout(5*1000);    //设置请求过时时间为5秒
        InputStream inputStream = conn.getInputStream();   //通过输入流获得图片数据
        byte[] data = readInputStream(inputStream);     //获得图片的二进制数据

        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);  //生成位图
        return bitmap;

    }

    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();

    }

}
