package com.example.world;

public class PointF {
	public float X;
	public float Y;
	
	public PointF() {
		this.X = 0;
		this.Y = 0;
	}

	public PointF(int x, int y) {
		this.X = x;
		this.Y = y;
	}
	public PointF(float x, float y) {
		this.X = x;
		this.Y = y;
	}
	
	public PointF add(PointF pt) {
		this.X += pt.X;
		this.Y += pt.Y;
		
		return this;
	}
	
	public PointF minus(PointF pt) {
		this.X -= pt.X;
		this.Y -= pt.Y;
		
		return this;
	}

	public PointF multipy(float a) {
		this.X *= a;
		this.Y *= a;
		
		return this;
	}
	
	public PointF clone(PointF pt) {
		this.X = pt.X;
		this.Y = pt.Y;
		
		return this;
	}
	
	public PointF set(float x, float y) {
		this.X = x;
		this.Y = y;
		
		return this;
	}
	
	public PointF normalize() {
		float sa = this.X * this.X + this.Y * this.Y;
		float a = (float) Math.sqrt(sa);
		float rX = (float) Math.sqrt(1 / (1 + sa));
		float rY = (float) Math.sqrt(a / (a + sa));
		if (this.X < 0)
		{
			rX = -rX;
		}
		if (this.Y < 0)
		{
			rY = -rY;
		}
		this.X = rX;
		this.Y = rY;
		return this;
	}
}