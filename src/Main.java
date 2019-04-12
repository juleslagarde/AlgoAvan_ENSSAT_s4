import java.util.HashSet;
import java.util.Set;

public class Main {

    private static Set<Ligne> algoEssaisSuccessif() {
        HashSet<Ligne> lignes = new HashSet<>();
        return lignes;
    }

    public static Set<Ligne> algoDynamique(){
        HashSet<Ligne> lignes = new HashSet<>();
        return lignes;
    }

    public static void main(String[] args){

        Set<Point> points = Parser.recuperePoints();

        Set<Ligne> lignes = algoEssaisSuccessif();

        Visu v1 = new Visu(points,lignes,"Solution de score "+42);

    }

}
