﻿/*
 * Copyright (C) 2012 M.Nakamura
 *
 * This software is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 2.1 Japan License.
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		http://creativecommons.org/licenses/by-nc-sa/2.1/jp/legalcode
 */

package jp.kumamonlivewallpapers;

import java.util.Random;

import jp.library.LiveWallPaper;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.Display;
import android.view.WindowManager;

public class KumamonLockLiveWall extends LiveWallPaper {
	private int displayWidth;
	private Random randam = new Random();
	private boolean warking = false;
    private int preSingleTap = 0;

	@Override
	public void onCreate() {
		super.onCreate();
		// ウィンドウマネージャのインスタンス取得
		WindowManager windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
		// ディスプレイのインスタンス生成
		Display display = windowManager.getDefaultDisplay();
		displayWidth = display.getWidth();
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
	
	public void DrawCanvas(Canvas canvas) {
		// draw something
		canvas.drawColor(BackgroundColor);
		ChangeImage();
		canvas.drawBitmap(Image, 0, 0, null);
		KumamonCopyright(canvas);
	}
	
	public void ChangeImage() {
		if(preSingleTap != SingleTap) {
			int[] images = {R.drawable.lock2,R.drawable.lock3,R.drawable.lock4,R.drawable.lock5,};
			Image = BitmapFactory.decodeResource(getResources(), images[randam.nextInt(images.length)]);
			preSingleTap = SingleTap;
			DrawDelayTime = 3000;
		} else {
			if(warking) {
				Image = BitmapFactory.decodeResource(getResources(), R.drawable.lock0);
				warking  = false;
			} else {
				Image = BitmapFactory.decodeResource(getResources(), R.drawable.lock1);
				warking  = true;
			}
			DrawDelayTime = 1000;
		}
	}
	
	private void KumamonCopyright(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		paint.setTextSize(14);
		Resources resource = getResources();
		int x = 280 * displayWidth / 480;
		int y = 580 * displayWidth / 480;
		canvas.drawText(resource.getString(R.string.KumamonCopyright), x, y, paint);
	}
}
