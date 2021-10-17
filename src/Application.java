import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class Application {

    public static LinkedList<Objet> lireFichier(){
        LinkedList<Objet> objets = new LinkedList<>();
        try
        {
            FileInputStream file = new FileInputStream("Objets.txt");
            Scanner scan = new Scanner(file);
            int i = 0;
            while(scan.hasNextLine())
            {
                String s = scan.nextLine();
                String[] objet1 = s.split(" ; ");
                objets.add(new Objet(objet1[0], Float.parseFloat(objet1[1]), Float.parseFloat(objet1[2])));

            }
            scan.close();
        }
        catch(IOException e)
        {
            System.out.println("Le fichier n'existe pas!");
        }
        return objets;
    }

    public static float maximum(float firstValue, float secondValue){
        if(firstValue > secondValue){
            return firstValue;
        } else {
            return secondValue;
        }
    }

    public static void gloutonne(SacADos sac, LinkedList<Objet> objets){
        Objet tmp = null;
        for(int i = 2; i <= objets.size(); i++){
            for(int j = 0; j < objets.size()-1; j++){
                if (objets.get(j+1).smallerThan(objets.get(j)) < 0){
                    objets.add(j+2, objets.get(j));
                    objets.remove(j);
                }
            }
        }
        for(Objet objet: objets){
            if(sac.getPoids() + objet.getWeight() <= sac.getPoidsMaximal()){
                sac.addObjet(objet);
            }
        }
        System.out.println(sac.toString());
    }

    public static void dynamique(SacADos sac, LinkedList<Objet> objets){
        for(Objet objet:objets){
            objet.setWeight(objet.getWeight()*10);
        }
        sac.setPoidMax(sac.getPoidsMaximal() * 10);
        float[][] M= new float[objets.size()][(int)sac.getPoidsMaximal()+1];
        for(int j = 0; j <= sac.getPoidsMaximal(); ++j){
            if(objets.get(0).getWeight() > j){
                M[0][j] = 0;
            } else {
                M[0][j] = objets.get(0).getValue();
            }
            for(int i = 1; i < objets.size(); ++i){
                if(objets.get(i).getWeight() > j){
                    M[i][j] = M[i-1][j];
                } else {
                    M[i][j] = maximum(M[i-1][j], M[i-1][j-(int)objets.get(i).getWeight()]+objets.get(i).getValue());
                }
            }
        }
        int j = (int)sac.getPoidsMaximal();
        int i = objets.size()-1;
        while(M[i][j] == M[i][j-1]){
            --j;
        }
        while(j > 0){
            while((i > 0) && (M[i][j] == M[i-1][j])){
                --i;
            }
            j = j - (int)objets.get(i).getWeight();
            if(j >= 0){
                sac.addObjet(objets.get(i));
            }
            --i;
        }
        for(Objet objet: sac.getObjets()){
            objet.setWeight(objet.getWeight()/10);
        }
        sac.setPoidMax(sac.getPoidsMaximal() / 10);
        System.out.println(sac.toString());
    }

    public static void PSE(SacADos sac, LinkedList<Objet> objets){
        Arbre racine = new Arbre();
        Itérateur it = new Itérateur();
        racine.initBorneInf();
        racine.remplirArbre(objets, false, -1, sac.getPoidsMaximal(), it);
        sac.setObjets(it.getOptimalNode());
        System.out.println(sac);
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        sc.next();
        String chemin = sc.next();
        int poidMaximal = sc.nextInt();
        String methode = sc.next();
        SacADos sac = new SacADos(chemin, poidMaximal);
        LinkedList<Objet> objets = lireFichier();
        switch (methode){
            case("gloutonne") :
                gloutonne(sac, objets);
                break;
            case("dynamique"):
                dynamique(sac, objets);
                break;
            case("PSE"):
                PSE(sac, objets);
        }
    }
}
