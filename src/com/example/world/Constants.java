package com.example.world;

public class Constants {
	
	public static class Animation_Mode {
		public static int Still = 0;
		public static int Step = 1;
		public static int Auto = 2;
		public static int Loop = 3;
	}
	// AnimationID Calculate:
	// Animation_Base + Animation_Direction
	// e.g. moving object heading right up
	// Move:100 + Right_Up:104
	// then the animation of player's definition is
	// 204
	public static class Animation_Base {
		public static int Still = 0;
		public static int Move = 100;
		public static int Run = 200;
		public static int Attack = 300;
		public static int Dead = 400;
	}

	// When using 8 directions use this variable
//	public static class Animation_Direction {
//		public static int Undefined = 0;
//		public static int Left = 1;
//		public static int Left_Up = 2;
//		public static int Up = 3;
//		public static int Right_Up = 4;
//		public static int Right = 5;
//		public static int Right_Down = 6;
//		public static int Down = 7;
//		public static int Left_Down = 8;
//	}
	// When using 4 directions use this variable
	// Just using some duplicated value
	public static class Animation_Direction {
		public static int Undefined = 0;
		public static int Left = 1;
		public static int Left_Up = 3;
		public static int Up = 3;
		public static int Right_Up = 3;
		public static int Right = 5;
		public static int Right_Down = 7;
		public static int Down = 7;
		public static int Left_Down = 7;
	}
	
	public static class Role_Link {
		public static int Role = 0;
		public static int Sort = 1;
		public static int Object = 2;
		public static int Back = 3;
		public static int Front = 4;
		public static int Player = 5;
		public static int Collision = 6;
		public static int Sortsbacks = 7;
		public static int QuadTree = 8;
		public static int Temp1 = 9;
		public static int Temp2 = 10;
		public static int Max = 11;
	}
	
	public static class Role_Type {
		public static final int Disabled = 0;
		public static final int Normal = 1;
		public static final int Back = 2;
		public static final int Cursor = 3;
		public static final int Pitch = 4;
		public static final int Player = 5;
	}
	
	
	public static class Path_Link {
		public static int Path = 0;
		public static int Open = 1;
		public static int Close = 2;
		public static int Max = 3;
	}
	
	public static float DEF_ZERO = (float) 1e-10;
	
	public static boolean DEF_ISZERO(float x) {
		return (x > -DEF_ZERO && x < DEF_ZERO);
	}
	
	public static class Relation {
		public static final int A = 0;
		public static final int B = 1;
		public static final int C = 2;
		public static final int D = 3;
		public static final int D_L = 4;
		public static final int D_T = 5;
		public static final int D_R = 6;
		public static final int D_B = 7;
	}
	
	public static class RB_Color {
		public static int Red = 0;
		public static int Black = 1;
	}
	
	public static int DEF_ASTAR_LIMIT = 100;
	public static int DEF_FETCH_SPEED = 2;
	public static int DEF_OCCUPY = 100;
	
	public static int DEF_QUAD_W = 10000;
	public static int DEF_QUAD_H = 10000;

	public static int DEF_RANGE_W = 2;
	public static int DEF_RANGE_H = 2;

	public static int DEF_HEALTH = 100;
	
	public static int Astar_Infinity = 99999999;
	
	public static float PI = (float) 3.141592654;
}