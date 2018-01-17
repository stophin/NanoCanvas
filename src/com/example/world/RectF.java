package com.example.world;

public class RectF {
	public float X;
	public float Y;
	public float Width;
	public float Height;
	
	public RectF() {
		this.X = 0;
		this.Y = 0;
		this.Width = 0;
		this.Height = 0;
	}
	public RectF(int x, int y, int w, int h) {
		this.X = x;
		this.Y = y;
		this.Width = w;
		this.Height = h;
	}
	
	public RectF(float x, float y, float w, float h) {
		this.X = x;
		this.Y = y;
		this.Width = w;
		this.Height = h;
	}
	
	public RectF scale(float rate) {
		this.Width *= rate;
		this.Height *= rate;
		this.X *= rate;
		this.Y *= rate;
		
		return this;
	}
	
	public RectF clone(RectF rect) {
		this.Width = rect.Width;
		this.Height = rect.Height;
		this.X = rect.X;
		this.Y = rect.Y;
		
		return this;
	}
	
	public int atPointXY(float x, float y) {
		if (x > this.X && x < this.X + this.Width &&
		y > this.Y && y < this.Y + this.Height)
		{
			return 1;
		}
		return 0;
	}
	
	public RectF set(float x, float y, float w, float h) {
		this.X = x;
		this.Y = y;
		this.Width = w;
		this.Height = h;
		
		return this;
	}

	public void give(android.graphics.RectF rect) {
		rect.left = this.X;
		rect.top = this.Y;
		rect.right = this.Width;
		rect.bottom = this.Height;
	}
	public void give(android.graphics.Rect rect) {
		rect.left = (int)this.X;
		rect.top = (int)this.Y;
		rect.right = (int)this.Width;
		rect.bottom = (int)this.Height;
	}
}