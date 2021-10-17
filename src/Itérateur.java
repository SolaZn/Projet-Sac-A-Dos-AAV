import java.util.LinkedList;

public class Itérateur {
    private Arbre noeudOptimal;

    public Itérateur(){
        noeudOptimal = new Arbre();
    }

    public LinkedList<Objet> getOptimalNode(){
        return this.noeudOptimal.getObjets();
    }

    public void addPotentialNode(Arbre noeudPotentiel){
            if(noeudPotentiel.getValue() > this.noeudOptimal.getValue()){
                this.noeudOptimal = noeudPotentiel;
            }else if(noeudPotentiel.getValue() == this.noeudOptimal.getValue()) {
                if (noeudPotentiel.getWeight() < this.noeudOptimal.getWeight()) {
                    this.noeudOptimal = noeudPotentiel;
                }
            }
    }
}
