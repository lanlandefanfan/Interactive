package com.example.interactive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class lightActivity extends Activity {

	private SensorManager sensorManager;//感应器管理器
	private Sensor sensor;
	private TextView textView;
	private ImageView image;
	private TextView textToast;
	private Button intoButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(com.example.interactive.R.layout.activity_light);
		
		textView=(TextView) findViewById(com.example.interactive.R.id.lightText);
		image=(ImageView) findViewById(com.example.interactive.R.id.image);
		textToast=(TextView) findViewById(com.example.interactive.R.id.lightTextToast);
		intoButton=(Button) findViewById(R.id.intoCount);
		//获取感应器服务
		sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
		//获得光线感应器
		sensor=sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		//注册监听器
		sensorManager.registerListener(new SensorEventListener() {
			
			public void onSensorChanged(SensorEvent event) {
				// 当感应器检测到数值变化时
				float value = event.values[0];
				textView.setText("当前亮度："+value+"勒克斯");
				if(value<20.0){
					image.setBackgroundResource(com.example.interactive.R.drawable.winter);
					textToast.setText("哎呦，没光了，好冷~~~");
				}else if(value<40.0&&value>=20.0){
					image.setBackgroundResource(com.example.interactive.R.drawable.autumn);
					textToast.setText("呀嘿，有一点光了，还可以~~~");
				}else if(value<100.0&&value>=40.0){
					image.setBackgroundResource(com.example.interactive.R.drawable.spring);
					textToast.setText("哦哦哦，光来了，好暖和~~~");
				}else if(value<200.0&&value>=100.0){
					image.setBackgroundResource(com.example.interactive.R.drawable.summer);
					textToast.setText("哎呀，好多光了，开始热了~~~");
				}
				else{
					image.setBackgroundResource(com.example.interactive.R.drawable.hot);
					textToast.setText("妈呀，太多光了，着火了~~~");
				}
			}
			
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// 当感应器精度变化
				
			}
		}, sensor,sensorManager.SENSOR_DELAY_NORMAL);
		
		intoButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(lightActivity.this,fingerActivity.class);
				startActivity(intent);
			}
		});
		
	}
	
	
}
