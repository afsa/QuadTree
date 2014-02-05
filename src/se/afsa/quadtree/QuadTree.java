package se.afsa.quadtree;

public class QuadTree {
	private int
			maxDepth,
			maxObjectsPerRectangle;
	
	private final int[]
			x,
			y;
			
	public QuadTree(int maxDepth, int maxObjectsPerRectangle, int[] x, int[] y) {
		this.maxDepth = maxDepth;
		this.maxObjectsPerRectangle = maxObjectsPerRectangle;
		this.x = x;
		this.y = y;
	}
	
	public int getMaxDepth() {
		return maxDepth;
	}
	
	public int getMaxObjectsPerRectangle() {
		return maxObjectsPerRectangle;
	}
}
