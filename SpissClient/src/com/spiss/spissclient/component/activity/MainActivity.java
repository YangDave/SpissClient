package com.spiss.spissclient.component.activity;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.spiss.spissclient.R;
import com.spiss.spissclient.application.ExitApplication;
import com.spiss.spissclient.application.MyApplication;
import com.spiss.spissclient.component.fragment.ForumFragment;
import com.spiss.spissclient.component.fragment.Fragment2;
import com.spiss.spissclient.component.fragment.AlarmFragment;
import com.spiss.spissclient.component.fragment.MoreFragment;
import com.spiss.spissclient.utils.DeviceUtil;


public class MainActivity extends FragmentActivity
 {
	
	private FragmentTabHost mTabHost;
	private RadioGroup mTabRg;

	@SuppressWarnings("rawtypes")
	private final Class[] fragments = { ForumFragment.class, Fragment2.class,
			AlarmFragment.class, MoreFragment.class };
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		DeviceUtil.bePortrait(MainActivity.this);
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		ExitApplication.getInstance().addActivity(this);

		findView();
		
		
		
		
	}
	
	public void findView() {
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(MainActivity.this, getSupportFragmentManager(), R.id.realtabcontent);
		// 寰楀埌fragment鐨勪釜鏁�
		int count = fragments.length;
		for (int i = 0; i < count; i++) {
			// 涓烘瘡涓�釜Tab鎸夐挳璁剧疆鍥炬爣銆佹枃瀛楀拰鍐呭
			TabSpec tabSpec = mTabHost.newTabSpec(i + "").setIndicator(i + "");
			// 灏員ab鎸夐挳娣诲姞杩汿ab閫夐」鍗′腑
			mTabHost.addTab(tabSpec, fragments[i], null);
		}

		
		mTabRg = (RadioGroup) findViewById(R.id.tab_rg_menu);
		mTabRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				MyApplication ma = (MyApplication)getApplicationContext();
				switch (checkedId) {
				case R.id.tab_rb_1:
					mTabHost.setCurrentTab(0);
					ma.setFragIndex(0);
					break;
				case R.id.tab_rb_2:
					mTabHost.setCurrentTab(1);
					ma.setFragIndex(1);
					break;
				case R.id.tab_rb_3:
					mTabHost.setCurrentTab(2);
					ma.setFragIndex(2);
					break;
				case R.id.tab_rb_4:
					mTabHost.setCurrentTab(3);
					ma.setFragIndex(3);
					break;

				default:
					break;
				}
			}
		});
		

		mTabHost.setCurrentTab(0);
	}
	

	

	
/*
 * (non-Javadoc)
 * @see android.app.Activity#onRestart()
 * activity重新启动并加载对应的该加载的页面
 */
	@Override
	protected void onRestart() {
		
		MyApplication ma = (MyApplication)getApplicationContext();
		mTabHost.setCurrentTab(ma.getFragIndex());
		super.onRestart();
	}
	
	

	/*
	 * (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 * activity返回foreground 加载对应页面
	 */
	@Override
	protected void onResume() {

		MyApplication ma = (MyApplication)getApplicationContext();
		mTabHost.setCurrentTab(ma.getFragIndex());
		super.onResume();
	}

	long currentTime = 0;
	@Override
	public void onBackPressed() {
		
		if(currentTime == 0){
			Toast.makeText(this, "再按一次后退键退出程序", Toast.LENGTH_SHORT).show();
			currentTime = System.currentTimeMillis();
		}else{
			long systemTime = System.currentTimeMillis();
			if(systemTime - currentTime > 2000){
				Toast.makeText(this, "再按一次后退键退出程序", Toast.LENGTH_SHORT).show();
				currentTime = systemTime;
			}else{
				
				MainActivity.this.finish();
				ExitApplication.getInstance().exit();
				System.exit(0);
			}
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int id = item.getItemId();
		switch(id){
		case R.id.action_exit:
//			MainActivity.this.finish();
			ExitApplication.getInstance().exit();
			System.exit(0);
			break;
		case R.id.action_settings:
			Toast toast=Toast.makeText(MainActivity.this, "setting is work", Toast.LENGTH_LONG);
			toast.show();
			break;
			
		}
		return super.onMenuItemSelected(featureId, item);
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
		
	}
	

}
