package items;


import java.util.LinkedList;

public class SacADos {
    private LinkedList<Objet> objets;
    private float poidsMaximal;
    private static int nombreItemsTotal = 0;


    public SacADos(){
        objets = null;
    }

    public SacADos(float poidsMaximal){
        this.objets = new LinkedList<>();
        this.poidsMaximal = poidsMaximal;
    }

    public static void setNombreItemsTotal(int nombreItemsTotal) {
        SacADos.nombreItemsTotal = nombreItemsTotal;
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
        sb.append("Contenu du sac :\n\n");
        for (Objet objet: objets) {
            sb.append(objet.toString()).append("\n");
        }
        sb.append("\nPoids maximum à respecter : ").append(poidsMaximal).append(" kilogrammes\n");
        sb.append("Poids total : ").append(getPoids()).append(" kilogrammes\n\n");
        sb.append("Nombre d'items à ranger : ").append(nombreItemsTotal).append(" items\n");
        sb.append("Nombre d'items rangés : ").append(objets.size()).append(" items\n");
        sb.append("Valeur totale : ").append(getValeur()).append("€\n");
        return String.valueOf(sb);
    }

    public void setPoidsMaximal(float poids) {
        poidsMaximal = poids;
    }
}
