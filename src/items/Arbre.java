package items;

import java.util.LinkedList;

public class Arbre {
    private LinkedList<Objet> objets;
    private Arbre sousArbreDroit, sousArbreGauche;
    private static float BorneInf;
    //variable stockant l'état de traitement du noeud
    // TRUE si noeud checké par algo
    // FALSE si noeud à créer/créé mais pas encore checké
    private boolean noeudVerifie;

    // variables globales de benchmark
    public static int nombreNoeudsCrees = 1;
    public static int nombreNoeudsSupprimes = 1;

    public static long usageMemMax = 0;
    public static float coutParNoeud = 0;

    // constructeurs et initialisateurs
    public Arbre(){
        objets = new LinkedList<>();
        sousArbreDroit = sousArbreGauche = null;
        noeudVerifie = false;
    }

    public Arbre(LinkedList<Objet> objetsPere){
        this.objets = new LinkedList<>();
        this.objets.addAll(objetsPere);
        sousArbreDroit = sousArbreGauche = null;
        noeudVerifie = false;
    }

    public static void initBorneInf(){
        Arbre.BorneInf = 0;
    }

    // fonctions liées à la liste d'objets de l'arbre
    private void setObjet(Objet objet) {
        if(objets == null){
            this.objets = new LinkedList<>();
        }
        this.objets.add(objet);
    }

    LinkedList<Objet> getObjets() {
        return objets;
    }

    // fonctions liées aux outils de benchmark
    private void setUsageMemMax(long usageMem){
        if(usageMem > usageMemMax){
            usageMemMax = usageMem;
        }
    }
    private void setCoutParNoeud(long usageMem) {
        coutParNoeud = usageMem / (float) nombreNoeudsCrees;
    }

    // getters et setters des sous arbres
    Arbre getSousArbreDroit() {
        return sousArbreDroit;
    }

    Arbre getSousArbreGauche() {
        return sousArbreGauche;
    }

    void setSousArbreDroit(Arbre sousArbreDroit) {
        this.sousArbreDroit = sousArbreDroit;
    }

    void setSousArbreGauche(Arbre sousArbreGauche) {
        this.sousArbreGauche = sousArbreGauche;
    }

    // fonctions de construction de l'arbre
    public void remplirArbre(LinkedList<Objet> objets, boolean isAscendante, int entier, float poidsMax, NoeudOptimal node){
        long usageMem; // benchmark | mémoire utilisée

        // étape 1 bis : si il s'agit d'un noeud en branche ascendante,
        // ajouter l'objet correspondant à la profondeur actuelle
        if (isAscendante) {
            this.setObjet(objets.get(entier));
        }

        // étape 1 : verification du poids
        // si le poids du noeud dépasse le poids max,
        // on considère que le traitement du noeud est terminé
        // RETOUR au noeud PERE
        float weight = getWeight();
        if(weight > poidsMax) {
            noeudVerifie = true;
            return;
        }

        // étape 2 : verification de la borne INFerieure
        // on vérifie d'abord si la borne inférieure est une
        // potentielle NOUVELLE borne inférieure
        // SI borneINF arbre > borneINF globale actuelle
        // ALORS borneINF arbre devient nouvelle borneINF globale
        float val = 0;
        for(Objet objet: this.getObjets()){
            val += objet.getValue();
        }
        if(val > Arbre.BorneInf) Arbre.BorneInf = val;

        // étape 3 : verification de la borne SUPerieure
        // on vérifie ensuite si la borne supérieure est inférieure
        // à la borneINF globale actuelle
        // SI borneINF globale > borneSUP arbre
        // ALORS le traitement se termine
        // RETOUR au noeud PERE
        val = getBorneSup(objets, entier, val);
        if(val < Arbre.BorneInf){
            noeudVerifie = true;
            return;
        }

        // étape 4 : verification de la qualité du noeud
        // on vérifie ensuite si le noeud est un possible
        // nouveau meilleur noeud à l'aide de la fonction addPotentialNode()
        node.addPotentialNode(this);
        noeudVerifie = true;

        // DEBUT benchmark | mémoire utilisée
        // récupère la quantité de mémoire mobilisées pour effectuer les traitements précédents
        usageMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        setCoutParNoeud(usageMem);
        // FIN benchmark | mémoire utilisée

        // étape 5 : verification de la profondeur du noeud
        // une fois l'hypothèse meilleur noeud vérifiée et donc
        // potentiellement enregistrée, on vérifie si l'on a
        // atteint la profondeur max de l'arbre
        // SI atteinte ALORS RETOUR au PERE
        if(entier == objets.size() - 1) {
            noeudVerifie = true;
            return;
        }

        // étape 6 : ajout d'un premier noeud fils GAUCHE
        // une fois tous les traitements effectués, on créé l'enfant
        // de gauche, descendant, en lui donnant comme liste d'objets
        // la même que celle du PERE
        this.setSousArbreGauche(new Arbre(this.objets));
        getSousArbreGauche().remplirArbre(objets,false, entier + 1, poidsMax, node);
        nombreNoeudsCrees++;

        // DEBUT benchmark | mémoire utilisée
        usageMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        setUsageMemMax(usageMem);
        // FIN benchmark | mémoire utilisée

        // étape 7 : ajout d'un premier noeud fils DROIT
        // une fois le noeud de gauche créé, on passe au noeud droit,
        // ascendant, à qui on donne la même liste, à laquelle il ajoutera
        // l'objet suivant dans la liste d'objets

        // OPTIMISATION 1 : évaluation de l'arbre supérieur suivant
        // on ne crée un noeud droit (supérieur) que si son hypothétique poids ne dépasse pas le poids max
        // gain de performance énorme car on gagne du TEMPS pour avancer dans les branches plus vite
        // améliore la complexité en TEMPS
        if(this.getPoidsSuivant(objets, entier) + this.getWeight() <= poidsMax) {
            this.setSousArbreDroit(new Arbre(this.objets));
            getSousArbreDroit().remplirArbre(objets, true, entier + 1, poidsMax, node);
            nombreNoeudsCrees++;

            // DEBUT benchmark | mémoire utilisée
            usageMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            setUsageMemMax(usageMem);
            // FIN benchmark | mémoire utilisée
        }

        // étape 8 : mise au rebut des sous arbres
        // une fois le traitement des deux sous arbres terminés,
        // (arbres créés, puis signalés traité), on les met au rebut
        // (le GarbageCollector se chargera de vider la mémoire allouée)
        // afin de limiter l'espace mémoire

        // OPTIMISATION 2 : suppression en mémoire des branches déjà analysées
        // une fois les deux fils vérifiés, il n'y a plus lieu de les conserver dans l'arbre
        // gain de performance énorme car on gagne de l'ESPACE pour créer de nouveau noeuds
        // améliore la complexité en ESPACE
        if(sousArbreDroit != null && sousArbreGauche != null) {
            if (sousArbreDroit.noeudVerifie && sousArbreGauche.noeudVerifie) {
                sousArbreDroit = sousArbreGauche = null;
                nombreNoeudsSupprimes++;
                nombreNoeudsSupprimes++;
            }
        }
    }

    private float getPoidsSuivant(LinkedList<Objet> objets, int entier){
        return objets.get(entier+1).getWeight();
    }

    // fonctions de récupération des valeurs clés
    private float getBorneSup(LinkedList<Objet> objets, int entier, float val) {
        for(int i = entier +1; i < objets.size(); ++i){
            val += objets.get(i).getValue();
        }
        return val;
    }

    float getWeight() {
        float weight = 0;
        for(Objet objet: this.getObjets()){
            weight += objet.getWeight();
        }
        return weight;
    }

    float getValue(){
        float value = 0;
        for (Objet objet : this.getObjets()) {
            value += objet.getValue();
        }
        return value;
    }
}