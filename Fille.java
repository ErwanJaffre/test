
import java.io.*;
import java.awt.*;



import objets.*;

/** Classe qui cree une fille.
 * @author Cecile FRANCOU
 * @author Sophia GALLARDO
 * @version 1.1.6 
 * @see Interaction
 */

public class Fille extends Joueur{
    
    /** toolkit pour l'image de la fille */
    Toolkit toolkit_petite_fille;

    /** image de la fille */
    Image image_petite_fille;

    /** Interaction entre les joueurs, le labyrinthe et les decors */
    Interaction interaction;
    
   
 
    /** Genere une fille avec des touches de deplacement, une touche d'action
     * pour donner la gifle et une position dans le labyrinthe specifique. 
     * @param touches_clavier touches claviers de la fille.
     * @param interaction interaction entre la fille et le labyrinthe
     */
    public Fille(TouchesClavier touches_clavier,Cell positionOrigine, Interaction interaction){
        super(touches_clavier,positionOrigine,interaction);
        this.interaction = interaction;
 
  

  }

    /** Dessin de  la fille.
     * @param fille_rouge est ce que la fille est rouge ou pas
     * la fille est rouge si elle a recu un bisou du garcon.
     * @param g graphique.
     */
    public void dessineToi(Graphics g, boolean fille_rouge){
        String nom_image;
        toolkit_petite_fille = interaction.getToolkit();
        if (fille_rouge){ /*
                            si la fille est rouge (si le garcon lui a fait
                            le bisous) on affiche la fille qui rougit
                          */
            nom_image = chemin_images+"petitefillehonteuse.gif";
	}
        else 
            nom_image = chemin_images+"petitefille.gif";
        image_petite_fille = toolkit_petite_fille.getImage(nom_image);
        
        g.drawImage(image_petite_fille,getX()*interaction.getLongueurcase(),getY()*interaction.getHauteurcase(),interaction.getLongueurcase(),interaction.getHauteurcase(),interaction); 
        
    } 

}// fin classe






