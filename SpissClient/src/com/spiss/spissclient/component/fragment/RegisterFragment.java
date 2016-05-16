package com.spiss.spissclient.component.fragment;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.spiss.spissclient.R;
import com.spiss.spissclient.base.LoginCallBack;
import com.spiss.spissclient.base.OnPostSent;
import com.spiss.spissclient.constants.Config;
import com.spiss.spissclient.http.HttpTaskUtil;
import com.spiss.spissclient.http.HttpTaskWithLoadUtil;


public class RegisterFragment extends Fragment implements OnClickListener{

	EditText muserEditText,mEmailText;
	EditText mpwdEditText;
	Button registerBt;
	TextView toLoginBt;
	CheckBox passCb;


	LoginCallBack loginCallBack = null;


	String username;

	public RegisterFragment(){

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try{
			loginCallBack = (LoginCallBack)activity;
		}catch(ClassCastException e){
			throw new ClassCastException("activity must implement LoginCallBack!");
		}

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_register, container, false);

		initView(rootView);


		addListener();

		return rootView;
	}

	private void addListener() {
		registerBt.setOnClickListener(this);
		toLoginBt.setOnClickListener(this);
		
		passCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if(isChecked){
					mpwdEditText.setInputType(InputType.TYPE_CLASS_TEXT);
				}
			}
		});
		
	}

	public void initView(View rootView) {
		registerBt = (Button)rootView.findViewById(R.id.register);
		toLoginBt = (TextView)rootView.findViewById(R.id.to_login);
		muserEditText = (EditText) rootView.findViewById(R.id.username);
		mpwdEditText = (EditText)rootView.findViewById(R.id.password);
		mEmailText = (EditText)rootView.findViewById(R.id.email);
		passCb = (CheckBox)rootView.findViewById(R.id.passcb);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.register:

			register();

			break;
		case R.id.to_login:

			loginCallBack.changeFragment(R.layout.fragment_login);
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private void register(){
		String username = muserEditText.getText().toString().trim();
		String password = mpwdEditText.getText().toString().trim();
		String email = mEmailText.getText().toString().trim();

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("username", username);
		map.put("password", password);
		map.put("email", email);
		map.put("func", "register");

		HttpTaskUtil htu = new HttpTaskWithLoadUtil(getActivity(), Config.URL, new OnPostSent() {

			@Override
			public void onPostSuccess(Object obj) {

				JSONObject result = (JSONObject)obj;
					LoginFragment.loginSuccess(getActivity(), result);

			}

		});

		htu.execute(map);

	}

}
