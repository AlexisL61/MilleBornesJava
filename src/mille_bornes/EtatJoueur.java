package mille_bornes;

import mille_bornes.cartes.Attaque;
import mille_bornes.cartes.Bataille;
import mille_bornes.cartes.Carte;
import mille_bornes.cartes.attaques.Accident;
import mille_bornes.cartes.attaques.Crevaison;
import mille_bornes.cartes.attaques.FeuRouge;
import mille_bornes.cartes.attaques.PanneEssence;
import mille_bornes.cartes.bottes.Botte;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class EtatJoueur {

    private Joueur joueur;
    private Stack<Bataille> pileBataille;
    private List<Carte> main;
    private List<Botte> bottes;
    private int km;
    private boolean limiteVitesse;

    public EtatJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    public int getKm() {
        return km;
    }

    public void ajouteKm(int km) {
        this.km += km;

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

    public Bataille getPileBataille() {
        if (!pileBataille.empty()) return pileBataille.peek();
        return null;
    }

    public void setPileBataille(Bataille carte) {
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
        if (pileBataille.isEmpty()) {
            //Regarde si la personne est sensible à l'attaque
            for (Botte botte : bottes) {
                if (botte.contre(carte)) {
                    throw new IllegalStateException();
                }
            }
            for (Carte c : main) {
                if (c instanceof Botte) {
                    if (((Botte) c).contre(carte)) {
                        //COUP-FOURRE
                        jeu.defausse(carte);
                        //joueur.pioche();
                        //jeu.setProchainJoueur(joueur);
                        //jeu.activeProchainJoueurEtTireCarte();
                    }
                }
            }
            pileBataille.add(carte);
        }
        throw new IllegalStateException();
    }

    public String toString() {

        StringBuilder message = new StringBuilder();

        message.append(km).append(" km, ");
        if (limiteVitesse) {
            message.append("(50), ");
        } else {
            message.append("null, ");
        }

        message.append("[");
        for (Botte botte : bottes) {
            message.append(botte.nom).append(", ");
        }
        message = new StringBuilder(message.substring(0, message.length() - 3));
        message.append("]");

        if (!pileBataille.empty()) message.append(", ").append(pileBataille.peek().nom);

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
}

