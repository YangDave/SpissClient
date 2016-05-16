package com.spiss.spissclient.http;

import org.json.JSONObject;

import com.spiss.spissclient.base.OnPostSent;

import android.app.ProgressDialog;
import android.content.Context;

public class HttpTaskWithLoadUtil extends HttpTaskUtil{
	
	protected String title = "正在加载";
	protected String content = "请稍候";
	protected ProgressDialog proDialog;
	

	public HttpTaskWithLoadUtil(Context mContext, String url,
			OnPostSent onPostSent) {
		super(mContext, url, onPostSent);
	}
	
	public HttpTaskWithLoadUtil(Context mContext,OnPostSent onPostSent){
		super(mContext,onPostSent);
	}
	
	public HttpTaskWithLoadUtil(Context mContext,String title,String content,OnPostSent onPostSent){
		this(mContext,onPostSent);
		this.title = title;
		this.content = content;
		
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		proDialog = ProgressDialog.show(mContext,title, content, true, true);
	}
	@Override
	protected void onPostExecute(JSONObject json) {
		proDialog.dismiss();
		super.onPostExecute(json);
	}
	@Override
	protected void onCancelled(JSONObject result) {
		super.onCancelled(result);
		proDialog.cancel();
	}
	
	
	
	
	
	
	
	
	

}
