package com.example.world;

import com.example.nano.CanvasL;
import android.graphics.Bitmap;

public class Resources extends LinkList.Element<Resources> {
	public Bitmap image = null;
	public String src;
	public float width;
	public float height;
	
	public Resources(CanvasL canvas, String filename, int resourceID) {
		
		this.src = filename;
		this.uniqueID = resourceID;
		
		//BitmapFactory.decodeResource(getResources(), R.drawable.block)
		String path = "drawable";
		int rID = canvas.getFileIndex(filename, path);
		this.image = canvas.getBitmap(rID);
	}
}