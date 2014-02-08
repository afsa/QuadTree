package se.afsa.quadtree;

import java.util.List;

public class QuadTree<E extends QuadTreeElement> {
	private int
			maxDepth,
			maxObjectsPerRectangle;
	private QuadTreeNode<E> root;
	private Bounds area;
			
	public QuadTree(int maxDepth, int maxObjectsPerRectangle, Bounds area) {
		this.maxDepth = maxDepth;
		this.maxObjectsPerRectangle = maxObjectsPerRectangle;
		this.area = area;
		root = new QuadTreeNode<>(0, area, this);
	}
	
	public int getMaxDepth() {
		return maxDepth;
	}
	
	public int getMaxObjectsPerRectangle() {
		return maxObjectsPerRectangle;
	}
	
	public void addEntity(E entity) {
		root.addEntity(entity);
	}
	
	public void addEntities(List<E> entities) {
		for (int i = 0; i < entities.size(); i++) {
			addEntity(entities.get(i));
		}
	}
	
	public <T extends QuadTreeElement> void clear(Class<T> className) {
		if(className == QuadTreeElement.class) {
			root = new QuadTreeNode<>(0, area, this);
		} else {
			root.clear(className);
			root.merge();
		}
	}
	
	public void clear() {
		clear(QuadTreeElement.class);
	}
	
	public int countEntities() {
		return root.countEntities();
	}
	
	public int getDepth() {
		return root.depth();
	}
	
	public List<E> getPossibleCollisions(Bounds bounds) {
		return root.possibleCollisions(bounds);
	}
	
	public boolean remove(E entity) {
		return root.remove(entity);
	}
}