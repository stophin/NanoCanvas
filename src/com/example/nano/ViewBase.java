package com.example.nano;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public abstract class ViewBase extends View implements Runnable {
	protected Paint mPaint = null;
	
	public ViewBase(Context context) {
		super(context);
		mPaint = new Paint();
		new Thread(this).start();
	}
	
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			try {
				Thread.sleep(100);
			}catch(InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			postInvalidate();
		}
	}
	
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	}
	
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return false;
	}
	
	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
		return true;
	}
}