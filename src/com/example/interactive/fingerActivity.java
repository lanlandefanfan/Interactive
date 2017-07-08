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
	 * intent������ʵ�ֽ���֮�����Ϣ���ݵģ�intent�����ַ�ʽ��
	 * һ��������Ϣ�ش�����һ��������Ϣ�Ļش����ڱ���ʹ�õ�������Ϣ�ش��ķ���
	 * ��ȡ���ش�����������֮�󣬰���װ�س�bitmap�ĸ�ʽ
	 * Ȼ���ڵ��ö�ֵ�������ͼ�����ָ���ķ�������ȡ��ָ��
	 * �ڰѻ�õ���Ϣ��д��textview����
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
					textView.setText("�����ָ����" + countFinger);
				} else {
					Toast.makeText(getApplicationContext(), "�Ҳ���ͼƬ",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	/**
	 * ����Android�Դ���bitmap���ͣ�
	 * ����һ��λͼ��ͼƬ�Ĵ洢�������ذ��������ʽ�洢��
	 * �ڱ������У����Ȼ�ȡͼ��ĳ����ڻ�ȡ���ص�����
	 * ͨ��forѭ������ʽ�Ѷ����Ƶ���ɫ����ת����ʮ���Ƶ�
	 * �ڸ��������ɫ����ֵ������Ӧ ����ɫ����ı�ɺ�ɫ�Ͱ�ɫ
	 * ����½�һ���µ�bitmap�����ǵ����ؾ�������
	 * */
	protected Bitmap convertToBMW(Bitmap bmp) {
		int width = bmp.getWidth(); // ��ȡλͼ�Ŀ�
		int height = bmp.getHeight(); // ��ȡλͼ�ĸ�
		int[] pixels = new int[width * height]; // ͨ��λͼ�Ĵ�С�������ص�����
		// �趨��ֵ������ֵ��Ĭ��ֵΪ100
		// tmp = 180;
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		int alpha = 0xFF << 24;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int grey = pixels[width * i + j];
				// ������ԭɫ
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
		// �½�ͼƬ
		Bitmap newBmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		// ����ͼƬ����
		newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
		// Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(newBmp, w, h);
		return newBmp;
	}

	/**
	 * ͨ��������ֵ��ͼƬ�����ؾ�������ȡ��Ϣ
	 * ���ڶ�ֵ��ͼƬ������ֻ��0��255���ںͰף�
	 * �������ں��������ֱ�ȡ�����������жϣ�ֻҪ�дӺڵ��׵ı任�ͼ���һ��
	 * �൱����һ����ָ�����ȽϺ��������Ĵ�С��ȡ���ֵ��Ϊ��ָ��
	 * */
	protected int countFinger(Bitmap bmp) {
		int countFinger = 0;
		int countFinger1 = 0;

		int width = bmp.getWidth(); // ��ȡλͼ�Ŀ�
		int height = bmp.getHeight(); // ��ȡλͼ�ĸ�
		int[] pixels = new int[width * height]; // ͨ��λͼ�Ĵ�С�������ص�����
		// �趨��ֵ������ֵ��Ĭ��ֵΪ100
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
