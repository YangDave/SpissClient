package com.spiss.spissclient.component.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.spiss.spissclient.R;
import com.spiss.spissclient.application.ExitApplication;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class FaceCameraActivity extends Activity {
	
	Button button2;
	Button button3;
	Button button4;
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	private Camera camera;
	private boolean isPreview = false;
	private int cameraID = 1;
	private String username;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_face_camera);
        
        ExitApplication.getInstance().addActivity(this);
        
        button2 = (Button)findViewById(R.id.button2);
        button3 = (Button)findViewById(R.id.button3);
        button4 = (Button)findViewById(R.id.button4);
        
        Intent intent=getIntent();
        if(intent != null){
        	username = intent.getStringExtra("user");
        	
        }
        else{
        	Intent intent_back = new Intent(FaceCameraActivity.this, LoginActivity.class);
			startActivity(intent_back);
        }
        
        button2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*Intent intent = new Intent(photoActivity.this, FaceCameraActivity.class);
				startActivity(intent);*/
				if(cameraID == 0){
					cameraID = 1;
					camera.stopPreview();
					camera.release();
					camera = null ;
					isPreview = false;
					camera = Camera.open(cameraID);
					camera.setDisplayOrientation(90);
					camera.startPreview();
					try {
						camera.setPreviewDisplay(surfaceHolder);
					} catch (IOException e) {
						e.printStackTrace();
					}
					isPreview = true;
				}
				else{
					cameraID = 0;
					camera.stopPreview();
					camera.release();
					camera = null ;
					isPreview = false;
					camera = Camera.open(cameraID);
					camera.setDisplayOrientation(90);
					camera.startPreview();
					try {
						camera.setPreviewDisplay(surfaceHolder);
					} catch (IOException e) {
						e.printStackTrace();
					}
					isPreview = true;
				}
			}
		});
        
        button4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FaceCameraActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
        
        
        surfaceView = (SurfaceView)findViewById(R.id.surfaceView1);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new Callback() {
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				if(camera != null){
					if(isPreview)
						camera.stopPreview();
					camera.release();
					camera = null;
				}
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				if(!isPreview){
					camera = Camera.open(cameraID);
					camera.setDisplayOrientation(90);
				}
				if(camera != null && !isPreview){
		    		try {
						Camera.Parameters parameters = camera.getParameters();
						parameters.setPreviewSize(270, 430);
						parameters.setPreviewFpsRange(4, 10);
						parameters.setPictureFormat(ImageFormat.JPEG);
						parameters.setJpegQuality(50);
						parameters.setPictureSize(270, 430);
						camera.setPreviewDisplay(surfaceHolder);
						camera.startPreview();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
		    		isPreview = true;
				}
				
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
				// TODO Auto-generated method stub
				
			}
		});
        
        button3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				capture();
			}
		});
    }
	
	
	public void capture(){
    	if(camera != null){
    		camera.autoFocus(autoFocusCallback);
    	}
    }
    
    AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {
		
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			// TODO Auto-generated method stub
			if(success){
				camera.takePicture(shutter, raw, jpeg);
			}
		}
	};
	
	ShutterCallback shutter = new ShutterCallback() {
		
		@Override
		public void onShutter() {
			// TODO Auto-generated method stub
			
		}
	};
	
	PictureCallback raw = new PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			
		}
	};
	Bitmap bm;
	PictureCallback jpeg = new PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			camera.stopPreview();
			isPreview =false;
			bm = BitmapFactory.decodeByteArray(data, 0, data.length);
			//rotate
			Bitmap bMapRotate;  
            if (cameraID ==1) {  //�����Ƕ�
                Matrix matrix = new Matrix();  
                matrix.reset();  
                matrix.postRotate(270);  
                bMapRotate = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),  
                		bm.getHeight(), matrix, true); 
                bm = bMapRotate;
            }
            else{
            	Matrix matrix = new Matrix();  
                matrix.reset();  
                matrix.postRotate(90);  
                bMapRotate = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),  
                		bm.getHeight(), matrix, true); 
                bm = bMapRotate;
            }
			//save bm
            String fileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/1.jpg";
            Log.v("fileanem", "fileName = "+fileName);
			File file_save = new File(fileName);
			try {
				FileOutputStream outputStream = null;
				outputStream = new FileOutputStream(file_save);
				bm.compress(CompressFormat.JPEG, 20, outputStream);
				outputStream.close();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.gc();
			Intent intent = new Intent(FaceCameraActivity.this, ProcessActivity.class);
			intent.putExtra("user", username);
			startActivity(intent);
			finish();
		}
	};
	
	
	/*public void create(final String userName){
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HttpRequests httpRequests = new HttpRequests("270ff76b7c212533a3079aa0be82a3c5", "hsWv6TPO6nc_PteGNxAPMwihBEBQQpYz");
				FaceppResult result = null;
				try {
					result = httpRequests.personCreate(new PostParameters().setPersonName(userName));
				} catch (FaceppParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(result);
				
			}
		}).start();
	}*/
	
	

}
