package mille_bornes.cartes.attaques;

import mille_bornes.cartes.Attaque;

public class PanneEssence extends Attaque {

    public PanneEssence() {
        super("PanneEssence");
    }

    @Override
    public boolean estContreeParCiterne(){
        return true;
    }

    @Override
    public boolean estContreeParEssence(){
        return true;
    }
}
