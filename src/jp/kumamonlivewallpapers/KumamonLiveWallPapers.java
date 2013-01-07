/*
 * Copyright (C) 2012 M.Nakamura
 *
 * This software is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 2.1 Japan License.
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		http://creativecommons.org/licenses/by-nc-sa/2.1/jp/legalcode
 */

package jp.kumamonlivewallpapers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import jp.kumamonlivewallpapers.ForecastTask;
import jp.kumamonlivewallpapers.R;
import jp.template.LiveWallPaper;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.MotionEvent;

public class KumamonLiveWallPapers extends LiveWallPaper {
	private static final int[] images = {
		R.drawable.image10,R.drawable.image11,R.drawable.image12,
		R.drawable.image13,R.drawable.image14,R.drawable.image40,
		R.drawable.image60,R.drawable.image80,R.drawable.image100,};
	private static final int[] sma = {
		R.drawable.sma_0, R.drawable.sma_1, R.drawable.sma_2, R.drawable.sma_3, R.drawable.sma_4,
		R.drawable.sma_5, R.drawable.sma_6, R.drawable.sma_7, R.drawable.sma_8, R.drawable.sma_9,};
	private int mLocateId = 0;
	private Random randam = new Random();
	private int width;
	private int hight;
	private int position;

	@Override
	public void onCreate() {
		super.onCreate();
		Image = BitmapFactory.decodeResource(getResources(), R.drawable.image60);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public Engine onCreateEngine() {
		return new LiveEngine();
	}

	@Override
	public void ChangeImage() {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		int select = Integer.parseInt(sharedPreferences.getString("select", "0"));
		switch(select) {
		case 1:
			ChangeImage1();
			getForecast();
			break;
		case 2:
			Images = images;
			Image = BitmapFactory.decodeResource(getResources(), Images[randam.nextInt(Images.length)]);
			getForecast();
			break;
		case 3:
			Image = BitmapFactory.decodeResource(getResources(), R.drawable.kuma6);
			width = Image.getWidth();
			hight = Image.getHeight();
			break;
		case 4:
			Image = BitmapFactory.decodeResource(getResources(), R.drawable.kuma5);
			width = Image.getWidth();
			hight = Image.getHeight();
			break;
		case 5:
			Images = sma;
			super.ChangeImage();
		}
	}

	@Override
	public void DrawCanvas(Canvas canvas) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		int select = Integer.parseInt(sharedPreferences.getString("select", "0"));
		switch(select) {
		case 1:
			DrawCanvas1(canvas);
			break;
		case 2:
			BackgroundColor = Color.WHITE;
			super.DrawCanvas(canvas);
			OverLayer(canvas);
			KumamonCopyright(canvas);
			break;
		case 3:
			DrawCanvas2(canvas);
			break;
		case 4:
			DrawCanvas2(canvas);
			break;
		case 5:
			BackgroundColor = Color.BLACK;
			super.DrawCanvas(canvas);
			break;
		}
	}
	
	@Override
	public boolean Scroll(MotionEvent event1, MotionEvent event2, float distanceX, float distanceY) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		int select = Integer.parseInt(sharedPreferences.getString("select", "0"));
		switch(select) {
		case 1:
			break;
		case 2:
			break;
		case 3:
			position += distanceX;
			return true;
		case 4:
			position += distanceX;
			return true;
		case 5:
			break;
		}
		return false;
	}
	
	public void ChangeImage1() {
		if (Offset == 0) {
			Image = BitmapFactory.decodeResource(getResources(), R.drawable.image0);
		} else if (Offset == 1) {
			Image = BitmapFactory.decodeResource(getResources(), R.drawable.image1);
		} else if (Offset == 2) {
			if(BatteryLevel < BatteryScale / 5) {
				Image = BitmapFactory.decodeResource(getResources(), R.drawable.image20);
			} else if(BatteryLevel < BatteryScale * 2 / 5) {
				Image = BitmapFactory.decodeResource(getResources(), R.drawable.image40);
			} else {
				Calendar calendar = Calendar.getInstance();
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				switch(hour)
				{
				case 7:
				case 12:
				case 18:
					Image = BitmapFactory.decodeResource(getResources(), R.drawable.image10);
					break;
				case 20:
					Image = BitmapFactory.decodeResource(getResources(), R.drawable.image11);
					break;
				case 10:
					Image = BitmapFactory.decodeResource(getResources(), R.drawable.image12);
					break;
				case 14:
					Image = BitmapFactory.decodeResource(getResources(), R.drawable.image13);
					break;
				case 16:
					Image = BitmapFactory.decodeResource(getResources(), R.drawable.image14);
					break;
				default:
					if(BatteryLevel < BatteryScale * 3 / 5) {
						Image = BitmapFactory.decodeResource(getResources(), R.drawable.image60);
					} else if(BatteryLevel < BatteryScale * 4 / 5) {
						Image = BitmapFactory.decodeResource(getResources(), R.drawable.image80);
					} else {
						Image = BitmapFactory.decodeResource(getResources(), R.drawable.image100);
					}
					break;
				}
			}
		} else if (Offset == 3) {
			Image = BitmapFactory.decodeResource(getResources(), R.drawable.image3);
		} else if (Offset == 4) {
			Image = BitmapFactory.decodeResource(getResources(), R.drawable.image4);
		}
	}
	
	public void DrawCanvas1(Canvas canvas) {
		// draw something
		if(BatteryLevel <= BatteryScale / 2) {
			BackgroundColor = Color.rgb(0xff, (int)(0xff * (double)BatteryLevel * 2 / BatteryScale), 0);
		} else {
			BackgroundColor = Color.rgb((int)(0xff * (2 - (double)BatteryLevel * 2 / BatteryScale)), 0xff, 0);
		}
		super.DrawCanvas(canvas);
		OverLayer(canvas);
		KumamonCopyright(canvas);
	}
	
	public void DrawCanvas2(Canvas canvas) {
		// draw something
		BackgroundColor = Color.BLACK;
		canvas.drawColor(BackgroundColor);
		if(position < 0) position = 0;
		if(position > (width - WidthPixels)) position = (width - WidthPixels);
		canvas.drawBitmap(Image, new Rect(position, 0, position + WidthPixels, hight),
				new Rect(0, 0, WidthPixels, hight), null);
	}

	private void OverLayer(Canvas canvas) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		if(sharedPreferences.getBoolean("forecast", false)) {
			String today = sharedPreferences.getString(ForecastTask.KEY_TODAY, "");
			String tomorrow = sharedPreferences.getString(ForecastTask.KEY_TOMORROW, "");
			String day_after_tomorrow = sharedPreferences.getString(ForecastTask.KEY_DAY_AFTER_TOMORROW, "");
			paint.setTextSize(8 * Scaled);
			canvas.drawText(today, 3 * Scaled, 22 * Scaled, paint);
			canvas.drawText(tomorrow, 3 * Scaled, 32 * Scaled, paint);
			canvas.drawText(day_after_tomorrow, 3 * Scaled, 42 * Scaled, paint);
		}
		if(sharedPreferences.getBoolean("date", false)) {
			paint.setTextSize(6 * Scaled);
			Date date = Calendar.getInstance().getTime();
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd(EEE)", Locale.JAPANESE);
			SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm", Locale.JAPANESE);
			canvas.drawText(sdf1.format(date), 115 * Scaled, 22 * Scaled, paint);
			paint.setTextSize(16 * Scaled);
			canvas.drawText(sdf2.format(date), 115 * Scaled, 37 * Scaled, paint);
			int battery = (int)((double)BatteryLevel / BatteryScale * 100.0 + 0.5);
			paint.setTextSize(6 * Scaled);
			Resources resource = getResources();
			canvas.drawText(resource.getString(R.string.battery)+ String.valueOf(battery) +"%", 117 * Scaled, 43 * Scaled, paint);
		}
	}

	private void KumamonCopyright(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		paint.setTextSize(5 * Scaled);
		Resources resource = getResources();
		canvas.drawText(resource.getString(R.string.KumamonCopyright), 80 * Scaled, 215 * Scaled, paint);
	}

	private void getForecast() {
		try {
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
			if(sharedPreferences.getBoolean("forecast", false)) {
				Calendar nowDate = Calendar.getInstance();
				Calendar lastUpdate = Calendar.getInstance();
				lastUpdate.setTimeInMillis(sharedPreferences.getLong(ForecastTask.KEY_LASTUPDATE, nowDate.getTimeInMillis()));
				int locateId = Integer.parseInt(sharedPreferences.getString("locate", "63"));
				if(mLocateId == locateId) {
					if(lastUpdate.get(Calendar.DAY_OF_YEAR) == nowDate.get(Calendar.DAY_OF_YEAR) &&
							lastUpdate.get(Calendar.HOUR_OF_DAY) >= 6 && nowDate.get(Calendar.HOUR_OF_DAY) < 12) return;
					if(lastUpdate.get(Calendar.DAY_OF_YEAR) == nowDate.get(Calendar.DAY_OF_YEAR) &&
							lastUpdate.get(Calendar.HOUR_OF_DAY) >= 12 && nowDate.get(Calendar.HOUR_OF_DAY) < 18) return;
					if(lastUpdate.get(Calendar.DAY_OF_YEAR) == nowDate.get(Calendar.DAY_OF_YEAR) &&
							lastUpdate.get(Calendar.HOUR_OF_DAY) >= 18) return;
					if(lastUpdate.get(Calendar.DAY_OF_YEAR) == nowDate.get(Calendar.DAY_OF_YEAR) &&
							lastUpdate.get(Calendar.HOUR_OF_DAY) < 6 && nowDate.get(Calendar.HOUR_OF_DAY) < 6) return;
				}
				mLocateId = locateId;
				ForecastTask task = new ForecastTask(this);
				task.execute(locateId);
			}
		} catch (Exception e) {
			//ExceptionLog.Log(TAG, e);
		}
	}
}
