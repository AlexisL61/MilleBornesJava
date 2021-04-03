package mille_bornes.cartes.parades;

import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Parade;

public class Reparations extends Parade {

    public Reparations() {
        super("Reparations");
    }

    @Override
    public boolean contre(Attaque carte) {
        return carte.nom.equals("Accident");
    }
}
