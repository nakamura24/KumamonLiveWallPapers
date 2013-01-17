/*
 * Copyright (C) 2012 M.Nakamura
 *
 * This software is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 2.1 Japan License.
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		http://creativecommons.org/licenses/by-nc-sa/2.1/jp/legalcode
 */

package jp.kumamon.livewallpapers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import jp.kumamon.livewallpapers.ForecastTask;
import jp.kumamon.livewallpapers.R;
import jp.template.LiveWallPaper;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.MotionEvent;

public class KumamonLockLiveWallPaper extends LiveWallPaper {
	private Random randam = new Random();
	private boolean warking = false;
	private boolean SingleTap = false;
	private int mLocateId = 0;
	
	public KumamonLockLiveWallPaper() {
		BackgroundColor = Color.WHITE;
		//　ActionTimeTickを有効にする
		ActionTimeTick = true;
		//　ActionBatteryChangedを有効にする
		ActionBatteryChanged = true;
	}

	@Override
	public void onCreate() {
		super.onCreate();
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
	public void DrawCanvas(Canvas canvas) {
		super.DrawCanvas(canvas);
		// draw something
		OverLayer(canvas);
		KumamonCopyright(canvas);
	}

	@Override
	public void ChangeImage() {
		if(SingleTap) {
			int[] images = {R.drawable.lock2,R.drawable.lock3,R.drawable.lock4,R.drawable.lock5,};
			Image = BitmapFactory.decodeResource(getResources(), images[randam.nextInt(images.length)]);
			DelayMillis = 3000;	// millisecond
			SingleTap = false;
		} else {
			if(warking) {
				Image = BitmapFactory.decodeResource(getResources(), R.drawable.lock0);
				warking  = false;
			} else {
				Image = BitmapFactory.decodeResource(getResources(), R.drawable.lock1);
				warking  = true;
			}
			DelayMillis = 1000;	// millisecond
		}
		getForecast();
	}
	
	@Override
	public boolean SingleTapConfirmed(MotionEvent event) {
		SingleTap = true;
		return true;
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
		canvas.drawText(resource.getString(R.string.KumamonCopyright), 80 * Scaled, 200 * Scaled, paint);
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
