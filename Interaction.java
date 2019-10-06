
import java.awt.*;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.awt.event.*;


import objets.Cell;
import objets.TouchesClavier;
import fenetres.Message;
import scores.AffichageScores;

/** Classe qui fait le lien entre les joueurs, le labyrinthe et les decors. Gere et definit les interactions entre les joueurs et le
 * labyrinthe. 
 * @author Cecile FRANCOU
 * @author Sophia GALLARDO
 * @version 1.1.6 
 * @see Labyrinthe
 * @see Joueur
 * @see FenetreDuJeu
 * @see AffichageScores
 * @see Decors
 * @see Fille
 * @see Garcon
 * @see Labyrinthe
 */

public class Interaction extends Canvas implements Runnable, KeyListener{

    /** fenetre ou se deroule le jeu */
    private FenetreDuJeu fenetre;

    /** Affichage des scroes */
    private AffichageScores affichage_scores;

    /** la fille */
    private Fille fille;

    /** le labyrinthe */
    private Labyrinthe laby;
    
    /** nombre de cases dans le sens de la hauteur */
    private int nbcases_y;

    /** nombre de cases dans le sens de la longueur */
    private int nbcases_x;
    
    /** longueur de la fenetre en pixels */
    private int longueur;

    /** hauteur de la fenetre en pixels */
    private int hauteur;
    
    /** nombre de jeux dans une partie : 1, 3 ou 5 jeux */
    private int nb_jeux;

    /** compteur de jeu dans une partie */
    private int  compteur_jeu; 

    /** double buffer pour mettre les images en memoire */
    private Image db;

    /** objet graphique qui sert a afficher tous les elements
     * graphiques du jeu dans le meme contexte graphique*/
    private Graphics dbGC;
    
    /** le thread de la classe interaction qui dessine tout */
    private Thread thread_interaction;

    /** Indique si le jeu est termine */
    private boolean jeu_termine;
    

    /** Indique si la fille a atteint la sortie */
    private boolean sortie;

    /** Tableau pour afficher les scores des joueurs et les statistiques
     * telles que le nombre de jeux dans une partie et le numero de jeu
     * dans la partie.
     */
    private int tableau_pour_afficher[];



 


    /** Cree le jeu.
     * L'objet Interaction fait le lien entre les joueurs et le labyrinthe
     * il gere toutes les interactions entre les joueurs et le labyrinthe 
     * (ainsi qu'avec les autres objets du jeu comme le gant de boxe)
     * Ajout de l'ecouteur dans la fenetre.
     */
    public Interaction(FenetreDuJeu fenetre){
      this.fenetre = fenetre;
      jeu_termine = false; // le jeu n'est pas termine
      System.out.println("LE LABYRINTHE EST GÉNÉRÉ !");
      addKeyListener(this); // on ajoute l'ecouteur
      compteur_jeu = 1; // le compteur de jeu est a 1
    }

    /** Initialisation du jeu. 
     * Initialisation des compteurs.*/
    public void initJeu(){
        // initialisation du compteur de jeux
        initCompteur_de_jeu();
        // le nombre de jeux gagnes par la fille
        fille.initNb_jeux_gagnes();
        // initialisation du tableau pour afficher les scores
        tableau_pour_afficher = new int[4]; 
        affichage_scores = new AffichageScores();
        // la fille n'est pas sortie
        sortie = false;
        // le jeu n'est pas termine
        jeu_termine = false;
    }

    /** Genere un nouveau jeu.      
     * On initialise les variables et on demarre le jeu.
     * @throws IOException 
     */
    public void nouveau_jeu() throws IOException{

        // la fille n'est pas sortie
        sortie = false;
        // le jeu n'est pas termine
        jeu_termine = false;
        
        /*
          on efface toutes les coordonnees des positions anciennes de 
          la fille, du garcon, du gant et de la sortie qui se trouvent dans le 
          vecteur pour pouvoir allouer d'autres coordonnees pour ces 
          elements du jeu et pour pouvoir les stocker dans ce vecteur
        */
       
         
        // on demarre un autre jeu
	demarrer(laby.getDX(),laby.getDY(),fille.getNb_jeux_gagnes());
        
        
    }



    /** Renvoie le nombre de jeux totaux dans la partie.
     *   @return int nombre de jeux dans la partie
     */
    public int getNb_jeux(){ 
        return nb_jeux;
    }
    

    /** Modifie le nombre de jeux dans la partie.
     *  @param nb_jeux nombre de jeux dans la partie
     */
    public void setNb_jeux(int nb_jeux){
        this.nb_jeux = nb_jeux;
    }

    /** Incremente le compteur de jeu.*/
    public void AddCompteur_de_jeu(){
        compteur_jeu++;
    }
    
    /** Renvoie le compteur de jeu.
     * numero de jeu dans la partie courante
     * @return int compteur de jeu
     */ 
    public int getCompteur_jeu(){
	return compteur_jeu;
    }	




    /** Intialialise le compteur de jeu a 1.*/
    public void initCompteur_de_jeu() {
        compteur_jeu = 1;
    }

    /** On demarre le jeu.
     * On genere les joueurs, le gant et le labyrinthe et on demarre
     * les threads.
     * @param longueur longueur du labyrinthe
     * @param hauteur hauteur du labyrinthe
     * @param nb_jeux_gagnes_fille nombre de jeux gagnes par la fille
     * @param nb_jeux_gagnes_garcon nombre de jeux gaggnes par le garcon
     * @throws IOException 
     */
    public void demarrer(int nb_cases_x, int nb_cases_y,int nb_jeux_gagnes_fille) throws IOException {
        // la fille n'est pas sortie
        sortie = false;
	
        /* si la longueur et la hauteur du labyrinthe est differente de 0,
         * ca veut dire que l'on a personnalise le labyrinthe
         * et donc on cree le labyrinthe avec ces nouvelles valeurs
         * passees en parametre.
         * Si ces valeurs sont egales a 0, ca veut dire, que l'on n'a
         * pas personnalise le labyrinthe et donc la hauteur et la longueur
         * ont des valeurs par defaut (longueur = 10 et hauteu)
         */

	if ((nb_cases_x != 0) && (nb_cases_y != 0)){
		this.nbcases_x = nb_cases_x;
		this.nbcases_y = nb_cases_y;
	}      

        // on cree le double buffer
        db = createImage(1000,1000);
        dbGC = db.getGraphics();
    
        // on genere le labyrinthe qvec une certaine hauteur et longueur
	genererLaby(this.nbcases_x,this.nbcases_y);
     
     
        
        // on affecte le nombre de jeux gagnes par le garcon
        // au cas ou ce le jeu en cours n'est pas le premier jeu de la partie.

        // on genere la fille
        genererFille(laby.choix_position_lab());
        fille.setNb_jeux_gagnes(nb_jeux_gagnes_fille);
        
       
      


        // on demarre les joueurs : la fille et le garcon
        
        fille.demarrer();

        
        // on demarre le thread qui va tout dessiner
	(thread_interaction = new Thread(this)).start();
    }


	



    
    

    /** Creation de la fille : on lui attribut des touches et une position
     *initiale.
     * @param position position position de la fille
     */
    public void genererFille(Cell position){
        fille = new Fille(new TouchesClavier(KeyEvent.VK_LEFT,
                                                        KeyEvent.VK_RIGHT,
                                                        KeyEvent.VK_UP,
                                             KeyEvent.VK_DOWN,'0',KeyEvent.VK_ENTER),
                          position, (Interaction)this);
	
    }//fin genererFille()


    /** Creation du labyrinthe avec un certain nombre de cases en hauteur et
     * en longueur.
     * @param longueur longueur du labyrinthe
     * @param hauteur hauteur du labyrinthe
     * @throws IOException 
     */
    public void genererLaby(int nb_cases_x, int nb_cases_y) throws IOException{
        laby = new Labyrinthe(nb_cases_x,nb_cases_y,true,(Interaction)this);
  
        
    }//fin genererLaby()

    /** On veut afficher les joueurs, le numero du jeu, le nombre 
     * de jeu total et le nombre de jeu gagne par chacun des joueurs.
     *tableau_pour_afficher[0] : numero du jeu
     *tableau_pour_afficher[1] : score de la fille (jeux gagnes)
     *tableau_pour_afficher[2] : score du garcon
     *tableau_pour_afficher[3] : nombre de jeux dans la partie
     */
    public void afficher() {
        // initialisation du tableau
        tableau_pour_afficher[0] = compteur_jeu;
        tableau_pour_afficher[1] = fille.getNb_jeux_gagnes();
        tableau_pour_afficher[2] = fille_positionY();
        tableau_pour_afficher[3] = fille_positionX();
    
        // on affiche les scores
        affichage_scores.onAffiche_les_scores(tableau_pour_afficher);
    }


    /** Renvoie la dimension de la fenetre.
     * @return dimension dimension de la fenetre.
     */
    public Dimension getPreferredSize(){
        return new Dimension (800,800);
    }
    
    /** Renvoie la dimension de la fenetre.
     *@return la dimension de la fenetre.
     */
    public Dimension getMinimumSize(){
        return getPreferredSize();
    }
    
 

    /** Run : Procedure executee continuellement si le thread est actif.
     * On endort le thread "interaction" toutes les 100 ms.
     */
    public void run(){
        while(true){
	    repaint();
            requestFocus();
            // On attend 100ms
            try {
                Thread.sleep(100);
            } catch(InterruptedException e) {
                System.out.println("Erreur dans le sleep(100);");
                e.printStackTrace();
            }
          
        }
    }//fin de run()
    

    /** Renvoie la longueur d'une case.
     * @return int longueur d'une case du labyrinthe.
     */
    public int getLongueurcase(){
        return longueur/nbcases_x;
    }

    /** Renvoie la hauteur d'une case.
     * @return int hauteur d'une case du labyrinthe.
     */
    public int getHauteurcase(){
        return hauteur/nbcases_y;
    }

    /** Renvoi  l'affichage pour les scores.
     * @return Affichage affichage des scores.
     */
    public AffichageScores getAffichage(){
        return affichage_scores;
    }

    /** Dessine notre labyrinthe avec les joueurs et les decors.
     * @param g graphique.
     */
    public void paint(Graphics g){
        
        // initialisation de la longueur de la fenetre 
        longueur = getSize().width;
        // initialisation de la hauteur de la fenetre
        hauteur = getSize().height; 

	
        // on dessine le labyrinthe dans la fenetre
	laby.dessineToi(dbGC,nbcases_x, nbcases_y);
      
        /*
          si la fille n'a pas le gant et
          si elle n'a pas donne une gifle au garcon
          le gant doit etre affiche, sinon ca veut dire
          que la fille a le gant ou bien qu'elle s'en 
          ait servi donc il ne doit plus s'afficher dans le
          labyrinthe
        */ 
	
	
	
	
	if (!jeu_termine) { // si le jeu n'est pas termine
            
            // on dessine la fille	    
            fille.dessineToi(dbGC,false);
              
	
	}
	if (getSortie()){
            if (!jeu_termine)
                // elle gagne un jeu de plus
                fille.setNb_jeux_gagnes(fille.getNb_jeux_gagnes()+1);
	 	
	   	
		
            fille.effaceToi(dbGC);	
            
            try { 
		fille.getThread().sleep(200); // on endort pour voir le garcon pleurer
            }
            catch(InterruptedException e) {
                e.getMessage();
            }
            tuer_threads();
	
            // la fille est sortie
            sortie = true; 

	  	if (!jeu_termine)
			try {
				traitement_fin_de_jeu();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
                
        }
	
	
	g.drawImage(db, 0, 0, this);
	afficher(); // on affiche les scores
			   
    }// fin de paint()

    /** Surcharge le update, car le update efface tout et l'affichage clignote.
     * @param g graphique
     */
    public void update(Graphics g){
     paint(g);
  }




    public void keyTyped (KeyEvent keyevent){}

    /** On definit les evenements sur les touches claviers. 
     * On definit les evenements qui se produisent quand les joueurs
     * pressent les touches clavier.
     * @param keyevent evenement associe a la touche pressee.
     */ 
     public void keyPressed (KeyEvent keyevent){

  // on fait un vecteur contenant un garcon et une fille
  Vector joueurs = new Vector();
  joueurs.addElement(fille);
 
	int car = keyevent.getKeyCode();

        /* si le garcon est a l'envers,
           des au'on appui sur une touche,
           il se remet a l'endroit
        */
	

        if (car==KeyEvent.VK_ESCAPE){ // on quitte le jeu
            System.out.println("Au revoir !");
	    tuer_threads(); // on tu les threads
            System.exit(0); // on ferme la fenetre
        }
        else {
	  for(int i = 0 ; i < joueurs.size() ; i++){
	    
              // on verifie a chaque fois qu'il n'y a pas de mur
            if(car == (((Joueur)joueurs.elementAt(i)).getGauche()) &&(!limites(((Joueur)joueurs.elementAt(i)).getX() - 1)&&
	       (!choc_mur(((Joueur)joueurs.elementAt(i)).getX() - 1 ,((Joueur)joueurs.elementAt(i)).getY())))){
                ((Joueur)joueurs.elementAt(i)).setDirection(0);
              // le joueur avance
	      ((Joueur)joueurs.elementAt(i)).setAvance(true); 
	      
	       return;
            }
            else
	      if (car == (((Joueur)joueurs.elementAt(i)).getDroite())&&(!limites(((Joueur)joueurs.elementAt(i)).getX() + 1)&&
		  (!choc_mur(((Joueur)joueurs.elementAt(i)).getX() + 1 ,((Joueur)joueurs.elementAt(i)).getY())))){
                  ((Joueur)joueurs.elementAt(i)).setDirection(1);
		 ((Joueur)joueurs.elementAt(i)).setAvance(true);
		 
		return;
	      }
	      else
		if (car == (((Joueur)joueurs.elementAt(i)).getHaut())&&(!limites(((Joueur)joueurs.elementAt(i)).getY() - 1)&&
	       (!choc_mur(((Joueur)joueurs.elementAt(i)).getX() ,((Joueur)joueurs.elementAt(i)).getY() - 1 )))){
                     ((Joueur)joueurs.elementAt(i)).setDirection(2);  
		 ((Joueur)joueurs.elementAt(i)).setAvance(true);
		  return;
		}
		else
		  if (car == (((Joueur)joueurs.elementAt(i)).getBas())&&(!limites(((Joueur)joueurs.elementAt(i)).getY() + 1)&&
	       (!choc_mur(((Joueur)joueurs.elementAt(i)).getX()  ,((Joueur)joueurs.elementAt(i)).getY() + 1 )))){
                      ((Joueur)joueurs.elementAt(i)).setDirection(3);
		   ((Joueur)joueurs.elementAt(i)).setAvance(true);
		      return;
		  }
		
		    
	  }
	}
 
     }

     public void keyReleased(KeyEvent keyevent){}
    


  





   
     
	
    
    /** Indique si la fille est sortie.
     * @return <code> true : </code> la fille est sortie <BR> <code> false : </code> la fille n'est pas sortie
     */ 
    public boolean getSortie() {
        return sortie;
    }

    /** Indique si il y a un mur selon les coordonnees x et y.
     * @param x coordonnee en x
     * @param y coordonnee en y
     * @return <code> true : </code> il y a un mur(buisson) dans cette case <BR> <code> false : </code> il n'y a pas de mur(buisson) dans
     * cette case
     */
    
    public boolean choc_mur(int x,int y){
        return laby.mur(x,y);
    }//fin de joueur_choc_mur()
    
    
    public boolean limites(int x) {
        if ((x==laby.getDX()+1) || (x==-1))
            {
      
                return true;
            }
        else {
            
            return false;}
    }
    
    /** La fille gagne le jeu : on tue les threads et on elle gagne une
     * jeu de plus.
     */
    public void fille_est_sortie(){
        sortie = true;
    }
  

    /** Indique si la fille est sur la case de la sortie.
     * @return <code> true : </code> la fille est sur la case de la sortie <BR> <code> false : </code> la fille n'est pas sur la case de la sortie
     */ 
    public boolean fille_sur_sortie(){
        // elle est sortie si elle est sur la case de la sortie
        Cell coor_sortie = laby.getPosition_sortie();
        return ((fille.getX() == coor_sortie.getX()) && 
                (fille.getY() == coor_sortie.getY()));
    }
    
    public int fille_positionX(){
        // elle est sortie si elle est sur la case de la sortie
    	int pos;
    	pos = fille.getX();
		return pos + 1;
    }
    
    public int fille_positionY(){
        // elle est sortie si elle est sur la case de la sortie
    	int pos;
    	pos = fille.getY();
		return pos + 1;
    }
    
   
    public String typo_terrain(){
		return laby.type_terrain(fille.getX(),fille.getY());
 }

    /** Indique si la fille a gagne.
     * @return <code> true : </code>la fille a gagne <BR> <code> false : </code>la fille n'a pas gagne
     */
    public boolean fille_gagne(){
        // la fille gagne si elle est sortie
        return getSortie();
    }
    
    /** Traite la fin du jeu.
     * On incremente le nombre de jeux gagnes au gagnant de ce jeu
     * @throws IOException 
     */
    public void traitement_fin_de_jeu() throws IOException{
        
        if (jeu_fini()){ // si le jeu est finie
            jeu_termine = true;
            if (partie_finie()){
		tuer_threads();
                // si la partie est finie
                // le menu des parties devient selectionnable
                fenetre.selectionner_menu_parties_et_personnaliser(true);
                // ouvrir une fenetre selon qui gagne
                if (fille.getNb_jeux_gagnes() > 0){
                   
                    new Message("La fille a gagne");
                   
                }
            }
            else { // si la partie n'est pas finie
                AddCompteur_de_jeu(); // on incremente le compteur de jeu
                nouveau_jeu(); // on fait un nouveau jeu
                
            }
        }
	
    }


    /** Indique si le jeu est fini.
     * Le jeu est fini car le garcon a fait le bisou a la fille
     * ou bien parce que la fille a atteint la sortie.
     * @return <code> true : </code>le jeu est fini <BR> <code> false : </code>le jeu n'est pas fini
     */
  public boolean jeu_fini(){
    return (fille_gagne());
  }
  

  

    /** Indique si la partie est finie.
     * La partie est finie si le jeu courant est le dernier jeu de la
     * partie et si le jeu courant est fini.
     * @return <code> true : </code> la partie est finie <BR> <code> false : </code> la partie n'est pas finie
     */
    public boolean partie_finie(){
      return ((getNb_jeux() == getCompteur_jeu()) && jeu_fini());
  }
 
 
   
  


    /** Modifie le theme du labyrinthe : montagne, traditionnel, mer ou "brique".
     * On initialise les chemins des images qui sont utiles pour definir les themes
     * @param chemin_images chemin des images utiles pour construire le theme
     * @param couleur_chemin couleur du chemin du labyrinthe
     */
    public void modifier_look_labyrinthe(String chemin_images, Color couleur_chemin){
	laby.setChemin_image(chemin_images ,couleur_chemin);
	fille.setChemin_image(chemin_images);
	
}


    /** On tue les threads qui sont actifs.
     */
  public void tuer_threads(){
      // les threads des joueurs
    if (fille.getThread().isAlive())
      fille.getThread().stop();
    // et le thread qui dessine tout
    if (thread_interaction.isAlive())
        thread_interaction.stop();

      
  }

  
    /** Desactiver les threads : interaction et joueurs */
    public void desactiver_threads() {
        // les threads des joueurs
        if (fille.getThread().isAlive())
            fille.getThread().suspend();
        // et le thread qui dessine tout
        if (thread_interaction.isAlive())
            thread_interaction.suspend();
      
    }

    /** Activer les threads : interaction et joueurs */
    public void activer_threads() {
        // les threads des joueurs
        if (fille.getThread().isAlive())
            fille.getThread().resume();
        // et le thread qui dessine tout
        if (thread_interaction.isAlive())
            thread_interaction.resume();
	
    }


    /** Renvoie  le thread "interaction".
     * @return Thread le thread qui dessine tout.
     */
    public Thread getThread() {
        return thread_interaction;
    }

    /** Est  ce que les 2 elements passes en parametre sont sur la meme case.
     * @param element1 un premier element
     * @param element2 un deuxieme element
     * @return <code> true </code> Les 2 elements sont sur la meme case <BR> <code> false </code> les 2 elements ne sont pas sur la meme case.
     */
    public boolean meme_case(Cell position1, Cell position2){
	return ( (position1.getX() == position2.getX())
  		 && (position1.getY() == position2.getY()));
}

    /** le garcon est envoye n'importe ou dans le labyrinthe sauf dans une case deja occupee.*/ 
    public void garcon_valse(){
        // le garcon valse...
        // on fait en sorte qu'il n'arrive pas dans une case occupee
        // on cree un vecteur ou on va stocker toutes les cases
        // occupeees par les joueurs ou decors
        
    Vector v = new Vector(); 
    v.addElement(fille.getPosition());
    v.addElement(laby.getPosition_sortie());
    
    // on efface le contenu de l'encien vecteur cree lors du lancement
    // du jeu : il contient les positions initiales des joueurs
    laby.getPosition_elements_du_jeu().removeAllElements();
    
    // on initialise le vecteur des cases occupees
    laby.setPosition_elements_du_jeu(v);
    
    // on modifie sa position et son "etat" (il est a l'envers)
}

}// fin classe

