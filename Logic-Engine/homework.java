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
		LinkedHashSet<Clause> KB=new LinkedHashSet<>();
		LinkedHashSet<Clause> newKB=new LinkedHashSet<>();
		LinkedHashSet<Clause> resolvents=new LinkedHashSet<>();
		for(int j=0;j<KBCount;j++){
			Clause x=new Clause(br.readLine());
			KB.add(x);
		}
		br.close();

		BufferedWriter bw=new BufferedWriter(new FileWriter("./output.txt"));
		for(String query: queries){
			resolvents.clear();
			newKB.clear();
			if(query.charAt(0)=='~'){
				newKB.add(new Clause(query.substring(1)));
			}else{
				newKB.add(new Clause("~"+query));
			}

			boolean success=false;
			int kbSize=0;
			while(!success){
				for(Clause c1:newKB){
					for(Clause c2:KB){
						success=Clause.resolve(c2,c1,resolvents);
						if(success)
							break;
					}
					if(success)
						break;
					for(Clause c3:newKB){
						success=Clause.resolve(c3,c1,resolvents);
						if(success)
							break;
					}
					if(success)
						break;
				}
				if(success)
					break;
				kbSize=newKB.size();
				newKB.addAll(resolvents);
				//System.out.println(kbSize+" "+newKB.size());
				//System.out.println(newKB.size());
				resolvents.clear();
				if(kbSize==newKB.size())
					break;	
			}

			if(success){
				bw.write("TRUE\n");
			}else{
				bw.write("FALSE\n");
			}

			// for(Clause c:newKB){
			// 	c.print();
			// }
		}
		bw.close();
	}

}

class Literal{
	boolean positive=true;
	boolean remove=false;
	String[] predicate=null;

	public Literal(String str){
		predicate=str.split("\\(|,|\\)");
		if(str.charAt(0)=='~'){
			positive=false;
			predicate[0]=predicate[0].substring(1);
		}
    }
    
    public Literal(Literal l){
        positive=l.positive;
        predicate=new String[l.predicate.length];
        for(int i=0;i<predicate.length;i++){
			predicate[i]=l.predicate[i];
		}
    }
    
    public void print(){
        StringBuilder sb=new StringBuilder();
		if(!positive){
			sb.append("~");
		}
		for(String i:predicate){
			sb.append(i);
        }
        System.out.print(sb.toString());
    }

	@Override public boolean equals(Object o){
		Literal literal=(Literal)o;
		if(positive!=literal.positive)
			return false;
		if(predicate.length!=literal.predicate.length)
			return false;
        if(hashCode()!=literal.hashCode())
            return false;

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
	
	LinkedHashSet<Literal> literals=null;

	public Clause(String str){
		String[] litStr=str.split(" \\| ");
		literals=new LinkedHashSet<>();
		for(int i=0;i<litStr.length;i++){
			literals.add(new Literal(litStr[i]));
		}
    }
    
    public Clause(){
        literals=new LinkedHashSet<>();
    }

    public int size(){
        return literals.size();
    }
	public void add(Literal l){
		literals.add(l);
	}

	public static boolean unifyVar(String x, String y, HashMap<String, String> theta){
		if(x.equals(y)) return true;
		if(Character.isLowerCase(x.charAt(0))){
			if(theta.get(x)!=null){
				return unifyVar(theta.get(x),y,theta);
			}else if(theta.get(y)!=null){
				return unifyVar(x,theta.get(y),theta);
			}else{
                theta.put(x,y);
			}
		}else if(Character.isLowerCase(y.charAt(0))){
			if(theta.get(y)!=null){
				return unifyVar(theta.get(y),x,theta);
			}else{
                theta.put(y,x);
			}
		}else{
			if(!x.equals(y)){
                theta.clear();
                return false;
			}
        }
        return true;
	}

	public static boolean resolve(Clause c1,Clause c2,LinkedHashSet<Clause> KB){	
        HashMap<String, String> theta=new HashMap<>();
        boolean success=true;
		for(Literal i:c1.literals){
			for(Literal j:c2.literals){
                success=true;
				if((i.positive^j.positive)&&(i.predicate[0].equals(j.predicate[0]))){
                   
					for(int k=1;k<i.predicate.length;k++){
						String x=Character.isLowerCase(i.predicate[k].charAt(0))?i.predicate[k]+"1":i.predicate[k];                                              //need to modify here
						String y=Character.isLowerCase(j.predicate[k].charAt(0))?j.predicate[k]+"2":j.predicate[k]; 										//Consider constant
						if(!unifyVar(x,y,theta)){
                            success=false;
                            break;
                        }
					}
					if(success){
                        Clause c=new Clause();
						for(Literal m:c1.literals){
							if(!m.toString().equals(i.toString())){
								// m.print();
                                Literal l=new Literal(m);
								for(int k=1;k<m.predicate.length;k++){
									if(Character.isLowerCase(m.predicate[k].charAt(0))){
										String x=m.predicate[k]+"1";
										while(theta.get(x)!=null){
											x=theta.get(x);
										}
										l.predicate[k]=x;
									}
								}
                                c.add(l);
                                
							}	
							
						}
						for(Literal n:c2.literals){
							if(!n.toString().equals(j.toString())){
								Literal l=new Literal(n);
								for(int k=1;k<n.predicate.length;k++){
									if(Character.isLowerCase(n.predicate[k].charAt(0))){
										String x=n.predicate[k]+"1";
										while(theta.get(x)!=null){
											x=theta.get(x);
										}
										l.predicate[k]=x;			
									}
                                }
                                c.add(l);
							}		
						}
                        if(c.size()==0){
                            return true;
                        }else{
							// System.out.print("before:");
							// c.print();
							c=factoring(c);
							// System.out.print("after:");
							// c.print();
                            KB.add(c);
                            theta.clear();
                        }	
					}
				}
			}
        }
        return false;
    }

    public static Clause factoring(Clause c){
        HashMap<String, String> theta=new HashMap<>();
		boolean success=true;

        for(Literal i: c.literals){
            for(Literal j:c.literals){
				success=true;
                if((!i.remove&&!j.remove&&(i.positive==j.positive)&&(i.predicate[0].equals(j.predicate[0]))&&(!i.equals(j)))){
                     for(int k=1;k<i.predicate.length;k++){
                         if(!unifyVar(i.predicate[k],j.predicate[k],theta)){
                             success=false;
                             break;
                         }
                    }
                    if(success){
                        j.remove=true;
                        for(Literal m:c.literals){
                            if(m.predicate!=null){
                                for(int k=1;k<m.predicate.length;k++){
                                    if(Character.isLowerCase(m.predicate[k].charAt(0))){
                                        String x=m.predicate[k];
                                        while(theta.get(x)!=null){
                                            x=theta.get(x);
                                        }
                                        m.predicate[k]=x;
                                    }
                                }
                            }   
                        }
                        theta.clear();
                    }
                }
            }
            
		}
		
        Clause newC=new Clause();
        HashMap<String, String> asgnLet=new HashMap<>();
        char letter='a';
        for(Literal i:c.literals){
           if(!i.remove){
                for(int j=0;j<i.predicate.length;j++){
                    String k=i.predicate[j];
                    if(Character.isLowerCase(k.charAt(0))){
                        if(!asgnLet.containsKey(k)){
                            asgnLet.put(k,Character.toString(letter));
                            letter++;
                        }
                        i.predicate[j]=asgnLet.get(k);
                    }
                }
                newC.add(i);
           }else{
			   i.remove=false;
		   }
        }
        return newC;
	}
	
	public void print(){
		for(Literal i: literals){
			i.print();
			System.out.print(" ");
		}
		System.out.println();
	}

    @Override public boolean equals(Object o){
        Clause c=(Clause)o;
        return hashCode()==c.hashCode();
    }
    
    @Override public int hashCode() {
        ArrayList<Integer> lid=new ArrayList<>();
        for(Literal i:literals){
            lid.add(i.hashCode());
        }
        Collections.sort(lid);
        StringBuilder sb=new StringBuilder();
        for(int i: lid){
            sb.append(i);
        } 
        return sb.toString().hashCode();
    }
}
