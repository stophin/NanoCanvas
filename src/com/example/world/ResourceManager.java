package com.example.world;

import com.example.nano.CanvasL;
import android.annotation.SuppressLint;


public class ResourceManager {
	public CanvasL canvas = null;
	@SuppressLint("UseSparseArrays") 
	//public HashMap<Integer, Resources> resource = new HashMap<Integer, Resources>();
	public LinkList<Resources> resource = new LinkList<Resources>();
	
	public ResourceManager (CanvasL canvas) {
		this.canvas = canvas;
	}
	
	
	public void addResource(Resources resource) {
		this.resource.insertLink(resource);
	}
	public Resources createResource(String filename, int resourceID) {
		return createResource(filename, resourceID, null, 0);
	}
	public Resources createResource(String filename, int resourceID, Resources mask) {
		return createResource(filename, resourceID, mask, 0);
	}
	public Resources createResource(String filename, int resourceID, Resources mask, int cutmode) {
		return new Resources(canvas, filename, resourceID);
	}
	
	public Resources getResource(int resourceID) {
		return this.resource.getLink(resourceID);
	}
	
	public Resources removeResource(int resourceID) {
		Resources resource = this.resource.getLink(resourceID);
		if (resource == null) {
			return null;
		}
		this.resource.removeLink(resource);
		return resource;
	}
}