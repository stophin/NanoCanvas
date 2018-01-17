package com.example.world;

import com.example.world.Constants;

public class Projection {
	public static float DEF_OFFSETX = 100;
	public static float e = 0.7f;
	public static float degree = 30 * Constants.PI / 180;
	public static float degree1 = 45 * Constants.PI / 180;
	public static float cos45 =  -(float)Math.cos(degree1);
	public static float sin45 = (float)Math.sin(degree1);
	public static float tan45 = sin45 / cos45;
	public static float parameter = (float)Math.sqrt(1 - e * e) / (float)Math.sqrt(1 - e * e * cos45 * cos45);
	public static float offset = DEF_OFFSETX;

	public static PointF projectShow(float x, float y)
	{
		if (x == 0)
		{
			x = Constants.DEF_ZERO;
		}
		float h = (float)Math.atan(y / x);
	
		if (y > 0)
		{
			if (x < 0)
			{
				h += Constants.PI;
			}
		}
		else
		{
			if (x >= 0)
			{
				h += 2 * Constants.PI;
			}
			else if (x < 0)
			{
				h += Constants.PI;
			}
		}
	
		float r = (float)Math.sqrt(x * x + y * y);
	
		float sx = r * (float)Math.cos(h + degree);
		float sy = r * (float)Math.sin(h + degree);
	
		float dx = offset + sx + sy * parameter * cos45;
		float dy = sy * parameter * sin45;
		return new PointF(dx, dy);
	}

	public static  PointF projectFlat(float x, float y)
	{
		float sy = y / (sin45 * parameter);
		float sx = x - offset - sy * parameter * cos45;
	
		if (sx == 0)
		{
			sx = Constants.DEF_ZERO;
		}
		float gh =(float)Math.atan(sy / sx);
	
		if (sy > 0)
		{
			if (sx < 0)
			{
				gh += Constants.PI;
			}
		}
		else
		{
			if (sx > 0)
			{
				gh += 2 * Constants.PI;
			}
			else if (sx < 0)
			{
				gh += Constants.PI;
			}
		}
	
		float r = (float)Math.sqrt(sx * sx + sy * sy);
	
		float dx = r * (float)Math.cos(gh - degree);
		float dy = r * (float)Math.sin(gh - degree);
	
		return new PointF(dx, dy);
	}

}