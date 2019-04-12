
public class Ligne {
	private Point point1;
	private Point point2;

	public Ligne(Point p1, Point p2 ){
		point1=p1;
		point2=p2;
	}
	
	Point getp1(){return point1;}
	Point getp2(){return point2;}

	@Override
	public String toString() {
		return "Ligne{" +
				"point1=" + point1 +
				", point2=" + point2 +
				'}';
	}
}
