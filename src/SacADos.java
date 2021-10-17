import java.util.LinkedList;

public class SacADos {
    private final String chemin;
    private LinkedList<Objet> objets;
    private float poidsMaximal;

    public SacADos(){
        chemin = null;
        objets = null;
    }

    public SacADos(String chemin, float poidsMaximal){
        this.chemin = chemin;
        this.objets = new LinkedList<>();
        this.poidsMaximal = poidsMaximal;
    }

    public float getPoidsMaximal(){
        return poidsMaximal;
    }

    public float getPoids(){
        float poids = 0;
        for(Objet objet: this.objets){
            poids += objet.getWeight();
        }
        return poids;
    }

    public float getValeur(){
        float val = 0;
        for(Objet objet: this.objets){
            val += objet.getValue();
        }
        return val;
    }

    public LinkedList<Objet> getObjets() {
        return objets;
    }

    public void addObjet(Objet objet) {
        this.objets.add(objet);
    }

    public void setObjets(LinkedList<Objet> objets){
        this.objets = objets;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Objet objet: objets){
            sb.append(objet.toString()).append("\n");
        }
        sb.append("Le poids du sac est de: ").append(getPoids()).append("\n");
        sb.append("La valeur du sac est de: ").append(getValeur());
        return sb.toString();
    }

    public void setPoidMax(float v) {
        poidsMaximal = v;
    }
}
