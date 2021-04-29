package mille_bornes.cartes;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;
import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Carte;
import mille_bornes.cartes.Categorie;

public abstract class Botte extends Carte {

    public Botte(String nom, Categorie categorie) {
        super(nom, categorie);
    }

    public abstract boolean contre(Attaque carte);

    public void appliqueEffet(Jeu jeu, EtatJoueur joueur) {
        if (joueur.getBataille() != null && contre((Attaque) joueur.getBataille())){
            joueur.defausseBataille(jeu);
        }
        joueur.addBotte(this);
    }
}
