package com.spiss.spissclient.http;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

/**
 * @author yuandunlong
 *
 */
public class MyHttpClient {

	private DefaultHttpClient client = null;

	private static MyHttpClient instance = null;

	private MyHttpClient() {
		init();

	}

	private void init() {

		try {
			
			client=new DefaultHttpClient();
			HttpParams params = new BasicHttpParams();

			HttpProtocolParams.setContentCharset(params, "UTF-8");
			HttpConnectionParams.setStaleCheckingEnabled(params, false);
			HttpConnectionParams.setConnectionTimeout(params, 30 * 1000);
			HttpConnectionParams.setSoTimeout(params, 30 * 1000);
			HttpConnectionParams.setSocketBufferSize(params, 8192);
			HttpClientParams.setRedirecting(params, true);
			HttpClientParams.setCookiePolicy(params,
					CookiePolicy.BROWSER_COMPATIBILITY);
			SchemeRegistry sr = new SchemeRegistry();
			sr.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

			keyStore.load(null, null);
		} catch (Exception e) {

		}

	}

	public static MyHttpClient getInstance() {

		if (instance == null) {

			synchronized (MyHttpClient.class) {

				instance = new MyHttpClient();

			}
		}

		return instance;
	}
	
	public InputStream get(String url) throws Exception{
		
		
		
		HttpGet get=new HttpGet(url);
		HttpResponse hr=client.execute(get);
		
		if(hr.getStatusLine().getStatusCode()==200){
			
			return hr.getEntity().getContent();
			
			
		}else{
			
			
			return null;
		}
		
		
	}
	
	public HttpEntity post(String url , List<NameValuePair> params)throws Exception{
		
		HttpPost post=new HttpPost(url);
		post.setEntity(getEncodedForm(params));
		HttpResponse hr= client.execute(post);
		HttpEntity ret=null;
		if(hr.getStatusLine().getStatusCode()==200){
			ret=hr.getEntity();
			
		}
		
		
		
		return ret;
	}

	public <T> T post(String url, List<NameValuePair> params,
			ResponseHandler<T> handler) throws Exception {

		T t = null;
		HttpPost post = new HttpPost(url);
		post.setEntity(getEncodedForm(params));
		t = client.execute(post, handler);
		return t;

	}

	public <T> T post(String url, MultipartEntity entity,
			ResponseHandler<T> handler) throws Exception {

		T t = null;

		HttpPost post = new HttpPost(url);

		post.setEntity(entity);
		client.execute(post, handler);

		return t;

	}

	private UrlEncodedFormEntity getEncodedForm(List<NameValuePair> params) {

		UrlEncodedFormEntity ret = null;
		try {
			ret = new UrlEncodedFormEntity(params, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// no exception
		}
		return ret;

	}

}
