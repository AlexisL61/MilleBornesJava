package mille_bornes;

import mille_bornes.cartes.Borne;
import mille_bornes.cartes.Carte;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

public class Jeu {

    private final List<Joueur> joueurs = new ArrayList<>();
    private Joueur joueurActif;
    private Joueur prochainJoueur;
    private TasDeCartes defausse;
    private TasDeCartes sabot;

    public Jeu() {
        Scanner sc = new Scanner(System.in);
        String reponse = "";
        while (!reponse.equals("fini") && joueurs.size() < 4 || joueurs.size() <= 1) {
            String reponseBot;
            System.out.println("Voulez-vous jouer avec un bot? (Tapez oui pour jouer avec un bot, autre chose sinon)");
            reponseBot = sc.nextLine();
            int botDifficulty = 0;
            if (reponseBot.equals("oui")) {
                while (botDifficulty != 1 && botDifficulty != 2 && botDifficulty != 3) {
                    System.out.println("Quel sera le niveau de ce bot (1 pour facile, 2 pour moyen, 3 pour difficile)");
                    botDifficulty = sc.nextInt();
                    sc.nextLine();
                }
            }
            System.out.println("Entrez le nom du joueur/bot n°" + (joueurs.size() + 1) + ". (Tapez \"fini\" si vous ne voulez entrer de joueur.)");
            reponse = sc.nextLine();
            if (!reponse.equals("fini")) {
                if (botDifficulty > 0 && botDifficulty < 4) {
                    ajouteJoueurs(new Bot(reponse, Difficulte.values()[botDifficulty - 1]));
                } else {
                    ajouteJoueurs(new Joueur(reponse));
                }
            }
        }
    }

    public Jeu(Joueur... joueurs) {
        ajouteJoueurs(joueurs);
    }

    public Jeu(JSONObject save) {

        JSONArray totalJoueurs = (JSONArray) save.get("players");
        JSONObject jeu = (JSONObject) save.get("jeu");
        for (int i = 0; i < totalJoueurs.length(); i++) {
            if (!(boolean)((JSONObject) totalJoueurs.get(i)).get("bot")) {
                joueurs.add(new Joueur((JSONObject) totalJoueurs.get(i)));
            }else{
                joueurs.add(new Bot((JSONObject) totalJoueurs.get(i)));
            }
        }
        this.prochainJoueur = joueurs.get((Integer) jeu.get("currentPlayer"));
        for (int i = 0; i < joueurs.size(); i++) {
            joueurs.get(i).setProchainJoueur(joueurs.get((i + 1) % joueurs.size()));
        }

        this.defausse = new TasDeCartes((JSONArray) jeu.get("defausse"));
        this.sabot = new TasDeCartes((JSONArray) jeu.get("sabot"));
    }

    public void ajouteJoueurs(Joueur... joueurs) throws IllegalStateException {
        this.joueurs.addAll(Arrays.asList(joueurs));
    }

    public void prepareJeu() {
        Collections.shuffle(joueurs);
        sabot = new TasDeCartes(true);
        sabot.melangeCartes();
        defausse = new TasDeCartes(false);

        for (int i = 0; i < 6; i++) {
            for (Joueur joueur : joueurs) {
                joueur.prendCarte(sabot.prend());
            }
        }

        for (int i = 0; i < joueurs.size(); i++) {
            joueurs.get(i).setProchainJoueur(joueurs.get((i + 1) % joueurs.size()));
        }
        prochainJoueur = joueurs.get(0); //La liste des joueurs est déjà randomisé

    }

    public String toString() {
        StringBuilder finalString = new StringBuilder();
        for (Joueur joueur : joueurs) {
            if (joueurActif.equals(joueur)) finalString.append("\u001B[36m> ");
            finalString.append(joueur.toString());
            finalString.append("\u001B[0m\n");
        }
        return finalString.toString();
    }

    public void sauvegarder() throws IOException {
        String chemin = System.getenv("APPDATA");
        File folder = new File(chemin + "\\" + "mille_borne_java_tp22c");
        File file = new File(chemin + "\\" + "mille_borne_java_tp22c" + "\\save.json");

        if (!folder.exists()) {
            folder.mkdir();
        }
        if (!file.exists()) {
            file.createNewFile();
        }

        JSONObject saveData = new JSONObject();
        JSONObject jeuData = new JSONObject();
        JSONArray players = new JSONArray();

        for (int i = 0; i < this.joueurs.size(); i++) {
            players.put(this.joueurs.get(i).toJson());
            if (this.joueurs.get(i) == joueurActif) {
                jeuData.put("currentPlayer", i);
            }
        }
        jeuData.put("sabot", sabot.toJson());
        jeuData.put("defausse", defausse.toJson());
        saveData.put("players", players);
        saveData.put("jeu", jeuData);

        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(saveData.toString());
        bw.close();
    }

    public boolean joue(boolean pioche) throws IOException {
        activeProchainJoueurEtTireCarte(pioche);
        System.out.println("-------------------------------------------");
        System.out.println(this.toString());
        System.out.println();
        System.out.println("---- AU TOUR DE " + joueurActif.nom + " ----");
        System.out.println(joueurActif.toString());
        System.out.println("-------------------------------------------");
        System.out.println();
        boolean succeed = false;
        while (!succeed) {
            this.sauvegarder();
            //Si le joueur actif change pendant un coup fourré, le joueur se faisant défausser une carte reste le même
            Joueur currentJoueur = joueurActif;
            int carteChoisie = currentJoueur.choisitCarte();
            try {
                if (carteChoisie > 0) {
                    if (currentJoueur instanceof Bot)
                        System.out.println(currentJoueur.nom + " utilise la carte " + currentJoueur.getMain().get(carteChoisie - 1).toString());
                    currentJoueur.joueCarte(this, carteChoisie - 1);
                } else {
                    if (currentJoueur instanceof Bot)
                        System.out.println(currentJoueur.nom + " defausse la carte " + currentJoueur.getMain().get(Math.abs(carteChoisie) - 1).toString());
                    currentJoueur.defausseCarte(this, Math.abs(carteChoisie) - 1);
                }

                succeed = true;
            } catch (IllegalStateException | IllegalArgumentException e) {
                if (currentJoueur.getMain().get(carteChoisie - 1) instanceof Borne) {
                    System.err.println(currentJoueur.ditPourquoiPeutPasAvancer());
                } else {
                    System.err.println("VOUS NE POUVEZ PAS POSER CETTE CARTE!!!");
                }
            }
        }
        return estPartieFinie();
    }

    public void activeProchainJoueurEtTireCarte(boolean pioche) {
        joueurActif = prochainJoueur;
        prochainJoueur = joueurActif.getProchainJoueur();
        if (pioche) joueurActif.prendCarte(pioche());
    }

    public boolean estPartieFinie() {
        for (Joueur joueur : joueurs) {
            if (sabot.estVide() || joueur.getKm() == 1000) return true;
        }
        return false;
    }

    public void setProchainJoueur(Joueur prochainJoueurActif) {
        prochainJoueur = prochainJoueurActif;
    }

    public Joueur getJoueurActif() {
        return joueurActif;
    }

    public List<Joueur> getGagnant() {
        ArrayList<Joueur> joueursGagnants = new ArrayList<>();

        if (!sabot.estVide()) {
            for (Joueur joueur : joueurs) {
                if (joueur.getKm() == 1000) {
                    joueursGagnants.add(joueur);
                }
            }
        } else {
            int maxKm = 0;
            for (Joueur joueur : joueurs) {
                if (joueur.getKm() > maxKm) {
                    maxKm = joueur.getKm();
                }
            }
            for (Joueur joueur : joueurs) {
                if (joueur.getKm() == maxKm) {
                    joueursGagnants.add(joueur);
                }
            }
        }
        return joueursGagnants;
    }

    public Carte pioche() {
        return sabot.prend();
    }

    public void defausse(Carte carte) {
        defausse.pose(carte);
    }

    public int getNbCartesSabot() {
        return sabot.getNbCartes();
    }

    public Carte regardeDefausse() {
        return defausse.regarde();
    }

    public static String chargerSave() {
        String chemin = System.getenv("APPDATA");
        File file = new File(chemin + "\\" + "mille_borne_java_tp22c" + "\\save.json");
        try {
            Scanner myReader = new Scanner(file);
            StringBuilder data = new StringBuilder();
            while (myReader.hasNextLine()) {
                data.append(myReader.nextLine());
            }
            myReader.close();
            return data.toString();
        } catch (FileNotFoundException e) {
            return null; //Le fichier n'existe pas
        }

    }


}
