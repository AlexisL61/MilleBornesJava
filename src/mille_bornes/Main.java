package mille_bornes;

public class Main {
    public static void main(String[] args) {
        Jeu jeu = new Jeu(new Joueur("Alexis"), new Joueur("Clément"), new Joueur("Morgane"), new Joueur("Tony-Paul"));
        jeu.prepareJeu();
        while(jeu.getGagnant().size() == 0){
            jeu.joue();
        }

    }
}
