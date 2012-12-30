/*
 * Copyright (C) 2012 M.Nakamura
 *
 * This software is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 2.1 Japan License.
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		http://creativecommons.org/licenses/by-nc-sa/2.1/jp/legalcode
 */

package jp.kumamonlivewallpapers.livewallpaper2;

import java.text.SimpleDateFormat;
import java.util.*;

import jp.kumamonlivewallpapers.R;
import jp.template.LiveWallPaper;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
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
	}

	private void OverLayer(Canvas canvas) {
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd(EEE)", Locale.JAPANESE);
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm", Locale.JAPANESE);
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		paint.setTextSize(18);
		canvas.drawText(sdf1.format(date), 20, 70, paint);
		paint.setTextSize(50);
		canvas.drawText(sdf2.format(date), 20, 115, paint);
		int battery = (int)((double)BatteryLevel / BatteryScale * 100.0 + 0.5);
		paint.setTextSize(18);
		Resources resource = getResources();
		canvas.drawText(resource.getString(R.string.battery)+ String.valueOf(battery) +"%", 25, 140, paint);
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
}
