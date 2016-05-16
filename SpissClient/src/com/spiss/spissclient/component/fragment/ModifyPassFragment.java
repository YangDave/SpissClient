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
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.spiss.spissclient.R;
import com.spiss.spissclient.base.LoginCallBack;
import com.spiss.spissclient.base.OnPostSent;
import com.spiss.spissclient.http.HttpTaskUtil;
import com.spiss.spissclient.http.HttpTaskWithLoadUtil;

public class ModifyPassFragment extends Fragment implements OnClickListener{
	
	LoginCallBack loginCallBack = null;
	
	EditText usernameEt,passwordEt,emailEt;
	Button modifyBt;
	TextView toLoginBt,toRegisterBt;
	CheckBox passCb;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_modify_pass, container,false);
		
		initView(rootView);
		
		addListener();
		return rootView;
	}
	
	private void addListener() {
		
		modifyBt.setOnClickListener(this);
		toLoginBt.setOnClickListener(this);
		toRegisterBt.setOnClickListener(this);
		
		passCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if(isChecked){
					passwordEt.setInputType(InputType.TYPE_CLASS_TEXT);
				}
			}
		});
		
		
	}

	@Override
	public void onAttach(Activity activity) {

		loginCallBack = (LoginCallBack)activity;
		
		super.onAttach(activity);
	}
	
	private void initView(View v) {
		
		usernameEt = (EditText)v.findViewById(R.id.username);
		passwordEt = (EditText)v.findViewById(R.id.password);
		emailEt = (EditText)v.findViewById(R.id.email);
		modifyBt = (Button)v.findViewById(R.id.modify_pass);
		toLoginBt = (TextView)v.findViewById(R.id.to_login);
		toRegisterBt = (TextView)v.findViewById(R.id.to_register);
		passCb = (CheckBox)v.findViewById(R.id.passcb);

	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.modify_pass:
			
			if(validate()){
				modifyPass();
			}else{
				Toast.makeText(getActivity(), "输入不规范", Toast.LENGTH_LONG).show();
			}
			
			break;
		case R.id.to_login:
			
			loginCallBack.changeFragment(R.layout.fragment_login);
			break;
			
		case R.id.to_register:
			
			loginCallBack.changeFragment(R.layout.fragment_register);
			break;

		default:
			break;
		}
		
	}

	private boolean validate() {
		// TODO Auto-generated method stub
		return true;
	}

	@SuppressWarnings("unchecked")
	private void modifyPass() {
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("func", "modifyPass");
		map.put("username", usernameEt.getText().toString().trim());
		map.put("email", emailEt.getText().toString().trim());
		map.put("password",passwordEt.getText().toString().trim());
		
		HttpTaskUtil htu = new HttpTaskWithLoadUtil(getActivity(), "密码修改中", "请稍候。。。", new OnPostSent() {
			
			@Override
			public void onPostSuccess(Object obj) {
				
				JSONObject result = (JSONObject)obj;
					Toast.makeText(getActivity(), "密码修改成功", Toast.LENGTH_LONG).show();
					LoginFragment.loginSuccess(getActivity(), result);
				
			}
		});
		
		htu.execute(map);
		
	}

}
