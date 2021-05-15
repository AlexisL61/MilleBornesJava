package mille_bornes;

import mille_bornes.cartes.*;
import mille_bornes.cartes.attaques.*;
import mille_bornes.cartes.Botte;
import mille_bornes.cartes.parades.FinDeLimite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class EtatJoueur {

    private final Joueur joueur;
    private final Stack<Bataille> pileBataille = new Stack<>();
    private final List<Carte> main = new ArrayList<>();
    private final List<Botte> bottes = new ArrayList<>();
    private int km;
    private boolean limiteVitesse;

    public EtatJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    public int getKm() {
        return km;
    }

    public void setKm(int km){
        this.km = km;
    }

    public void ajouteKm(int km) {
        this.km += km;
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public String ditPourquoiPeutPasAvancer() {
        if (!pileBataille.empty()) {
            if (pileBataille.peek() instanceof Accident) {
                return "Vous ne pouvez pas avancer car vous êtes accidenté";
            }
            if (pileBataille.peek() instanceof Crevaison) {
                return "Vous ne pouvez pas avancer car vous êtes en crevaison";
            }
            if (pileBataille.peek() instanceof PanneEssence) {
                return "Vous ne pouvez pas avancer car vous n'avez plus d'essence";
            }
            if (pileBataille.peek() instanceof FeuRouge) {
                return "Vous ne pouvez pas avancer car vous êtes devant un feu rouge";
            }
        }
        return null;
    }

    public boolean getLimiteVitesse() {
        return limiteVitesse;
    }

    public void setLimiteVitesse(boolean limiteVitesse) {
        this.limiteVitesse = limiteVitesse;
    }

    public Bataille getBataille() {
        if (pileBataille.isEmpty()) return null;
        return pileBataille.peek();
    }

    public Stack<Bataille> getPileBataille() {
        return pileBataille;
    }

    public void setBataille(Bataille carte) {
        this.pileBataille.push(carte);
    }

    public void defausseBataille(Jeu jeu) {
        jeu.defausse(pileBataille.pop());
    }

    public List<Carte> getMain() {
        return main;
    }

    public void addBotte(Botte carte) {
        bottes.add(carte);
    }

    public List<Botte> getBottes() {
        return Collections.unmodifiableList(bottes);
    }

    public void attaque(Jeu jeu, Attaque carte) throws IllegalStateException {

            //Regarde si la personne est sensible à l'attaque
            for (Botte botte : bottes) {
                if (botte.contre(carte)) {
                    throw new IllegalStateException();
                }
            }

            //Regarde s'il y a un coup fourré
            int i = 0;
            for (Carte c : main) {
                if (c instanceof Botte) {
                    if (((Botte) c).contre(carte)) {
                        //COUP-FOURRE
                        System.out.println("\u001B[35m" + joueur.nom + " fait un coup fourré !\u001B[0m");
                        ((Botte) c).appliqueEffet(jeu, this);
                        main.remove(i);
                        jeu.setProchainJoueur(joueur);
                        if (jeu.getNbCartesSabot() != 0) jeu.activeProchainJoueurEtTireCarte(true); //Sinon c'est la fin de la partie

                        jeu.setProchainJoueur(joueur);
                        return;
                    }
                }
                i++;
            }

            //Applique la carte
            try {
                carte.appliqueEffet(jeu, this);
            } catch (IllegalStateException e){
                throw new IllegalStateException();
            }

    }

    public String toString() {

        StringBuilder message = new StringBuilder();

        message.append(km).append(" km, ");
        if (limiteVitesse) {
            message.append("limité à 50km/h, ");
        } else {
            message.append("non limité, ");
        }

        message.append("[");
        for (Botte botte : bottes) {
            message.append(botte.nom).append(", ");
        }
        message = bottes.size()==0?message:new StringBuilder(message.substring(0, message.length() - 2));
        message.append("]");

        if (!pileBataille.empty() && pileBataille.peek() instanceof Attaque) message.append(", ").append(pileBataille.peek().nom);

        return message.toString();
    }

    public void prendCarte(Carte carte) throws IllegalStateException {
        if (main.size() > 6){
            throw new IllegalStateException();
        }else{
            main.add(carte);
        }
    }

    public void defausseCarte(Jeu jeu, int numero){
        jeu.defausse(main.get(numero));
        main.remove(numero);
    }

    public void joueCarte(Jeu jeu, int numero) throws IllegalStateException {

        Carte laCarteAJouer = main.get(numero);

        if (laCarteAJouer instanceof Attaque){
            Joueur joueurAAttaquer = joueur.choisitAdversaire(laCarteAJouer);
            if (joueurAAttaquer != null){
                joueCarte(jeu, numero, joueurAAttaquer);
            }
        }

        if (laCarteAJouer instanceof Parade || laCarteAJouer instanceof Botte || laCarteAJouer instanceof Borne){
            try {
                laCarteAJouer.appliqueEffet(jeu, this);
            } catch (IllegalStateException e){
                throw e;
            }
        }
        if (laCarteAJouer instanceof Borne || laCarteAJouer instanceof LimiteVitesse || laCarteAJouer instanceof FinDeLimite){
            this.defausseCarte(jeu,numero);
        }else{
            main.remove(numero);
        }
    }

    public void joueCarte(Jeu jeu, int numero, Joueur adversaire) throws IllegalStateException{
        try {
            adversaire.attaque(jeu, (Attaque) main.get(numero));
        }catch(IllegalStateException e){
            throw e;
        }
    }

}

