package se.afsa.quadtree;

import java.util.ArrayList;
import java.util.List;

public class QuadTreeNode<E extends QuadTreeElement> {
	
	private final int depthLevel;
	private List<E> entities = new ArrayList<>();
	private int entitiesLength = 0;
	private Bounds area;
	private QuadTree<E> tree;
	private List<QuadTreeNode<E>> children = new ArrayList<QuadTreeNode<E>>();
	private final int numberOfChildren = 4;
	
	protected QuadTreeNode(int depthLevel, Bounds area, QuadTree<E> tree) {
		this.depthLevel = depthLevel;
		this.area = area;
		this.tree = tree;
	}
	
	protected void split() {
		for (int i = 0; i < numberOfChildren; i++) {
			children.add(i, new QuadTreeNode<>(depthLevel + 1, area.split(i), tree));
		}
	}
	
	protected int countEntities() {
		int tot = entitiesLength;
		if(!children.isEmpty()) {
			for (int i = 0; i < numberOfChildren; i++) {
				tot += children.get(i).countEntities();
			}
		}
		return tot;
	}
	
	protected boolean checkMerge() {
		return countEntities() <= tree.getMaxObjectsPerRectangle();
	}
	
	protected void merge() {
				
	}
	
	protected boolean hasEntities() {
		return !entities.isEmpty();
	}
	
	private void append(E entity) {
		entities.add(entity);
		entitiesLength++;
	}
	
	protected void addEntity(E entity) {		
		if(!children.isEmpty() || (entitiesLength >= tree.getMaxObjectsPerRectangle() && depthLevel < tree.getMaxDepth())) {
			if(children.isEmpty()) {
				split();
			}
			
			int index = getIndex(entity);
			
			if(index != -1) {
				children.get(index).addEntity(entity);
				return;
			}
		}
		
		append(entity);
	}
	
	private int getIndex(E entity) {
		Bounds bounds = entity.bounds();
		int x, y;
		
		x = (bounds.getMinX() > area.getMinX()) ? 1 : ((bounds.getMaxX() < area.getMinX()) ? -1 : 0);
		y = (bounds.getMinY() > area.getMidY()) ? 1 : ((bounds.getMaxY() < area.getMaxY()) ? -1 : 0);
		
		if(x == 0 || y == 0) {
			return -1;
		}
		
		int tot = 2*x + y;
		
		switch (tot) {
			case 3:
				return 0;
				
			case -1:
				return 1;
				
			case -3:
				return 2;
				
			case 1:
				return 3;
				
		default:
			return -1;
		}
	}
}
