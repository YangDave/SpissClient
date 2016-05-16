package com.spiss.spissclient.component.fragment;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.Type;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.spiss.spissclient.R;
import com.spiss.spissclient.application.MyApplication;
import com.spiss.spissclient.base.LoginCallBack;
import com.spiss.spissclient.base.OnPostSent;
import com.spiss.spissclient.component.activity.MainActivity;
import com.spiss.spissclient.constants.Config;
import com.spiss.spissclient.http.HttpTaskUtil;
import com.spiss.spissclient.http.HttpTaskWithLoadUtil;
import com.spiss.spissclient.utils.InputStyleValidate;

public class LoginFragment extends Fragment{

	EditText muserEditText;
	EditText mpwdEditText;
	Button mLoginButton;
	Button faceLoginButton;
	TextView toRegisterText,toModifyPassText;
	ImageView imageView;
	CheckBox passCb;


	SharedPreferences preference;
	SharedPreferences.Editor editor;

	LoginCallBack loginCallBack = null;

	String username;
	int userId;

	public LoginFragment(){

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try{
			loginCallBack = (LoginCallBack)activity;
		}catch(ClassCastException e){
			throw new ClassCastException("activity must implement MyCallBack!");
		}
		
	}




	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_login, container, false);
		
		initView(rootView);

		addListener();
		
		
		return rootView;
	}

	private void addListener() {
		mLoginButton.setOnClickListener(new OnClickListener() {

			/* (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View arg0) {
				if(InputStyleValidate.isUserNameOrPwdFormat(muserEditText.getText().toString())&&
						InputStyleValidate.isUserNameOrPwdFormat(mpwdEditText.getText().toString())){

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("username", muserEditText.getText().toString().trim());
					map.put("password", mpwdEditText.getText().toString().trim());
					map.put("func", "login");
					
					HttpTaskUtil task = new HttpTaskWithLoadUtil(getActivity(), Config.URL, new OnPostSent() {
						
						@Override
						public void onPostSuccess(Object obj) {
							
							JSONObject result = (JSONObject)obj;
								loginSuccess(getActivity(),result);
						}
					});

					task.execute(map);

				}

				else { Toast.makeText(getActivity(), "请检查输入是否有误", Toast.LENGTH_LONG).show();

				}

			}
		});
		
		toRegisterText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				loginCallBack.changeFragment(R.layout.fragment_register);
				
			}
		});
		toModifyPassText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loginCallBack.changeFragment(R.layout.fragment_modify_pass);
			}
		});
		
		faceLoginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

//				Intent intent = new Intent(getActivity(),FaceLoginActivity.class);
//				startActivity(intent);
				loginCallBack.changeFragment(R.layout.fragment_face_login);
			}
		});
		
		passCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if(isChecked){
					mpwdEditText.setInputType(InputType.TYPE_CLASS_TEXT);
				}
			}
		});
		
		
	}
	
	/**
	 * 保存用户信息至application
	 * 保存用户信息至sharepreference，以便下次登录
	 * 执行跳转
	 * @param result
	 */
	public static void loginSuccess(Context context, JSONObject result) {
		Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
		String user_id = String.valueOf(result.optInt("user_id"));
		String username = result.optString("user_name");
		String userm_id = String.valueOf(result.optInt("userm_id"));
		MyApplication ma = (MyApplication)context.getApplicationContext();
		
		ma.setUser_id(String.valueOf(user_id));
		ma.setUsername(username);
		ma.setUserm_id(userm_id);
		SharedPreferences preference;
		SharedPreferences.Editor editor;
		
		preference =context.getSharedPreferences("LastLoginRecord", context.MODE_APPEND);
		editor = preference.edit();
		editor.putString("user_id", user_id);
		editor.putString("username", username);
		editor.putString("userm_id", userm_id);
		editor.commit();
		
		Intent  intent = new Intent(context,MainActivity.class);
		context.startActivity(intent);
		
	}

	private void initView(View v){
		muserEditText = (EditText) v.findViewById(R.id.usernameEdit);
		mpwdEditText = (EditText) v.findViewById(R.id.passwordEdit);
		mLoginButton = (Button) v.findViewById(R.id.loginButton);
		toRegisterText = (TextView)v.findViewById(R.id.toRegisterButton);
		toModifyPassText = (TextView)v.findViewById(R.id.forgetPass);
		faceLoginButton = (Button)v.findViewById(R.id.face_login);
		passCb = (CheckBox)v.findViewById(R.id.passcb);
	}

}
