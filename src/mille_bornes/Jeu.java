package mille_bornes;

import mille_bornes.cartes.Carte;

public class Jeu {

    private TasDeCartes defausse;
    public Jeu() {

    }

    public void defausse(Carte carte){
        defausse.pose(carte);
    }
}
