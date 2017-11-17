import java.io.*;
import java.util.*;

public class homework{
	public static void main(String[] args) throws IOException{
		BufferedReader br=new BufferedReader(new FileReader("./input.txt"));
		int queriesCount=Integer.parseInt(br.readLine());
		String[] queries=new String[queriesCount];
		for(int i=0;i<queriesCount;i++){
			queries[i]=br.readLine();
		}
		int KBCount=Integer.parseInt(br.readLine());
		String[] KBArray=new String[KBCount];
		ArrayList<String> KB=new ArrayList<>(KBCount);
		ArrayList<String> newKB=new ArrayList<>(KBCount);
		for(int j=0;j<KBCount;j++){
			KBArray[j]=br.readLine();
			KB.add(KBArray[j]);
			newKB.add(KBArray[j]);
		}

		String a="~Apple(x,y,z)";
		String[] predicate=a.split("\\(|,|\\)");
		if(a.charAt(0)=='~'){
			//positive=false;
			predicate[0]=predicate[0].substring(1);
		}
		for(String i : predicate)
			System.out.println(i+"end");
	}

	public static boolean unify(String a, String b){
		return true;
	}

	public static void resolve(String a, String b, ArrayList<String> resolvents){
		boolean success=false;
		String[] aLiterals=a.split(" \\| ");
		String[] bLiterals=b.split(" \\| ");
		for(int i=0;i<aLiterals.length;i++){
			String l1=aLiterals[i];
			for(int j=0;j<bLiterals.length;j++){
				String l2=bLiterals[j];
				if(unify(l1,l2)){
					aLiterals[i]=null;
					bLiterals[j]=null;
					success=true;
				}
			}
		}
		if(success){
			//resolvent=combine(aLiterals,bLiterals);
		}
		//process literal pairs
		//resolve as much as we can
		//if literal pair is not contradictory, skip. Otherwise, try unify.
		//if same except ~, resolve
		//unify succeded resolve
		//do nothing is all failed to resolve
	}

	public static void resolution(ArrayList<String> KB, ArrayList<String> newKB, String alpha){
		int flag=1;
		ArrayList<String> resolvents=new ArrayList<>();
		while(flag>0){
			for(String i:KB){
				for(String j:newKB){
					resolve(i,j,resolvents);
					//put this part in resolve
					// String resolvent=resolve(i,j);
					// if(resolvent.isEmpty()){
					// 	System.out.println("True");
					// 	flag=0;
					// }else{
					// 	resolvents.add(resolvent);
					// }
				}
			}
			if(flag!=1){
				KB.addAll(newKB);
			}
			newKB.clear();
			flag++;
			for(String k:resolvents){
				if(!KB.contains(k)){
					KB.add(k);
					newKB.add(k);
				}
			}
			if(newKB.isEmpty()){
				System.out.println("False");
			}
		}
	}
}

class Literal{
	boolean positive=true;
	String[] predicate=null;

	public Literal(String str){
		predicate=str.split("\\(|,|\\)");
		if(str.charAt(0)=='~'){
			positive=false;
			predicate[0]=predicate[0].substring(1);
		}
	}

	@Override public boolean equals(Object o){
		Literal literal=(Literal)o;
		if(positive!=literal.positive)
			return false;
		if(predicate.length!=literal.predicate.length)
			return false;
		for(int i=0;i<predicate.length;i++){
			if(!predicate[i].equals(literal.predicate[i]))
				return false;
		}
		return true;
	}

	@Override public int hashCode(){
		StringBuilder sb=new StringBuilder();
		if(!positive){
			sb.append("~");
		}
		for(String i:predicate){
			sb.append(i);
		}
		return sb.toString().hashCode();
	}
}

class Clause{
	String[] subs=new String[26];
	ArrayList<Literal> literals=null;

	public Clause(String str){
		String[] litStr=str.split(" \\| ");
		literals=new ArrayList<>();
		for(int i=0;i<litStr.length;i++){
			literals.add(new Literal(litStr[i]));
		}
	}

	public void Substitute(String str){

	}

	public void resolve(Clause c){
		for(Literal i:literals){
			for(Literal j:c.literals){
				if(i.positive^j.positive&&i.predicate[0]==j.predicate[0]){
					
				}
			}
		}
	}
}
