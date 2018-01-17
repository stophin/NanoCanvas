package com.example.world;

import com.example.world.Constants.Relation;

public class Relations {
	public static int getRelation(Roles tango, Roles item) {
		if (tango == null || item == null)
		{
			return Relation.A;
		}
		float item_posX = item.flatting.X;
		float item_posY = item.flatting.Y;
		float item_geomW = item.flatting.Width;
		float item_geomH = item.flatting.Height;
		float tango_posX = tango.flatting.X;
		float tango_posY = tango.flatting.Y;
		float tango_geomW = tango.flatting.Width;
		float tango_geomH = tango.flatting.Height;

		item_posY += item_geomH;
		tango_posY += tango_geomH;
		
		return getRelationExternal(item_posX, item_posY, item_geomW, item_geomH, tango_posX, tango_posY, tango_geomW, tango_geomH);
	}
	
	public static int getRelationExternal(
			float item_posX, float item_posY, float item_geomW, float item_geomH,
			float tango_posX, float tango_posY, float tango_geomW, float tango_geomH) {

		if (item_posX >= tango_posX && item_posX <= tango_posX + tango_geomW)
		{
			if (item_posY <= tango_posY && item_posY - item_geomH >= tango_posY - tango_geomH)
			{
				return Relation.D;
			}
			if (item_posY - item_geomH <= tango_posY && item_posY >= tango_posY - tango_geomH)
			{
				return Relation.D;
			}
		}
		if (item_posX + item_geomW >= tango_posX && item_posX + item_geomW <= tango_posX + tango_geomW)
		{
			if (item_posY <= tango_posY && item_posY - item_geomH >= tango_posY - tango_geomH)
			{
				return Relation.D;
			}
			if (item_posY - item_geomH <= tango_posY && item_posY >= tango_posY - tango_geomH)
			{
				return Relation.D;
			}
		}
		if (item_posX + item_geomW < tango_posX + tango_geomW && item_posY - item_geomH < tango_posY)
		{
			return Relation.A;
		}
		if (item_posX < tango_posX + tango_geomW && item_posY < tango_posY - tango_geomH)
		{
			return Relation.A;
		}
		if (item_posX >= tango_posX + tango_geomW && item_posY - item_geomH >= tango_posY)
		{
			return Relation.B;
		}
		if (item_posX + item_geomW >= tango_posX && item_posY - item_geomH >= tango_posY - tango_geomH)
		{
			return Relation.B;
		}
		if (item_posX + item_geomW < tango_posX && item_posY >= tango_posY - tango_geomH)
		{
			return Relation.C;
		}
		if (item_posX >= tango_posX + tango_geomW && item_posY - item_geomH < tango_posY)
		{
			return Relation.C;
		}

		return Relation.A;
	}
	

	public static boolean isRelationD(int relation)
	{
		return (relation > Relation.C);
	}

	public static String stringRelation(int relation)
	{
		String string = "";
		switch (relation)
		{
		case Relation.A:
			string = "A";
			break;
		case Relation.B:
			string = "B";
			break;
		case Relation.C:
			string = "C";
			break;
		case Relation.D:
			string = "D";
			break;
		case Relation.D_L:
			string = "D_L";
			break;
		case Relation.D_R:
			string = "D_R";
			break;
		case Relation.D_T:
			string = "D_T";
			break;
		case Relation.D_B:
			string = "D_B";
			break;
		}
		return string;
	}
}