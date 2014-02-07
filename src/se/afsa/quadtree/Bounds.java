package se.afsa.quadtree;

public class Bounds {
	private double[] values = new double[4];
	
	public Bounds(double minX, double maxX, double minY, double maxY) {
		setValues(minX, maxX, minY, maxY);
	}
	
	public void setValues(double minX, double maxX, double minY, double maxY) {
		values[0] = minX;
		values[1] = maxX;
		values[2] = minY;
		values[3] = maxY;
	}
	
	public double getMinX() {
		return values[0];
	}
	
	public double getMaxX() {
		return values[1];
	}
	
	public double getMinY() {
		return values[2];
	}
	
	public double getMaxY() {
		return values[3];
	}
	
	public double getMidX() {
		return (getMinX()+getMaxY())/2;
	}
	
	public double getMidY() {
		return (getMinY()-getMaxY())/2;
	}
	
	public Bounds split(int i) {
		double
			tempMinX = (i == 0 || i == 3) ? getMidX() : getMinX(),
			tempMaxX = (i == 1 || i == 2) ? getMidX() : getMaxX(),
			tempMinY = (i == 1 || i == 0) ? getMidY() : getMinY(),
			tempMaxY = (i == 2 || i == 3) ? getMidY() : getMaxY();
		return new Bounds(tempMinX, tempMaxX, tempMinY, tempMaxY);
	}
}
