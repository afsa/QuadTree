package se.afsa.quadtree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mattias Jönsson
 * This class represents a node in a quad-tree.
 * @param <E> - the type of objects this tree should hold.
 */
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
	
	/**
	 * Split the node into four children.
	 */
	protected void split() {
		for (int i = 0; i < numberOfChildren; i++) {
			children.add(i, new QuadTreeNode<>(depthLevel + 1, area.split(i), tree));
		}
		
		moveEntities();
	}
	
	/**
	 * Merge nodes with to few elements.
	 */
	protected void merge() {
		if(hasChildren()) {
			boolean shouldRemove = true;
			for (int i = 0; i < numberOfChildren; i++) {
				QuadTreeNode<E> child = children.get(i);
				child.merge();
				if(child.hasChildren() || child.hasEntities()) {
					shouldRemove = false;
				}
			}
			
			if (shouldRemove) {
				children.clear();
			}
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
	
	protected boolean hasEntities() {
		return !entities.isEmpty();
	}
	
	protected boolean hasChildren() {
		return !children.isEmpty();
	}
	
	private void append(E entity) {
		entities.add(entity);
		entitiesLength++;
	}
	
	protected void addEntity(E entity) {		
		if(hasChildren() || (entitiesLength >= tree.getMaxObjectsPerRectangle() && depthLevel < tree.getMaxDepth())) {
			if(!hasChildren()) {
				split();
			}
			
			int index = getIndex(entity.bounds()); 
			
			if(index != -1) {
				children.get(index).addEntity(entity);
				return;
			}
		}
		
		append(entity);
	}
	
	/**
	 * Get where to insert the child.
	 * @param bounds - the child's bounds.
	 * @return The index for the child. -1 is parent.
	 */
	private int getIndex(Bounds bounds) {
		int x, y;
		
		x = (bounds.getMinX() > area.getMidX() && bounds.getMaxX() < area.getMaxX()) ? 1 : ((bounds.getMaxX() < area.getMidX() && bounds.getMinX() > area.getMinX()) ? -1 : 0);
		y = (bounds.getMinY() > area.getMidY() && bounds.getMaxY() < area.getMaxY()) ? 1 : ((bounds.getMaxY() < area.getMidY() && bounds.getMinY() > area.getMinY()) ? -1 : 0);
		
		if(x == 0 || y == 0) {
			return -1;
		}
		
		//Make it possible to detect x, y with one comparison.
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
	
	protected int depth() {
		int[] childrenNodesLength = new int[4];
		if(!children.isEmpty()) {
			for (int i = 0; i < numberOfChildren; i++) {
				childrenNodesLength[i] = children.get(i).depth();
			}
			
			return 1 + max(childrenNodesLength);
		}
		
		return 1;
	}
	
	/**
	 * Get the max value of ints.
	 * @param ints - the ints.
	 * @return The largest int.
	 */
	private int max(int[] ints) {
		int maxValue;
		
		if(ints.length == 0) {
			return 0;
		}
		
		maxValue = ints[0];
		
		for (int i = 1; i < ints.length; i++) {
			maxValue = Math.max(maxValue, ints[i]);
		}
		
		return maxValue;
	}
	
	/**
	 * Move entities after a split.
	 */
	private void moveEntities() {
		List<E> temp = new ArrayList<>(entities);
		int tempLength = entitiesLength;
		entities.clear();
		entitiesLength = 0;
		
		for (int i = 0; i < tempLength; i++) {
			addEntity(temp.get(i));
		}
	}

	/**
	 * Remove all objects in the tree that is instance of className.
	 * @param className - the type of objects to remove.
	 */
	protected <T extends QuadTreeElement> void clear(Class<T> className) {
		// TODO Auto-generated method stub
		for (int i = 0; i < entitiesLength; i++) {
			E temp = entities.get(i);
			if(className.isInstance(temp)) {
				entities.remove(temp);
				entitiesLength--;
				i--;
			}
		}
		
		if (!children.isEmpty()) {
			for (int i = 0; i < numberOfChildren; i++) {
				children.get(i).clear(className);
			}
		}
	}
	
	/**
	 * Get a list of possible collisions with a specific area.
	 * @param bounds - the area to test for collisions.
	 * @return The objects that could collide with the area.
	 */
	protected List<E> possibleCollisions(Bounds bounds) {
		List<E> temp = new ArrayList<>();
		int index = getIndex(bounds);
		
		if(!hasChildren()) {
			return new ArrayList<>(entities);
		}
		
		if(index == -1) {
			temp.addAll(getAllChildNodeEntities());
		} else {
			temp.addAll(entities);
			temp.addAll(children.get(index).possibleCollisions(bounds));
		}
		
		return temp;
	}
	
	/**
	 * Get all entities form the children nodes.
	 * @return All the children.
	 */
	protected List<E> getAllChildNodeEntities() {
		List<E> temp = new ArrayList<>(entities);
		
		if(hasChildren()) {
			for (int i = 0; i < numberOfChildren; i++) {
				temp.addAll(children.get(i).getAllChildNodeEntities());
			}
		}
		
		return temp;
	}
	
	protected boolean remove(E entity) {
		int index = getIndex(entity.bounds());
		
		if ((!hasChildren() || index == -1) && entities.contains(entity)) {
			entities.remove(entity);
			entitiesLength--;
			return true;
		} else if(hasChildren() && index != -1) {
			return children.get(index).remove(entity);
		}
		
		return false;
	}
}
