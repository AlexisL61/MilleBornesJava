package mille_bornes.cartes.attaques;

import mille_bornes.cartes.Attaque;
import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;

public class LimiteVitesse extends Attaque {

    public LimiteVitesse() {
        super("LimiteVitesse");
    }

    @Override
    public boolean estContreeParFinDeLimite(){
        return true;
    }

    @Override
    public boolean estContreeParVehiculePrioritaire(){
        return true;
    }

    @Override
    public void appliqueEffet(Jeu jeu, EtatJoueur joueur) throws IllegalStateException{
        if (!joueur.getLimiteVitesse()){
            joueur.setLimiteVitesse(true);
        }else{
            throw new IllegalStateException();
        }
    }
}
