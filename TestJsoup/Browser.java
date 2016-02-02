import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Browser {
	private Document doc = null;
	private String request;
	private int googleCount = 1;
	private ArrayList<String> googleHash;
	private ArrayList<String> yahooHash;
	private ArrayList<String> bingHash;
	private HashMap<String,Integer> results;
	Comparator<Integer> valueComparator ;
	Map<String,Integer> sortedOnValuesMap; 

	public Browser(String request){
		this.googleHash = new ArrayList<String>();
		this.yahooHash = new ArrayList<String>();
		this.bingHash = new ArrayList<String>();
		this.results = new HashMap<String,Integer>();
		this.request = request;
		valueComparator = new Comparator<Integer>() {
			@Override
			public int compare(Integer s1, Integer s2) {
				return s1.compareTo(s2);
			}
		};

	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public void sort(){

		MapValueComparator<String, Integer> mapComparator = new MapValueComparator<String,Integer>(results, valueComparator);
		sortedOnValuesMap = new TreeMap<String,Integer>(mapComparator);
		sortedOnValuesMap.putAll(results);
	}

	public void Borda(int nbMoteur, int nbResult){

		/*   ON MET LES RESULTATS DE GOOGLE DANS Le RESULTAT FINAL  */
		for (int i=0; i<nbResult;i++){
			results.put(googleHash.get(i),(i+1)+(nbMoteur-1)*(nbResult+1));
		}
		for (int i=0; i<nbResult; i++){
			if (results.containsKey(bingHash.get(i))){
				//On enlve le n+1 de Bing
				results.put(bingHash.get(i),(results.get(bingHash.get(i))-(nbMoteur-2)*(nbResult+1)+i+1));
			}
			else{ 
				results.put(bingHash.get(i), (nbMoteur-1)*(nbResult+1)+i+1);
			}
		}
		for (int i=0; i<nbResult; i++){
			if (results.containsKey(yahooHash.get(i))){
				//On enlve le n+1 de Yahoo
				results.put(yahooHash.get(i),(results.get(yahooHash.get(i))-(nbMoteur-3)*(nbResult+1)-(nbResult+1)+i+1));
			}
			else {
				results.put(yahooHash.get(i), (nbMoteur-1)*(nbResult+1)+i+1);
			}
		}
	}

	public void display(){

		Set<String> keys = sortedOnValuesMap.keySet();
		Iterator<String> it = keys.iterator();
		int i=1;
		while (it.hasNext()){
			System.out.println(i+++" : "+it.next());
		}
	}
	public void displayLink(String link) throws IOException{
		String site = null;
		Document doc = Jsoup.connect(link).userAgent("Mozilla").timeout(20000).get();
		for(Element result : doc.select("cite")){
			if(result.text().contains(".") && !result.text().contains("google"))
				if(result.text().contains("https")){
					site = result.text().substring(8);
				}else if(result.text().contains("http")){
					site = result.text().substring(7);
				}else{
					site = result.text();
				}
			if(site.endsWith("/")){
				site = site.substring(0, site.length()-1);
			}
			if(site.startsWith("www.")){
				site = site.substring(4);
			}
			googleHash.add(site);

			System.out.println("Google "+googleCount+++": "+site);
			if(googleCount > 100)
				break;
		}
	}

	public void actionGoogle() throws IOException{
		String site = null;
		doc = Jsoup.connect("http://www.google.fr/search?q="+request).userAgent("Mozilla").timeout(20000).get();
		for(Element link : doc.select("cite")){
			if(link.text().contains(".")){
				if(link.text().contains("https")){
					site = link.text().substring(8);
				}else if(link.text().contains("http")){
					site =link.text().substring(7);
				}else{
					site = link.text();
				}
			}
			if(site.endsWith("/")){
				site = site.substring(0, site.length()-1);
			}
			if(site.startsWith("www.")){
				site = site.substring(4);
			}
			googleHash.add(site);
			System.out.println("Google "+googleCount+++": "+site);
			if(googleCount > 100)
				break;
		}


		Elements elem = doc.getElementsByClass("fl");
		for(Element e : elem){
			System.out.println("************************************************************************************");
			displayLink("https://www.google.fr"+e.attr("href"));
		}

	}

	public void actionYahoo() throws IOException{
		String site = null;
		int yahooCount = 1;
		for(int i = 1; yahooCount <= 100; i+=10){
			System.out.println("**********************************************" +
					"*************************************************" +
					"******************");
			Document doc = Jsoup.connect("https://fr.search.yahoo.com/search;_ylt=A9mSs" +
					"3LYQfBUpkoAIgBjAQx.?p="+request+"&b="+i).
					userAgent("Mozilla").timeout(20000).get();
			Elements elem = doc.getElementsByClass("yschttl");
			for(Element link : elem){
				if(link.attr("href").contains(".")){	
					if(link.attr("href").contains("https")){
						site = link.attr("href").substring(8);
					}else if(link.attr("href").contains("http")){
						site = link.attr("href").substring(7);
					}else{
						site = link.attr("href");
					}	
				}
				if(site.endsWith("/")){
					site = site.substring(0, site.length()-1);
				}
				if(site.startsWith("www.")){
					site = site.substring(4);
				}

				yahooHash.add(site);
				System.out.println("Yahoo" +yahooCount+++": "+site);
				if(yahooCount > 100)
					break;
			}


		}
	}

	public void actionBing() throws IOException{
		String site = null;
		int bingCount = 0;
		for(int i = 1;bingCount <= 100; i+=10){
			System.out.println("***************************************************************************************");
			Document doc = Jsoup.connect("http://www.bing.com/search?q="+request+"&amp;first=21&first="+i).
					userAgent("Mozilla").timeout(20000).get();
			for	(Element link :doc.select("cite")){
				if(link.text().contains(".") && !link.text().contains("bing.com")){	
					if(!link.text().contains(" ")){
						if(link.text().contains("https")){
							site = link.text().substring(8);
						}else if(link.text().contains("http")){
							site = link.text().substring(7);
						}else{
							site = link.text();
						}
					}
				}
				if(site.endsWith("/")){
					site = site.substring(0,site.length()-1);
				}
				if(site.startsWith("www.")){
					site = site.substring(4);
				}
				if(!bingHash.contains(site)){
					bingHash.add(site);
					System.out.println("Bing "+bingCount+++"-"+site);
				}
				if(bingCount > 100)
					break;
			}
		}
	}

	public ArrayList<String> getGoogleHash() {
		return googleHash;
	}


}
