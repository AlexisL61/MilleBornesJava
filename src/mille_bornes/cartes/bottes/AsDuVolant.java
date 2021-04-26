package mille_bornes.cartes.bottes;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;
import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Categorie;

public class AsDuVolant extends Botte{

    public AsDuVolant() {
        super("AsDuVolant",Categorie.Botte);
    }

    public boolean contre(Attaque carte){
        return carte.nom.equals("Accident");
    }

    public void appliqueEffet(Jeu jeu, EtatJoueur joueur){
        joueur.addBotte(this);
        // To-do ajout transfère le tour de jeu
    }
}
