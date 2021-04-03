package mille_bornes.cartes;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;

public abstract class Carte {

    public final String nom;
    public final Categorie categorie;

    public Carte(String nom, Categorie categorie) {
        this.nom = nom;
        this.categorie = categorie;
    }

    public abstract void appliqueEffet(Jeu jeu, EtatJoueur joueur) throws IllegalArgumentException;

    @Override
    public String toString() {
        return nom;
    }
}