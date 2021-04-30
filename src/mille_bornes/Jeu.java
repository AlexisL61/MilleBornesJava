package mille_bornes;

import mille_bornes.cartes.Carte;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Jeu {

    private final List<Joueur> joueurs = new ArrayList<>();
    private Joueur joueurActif;
    private Joueur prochainJoueur;
    private TasDeCartes defausse;
    private TasDeCartes sabot;

    public Jeu() {

    }

    public Jeu(Joueur... joueurs) {
        ajouteJoueurs(joueurs);
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

    public boolean joue() {
        activeProchainJoueurEtTireCarte();
        System.out.println("-------------------------------------------");
        System.out.println(this.toString());
        System.out.println();
        System.out.println("---- AU TOUR DE " + joueurActif.nom + " ----");
        System.out.println(joueurActif.toString());
        System.out.println("-------------------------------------------");
        System.out.println();
        boolean succeed = false;
        while (!succeed) {
            //Si le joueur actif change pendant un coup fourré, le joueur se faisant défausser une carte reste le même
            Joueur currentJoueur = joueurActif;
            int carteChoisie = currentJoueur.choisitCarte();
            try {
                if (carteChoisie > 0) {
                    currentJoueur.joueCarte(this, carteChoisie - 1);
                }
                currentJoueur.defausseCarte(this, Math.abs(carteChoisie) - 1);
                succeed = true;
            } catch (IllegalStateException | IllegalArgumentException e) {
                System.err.println("VOUS NE POUVEZ PAS POSER CETTE CARTE!!!");
            }
        }
        return estPartieFinie();
    }

    public void activeProchainJoueurEtTireCarte() {
        joueurActif = prochainJoueur;
        prochainJoueur = joueurActif.getProchainJoueur();
        joueurActif.prendCarte(pioche());
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


}
