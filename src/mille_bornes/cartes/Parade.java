package mille_bornes.cartes;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;

public abstract class Parade extends Bataille{

    public Parade(String nom) {
        super(nom, Categorie.Parade);
    }

    public void appliqueEffet(Jeu jeu, EtatJoueur joueur) throw IllegalStateException;
}
