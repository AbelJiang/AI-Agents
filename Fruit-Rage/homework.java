import java.io.*;
import java.util.*;

public class homework{
	static int N=0; //Board Size
	static int P=0; //Fruit Types
	static float T=0;
	static int level=0;

	public static void levelControl(int n, Node node){
		node.getChildNodes();
		level=n;
	}

	public static boolean terminalTest(Node node){
		if(node.getLevel()==level||node.childNodes.size()==0){
			node.getGainDiff();
			return true;
		}else{
			return false;
		}
	}

	public static Node maxValue(Node node, int a, int b){
		if(terminalTest(node)){
			return node;
		}
		Node retNode=null;
		node.gainDiff=Integer.MIN_VALUE;
		node.sortChild();
		for(Node n:node.childNodes){
			n.getChildNodes();
			int minGain=minValue(n,a,b).gainDiff;
			if(node.gainDiff<minGain){
				node.gainDiff=minGain;
				retNode=n;
				if(node.gainDiff>=b){
					return retNode;
				}
				if(node.gainDiff>a){
					a=node.gainDiff;
				}
			}
		}
		return retNode;
	}

	public static Node minValue(Node node, int a, int b){
		if(terminalTest(node)){
			return node;
		}
		Node retNode=null;
		node.gainDiff=Integer.MAX_VALUE;
		node.sortChildReverse();
		for(Node n:node.childNodes){
			n.getChildNodes();
			int maxGain=maxValue(n,a,b).gainDiff;
			if(node.gainDiff>maxGain){
				node.gainDiff=maxGain;
				retNode=n;
				if(node.gainDiff<=a){
					return retNode;
				}
				if(node.gainDiff<a){
					b=node.gainDiff;
				}
			}
		}
		
		return retNode;
	}

	public static Node abs(Node node){
		int level=3;
		// if(N>10){
		// 	level=2;
		// }

		// if(T<50){
		// 	level=2;
		// }
		levelControl(level, node);
		return maxValue(node, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	public static void main(String[] args) throws IOException{
		BufferedReader br=new BufferedReader(new FileReader("./input.txt"));
		N=Integer.parseInt(br.readLine());
		P=Integer.parseInt(br.readLine());
		T=Float.parseFloat(br.readLine());

		char[][] InitState=new char[N][N];
		String row=null;
		for(int i=0;i<N;i++){
			row=br.readLine();
			for(int j=0;j<N;j++){
				InitState[i][j]=row.charAt(j);
			}
		}
		br.close();
		
		Node InitNode=new Node(InitState);
		
		Node node=abs(InitNode);
		node.print();

		int k=2;
	}
}

class Node{
	public char[][] state=null;
	public int[] lastChoice=null;
	public int Gain=0;
	public int gainDiff=0;
	public boolean maxNode=true;
	ArrayList<int[]> delMatrix=null;

	public int size=0;
	public Node parentNode=null;
	public ArrayList<Node> childNodes=null;

	public Node(int size){
		this.size=size;
		this.state=new char[size][size];
		if(parentNode!=null&&parentNode.maxNode==true)
			maxNode=false;
	}

	public Node(char[][] state){
		this.size=state[0].length;
		this.state=this.stateClone(state);
	}

	public void getChildNodes(){
		byte[][] component=new byte[size][size];
		this.childNodes=new ArrayList<Node>();
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				if(state[i][j]=='*'){
					component[i][j]=-1;
				}
				if(component[i][j]!=-1){
					Node node=new Node(this.state);
					node.delMatrix=new ArrayList<int[]>();
					node.parentNode=this;
					node.lastChoice=new int[2];
					node.lastChoice[0]=i;
					node.lastChoice[1]=j;
					dfs(i,j,component,node);
					node.Gain=node.Gain*node.Gain;
					node.delCompo();

					if(this.maxNode==true){
						node.maxNode=false;
					}else{
						node.maxNode=true;
					}	
					this.childNodes.add(node);
				}
			}
		}
	}

	public void dfs(int x, int y, byte[][] component, Node node){
		component[x][y]=-1;
		//node.state[x][y]='X';
		node.delMatrix.add(new int[]{x,y});
		
		node.Gain++;
		char type=state[x][y];
	
		if(x-1>=0&&state[x-1][y]==type&&component[x-1][y]!=-1){
			dfs(x-1,y,component,node);
		}
	
		if(y-1>=0&&state[x][y-1]==type&&component[x][y-1]!=-1){
			dfs(x,y-1,component,node);
		}
		
		if(x+1<size&&state[x+1][y]==type&&component[x+1][y]!=-1){
			dfs(x+1,y,component,node);
		}

		if(y+1<size&&state[x][y+1]==type&&component[x][y+1]!=-1){
			dfs(x,y+1,component,node);
		}
	}

	public char[][] stateClone(char[][] state){
		char[][] stateCopy=new char[size][size];
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				stateCopy[i][j]=state[i][j];
			}
		}
		return stateCopy;
	}

	public void delCompo(){
		int count=0;
		int flag=delMatrix.size();
		while(flag!=0){
			int x=delMatrix.get(0)[0];
			int y=delMatrix.get(0)[1];

			for(int j=x;j<size;j++){
				if(delMContains(new int[]{j,y})){
					count++;
				}else{
					break;
				}
			}
			int k=x-1;
			for(;k>=0;k--){
				if(delMContains(new int[]{k,y})){
					count++;
				}else{
					break;
				}
			}
			
			if(k!=-1){
				for(int q=k;q>=0;q--){
					state[q+count][y]=state[q][y];
				}
				for(int p=0;p<count;p++){
					state[p][y]='*';
				}
			}else{
				for(int m=0;m<count;m++){
					state[m][y]='*';
				}
			}
			flag=delMatrix.size();
			count=0;
		}
	}

	public boolean delMContains(int[] a){
		for(int m=0;m<delMatrix.size();m++){
			if(delMatrix.get(m)[0]==a[0]&&delMatrix.get(m)[1]==a[1]){
				delMatrix.remove(m);
				return true;
			}
		}
		return false;
	}

	public void sortChild(){
		Comparator<Node> comp=(Node a, Node b)->{return a.Gain-b.Gain;};
		Collections.sort(this.childNodes,comp);
	}

	public void sortChildReverse(){
		Comparator<Node> comp=(Node a, Node b)->{return b.Gain-a.Gain;};
		Collections.sort(this.childNodes,comp);
	}

	public void getGainDiff(){
		Node a=this;
		while(a.parentNode!=null){
			if(a.maxNode){
				gainDiff=gainDiff-a.Gain;
			}else{
				gainDiff=gainDiff+a.Gain;
			}
			a=a.parentNode;
		}
	}

	public int getLevel(){
		int i=0;
		Node a=this;
		while(a.parentNode!=null){
			i++;
			a=a.parentNode;
		}
		return i;
	}

	public String getString(){
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<this.size;i++){
			for(int j=0;j<this.size;j++){
				sb.append(this.state[i][j]);
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public void print() throws IOException{
		BufferedWriter bw=new BufferedWriter(new FileWriter("./output.txt"));
		bw.write((char)(lastChoice[1]+65));
		bw.write(Integer.toString(lastChoice[0]+1)+"\n");
		bw.write(this.getString());
		bw.close();
	}
}
