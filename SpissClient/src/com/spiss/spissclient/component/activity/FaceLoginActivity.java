package com.spiss.spissclient.component.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.spiss.spissclient.R;
import com.spiss.spissclient.application.ExitApplication;


public class FaceLoginActivity extends Activity {

	private Button button1;
	private Button button_rebuilt;
	private String username;
	private Thread thread;
	EditText userNameEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_face_login);
		
		ExitApplication.getInstance().addActivity(this);

		button1 = (Button)findViewById(R.id.button1);
		userNameEdit = (EditText)findViewById(R.id.user_name_edit);
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				username = userNameEdit.getText().toString().trim();
				if(username.equals("")){
					Toast.makeText(FaceLoginActivity.this, "请输入用户名", Toast.LENGTH_LONG).show();
				}else{
					Intent intent = new Intent(FaceLoginActivity.this, FaceCameraActivity.class);
					intent.putExtra("user", username);
					startActivity(intent);
				}
			}
		});
		
		Intent intent = getIntent();
		username = intent.getStringExtra("username");
		if(username != null){
			userNameEdit.setText(username);
		}

		button_rebuilt = (Button)findViewById(R.id.button_1);
		button_rebuilt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				username = userNameEdit.getText().toString().trim();
				if(username.equals("")){
					Toast.makeText(FaceLoginActivity.this, "请输入用户名", Toast.LENGTH_LONG).show();
				}else{
					thread = new Thread(runnable);
					thread.start();
				}
			}
		});
	}

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			JSONObject result = null;
			HttpRequests httpRequests = new HttpRequests("270ff76b7c212533a3079aa0be82a3c5","hsWv6TPO6nc_PteGNxAPMwihBEBQQpYz");
			try {
				result = httpRequests.personDelete(new PostParameters().setPersonName(username));
				try {
					if(result.getBoolean("success")){
						FaceLoginActivity.this.runOnUiThread(new Runnable() {

							public void run() {
								Toast.makeText(getApplication(), "删除成功，请重新建模", Toast.LENGTH_LONG).show();
							}
						});
					}
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


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
