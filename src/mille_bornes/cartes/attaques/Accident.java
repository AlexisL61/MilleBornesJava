package mille_bornes.cartes.attaques;

import mille_bornes.cartes.Attaque;

public class Accident extends Attaque {

    public Accident() {
        super("Accident");
    }

    @Override
    public boolean estContreeParAsDuVolant(){
        return true;
    }

    @Override
    public boolean estContreeParReparations(){
        return true;
    }
}
