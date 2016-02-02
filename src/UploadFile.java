import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;


public class UploadFile {

	public HashMap<String,Vector<Integer>> matrice;
	static public String []dictionnaire = {"Argousin","atour","babillard","badauderie","bailler","bancroche",
		"barguigner","bath","bejaune","billevesee","brimborion","brocard","brune","cagoterie","capon",
		"carabistouille","caraco","cautele","chemineau","clampin","coquecigrue","debagouler","deduit",
		"derechef","diantre !","s'ebaudir",	"s'esbigner","etalier","faix","faquin","fesse-mathieu","fi !",
		"fla-fla","flambard","flandrin","fortifs","gandin","genitoires","goguenardisexx","gommeux",
		"goualante","gourgandine","gourme","grimaux","geux - geuse - geuserie","hommasse","huis",
		"icelui - icelle","jean-foutre","jocrisse","jouvenceau","lupanar","macache","mafflu","manant","metin",
		"matutinal","melliflu","mirliflore","momerie","moult","nasarde","nenette","nitescence",
		"s\'opinietrer","patache","pauvresse - pauvret","peccamineux","pekin","pendard","peronnelle",
		"petuner","potiner","potron-minet","priapee","purotin","e quia","radeuse","rastaquouere","ribote",
		"robin","rufian","saperlipopette !","sapience","scrogneugneu","seoir","septentrion","subsequemment",
		"suivez-moi-jeune-homme","tire-laine","toquer","torche-cul","tranche-montagne","trotte-menu",
		"turlutaine","valetudinaire","venette","vetille-vetiller-vetilleux-vetillard","vit","y"};

		
	public UploadFile(){
		matrice = new HashMap<String,Vector<Integer>>();
	}

	public double[][] trie(double[] p){

		double[][] tab = new double[p.length][2];
		ArrayList<Double> r = new ArrayList<Double>();
		ArrayList<Double> t = new ArrayList<Double>();
		int indice;
		for(int i=0; i<p.length; i++){
			r.add(p[i]);
			t.add(p[i]);
		}
		Collections.sort(t);
		for(int i=0;i<t.size(); i++){
			indice = r.indexOf(t.get(i));
			tab[i][0] = indice;
			tab[i][1] = t.get(i);
			r.set(indice, -1.0);
		}

		return tab;
	}

	public void remplirFichier(double[][] p,File f) throws IOException{

		FileWriter fw = new FileWriter (f);
		Random r = new Random();
		for(int i=0;i<dictionnaire.length; i++){
			matrice.put(dictionnaire[i], new Vector<Integer>());
		}
		for(Entry<String, Vector<Integer>> entry : matrice.entrySet()) {
			String cle = entry.getKey();
			Vector<Integer> valeur = entry.getValue();
			System.out.println(" Key : "+cle+"size tableau "+valeur.size());
		}

		/* On donne pour chaque sommet du graphe 5 mots aléatoirement SANS DOUBLANT */
		int rang,j;
		for(int i=0; i<p.length;i++){
			j=0;
			while(j < 5){
				rang  = r.nextInt(100);
				if(!matrice.get(dictionnaire[rang]).contains((int) p[i][0])){
					matrice.get(dictionnaire[rang]).add((int) p[i][0]);
					j++;
				}
			}
		}
		
		String ligne;
		for(Entry<String, Vector<Integer>> entry : matrice.entrySet()) {
			ligne="";
			String cle = entry.getKey();
			Vector<Integer> valeur = entry.getValue();
		    ligne = cle+"   ";
		    for(int i=0; i<valeur.size(); i++){
		    	ligne += valeur.get(i)+"/";
		    }
		    ligne+="\n";
		    fw.write(ligne);
		    ligne="";
			
		}
		fw.close();


	}	

	public static void main(String[] args) throws InterruptedException, IOException {

		if(args.length < 3){
			System.out.println(" Missing arguments .... \n"
					+ " java UploadFile \'lien du Fichier\' \' Nombre de noeud\' \' Nombre d'aretes\' ");
		}else{
			UploadFile instance = new UploadFile();
			Scanner sc = new Scanner(System.in);
			int nbrNoeud = Integer.valueOf(args[1]);
			Fichier f = new Fichier(args[0],Integer.valueOf(args[2]),nbrNoeud);
			System.out.print("Entrer un nom de fichier ou vous voulez charger vos donnees :");
			String nomFichier = sc.nextLine();
			File fichier = new File (nomFichier);

			/* Parser le fichier et le mettre dans une Matrice code en C,I et L */
			Matrice m = Parseur.parser(f);
			double []T = new double[nbrNoeud];

			/* Vecteur de distibution de probalbilite */
			for(int u=0;u<T.length;u++)
				T[u] = (double)1/nbrNoeud;

			/* Execution de l'algorithme PageRank  */
			System.out.println("***** PageRank ******");
			double []p = Matrice.pageRankZero(m, T,0.001,1500); 
			double[][]tab = new double[p.length][2];

			System.out.println("***** Trie des donnees ******");
			/* On met le resultat de PageRank dans une matrice triee de 2 colonnes ( chaque Noeud et son PageRank ) */
			tab = instance.trie(p);

			System.out.println("***** Ecriture du fichier ******");
			/* On ecrit dans ecrit dans le fichier pour chaque mot les noeuds qui contiennent ce mot */
			instance.remplirFichier(tab,fichier);


			System.out.println("Le fichier "+nomFichier+" est charge ...");

		}
	}
}
