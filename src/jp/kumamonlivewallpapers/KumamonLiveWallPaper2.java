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
import java.util.*;

import jp.kumamonlivewallpapers.ForecastTask;
import jp.kumamonlivewallpapers.R;
import jp.template.LiveWallPaper;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.WindowManager;

public class KumamonLiveWallPaper2 extends LiveWallPaper {
	private static final int[] images = {
		R.drawable.image10,R.drawable.image11,R.drawable.image12,
		R.drawable.image13,R.drawable.image14,R.drawable.image40,
		R.drawable.image60,R.drawable.image80,R.drawable.image100,};
	private int displayWidth;
	private Random randam = new Random();
	private int preDoubleTap = 0;
	private int preOffset = 0;
	private int mLocateId = 0;

	// 設定が変更された時に呼び出されるListener
	private final SharedPreferences.OnSharedPreferenceChangeListener mListerner = 
			new SharedPreferences.OnSharedPreferenceChangeListener()
	{
		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		// ウィンドウマネージャのインスタンス取得
		WindowManager windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
		// ディスプレイのインスタンス生成
		Display display = windowManager.getDefaultDisplay();
		displayWidth = display.getWidth();
		Images = images;
		BackgroundColor = Color.WHITE;
		Image = BitmapFactory.decodeResource(getResources(), Images[randam.nextInt(Images.length)]);

		// 設定が変更された時に呼び出されるListenerを登録
		SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		setting.registerOnSharedPreferenceChangeListener(mListerner);
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
		// draw something
		super.DrawCanvas(canvas);
		OverLayer(canvas);
		KumamonCopyright(canvas);
	}

	@Override
	public void ChangeImage() {
		if(preDoubleTap != DoubleTap || preOffset != Offset) {
			Image = BitmapFactory.decodeResource(getResources(), Images[randam.nextInt(Images.length)]);
		}
		preDoubleTap = DoubleTap;
		preOffset = Offset;
		getForecast();
	}

	private void OverLayer(Canvas canvas) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		if(sharedPreferences.getBoolean("date", false)) {
			paint.setTextSize(18);
			Date date = Calendar.getInstance().getTime();
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd(EEE)", Locale.JAPANESE);
			SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm", Locale.JAPANESE);
			canvas.drawText(sdf1.format(date), 340, 70, paint);
			paint.setTextSize(50);
			canvas.drawText(sdf2.format(date), 340, 115, paint);
			int battery = (int)((double)BatteryLevel / BatteryScale * 100.0 + 0.5);
			paint.setTextSize(18);
			Resources resource = getResources();
			canvas.drawText(resource.getString(R.string.battery)+ String.valueOf(battery) +"%", 340, 140, paint);
		}

		if(sharedPreferences.getBoolean("forecast", false)) {
			String today = sharedPreferences.getString(ForecastTask.KEY_TODAY, "");
			String tomorrow = sharedPreferences.getString(ForecastTask.KEY_TOMORROW, "");
			String day_after_tomorrow = sharedPreferences.getString(ForecastTask.KEY_DAY_AFTER_TOMORROW, "");
			paint.setTextSize(24);
			canvas.drawText(today, 10, 70, paint);
			canvas.drawText(tomorrow, 10, 100, paint);
			canvas.drawText(day_after_tomorrow, 10, 130, paint);
		}
	}

	private void KumamonCopyright(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		paint.setTextSize(16 * displayWidth / 480);
		Resources resource = getResources();
		int x = 240 * displayWidth / 480;
		int y = 640 * displayWidth / 480;
		canvas.drawText(resource.getString(R.string.KumamonCopyright), x, y, paint);
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
