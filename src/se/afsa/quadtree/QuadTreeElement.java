package se.afsa.quadtree;

/**
 * @author Mattias J�nsson
 * An object must implement this interface to be able to be used in a quad-tree.
 *
 */
public interface QuadTreeElement {
	public Bounds bounds();
}
