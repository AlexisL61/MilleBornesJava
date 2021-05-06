package mille_bornes.cartes.bottes;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;
import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Botte;
import mille_bornes.cartes.Categorie;

public class VehiculePrioritaire extends Botte {

    public VehiculePrioritaire() {
        super("VehiculePrioritaire",Categorie.Botte);
    }

    @Override
    public boolean contre(Attaque carte) {
        return carte.nom.equals("FeuRouge") || carte.nom.equals("LimiteVitesse");
    }

    @Override
    public void appliqueEffet(Jeu jeu, EtatJoueur joueur) {
        super.appliqueEffet(jeu,joueur);
        joueur.setLimiteVitesse(false);
    }
}
