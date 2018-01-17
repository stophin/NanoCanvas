package com.example.world;


@SuppressWarnings("unchecked")
public class MultiLinkList<T> {
	public static abstract class Element<T> {
		public int uniqueID = 0;
		public T prev[] = null;
		public T next[] = null;
		public int f = 0;
		public abstract boolean isin(T link);
	}
	public static interface Compare<T> {
		public int run(T temp, T link); 
	}
	
	public T link = null;
	public int linkcount = 0;
	public int linkindex = 0;
	
	public MultiLinkList(int linkindex) {
		this.linkindex = linkindex;
	}
	
	public void insertLink(T link, T before) {
		this.insertLink(link, before, null);
	}
	public void insertLink(T link) {
		this.insertLink(link, null, null);
	}
	public void insertLink(T link, T before, T after) {
		if (null == link) {
			return;
		}
		if (this.link == null)
		{
			this.link = link;

			((Element<T>)this.link).prev[this.linkindex] = link;
			((Element<T>)this.link).next[this.linkindex] = link;

			this.linkcount = this.linkcount + 1;

			return;
		}
		else
		{
			T _link = null;
			if (before == this.link)
			{
				_link = link;
			}
			if (before == null && after == null)
			{
				before = this.link;
				after = ((Element<T>)this.link).prev[this.linkindex];
			}
			else if (before == null)
			{
				before = ((Element<T>)after).next[this.linkindex];
			}
			else if (after == null)
			{
				after = ((Element<T>)before).prev[this.linkindex];
			}
			else // before != null && after != null
			{
				if (((Element<T>)before).prev[this.linkindex] != after || ((Element<T>)after).next[this.linkindex] != before)
				{
					return;
				}
			}
			if (before == null || after == null ||
				((Element<T>)before).prev[this.linkindex] == null ||
				((Element<T>)after).next[this.linkindex] == null)
			{
				return;
			}

			((Element<T>)link).prev[this.linkindex] = after;
			((Element<T>)link).next[this.linkindex] = before;
			((Element<T>)after).next[this.linkindex] = link;
			((Element<T>)before).prev[this.linkindex] = link;

			if (null != _link)
			{
				this.link = _link;
			}

			this.linkcount = this.linkcount + 1;
		}
	}
	
	public T removeLink(T link) {
		if (link == null)
		{
			return null;
		}
		if (this.linkindex < 0)
		{
			return null;
		}
		if (((Element<T>)link).prev[this.linkindex] == null || ((Element<T>)link).next[this.linkindex] == null)
		{
			return null;
		}
		T before = ((Element<T>)link).prev[this.linkindex];
		T after = ((Element<T>)link).next[this.linkindex];

		((Element<T>)before).next[this.linkindex] = after;
		((Element<T>)after).prev[this.linkindex] = before;
		((Element<T>)link).prev[this.linkindex] = null;
		((Element<T>)link).next[this.linkindex] = null;

		if (this.link == link)
		{
			this.link = after;
		}
		if (this.link == link)
		{
			this.link = null;
		}

		this.linkcount = this.linkcount - 1;

		return link;
	}
	
	public void clearLink() {
		if (null != this.link) {
			T temp = this.link;
			do
			{
				if (this.removeLink(temp) == null)
				{
					break;
				}

				temp = this.link;
			} while (null != temp);
		}
	}

	public void insertSortF(T link, Compare<T> fun) {
		if (link == null)
		{
			return;
		}

		if (null != this.link)
		{
			T temp = this.link;
			do
			{
				if (null != fun && fun.run(temp, link) > 0)
				{
					this.insertLink(link, temp);
					return;
				}

				temp = ((Element<T>)temp).next[this.linkindex];
			} while (null != temp && temp != this.link);
			this.insertLink(link);
		}
		else
		{
			this.insertLink(link);
		}
	}

	public void insertSort(T link) {
		if (link == null)
		{
			return;
		}

		if (null != this.link)
		{
			T temp = this.link;
			do
			{
				if (((Element<T>)temp).f > ((Element<T>)link).f)
				{
					this.insertLink(link, temp);
					return;
				}

				temp = ((Element<T>)temp).next[this.linkindex];
			} while (null != temp && temp != this.link);
			this.insertLink(link);
		}
		else
		{
			this.insertLink(link);
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
				return temp;
			}
			temp = ((Element<T>)temp).next[this.linkindex];
		} while (null != temp && temp != this.link);
		return null;
	}
	
	public T isin(T link) {
		if (this.link == null)
		{
			return null;
		}
		T temp = this.link;
		do
		{
			if (((Element<T>)temp).isin(link))
			{
				return temp;
			}
			temp = ((Element<T>)temp).next[this.linkindex];
		} while (null != temp && temp != this.link);
		return null;
	}
	
	public T topnLink(int i) {
		if (null == this.link)
		{
			return null;
		}
		if (i < 0 || i > this.linkcount)
		{
			return null;
		}
		T link = ((Element<T>)this.link).prev[this.linkindex];
		int j;
		for (j = i; j > 0; j--)
		{
			link = ((Element<T>)link).prev[this.linkindex];
		}
		return link;
	}
	
	public T next(T link) {
		if (link == null)
		{
			return null;
		}
		return ((Element<T>)link).next[this.linkindex];
	}
	public T prev(T link) {
		if (link == null)
		{
			return null;
		}
		return ((Element<T>)link).prev[this.linkindex];
	}
}