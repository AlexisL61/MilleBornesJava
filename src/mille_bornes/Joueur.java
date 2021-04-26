package mille_bornes;

import mille_bornes.cartes.Carte;

import java.util.*;

public class Joueur {

    private String nom;
    private EtatJoueur etat;
    private Scanner input;
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
        System.out.println("Un entier inférieur à 0 défausse la carte tandis que supérieur à 0 la joue.");
        System.out.println("Indiquez la carte choisie");
        int carteChoisie;
        try {
            carteChoisie = input.nextInt();
        } catch (InputMismatchException e) {
            System.err.println("S'il vous plaît, entrez un entier");
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

        List<Joueur> players;
        Joueur currentPlayer = this;
        while (currentPlayer.prochainJoueur != this){
            currentPlayer = currentPlayer.prochainJoueur;
        }



        int joueurChoisi;
        try {
            joueurChoisi = input.nextInt();
        } catch (InputMismatchException e) {
            System.err.println("S'il vous plaît, entrez un entier");
            return choisitAdversaire(carte);
        }


        if (joueurChoisi > joueurs) {
            throw new NoSuchElementException();
        }

        return joueurChoisie;
    }

    public void prendCarte(Carte carte) throws IllegalStateException {

    }

    public void joueCarte(Jeu jeu, int numero) throws IllegalStateException {

    }

    public void defausseCarte​(Jeu jeu, int numero) {

    }
}
