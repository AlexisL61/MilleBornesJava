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
        if (this instanceof Borne)
            return "\u001B[34m"+ nom+"\u001B[0m";
        if (this instanceof Attaque)
            return "\u001B[31m"+ nom+"\u001B[0m";
        if (this instanceof Parade)
            return "\u001B[32m"+ nom+"\u001B[0m";
        if (this instanceof Botte)
            return "\u001B[35m"+ nom+"\u001B[0m";
        return nom;
    }
}