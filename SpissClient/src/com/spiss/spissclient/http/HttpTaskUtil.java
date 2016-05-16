package com.spiss.spissclient.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.spiss.spissclient.base.OnPostInterface;
import com.spiss.spissclient.base.OnPostSent;
import com.spiss.spissclient.constants.Config;

public class HttpTaskUtil extends AsyncTask<Map<String,Object>, Integer, JSONObject> {
	
	protected String url = Config.URL;
	protected Context mContext;
	protected JSONObject ret = null;
    protected OnPostSent onPostSent;
    

    
	public HttpTaskUtil(Context mContext,String url,OnPostSent onPostSent){
		
		this.mContext = mContext;
		this.url = url;
		this.onPostSent = onPostSent;
	}
	
	public HttpTaskUtil(Context mContext, OnPostSent onPostSent) {
		super();
		this.mContext = mContext;
		this.onPostSent = onPostSent;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}



	@Override
	protected JSONObject doInBackground(Map<String, Object>... params) {
		
		MyHttpClient cl = MyHttpClient.getInstance();

		List<NameValuePair> list = new ArrayList<NameValuePair>();

		JSONObject json = new JSONObject(params[0]);

		NameValuePair pa = new BasicNameValuePair("json", json.toString());
		list.add(pa);

		try {

			cl.post(url, list,
					new JSONResponseHandler<JSONObject>() {

				@Override
				public JSONObject handlerJson(JSONObject retJson) {
					
					Log.v("return"," "+retJson.toString());
					ret = retJson;
					return retJson;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	@SuppressWarnings("null")
	@Override
	protected void onPostExecute(JSONObject json) {
		super.onPostExecute(json);
		
		if(json == null){
			
			Toast.makeText(mContext, "无法连接到网络", Toast.LENGTH_LONG).show();
			return;
			
		}else{
			int code = json.optInt("code");
			Object result = json.opt("result");
			if(code == 1){
				onPostSent.onPostSuccess(result);
				
			}else{
				JSONObject obj = (JSONObject)result;
				Toast.makeText(mContext, obj.optString("message"), Toast.LENGTH_LONG).show();
				
			}
		}
		
	}


}
