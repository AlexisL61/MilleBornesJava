package mille_bornes.cartes.bottes;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;
import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Categorie;

public class Citerne extends Botte{

    public Citerne (){
        super("Citerne", Categorie.Botte);
    }

    @Override
    public boolean contre(Attaque carte) {
        return carte.nom.equals("PanneEssence");
    }

    @Override
    public void appliqueEffet(Jeu jeu, EtatJoueur joueur);
}
