package mille_bornes.cartes.parades;

import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Parade;

public class FinDeLimite extends Parade {

    public FinDeLimite() {
        super("FinDeLimite");
    }

    @Override
    public boolean contre(Attaque carte) {
        return carte.nom.equals("LimiteVitesse");
    }
}
