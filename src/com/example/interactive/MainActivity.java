package com.example.interactive;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnCheckedChangeListener {

	private RadioGroup rg;
	private EditText birthEditText;
	private TextView birthChooseText;
	private DatePickerDialog dpd;
	private int Text_year;
	private int Text_month;
	private int Text_day;
	private Spinner spinner;
	private List<String>list;
	private ArrayAdapter<String> aad;
	private AutoCompleteTextView autoText;
	private List<String>autolist;
	private ArrayAdapter<String> auto_aad;
	private ToggleButton tb;
	private Button button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//实例化各种控件
		rg=(RadioGroup)findViewById(R.id.radioGroup1);
		birthEditText=(EditText) findViewById(R.id.birthEditText);
		birthChooseText=(TextView) findViewById(R.id.birthChooseText);
		spinner=(Spinner) findViewById(R.id.spinner);
		autoText=(AutoCompleteTextView) findViewById(R.id.autoText);
		tb = (ToggleButton) findViewById(R.id.tb);
		button = (Button) findViewById(R.id.button);
		
		rg.setOnCheckedChangeListener(this);
		
		/*
		 * 设置时间选择器
		 * */
		dpd=new DatePickerDialog(this, new OnDateSetListener(){

			public void onDateSet(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				Text_year=year;
				Text_month=monthOfYear+1;
				Text_day=dayOfMonth;
				birthEditText.setText(Text_year+"-"+Text_month+"-"+Text_day);
			}
			
		}, 1990, 1, 1);
		
		//textview的点击事件
		birthChooseText.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				dpd.show();
			}
		});
		
		list= new ArrayList<String>();
		list.add("小学生");
		list.add("初中生");
		list.add("高中生");
		list.add("大学生");
		list.add("研究生");
		list.add("博士生");
		//数组适配器
		aad= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		//设置下拉菜单的样式
		aad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//加载适配器
		spinner.setAdapter(aad);
		
		autolist= new ArrayList<String>();
		autolist.add("xieshijie@163.com");
		autolist.add("xieshijie@qq.com");
		autolist.add("xieshijie@126.com");
		autolist.add("xieshijie@sina.com");
		autolist.add("研究生");
		autolist.add("博士生");
		auto_aad = new ArrayAdapter<String>(this
				,android.R.layout.simple_list_item_1,autolist);
		autoText.setAdapter(auto_aad);
		
		button.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,lightActivity.class);
				startActivity(intent);
			}
		});
		
		
	}
	
	
	//radioButton的点击事件
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.radioButton_man:
			
			break;

		case R.id.radioButton_woman:
			break;
		}
	}
	


	
}
