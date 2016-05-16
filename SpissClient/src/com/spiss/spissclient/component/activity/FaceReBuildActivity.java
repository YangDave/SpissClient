package com.spiss.spissclient.component.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.spiss.spissclient.R;
import com.spiss.spissclient.base.MyActivity;

public class FaceReBuildActivity extends MyActivity{
	
	Button rebuildBt,startBuildBt;
	private Thread thread;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_face_rebuild);
		
		initView();
		addListener();
	}

	private void addListener() {

		rebuildBt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				thread = new Thread(runnable);
				thread.start();
			}
		});
		
		startBuildBt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(FaceReBuildActivity.this, FaceCameraActivity.class);
				intent.putExtra("user", username);
				startActivity(intent);
			}
		});
	}

	private void initView() {

		rebuildBt = (Button)findViewById(R.id.rebuildBt);
		startBuildBt = (Button)findViewById(R.id.startReBuild);
		
	}
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			startBuildBt.setEnabled(true);
			super.handleMessage(msg);
		}
		
	};
	
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			JSONObject result = null;
			HttpRequests httpRequests = new HttpRequests("270ff76b7c212533a3079aa0be82a3c5","hsWv6TPO6nc_PteGNxAPMwihBEBQQpYz");
			try {
				Log.v("username", "username :"+username);
				result = httpRequests.personDelete(new PostParameters().setPersonName(username));
				try {
					if(result.getBoolean("success")){
						FaceReBuildActivity.this.runOnUiThread(new Runnable() {

							public void run() {
								Toast.makeText(getApplication(), "删除成功，请重新建模", Toast.LENGTH_LONG).show();
								
							}
						});
					}
					handler.sendEmptyMessage(0);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FaceppParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};
	

}
