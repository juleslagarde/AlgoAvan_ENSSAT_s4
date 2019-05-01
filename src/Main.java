import javax.swing.*;
import java.util.*;

public class Main {

    private static Point[] points;

    public static double distance(int a, int b) {
        double sum = 0;
        Ligne ligne = new Ligne(points[a], points[b]);
        for (int i = a + 1; i < b; i++) {
            sum += points[i].distanceTo(ligne);
        }
        return sum;
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * essais successifs
     * @param C cout de création d'un segment
     * @return (lignes, cout)
     */
    private static State algoEssaisSuccessif(Double C) {
        HashSet<Ligne> lignes = new HashSet<>();
        int n = points.length - 1;									//On récupère le nombre de points étudié
        double SD = 0;												//On initialise les différentes variables
        int m = 0;
        double[] opt = new double[1];
        opt[0] = Double.POSITIVE_INFINITY;
        int[] X = new int[n];
        int[] ChemOpt = new int[n];
        for (int i = 0; i < n; i++) {
            X[i] = 0;
            ChemOpt[i] = 0;
        }
        X[0] = 1;
        appligbri(0, X, ChemOpt, opt, n, SD, C, m);					//On lance un appel d'appligbri
        int prec = 1;
        for (int i = 2; i <= n; i++) {								//On convertit ChemOpt en un ensemble de lignes
            if (ChemOpt[i - 1] == 1) {
                lignes.add(new Ligne(points[prec], points[i]));
                prec = i;
            }
        }
        return new State(lignes, opt[0]);							//On renvoie l'ensemble de lignes et son coût
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * algo recursif essais successifs
     *
     * @param i position dans X à partir de laquel on commence la modification
     * @param X chemin en cours de construction
     * @param ChemOpt chemin actuellement optimal
     * @param opt cout de ChemOpt
     * @param n taille de X et de ChemOpt
     * @param SD distance de tous les points inférieurs à i par rapport au chemin X
     * @param C cout de création d'un segment
     * @param m nombre de segments
     */
    private static void appligbri(int i, int[] X, int[] ChemOpt, double[] opt, int n, double SD, double C, int m) {
        for (int Xi = n - 1; Xi > i; Xi--) {						//On explore tous les points après i
            //enregistrer(Xi)
            X[Xi] = 1;
            m++;
            SD += distance(i + 1, Xi + 1);
            //
            if (Xi == n - 1) {		//satisfaisant(Xi)
                if (opt[0] > (double) m * C + SD) {		//soltrouvée
                    //optimal
                    opt[0] = (double) m * C + SD;
                    for (int j = 0; j < n; j++) {
                        ChemOpt[j] = X[j];
                    }
                    //
                }
            } else {
                if (opt[0] > (m + 1) * C + SD) {	//OptEncorePossible
                    //Le chemin passe maintenant par i puis Xi, et on teste toutes les façons de le compléter
                    appligbri(Xi, X, ChemOpt, opt, n, SD, C, m);
                }
            }
            //défaire(Xi)
            X[Xi] = 0;
            m--;
            SD -= distance(i + 1, Xi + 1);
            //
        }
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * algo dynamique
     * @param C cout de cr&eacute;ation d'un segment
     * @return (lignes, score)
     */
    public static State algoDynamique(double C) {
        int n = points.length - 1; // le nombre de points
        State[][] states = new State[n + 1][n + 1];
        for (int i = 0; i < n-1; i++) { //initialisation de la diagonale b-1-a==0
            HashSet<Ligne> lignes = new HashSet<>();
            lignes.add(new Ligne(points[i+1],points[i+2]));
            states[i+1][i+2]=new State(lignes, C);
        }
        //iteration sur les diagonales b-delta-a==0
        for(int delta=2; delta<n; delta++){
            //iteration le long de la diagonale b-delta-a==0
            for (int a = 1, b=a+delta; b <= n; a++, b++){
                //initialisation du chemin optimal avec le chemin direct de a à b
                HashSet<Ligne> lignes = new HashSet<>();
                lignes.add(new Ligne(points[a],points[b]));
                states[a][b] = new State(lignes,distance(a,b)+C);
                //recherche du score minimal pour le chemin de a à b
                for (int i = a+1; i < b; i++) {
                    State sL = states[a][i];
                    State sR = states[i][b];
                    if(sL.score+sR.score<states[a][b].score){
                        lignes = new HashSet<>(sL.lignes);
                        lignes.addAll(sR.lignes);
                        states[a][b] = new State(lignes, sL.score + sR.score);
                    }
                }
            }
        }
        return states[1][n];
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////


    public static void main(String[] args) {
        Random rand = new Random(42);

        Set<Point> pointSet = Parser.recuperePoints();
        points = new Point[pointSet.size() + 1];
        for (Point p : pointSet)
            points[(int) p.getx()] = p;

        System.out.println();

        double c = Double.parseDouble(JOptionPane.showInputDialog("valeur de  C : "));
        State s = algoEssaisSuccessif(c);
        Visu v1 = new Visu(pointSet, s.lignes, "Solution de score avec algorithme successif " + s.score);
        State s1 = algoDynamique(c);
        Visu v2 = new Visu(pointSet, s1.lignes, "Solution de score avec algorithme dynamique " + s1.score);
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////


    static class State {
        public Set<Ligne> lignes;
        public double score;

        public State(Set<Ligne> lignes, double score) {
            this.lignes = lignes;
            this.score = score;
        }

        @Override
        public String toString() {
            return ""+score;
        }
    }
}
