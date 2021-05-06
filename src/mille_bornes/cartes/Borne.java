package mille_bornes.cartes;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;
import org.json.JSONObject;

public class Borne extends Carte {

    public final int km;

    public Borne(int km) {
        super("Borne", Categorie.Borne);
        this.km = km;
    }

    @Override
    public void appliqueEffet(Jeu jeu, EtatJoueur joueur) throws IllegalStateException {
        if (joueur.getBataille() instanceof Attaque){
            throw new IllegalStateException();
        }
        if (joueur.getLimiteVitesse() && km > 50) throw new IllegalStateException();
        if (joueur.getKm() + km > 1000) throw new IllegalStateException();
        joueur.ajouteKm(km);
    }

    @Override
    public String toString(){
        return super.toString() + " " + km + "km";
    }

    @Override
    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        json.put("nom", nom);
        json.put("categorie", categorie.toString());
        json.put("km", km);
        return json;
    }
}
