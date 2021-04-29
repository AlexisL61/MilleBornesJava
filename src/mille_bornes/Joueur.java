package mille_bornes;

import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Bataille;
import mille_bornes.cartes.Carte;
import mille_bornes.cartes.Botte;

import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.ArrayList;

public class Joueur {

    public final String nom;
    private final EtatJoueur etat = new EtatJoueur(this);
    private final Scanner input = new Scanner(System.in);
    private Joueur prochainJoueur;

    public Joueur(String nom) {
        this.nom = nom;
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

    public void ditPourquoiPeutPasAvancer() {
        etat.ditPourquoiPeutPasAvancer();
    }

    public String montreLesCartes() {
        StringBuilder cartes = new StringBuilder();
        for (int i = 0; i<getMain().size(); i++) {
            cartes.append(i + 1).append(" : ").append(getMain().get(i).toString()).append("\n");
        }
        return cartes.toString();
    }

    @Override
    public String toString() {
        return nom + " : " + etat.toString();
    }
}
