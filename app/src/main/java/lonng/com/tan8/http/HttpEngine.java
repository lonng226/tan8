package lonng.com.tan8.http;

import android.util.Log;

import com.nostra13.universalimageloader.utils.L;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpEngine
{
	public static String executeGet(String url)
	{
		String result = "";
		BufferedReader bufferedReader;
		try
		{
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			StringBuffer sBuffer = new StringBuffer("");
			String str = "123";
			while ((str = bufferedReader.readLine()) != null)
			{
				sBuffer.append(str);
			}
			result = sBuffer.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	public static String executePost(String url, Map<String, String> params)
	{
		String result = "";
		BufferedReader bufferedReader;
		@SuppressWarnings("deprecation")
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		for (String key : params.keySet())
		{
			list.add(new BasicNameValuePair(key, params.get(key)));
			Log.i("message", "key==" + key + ",value==" + params.get(key));
		}
		try
		{
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(list,"utf-8");
			httpPost.setEntity(formEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			String string2 = "";
			StringBuffer stringBuffer = new StringBuffer();
			while ((string2 = bufferedReader.readLine()) != null)
			{
				stringBuffer.append(string2);
			}
			result = stringBuffer.toString();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 提交参数里有文件的数据
	 * @param url 服务器地址
	 * @param param 参数
	 * @return 服务器返回结果
	 * @throws Exception
	 */
	//发送个人头像
	public static String uploadSubmit(String url, Map<String, String> param,Map<String,File> files) throws Exception {
		Log.i("tan8", "url:" +url);
		Log.i("tan8", "param:" +param.toString());
		if (files != null) {
			Log.i("tan8", "files:" +files.size());
		}
		HttpPost post = new HttpPost(url);
		HttpClient httpClient=new DefaultHttpClient();
		MultipartEntity entity = new MultipartEntity();
		if (param != null && !param.isEmpty()) {
			for (Map.Entry<String, String> entry : param.entrySet()) {
				if (entry.getValue() != null&& entry.getValue().trim().length() > 0) {
					entity.addPart(entry.getKey(),new StringBody(entry.getValue(),Charset.forName(org.apache.http.protocol.HTTP.UTF_8)));
				}
			}
		}
		// 添加文件参数
		if (files != null) {
			for (Map.Entry<String, File> entry : files.entrySet()) {
				if (entry.getValue() != null) {
					Log.i("tan8",entry.getKey()+"");
					entity.addPart(entry.getKey(), new FileBody(entry.getValue()));
				}
			}
		}
		post.setEntity(entity);
		HttpResponse response = httpClient.execute(post);
		int stateCode = response.getStatusLine().getStatusCode();
		StringBuffer sb = new StringBuffer();
		System.out.println("222");
		if (stateCode == HttpStatus.SC_OK) {
			System.out.println("333");
			HttpEntity result = response.getEntity();
			if (result != null) {
				InputStream is = result.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String tempLine;
				while ((tempLine = br.readLine()) != null) {
					sb.append(tempLine);
				}
			}
		}
		post.abort();
		return sb.toString();
	}

}
