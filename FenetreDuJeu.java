

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.File;
import java.io.IOException;

import fenetres.Message;
import scores.AffichageScores;


/** Classe qui permet d'afficher la fenetre avec le menu, le labyrinthe,
 * et les  scores.
 * Fentre ou se deroule le jeu.
 * @author Cecile FRANCOU
 * @author Sophia GALLARDO
 * @version 1.1.6 
 * @see Interaction
 */

public class FenetreDuJeu extends Frame implements ActionListener, Observer{


    

    /** sous menus de "jeu" : "Jouer" */
    private MenuItem menu_jouer;

    /** sous menu de "jeu" : "Quitter" */
    private MenuItem menu_quitter;


    /** sous menus : "parties" */
    private Menu menu_parties;
    
    private Menu terrain;

    /** sous menus de "parties" : "1 jeu" */
    private MenuItem menu_1_jeu;

    /** sous menu de "parties" : "2 jeux" */
    private MenuItem menu_3_jeu;

    /** sous menu de "parties" " " 3 jeux " */
    private MenuItem menu_5_jeu;


    /** sous menu de "Labyrinthe" : " Personnaliser Taille" */
    private MenuItem menu_personnaliser_taille;	

    /** sous menu de "Decors..." (qui est un sous menu de "Labyrinthe") : "Mer" */	
    private MenuItem menu_decor_mer;
    
    /** sous menu de "Decors..." (qui est un sous menu de "Labyrinthe") : "Traditionnel" */	
    private MenuItem menu_decor_traditionnel;
    
    /** sous menu de "Decors..." (qui est un sous menu de "Labyrinthe") : "Montagne" */	
    private MenuItem menu_decor_montagne;	
    
    /** sous menu de "Decors..." (qui est un sous menu de "Labyrinthe") : "Brique" */	
    private MenuItem menu_decor_brique;
    
    /** sous menu de "Labyrinthe" : "Decor"*/
    private MenuItem menu_decor;
    
    /** sous menu de "Aide" : "Aide" */
    private MenuItem menu_aide;

    /** sous menu de "Aide" : "A propos" */
    private MenuItem menu_apropos;
    
    
    /** interaction entre le labyrinthe, les joueurs et les decors */
    private Interaction interaction;
    
    /** texte en haut de la fenetre */
    private Label haut;


    /** texte en bas de la fenetre */
    private Label bas;
 

    /** nombre de jeux dans une partie */
    private int nb_jeux;

    /** l'image de l'accueil */
    Image image_accueil;

    /** toolkit pour l'image de l'accueil */
    Toolkit toolkit_accueil;

 

    /** Construit la fenetre ou va se derouler tout le jeu. 
     * Elle est composee : des menus, des scores et du labyrinthe avec 
     * ses joueurs.
     */
    public FenetreDuJeu(){
        nb_jeux = 1; // on initialise a 1 le nombre de jeux
        setTitle("LABYRINTHE"); // titre de la fenetre
        setSize(400,400); // taille de la fenetre
        setBounds(10,10,300,300);
        setLayout(new BorderLayout());
        construire_menu(); // on affiche le menu
        indiquerJeuSelectionne(); // on affiche "*" a cote de "1 jeu"
	
        pack();
        setVisible(true);

        // initialisation des labels
        haut = new Label();
	bas = new Label();
    
       
        setVisible(true);// la fenetre est visible

       
    }

    /**  Dessine l'image d'accueil.
     * @param g graphique
     */
    public void paint(Graphics g){
      toolkit_accueil = this.getToolkit();
      image_accueil = toolkit_accueil.getImage("images"+File.separatorChar+"accueil.gif");
      g.drawImage(image_accueil,0,20,400,400,this);
  }
    
    private String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char)(i + 'A' - 1)) : null;
    }
    /**  Mise a jour des scores.
     * @param observable objet de type Observable
     * @param obj statistiques;
     */
    public void update(Observable observable, Object obj){
	
        int tableau_pour_afficher[] = (int[])obj;
        
	
	haut.setFont(new Font("Helvetica",Font.BOLD,14));
	haut.setForeground(Color.white);
	haut.setBackground(Color.black);
	String s = "Jeux gagnes par la fille:"+String.valueOf(tableau_pour_afficher[1])+"    Type de terrain : "+ Joueur.type;
	haut.setText(s); // affiche les scores
	
		bas.setFont(new Font("Helvetica",Font.BOLD,14));
	bas.setForeground(Color.white);
	bas.setBackground(Color.black);
	String letters = getCharForNumber((tableau_pour_afficher[2]));
	bas.setText("Coordonnees Fille : " + letters + String.valueOf(tableau_pour_afficher[3]) + 
		    "	               Movements used : " + Joueur.compteur);   	
 }	

    /** Gere les evenements du menu.
     * @param e evenement.
     */
    public void actionPerformed(ActionEvent e){
        
        if (e.getSource() == menu_jouer){ // on veut jouer
           
            setSize(400,400);
            setLocation(100,100);
            //pas le droit de modifier le nombre de parties
            selectionner_menu_parties_et_personnaliser(false);
            if (interaction == null){
                interaction = new Interaction((FenetreDuJeu) this);
		add("North",haut);
                add("Center",interaction);
                add("South", bas);
		
                // on demarre le jeu
                try {
					interaction.demarrer(15,15,0);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                setVisible(true);
		
                
            }
            else { // si interaction n'est pas null          
                // on demarre un nouveau jeu
                try {
					interaction.nouveau_jeu();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                
            }
	    // des menus deviennent selectionnables            
            rendre_menu_selectionnable();
            // on initialise les compteurs
	    interaction.initJeu();	
            interaction.setNb_jeux(nb_jeux);
            // on ajoute "l'ecouteur"
            interaction.getAffichage().addObserver(this);
                
        }
        else if (e.getSource() == menu_quitter){ // on quitte le jeu
            if (interaction != null)
                interaction.tuer_threads(); // on tue les threads
            System.exit(0); // on ferme le jeu
        }
     
        else if (e.getSource() == menu_1_jeu){ // on choisit 1 partie d'1 jeu
            nb_jeux = 1;
	    indiquerJeuSelectionne(); // on indique le jeu selectionne 
	    new Message("Vous avez choisi 1 jeu.");
        }
        else if (e.getSource() == menu_3_jeu){ // on choisit 1 partie de 3 jeux
            new Message("Vous avez choisi 3 jeux.");
            nb_jeux = 3;
	    indiquerJeuSelectionne();	
        }
        else if (e.getSource() == menu_5_jeu){ // on chosit 1 partie de 5 jeux
            nb_jeux = 5;
	    indiquerJeuSelectionne();
            new Message("Vous avez choisi 5 jeux");
            //tableau_pour_afficher[3] = 5;
        }
	else if (e.getSource() == menu_personnaliser_taille){
            
            new Personnaliser(interaction);
	}

	// theme de la mer
	else if (e.getSource() == menu_decor_mer){
            if (interaction != null){
		interaction.modifier_look_labyrinthe("images"+File.separatorChar+"mer"+File.separatorChar,
                                                     new Color(4,5,135));
            }
	}

	// theme traditionnel
	else if (e.getSource() == menu_decor_traditionnel){
            if (interaction != null){
		interaction.modifier_look_labyrinthe("images"+File.separatorChar+"traditionnel"+File.separatorChar,
                                                     new Color(240,144,0));
            }
	}
        
	// theme de la montagne
	else if (e.getSource() == menu_decor_montagne){
            if (interaction != null){
		interaction.modifier_look_labyrinthe("images"+File.separatorChar+"montagne"+File.separatorChar,
                                                     new Color(80,48,0));
            }
	}
        
	// theme "brique"
	else if (e.getSource() == menu_decor_brique){
            if (interaction != null){
		interaction.modifier_look_labyrinthe("images"+File.separatorChar+"briques"+File.separatorChar,
                                                     new Color(255,132,66));
            }
	}
        
    }
    
    
    
    /** Construit le menu du <code>Trap Trap Bisou</code>.
     */
    public void construire_menu(){
        
        // la barre de menu
        MenuBar menubar = new MenuBar();
        
        // menu "jouer"
        Menu menu_jeu = new Menu("Jeu");

        // menu "?"
        Menu menu_help = new Menu("?");

        // sous menu "jouer"
        menu_jouer = new MenuItem("Jouer");
        menu_jouer.addActionListener(this); // on ajoute l'actionListener

        // menu "parties"
        menu_parties = new Menu("Parties...");

        // sous menus des jeux
        menu_1_jeu = new MenuItem("1 jeu");
        menu_3_jeu = new MenuItem("3 jeux");
        menu_5_jeu = new MenuItem("5 jeux");
        // on ajoute les ecouteurs
        menu_1_jeu.addActionListener(this);
        menu_3_jeu.addActionListener(this);
        menu_5_jeu.addActionListener(this);
        
        // on ajoute les sous menus au menu
        menu_parties.add(menu_1_jeu);
        menu_parties.add(menu_3_jeu);
        menu_parties.add(menu_5_jeu);


	

        // menu quitter
        menu_quitter = new MenuItem("Quitter");
        menu_quitter.addActionListener(this);

        menu_jeu.add(menu_jouer);
        menu_jeu.add(menu_parties);
	    menu_jeu.addSeparator(); // on tire un trait avant le sous menu quitter
        menu_jeu.add(menu_quitter);
        


   	// menu Labyrinthe
	Menu menu_labyrinthe = new Menu("Labyrinthe");

	// pour personnaliser la taille du labyrinthe
	menu_personnaliser_taille = new MenuItem("Personnaliser taille");
	menu_personnaliser_taille.addActionListener(this);
	menu_personnaliser_taille.setEnabled(false);

	
	// pour personnaliser les themes du labyrinthe
	Menu menu_decors = new Menu("Decors");

	// theme de la mer
	menu_decor_mer = new MenuItem("Mer");
	menu_decor_mer.addActionListener(this);
	menu_decor_mer.setEnabled(false);

	// theme traditionnel
	menu_decor_traditionnel = new MenuItem("Traditionnel");
	menu_decor_traditionnel.addActionListener(this);
	menu_decor_traditionnel.setEnabled(false);

	// theme de la montagne
	menu_decor_montagne = new MenuItem("Montagne");
	menu_decor_montagne.addActionListener(this);
	menu_decor_montagne.setEnabled(false);

	// theme "brique"
	menu_decor_brique = new MenuItem("Brique");
	menu_decor_brique.addActionListener(this);
	menu_decor_brique.setEnabled(false);

	// on ajoute ces sous menus
	menu_decors.add(menu_decor_traditionnel);
	menu_decors.add(menu_decor_montagne);
	menu_decors.add(menu_decor_mer);
	menu_decors.add(menu_decor_brique);

	menu_labyrinthe.add(menu_personnaliser_taille);
	menu_labyrinthe.add(menu_decors);

	

	

        // menu Aide
        menu_aide = new MenuItem("Aide");
        menu_aide.addActionListener(this);

        // menu A propos
        menu_apropos = new MenuItem("A propos");
        menu_apropos.addActionListener(this);

        menu_help.add(menu_aide);
        menu_help.add(menu_apropos);

        menubar.add(menu_jeu);
	menubar.add(menu_labyrinthe);
        menubar.add(menu_help);

        setMenuBar(menubar);
  }
    
    /** Deselectionne le menu des parties et le menu "Personnaliser" pour que le joueur ne puisse pas 
     * cliquer dessus pendant qu'il joue
     * @param actif est ce qu'on selectionne le menu des parties?
     */
    public void selectionner_menu_parties_et_personnaliser(boolean actif){
        menu_parties.setEnabled(actif);
	menu_personnaliser_taille.setEnabled(actif);
    }
    
  /**  Indique dans le menu le nombre de jeux selectionnes pour une partie.
   * Ajoute une etoile dans le menu a cote du nombre de jeux choisis
   * pour une partie.
   */
    public void indiquerJeuSelectionne(){
        if (nb_jeux == 1){
            menu_1_jeu.setLabel("1 jeu *");
            menu_3_jeu.setLabel("3 jeux ");
            menu_5_jeu.setLabel("5 jeux ");
        }
        else if (nb_jeux == 3){
            menu_1_jeu.setLabel("1 jeu   ");	
            menu_3_jeu.setLabel("3 jeux *");
            menu_5_jeu.setLabel("5 jeux  ");
        }
        else if (nb_jeux == 5){
            menu_1_jeu.setLabel("1 jeu   ");	
            menu_3_jeu.setLabel("3 jeux  ");
            menu_5_jeu.setLabel("5 jeux *");
        }
        
        
    }

    /** Rendre menus selectionnables.
     * Ces menus sont selectionnables que quand un labyrinthe existe.
     */
    public void rendre_menu_selectionnable(){
        // on peut selectionner le menu qui nous permet de personnaliser le labyrinthe
        
        menu_decor_mer.setEnabled(true);
        menu_decor_montagne.setEnabled(true);
        menu_decor_traditionnel.setEnabled(true);
        menu_decor_brique.setEnabled(true);
    }


}// fin de FenetreDuJeu




