package items;

public final class Objet {
    private final String name;
    private float weight;
    private final float value;

    // constructeur
    public Objet(String name, float weight, float value){
        this.name = name;
        this.weight = weight;
        this.value = value;
    }

    // fonction de comparaison entre deux objets
    public int smallerThan(Objet objet){
        float tmp = this.value/this.weight;
        float tmp1 = objet.value/objet.weight;
        if(tmp < tmp1){
            return 1;
        } else if(tmp == tmp1){
            return 0;
        } else {
            return -1;
        }
    }

    // fonctions de récupération des valeurs clés
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
