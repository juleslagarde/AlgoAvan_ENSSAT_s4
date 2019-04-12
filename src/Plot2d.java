import java.awt.*;
import java.awt.geom.*;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;

@SuppressWarnings("serial")
public class Plot2d extends JPanel {

	// Ensemble de points à afficher
	Set <Point> points;

	// Ensemble de lignes à afficher, une ligne est caractérisée par deux points représentant ses extremités.
	Set <Ligne> lignes;

	public Plot2d(Set <Point> points, Set <Ligne> lignes) {
		super();
		setBackground(Color.white);
		System.out.println("contruction plot");
		this.points = points; System.out.println(points.toString());
		this.lignes = lignes; System.out.println(lignes.toString());
	}

	public Plot2d(Set <Point> points) {
		super();
		setBackground(Color.white);
		this.points = points;
		this.lignes = new HashSet<Ligne>();
	}

	final int PAD = 20;

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		int w = getWidth();
		int h = getHeight();
		// Draw ordinate
		g2.draw(new Line2D.Double(PAD, PAD, PAD, h-PAD));
		// Draw abcissa.
		g2.draw(new Line2D.Double(PAD, h-PAD, w-PAD, h-PAD));
		double xInc = (double)(w - 2*PAD)/(getMaxx());
		double scale = (double)(h - 2*PAD)/getMaxy();

		g2.setPaint(Color.black);

		// Affichage des points
		for (Point p : points){
			double x = PAD + p.getx()*xInc;
			double y = h - PAD - scale*p.gety();
			g2.fill(new Ellipse2D.Double(x-2, y-2, 4, 4));
			g2.drawString(p.toString(), (int) (x-xInc/2.0), (int) (y-3));
		}
	
	// Affichage des lignes
	for (Ligne l : lignes){
		double x1 = PAD + l.getp1().getx()*xInc;
		double y1 = h - PAD - scale*l.getp1().gety();
		double x2 = PAD + l.getp2().getx()*xInc;
		double y2 = h - PAD - scale*l.getp2().gety();
		g2.draw(new Line2D.Double(x1,y1,x2,y2));
	};

	}
	
	
	private double getMaxx() {
		double max = 0;
		for (Point p : points){if (p.getx()>max) max = p.getx();};
		return max;
	}

	private double getMaxy() {
		double max = 0;
		for (Point p : points){if (p.gety()>max) max = p.gety();};
		return max;
	}

	public static void main(String[] args) {
		
		// EXEMPLE d'UTILISATION DE LA CLASSE
		
		// Création frame
		JFrame f = new JFrame();
		f.setTitle("Algorithmique avancée - Visualiseur points et lignes brisées");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Création des points
		Point p1 = new Point (1.0,8.6);
		Point p2 = new Point (2.0,25.8);
		Point p3 = new Point (3.0,7.9);
		Point p4 = new Point (4.5,5.0);
		Point p5 = new Point (5.0,8.6);
		Point p6 = new Point (6.0,25.8);
		Point p7 = new Point (7.0,7.9);
		Point p8 = new Point (8.5,5.0);
		Set <Point> points = new HashSet<Point>();
		points.add(p1);
		points.add(p2);
		points.add(p3);
		points.add(p4);
		points.add(p5);
		points.add(p6);
		points.add(p7);
		points.add(p8);
		// Les points peuvent être obtenus par lecture d'un fichier de données par :
		//Set  <Point> points_lus ;
		//points_lus = Parser.recuperePoints();

		
		// Création des lignes composanr la ligne brisée (A VOUS DE JOUER pour trouver la meilleure approximation)
		// Encore une fois, les structues données ici sont à adapter à votre programme.
		Set <Ligne> lignes = new HashSet<Ligne>();
		lignes.add(new Ligne (p1,p2));
		lignes.add(new Ligne (p2,p7));
		lignes.add(new Ligne (p7,p8));

		f.add(new Plot2d(points,lignes));
		// si pas de ligne à afficher :
		//f.add(new Plot2d(points_lus)); 

		f.setSize(500,500);
		f.setLocation(200,200);
		f.setVisible(true);
	}
}