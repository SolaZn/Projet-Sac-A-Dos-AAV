package items;

import java.util.LinkedList;

public class Arbre {
    private LinkedList<Objet> objets;
    private Arbre sousArbreDroit, sousArbreGauche;
    private static float BorneInf;
    private boolean noeudVerifie;

    //benchmark
    public static int nombreNoeudsCrees = 1;
    public static int nombreNoeudsSupprimes = 1;

    public static long usageMemMax = 0;
    public static long coutParNoeudAsc = 0;
    public static long coutParNoeudDsc = 0;

    //test optimisation; donner un arbre quasi vide
    Arbre getStrippedTree(){
        return new Arbre(this.objets);
    }

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

    public void setObjet(Objet objet) {
        if(objets == null){
            this.objets = new LinkedList<>();
        }
        this.objets.add(objet);
    }

    // opti
    protected void changeObjetList(LinkedList<Objet> objets){
        this.objets = objets;
    }

    protected boolean getnoeudVerifie(){
        return this.noeudVerifie;
    }

    // benchmark
    public void setUsageMemMax(long usageMem){
        if(usageMem > usageMemMax){
            usageMemMax = usageMem;
        }
    }

    private void setCoupParNoeud(long usageMem, boolean isAscendante) {
        if(isAscendante){
            coutParNoeudAsc = usageMem / nombreNoeudsCrees;
        }else
            coutParNoeudDsc = usageMem / nombreNoeudsCrees;
    }

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

    public LinkedList<Objet> getObjets() {
        return objets;
    }

    public void remplirArbre(LinkedList<Objet> objets, boolean isAscendante, int entier, float poidsMax, NoeudOptimal it){
        long usageMem = 0;
        if (isAscendante) {
            this.setObjet(objets.get(entier));
        }
        else{
            usageMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            setCoupParNoeud(usageMem,false);
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
        it.addPotentialNode(this);
        noeudVerifie = true;

        if (isAscendante) {
            usageMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            setCoupParNoeud(usageMem,true);
        }
        else{
            usageMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            setCoupParNoeud(usageMem,false);
        }

        if(entier == objets.size() - 1) {
            noeudVerifie = true;
            return;
        }

        this.setSousArbreGauche(new Arbre(this.objets));
        getSousArbreGauche().remplirArbre(objets,false, entier + 1, poidsMax, it);
        nombreNoeudsCrees++;
        usageMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        setUsageMemMax(usageMem);

        // on ne crée un noeud droit (supérieur) que si son hypothétique poids ne dépasse pas le poids max
        // gain de performance
        if(this.getPoidsSuivant(objets, entier) + this.getWeight() <= poidsMax) {
            this.setSousArbreDroit(new Arbre(this.objets));
            getSousArbreDroit().remplirArbre(objets, true, entier + 1, poidsMax, it);
            nombreNoeudsCrees++;

            usageMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            setUsageMemMax(usageMem);
        }
        if(sousArbreDroit != null && sousArbreGauche != null) {
            if (sousArbreDroit.noeudVerifie && sousArbreGauche.noeudVerifie) {
                sousArbreDroit = sousArbreGauche = null;
                nombreNoeudsSupprimes++;
                nombreNoeudsSupprimes++;
            }
        }
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

    private float getBorneSup(LinkedList<Objet> objets, int entier, float val) {
        for(int i = entier +1; i < objets.size(); ++i){
            val += objets.get(i).getValue();
        }
        return val;
    }

    private float getPoidsSuivant(LinkedList<Objet> objets, int entier){
        return objets.get(entier+1).getWeight();
    }
}