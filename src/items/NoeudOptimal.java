package items;

import java.util.LinkedList;

public class NoeudOptimal {
    private Arbre noeudOptimal;

    public NoeudOptimal(){
        noeudOptimal = new Arbre();
    }

    // on copie le node pour éviter de tomber sur la liste d'un
    // node supprimé et donc déréférencé et potentiellement
    // d'une liste balayée par le garbageCollector
    public void copyNode(LinkedList<Objet> objets){
        noeudOptimal.getObjets().clear();
        noeudOptimal.getObjets().addAll(objets);
    }

    public LinkedList<Objet> getOptimalNode(){
        return this.noeudOptimal.getObjets();
    }

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
