package com.example.world;

import com.example.nano.CanvasL;

public class ObjectManager extends LinkList.Element<ObjectManager> {
	public ObjectManager() {
		
	}
	
	public LinkList<Objects> object = new LinkList<Objects>();
	
	public ObjectManager addStep(Objects object, int uniqueID) {
		object.uniqueID = uniqueID;
		this.object.insertLink(object);
		
		return this;
	}
	
	public ObjectManager addStepExternal(Objects object, Resources resource, RectF destination, RectF truncation, int uniqueID, float scale) {

//		if (!scale)
//		{
//			scale = 1;
//		}
//		if (!destination)
//		{
//			destination = new RectF();
//		}
//		if (!truncation)
//		{
//			truncation = new RectF();
//		}
		
		object.resource = resource;
		object.destination = destination;
		object.truncation = truncation;
		object.destination.scale(scale);
		
		this.addStep(object, uniqueID);
		
		return this;
	}
	
	public void removeStemp(Objects object) {
		this.object.removeLink(object);
	}
	
	public void drawStep(CanvasL canvas) {
		if (this.object.link == null) {
			return;
		}
		this.object.link.drawObject(canvas);
	}
	
	public void moveStep(float directionX, float directionY) {

		if (this.object.link == null)
		{
			return;
		}
		Objects temp = this.object.link;
		do
		{
			temp.moveObject(directionX, directionY);

			temp = temp.next;
		} while (null != temp && temp != this.object.link);
	}
	
	public int objectCount = 0;
	public static interface Trigger {
		public int run(Object lParam, Object wParam); 
	}
	public Trigger callbackFunction = null;
	public void triggerFunction(Object lParam) {
		this.objectCount ++;
		this.objectCount++;
		if (null != this.callbackFunction && null != lParam && this.objectCount >= this.object.linkcount)
		{
			this.objectCount = 0;
			this.callbackFunction.run(lParam, this);
		}
		if (this.objectCount < 0)
		{
			this.objectCount = 0;
		}
	}
}