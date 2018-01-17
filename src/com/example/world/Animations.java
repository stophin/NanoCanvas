package com.example.world;

import com.example.nano.CanvasL;
import com.example.world.Constants.Animation_Mode;

public class Animations  extends LinkList.Element<Animations>{
	public Animations() {
		
	}
	
	public int delay = 0;
	public int delayCount = 0;
	
	public int mode = Animation_Mode.Still;
	
	//public HashMap<Integer, Objects> objects = new HashMap<Integer, Objects>();
	public LinkList<ObjectManager> objects = new LinkList<ObjectManager>();
	
	public ObjectManager addSequence(ObjectManager objects, int uniqueID) {
		objects.uniqueID = uniqueID;
		this.objects.insertLink(objects);
		return objects;
	}
	
	public void removeSequence(ObjectManager objects) {
		this.objects.removeLink(objects);
	}
	
	public void drawSequence(CanvasL canvas) {
		if (this.objects.link == null)
		{
			return;
		}
		ObjectManager temp = this.objects.link;
		do
		{
			temp.drawStep(canvas);
			temp = temp.next;
		} while (null != temp && temp != this.objects.link);
	}
	
	public void nextSequence(Object lParam, int moving) {
		if (this.objects.link == null)
		{
			return;
		}
		if (this.mode == Animation_Mode.Still)
		{
			return;
		}
		if (this.delayCount++ >= this.delay)
		{
			this.delayCount = 0;
			ObjectManager temp = this.objects.link;
			do
			{
				temp.object.link = temp.object.link.next;
				
				// when lParam is not null and mode is Step
				// do trigger when sequence is done
				if (null != lParam)
				{
					// Do not do trigger when moving if mode is Step
					// even if sequence is done
					if (null != lParam && moving > 0)
					{
						temp.objectCount = -1;
					}
					if (this.mode == Animation_Mode.Step)
					{
						// Force trigger every step
						// Thus change animation whenever stopped
						if (temp.objectCount > 0)
						{
							temp.objectCount = temp.object.linkcount;
						}
						temp.triggerFunction(lParam);
					}
					else if (this.mode == Animation_Mode.Auto)
					{
						temp.triggerFunction(lParam);
					}
				}
				
				temp = temp.next;
			} while (null != temp && temp != this.objects.link);
		}
	}
	
	
	public void moveSequence(float directionX, float directionY) {

		if (this.objects.link == null)
		{
			return;
		}
		ObjectManager temp = this.objects.link;
		do
		{
			temp.moveStep(directionX, directionY);

			temp = temp.next;
		} while (null != temp && temp != this.objects.link);
	}
	
}