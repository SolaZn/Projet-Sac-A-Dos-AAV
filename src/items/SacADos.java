package items;

import java.text.DecimalFormat;
import java.util.LinkedList;

public class SacADos {
    private LinkedList<Objet> objets;
    private float poidsMaximal;

    // fonctions globales de benchmark et de formatage
    private static int nombreItemsTotal = 0;
    public static final DecimalFormat df = new DecimalFormat("#.#");

    public static void setNombreItemsTotal(int nombreItemsTotal) {
        SacADos.nombreItemsTotal = nombreItemsTotal;
    }

    // fonction globale de changement de format de poids pour tout type de liste d'objets
    // (que ce soit des sacs implicites ou explicites)
    public static void setWeight(String mode, LinkedList<Objet> objets) {
        if(mode.equals("etendre")){ // passer les poids décimaux en poids entiers "exacts"
            for(Objet objet:objets){
                objet.setWeight(objet.getWeight()*10);
            }
        }else if(mode.equals("reduire")){ // revenir aux valeurs décimales
            for(Objet objet: objets){
                objet.setWeight(objet.getWeight()/10);
            }
        }

    }

    // constructeurs
    public SacADos(){
        objets = null;
    }

    public SacADos(float poidsMaximal){
        this.objets = new LinkedList<>();
        this.poidsMaximal = poidsMaximal;
    }

    // fonctions de récupération des valeurs clés du sac
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

    // fonction de gestion du contenu du sac
    public void addObjet(Objet objet) {
        this.objets.add(objet);
    }

    public void setObjets(LinkedList<Objet> objets){
        this.objets = objets;
    }

    public void setPoidsMaximal(float poids) {
        poidsMaximal = poids;
    }

    // fonction d'affichage du contenu du sac
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nLe sac à dos contient :\n");
        for (Objet objet: objets) {
            sb.append("  | ");
            sb.append(objet.toString()).append("\n");
        }
        sb.append("\nPoids maximum à respecter : ").append(poidsMaximal).append(" kilogrammes\n");
        sb.append("Poids total : ").append(df.format(getPoids())).append(" kilogrammes\n");
        sb.append("Nombre d'items à ranger : ").append(nombreItemsTotal).append(" items\n");
        sb.append("Nombre d'items rangés : ").append(objets.size()).append(" items\n");
        sb.append("Valeur totale : ").append(df.format(getValeur())).append("€\n");
        return String.valueOf(sb);
    }

}
