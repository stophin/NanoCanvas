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
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class CanvasL extends ViewBase {
	
	private float width;
	private float height;
	private Roles role;
	private Roles cursor;
	private Roles player;
	private World world;
	private ResourceManager resm;
	
	private Context context = null;
	public CanvasL(Context context) {
		super(context);
		this.context = context;
		
		width = getWidth();
		height = getHeight();
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
		

		player = Loading.loadRole(resm, 0.5f);
		player.moveRole(200,  100);
		world.addRole(player, Role_Type.Player);
//		
//		cursor = Loading.loadRole(resm, 1, "cursor");
//		cursor.moveRole(100,  100);
//		world.addRole(cursor, Role_Type.Cursor);
//
//		role = Loading.loadRole(resm, 1, "tree1");
//		role.moveRole(200,  200);
//		world.addRole(role, Role_Type.Normal);
//		role = Loading.loadRole(resm, 1, "tree2");
//		role.moveRole(300,  200);
//		world.addRole(role, Role_Type.Normal);
//		role = Loading.loadRole(resm, 1, "tree3");
//		role.moveRole(400,  200);
//		world.addRole(role, Role_Type.Normal);
		
		Loading.loadScene(resm, world);
		
	}
	private Canvas canvas = null;
	private int isRefresh = 1;
	@Override
	public void onDraw(Canvas canvas) {
		if (0 >= isRefresh) {
			//return;
		}
		isRefresh--;
		
		super.onDraw(canvas);
		
		this.canvas = canvas;

		//onDrawExternal(canvas);
		world.drawRole(this);
		
		if (null != player) {
			player.fetchPath();
		}
		
		this.canvas = null;
	}
	
	public void onAstar(float x, float y) {
		if (null == player) {
			return;
		}
		PointF pt = Projection.projectFlat(x - world.display.X,  y - world.display.Y);
		Paths dest = new Paths((pt.X - player.flatting.X) / player.flatting.Width,
				(pt.Y - player.flatting.Y)/ player.flatting.Height);
		player.astarPath(dest, Constants.DEF_ASTAR_LIMIT);
	}
	
	private PointF start = new PointF();
	private int isDrag = 0;
	public void onDrag(float x, float y, int mode) {
		if (1 == mode) {
			start.X = x - world.leftTop.X;
			start.Y = y + world.leftTop.Y;
		} else if (2 == mode) {
			if (!Constants.DEF_ISZERO(start.X) && !Constants.DEF_ISZERO(start.Y)) {
				world.offset(x - world.leftTop.X - start.X, y - world.leftTop.Y - start.Y);
				isDrag = 1;
			}
		} else {
			start.X = 0;
			start.Y = 0;
			isDrag = 0;
		}
		isRefresh++;
	}
	
	private PointF mouse = new PointF();
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mouse.set(event.getX(), event.getY());
		 switch (event.getAction()) {
		 case MotionEvent.ACTION_DOWN://0
			 onDrag(mouse.X, mouse.Y, 1);
		     break;
		 case MotionEvent.ACTION_UP://1
			 if (0 == isDrag) {
				 onAstar(mouse.X, mouse.Y);
			 } else {
				 onDrag(mouse.X, mouse.Y, 0);
			 }
		     break;
		 case MotionEvent.ACTION_MOVE://2
			 onDrag(mouse.X, mouse.Y, 2);
		     break;    
		 }
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
	

	
	public int getFileIndex(String imageName, String path) {
		 return this.context.getResources().getIdentifier(imageName, path , this.context.getPackageName()); 
	}

	public InputStream openFile(int rId) {
		return this.context.getResources().openRawResource(rId);
	}
	
	public Bitmap getBitmap(int rId) {
		//Load image using it's raw geometry without stretch
	    TypedValue value = new TypedValue();
	    android.content.res.Resources resources = getResources();
	    resources.openRawResource(rId, value);
	    BitmapFactory.Options opts = new BitmapFactory.Options();
	    opts.inTargetDensity = value.density;
	    return BitmapFactory.decodeResource(resources, rId, opts);
	}
	
	public static Trigger trigger = new Trigger() {

		@Override
		public int run(Object lParam, Object wParam) {
			// TODO Auto-generated method stub
			return 0;
		}
		
	};
	
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
	
}