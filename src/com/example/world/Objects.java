package com.example.world;

import com.example.nano.CanvasL;

public class Objects extends LinkList.Element<Objects> {
	
	public RectF destination;
	public RectF truncation;
	public float scale = 1;
	public int reverse = 0;

	public Objects() {
		this.destination = null;
		this.truncation = null;
		this.scale = 1;
		
		this.resource = null;
	}

	public Objects(Resources resource, RectF destination, RectF truncation) {
		this.destination = destination;
		this.truncation = truncation;
		this.scale = 1;
		this.destination.scale(scale);
		
		this.resource = resource;
	}
	
	public Objects(Resources resource, RectF destination, RectF truncation, float scale) {
		this.destination = destination;
		this.truncation = truncation;
		this.scale = scale;
		this.destination.scale(scale);
		
		this.resource = resource;
	}
	
	public void drawObject(CanvasL canvas) {
//		if (!this.resource || !this.resource.image) {
//			return;
//		}
//		if (!this.world) {
//			return;
//		}
		try {
			if (1 == this.reverse){
				canvas.drawBitmap(this.resource.image_r,
						(float)this.resource.image_r.getWidth() - this.truncation.X, this.truncation.Y,
						this.truncation.Width, this.truncation.Height,
						this.destination.X + this.world.display.X,
						this.destination.Y + this.world.display.Y,
						this.destination.Width, this.destination.Height);
			} else {
				canvas.drawBitmap(this.resource.image,
						this.truncation.X, this.truncation.Y,
						this.truncation.Width, this.truncation.Height,
						this.destination.X + this.world.display.X,
						this.destination.Y + this.world.display.Y,
						this.destination.Width, this.destination.Height);
			}
		} catch(Exception e) {
			//TODO
		}
	}

	
	public Objects moveObject(float directionX, float directionY) {
		this.destination.X += directionX;
		this.destination.Y += directionY;
		
		return this;
	}
	
	public Resources resource = null;
	public World world = null;

}