package items;

import java.util.Comparator;

public final class Objet {
    private final String name;
    private float weight;
    private final float value;
    private final float WVratio; // Weight/Value ratio

    // comparateur : utilise la valeur du WRatio pour comparer
    public static final Comparator<Objet> parRatio = Comparator.comparing(Objet::getWVratio);

    // constructeur
    public Objet(String name, float weight, float value){
        this.name = name;
        this.weight = weight;
        this.value = value;
        this.WVratio = value / weight;
    }

    // fonctions de récupération des valeurs clés
    private float getWVratio() {
        return WVratio;
    }

    public float getWeight() {
        return this.weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getValue() {
        return value;
    }

    // fonction d'affichage des propriétés de l'objet
    @Override
    public String toString() {
        return this.name + " ; " + this.weight + " ; " + this.value;
    }
}
