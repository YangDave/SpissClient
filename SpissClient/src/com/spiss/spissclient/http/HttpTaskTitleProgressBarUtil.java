package com.spiss.spissclient.http;

import org.apache.http.impl.client.RequestWrapper;

import android.content.Context;

import com.spiss.spissclient.base.OnPostSent;

public class HttpTaskTitleProgressBarUtil extends HttpTaskUtil{

	public HttpTaskTitleProgressBarUtil(Context mContext, OnPostSent onPostSent) {
		super(mContext, onPostSent);
	}

}
