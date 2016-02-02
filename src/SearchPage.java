import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class SearchPage {


	public static HashMap<String,ArrayList<Integer>> matrice = new HashMap<String,ArrayList<Integer>>();

	public static void uploadMatrice(String nameFile){


		try{
			InputStream ips=new FileInputStream(nameFile); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			String mot="";
			String s[];
			ArrayList<Integer> noeuds;
			while ((ligne=br.readLine())!=null){
				noeuds = new ArrayList<Integer>();
				s = ligne.split("   ");
				mot = s[0];
				s = s[1].split("/");
				for(int i=0;i<s.length;i++){
					noeuds.add(Integer.valueOf(s[i]));
				}
				matrice.put(mot, noeuds);
			}
			br.close(); 
		}
		catch (Exception e){
			System.out.println(e.toString());
		}

	}
	
	 public <T> List<T> intersection(List<T> list1, List<T> list2) {
	        List<T> list = new ArrayList<T>();

	        for (T t : list1) {
	            if(list2.contains(t)) {
	                list.add(t);
	            }
	        }

	        return list;
	    }

	public static void main(String[] args) {

		if(args.length < 1){
			System.out.println(" Missing arguments .... \n"
					+ " java SearchPage \'nomFichier\'");
		}else{
			System.out.println("Chargement du Fichier .......");
			uploadMatrice(args[0]);
			System.out.println("Chargement du Fichier Terminé !!!! \n\n ");
			while( true){
				Scanner sc = new Scanner(System.in);
				System.out.print(" Entrer un mot :");
				String s = sc.nextLine();
				if(matrice.containsKey(s)){
					for(int k=0; k<matrice.get(s).size(); k++){
						System.out.println("Page "+k+" : "+matrice.get(s).get(k));
					}
					System.out.println(" Le nombre de page trouve pour le mot "+s+" est :"+matrice.get(s).size());
				}else
					System.out.println(" On a pas trouve le mot que vous avez rentre");



			}

		}
	}

}
