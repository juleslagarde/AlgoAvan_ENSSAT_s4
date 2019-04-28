import javax.swing.*;
import java.util.*;

public class Main {

    private static Point[] points;
    private static int compteur;

    public static double distance(int a, int b) {
        double sum = 0;
        Ligne ligne = new Ligne(points[a], points[b]);
        for (int i = a + 1; i < b; i++) {
            sum += points[i].distanceTo(ligne);
        }
        return sum;
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////

    private static State algoEssaisSuccessif(Double C) {
        HashSet<Ligne> lignes = new HashSet<>();
        int n = points.length - 1;
        double SD = 0;
        int m = 0;
        double[] opt = new double[1];
        opt[0] = 10000;
        int[] X = new int[n];
        int[] ChemOpt = new int[n];
        for (int i = 0; i < n; i++) {
            X[i] = 0;
            ChemOpt[i] = 0;
        }
        X[0] = 1;
        appligbri(0, X, ChemOpt, opt, n, SD, C, m);
        int prec = 1;
        for (int i = 2; i <= n; i++) {
            if (ChemOpt[i - 1] == 1) {
                lignes.add(new Ligne(points[prec], points[i]));
                prec = i;
            }
        }
        return new State(lignes, opt[0]);
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////

    private static void appligbri(int i, int[] X, int[] ChemOpt, double[] opt, int n, double SD, double C, int m) {
        for (int Xi = n - 1; Xi > i; Xi--) {
            X[Xi] = 1;
            m++;
            SD += distance(i + 1, Xi + 1);
            if (opt[0] > (m + 1) * C + SD) {
                //System.out.println("Passage");
                if (Xi == n - 1) {
                    if (opt[0] > (double) m * C + SD) {
                        //System.out.println(opt[0]);
                        opt[0] = (double) m * C + SD;
                        //System.out.println("Nouveau");
                        for (int j = 0; j < n; j++) {
                            ChemOpt[j] = X[j];
                            //System.out.println(ChemOpt[j]);
                        }
                        //System.out.println("-----");
                        //System.out.println(opt[0]);
                        //System.out.println("-----");
                    }
                } else {
                    appligbri(Xi, X, ChemOpt, opt, n, SD, C, m);
                }
            }
            X[Xi] = 0;
            m--;
            SD -= distance(i + 1, Xi + 1);
        }
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////

    private static State algoEssaisSuccessif2(Double C) {
//        boolean[] mpoints = new boolean[points.length-1-2];//selected middle points (mpoints[0]==true => points number 2 selected)
        int max = 1 << points.length -1-2;
        int pointsI;//bit 0 correspond to points 1, bit 1 to points 2
        State bestState = new State(null,Double.POSITIVE_INFINITY);
        for(pointsI = 0; pointsI < max; pointsI++){
            Set<Ligne> lignes = createLignes(pointsI);
            //if(bestState.score < lignes.size()*C) break; //elagage, si le nombre de lignes done un score plus grand que le meilleur score
            //scores
            double score = lignes.size()*C;
            for(Ligne l : lignes){
                score += distance((int)l.getp1().getx(), (int)l.getp2().getx());
            }
            if(score<bestState.score){
                bestState.lignes=lignes;
                bestState.score=score;
            }
        }
        return bestState;
    }

    public static Set<Ligne> createLignes(int pointsI){
        Set<Ligne> lignes = new HashSet<>();
        int lastPointsSelected = 1;
        for(int i = 2; i< points.length -1; i++){
            int mask = 1 << (i - 2);
            if((pointsI&mask)!=0) {//points selected
                lignes.add(new Ligne(points[lastPointsSelected], points[i]));
                lastPointsSelected=i;
            }
        }
        lignes.add(new Ligne(points[lastPointsSelected], points[points.length - 1]));
        return lignes;
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////


    public static State algoDynamique(double C) {
        int n = points.length - 1;
        Double[][] scores = new Double[n + 1][n + 1];
        for(int a=1; a<points.length; a++)
            for(int b=a+1; b<points.length; b++)
                scores[a][b] = distance(a, b) + C;
        State state = algoDynamique_rec(C, 1, n, scores);
//        for (Double[] d : scores) {
//            System.out.println(Arrays.toString(d));
//        }
        return state;
    }

    public static State algoDynamique_rec(double C, int a, int b, Double[][] scores) {
        compteur++;
        if (b - a == 1) {
            HashSet<Ligne> lignesN = new HashSet<>();
            lignesN.add(new Ligne(points[a], points[b]));
            return new State(lignesN, C);
        }

        HashSet<Ligne> lignesN = new HashSet<>();
        lignesN.add(new Ligne(points[a], points[b]));
        State bestState = new State(lignesN, scores[a][b]);
        for (int i = a + 1; i < b; i++) {
            State sG = algoDynamique_rec(C, a, i, scores);
//            HashSet<Ligne> l = new HashSet<>();
//            l.add(new Ligne(points[a], points[i]));
//            State sG = new State(l, scores[a][i]);
            State sD = algoDynamique_rec(C, i, b, scores);
            double score = sG.score + sD.score;
            if (score < bestState.score) {
                bestState.score = score;
                sG.lignes.addAll(sD.lignes);
                bestState = new State(sG.lignes, sG.score + sD.score);
            }
        }
        return bestState;
    }

    public static void main(String[] args) {
        Random rand = new Random(42);

        Set<Point> pointSet = Parser.recuperePoints();
        points = new Point[pointSet.size() + 1];
        for (Point p : pointSet)
            points[(int) p.getx()] = p;

        System.out.println();

        double c = Double.parseDouble(JOptionPane.showInputDialog("valeur de  C : "));
        State s = new State(null, 0);
        while (points.length < 19+1) {
            compteur=0;
            s = algoDynamique(c);
            System.out.println((points.length-1) +"\t"+compteur+"\t"+s.score);
            points = Arrays.copyOf(points, points.length+1);
            points[points.length-1] = new Point(points.length-1, rand.nextDouble()*3);
            pointSet.add(points[points.length-1]);
        }
//        State s = algoEssaisSuccessif2(c);

        Visu v1 = new Visu(pointSet, s.lignes, "Solution de score " + s.score);
    }

    static class State {
        public Set<Ligne> lignes;
        public double score;

        public State(Set<Ligne> lignes, double score) {
            this.lignes = lignes;
            this.score = score;
        }
    }
}