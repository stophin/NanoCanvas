package com.example.nano;

import java.io.InputStream;

import com.example.world.*;
import com.example.world.ObjectManager.Trigger;
import com.example.world.Constants.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Rect;

public class CanvasL extends ViewBase {
	
	private float width;
	private float height;
	private Role role;
	private Roles cursor;
	private World world;
	private ResourceManager resm;
	
	private Context context = null;
	public CanvasL(Context context) {
		super(context);
		this.context = context;
		
		width = getWidth();
		height = getHeight();
		role = new Role();
		//Initialize
		world = new World(10);
		resm = new ResourceManager(this);
		world.resize(width, height);
		
		Loading.loadResources(resm);
		
//		cursor = new Roles();
//		Animations animation = new Animations();
//		animation.delay = 1;
//		animation.mode = Animation_Mode.Step;
//		cursor.animations.addAnimation(animation, 233)
//		.addSequence(new ObjectManager(), 3455)
//		.addStep(new Objects(resm.getResource(22), 
//				new RectF(0, 0, 10, 14),
//				new RectF(0, 0, 10, 14)), 202);
//		cursor.setFlatting(new RectF(0, 0, 10, 13), 14);
//		cursor.moveRole(100,  100);
//		world.addRole(cursor, Role_Type.Cursor);
		
		Loading.loadMap(resm, world);
		

		cursor = Loading.loadRole(resm, 0.5f);
		cursor.moveRole(200,  100);
		world.addRole(cursor, Role_Type.Player);
		
		cursor = Loading.loadRole(resm, 1, "cursor");
		cursor.moveRole(100,  100);
		world.addRole(cursor, Role_Type.Cursor);
	}
	private Canvas canvas = null;
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		this.canvas = canvas;

		//onDrawExternal(canvas);
		world.drawRole(this);
		
		this.canvas = null;
	}
	
	public int getFileIndex(String imageName, String path) {
		 return this.context.getResources().getIdentifier(imageName, path , this.context.getPackageName()); 
	}

	public InputStream openFile(int rId) {
		return this.context.getResources().openRawResource(rId);
	}
	
	public Bitmap getBitmap(int rId) {
		return BitmapFactory.decodeResource(getResources(), rId);
	}
	
	private RectF truncation = null;
	private RectF destination = null;
	private android.graphics.Rect trunc = null;
	private android.graphics.RectF truncF = null;
	private android.graphics.RectF desti = null;
	public void drawBitmap(Bitmap image,
			float truncation_x, float truncation_y,
			float truncation_width, float truncation_height,
			float destination_x, float destination_y, 
			float destination_width, float destination_height) {
		if (null == this.canvas) {
			return;
		}
		if (null == truncation) {
			truncation = new RectF();
		}
		if (null == destination) {
			destination = new RectF();
		}
		truncation.set(truncation_x, truncation_y,
						truncation_x + truncation_width, truncation_y +  truncation_height);
		destination.set(destination_x, destination_y,
						destination_x + destination_width, destination_y +  destination_height);
		//Don't know why here must be multipied by 2
		truncation.scale(2);
		destination.scale(2);

		if (null == trunc) {
			trunc = new android.graphics.Rect();
		}
		truncation.give(trunc);
//		if (null == truncF) {
//			truncF = new android.graphics.RectF();
//		}
//		truncation.give(truncF);
		if (null == desti) {
			desti = new android.graphics.RectF();
		}
		destination.give(desti);
		canvas.drawBitmap(image, trunc, desti, mPaint);
		
		mPaint.setStyle(Style.STROKE);
		//mPaint.setColor(Color.GREEN);
		//canvas.drawRect(trunc, mPaint);
		mPaint.setColor(Color.RED);
		canvas.drawRect(desti, mPaint);
	}
	
	public static Trigger trigger = new Trigger() {

		@Override
		public int run(Object lParam, Object wParam) {
			// TODO Auto-generated method stub
			return 0;
		}
		
	};
}