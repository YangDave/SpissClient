package com.spiss.spissclient.component.fragment;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.spiss.spissclient.R;

public class AlarmFragment extends Fragment implements SensorEventListener{
	
	SensorManager sensorManager;
	private SoundPool soundPool;
	private Spinner spinSeismAlarm = null;
	private Spinner spinFireAlarm = null;
	private Spinner spinVolume = null;
	TextView textView;
	Switch switcher1;
	Switch switcher2;
	boolean seism_switch = true;  
	boolean fire_switch = true;   
	float[] values;   //传感器
	float[] values_x = { 0.0f, 0.0f };
	float[] values_y = { 0.0f, 0.0f };
	float[] values_z = { 0.0f, 0.0f };
	float[] values_f = { 0.0f, 0.0f };
	float values_fDiff =  0.0f ;
	float[] lowpass = { 0.0f, 0.0f, 0.0f };
	float[] v = { 0.0f, 0.0f, 0.0f };
	int seism_level = 0;
	int seism_alarmlv = 0;
	float values_level = 0.0f;
	float xyz;
	float xyz_threshold = 0.02f;
	float light_max = 300;
	float light_min = 20;
	float light_alarmlv=20;
	int shake_times = 0;
	int fire_times = 0;
	int fire_zerotimes=0;
	int safety_times = 0;
	boolean earthquake = false;
	boolean fire = false;
	float volumeLeft=0.1f;
	float volumeRight=0.1f;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_3, container,false);
		
		//获取地震预警spinner 注册OnItemSelected事件
		spinSeismAlarm = (Spinner)rootView.findViewById(R.id.seismsp);
		spinSeismAlarm.setSelection(6);
		spinSeismAlarm.setOnItemSelectedListener(new SeismOnItemSelectedListener());
		//获取火灾spinner 注册OnItemSelected事件
		spinFireAlarm = (Spinner)rootView.findViewById(R.id.lightsp);
		spinFireAlarm.setOnItemSelectedListener(new FireOnItemSelectedListener());
		//获取音量spinner 注册OnItemSelected事件
		spinVolume = (Spinner)rootView.findViewById(R.id.volumesp);
		spinVolume.setOnItemSelectedListener(new VolumeOnItemSelectedListener());
		
		textView = (TextView)rootView.findViewById(R.id.textView1);
		//注册传感器管理器
		sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
		soundPool = new SoundPool(5,AudioManager.STREAM_ALARM,0);
		soundPool.load(getActivity(), R.raw.alarm, 1);
		// 地震预警开关
		switcher1 = (Switch) rootView.findViewById(R.id.switcher1);
		switcher1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked){
					seism_switch = true;
					textView.setVisibility(View.VISIBLE);
				}
				else{
					seism_switch = false;
					soundPool.pause(1);
					textView.setVisibility(View.INVISIBLE);
				}
			}
		});
		// 火灾预警开关
		switcher2 = (Switch) rootView.findViewById(R.id.switcher2);
		switcher2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked){
					fire_switch = true;
				}
				else{
					fire_switch = false;
					soundPool.pause(1);
				}
			}
		});
		return rootView;
	}
	
	
	//seismonItemSelected监听器
	private class SeismOnItemSelectedListener implements OnItemSelectedListener{
		public void onItemSelected(AdapterView<?> adapter,View view,int position,long id){
			seism_alarmlv=position+1;
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
	}
	
	//FireOnItemSelected监听器
	private class FireOnItemSelectedListener implements OnItemSelectedListener{
		public void onItemSelected(AdapterView<?> adapter,View view,int position,long id){
			if(position==0)
			{
				light_alarmlv=10;
			}
			if(position==1)
			{
				light_alarmlv=50;
			}
			if(position==2)
			{
				light_alarmlv=100;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
		
	}

	//VolumeOnItemSelected监听
	private class VolumeOnItemSelectedListener implements OnItemSelectedListener{
		public void onItemSelected(AdapterView<?> adapter,View view,int position,long id){
			if(position==0)
			{
				volumeLeft=0.0f;
				volumeRight=0.0f;
			}
			if(position==1)
			{
				volumeLeft=0.2f;
				volumeRight=0.2f;
			}
			if(position==2)
			{
				volumeLeft=0.5f;
				volumeRight=0.5f;
			}
			if(position==3)
			{
				volumeLeft=1.0f;
				volumeRight=1.0f;
			}
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		// 为系统的加速度传感器注册监听器
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		// 为系统的光传感器注册监听器
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void onStop() {
		sensorManager.unregisterListener(this);
		super.onStop();
	}

	// 低通滤波
	private float[] low_pass(float[] input) {
		for (int i = 0; i < 3; i++) {
			lowpass[i] = input[i] * 0.2f + lowpass[i] * 0.8f;
		}
		return lowpass;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		values = event.values;
		//获取传感器类型
		int sensorType = event.sensor.getType();
		StringBuilder stringBuilder = new StringBuilder();
		//判断是哪个传感器发生变化
		switch(sensorType)
		{
		//加速度传感器
		case Sensor.TYPE_ACCELEROMETER:
		if (seism_switch == true) {
			values_x[0] = values[0];
			values_y[0] = values[1];
			values_z[0] = values[2];
			v[0] = Math.abs(values_x[1] - values_x[0]);
			v[1] = Math.abs(values_y[1] - values_y[0]);
			v[2] = Math.abs(values_z[1] - values_z[0]);
			v = low_pass(v);
			xyz = (v[0] + v[1] + v[2]) / 3;
			values_x[1] = values_x[0];
			values_y[1] = values_y[0];
			values_z[1] = values_z[0];
			if (xyz > xyz_threshold) {
				shake_times++;
				values_level = values_level + xyz;

			}
			if (xyz < xyz_threshold) {
				safety_times++;
				if (safety_times == 20) {
					shake_times = 0;
					safety_times = 0;

				}
			}


			if (shake_times > 5) {
				earthquake = true;
				if (values_level < (0.02 * shake_times))
					seism_level = 0;
				if ((0.02 * shake_times) < values_level
						&& values_level < (0.05 * shake_times))
					seism_level = 1;
				if ((0.05 * shake_times) < values_level
						&& values_level < (0.1 * shake_times))
					seism_level = 2;
				if ((0.1 * shake_times) < values_level
						&& values_level < (0.15 * shake_times))
					seism_level = 3;
				if ((0.15 * shake_times) < values_level
						&& values_level < (0.2 * shake_times))
					seism_level = 4;
				if ((0.2 * shake_times) < values_level
						&& values_level < (0.25 * shake_times))
					seism_level = 5;
				if ((0.25 * shake_times) < values_level
						&& values_level < (0.3 * shake_times))
					seism_level = 6;
				if (values_level > (0.35 * shake_times))
					seism_level = 7;
				stringBuilder.append("震感级别：");
				stringBuilder.append(seism_level);

			}

			if (shake_times < 5) {
				earthquake = false;
				values_level = 0.0f;	
			}
		
		textView.setText(stringBuilder.toString());
		if(earthquake==true&&seism_level>=seism_alarmlv)
		{
			soundPool.play(1, volumeLeft, volumeRight, 0, 3, 1);
		}
		if(earthquake==false||seism_level<seism_alarmlv)
		{
			soundPool.pause(1);
		}
		}
			break;
			//光传感器
		case Sensor.TYPE_LIGHT:
			
			if(fire_switch==true)
			{
				
				values_f[0]=values[0];
				values_fDiff=values_f[1]-values_f[0];
				values_f[1]=values_f[0];
				if(values_fDiff>light_alarmlv)
				{					
					fire_times++;
					
				}
				if(values_f[0]>=light_max||values_f[0]<=light_min)
				{
					fire_times = 0;
				}
				if(fire_times>3)
					fire = true;
				else
					fire = false;
			
			if(fire==true)
				soundPool.play(1, volumeLeft, volumeRight, 0, 3, 1);
			
			else
				soundPool.pause(1);
			}
			break;
			
		}
				
	}
	

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	
	

}
