package items;

public class Objet {
    private String name;
    private float weight;
    private float value;

    public Objet(String name, float weight, float value){
        this.name = name;
        this.weight = weight;
        this.value = value;
    }

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

    @Override
    public String toString() {
        return this.name + " ; " + this.weight + " ; " + this.value;
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
}
