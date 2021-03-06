package items;

import java.util.LinkedList;

public class Arbre {
    private LinkedList<Objet> objets;
    private Arbre sousArbreDroit, sousArbreGauche;
    private static float BorneInf;
    public static int nombreNoeuds = 0;

    public Arbre(){
        objets = new LinkedList<>();
        sousArbreDroit = sousArbreGauche = null;
    }

    public Arbre(LinkedList<Objet> objetsPere){
        this.objets = new LinkedList<>();
        this.objets.addAll(objetsPere);
        sousArbreDroit = sousArbreGauche = null;
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
        if (isAscendante) {
            this.setObjet(objets.get(entier));
        }

        //Verification du poids
        float weight = getWeight();
        if(weight > poidsMax) return;

        //Verification Borne Inférieur
        float val = 0;
        for(Objet objet: this.getObjets()){
            val += objet.getValue();
        }
        if(val > Arbre.BorneInf) Arbre.BorneInf = val;

        //Verification Borne Supérieur
        val = getBorneSup(objets, entier, val);
        if(val < Arbre.BorneInf) return;

        //Verification si le noeud est un possible meilleur noeud
        it.addPotentialNode(this);

        if(entier == objets.size() - 1) return;

        this.setSousArbreGauche(new Arbre(this.objets));
        getSousArbreGauche().remplirArbre(objets,false, entier + 1, poidsMax, it);
        nombreNoeuds++;

        // on ne crée un noeud droit (supérieur) que si son hypothétique poids ne dépasse pas le poids max
        // gain de performance
        if(this.getPoidsSuivant(objets, entier) + this.getWeight() <= poidsMax) {
            this.setSousArbreDroit(new Arbre(this.objets));
            getSousArbreDroit().remplirArbre(objets, true, entier + 1, poidsMax, it);
            nombreNoeuds++;
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