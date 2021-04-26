package mille_bornes.cartes;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;

public class Borne extends Carte{

    public final int km;



    public Borne(int km) {
        super("Borne", Categorie.Borne);
        this.km = km;
    }

    @Override
    public void appliqueEffet(Jeu jeu, EtatJoueur joueur) throws IllegalArgumentException;
}
