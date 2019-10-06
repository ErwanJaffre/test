
import java.io.*;
import java.awt.*;

import objets.Cell;
import objets.TouchesClavier;

/** Cree un nouveau joueur
 * @author Cecile FRANCOU
 * @author Sophia GALLARDO
 * @version 1.1.6 
 */

/** Cette classe cree un joueur.
 */

public class Joueur extends Personnage implements Runnable{
    
   
    /** Indique si le joueur avance */
    private boolean avance;

    /** le thread du joueur */
    private Thread thread;

    /** le nom du joueur pour plus tard... */
    private String nom;

    /** Touches claviers pour les deplacements du joueur et actionner
     *  la gifle sur la fille
     */
    private TouchesClavier touches;

    /** direction du joueur : droite, gauche, haut et bas */
    private int direction;

    
    /** nombre de jeux gagnes dans une partie par le joueur */
    private int jeux_gagnes;

    /** Genere un joueur avec une position dans le labyrinthe et des
     * touches clavier pour son deplacement 
     * @param touches_clavier touches du clavier du joueur
     * @param position_origine position d'origine du joueur
     * @param interaction interaction entre les joueurs et le labyrinthe.
     */
    public Joueur(TouchesClavier touches_clavier,Cell position_origine, Interaction interaction){
        super(position_origine,interaction);
        initNb_jeux_gagnes();
	chemin_images = "images" + File.separatorChar+"traditionnel"+File.separatorChar;
        this.touches = touches_clavier;
        
        // on initialise le thread
        thread = new Thread(this);
	direction = 7; // verifier si necessaire...
        
        // le joueur n'avance pas
	avance = false;
        
        // aucun jeu n'est encore gagne
	jeux_gagnes = 0;
    }

  



    /** Retourne le thread du joueur
     * @return Thread thread du joueur.
     */
    public Thread getThread(){
        return thread;
    }

  

    /** Initialise le nombre de jeux gagnes a 0.
     */
    public void initNb_jeux_gagnes(){
        jeux_gagnes = 0;
    } 

    /** Retourne le nombre de jeux gagnes.
     * @return int nombre de jeux gagnes par le joueur.
     */
    public int getNb_jeux_gagnes(){
        return jeux_gagnes;
    }

    /** Modifie le nombre de jeux gagnes du joueur.
     * @param jeux_gagnes jeux gagnes par le joueur.
     */
    public void setNb_jeux_gagnes(int jeux_gagnes){
        this.jeux_gagnes = jeux_gagnes;
    }
    
  
    /** Retourne le boolean pour savoir si le joueur avance.
     * @return <code> true : </code> le joueur avance <BR> <code> false : </code> le joueur n'avance pas
     */
    public boolean getAvance(){
        return avance;
    }

    /** On demarre le thread du joueur.
     */
    public void demarrer(){
        thread.start();
    }    
    
    /** Methode qui s'execute continuellement uniquement si le thread du
     * joueur est actif.
     * On endort le thread du joueur toutes les 100ms.
     */
    public void run(){
        while (!interaction.partie_finie()){ // tant que la partie n'est pas finit
            if (avance){ // si joueur avance
		
                garcon_et_fille_courent(direction); // on fait avancer le joueur
                
               
                
                if (interaction.fille_sur_sortie()) // si la fille est sur la case de la sortie
		    interaction.fille_est_sortie(); // on la fait sortir et le jeu est fini
                
                
            }
            try{
                Thread.sleep(100); // on "endort" le joueur pendant 100ms
            }
            catch(Exception e) {
                e.printStackTrace();
            }	
        }
    }
    
    
    /** Calcul le cout du mouvement en focntion du terrain
     * 
     */
    public void movement_cost() {
    	//REGARDER LE TYPE DE CASE 
    }
    
    /** Fait avancer le joueur d'une case soit a droite, a gauche, en haut
     * ou en bas selon la touche clavier qu'il a presse.
     * @param direction direction : droite, gauche, haut et bas.
     */
    
    public static int compteur;
    public static String type;
    public void garcon_et_fille_courent(int direction){
        // regarder qu'il n'y a pas de mur
    	
  
    	
        switch (direction){
            /* on verifie qu'il n'y a pas de mur et si il n'y a pas de mur,
                   on modifie la position du joueur
            */
        case 0 : // on va a gauche
            if (!interaction.choc_mur(getX() - 1 , getY())){
            	
            	
                setPosition(new Cell(getX()-1,getY()));
                
            	//System.out.println(Labyrinthe.case_speciale( getX(),getY()));

            	//System.out.println("getX :"+(getX())+" getY :"+getY());

                if (interaction.typo_terrain() == "Mar") {
                	System.out.println("Cost 5");
                	compteur+=5;
                	System.out.println("compteur : "+compteur);
                	type="eau";
                }
                else {
                	System.out.println("Cost 1");
                	compteur+=1;
                	System.out.println("compteur : "+compteur);
                	type="sable";
                }
                
                
            } 
            break;
        case 1: // on va a droite
            if (!interaction.choc_mur(getX() + 1 , getY())){
            	

            	
                   setPosition(new Cell(getX()+1,getY())); 
               		//System.out.println("\n"+"\n"+Labyrinthe.case_speciale( getX(),getY()));

           			//System.out.println("getX :"+(getX())+" getY :"+getY());

                   if (interaction.typo_terrain() == "Mar") {
                   	System.out.println("Cost 5");
                   	compteur+=5;
                	System.out.println("compteur : "+compteur);
                	type="eau";
                   }
                   else {
                   	System.out.println("Cost 1");
                   	compteur+=1;
                	System.out.println("compteur : "+compteur);
                	type="sable";
                   }

            }
            break;
        case 2: // on monte
            if (!interaction.choc_mur(getX() , getY() - 1)){
            	

            	
                setPosition(new Cell(getX(),getY()-1));
            	//System.out.println("\n"+"\n"+Labyrinthe.case_speciale( getX(),getY()));

            	//System.out.println("getX :"+getX()+" getY :"+(getY()));
                if (interaction.typo_terrain() == "Mar") {
                	System.out.println("Cost 5");
                	compteur+=5;
                	System.out.println("compteur : "+compteur);
                	type="eau";
                }
                else {
                	System.out.println("Cost 1");
                	compteur+=1;
                	System.out.println("compteur : "+compteur);
                	type="sable";
                }

            }
            break;
        case 3: // on descend
            if (!interaction.choc_mur(getX() , getY() + 1)){ 
            	

                setPosition(new Cell(getX(), getY()+1)); 
            	//System.out.println("\n"+"\n"+Labyrinthe.case_speciale( getX(),getY()));

            	//System.out.println("getX :"+getX()+" getY :"+(getY()));

                
                if (interaction.typo_terrain() == "Mar") {
                	System.out.println("Cost 5");
                	compteur+=5;
                	System.out.println("compteur : "+compteur);
                	type="eau";
                }
                else {
                	System.out.println("Cost 1");
                	compteur+=1;
                	System.out.println("compteur : "+compteur);
                	type="sable";
                }

            }
            break;
        }// fin du switch
        avance = false; // le joueur n'avance plus
        
    }
 
    

    /** Modifie le bouleen qui indique si le joueur avance ou pas.
     * @param flag le joueur avance
     */
    public void setAvance(boolean le_joueur_avance){
        avance = le_joueur_avance;
    }
    
    
    
    /** Change la direction du joueur dans le labyrinthe*/
    public void setDirection(int direction){
            this.direction=direction;
    }

        

    /**  Retourne l'entier qui indique la direction "gauche".
     * @return <code> 0 </code> direction gauche.
     */
    public int getGauche(){
	return touches.getGauche();
    }
    
    /** Retourne l'entier qui indique la direction "droite".
     * @return  <code> 1 </code>droite;
     */
    public int getDroite(){
	return touches.getDroite();
    }

    /** Retourne l'entier qui indique la direction "bas".
     * @return <code> 2 </code> haut.
     */
    public int getHaut(){
	return touches.getHaut();
    }

    /** Retourne l'entier qui indique la direction "haut".
     * @return <code> 3 </code> bas.
     */
    public int getBas(){
	return touches.getBas();
    }

    /** Retourne l'entier qui indique qu'il y a eu un bisou ou une claque.
     * @return <code>  </code> bisou.
     */
    public int getAction(){
	return touches.getAction();
    }
 
    /** Efface le joueur du labyrinthe.
     * @param g graphique.
     */
    public void effaceToi(Graphics g){
        
	Toolkit toolkit_chemin = interaction.getToolkit();
        Image image_chemin = toolkit_chemin.getImage(chemin_images+"chemin.gif");
        
        // on affiche l'image "vide"
        g.drawImage(image_chemin,getX()*interaction.getLongueurcase(),getY()*interaction.getHauteurcase(),interaction.getLongueurcase(),interaction.getHauteurcase(),interaction); 
  
               
  }
 
}// fin classe

