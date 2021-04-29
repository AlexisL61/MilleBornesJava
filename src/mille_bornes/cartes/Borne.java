package mille_bornes.cartes;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;

public class Borne extends Carte {

    public final int km;

    public Borne(int km) {
        super("Borne", Categorie.Borne);
        this.km = km;
    }

    @Override
    public void appliqueEffet(Jeu jeu, EtatJoueur joueur) throws IllegalArgumentException {
        if (joueur.getBataille() instanceof Attaque) throw new IllegalArgumentException();
        if (joueur.getLimiteVitesse() && km > 50) throw new IllegalArgumentException();
        if (joueur.getKm() + km > 1000) throw new IllegalArgumentException();
        joueur.ajouteKm(km);
    }

    @Override
    public String toString(){
        return super.toString() + " " + km + "km";
    }
}
