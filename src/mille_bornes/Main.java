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
            while (!reponse.equals("non") && !reponse.equals("oui")) {
                System.out.println("Voulez-vous reprendre votre sauvegarde ? (oui ou non)");
                reponse = sc.nextLine();
            }
            if (reponse.equals("non")) {
                jeu = new Jeu();
                jeu.prepareJeu();
            } else {
                jeu = new Jeu(new JSONObject(Jeu.chargerSave()));
                jeu.joue(false);
            }
        } else {
            jeu = new Jeu();
            jeu.prepareJeu();
        }

        while (jeu.getGagnant().size() == 0) {
            jeu.joue(true);
        }
        System.out.println("\n-------------------------------------------------\n");
        if (jeu.getGagnant().size() == 1) {
            System.out.println("\u001B[33m" + jeu.getGagnant().get(0).nom + " est le seul gagnant !\u001B[0m");
        } else {
            for (int i = 0; i < jeu.getGagnant().size(); i++) {
                System.out.println("\u001B[33m" + jeu.getGagnant().get(i).nom + " est un des gagnants !\u001B[0m");
            }
        }
        //Suppression de la sauvegarde
        String chemin = System.getenv("APPDATA");
        File file = new File(chemin + "\\" + "mille_borne_java_tp22c" + "\\save.json");
        file.delete();

    }
}
