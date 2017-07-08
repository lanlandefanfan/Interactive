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

	private SensorManager sensorManager;//��Ӧ��������
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
		//��ȡ��Ӧ������
		sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
		//��ù��߸�Ӧ��
		sensor=sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		//ע�������
		sensorManager.registerListener(new SensorEventListener() {
			
			public void onSensorChanged(SensorEvent event) {
				// ����Ӧ����⵽��ֵ�仯ʱ
				float value = event.values[0];
				textView.setText("��ǰ���ȣ�"+value+"�տ�˹");
				if(value<20.0){
					image.setBackgroundResource(com.example.interactive.R.drawable.winter);
					textToast.setText("���ϣ�û���ˣ�����~~~");
				}else if(value<40.0&&value>=20.0){
					image.setBackgroundResource(com.example.interactive.R.drawable.autumn);
					textToast.setText("ѽ�٣���һ����ˣ�������~~~");
				}else if(value<100.0&&value>=40.0){
					image.setBackgroundResource(com.example.interactive.R.drawable.spring);
					textToast.setText("ŶŶŶ�������ˣ���ů��~~~");
				}else if(value<200.0&&value>=100.0){
					image.setBackgroundResource(com.example.interactive.R.drawable.summer);
					textToast.setText("��ѽ���ö���ˣ���ʼ����~~~");
				}
				else{
					image.setBackgroundResource(com.example.interactive.R.drawable.hot);
					textToast.setText("��ѽ��̫����ˣ��Ż���~~~");
				}
			}
			
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// ����Ӧ�����ȱ仯
				
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
