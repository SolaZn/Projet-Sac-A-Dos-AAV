package appli;

import items.Arbre;
import items.NoeudOptimal;
import items.Objet;
import items.SacADos;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;

public class Application {
    // variables globales de benchmark
    private static long debut = 0;
    private static long fin = 0;

    // variables globales d'affichage
    private static final String GLOU = "gloutonne";
    private static final String DYNA = "dynamique";
    private static final String PSE = "pse";
    private static final String MSG_LANCEMENT = "Calcul lancé...";
    private static final String MSG_FIN = "Calcul terminé!";

    // fonction de traitement de l'entrée texte
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

    // premier algorithme
    // méthode approximative : GLOUTONNE
    public static void gloutonne(SacADos sac, LinkedList<Objet> objets){
        SacADos.setNombreItemsTotal(objets.size());
        System.out.println(MSG_LANCEMENT);
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

        System.out.println(MSG_FIN);
        System.out.println(sac);
    }

    // deuxième algorithme
    // méthode exacte : DYNAMIQUE (à l'aide d'une matrice et de concepts de programmation dynamique)
    public static void dynamique(SacADos sac, LinkedList<Objet> objets){
        SacADos.setNombreItemsTotal(objets.size());
        System.out.println(MSG_LANCEMENT);
        debut = System.nanoTime();

        // on passe la partie décimale des poids en partie entière pour pouvoir
        // tout prendre en compte
        SacADos.setWeight("etendre",objets);

        sac.setPoidsMaximal(sac.getPoidsMaximal() * 10);

        //TODO: réduire la compexité de cette fonction (en découpant encore un peu plus les blocs + documenter !

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

        // on remets les poids en float pour pouvoir les afficher correctement
        SacADos.setWeight("reduire", sac.getObjets());

        sac.setPoidsMaximal(sac.getPoidsMaximal() / 10);

        fin = System.nanoTime();

        System.out.println(MSG_FIN);
        System.out.println(sac);
    }

    // troisième algorithme
    // méthode exacte : PSE (à l'aide d'un Arbre Binare de Recherche et de bornes dans la recherche)
    public static void PSE(SacADos sac, LinkedList<Objet> objets){
        SacADos.setNombreItemsTotal(objets.size());
        System.out.println(MSG_LANCEMENT);
        debut = System.nanoTime();

        Arbre racine = new Arbre();
        NoeudOptimal noeudOptimal = new NoeudOptimal();

        Arbre.initBorneInf();
        racine.remplirArbre(objets, false, -1, sac.getPoidsMaximal(), noeudOptimal);

        sac.setObjets(noeudOptimal.getOptimalNode());

        fin = System.nanoTime();

        System.out.println(MSG_FIN);
        System.out.println(sac);
    }

    // main
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        //traitement de l'entrée
        sc.next();
        String chemin = sc.next();
        float poidsMaximal = Float.parseFloat(sc.next());
        String methode = sc.next();

        // préparation de l'exécution
        SacADos sac = new SacADos(poidsMaximal);
        LinkedList<Objet> objets = lireFichier(chemin);

        // exécution d'un algorithme parmi ceux disponibles
        switch (methode.toLowerCase(Locale.ROOT)){
            case GLOU :
                gloutonne(sac, objets);
                System.out.println("Méthode : gloutonne");
                break;
            case DYNA:
                dynamique(sac, objets);
                System.out.println("Méthode : dynamique");
                break;
            case PSE:
                PSE(sac, objets);
                System.out.println("Méthode : séparation/évaluation PSE");
                // outils de benchmark propres à l'algorithme PSE
                System.out.println(Arbre.nombreNoeudsCrees + " noeud(s) créés dans l'arbre pour trouver le résultat");
                System.out.println(Arbre.nombreNoeudsCrees - Arbre.nombreNoeudsSupprimes + " noeud(s) restants dans l'arbre à la fin du traitement");
                System.out.println(Arbre.nombreNoeudsSupprimes + " noeud(s) mis au rebut par l'algorithme durant la construction");
                System.out.println("Coût mémoire par noeud : " + Arbre.coutParNoeud + " octets");
                System.out.println("Usage mémoire max : " + Arbre.usageMemMax / (double) 1000000 + " MB");
                break;
            default:
                System.exit(0);
        }

        // outil de benchmark du temps d'exécution
        long duree = (fin - debut);
        System.out.println("Temps d'exécution : " + duree + " ns" + "\n\t\t\t\t    "
                + duree / (double) 1000000 + " ms" + "\n\t\t\t\t    " + duree / (double) 1000000000 + " s");
    }
}
