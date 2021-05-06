package mille_bornes;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {


        Jeu jeu;
        if (Jeu.chargerSave() != null) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Voulez-vous reprendre votre sauvegarde ? (oui ou non)");
            String reponse = sc.nextLine();
            while(!reponse.equals("non") && !reponse.equals("oui")){
                System.out.println("Voulez-vous reprendre votre sauvegarde ? (oui ou non)");
                reponse = sc.nextLine();
            }
            if (reponse.equals("non")) {
                jeu = new Jeu(new Joueur("Alexis"), new Joueur("Clément"), new Joueur("Morgane"), new Joueur("Tony-Paul"));
                jeu.prepareJeu();
            } else {
                jeu = new Jeu(new JSONObject(Jeu.chargerSave()));
                jeu.joue(false);
            }
        }else{
            jeu = new Jeu(new Joueur("Alexis"), new Joueur("Clément"), new Joueur("Morgane"), new Joueur("Tony-Paul"));
            jeu.prepareJeu();
        }

        while (jeu.getGagnant().size() == 0) {
            jeu.joue(true);
        }
        //ecrasersave

    }
}
