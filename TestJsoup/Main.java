import java.io.IOException;
import java.util.Scanner;



public class Main {

	public static void main(String [] args) throws IOException{
		String request = null;
		Scanner sc = new Scanner(System.in);
		System.out.println("Can you enter your request, please?");
		request = sc.next();

		Browser bw = new Browser(request);
		
		System.out.println("**************GOOGLE***************************");
		bw.actionGoogle();
		
		System.out.println("**************YAHOO***************************");
		bw.actionYahoo();
		
		System.out.println("**************BING***************************");
		bw.actionBing();
		
		System.out.println("**************FINAL RESULT***************************");
		
		
		bw.Borda(3, 100);
		bw.sort();
		
		bw.display();
	}

}
