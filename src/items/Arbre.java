package items;

import java.util.LinkedList;

public class Arbre {
    private LinkedList<Objet> objets;
    private Arbre sousArbreDroit, sousArbreGauche;
    private static float BorneInf;
    private boolean noeudVerifie;

    // variables globales de benchmark
    public static int nombreNoeudsCrees = 1;
    public static int nombreNoeudsSupprimes = 1;

    public static long usageMemMax = 0;
    public static float coutParNoeud = 0;

    // constructeurs
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
    public void setObjet(Objet objet) {
        if(objets == null){
            this.objets = new LinkedList<>();
        }
        this.objets.add(objet);
    }

    public LinkedList<Objet> getObjets() {
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
    public Arbre getSousArbreDroit() {
        return sousArbreDroit;
    }

    public Arbre getSousArbreGauche() {
        return sousArbreGauche;
    }

    public void setSousArbreDroit(Arbre sousArbreDroit) {
        this.sousArbreDroit = sousArbreDroit;
    }

    public void setSousArbreGauche(Arbre sousArbreGauche) {
        this.sousArbreGauche = sousArbreGauche;
    }

    // fonctions de construction de l'arbre
    public void remplirArbre(LinkedList<Objet> objets, boolean isAscendante, int entier, float poidsMax, NoeudOptimal node){
        long usageMem;
        if (isAscendante) {
            this.setObjet(objets.get(entier));
        }

        //Verification du poids
        float weight = getWeight();
        if(weight > poidsMax) {
            noeudVerifie = true;
            return;
        }

        //Verification Borne Inférieur
        float val = 0;
        for(Objet objet: this.getObjets()){
            val += objet.getValue();
        }
        if(val > Arbre.BorneInf) Arbre.BorneInf = val;

        //Verification Borne Supérieur
        val = getBorneSup(objets, entier, val);
        if(val < Arbre.BorneInf){
            noeudVerifie = true;
            return;
        }

        //Verification si le noeud est un possible meilleur noeud
        node.addPotentialNode(this);
        noeudVerifie = true;

        usageMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        setCoutParNoeud(usageMem);

        if(entier == objets.size() - 1) {
            noeudVerifie = true;
            return;
        }

        this.setSousArbreGauche(new Arbre(this.objets));
        getSousArbreGauche().remplirArbre(objets,false, entier + 1, poidsMax, node);
        nombreNoeudsCrees++;
        usageMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        setUsageMemMax(usageMem);

        // OPTIMISATION 1 : évaluation de l'arbre supérieur suivant
        // on ne crée un noeud droit (supérieur) que si son hypothétique poids ne dépasse pas le poids max
        // gain de performance énorme car on gagne du TEMPS pour avancer dans les branches plus vite
        // améliore la complexité en TEMPS
        if(this.getPoidsSuivant(objets, entier) + this.getWeight() <= poidsMax) {
            this.setSousArbreDroit(new Arbre(this.objets));
            getSousArbreDroit().remplirArbre(objets, true, entier + 1, poidsMax, node);
            nombreNoeudsCrees++;

            usageMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            setUsageMemMax(usageMem);
        }

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

    private float getBorneSup(LinkedList<Objet> objets, int entier, float val) {
        for(int i = entier +1; i < objets.size(); ++i){
            val += objets.get(i).getValue();
        }
        return val;
    }
}