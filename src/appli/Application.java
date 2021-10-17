package appli;

import items.Arbre;
import items.NoeudOptimal;
import items.Objet;
import items.SacADos;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class Application {
    private static long debut = 0;
    private static long fin = 0;

    public static LinkedList<Objet> lireFichier(String chemin){
        LinkedList<Objet> objets = new LinkedList<>();
        try
        {
            FileInputStream file = new FileInputStream(chemin);
            Scanner scan = new Scanner(file);
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
            System.exit(1);
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
        debut = System.nanoTime();

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

        fin = System.nanoTime();

        System.out.println(sac);
    }

    public static void dynamique(SacADos sac, LinkedList<Objet> objets){
        debut = System.nanoTime();

        for(Objet objet:objets){
            objet.setWeight(objet.getWeight()*10);
        }

        sac.setPoidsMaximal(sac.getPoidsMaximal() * 10);

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
        sac.setPoidsMaximal(sac.getPoidsMaximal() / 10);

        fin = System.nanoTime();

        System.out.println(sac);
    }

    public static void PSE(SacADos sac, LinkedList<Objet> objets){
        debut = System.nanoTime();

        Arbre racine = new Arbre();
        NoeudOptimal noeudOptimal = new NoeudOptimal();

        Arbre.initBorneInf();
        racine.remplirArbre(objets, false, -1, sac.getPoidsMaximal(), noeudOptimal);

        sac.setObjets(noeudOptimal.getOptimalNode());

        fin = System.nanoTime();

        System.out.println(sac);
        System.out.println(Arbre.nombreNoeuds + " noeud(s) créés dans l'arbre");
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        sc.next();
        String chemin = sc.next();
        float poidsMaximal = Float.parseFloat(sc.next());
        String methode = sc.next();
        SacADos sac = new SacADos(poidsMaximal);
        LinkedList<Objet> objets = lireFichier(chemin);

        switch (methode){
            case("gloutonne") :
                gloutonne(sac, objets);
                break;
            case("dynamique"):
                dynamique(sac, objets);
                break;
            case("PSE"):
                PSE(sac, objets);
                break;
            default:
                System.exit(0);
        }
        long duree = (fin - debut);
        System.out.println("Temps d'exécution : " + duree + "ns" + "\n\t\t\t\t    " + duree / (double) 1000000 + "ms" + "\n\t\t\t\t    " + duree / (double) 1000000000 + "s");
    }
}
