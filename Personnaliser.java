
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import fenetres.Message;

/** Une fenetre s'affiche demandant au joueur de rentrer les nouvelles 
 * dimensions qu'il veut affecter au labyrinthe.
 * @author Cecile FRANCOU
 * @author Sophia GALLARDO
 * @version 1.1.6
 * @see Interaction 
 */


public class Personnaliser extends Panel implements ActionListener{



    /** Fenetre pour pouvoir personnaliser les dimensions du labyrinthe*/
    private Frame fenetre;

    /** Longueur du labyrinthe en nombre de cases */
    private int nb_cases_x;

    /** Hauteur du labyrinthe en nombre de cases */
    private int nb_cases_y;

    /** Champs pour ecrire la longueur du labyrinthe desiree en nombre de cases*/
    private TextField longueur;
    

    /** Champs pour ecrire la hauteur du labyrinthe desiree en nombre de cases*/
    private TextField hauteur;
    
    /** Interaction entre le labyrinthe et les joueurs */
    private Interaction interaction;

    
    /**
     * @param interaction interaction entre les joueurs et le labyrinthe.
     */
    public Personnaliser(Interaction interaction) {
        //requestFocus();
        this.interaction = interaction;
        
        fenetre = new Frame("Dimensions du labyrinthe"); // titre de la fenetre
        fenetre.setLayout(new GridLayout(3,2,0,10)); 
        fenetre.setBackground(Color.lightGray); // couleur de l'arriere plan
        fenetre.setSize(100,100); // taille de la fenetre
        fenetre.setLocation(150,150); // localisation de la fenetre

        setDesign(); // on affiche les composants de la fenetre
        
        fenetre.pack();
        fenetre.setVisible(true); // fenetre visible
        
    }// fin constructeur


    /** On affiche les composants de la fenetre.
     * Ces composants sont : les boutons, les zones de textes et les labels.
     */
    public void setDesign(){

        // pour la longueur du labyrinthe
        fenetre.add(new Label("Longueur:"));
        longueur = new TextField("10",2);
        fenetre.add(longueur);
        
        // pour la hauteur du labyrinthe
        fenetre.add(new Label("Hauteur:"));
        hauteur = new TextField("10",2);
        fenetre.add(hauteur);
        
        // Panel pour afficher les boutons
        Panel panel = new Panel();
        
        Button Ok_Button = new Button("Ok");
        Button Cancel_Button = new Button("Cancel");

        Ok_Button.addActionListener(this);
        Cancel_Button.addActionListener(this);
 
        panel.add(Ok_Button); 
        panel.add(Cancel_Button);
 
        fenetre.add(panel);
        
    }
    //fin de setDesign()


    /** Gere les evenements boutons.
     * Definit les evenements declenches lies actions sur les boutons.
     * @param e evenement.
     */
    public void actionPerformed(ActionEvent e){

        if (e.getSource() instanceof Button){
            Button theSource = (Button)e.getSource();
            if (theSource.getLabel().equals("Ok")){
                if (interaction != null){
	
                    String chaine_x = longueur.getText();
                    String chaine_y = hauteur.getText();
                    if ((chaine_x != null) && (chaine_y != null)
                        && (isNumber(chaine_x)) && (isNumber(chaine_y))){
		
                        int x =  (new Integer(chaine_x)).intValue();
                        int y = (new Integer(chaine_y)).intValue();
                        if (!isCorrectData(x,y)){
                            
                            new Message("Les chiffres entres sont incorrect "+
                                        " 4 < longueur < 50 et 4 < hauteur < 50");
			
                        }
                        else {	
                            //interaction.activer_threads();
                            try {
								interaction.demarrer(x,y,0);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
                            try {
								interaction.nouveau_jeu();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
                            fenetre.setVisible(false);
        	   
                        }
                    }
                    else {
		
                        new Message("Veuillez rentrer des chiffres!");
                    }
                }
            }
            else if (theSource.getLabel().equals("Cancel")){
                fenetre.setVisible(false);
                if (interaction != null)
                    interaction.activer_threads();
            }

        }
    }//fin actionPerformed


    /** Verifie l'int�grit� des dimensions entrees.
     *  4 cases < longueur < 30 cases
     *  4 cases < hauteur < 30 cases
     * @param x longueur du labyrinthe (en nombre de cases)
     * @param y hauteur du labyrinthe (en nombre de cases)
     * @return <code> true </code> les dimensions sont correctes <BR> <code> false </code> les dimensions sont incorrectes
     */
    public boolean isCorrectData(int x, int y){
        return (( x > 4) && ( x < 30)
                && ( y > 4) && ( y < 30));
    }		
	
    /** Indique si la chaine passee en parametre contient que des chiffres.
     * Lorsque l'utilisateurs determine ses preferences de longueur et 
     * d'hauteur du labyrinthe, on recupere ces informations sous forme de
     * String, il faut donc parcourir cette String caractere par caractere 
     * pour verifier que c'est une suite de chiffres et qu'ell ne comprend
     * aucune lettre et aucun caractere special.
     * @param chaine chaine suite de chiffres.
     * @return <code> true </code> la chaine est un nombre <BR> <code> false </code> la chaine n'est pas un nombre
     */
    public boolean isNumber(String chaine){
        for (int i = 0 ; i < chaine.length() ; i++){
            if (!Character.isDigit(chaine.charAt(i)))
                return false;
        }
        return true;
    }
}
// fin classe Personnaliser 



