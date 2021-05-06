package mille_bornes;

import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Bataille;
import mille_bornes.cartes.Carte;
import mille_bornes.cartes.Botte;
import mille_bornes.cartes.attaques.FeuRouge;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class Joueur {

    public final String nom;
    private final EtatJoueur etat = new EtatJoueur(this);
    private final Scanner input = new Scanner(System.in);
    private Joueur prochainJoueur;

    public Joueur(String nom) {
        this.nom = nom;
        etat.setBataille(new FeuRouge());
    }

    public Joueur(JSONObject joueur){
        this.nom = (String) joueur.get("nom");
        this.etat.setKm((Integer) joueur.get("km"));
        this.etat.setLimiteVitesse((boolean) joueur.get("limite"));
        JSONArray cartes = ((JSONArray) joueur.get("cartes"));
        JSONArray bottes = ((JSONArray) joueur.get("bottes"));
        JSONArray pileBataille = ((JSONArray) joueur.get("pileBataille"));
        for (int i = 0;i<cartes.length();i++){
            this.etat.getMain().add(Carte.newCarte((JSONObject) cartes.get(i)));
        }
        for (int i = 0;i<bottes.length();i++){
            this.etat.addBotte((Botte) Carte.newCarte((JSONObject) bottes.get(i)));
        }
        for (int i  = pileBataille.length()-1; i>=0; i--){ //Pile renversée donc on part de la fin
            etat.setBataille((Bataille) Carte.newCarte((JSONObject) pileBataille.get(i)));
        }
    }

    public Joueur getProchainJoueur() {
        return prochainJoueur;
    }

    public void setProchainJoueur(Joueur prochainJoueur) {
        this.prochainJoueur = prochainJoueur;
    }

    public List<Carte> getMain() {
        return etat.getMain();
    }

    public int getKm() {
        return etat.getKm();
    }

    public boolean getLimiteVitesse() {
        return etat.getLimiteVitesse();
    }

    public EtatJoueur getEtat() {
        return etat;
    }

    public int choisitCarte() {
        System.out.println(montreLesCartes());
        System.out.println("Un entier inférieur à 0 défausse la carte tandis que supérieur à 0 la joue.");
        System.out.println("Indiquez la carte choisie");
        int carteChoisie;
        try {
            carteChoisie = input.nextInt();
        } catch (InputMismatchException e) {
            System.err.println("S'il vous plaît, entrez un entier");
            input.nextLine();
            return choisitCarte();
        }

        try {
            if (carteChoisie == 0 || carteChoisie < -7 || carteChoisie > 7) {
                throw new NoSuchElementException();
            }
        } catch (NoSuchElementException e) {
            System.err.println("Le nombre choisi n'est pas correct");
            return choisitCarte();
        }
        return carteChoisie;
    }

    public Joueur choisitAdversaire(Carte carte) throws IllegalStateException {
        System.out.println("Choisissez un joueur à attaquer");
        System.out.println("Indiquez l'adversaire choisi");

        //Creation de la liste des joueurs
        Joueur currentPlayer = this;
        ArrayList<Joueur> joueurs = new ArrayList<>();
        while (currentPlayer.prochainJoueur != this) {
            currentPlayer = currentPlayer.prochainJoueur;
            joueurs.add(currentPlayer);
        }

        //Afficher dans la console les joueurs et leur numéro
        for (int i = 0; i < joueurs.size(); i++) {
            System.out.println((i + 1) + " : " + joueurs.get(i).nom);
        }

        //Choix du joueur
        int joueurChoisi;
        try {
            joueurChoisi = input.nextInt();
        } catch (InputMismatchException e) {
            System.err.println("S'il vous plaît, entrez un entier");
            return choisitAdversaire(carte);
        }

        if (joueurChoisi > joueurs.size() || joueurChoisi < 1) { //Attaque annulée
            return null;
        }
        return joueurs.get(joueurChoisi - 1);
    }

    public void prendCarte(Carte carte) throws IllegalStateException {
        etat.prendCarte(carte);
    }

    public void joueCarte(Jeu jeu, int numero) throws IllegalStateException {
        etat.joueCarte(jeu, numero);
    }

    public void joueCarte(Jeu jeu, int numero, Joueur adversaire) throws IllegalStateException {
        etat.joueCarte(jeu, numero, adversaire);
    }

    public Bataille getBataille() {
        return etat.getBataille();
    }

    public List<Botte> getBottes() {
        return etat.getBottes();
    }

    public void defausseCarte(Jeu jeu, int numero) {
        etat.defausseCarte(jeu, numero);
    }

    public void attaque(Jeu jeu, Attaque carte) throws IllegalStateException {
        etat.attaque(jeu, carte);
    }

    public String ditPourquoiPeutPasAvancer() {
        return etat.ditPourquoiPeutPasAvancer();
    }

    public String montreLesCartes() {
        StringBuilder cartes = new StringBuilder();
        for (int i = 0; i<getMain().size(); i++) {
            cartes.append(i + 1).append(" : ").append(getMain().get(i).toString()).append("\n");
        }
        return cartes.toString();
    }

    public JSONObject toJson(){
        JSONObject joueurObject = new JSONObject();
        joueurObject.put("km",etat.getKm());
        joueurObject.put("limite",etat.getLimiteVitesse());
        joueurObject.put("nom",nom);
        JSONArray carteObject = new JSONArray();
        for (Carte carte:etat.getMain()){
            carteObject.put(carte.toJson());
        }
        JSONArray botteObject = new JSONArray();
        for (Carte botte:etat.getBottes()){
            botteObject.put(botte.toJson());
        }
        joueurObject.put("cartes",carteObject);
        joueurObject.put("bottes",botteObject);
        joueurObject.put("bot",false);

        Stack<Bataille> copiePile = (Stack<Bataille>) etat.getPileBataille().clone();

        JSONArray batailleObject = new JSONArray();

        while(!copiePile.isEmpty()){
            batailleObject.put(copiePile.pop().toJson());
        }

        joueurObject.put("pileBataille", batailleObject);
        return joueurObject;
    }

    @Override
    public String toString() {
        return nom + " : " + etat.toString();
    }
}
