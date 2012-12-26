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

import java.util.Calendar;

import jp.template.LiveWallPaper;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.Display;
import android.view.WindowManager;

public class KumamonLiveWall1 extends LiveWallPaper {
	private int displayWidth;

	@Override
	public void onCreate() {
		super.onCreate();
		// ウィンドウマネージャのインスタンス取得
		WindowManager windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
		// ディスプレイのインスタンス生成
		Display display = windowManager.getDefaultDisplay();
		displayWidth = display.getWidth();
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
		if(BatteryLevel <= BatteryScale / 2) {
			// 描画
			canvas.drawColor(Color.rgb(0xff, (int)(0xff * (double)BatteryLevel * 2 / BatteryScale), 0));
		} else {
			// 描画
			canvas.drawColor(Color.rgb((int)(0xff * (2 - (double)BatteryLevel * 2 / BatteryScale)), 0xff, 0));
		}
		ChangeImage();
		canvas.drawBitmap(Image, 0, 0, null);
		KumamonCopyright(canvas);
	}

	@Override
	public void ChangeImage() {
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

	private void KumamonCopyright(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		paint.setTextSize(14 * displayWidth / 480);
		Resources resource = getResources();
		int x = 260 * displayWidth / 480;
		int y = 680 * displayWidth / 480;
		canvas.drawText(resource.getString(R.string.KumamonCopyright), x, y, paint);
	}
}
