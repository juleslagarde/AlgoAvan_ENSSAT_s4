import javax.swing.*;
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

    public static State algoDynamique(double C){
        int n = points.length-1;
        Double[][] scores= new Double[n+1][n+1];
        State state = algoDynamique_rec(C, 1, n, scores);
        for(Double[] d : scores){
            System.out.println(Arrays.toString(d));
        }
        return state;
    }

    public static State algoDynamique_rec(double C, int a, int b, Double[][]scores) {
        if(b-a==1){
            HashSet<Ligne> lignesN = new HashSet<>();
            lignesN.add(new Ligne(points[a], points[b]));
            return new State(lignesN, C);
        }
        if(scores[a][b]==null)
            scores[a][b] = distance(a, b)+C;

        HashSet<Ligne> lignesN = new HashSet<>();
        lignesN.add(new Ligne(points[a], points[b]));
        State bestState=new State(lignesN, scores[a][b]);
        for(int i=a+1; i<b; i++) {
            State sG = algoDynamique_rec(C, a, i, scores);
            State sD = algoDynamique_rec(C, i, b, scores);
            double score = sG.score + sD.score;
            if(score < bestState.score){
                bestState.score=score;
                sG.lignes.addAll(sD.lignes);
                bestState=new State(sG.lignes, sG.score+sD.score);
            }
        }
        return bestState;
    }

    public static void main(String[] args){

        Set<Point> pointSet = Parser.recuperePoints();
        points = new Point[pointSet.size()+1];
        for(Point p: pointSet)
            points[(int)p.getx()]=p;

        System.out.println();

        double c = Double.parseDouble(JOptionPane.showInputDialog("valeur de  C : "));
        State s = algoDynamique(c);

        Visu v1 = new Visu(pointSet,s.lignes,"Solution de score "+s.score);

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
