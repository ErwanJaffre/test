

import java.awt.*;

import objets.*;
 
/** Classe qui cree un personnage dans le labyrinthe a une certaine
 * position dans le labyrinthe.
 * @author Cecile FRANCOU
 * @author Sophia GALLARDO
 * @version 1.1.6
 * @see Interaction
 */

public class Personnage {

    /** chemin des images */
    protected String chemin_images;
    
    /** Les coordonnees de depart du personnage dans le labyrinthe */
    protected Cell position_origine;
    
    /** position actuelle (courante) du personnage dans le labyrinthe */
    protected Cell position_courante;
    
    /** interaction entre les joueurs et le labyrinthe */
    protected Interaction interaction;
    
    /**
     * @param position_origine position d'origine du personnage
     * @param interaction interaction entre les personnages et le labyrinthe
     */ 
    public Personnage(Cell position_origine, Interaction interaction){
        // initialisation
        this.interaction = interaction;
        this.position_origine = position_origine;
        position_courante = position_origine;
    }
    
    
    /** Modifie le chemin des images.
     * @param chemin_images chemin des images
     */
    public void setChemin_image(String chemin_images){
        this.chemin_images = chemin_images;
    }
    
    /** Modifie la position courante du personnage.
     * @param position position du personnage.
     */
    public void setPosition(Cell position){
        position_courante = position;
    }
  

    /** Retourne la position courante du personnage.
     * @return Cell position du personnage.
     */
    public Cell getPosition(){
        return position_courante;
    }

    /** Retourne la position en x du personnage.
     * @return int position en x du personnage.
     */
    public int getX(){
        return position_courante.getX();
    }
    
    /** Retourne la position en y du personnage.
     * @return int position en y du personnage.
     */
    public int getY(){
        return position_courante.getY();
    }

} // fin de la classe Personnage
