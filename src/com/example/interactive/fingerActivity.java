package com.example.interactive;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class fingerActivity extends Activity {
	private ImageView sourceImage;
	private ImageView resultImage;
	private Button takeCameraBt;
	private TextView textView;
	private final int TAKE_PHOTO = 1;
	private Bitmap photo;
	private Bitmap resultPhoto;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.example.interactive.R.layout.countfinger);
		sourceImage = (ImageView) findViewById(com.example.interactive.R.id.sourceImage);
		resultImage = (ImageView) findViewById(com.example.interactive.R.id.resultImage);
		takeCameraBt = (Button) findViewById(com.example.interactive.R.id.takeCamera);
		textView = (TextView) findViewById(com.example.interactive.R.id.textView);

		takeCameraBt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				startActivityForResult(intent, TAKE_PHOTO);
			}
		});
	}

	/**
	 * intent是用来实现界面之间的信息传递的，intent用两种方式，
	 * 一种是无信息回传，另一种是有信息的回传，在本次使用的是有信息回传的方法
	 * 获取到回传回来的数据之后，把它装载成bitmap的格式
	 * 然后在调用二值化方法和计算手指数的方法，获取手指数
	 * 在把获得的信息填写到textview上面
	 * */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == TAKE_PHOTO) {
			if (data.getData() != null || data.getExtras() != null) {
				Bundle bundle = data.getExtras();
				photo = (Bitmap) bundle.get("data");
				int countFinger = 0;
				if (photo != null) {
					sourceImage.setImageBitmap(photo);
					resultPhoto = convertToBMW(photo);
					resultImage.setImageBitmap(resultPhoto);
					countFinger = countFinger(resultPhoto);
					textView.setText("你的手指数：" + countFinger);
				} else {
					Toast.makeText(getApplicationContext(), "找不到图片",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	/**
	 * 调用Android自带的bitmap类型，
	 * 这是一个位图，图片的存储是以像素按矩阵的形式存储的
	 * 在本方法中，首先获取图像的长宽，在获取像素的数组
	 * 通过for循环的形式把二进制的颜色代码转换成十进制的
	 * 在根据人体肤色的阈值，把相应 的颜色区域改变成黑色和白色
	 * 最后新建一个新的bitmap把他们的像素矩阵填充好
	 * */
	protected Bitmap convertToBMW(Bitmap bmp) {
		int width = bmp.getWidth(); // 获取位图的宽
		int height = bmp.getHeight(); // 获取位图的高
		int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组
		// 设定二值化的域值，默认值为100
		// tmp = 180;
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		int alpha = 0xFF << 24;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int grey = pixels[width * i + j];
				// 分离三原色
				alpha = ((grey & 0xFF000000) >> 24);
				int red = ((grey & 0x00FF0000) >> 16);
				int green = ((grey & 0x0000FF00) >> 8);
				int blue = (grey & 0x000000FF);

				int Max = 0;
				int Min = 0;
				if (red > 95 && green > 40 && blue > 20 && red > blue
						&& red > green && Math.abs(red - green) > 15) {
					if (blue >= green) {
						Max = blue;
						Min = green;
					} else {
						Max = green;
						Min = blue;
					}
					if (red > Max)
						Max = red;
					else if (red < Min)
						Min = red;
					if (Max - Min > 15) {
						red = 255;
						blue = 255;
						green = 255;
					}
				}

				pixels[width * i + j] = alpha << 24 | red << 16 | green << 8
						| blue;
				if (pixels[width * i + j] == -1) {
					pixels[width * i + j] = -1;
				} else {
					pixels[width * i + j] = -16777216;
				}
			}
		}
		// 新建图片
		Bitmap newBmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		// 设置图片数据
		newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
		// Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(newBmp, w, h);
		return newBmp;
	}

	/**
	 * 通过遍历二值化图片的像素矩阵来获取信息
	 * 由于二值化图片的像素只有0和255（黑和白）
	 * 所以我在横向和纵向分别取三个数组做判断，只要有从黑到白的变换就计算一次
	 * 相当于有一根手指，最后比较横向和纵向的大小，取最大值作为手指数
	 * */
	protected int countFinger(Bitmap bmp) {
		int countFinger = 0;
		int countFinger1 = 0;

		int width = bmp.getWidth(); // 获取位图的宽
		int height = bmp.getHeight(); // 获取位图的高
		int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组
		// 设定二值化的域值，默认值为100
		// tmp = 180;
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		// int alpha = 0xFF << 24;
		for (int j = 0; j < width; j++) {
			int grey1 = pixels[width * (width / 2) + j];
			int red1 = ((grey1 & 0x00FF0000) >> 16);
			int green1 = ((grey1 & 0x0000FF00) >> 8);
			int blue1 = (grey1 & 0x000000FF);

			int grey2 = pixels[width * (width / 2) + j + 1];
			int red2 = ((grey2 & 0x00FF0000) >> 16);
			int green2 = ((grey2 & 0x0000FF00) >> 8);
			int blue2 = (grey2 & 0x000000FF);

			int grey3 = pixels[width * (width / 2) + j + 2];
			int red3 = ((grey3 & 0x00FF0000) >> 16);
			int green3 = ((grey3 & 0x0000FF00) >> 8);
			int blue3 = (grey3 & 0x000000FF);

			int grey4 = pixels[width * (width / 2) + j + 3];
			int red4 = ((grey4 & 0x00FF0000) >> 16);
			int green4 = ((grey4 & 0x0000FF00) >> 8);
			int blue4 = (grey4 & 0x000000FF);
			if (red1 == 0 && green1 == 0 && blue1 == 0 && red2 == 0
					&& green2 == 0 && blue2 == 0 && red3 == 255
					&& green3 == 255 && blue3 == 255 && red4 == 255
					&& green4 == 255 && blue4 == 255) {
				countFinger++;
			}
		}
		
		for(int j=0;j<(height-5);j++){
			int grey1 = pixels[(width/2)+j*width+0];
			int red1 = ((grey1 & 0x00FF0000) >> 16);
			int green1 = ((grey1 & 0x0000FF00) >> 8);
			int blue1 = (grey1 & 0x000000FF);

			int grey2 = pixels[(width/2)+j*width+width];
			int red2 = ((grey2 & 0x00FF0000) >> 16);
			int green2 = ((grey2 & 0x0000FF00) >> 8);
			int blue2 = (grey2 & 0x000000FF);

			int grey3 = pixels[(width/2)+j*width+2*width];
			int red3 = ((grey3 & 0x00FF0000) >> 16);
			int green3 = ((grey3 & 0x0000FF00) >> 8);
			int blue3 = (grey3 & 0x000000FF);

			int grey4 = pixels[(width/2)+j*width+3*width];
			int red4 = ((grey4 & 0x00FF0000) >> 16);
			int green4 = ((grey4 & 0x0000FF00) >> 8);
			int blue4 = (grey4 & 0x000000FF);
			
			if (red1 == 0 && green1 == 0 && blue1 == 0 && red2 == 0
					&& green2 == 0 && blue2 == 0 && red3 == 255
					&& green3 == 255 && blue3 == 255 && red4 == 255
					&& green4 == 255 && blue4 == 255) {
				countFinger1++;
			}
		}
		if(countFinger1>countFinger)
			countFinger=countFinger1;

		return countFinger;
	}
	
}
