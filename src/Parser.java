import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/* 
 * Fichier fourni pour aider à la lecture d'un fichier de données
 * un fichier de données est composé d'un point par ligne, chaque point étant décrit par son abscisse (entier)
 * et son ordonnée (entier) séparées par un espace.
 * Les points du fichier sont ordonnées selon leurs abscisses, de 1 à n, sans point manquant ni doublon 
 * (former à respecter dans les fichiers d'entrée, ce parser ne le vérifiera pas).
 */

public class Parser {

	/**
	 * Renvoie une liste de points lu dans un fichier
	 * @param filePath Chemin d'accès au fichier
	 * @return Liste de points lus dans filePath
	 */
	public static Set <Point> recuperePoints(){
		// Liste de points récupérés
		Set <Point> points= new HashSet<Point>();

		JFileChooser dialogue = new JFileChooser(new File("."));
		dialogue.setApproveButtonText("Choisir ce fichier de test");

		if (dialogue.showOpenDialog(null)== JFileChooser.APPROVE_OPTION) {
			File fichier = dialogue.getSelectedFile();
			System.out.println(fichier.getAbsolutePath());

			try{
				// Création du flux bufférisé sur un FileReader
				BufferedReader buff = new BufferedReader(new FileReader(fichier.getAbsolutePath()));

				try {
					String line;
					// Lecture du fichier ligne par ligne.
					while ((line = buff.readLine()) != null) {
						StringTokenizer st = new StringTokenizer(line);
						Point p = new Point(Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()));
						points.add(p);
					}
				} finally {
					buff.close();
				}
			} catch (IOException ioe) {
				// erreur de fermeture des flux
				System.out.println("Erreur --" + ioe.toString());
			}
			catch (Exception e){
				JOptionPane.showMessageDialog(dialogue,
						"Fichier d'entrée non parsable, sortie du programme.",
						"Erreur",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		return points;
	}


	/**
	 * Exemple d'utilisation de la méthode recuperePoints. 
	 * Par exemple, par l'execution de la commande "java Lecteur tst1.txt" 
	 * (pour simplification, mettre les fichiers tstX.txt avec les fichiers binaires de votre programme)
	 * @param args
	 */
	public static void main(String args[]) {
		Set <Point> points;

		points=recuperePoints();

		// A ce stade, les points lus sont entrés dans la collection
		// nommée points icic, dont on ne fait qu'afficher le contenu ci-dessous
		// A vous d'en faire le traitement attendu pour le mini-projet..
		System.out.println(points.size()+" points recuperes :");
		System.out.println(points.toString());
	}
}


