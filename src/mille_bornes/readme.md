# Modification de l'api initiale

Nom de la méthode | Classe | Modifications apportées
--- | --- | ---
activeProchainJoueurEtTireCarte | Jeu | Désormais, cette méthode prend un nouveau booléen en paramètre. Car nous nous sommes rendu compte que lors d'un coup fourré, le joueur tire une carte. Mais s'il effectue son coup fourré en fin de partie, il se peut le sabot ne contienne plus de carte. Et cela lève une exception lors de la pioche. Pour éviter cela, nous regardons si le joueur peut piocher ou non et entrons le résultat en paramètre. Si oui, la fonction fera piocher le joueur, non sinon. De plus, ce paramètre est utile lors d'un chargement de sauvegarde. En effet, lors du chargement de sauvegarde, le joueur qui doit jouer a déjà pioché. Cette argument permet de relancer le jeu en lançant la fonction "joue" sans lui faire retirer une carte.
montreLesCartes | Joueur | Cette méthode a été ajoutée pour montrer plus facilement les cartes du joueur. Lors de son appel, la fonction va afficher dans la console les cartes du joueur.

# Extensions

## Bottes

Les méthodes que nous avons ajoutées pour cette extension sont:

Nom de la méthode | Classe | Description
--- | --- | ---
getJoueur | EtatJoueur | Permet de récupérer le joueur à partir de EtatJoueur
appliqueEffet | Botte | Regarde si le joueur à une carte Attaque au dessus de sa pile bataille. Si cela est le cas, il regarde si la botte s'apprêtant à être posée la contre. Si oui, on défausse l'attaque. Dans tous les cas, on pose la botte et on set le prochain joueur au joueur posant la botte pour qu'il rejoue
contre | toutes les classes du package "bottes" | Définie les cartes que contre chaque botte
## Sauvegarde/Chargement de partie

#### Système de sauvegarde

Pour sauvegarder notre partie, nous avons utilisé le JSON.
Le JSON est un système de clé-valeur qui permet donc de récupérer des données pour une valeur donnée.
Nous sauvegardons donc, dans les fichiers de l'utilisateur, un fichier.json qui contient toutes les données de la partie.
C'est-à-dire, pour chaque classe, il y a les noms d'attributs en clé, et leur valeur en valeur.
Ce fichier est sauvegardé dans la partie AppData de l'ordinateur et plus particulièrement dans la partie Roaming (Pour un ordinateur Windows). De ce fait, nous sommes sûr que l'application aura accès à ce dossier et va donc pouvoir modifier le fichier.json
Pour une meilleure visualisation de l'emplacement du fichier:

> C:\Users\<user>\AppData\Roaming\mille_borne_java_tp22c\save.json

Une partie du fichier ressemble à ceci:
![Fichier json](https://cdn.discordapp.com/attachments/758234478568669204/842748325492817920/unknown.png)

Nous utilisons la librairie [JSONObject](https://github.com/stleary/JSON-java)

Pour chaque objet que nous devons sauvegarder dans le fichier, nous avons créé une méthode toJson qui envoie un JSONObject qui permet ensuite d'être sauvegardé.
Classes affectées: Jeu, Joueur, Carte, TasDeCarte, *Bot*.

Nous avons aussi inclut dans l'objet Jeu, la méthode :
Nom | Description
--- | ---
sauvegarder | Permet de sauvegarder une partie en cours

> ℹ Les parties sont sauvegardées à chaque tour, donc il n'y a pas besoin de sauvegarder manuellement la partie

#### Système de chargement de partie

Au démarrage d'une partie il est demandé au joueur, si une partie a été sauvegardée auparavant, de démarrer la partie sauvegardée:
![Image](https://cdn.discordapp.com/attachments/758234478568669204/842746691795025950/unknown.png)

Pour plusieurs objets, nous avons ajoutés des constructeurs prenant un JSONObject en paramètre qui permet de créer un objet à partir d'un JSONObject.
Les classes ayant un constructeur acceptant un JSONObject sont les suivantes:
Jeu, Joueur, *Bot*, TasDeCartes,

Nom | Classe | Description
--- | --- | ---
newCarte | Carte | Permet de créer une carte à partir d'un JSONObject
chargerSave | Jeu | Permet de charger une partie à partir du fichier save.json déjà sauvegardé

## Bots

### Création
Nous avons commencé par faire une énumération nommées "Difficulte"
contenant trois valeurs :
- facile
- moyen
- difficile

Nous avons ensuite créé une classe nommée "Bot" étant un extend de "Joueur".
Ceci afin de pouvoir surcharger uniquement les classes où les joueurs humains avaient
des choix à faire tels que "choisitAdversaire" et "choisitCarte".
Nous avons finalement créé deux choisitAdversaire et deux choisitCarte
différents. En effet, l'un est pour le bot facile, l'autre pour le difficile.

### Les différentes fonctions implémentées

Nom de la méthode | Description
--- | ---
choisitAdversaireFacile | Retourne un joueur aléatoire à attaquer. Si jamais aucun n'est attaquable, le bot retirera une carte.
ChoisitCarteFacile | Choisit un nombre entre -7 et 7 différent de 0 et vérifie que la carte choisie est applicable. Si oui elle est retournée, sinon le bot rechoisit un nombre.
choisitAdversaireDifficile | Choisit le joueur à attaquer avec le plus de km à son compteur
choisitCarteDifficile | Le bot va d'abord essayer d'utiliser une parade s'il est sous l'influence d'une attaque. Sinon, il va essayer d'enlever sa limitation s'il est limité. Sinon il va essayer de poser une borne. S'il ne peut toujours pas faire tout cela, il va essayer d'attaquer la personne attaquable avec le plus de km à son compteur. S'il n'y arrive toujours pas, il va essayer de défausser une attaque, une parade ou une carte considérée comme inutile (c'est-à-dire une borne qui lui ferai dépasser les 1000km. Par exemple une borne 75 s'il est à 50km de l'arrivée). S'il n'a toujours pas réussi à choisir une carte, c'est qu'il ne lui reste que des bottes ou des bornes kilométriques utiles. Il choisira donc de jouer une botte. S'il en a pas, c'est qu'il possède que des bornes utiles. Il retira la première borne de sa main. Vu que sa main a été précédemment mélangée, cela revient à tirer une borne au hasard 
ChoisitRandom | Retourne un nombre entre -7 et 7, 0 exclus. 
ChoisitCarte | Appelle choisitCarteFacile si le bot est en mode facile ou alors choisitCarteDifficile si le bot est difficile. Si le bot est moyen, alors il appelle avec une probabilité de 50% l'une ou l'autre.
ChoisitAdversaire | Même principe que choisitCarte mais avec choisitAdversaireFacile et choisitAdversaireDifficile
joueurPeutEtreAttaque | Regarde si le joueur entré en paramètre peut-être attaqué avec la carte Attaque entrée en paramètre
checkSiJoueursAttaquables | Regarde si des joueurs peuvent être attaqués avec la carte Attaque entrée en paramètre
toString | Reprend le toString de EtatJoueur mais ajoute le nom du bot avec "(bot)" à côté pour préciser que le joueur est un bot
toJson | Reprend le toJson de Joueur (la classe mère) en ajoutant la clé "bot" avec pour valeur "true" et la clé "difficulté" avec la difficulté du bot. Cela permet de rendre le bot compatible avec la précédente extension "sauvegarde".

### Fonctionnement de l'extension

En lançant l'application, si on ne reprend pas une sauvegarde existante, le jeu va nous proposer d'ajouter des joueurs.
Il va d'abord demander pour chaque joueur si le joueur est un humain ou un bot. Si nous répondons que nous voulons un bot, il va demander son niveau de difficulté puis enfin son nom.
Le nouveau joueur sera donc un bot et jouera tout seul !