package com.example.world;

@SuppressWarnings("unchecked")
public class LinkList<T> {
	public static class  Element<T> {
		public int uniqueID = 0;
		public T prev = null;
		public T next = null;
	}
	public T link = null;
	public int linkcount = 0;

	public void insertLink(T link, T before) {
		this.insertLink(link, before, null);
	}
	public void insertLink(T link) {
		this.insertLink(link, null, null);
	}
	public void insertLink(T link, T before, T after) {
		if (null == before) {
			before = null;
		}
		if (null == after) {
			after = null;
		}
		if (null == this.link){
			this.link = link;
			((Element<T>)this.link).prev = link;
			((Element<T>)this.link).next = link;
			this.linkcount = this.linkcount + 1;
			return;
		} 
		else
		{
			if (before == null && after == null)
			{
				before = this.link;
				after = ((Element<T>)this.link).prev;
			}
			else if (before == null)
			{
				before = ((Element<T>)after).next;
			}
			else if (after == null)
			{
				after = ((Element<T>)before).prev;
			}
			else // before != null && after != null
			{
				if (((Element<T>)before).prev != after || ((Element<T>)after).next != before)
				{
					return;
				}
			}
			
			((Element<T>)link).prev = after;
			((Element<T>)link).next = before;
			((Element<T>)after).next = link;
			((Element<T>)before).prev = link;
			
			this.linkcount = this.linkcount + 1;
		}
	}
	
	public T getLink(int uniqueID) {
		if (this.link == null)
		{
			return null;
		}
		T temp = this.link;
		do
		{
			if (((Element<T>)temp).uniqueID == uniqueID)
			{
				break;
			}
			temp = ((Element<T>)temp).next;
		} while (null != temp && temp != this.link);
		return temp;
	}
	
	public void removeLink(T link) {
		if (((Element<T>)link).prev == null || ((Element<T>)link).next == null)
		{
			return;
		}
		T before = ((Element<T>)link).prev;
		T after = ((Element<T>)link).next;

		((Element<T>)before).next = after;
		((Element<T>)after).prev = before;
		((Element<T>)link).prev = null;
		((Element<T>)link).next = null;

		if (this.link == link)
		{
			this.link = before;
		}
		if (this.link == link)
		{
			this.link = null;
		}

		this.linkcount = this.linkcount - 1;
		
	}
}