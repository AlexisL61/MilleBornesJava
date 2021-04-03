package mille_bornes.cartes;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;

public abstract class Bataille extends Carte {

    public Bataille(String nom, Categorie categorie) {
        super(nom, categorie);
    }

    public boolean estContreeParFeuVert(){
        return false;
    };

    public boolean estContreeParFinDeLimite(){
        return false;
    };

    public boolean estContreeParEssence(){
        return false;
    };

    public boolean estContreeParRoueDeSecours(){
        return false;
    };

    public boolean estContreeParReparations(){
        return false;
    };

    public boolean estContreeParVehiculePrioritaire(){
        return false;
    };

    public boolean estContreeParCiterne(){
        return false;
    };

    public boolean estContreeParIncrevable(){
        return false;
    };

    public boolean estContreeParAsDuVolant(){
        return false;
    };

    public abstract boolean contre(Attaque carte);

    @Override
    public void appliqueEffet(Jeu jeu, EtatJoueur joueur) throws IllegalStateException;
}
