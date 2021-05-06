package mille_bornes.cartes;

import mille_bornes.EtatJoueur;
import mille_bornes.Jeu;
import mille_bornes.cartes.parades.*;
import mille_bornes.cartes.attaques.*;
import mille_bornes.cartes.bottes.*;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Carte {

    public final String nom;
    public final Categorie categorie;

    public Carte(String nom, Categorie categorie) {
        this.nom = nom;
        this.categorie = categorie;
    }

    public abstract void appliqueEffet(Jeu jeu, EtatJoueur joueur) throws IllegalArgumentException;

    @Override
    public String toString() {
        if (this instanceof Borne)
            return "\u001B[34m" + nom + "\u001B[0m";
        if (this instanceof Attaque)
            return "\u001B[31m" + nom + "\u001B[0m";
        if (this instanceof Parade)
            return "\u001B[32m" + nom + "\u001B[0m";
        if (this instanceof Botte)
            return "\u001B[35m" + nom + "\u001B[0m";
        return nom;
    }

    public static Carte newCarte(JSONObject json) {
        String nomCarte = (String) json.get("nom");

        if (nomCarte.equals("Borne")) return new Borne((Integer) json.get("km"));

        if (nomCarte.equals("Accident")) return new Accident();
        if (nomCarte.equals("Crevaison")) return new Crevaison();
        if (nomCarte.equals("FeuRouge")) return new FeuRouge();
        if (nomCarte.equals("LimiteVitesse")) return new LimiteVitesse();
        if (nomCarte.equals("PanneEssence")) return new PanneEssence();

        if (nomCarte.equals("Essence")) return new Essence();
        if (nomCarte.equals("FeuVert")) return new FeuVert();
        if (nomCarte.equals("FinDeLimite")) return new FinDeLimite();
        if (nomCarte.equals("Reparations")) return new Reparations();
        if (nomCarte.equals("RoueDeSecours")) return new RoueDeSecours();

        if (nomCarte.equals("AsDuVolant")) return new AsDuVolant();
        if (nomCarte.equals("Citerne")) return new Citerne();
        if (nomCarte.equals("Increvable")) return new Increvable();
        if (nomCarte.equals("VehiculePrioritaire")) return new VehiculePrioritaire();
        System.err.println("ERRRRRRRRRRRRRRRRRRRRRRRRRRREUR !!!!!!! "+nomCarte);
        return null;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("nom", nom);
        json.put("categorie", categorie.toString());
        return json;
    }
}