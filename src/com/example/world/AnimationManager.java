package com.example.world;

import com.example.nano.CanvasL;

public class AnimationManager {
	public AnimationManager() {
		
	}
	public LinkList<Animations> animation = new LinkList<Animations>();
	
	public Animations addAnimation(Animations animation, int uniqueID) {
		animation.uniqueID = uniqueID;
		this.animation.insertLink(animation);
		
		return animation;
	}
	
	public void moveAnimation(float directionX, float directionY) {
		if (this.animation.link == null)
		{
			return;
		}
		Animations temp = this.animation.link;
		do
		{
			temp.moveSequence(directionX, directionY);

			temp = temp.next;
		} while (null != temp && temp != this.animation.link);
	}
	
	public void removeAnimation(Animations animation) {
		this.animation.removeLink(animation);
	}
	
	public void drawAnimation(CanvasL canvas) {
		if (this.animation.link == null) {
			return;
		}
		this.animation.link.drawSequence(canvas);
	}
	
	public void nextAnimation(Object lParam, int moving) {
		if (this.animation.link == null) {
			return;
		}
		this.animation.link.nextSequence(lParam, moving);
		//var temp = this.animation.link;
		//do
		//{
		//	temp.nextSequence(lParam);
			
			// Only the current animation can do trigger
			// When sequence is done
			// Simply set lParam to null
			// will prevent other animations do trigger
		//	lParam = null;
		//	temp = temp.next;
		//} while (temp && temp != this.animation.link);
	}
}