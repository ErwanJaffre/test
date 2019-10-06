
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.lang.*;
import java.awt.event.*;

import objets.Cell;

/**
 * Classe qui cree un labyrinthe.
 * 
 * @author Cecile FRANCOU
 * @author Sophia GALLARDO
 * @version 1.1.6
 * @see Interaction
 */

public class Labyrinthe {

	/** taille des sprites dans le sens de la longueur */
	private char px = 5;

	/** taille des sprites dans le sens de la hauteur */
	private char py = 5;

	/** nombre de cases du labyrinthe dans le sens de la longueur */
	private int dx = 15;

	/** nombre de cases du labyrinthe dans le sens de la hauteur */
	private int dy = 15;

	/**
	 * Tableau qui contient des 'O' pour les cases vides et des '1' pour les cases
	 * contenant un "mur".
	 */
	private int lab[][];

	/**
	 * Utile pour generer le labyrinthe : trouve dans le code qui genere le
	 * labyrinthe.
	 */
	private boolean inv;

	/**
	 * Utile pour generer le labyrinthe : trouve dans le code qui genere le
	 * labyrinthe.
	 */
	private boolean ar = false;

	/** Coordonnees de la sortie dans le labyrinthe */
	private Cell position_sortie;

	/**
	 * Vecteur qui contient les coordonnees de toutes les cases deja occupees par :
	 * la fille, le garcon, le gant et la sortie
	 */
	private Vector position_elements_du_jeu;

	/** Interaction entre le labyrinthe, les joueurs et les decors */
	private Interaction interaction;

	/** toolkit pour l'image de la sortie */
	private Toolkit toolkit_sortie;

	/** toolkit pour l'image du "mur" */
	private Toolkit toolkit_mur;
	
	private Toolkit toolkit_mer;

	/** image de la sortie */
	private Image image_sortie;

	/** image du "mur" */
	private Image image_mur;
	private Image image_mer;
	/** couleur du chemin */
	private Color couleur_chemin;
	
	private Color couleur_mer;

	/** chemin des images : il depend du decor choisit */
	private String chemin_images;
	    public Color couleur_chemin2;
	    public Color couleur_chemin3;
	    public Color couleur_chemin4;
	    private int pass;
	    private int pass2;
	    public static List<Integer> list_x = new ArrayList<Integer>();
	    public static List<Integer> list_y = new ArrayList<Integer>();


	/**
	 * Cree un labyrinthe de longueur et de hauteur passees en parametre avec une
	 * sortie.
	 * 
	 * @param longueur    longueur du labyrinthe
	 * @param hauteur     hauteur du labyrinthe
	 * @param interaction interaction entre le labyrinthe et les joueurs
	 * @param inv         booleen necessaire a la generation du labyrinthe
	 * @throws IOException
	 *
	 */
	public Labyrinthe(int nb_cases_x, int nb_cases_y, boolean inv, Interaction interaction) throws IOException {
		chemin_images = "images" + File.separatorChar + "traditionnel" + File.separatorChar;
		couleur_chemin = new Color(240, 144, 0);
		couleur_mer = new Color(4,5,135);
		setDX(nb_cases_x);
		setDY(nb_cases_y);

		// initialisation du vecteur contenant les positions
		position_elements_du_jeu = new Vector();

		// les dimensions du laby doivent etre paires pour pouvoir le generer
		if ((dx % 2) != 0)
			dx = dx - 1;
		if ((dy % 2) != 0)
			dy = dy - 1;

		this.lab = new int[dx + 1][dy + 1];

		setLabyrinthe(); 
		// initialisation du labyrinthe

		this.interaction = interaction;

	}

	/**
	 * Modifie l'image du mur selon le decor du jeu choisit : traditionnel,
	 * montagnes, briques ou mer.
	 * 
	 * @param name_image     nom de l'image.
	 * @param couleur_chemin couleur du chemin
	 * @param chemin_images  chemin des images
	 */
	public void setChemin_image(String chemin_images, Color couleur_chemin) {
		this.chemin_images = chemin_images;
		this.couleur_chemin = couleur_chemin;
	}

	/**
	 * Retourne le chemin des images selon le decor que les joueurs ont choisi.
	 * 
	 * @return String chemin des images
	 */
	public String getChemin_image() {
		return chemin_images;
	}

	/**
	 * Retourne le vecteur qui contient les coordonnees de toutes les cases occupees
	 * par le garcon, la fille, le gant et la sortie.
	 * 
	 * @return Vector vecteur des positions.
	 */
	public Vector getPosition_elements_du_jeu() {
		return position_elements_du_jeu;
	}

	/**
	 * Re_initialise le vecteur des elements du labyrinthe.
	 * 
	 * @param position_elements_du_jeu vecteur des positions.
	 */
	public void setPosition_elements_du_jeu(Vector position_elements_du_jeu) {
		this.position_elements_du_jeu = position_elements_du_jeu;
	}

	/**
	 * Initialise le labyrinthe : genere un nouveau labyrinthe.
	 * 
	 * @throws IOException
	 */
	public void setLabyrinthe() throws IOException {
		genereParDFS(); // genere le labyrinthe
	}

	/**
	 * Indique si la case passee en parametre est libre ou pas.
	 * 
	 * @param coordonnees coordonnees d'une case.
	 * @return <code> true : </code > Les "coordonnees"(case) existe dans le vecteur
	 *         <BR>
	 *         <code> false : </code > Les "coordonnees"(case) existe dans le
	 *         vecteur
	 */
	public boolean existe_t_il(Cell coordonnees) {
		// on parcours le vecteur
		for (int i = 0; i < position_elements_du_jeu.size(); i++) {
			if ((((Cell) position_elements_du_jeu.elementAt(i)).getX() == coordonnees.getX())
					&& (((Cell) position_elements_du_jeu.elementAt(i)).getY() == coordonnees.getY())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Retourne les coordonnees de la sortie.
	 * 
	 * @return Cell coordonnees de la sortie.
	 */
	public Cell getPosition_sortie() {
		return position_sortie;
	}

	/**
	 * Initialise les coordonnees de la sortie.
	 * 
	 * @param position_sortie coordonnees de la sortie.
	 */
	public void setPosition_sortie(Cell position_sortie) {
		this.position_sortie = position_sortie;
	}

	/**
	 * Retourne la longueur du labyrinthe en pixels.
	 * 
	 * @return int longueur du labyrinthe.
	 */
	public int getPX() {
		return this.px;
	}

	/**
	 * Retourne la hauteur du labyrinthe en pixels.
	 * 
	 * @return int hauteur du labyrinthe.
	 */
	public int getPY() {
		return this.py;
	}

	/**
	 * Modifie la longueur du labyrinthe en nombres de cases.
	 * 
	 * @param dx longueur du labyrinthe.
	 */
	public void setDX(int dx) {
		this.dx = dx;
	}

	/**
	 * Modifie la hauteur du labyrinthe en nombres de cases.
	 * 
	 * @param dy hauteur du labyrinthe.
	 */
	public void setDY(int dy) {
		this.dy = dy;
	}

	/**
	 * Retourne la longueur du labyrinthe en nombre de cases.
	 * 
	 * @return int longueur du labyrinthe (en nombre de cases)
	 */
	public int getDX() {
		return this.dx;
	}

	/**
	 * Retourne la hauteur du labyrinthe en nombre de cases.
	 * 
	 * @return int hauteur du labyrinthe (en nombre de cases)
	 */
	public int getDY() {
		return this.dy;
	}

	/**
	 * Retourne les coordonnees d'une case vide du labyrinthe et qui n'est pas un
	 * mur.
	 * 
	 * @return Cell case libre
	 */
	public Cell choix_position_lab() {

		int position_x = (int) Math.floor(Math.random() * dx);
		int position_y = (int) Math.floor(Math.random() * dy);
		// tant que la case est un "mur" ou est occupee
		while ((mur(position_x, position_y)) || (existe_t_il(new Cell(position_x, position_y)))) {

			position_x = (int) Math.floor(Math.random() * dx);
			position_y = (int) Math.floor(Math.random() * dy);

		}
		Cell position = new Cell(position_x, position_y);
		// ajoute les coordonnees de la nouvelle case occupee dans le vecteur
		position_elements_du_jeu.addElement(position);

		return position;
	}

	/**
	 * Genere un labyrinthe : alogorithme trouvee sur le Web. Depth First Search
	 * algorithm http://www.mazeworks.com/mazetut/howto.htm
	 * 
	 * @throws IOException
	 */
	public void genereParDFS() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("laberinto1"));
		String line = null;
		List<String> items = new ArrayList<String>();
		StringTokenizer splitter;
		while ((line = reader.readLine()) != null) {
			items.add(line);
		}
		this.lab = new int[items.size()][15];
		int counter = 0;
		for (String item : items) {
			splitter = new StringTokenizer(item, ",");
			this.lab[counter][0] = Integer.parseInt((String) splitter.nextElement());
			this.lab[counter][1] = Integer.parseInt((String) splitter.nextElement());
			this.lab[counter][2] = Integer.parseInt((String) splitter.nextElement());
			this.lab[counter][3] = Integer.parseInt((String) splitter.nextElement());
			this.lab[counter][4] = Integer.parseInt((String) splitter.nextElement());
			this.lab[counter][5] = Integer.parseInt((String) splitter.nextElement());
			this.lab[counter][6] = Integer.parseInt((String) splitter.nextElement());
			this.lab[counter][7] = Integer.parseInt((String) splitter.nextElement());
			this.lab[counter][8] = Integer.parseInt((String) splitter.nextElement());
			this.lab[counter][9] = Integer.parseInt((String) splitter.nextElement());
			this.lab[counter][10] = Integer.parseInt((String) splitter.nextElement());
			this.lab[counter][11] = Integer.parseInt((String) splitter.nextElement());
			this.lab[counter][12] = Integer.parseInt((String) splitter.nextElement());
			this.lab[counter][13] = Integer.parseInt((String) splitter.nextElement());
			this.lab[counter][14] = Integer.parseInt((String) splitter.nextElement());
			counter++;
		}
		for (int i = 0; i < this.lab.length; i++) {
			for (int j = 0; j < this.lab[i].length; j++) {

				System.out.print(" " + this.lab[i][j]);
			}
			System.out.println();
		}

		for (int i = 0; i < this.lab.length; i++) {
			for (int j = 0; j < this.lab[i].length; j++) {
				if (this.lab[i][j] == 0) {
					this.setVal(i, j, 0);

				}
			}
		}

		int cx, cy;
		do {
			cx = (int) Math.floor(Math.random() * dx);
			cy = (int) Math.floor(Math.random() * dy);
		} while (this.lab[cx][cy] == 1);
		// on en choisit une au hazard comme point de depart : c'est la sortie
		Cell cc = new Cell(cx, cy);
		setPosition_sortie(cc); // la sortie
		// on ajoute la position de la sortie dans le vecteur
		position_elements_du_jeu.addElement(getPosition_sortie());
		nbterrains();
	}

	/**
	 * Indique si les murs sont intacts. Methode liee avec la methode qui genere le
	 * labyrinthe
	 * 
	 * @return <code> true : </code> les murs sont intacts <BE>
	 *         <code> false : </code> les murs ne sont pas intacts
	 */
	public boolean murIntacts(Cell c) {
		int x = c.getX();
		int y = c.getY();

		if ((this.lab[x + 1][y] == 1) && (this.lab[x - 1][y] == 1) && (this.lab[x][y + 1] == 1)
				&& (this.lab[x][y - 1] == 1)) {

			return true;
		} else {

			return false;
		}
	}
	


	/**
	 * Retourne '1' si les coordonnees de la case passee en parametre correspondent
	 * a un mur ou a un chemin. return <code> 0 : </code> si la case est vide <BR>
	 * <code> 1 : </code> si la case contient un "mur"
	 * 
	 * @param x coordonnees en x
	 * @param y coordonnees en y
	 */
	public int getVal(int x, int y) {
		if ((this.lab[x][y] == 1) || (this.lab[x][y] == 2))
			return '1';
		else
			return '0';
	}

	/**
	 * Modifie le contenu d'une case du tableau.
	 * 
	 * @param x   coordonnees en x
	 * @param y   coordonnees en y
	 * @param val valeur de la case
	 */
	public synchronized void setVal(int x, int y, int val) {
		this.lab[x][y] = val; // modification de la case
		if (!(this.inv) && (val == 3))
			return;
		setAskedRepaint();
	}

	/**
	 * Methode trouvee sur internet qui sert a generer le labyrinthe.
	 */
	public boolean isAskedRepaint() {
		return this.ar;
	}

	/**
	 * Methode trouvee sur internet qui sert a generer le labyrinthe.
	 */
	public void setAskedRepaint() {
		this.ar = true;
	}

	/**
	 * Methode trouvee sur internet qui sert a generer le labyrinthe.
	 */
	public void unSetAskedRepaint() {
		this.ar = false;
	}

	/**
	 * Indique si il y a un "mur" dans la case dont les coordonnees sont passees en
	 * parametre.
	 * 
	 * @return <code> true : </code> il y a un "mur" dans la case <BR>
	 *         <code> false : </code> la case est vide
	 * @param x coordonnees en x
	 * @param y coordonnees en y
	 */
	public boolean mur(int x, int y) {
		// il y a un mur si la case contient un 1
		return (lab[x][y] == 1);
	} // fin de mur()
	
	public boolean mer(int x, int y) {
		// il y a un mur si la case contient un 1
		return (lab[x][y] == 2);
	} // fin de mur()
	
	 public String type_terrain(int x, int y){
			if (this.lab[x][y]==1) {
				return "Pared";
			}
			else if (this.lab[x][y]==2){
				return "Mar";
			}
			return "Camino";
	 }

	
	
	public void nbterrains() {
		int nb=0;
		boolean valeurtrouve=false;
		int valeurs[] = {99,99,99,99};
		for (int i = 0; i < this.lab.length; i++) {
			for (int j = 0; j < this.lab[i].length; j++) {
				int c =this.lab[i][j];
				System.out.println(this.lab[i][j]);
				for(int x = 0; x < nb+1; x++) {
					if (valeurs[x]== c) {
						 valeurtrouve=true;
					}
				}
				if (!valeurtrouve) {	
				valeurs[nb]=c;
				nb=nb+1;
						
					
				}
					
				
				
			}
		
	} 
		System.out.println(Arrays.toString(valeurs));
	System.out.println("Il y a " + nb + " terrains différente(s) dans le labyrinthe ");
	} 
	

	

	/**
	 * Dessine le labyrinthe et la sortie dans la fenetre.
	 * 
	 * @param g          graphique
	 * @param nb_cases_x longueur du labyrinthe (en nombre de cases)
	 * @param mn_cases_y hauteur du labyrinthe (en nombre de cases)
	 */
	public void dessineToi(Graphics g, int nbcases_x, int nbcases_y) {

		// les toolkits
		toolkit_mur = interaction.getToolkit();
		toolkit_sortie = interaction.getToolkit();
		toolkit_mer = interaction.getToolkit();

		// les images
		image_mur = toolkit_mur.getImage(chemin_images + "mur.gif");
		image_sortie = toolkit_sortie.getImage(chemin_images + "sortie.gif");
		image_mer = toolkit_mer.getImage(chemin_images + "mer.gif");

		int x = 0;
		int y = 0;

		for (int i = 0; i <= dx; i++) {

			// Avant de dessiner une nouvelle ligne, on repart a la colonne 0
			y = 0;
			for (int j = 0; j <= dy; j++) {

				if (mur(i, j)) { // si la ligne des y est a remplir.. de "mur"
					// on affiche l'image
					g.drawImage(image_mur, x, y, interaction.getLongueurcase(), interaction.getHauteurcase(),
							interaction);

				} else
				if (mer(i, j)) {
					g.drawImage(image_mer, x, y, interaction.getLongueurcase(), interaction.getHauteurcase(),
							interaction);
				}
				else
				
				{
				
					// on dessine le chemin
					g.setColor(couleur_chemin);

					g.fillRect(x, y, interaction.getLongueurcase(), interaction.getHauteurcase());
				}

				// On passe a la colonne suivante

				y = y + interaction.getHauteurcase();
			}

			// On commence a la ligne suivante. Si le nombre de cases est

			x = x + interaction.getLongueurcase();

		}

		// on dessine la sortie
		g.drawImage(image_sortie, (getPosition_sortie()).getX() * interaction.getLongueurcase(),
				(getPosition_sortie()).getY() * interaction.getHauteurcase(), interaction.getLongueurcase(),
				interaction.getHauteurcase(), interaction);

	}// fin de dessineToi()

} // fin classe

/*

					
 */
