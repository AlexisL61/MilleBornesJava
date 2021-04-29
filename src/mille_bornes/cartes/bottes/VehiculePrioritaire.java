package mille_bornes.cartes.bottes;

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

}
