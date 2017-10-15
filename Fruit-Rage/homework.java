import java.io.*;
import java.util.*;

public class homework{
	static int N=0; //Board Size
	static int P=0; //Fruit Types
	static float T=0;
	public static void main(String[] args) throws IOException{
		

		BufferedReader br=new BufferedReader(new FileReader("./input.txt"));
		N=Integer.parseInt(br.readLine());
		P=Integer.parseInt(br.readLine());
		T=Float.parseFloat(br.readLine());
		System.out.println(N);

		char[][] InitState=new char[N][N];
		String row=null;
		for(int i=0;i<N;i++){
			row=br.readLine();
			for(int j=0;j<N;j++){
				InitState[i][j]=row.charAt(j);
			}
		}
		br.close();
	}
}
