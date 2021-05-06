package mille_bornes;

import mille_bornes.cartes.*;
import mille_bornes.cartes.attaques.LimiteVitesse;
import org.json.JSONObject;

import java.util.*;

public class Bot extends Joueur {

    private final Difficulte difficulte;

    Bot(String nom, Difficulte difficulte) {
        super(nom);
        this.difficulte = difficulte;
    }

    public Bot(JSONObject jsonObject) {
        super(jsonObject);
        difficulte = Difficulte.valueOf((String) jsonObject.get("difficulte"));
    }

    @Override
    public int choisitCarte() {
        System.out.println(montreLesCartes());
        if (difficulte == Difficulte.facile) {
            return choisitFacile();
        }
        if (difficulte == Difficulte.moyen) {
            int numChoose = ((int) (Math.random() * 2));
            if (numChoose == 0) {
                return choisitFacile();
            } else {
                return choisitDifficile();
            }
        }
        if (difficulte == Difficulte.difficile) {
            int carteNum = choisitDifficile();
            return choisitDifficile();
        }
        return 0;
    }

    private int choisitDifficile() {

        if (super.getBataille() != null && super.getBataille() instanceof Attaque) {

            //Il essaye de contrer l'attaque de sa pile de bataille
            for (int i = 0; i < super.getMain().size(); i++) {
                Carte carte = super.getMain().get(i);
                if (carte instanceof Parade) {
                    Parade parade = (Parade) carte;
                    if (parade.contre((Attaque) super.getBataille())) {
                        return (i + 1);
                    }
                }
            }
            //
        }

        //Borne
        if (super.getBataille() instanceof Parade) {
            int maxBorne = 0;
            int carteMaxBorne = 0;
            for (int i = 0; i < super.getMain().size(); i++) {
                if (super.getMain().get(i) instanceof Borne) {
                    if (((Borne) super.getMain().get(i)).km > maxBorne) {
                        if (((Borne) super.getMain().get(i)).km <= 1000 - super.getKm()) { //Regarde pour finir la part
                            if (!super.getLimiteVitesse() || ((Borne) super.getMain().get(i)).km <= 50) {
                                maxBorne = ((Borne) super.getMain().get(i)).km;
                                carteMaxBorne = (i + 1);
                            }
                        }
                    }
                }
            }
            if (carteMaxBorne != 0) return carteMaxBorne; //(Si carteMaxBorne = 0, alors il n'a de carte borne)
        }

        //Attaque
        for (int i = 0; i < super.getMain().size(); i++) {
            if (super.getMain().get(i) instanceof Attaque) {
                if (checkSiJoueursAttaquables(((Attaque) super.getMain().get(i)))) {
                    return (i + 1);
                }
            }
        }


        //Defausse car le bot n'a pas d'attaque et n'a pas pu avancer
        List<Carte> main = super.getMain();
        Collections.shuffle(main);
        for (int i = 0; i < main.size(); i++) {
            if (!(main.get(i) instanceof Botte)) {
                if (!(main.get(i) instanceof Borne && ((Borne) main.get(i)).km < 1000 - super.getKm())) {
                    return -(i + 1);
                }
            }
        }

        //ici le bot est proche de l'arrivée et n'a que des bottes ou des cartes bornes plus petite ou égale que la distance
        //restante à parcourir
        //priorité à jouer la botte
        for (int i = 0; i < main.size(); i++) {
            if (main.get(i) instanceof Botte) {
                return (i + 1);
            }
        }
        //ici le bot n'a que des bornes plus petites ou égales à la distance restante à parcourir
        return -1; //Retourne la première carte à défausser
    }

    private int choisitRandom() {
        int numberChoose = 0;
        while (numberChoose == 0) {
            numberChoose = (int) Math.floor((Math.random() * 15) - 7); //renvoie un entier entre -7 et 7
        }
        return numberChoose;
    }

    private int choisitFacile() {
        int numberChoose = 0;
        Carte carte;
        boolean succeed = false;

        while (!succeed) {
            numberChoose = choisitRandom(); //renvoie un entier entre -7 et 7
            if (numberChoose > 0) {
                carte = super.getMain().get(numberChoose-1);

                if (carte instanceof Parade) {
                    if (super.getBataille() instanceof Attaque && ((Parade) carte).contre((Attaque) super.getBataille())) {
                        succeed = true;
                    }
                }

                if (carte instanceof Borne) {
                    if (!(super.getBataille() instanceof Attaque) && ((Borne) carte).km < 1000 - getKm()) {
                        succeed = true;
                    }
                }
                if (carte instanceof Attaque && checkSiJoueursAttaquables((Attaque) carte)) {
                    succeed = true;
                }
                if (carte instanceof Botte) {
                    succeed = true;
                }
            } else {
                succeed = true;
            }
        }
        return numberChoose;
    }

    @Override
    public Joueur choisitAdversaire(Carte carte) {
        if (difficulte == Difficulte.facile) {
            if (checkSiJoueursAttaquables((Attaque) carte)) {
                return choisitAdversaireRandom();
            } else {
                return null;
            }
        }
        if (difficulte == Difficulte.moyen) {
            int numChoose = ((int) (Math.random() * 2));
            if (numChoose == 0) {
                if (checkSiJoueursAttaquables((Attaque) carte)) {
                    return choisitAdversaireRandom();
                } else {
                    return null;
                }
            } else {
                return choisitAdversaireDifficile((Attaque) carte);
            }
        }
        return choisitAdversaireDifficile((Attaque) carte);
    }

    private Joueur choisitAdversaireRandom() {
        Joueur currentPlayer = this;
        ArrayList<Joueur> joueurs = new ArrayList<>();
        while (currentPlayer.getProchainJoueur() != this) {
            currentPlayer = currentPlayer.getProchainJoueur();
            joueurs.add(currentPlayer);
        }
        Collections.shuffle(joueurs);
        int playerChoose = 0;
        for (int i = 0; i < joueurs.size(); i++) {
            if (joueurs.get(i).getBataille() == null || joueurs.get(i).getBataille() instanceof Parade) {
                playerChoose = i;
            }
        }
        System.out.println(nom + " attaque le joueur " + joueurs.get(playerChoose).nom);
        return joueurs.get(playerChoose);
    }

    private boolean checkSiJoueursAttaquables(Attaque carte) {
        Joueur currentPlayer = this;
        int nbJoueursAttaquables = 0;
        while (currentPlayer.getProchainJoueur() != this) {
            currentPlayer = currentPlayer.getProchainJoueur();
            if (joueurPeutEtreAttaque(currentPlayer, carte)) nbJoueursAttaquables++;
        }
        return nbJoueursAttaquables >= 1;
    }

    private boolean joueurPeutEtreAttaque(Joueur currentPlayer, Attaque carte){
        if (currentPlayer.getBataille() == null || currentPlayer.getBataille() instanceof Parade) {
            if (!(carte instanceof LimiteVitesse) || !currentPlayer.getLimiteVitesse()) {
                boolean botte = false;
                for (int i = 0; i < currentPlayer.getBottes().size(); i++) {
                    if (currentPlayer.getBottes().get(i).contre(carte)) botte = true;
                }
                return !botte;

            }
        }
        return false;
    }

    private Joueur choisitAdversaireDifficile(Attaque carte) {
        Joueur currentPlayer = this;
        int maxKm = 0;
        Joueur leJoueurAAttaque = null;
        while (currentPlayer.getProchainJoueur() != this) {
            currentPlayer = currentPlayer.getProchainJoueur();
            if (joueurPeutEtreAttaque(currentPlayer, carte) && (currentPlayer.getBataille() == null || currentPlayer.getBataille() instanceof Parade) && currentPlayer.getKm() >= maxKm) {
                leJoueurAAttaque = currentPlayer;
                maxKm = leJoueurAAttaque.getKm();
            }
        }
        if (leJoueurAAttaque != null) {
            System.out.println(nom + " attaque le joueur " + leJoueurAAttaque.nom);
        }
        return leJoueurAAttaque;
    }

    @Override
    public String toString() {
        return nom + " (bot) : " + super.getEtat().toString();
    }

    @Override
    public JSONObject toJson() {
            JSONObject o = super.toJson();
            o.put("bot",true);
            o.put("difficulte",this.difficulte);
            return o;
    }
}
