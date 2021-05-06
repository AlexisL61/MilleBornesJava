package mille_bornes;

import mille_bornes.cartes.Borne;
import mille_bornes.cartes.Carte;
import mille_bornes.cartes.attaques.*;
import mille_bornes.cartes.bottes.AsDuVolant;
import mille_bornes.cartes.bottes.Citerne;
import mille_bornes.cartes.bottes.Increvable;
import mille_bornes.cartes.bottes.VehiculePrioritaire;
import mille_bornes.cartes.parades.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class TasDeCartes {

    private final ArrayList<Carte> cartes = new ArrayList<>();

    public TasDeCartes(boolean creerLesCartes) {
        if (creerLesCartes){
            creeLesCartes();
        }
    }

    public TasDeCartes(JSONArray json) {
        for (int i = 0; i<json.length(); i++){
            cartes.add(Carte.newCarte((JSONObject) json.get(i)));
        }
    }

    private void creeLesCartes(){

        //Création cartes attaques
        for (int i = 0;i<5;i++){
            cartes.add(new FeuRouge());
        }

        for (int i = 0;i<4;i++){
            cartes.add(new LimiteVitesse());
        }

        for (int i = 0;i<3;i++){
            cartes.add(new Accident());
        }

        for (int i = 0;i<3;i++){
            cartes.add(new Crevaison());
        }

        for (int i = 0;i<3;i++){
            cartes.add(new PanneEssence());
        }

        //Création cartes parades
        for (int i = 0;i<14;i++){
            cartes.add(new FeuVert());
        }

        for (int i = 0;i<6;i++){
            cartes.add(new FinDeLimite());
        }

        for (int i = 0;i<6;i++){
            cartes.add(new Reparations());
        }

        for (int i = 0;i<6;i++){
            cartes.add(new Essence());
        }

        for (int i = 0;i<6;i++){
            cartes.add(new RoueDeSecours());
        }

        //Création cartes borne
        for (int i = 0; i<10;i++){
            cartes.add(new Borne(25));
        }

        for (int i = 0; i<10;i++){
            cartes.add(new Borne(50));
        }

        for (int i = 0; i<10;i++){
            cartes.add(new Borne(75));
        }

        for (int i = 0; i<12;i++){
            cartes.add(new Borne(100));
        }

        for (int i = 0; i<4;i++){
            cartes.add(new Borne(200));
        }

        cartes.add(new AsDuVolant());
        cartes.add(new Citerne());
        cartes.add(new Increvable());
        cartes.add(new VehiculePrioritaire());
    }

    public void melangeCartes(){
        Collections.shuffle(cartes);
    }

    public int getNbCartes(){
        return cartes.size();
    }

    public boolean estVide(){
        return cartes.isEmpty();
    }

    public Carte regarde(){
        return cartes.get(0);
    }

    public Carte prend(){
        return cartes.remove(0);
    }

    public void pose(Carte carte){
        cartes.add(0, carte);
    }

    public JSONArray toJson(){
        JSONArray jsonArray = new JSONArray();
        for (Carte carte: cartes){
            jsonArray.put(carte.toJson());
        }
        return jsonArray;
    }
}
