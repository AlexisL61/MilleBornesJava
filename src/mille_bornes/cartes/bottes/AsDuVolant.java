package mille_bornes.cartes.bottes;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;
import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Botte;
import mille_bornes.cartes.Categorie;

public class AsDuVolant extends Botte {

    public AsDuVolant() {
        super("AsDuVolant");
    }

    public boolean contre(Attaque carte){
        return carte.nom.equals("Accident");
    }

}
