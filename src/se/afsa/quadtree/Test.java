package se.afsa.quadtree;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		QuadTree<TestElement> test = new QuadTree<>(6, 5, new Bounds(0, 5, 0, 5));
		test.addEntity(new TestElement());
	}

}
