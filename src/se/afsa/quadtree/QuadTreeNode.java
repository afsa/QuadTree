package se.afsa.quadtree;

import java.util.List;

public class QuadTreeNode {
	
	private final int depthLevel;
	private List<Object> entities;
	private final int[]
			x,
			y;
	
	public QuadTreeNode(int depthLevel, int[] x, int[] y) {
		this.depthLevel = depthLevel;
		this.x = x;
		this.y = y;
	}
	
	protected void split() {
		
	}
	
	protected void merge() {
		
	}
	
	protected void hasEntities() {
		
	}
}
