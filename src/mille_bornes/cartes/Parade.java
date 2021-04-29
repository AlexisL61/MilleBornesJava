package mille_bornes.cartes;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;

public abstract class Parade extends Bataille{

    public Parade(String nom) {
        super(nom, Categorie.Parade);
    }

    public void appliqueEffet(Jeu jeu, EtatJoueur joueur) throws IllegalStateException{
        if (joueur.getBataille() != null && contre((Attaque) joueur.getBataille())) {
            joueur.setBataille(this);
        }else{
            throw new IllegalStateException();
        }
    }
}
