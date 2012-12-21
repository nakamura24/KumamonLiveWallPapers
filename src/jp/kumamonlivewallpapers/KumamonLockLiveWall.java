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

import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import android.util.Log;

public class KumamonLockLiveWall extends WallpaperService {
	private static final String Tag ="KumamonLockLiveWall";
	private Context context;
		
	/** 描画用のハンドラを用意 **/
	private final Handler mHandler = new Handler();
	@Override
	public Engine onCreateEngine() {
		context = this;
		// 描画用の自作Engineクラスを返す
		return new LiveEngine();
	}
	/** 描画を行うEngineクラス **/
	public class LiveEngine extends Engine {
		// ここに描画用の処理を記述していく
		/** イメージ **/
		private Bitmap image;
		/** x座標 **/
		private int x = 0;
		/** y座標 **/
		private int y = 0;
		/** 表示状態フラグ **/
		private boolean visible;
		private GestureDetector Detector;
		//private int imageSelect = 0;
		private boolean warking = false;
		private int touchSelect = 0;
		private int displayWidth;
		
		public LiveEngine() {
			// ウィンドウマネージャのインスタンス取得
			WindowManager windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
			// ディスプレイのインスタンス生成
			Display display = windowManager.getDefaultDisplay();
			displayWidth = display.getWidth();
		}
		
		/** 描画用のRunnableオブジェクト **/
		private final Runnable drawRunnable = new Runnable(){
			public void run(){
				// 描画メソッドを呼び出す
				drawFrame();
			}
		};

		/** Engine生成時に呼び出される **/
		@Override
		public void onCreate(SurfaceHolder surfaceHolder){
			super.onCreate(surfaceHolder);
			// タッチイベントを有効
			setTouchEventsEnabled(true);
			// GestureDetecotorクラスのインスタンス生成
			Detector = new GestureDetector(context,onGestureListener);
		}
		/** Engine破棄時に呼び出される **/
		@Override
		public void onDestroy(){
			mHandler.removeCallbacks(drawRunnable);
			if (image != null) {
				// Bitmapデータの解放
				image.recycle();
				image = null;
			}
			super.onDestroy();
		}
		/** 表示状態変更時に呼び出される **/
		@Override
		public void onVisibilityChanged(boolean visible){
			super.onVisibilityChanged(visible);
			this.visible = visible;
			if(visible){
				drawFrame();
			}else{
				mHandler.removeCallbacks(drawRunnable);
			}
		}
		/** サーフェイス生成時に呼び出される **/
		@Override
		public void onSurfaceCreated(SurfaceHolder surfaceHolder){
			super.onSurfaceCreated(surfaceHolder);
			Log.i(Tag, "onSurfaceCreated");
		}
		/** サーフェイス変更時に呼び出される **/
		@Override
		public void onSurfaceChanged(SurfaceHolder holder,int format, int width , int height){
			super.onSurfaceChanged(holder, format, width, height);
			Log.i(Tag, "onSurfaceChanged");
			drawFrame();
		}
		/** サーフェイス破棄時に呼び出される **/
		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder){
			super.onSurfaceDestroyed(holder);
			Log.i(Tag, "onSurfaceDestroyed");
			visible = false;
			mHandler .removeCallbacks(drawRunnable);
			if (image != null) {
				// Bitmapデータの解放
				image.recycle();
				image = null;
			}
		}
		/** オフセット変更時に呼び出される **/
		@Override
		public void onOffsetsChanged(float xOffset, float yOffset, float xStep, float yStep, int xPixels, int yPixels){
			super.onOffsetsChanged(xOffset, yOffset, xStep, yStep, xPixels, yPixels);
			Log.i(Tag, "onOffsetsChanged");
		}
		/** キャンバスで描画を行う **/
		private void drawFrame(){
			final SurfaceHolder holder = getSurfaceHolder();
			Canvas c = null;
			try{
				// キャンバスをロック
				c = holder.lockCanvas();
				if(c != null){
					// 描画
					c.drawColor(Color.WHITE);
					if(warking) {
						image = BitmapFactory.decodeResource(getResources(), R.drawable.lock0);
						warking  = false;
					} else {
						image = BitmapFactory.decodeResource(getResources(), R.drawable.lock1);
						warking  = true;
					}
					c.drawBitmap(image, x, y, null);
					KumamonCopyright(c);
					Log.d(Tag, "drawFrame");
				}
				x = 0;
				y = 0;
			} catch (Exception e) {
				Log.e(Tag, e.getMessage());
			}finally{
				// Canvas アンロック
				if(c != null){
					holder.unlockCanvasAndPost(c);
				}
			}
			// 次の描画をセット
			mHandler.removeCallbacks(drawRunnable);
			if(visible){
				mHandler.postDelayed(drawRunnable, 1000);
			}
		}
		private void drawFrame2(){
			final SurfaceHolder holder = getSurfaceHolder();
			int[] Images = {R.drawable.lock2,R.drawable.lock3,R.drawable.lock4,R.drawable.lock5,};
			Canvas c = null;
			try{
				// キャンバスをロック
				c = holder.lockCanvas();
				if(c != null){
					// 描画
					c.drawColor(Color.WHITE);
					image = BitmapFactory.decodeResource(getResources(), Images[touchSelect]);
					c.drawBitmap(image, x, y, null);
					KumamonCopyright(c);
					Log.d(Tag, "drawFrame2");
				}
				x = 0;
				y = 0;
			} catch (Exception e) {
				Log.e(Tag, e.getMessage());
			}finally{
				// Canvas アンロック
				if(c != null){
					holder.unlockCanvasAndPost(c);
				}
			}
			// 次の描画をセット
			mHandler.removeCallbacks(drawRunnable);
			if(visible){
				mHandler.postDelayed(drawRunnable, 3000);
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
		//Randomクラスのインスタンス化
		private Random rnd = new Random();
		/** タッチイベント **/
		@Override
		public void onTouchEvent(MotionEvent event){
			super.onTouchEvent(event);
			// タッチイベントをGestureDetector#onTouchEventメソッドに
			Detector.onTouchEvent(event);
		}
		// 複雑なタッチイベントを取得
		private final SimpleOnGestureListener onGestureListener = new SimpleOnGestureListener() {
			@Override
			public boolean onDoubleTap(MotionEvent event) {
				Log.d(Tag, "onDoubleTap");
				return super.onDoubleTap(event);
			}
			@Override
			public boolean onDoubleTapEvent(MotionEvent event) {
				Log.d(Tag, "onDoubleTapEvent");
				return super.onDoubleTapEvent(event);
			}
			@Override
			public boolean onDown(MotionEvent event) {
				Log.d(Tag, "onDown");
				return super.onDown(event);
			}
			@Override
			public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
				Log.d(Tag, "onFling");
				return super.onFling(event1, event2, velocityX, velocityY);
			}
			@Override
			public void onLongPress(MotionEvent event) {
				Log.d(Tag, "onLongPress");
				super.onLongPress(event);
			}
			@Override
			public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX, float distanceY) {
				Log.d(Tag, "onScroll");
				return super.onScroll(event1, event2, distanceX, distanceY);
			}
			@Override
			public void onShowPress(MotionEvent event) {
				Log.d(Tag, "onShowPress");
				super.onShowPress(event);
			}
			@Override
			public boolean onSingleTapConfirmed(MotionEvent event) {
				Log.d(Tag, "onSingleTapConfirmed");
				return super.onSingleTapConfirmed(event);
			}
			@Override
			public boolean onSingleTapUp(MotionEvent event) {
				Log.d(Tag, "onSingleTapUp");
				touchSelect = rnd.nextInt(4);
				drawFrame2();
				return super.onSingleTapUp(event);
			}
		};
	}
}
