package com.example.world;

import com.example.nano.CanvasL;
import com.example.world.Constants.*;
import com.example.world.Constants.Role_Type;

public class Roles extends MultiLinkList.Element<Roles>{
	public Roles() {
		this.prev = new Roles[Role_Link.Max];
		for (int i = 0; i < this.prev.length; i++) {
			this.prev[i] = null;
		}
		this.next = new Roles[Role_Link.Max];
		for (int i = 0; i < this.next.length; i++) {
			this.next[i] = null;
		}
	}
	@Override
	public boolean isin(Roles link) {
		// TODO Auto-generated method stub
		return (this == link);
	}
	
	public int count = 0;
	public int attacked = 0;
	public int health = Constants.DEF_HEALTH;
	
	public Roles dir[] = {null, null, null, null};
	public Roles _dir[] = {null, null, null, null};
	public Roles up = this.dir[0];
	public Roles down = this.dir[1];
	public Roles left = this.dir[2];
	public Roles right = this.dir[3];
	public Roles _up = this._dir[0];
	public Roles _down = this._dir[1];
	public Roles _left = this._dir[2];
	public Roles _right = this._dir[3];
	
	public PointF oFlat = new PointF(0, 0);
	public PointF oRole = new PointF(0, 0);
	
	public Roles irole = null;
	//Interactive range
	public PointF range = new PointF(Constants.DEF_RANGE_W, Constants.DEF_RANGE_H);
	
	public int base = Animation_Base.Still;
	public int direction = Animation_Direction.Down;
	
	public float scale = 1;
	
	public World world = null;
	public int type = Role_Type.Normal;
	public int relation = Relation.A;
	public AnimationManager animations = new AnimationManager();
	
	public Object quadTree = null;
	
	public int following = 0;
	public float tall = 0;
	public RectF position = new RectF();
	public RectF flatting = new RectF();
	public PointF leftTop = new PointF();
	public PointF vLeftBottom = new PointF();
	public PointF vRightBottom = new PointF();
	public PointF vRightTop = new PointF();
	
	public int fetchSpeed = Constants.DEF_FETCH_SPEED;
	public int fetchCount = 0;
	public int occupy = Constants.DEF_OCCUPY;
	
	public Paths prevPath = null;
	public Paths nextPath = null;
	public int pathCount = 0;
	public MultiLinkList<Paths> paths = new MultiLinkList<Paths>(Path_Link.Path);
	public MultiLinkList<Paths> open = new MultiLinkList<Paths>(Path_Link.Open);
	public MultiLinkList<Paths> close = new MultiLinkList<Paths>(Path_Link.Close);
	public int moving = 0;
	public Roles srole = null;
	public PointF speed = new PointF();
	public PointF enclose = new PointF();
	
	public void controlForce()
	{
		if (null == this.srole)
		{
			return;
		}
		this.removePath();
		//this.speed.add(this.enclose);
		//this.enclose.minus(new PointF(1,1));
		this.enclose.X = this.position.X - this.srole.position.X ;
		this.enclose.Y = this.position.Y - this.srole.position.Y;
		this.speed.normalize().multipy( 1 / this.fetchSpeed);
		this.enclose.normalize();//.multipy(1 / this.fetchSpeed);
		float dx = this.speed.X + this.enclose.X;
		float dy = this.speed.Y + this.enclose.Y;
		this.moveDelta(dx, dy);
	}
	public boolean equal(Roles role) {
		if (role == null) {
			return false;
		}
		return (this.uniqueID == role.uniqueID);
	}
	
	public void drawAnimation(CanvasL canvas) {

		this.animations.drawAnimation(canvas);
		
//		if (this.attacked && this.health > 0)
//		{
//			var g = graphics.getContext("2d");
//			g.strokeStyleLast = g.strokeStyle;
//			g.strokeStyle = 'rgba(255,0,0,1)';
//			g.strokeText(this.health, this.position.X, this.position.Y);
//			g.strokeStyle = g.strokeStyleLast;
//		}
	}
	
	public void setFlatting(RectF flatting, float tall) {
		this.tall = tall;
		this.flatting = flatting;
		
		this.updateFlatting();
	}
	
	public void updateFlatting() {
		this.leftTop = Projection.projectShow(this.flatting.X, this.flatting.Y);
		this.vLeftBottom = Projection.projectShow(this.flatting.X, this.flatting.Y + this.flatting.Height).minus(this.leftTop);
		this.vRightTop = Projection.projectShow(this.flatting.X + this.flatting.Width, this.flatting.Y).minus(this.leftTop);
		this.vRightBottom = Projection. projectShow(this.flatting.X + this.flatting.Width, this.flatting.Y + this.flatting.Height).minus(this.leftTop);

		float directionX = this.position.X;
		float directionY = this.position.Y;

		this.position.X = this.leftTop.X + this.vLeftBottom.X;
		//this.position.Y = this.leftTop.Y - this.tall;
		this.position.Y = this.leftTop.Y  + this.vRightBottom.Y - this.tall;
		this.position.Width = this.vRightTop.X - this.vLeftBottom.X;
		//this.position.Height = this.vRightBottom.Y + this.tall;
		this.position.Height = this.tall;

		this.animations.moveAnimation(this.position.X - directionX, this.position.Y - directionY);
	}

	public void moveRole(float directionX, float directionY) {
		moveRole(directionX, directionY, 0);
	}
	public void moveRole(float directionX, float directionY, int mode) {
		this.oRole.X += directionX;
		this.oRole.Y += directionY;
		
		if (0 == mode) {
			this.moveRoleEx();
		}
	}
	
	public void moveRoleEx() {
		if (Constants.DEF_ISZERO(this.oRole.X) && Constants.DEF_ISZERO(this.oRole.Y)) {
			return;
		}
		float directionX = this.oRole.X;
		float directionY = this.oRole.Y;
		this.oRole.X = 0;
		this.oRole.Y = 0;

		this.position.X += directionX;
		this.position.Y += directionY;
		this.animations.moveAnimation(directionX, directionY);

		directionX = this.flatting.X;
		directionY = this.flatting.Y;

		PointF point = Projection.projectFlat(this.position.X - this.vLeftBottom.X, this.position.Y + this.tall - this.vRightBottom.Y);
		this.flatting.X = point.X;
		this.flatting.Y = point.Y;

		directionX -= this.flatting.X;
		directionY -= this.flatting.Y;
		
		this.leftTop = Projection.projectShow(this.flatting.X, this.flatting.Y);
	}

	public void moveFlat(float directionX, float directionY) {
		moveFlat(directionX, directionY, 0);
	}
	public void moveFlat(float directionX, float directionY, int mode) {

		this.oFlat.X += directionX;
		this.oFlat.Y += directionY;

		if (0 == mode) {
			this.moveFlatEx();
		}
	}
	
	public void moveFlatEx() {

		if (Constants.DEF_ISZERO(this.oFlat.X) && Constants.DEF_ISZERO(this.oFlat.Y)) {
			return;
		}
		float directionX = this.oFlat.X;
		float directionY = this.oFlat.Y;
		this.oFlat.X = 0;
		this.oFlat.Y = 0;

		this.flatting.X += directionX;
		this.flatting.Y += directionY;

		this.leftTop = Projection.projectShow(this.flatting.X, this.flatting.Y);

		directionX = this.position.X;
		directionY = this.position.Y;

		this.position.X = this.leftTop.X + this.vLeftBottom.X;
		//this.position.Y = this.leftTop.Y - this.tall;
		this.position.Y = this.leftTop.Y + this.vRightBottom.Y - this.tall;

		this.animations.moveAnimation(this.position.X - directionX, this.position.Y - directionY);
	}
	

	public void nextAnimation()
	{
		this.animations.nextAnimation(this, this.moving);
		// Control Force
		this.controlForce();
	}
	public Animations selectAnimation(int animationID)
	{
		if (this.animations.animation.link.uniqueID == animationID)
		{
			return this.animations.animation.link;
		}
		
		Animations animation = this.animations.animation.getLink(animationID);
		if (animation != null)
		{
			this.animations.animation.link = animation;
		}
		return animation;
	}
	

	public void astarPath(Paths dest, int limit)
	{
		this.removePath();
		Paths start = new Paths(0, 0);
		start.f = 0;
		this.open.insertLink(start);
		dest.f = Constants.Astar_Infinity;
		this.open.insertSort(dest);
	
		Paths pt_l = new Paths(0, 0);
		Paths pt_r = new Paths(0, 0);
		Paths pt_t = new Paths(0, 0);
		Paths pt_b = new Paths(0, 0);
		Paths pt_lt = new Paths(0, 0);
		Paths pt_lb = new Paths(0, 0);
		Paths pt_rt = new Paths(0, 0);
		Paths pt_rb = new Paths(0, 0);
		Paths pts[] = { pt_l, pt_r, pt_t, pt_b, pt_lt, pt_lb, pt_rt, pt_rb};
	
		Paths t_pt, t_po, t_pc;
		while (this.open.linkcount > 0)
		{
			Paths pt = this.open.link;
			this.open.removeLink(pt);
			this.close.insertLink(pt);
	
			if (this.open.linkcount > limit || this.close.linkcount > limit)
			{
				this.close.clearLink();
				this.open.clearLink();
				break;
			}
			if (dest.atPointXY(new RectF(pt.x, pt.y, 1, 1)) > 0)
			{
				this.paths.insertLink(dest);
				Paths temp = pt.parent;
				while (null != temp)
				{
					this.paths.insertLink(temp);
	
					temp = temp.parent;
				}
	
				this.paths.removeLink(start);
				this.paths.insertLink(start);
	
				this.pathCount = this.paths.linkcount;
	
				this.close.clearLink();
				this.open.clearLink();
				break;
			}
	
			pt_l.set(pt.x - 1, pt.y);
			pt_r.set(pt.x + 1, pt.y);
			pt_t.set(pt.x, pt.y - 1);
			pt_b.set(pt.x, pt.y + 1);
			pt_lt.set(pt.x - 1, pt.y - 1);
			pt_lb.set(pt.x - 1, pt.y + 1);
			pt_rt.set(pt.x + 1, pt.y - 1);
			pt_rb.set(pt.x + 1, pt.y + 1);
	
			for (int i = 0; i < 8; i++)
			{
				t_pt = pts[i];
				t_pt.getF(this, dest);
				t_pt.parent = pt;
	
				t_pc = this.close.isin(t_pt);
				if (null == t_pc)
				{
					t_po = this.open.isin(t_pt);
					if (null == t_po)
					{
						this.open.insertSort(new Paths().clone(t_pt));
					}
					else if (t_pt.f < t_po.f)
					{
						//t_po.give(t_pt);
						t_po.f = t_pt.f;
						t_po.parent = t_pt.parent;
	
						this.open.removeLink(t_po);
						this.open.insertSort(t_po);
					}
				}
				else
				{
					if (t_pt.f < t_pc.f)
					{
						this.close.removeLink(t_pc);
						t_pc.give(t_pt);
						this.open.insertSort(t_pc);
					}
				}
			}
		}
	}
	
	

	public void removePath ()
	{
		if (null != this.nextPath)
		{
			//delete this.nextPath;
			this.nextPath = null;
		}
		if (null != this.prevPath)
		{
			//delete this.prevPath;
			this.prevPath = null;
		}

		this.paths.clearLink();
		this.pathCount = 0;
	}
	
	public void fetchPath ()
	{
		if (this.nextPath == null)
		{
			this.nextPath = this.paths.removeLink(this.paths.prev(this.paths.link));
			if (this.prevPath == null)
			{
				this.prevPath = this.nextPath;
				this.nextPath = this.paths.removeLink(this.paths.prev(this.paths.link));
			}
		}
		if (this.nextPath == null)
		{
			if (null != this.prevPath)
			{
				//delete this.prevPath;
				this.prevPath = null;
			}
			this.moving = 0;
			return;
		}
		if (this.fetchCount++ >= this.fetchSpeed)
		{
			// When first in path, focus role
			if (this.following > 0 && this.paths.linkcount == this.pathCount - 2)
			{
				this.center();
			}

			this.fetchCount = 0;
			if (null != this.prevPath)
			{
				//delete this.prevPath;
				this.prevPath = null;
			}
			this.prevPath = this.nextPath;
			this.nextPath = null;
		}
		else
		{
			float dx = (this.nextPath.x - this.prevPath.x) / this.fetchSpeed;
			float dy = (this.nextPath.y - this.prevPath.y) / this.fetchSpeed;
			
			this.moveDelta(dx, dy);
		}
	}
	public void moveDelta(float dx, float dy)
	{
			int oldbase = this.base;
			this.base = Animation_Base.Move;
//			if (oldbase != this.base)
//			{
//				Client.emitClientEvent("status", {
//					"worldID":this.world.uniqueID,
//					"role":{
//						"uniqueID": this.uniqueID,
//						"base":this.base
//					}
//				});
//			}

			
			float olddirection = this.direction;
			this.direction = this.getDirection(dx, dy);
//			if (olddirection != this.direction)
//			{
//				Client.emitClientEvent("direction", {
//					"worldID":this.world.uniqueID,
//					"role":{
//						"uniqueID": this.uniqueID,
//						"direction":this.direction
//					}
//				});
//			}
			int animationID = this.base + this.direction;
			if (animationID >= 0)
			{
				this.selectAnimation(animationID);
			}
			
			this.moveFlat(dx * this.flatting.Width, dy * this.flatting.Height);
			this.world.refreshDepth(this);
			
			//this.world.quadTree.move(this);
			
//			Client.emitClientEvent("position", {
//				"worldID":this.world.uniqueID,
//				"role":{
//					"uniqueID": this.uniqueID,
//					"position": {
//						"X": (this.flatting.X - this.world.geometry.X) ,
//						"Y": (this.flatting.Y - this.world.geometry.Y)
//					}
//				}
//			});

			if (this.following > 0)
			{
				this.world.offsetFlat(- dx * this.flatting.Width, - dy * this.flatting.Height);
			}
			
			this.moving = 1;
	}
	
	public int getDirection(float dx, float dy)
	{
		int animationID = 0;

		if (dx == 0)
		{
			dx = Constants.DEF_ZERO;
		}
		int h = (int) Math.atan(-dy/dx);

		if (-dy > 0)
		{
			if (dx < 0)
			{
				h += Constants.PI;
			}
		}
		else
		{
			if (dx >= 0)
			{
				h += 2 * Constants.PI;
			}
			else if (dx < 0)
			{
				h += Constants.PI;
			}
		}

		if (h >= Constants.PI / 8)
		{
			if (h < 3 * Constants.PI / 8)
			{
				animationID = Animation_Direction.Right;
				this.speed.X = 1;
				this.speed.Y = -1;
			}
			else if (h < 5 * Constants.PI / 8)
			{
				animationID = Animation_Direction.Right_Up;
				this.speed.X = 0;
				this.speed.Y = -1;
			}
			else if (h < 7 * Constants.PI / 8)
			{
				animationID = Animation_Direction.Up;
				this.speed.X = -1;
				this.speed.Y = -1;
			}
			else if (h < 9 * Constants.PI / 8)
			{
				animationID = Animation_Direction.Left_Up;
				this.speed.X = -1;
				this.speed.Y = 0;
			}
			else if (h < 11 * Constants.PI / 8)
			{
				animationID = Animation_Direction.Left;
				this.speed.X = -1;
				this.speed.Y = 1;
			}
			else if (h < 13 * Constants.PI / 8)
			{
				animationID = Animation_Direction.Left_Down;
				this.speed.X = 0;
				this.speed.Y = 1;
			}
			else if (h < 15 * Constants.PI / 8)
			{
				animationID = Animation_Direction.Down;
				this.speed.X = 1;
				this.speed.Y = 1;
			}
			else
			{
				animationID = Animation_Direction.Right_Down;
				this.speed.X = 1;
				this.speed.Y = 0;
			}
		}
		else
		{
			animationID = Animation_Direction.Right_Down;
				this.speed.X = 0;
				this.speed.Y = 1;
		}
		
		return animationID;
	}

	
	public void center()
	{
		if (null == this.world)
		{
			return;
		}
		this.world.offset(-(this.position.X - this.world.geometry.Width / 2 - this.world.display.X), -(this.position.Y - this.world.geometry.Height / 2 - this.world.display.Y));
	}
}