package com.example.world;

import com.example.nano.CanvasL;
import com.example.world.Constants.*;

public class World {
	public int uniqueID;
	
	public World(int uniqueID) {
		this.uniqueID = uniqueID;
	}
	
	public PointF display = new PointF();
	
	public PointF leftTop =  new PointF();
	public PointF vLeftBottom = new PointF();
	public PointF vRightBottom = new PointF();
	public PointF vRightTop = new PointF();
	public RectF geometry = new RectF();
	
	public Roles focus = null;
	public Roles cursor = null;
	
	//public HashMap<Integer, Roles> roles = new HashMap<Integer, Roles>();
	public MultiLinkList<Roles> roles = new MultiLinkList<Roles>(Role_Link.Role);// Roles, Except the Land
	public MultiLinkList<Roles> sorts = new MultiLinkList<Roles>(Role_Link.Sort);			// Sort Link
	public MultiLinkList<Roles> objects = new MultiLinkList<Roles>(Role_Link.Object);		// All the objects
	public MultiLinkList<Roles> backs = new MultiLinkList<Roles>(Role_Link.Back);			// Land
	public MultiLinkList<Roles> sortsbacks = new MultiLinkList<Roles>(Role_Link.Sortsbacks);// Land's sorts
	public MultiLinkList<Roles> fronts = new MultiLinkList<Roles>(Role_Link.Front);		// Front objects, cursor, etc.
	public MultiLinkList<Roles> players = new MultiLinkList<Roles>(Role_Link.Player);		// Players, dynamic
	
	public class Compare implements MultiLinkList.Compare<Roles> {
		@Override
		public int run(Roles temp, Roles link) {
			// TODO Auto-generated method stub
			int ret = 0;
			if (Constants.DEF_ISZERO(link.flatting.Y - temp.flatting.Y)) {
				if (link.flatting.X > temp.flatting.X) {
					ret = 1;
				}
			}
			return ret;
		}
		
	}
	public Compare compare = new Compare();
	
	//TODO
	public Object quadTree = null;
	public Object binTree = null;
	

	public void resize (float width, float height)
	{
		this.geometry.Width = width;
		this.geometry.Height = height;

		this.leftTop = Projection.projectShow(this.geometry.X, this.geometry.Y);
		this.vLeftBottom = Projection.projectShow(this.geometry.X, this.geometry.Y + this.geometry.Height).minus(this.leftTop);
		this.vRightTop = Projection.projectShow(this.geometry.X + this.geometry.Width, this.geometry.Y).minus(this.leftTop);
		this.vRightBottom = Projection.projectShow(this.geometry.X + this.geometry.Width, this.geometry.Y + this.geometry.Height).minus(this.leftTop);
	}
	public void offset (float offsetX, float offsetY)
	{
		this.leftTop.X += offsetX;
		this.leftTop.Y += offsetY;
		
		PointF flat = Projection.projectFlat(this.leftTop.X, this.leftTop.Y);

		float flatX = flat.X - this.geometry.X;
		float flatY = flat.Y - this.geometry.Y;
		this.geometry.X = flat.X;
		this.geometry.Y = flat.Y;

		Roles temp;

		if (null != this.roles.link)
		{
			temp = this.roles.link;
			do
			{
				temp.moveRole(offsetX, offsetY);

				if (this.geometryCut(temp) > 0)
				{
					//this.sorts.insertLink(temp);
					this.refreshDepth(temp);
				}
				else
				{
					this.sorts.removeLink(temp);
				}

				temp = this.roles.next(temp);
			} while (null != temp && temp != this.roles.link);
		}

		this.refreshGeometryCut(flatX, flatY);
	}
	public void offsetFlat (float offsetX, float offsetY)
	{
		this.geometry.X += offsetX;
		this.geometry.Y += offsetY;

		this.leftTop = Projection.projectShow(this.geometry.X, this.geometry.Y);

		Roles temp;
		if (null != this.roles.link)
		{
			temp = this.roles.link;
			do
			{
				temp.moveFlat(offsetX, offsetY);

				if (this.geometryCut(temp) > 0)
				{
					//this.sorts.insertLink(temp);
					this.refreshDepth(temp);
				}
				else
				{
					this.sorts.removeLink(temp);
				}

				temp = this.roles.next(temp);
			} while (null != temp && temp != this.roles.link);
		}

		this.refreshGeometryCut(offsetX, offsetY);
	}
	public void addRole(Roles role) {
		addRole(role, 0);
	}
	public void addRole (Roles role, int type)
	{
		if (0 == type)
		{
			type = role.type;
		}
		role.type = type;

		if (this.roles.link == null)
		{
			this.focus = role;
		}
		if (role.type != Role_Type.Back) {
			this.roles.insertLink(role);
		}
		role.world = this;

		switch (type)
		{
		case Role_Type.Normal:
			this.sorts.insertLink(role);
			this.refreshDepth(role, null);
			//this.refreshGeometryCut();
			this.objects.insertLink(role);
		
			//this.quadTree.insert(role);
			//this.binTree.insertNode(role);
			break;
		case Role_Type.Player:
			this.players.insertLink(role);
			this.objects.insertLink(role);
		
			//this.quadTree.insert(role);
			//this.binTree.insertNode(role);
			break;
		case Role_Type.Cursor:
			this.fronts.insertLink(role);
			this.cursor = role;
			break;
		default:
			this.backs.insertSortF(role, compare);
			if (this.geometryCut(role) > 0)
			{
				this.insertSortsBacks(role, compare);
			}
		}
		//bind world to each role's animation's object
		Animations animation = role.animations.animation.link;
		if (null != animation) {
			do {
				ObjectManager objectManager = animation.objects.link;
				if (null != objectManager) {
					do {
						Objects object = objectManager.object.link;
						if (null != object) {
							do {
								object.world = this;

								object = object.next;
							} while (null != object && object != objectManager.object.link);
						}

						objectManager = objectManager.next;
					} while (null != objectManager && objectManager != animation.objects.link);
				}

				animation = animation.next;
			} while (null != animation && animation != role.animations.animation.link);
		}
	}
	
	public Roles removeRole (Roles role)
	{
		if (role == null)
		{
			return null;
		}
		if (this.roles.removeLink(role) == null)
		{
			return null;
		}
		switch (role.type)
		{
		case Role_Type.Normal:
			this.objects.removeLink(role);
			break;
		case Role_Type.Cursor:
			this.fronts.removeLink(role);
			this.cursor = null;
			break;
		case Role_Type.Player:
			this.players.removeLink(role);
			break;
		default:
			this.backs.removeLink(role);
			this.removeSortsBacks(role);
		}
		this.sorts.removeLink(role);
		role.world = null;

		if (role.equal(this.focus))
		{
			this.focus = null;
		}
		
		role.type = Role_Type.Disabled;
		
		return role;
	}
	public void drawRole (CanvasL canvas)
	{
		int i = 0;
		Roles temp = null;
		if (null != this.sortsbacks.link)
		{
			temp = this.sortsbacks.link;
			do
			{
				temp.drawAnimation(canvas);

				i++;
				temp = this.sortsbacks.next(temp);
			} while (null != temp && temp != this.sortsbacks.link);
		}

		if (null != this.sorts.link)
		{
			temp = this.sorts.link;
			do
			{
				temp.drawAnimation(canvas);
				
				i++;
				if (temp.moving > 0) {
					temp.nextAnimation();
				}
				temp = this.sorts.next(temp);
			} while (null != temp && temp != this.sorts.link);
		}
		if (null != this.fronts.link)
		{
			temp = this.fronts.link;
			do
			{
				temp.drawAnimation(canvas);
				i++;
				temp = this.fronts.next(temp);
			} while (null != temp && temp != this.fronts.link);
		}
		if (null != this.players.link)
		{
			temp = this.players.link;
			do
			{
				//temp.drawAnimation(canvas);
				temp.nextAnimation();
				i++;
				temp = this.players.next(temp);
			}while (null != temp && temp != this.players.link);
		}
		
		//this.quadTree.draw(canvas, this.quadTree.drawable);
	}
	public void refreshRole ()
	{
		if (this.sorts.link == null)
		{
			return;
		}
		Roles temp = this.sorts.link;
		do
		{
			temp.nextAnimation();

			temp = this.sorts.next(temp);
		} while (null != temp && temp != this.sorts.link);

	}
	public void insertSortsBacks(Roles role, Compare fun) {
		/*
		for ( i = 0; i < 4; i++) {
			if (role.dir[i]) {
				var j = 0;
				switch (i) {
				case 0: j = 1; break;
				case 1: j = 0; break;
				case 2: j = 3; break;
				case 3: j = 2; break;
				}
				role._dir[i] = role.dir[i];
				role.dir[i]._dir[j] = role;
			}
		}
		*/

		this.sortsbacks.insertSortF(role, fun);

	}
	public void removeSortsBacks(Roles role) {
		/*
		for ( i = 0; i < 4; i++) {
			if (role._dir[i]) {
				var j = 0;
				switch (i) {
				case 0: j = 1; break;
				case 1: j = 0; break;
				case 2: j = 3; break;
				case 3: j = 2; break;
				}
				role._dir[i]._dir[j] = null;
			}
			role._dir[i] = null;
		}
		*/

		this.sortsbacks.removeLink(role);
	}

	
	public void refreshGeometryCut (float flatX, float flatY)
	{
		Roles temp = null, _temph = null, _tempw = null;
		temp = this.backs.link;
		if (null != temp) {
			do {
				temp.moveFlat(flatX, flatY, 1);

				temp = this.backs.next(temp);
			} while (null != temp && temp != this.backs.link);
		}
		if (null != this.sortsbacks.link) {
			MultiLinkList<Roles> tempLink = new MultiLinkList<Roles>(Role_Link.Temp1);
			temp = this.sortsbacks.link;
			float layw = (float) (Math.floor(Math.abs(flatX) / temp.flatting.Width) + 2);
			float layh = (float) (Math.floor(Math.abs(flatY) / temp.flatting.Height) + 2);
			float i, j, k;
			do
			{
				Roles next = this.sortsbacks.next(temp);
				//if (temp->_up == NULL ||temp->_down == NULL ||temp->_left == NULL ||temp->_left == NULL)
				{
					_temph = temp;
					for (i = 0; i <= layh; i++) {
						if (null != _temph && null != _temph.down) {
							_temph = _temph.down;
						}
						else {
							break;
						}
					}
					for (i = -layh; i <= layh; i++) {
						if (null != _temph) {
							_tempw = _temph;
							for (j = 0; j <= layw; j++) {
								if (null != _tempw && null != _tempw.left) {
									_tempw = _tempw.left;
								}
								else {
									break;
								}
							}
							for (k = -layw; k <= layw; k++) {
								if (null != _tempw) {
									if (null != tempLink.next(_tempw)) {
										//tempLink.insertSortF(_tempw, compare);
										tempLink.insertLink(_tempw);
									}
									_tempw.moveFlatEx();
									_tempw = _tempw.right;
								}
								else {
									break;
								}
							}
							_temph = _temph.up;
						}
						else {
							break;
						}
					}
				}
				temp.moveFlatEx();

				temp = next;
			} while (null != temp && temp != this.sortsbacks.link);
			temp = tempLink.link;
			if (null != temp) {
				do {
					if (this.geometryCut(temp) > 0)
					{
						this.removeSortsBacks(temp);
					}
					else
					{
						//if (!this.sortsbacks.isin(temp))
						if (null != this.sortsbacks.next(temp))
						{
							this.insertSortsBacks(temp, compare);
						}
						else {
							//temp = temp;
						}
					}
					temp = tempLink.next(temp);
				} while (null != temp && temp != tempLink.link);
			}
			tempLink.clearLink();
		}
		else {
			temp = null;

			if (null != this.backs.link)
			{
				temp = this.backs.link;
				do
				{
					temp.moveFlat(flatX, flatY);

					if (this.geometryCut(temp) > 0)
					{
						this.removeSortsBacks(temp);
					}
					else
					{
						if (null != this.sortsbacks.isin(temp))
						{
							this.insertSortsBacks(temp, compare);
						}
					}

					temp = this.backs.next(temp);
				} while (null != temp && temp != this.backs.link);
			}
		}
	}
	public MultiLinkList<Roles> getCollision (Roles item, MultiLinkList<Roles> collision)
	{
		if (collision == null)
		{
			return collision;
		}
		if (item == null)
		{
			return collision;
		}
		if (item.type != Role_Type.Normal && item.type != Role_Type.Player)
		{
			return collision;
		}
		
//		if (null != this.quadTree)
//		{
//			this.quadTree.move(item);
//			this.quadTree.collision(item, collision, graphics);
//			return collision;
//		}
		
		MultiLinkList<Roles>  linkList = this.objects;
		if (linkList.link == null)
		{
			return collision;
		}
		Roles tango = linkList.link;
		int relation;
		do
		{
			if (tango != item)
			{
				relation = Relations.getRelation(tango, item);
				if (Relations.isRelationD(relation))
				{
					tango.relation = relation;
					collision.insertLink(tango);
				}
			}
			tango = linkList.next(tango);
		} while (null != tango && tango != linkList.link);
		return collision;
	}
	public MultiLinkList<Roles> refreshDepth (Roles item)
	{
		return refreshDepth(item, null);
	}
	
	public MultiLinkList<Roles> refreshDepth (Roles item, MultiLinkList<Roles> collision)
	{
		if (null == collision)
		{
			collision = null;
		}
		if (item == null)
		{
			return collision;
		}
		if (this.sorts.link == null)
		{
			this.sorts.insertLink(item);
			return collision;
		}
		this.sorts.removeLink(item);
		if (this.sorts.link == null)
		{
			this.sorts.insertLink(item);
			return collision;
		}
		if (item.type != Role_Type.Normal && item.type != Role_Type.Player)
		{
			return collision;
		}
		
		Roles before = null;
		Roles after = null;
		MultiLinkList<Roles> linkList = this.sorts;
//		if (false && null != item.quadTree)
//		{
//			linkList = item.quadTree.objects;
//		}
		Roles tango = linkList.link;
		if (tango == null)
		{
			return collision;
		}
		int relation;
		do
		{
			relation = Relations.getRelation(tango, item);
			tango.relation = relation;
			if (Relations.isRelationD(relation))
			{
				if (null != collision)
				{
					collision.insertLink(tango);
				}
				if (tango.position.Y + tango.tall > item.position.Y + item.tall)
				{
					//this.sorts.insertLink(item, tango);
					before = tango;
					break;
				}
				else
				{
					//this.sorts.insertLink(item, null, tango);
					//after = tango;
				}
			}
			else if (relation == Relation.A)
			{
				//this.sorts.insertLink(item, tango);
				before = tango;
				break;
			}
			else if (relation == Relation.B || relation == Relation.C)
			{
				//this.sorts.insertLink(item, null, tango);
				//after = tango;
			}

			tango = linkList.next(tango);
		} while (null != tango && tango != linkList.link);
		
		this.sorts.insertLink(item, before, after);

		return collision;
	}
	
	public int geometryCut(Roles item)
	{
		float px = item.position.X;
		float itemW = item.position.Width;

		float bondX = 10;

		if (px + itemW < bondX)
		{
			return 1;
		}
		if (px > this.geometry.Width - bondX)
		{
			return 1;
		}
		float py = item.position.Y;
		float itemH = item.position.Height;
		float bondY = 10;
		if (py + itemH < bondY)
		{
			return 1;
		}
		if (py > this.geometry.Height - bondY)
		{
			return 1;
		}
		return 0;
	}
	
	public Roles getRole(float x, float y)
	{
		if (this.sorts.link == null)
		{
			return null;
		}
		Roles temp = this.sorts.link;
		do
		{
			RectF object = null;
			try
			{
				object = temp.animations.animation.link.objects.link.object.link.destination;
			}
			catch(Exception e)
			{
				object = temp.position;
			}
			
			if (object.atPointXY(x, y) > 0)
			{
				return temp;
			}
			temp = this.sorts.next(temp);
		}while(null != temp && temp != this.sorts.link);
		return null;
	}
	public Roles getRoleFlat(float x, float y)
	{
		if (this.sorts.link == null)
		{
			return null;
		}
		PointF flat = Projection.projectFlat(x, y);
		Roles temp = this.sorts.link;
        RectF object = null;
		do
		{
			object = temp.flatting;
			if (object.atPointXY(flat.X, flat.Y) > 0)
			{
				return temp;
			}
			temp = this.sorts.next(temp);
		}while(null != temp && temp != this.sorts.link);
		return null;
	}
}