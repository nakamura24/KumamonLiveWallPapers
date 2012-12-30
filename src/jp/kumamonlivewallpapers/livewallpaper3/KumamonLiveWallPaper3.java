/*
 * Copyright (C) 2012 M.Nakamura
 *
 * This software is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 2.1 Japan License.
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		http://creativecommons.org/licenses/by-nc-sa/2.1/jp/legalcode
 */

package jp.kumamonlivewallpapers.livewallpaper3;

import jp.kumamonlivewallpapers.R;
import jp.template.LiveWallPaper;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.Display;
import android.view.WindowManager;

public class KumamonLiveWallPaper3 extends LiveWallPaper {
	private static final int[] images = {R.drawable.kuma6,R.drawable.kuma5,};
	private int width;
	private int hight;
	private int displayWidth;
	private int position;

	 @Override
	 public void onCreate() {
		 super.onCreate();
		 // ウィンドウマネージャのインスタンス取得
		 WindowManager windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
		 // ディスプレイのインスタンス生成
		 Display display = windowManager.getDefaultDisplay();
		 displayWidth = display.getWidth();
		 Image = BitmapFactory.decodeResource(getResources(), R.drawable.kuma6);
		 width = Image.getWidth();
		 hight = Image.getHeight();
		 position = (width - displayWidth) / 2;
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
		canvas.drawColor(BackgroundColor);
		ChangeImage();
		if(position < 0) position = 0;
		if(position > (width - displayWidth)) position = (width - displayWidth);
		canvas.drawBitmap(Image, new Rect(position, 0, position + displayWidth, hight),
				new Rect(0, 0, displayWidth, hight), null);
	}

	@Override
	public void ChangeImage() {
		int touchSelect = DoubleTap % images.length;
		Image = BitmapFactory.decodeResource(getResources(), images[touchSelect]);
		position += DistanceX;
		DistanceX = 0;
		width = Image.getWidth();
		hight = Image.getHeight();
	}
}
