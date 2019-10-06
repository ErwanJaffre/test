
import java.awt.*;
import java.rmi.*;
import java.rmi.server.*;
import java.awt.event.*;

/** Demarre le jeu : ouvre une fenetre avec un menu
 * @author Cecile FRANCOU
 * @author Sophia GALLARDO
 * @version 1.1.6 
 * @see Interaction
 */
public class Start {

    /** Interaction entre les joueurs et le labyrinthe */
    private Interaction interaction;

    /** Fenetre qui s'affiche lorsqu'on demarre le jeu */
    private FenetreDuJeu f;


    public Start(){
  
        f = new FenetreDuJeu();
        f.setSize(425,450); // taille de la fenetre
        f.setLocation(400,400);// localisation de la fenetre dans l'ecran
        f.setVisible(true);// la fenetre est visible

        }

    public static void main (String args[]) {
        new Start(); 
    } //fin de main
    
} //fin classe Start




