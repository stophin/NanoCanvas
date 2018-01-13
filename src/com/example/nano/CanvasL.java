package com.example.nano;

import com.example.world.Role;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.DisplayMetrics;

public class CanvasL extends ViewBase {
	
	private Role role;
	
	public CanvasL(Context context) {
		super(context);
		
		role = new Role();
	}
	
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		role.Boundary(getWidth(), getHeight());
		role.Move();

		mPaint.setColor(Color.GREEN);
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.block);
		canvas.drawBitmap(bmp, null, new Rect(0, 0, getWidth(), getHeight()), mPaint);
		canvas.drawBitmap(bmp, new Rect(50, 50, 100, 100), new Rect(role.getGeometry().left, role.getGeometry().top, 50 + role.getGeometry().left, 50 + role.getGeometry().top), mPaint);

		//mPaint.setColor(Color.BLACK);
		//canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), mPaint);
		
		//mPaint.setColor(Color.GREEN);
		//canvas.drawRect(role.getGeometry(), mPaint);
		//canvas.drawLine(0, 0, getWidth() , getHeight(), mPaint);
		
	}
}