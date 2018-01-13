package com.example.world;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

public class Role {
	private Rect geometry;
	private Point velocity;
	
	public Role() {
		geometry = new Rect(0, 0, 0 + 100, 0 + 100);
		velocity = new Point(10, 10);
	}
	
	public Rect getGeometry() {
		return this.geometry;
	}
	
	public void Move() {
		geometry.offset(velocity.x, velocity.y);
	}
	
	public void Boundary(int width, int height) {
		if (geometry.left < 0) {
			geometry.offset(- geometry.left, 0);
			velocity.x = - velocity.x;
		} else if (geometry.right > width) {
			geometry.offset(width- geometry.right, 0);
			velocity.x = - velocity.x;
		}
		
		if (geometry.top < 0) {
			geometry.offset(0, -geometry.top);
			velocity.y = - velocity.y;
		} else if (geometry.bottom > height) {
			geometry.offset(0, height - geometry.bottom);
			velocity.y = - velocity.y;
		}
	}
}