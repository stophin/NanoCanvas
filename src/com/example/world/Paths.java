package com.example.world;

import com.example.world.Constants.*;

public class Paths extends MultiLinkList.Element<Paths> {
	@Override
	public boolean isin(Paths link) {
		// TODO Auto-generated method stub
		return this.atPointXY(new RectF(link.x, link.y, 1, 1)) > 0;
	}
	
	public float x = 0;
	public float y = 0;
	public Paths parent = null;
	
	public MultiLinkList<Roles> collisions = new MultiLinkList<Roles>(Role_Link.Collision);

	public Paths(float x, float y) {
		this.prev = new Paths[Path_Link.Max];
		for (int i= 0; i < this.prev.length; i++) {
			this.prev[i] = null;
		}
		this.next = new Paths[Path_Link.Max];
		for (int i= 0; i < this.next.length; i++) {
			this.next[i] = null;
		}
		
		this.x = x;
		this.y = y;
	}

	public Paths() {
		this.prev = new Paths[Path_Link.Max];
		for (int i= 0; i < this.prev.length; i++) {
			this.prev[i] = null;
		}
		this.next = new Paths[Path_Link.Max];
		for (int i= 0; i < this.next.length; i++) {
			this.next[i] = null;
		}
	}
	
	public Paths clone(Paths path) {
		this.x = path.x;
		this.y = path.y;
		this.f = path.f;
		this.parent = path.parent;
		this.uniqueID = path.uniqueID;
		
		for (int i = 0; i < Path_Link.Max; i++)
		{
			this.prev[i] = path.prev[i];
			this.next[i] = path.next[i];
		}
		
		return this;
	}
	
	public void give(Paths path) {
		this.x = path.x;
		this.y = path.y;
		this.f = path.f;
		this.parent = path.parent;
	}
	
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void getF(Roles role, Paths path) {

		this.f = 0;

		float x = this.x * role.flatting.Width;
		float y = this.y * role.flatting.Height;

		this.collisions.clearLink();
		role.moveFlat(x, y);
		Roles cursor = role.world.cursor;
		float o_x = 0, o_y = 0;
		if (null != cursor)
		{
			o_x = cursor.flatting.X;
			o_y = cursor.flatting.Y;
			cursor.moveFlat(role.flatting.X - cursor.flatting.X, role.flatting.Y - cursor.flatting.Y);
			//cursor.drawAnimation(graphics);
		}
		
		role.world.getCollision(role, this.collisions);
		if (null != this.collisions.link && this.collisions.linkcount > 0)
		{
			Roles collision = this.collisions.link;
			do
			{
				if (null != role.irole && collision.equal(role.irole))
				{
					continue;
				}
				this.f += collision.occupy;

				collision = this.collisions.next(collision);
			} while (null != collision && collision != this.collisions.link);
		}
		role.moveFlat(-x, -y);
		this.collisions.clearLink();
		if (null != cursor)
		{
			cursor.moveFlat(o_x - cursor.flatting.X, o_y - cursor.flatting.Y);
			//cursor.drawAnimation(graphics);
		}

		this.f += Math.abs(this.x - path.x) + Math.abs(this.y - path.y);
	}
	
	public int atPointXY(RectF rect) {
		if (this.x > rect.X - rect.Width && this.x < rect.X + rect.Width &&
				this.y > rect.Y - rect.Height && this.y < rect.Y + rect.Height){
			return 1;
		}
		return 0;
	}
}