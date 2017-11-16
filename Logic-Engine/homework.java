import java.io.*;
import java.util.*;

public class homework{
	public static void main(String[] args) throws IOException{
		BufferedReader br=new BufferedReader(new FileReader("./input.txt"));
		int queriesCount=Integer.parseInt(br.readLine());
		String[] queries=new String[queriesCount];
		for(int i=0;i<queriesCount;i++){
			queries[i]=br.readLine();
			System.out.println(queries[i]);
		}
		int KBCount=Integer.parseInt(br.readLine());
		String[] KB=new String[KBCount];
		for(int j=0;j<KBCount;j++){
			KB[j]=br.readLine();
			System.out.println(KB[j]);
		}
	}

	public static void uni(){

	}

	public static void resolve(){
		
	}
}
