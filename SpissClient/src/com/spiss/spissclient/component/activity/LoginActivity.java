package com.spiss.spissclient.component.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.spiss.spissclient.R;
import com.spiss.spissclient.application.ExitApplication;
import com.spiss.spissclient.application.MyApplication;
import com.spiss.spissclient.base.LoginCallBack;
import com.spiss.spissclient.component.fragment.FaceLoginFragment;
import com.spiss.spissclient.component.fragment.LoginFragment;
import com.spiss.spissclient.component.fragment.ModifyPassFragment;
import com.spiss.spissclient.component.fragment.RegisterFragment;
import com.spiss.spissclient.utils.DeviceUtil;

public class LoginActivity extends Activity implements LoginCallBack{

	SharedPreferences sp;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DeviceUtil.bePortrait(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题栏
		
		ExitApplication.getInstance().addActivity(this);

		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
				WindowManager.LayoutParams. FLAG_FULLSCREEN);//全屏
		setContentView(R.layout.activity_login);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
			.add(R.id.container, new LoginFragment()).commit();
		}

		sp=getSharedPreferences("LastLoginRecord", MODE_PRIVATE);
//		String user_id = sp.getString("user_id", null);
//
//		if(user_id !=null){
//			String username=sp.getString("username", null);
//			MyApplication ma = (MyApplication)getApplicationContext();
//			ma.setUser_id(user_id);
//			ma.setUsername(username);
//			ma.setUserm_id(sp.getString("userm_id", null));
//
//			Intent intent = new Intent(this,MainActivity.class);
//			startActivity(intent);
//			this.finish();
//		}

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



	@Override
	public void changeFragment(int layoutId) {

		FragmentTransaction ft =  getFragmentManager().beginTransaction();
		switch (layoutId) {
		case R.layout.fragment_login:
			ft.replace(R.id.container, new LoginFragment()).addToBackStack(null).commit();
			break;

		case R.layout.fragment_register:
			ft.replace(R.id.container, new RegisterFragment()).addToBackStack(null).commit();
			break;

		case R.layout.fragment_modify_pass:
			ft.replace(R.id.container, new ModifyPassFragment()).addToBackStack(null).commit();
			break;
		case R.layout.fragment_face_login:
			ft.replace(R.id.container, new FaceLoginFragment()).addToBackStack(null).commit();

		default:
			break;
		}


	}}
