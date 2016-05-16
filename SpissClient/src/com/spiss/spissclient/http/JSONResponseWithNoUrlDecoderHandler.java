package com.spiss.spissclient.http;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author yuandunlong
 * @createAt 2014-3-28
 * @param <T>
 */
public abstract class JSONResponseWithNoUrlDecoderHandler <T> implements ResponseHandler<T> {

	@Override
	public T handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		JSONObject jsobj = null;
		int status = response.getStatusLine().getStatusCode();
          
		HttpEntity entity = response.getEntity();
		if (entity == null) {
			throw new ClientProtocolException("Response contains no content");
		}
		if (status >= 300) {
			throw new HttpResponseException(response.getStatusLine()
					.getStatusCode(), response.getStatusLine()
					.getReasonPhrase());
		}
		// this method will auto check the entity encoding if not find will user
		// default encoding utf-8
		String ret = EntityUtils.toString(entity, "UTF-8");
		try {
     		jsobj = new JSONObject(ret);
		} catch (JSONException e) {
			return null;
		}
		return handlerJson(jsobj);

	}

	/**
	 * 
	 * @param json
	 * @return must be implemnted in subclass
	 */
	public abstract T handlerJson(JSONObject json);

}


