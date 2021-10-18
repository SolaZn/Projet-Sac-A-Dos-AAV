package items;

import java.util.LinkedList;

public class NoeudOptimal {
    private Arbre noeudOptimal;

    // constructeur
    public NoeudOptimal(){
        noeudOptimal = new Arbre();
    }

    // fonctions de gestion du noeud parfait actuel
    public void copyNode(LinkedList<Objet> objets){
        // GARDE-FOU : copier la liste plutôt que la passer en référence
        // on copie le node/noeud pour éviter de tomber sur la liste d'un
        // node supprimé et donc déréférencé et potentiellement
        // d'une liste balayée par le garbageCollector

        noeudOptimal.getObjets().clear();
        noeudOptimal.getObjets().addAll(objets);
    }

    public LinkedList<Objet> getOptimalNode(){
        return this.noeudOptimal.getObjets();
    }

    // fonction de découverte d'un potentiel nouveau noeud parfait
    public void addPotentialNode(Arbre noeudPotentiel){
            if(noeudPotentiel.getValue() > this.noeudOptimal.getValue()){
                copyNode(noeudPotentiel.getObjets());
            }else if(noeudPotentiel.getValue() == this.noeudOptimal.getValue()) {
                if (noeudPotentiel.getWeight() < this.noeudOptimal.getWeight()) {
                    copyNode(noeudPotentiel.getObjets());
                }
            }
    }
}
