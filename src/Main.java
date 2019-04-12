import java.util.HashSet;
import java.util.Set;

public class Main {

    private static Point[] points;

    public static float distance(int a, int b){
        int sum=0;
        for(int i=a+1; i<b; i++){

        }
        return sum;
    }

    private static Set<Ligne> algoEssaisSuccessif() {
        HashSet<Ligne> lignes = new HashSet<>();
        return lignes;
    }

    public static Set<Ligne> algoDynamique(){
        HashSet<Ligne> lignes = new HashSet<>();
        int[][] scores= new int[points.length][points.length];




        return lignes;
    }

    public static void main(String[] args){

        Set<Point> pointSet = Parser.recuperePoints();
        points = new Point[pointSet.size()+1];
        for(Point p: pointSet)
            points[(int)p.getx()]=p;

        points[2].distanceTo(new Ligne(points[1], points[5]));

        Set<Ligne> lignes = algoEssaisSuccessif();

        Visu v1 = new Visu(pointSet,lignes,"Solution de score "+42);

    }

}
