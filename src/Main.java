import java.util.*;

public class Main {

    private static Point[] points;

    public static double distance(int a, int b){
        double sum=0;
        Ligne ligne = new Ligne(points[a], points[b]);
        for(int i=a+1; i<b; i++){
            sum+=points[i].distanceTo(ligne);
        }
        return sum;
    }

    private static Set<Ligne> algoEssaisSuccessif() {
        HashSet<Ligne> lignes = new HashSet<>();
        return lignes;
    }

    public static Set<Ligne> algoDynamique(double C){
        HashSet<Ligne> lignes = new HashSet<>();
        int n = points.length-1;
        Double[][] scores= new Double[n+1][n+1];
        State s = algoDynamique_rec(C, 1, n, lignes, scores);
        return s.lignes;
    }

    public static State algoDynamique_rec(double C, int a, int b, Set<Ligne> lignes, Double[][]scores) {
        if(b-a==1){
            HashSet<Ligne> lignesN = new HashSet<>();
            lignesN.add(new Ligne(points[a], points[b]));
            return new State(lignesN, C);
        }
        if(scores[a][b]==null)
            scores[a][b] = distance(a, b)+C;
        else
            a=a;
        double bestScore = scores[a][b];
        Integer bestI=null;
        for(int i=a+1; i<b; i++) {
            State sG = algoDynamique_rec(C, a, i, lignes, scores);
            State sD = algoDynamique_rec(C, i, b, lignes, scores);
            double score = sG.score + sD.score;
            if(score <bestScore){
                bestScore=score;
                bestI=i;
            }
        }
        HashSet<Ligne> lignesN = new HashSet<>(lignes);
        if(bestI!=null){
            lignesN.add(new Ligne(points[a], points[b]));
        }
        return new State(lignesN, bestScore);
    }

    public static void main(String[] args){

        Set<Point> pointSet = Parser.recuperePoints();
        points = new Point[pointSet.size()+1];
        for(Point p: pointSet)
            points[(int)p.getx()]=p;

        System.out.println();

        Set<Ligne> lignes = algoDynamique(0);

        Visu v1 = new Visu(pointSet,lignes,"Solution de score "+42);

    }

    static class State{
        public Set<Ligne> lignes;
        public double score;

        public State(Set<Ligne> lignes, double score) {
            this.lignes = lignes;
            this.score = score;
        }
    }
}
