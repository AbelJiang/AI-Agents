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

		char[][] InitState=new char[N][N];
		String row=null;
		for(int i=0;i<N;i++){
			row=br.readLine();
			for(int j=0;j<N;j++){
				InitState[i][j]=row.charAt(j);
			}
		}
		br.close();


		//byte[][] component=new byte[N][N];
		// Node node=new Node(InitState);
		// node.getChildNodes();
		// System.out.print(node.childNodes.size());
		// System.out.print("\n\n");
		// for(int k=0;k<node.childNodes.size();k++){
		// 	for(int i=0;i<N;i++){
		// 		for(int j=0;j<N;j++){
		// 			System.out.print(node.childNodes.get(k).state[i][j]+" ");
		// 		}
		// 		System.out.print('\n');
		// 	}
		// 	System.out.print("Gain: "+node.childNodes.get(k).Gain);
		// 	System.out.print('\n');
		// 	System.out.print('\n');
		// }
		// dfs(4,1,component,InitState,node);
		// node.delCompo();
		// for(int i=0;i<N;i++){
		// 	for(int j=0;j<N;j++){
		// 		System.out.print(InitState[i][j]+" ");
		// 	}
		// 	System.out.print('\n');
		// }
		// System.out.print('\n');
		// for(int i=0;i<N;i++){
		// 	for(int j=0;j<N;j++){
		// 		System.out.print(node.state[i][j]+" ");
		// 	}
		// 	System.out.print('\n');
		// }
		// System.out.print(node.Gain);
	}
}

class Node{
	public char[][] state=null;
	public byte[][] component=null;
	public int[] lastChoice=null;
	public int Gain=0;
	public boolean maxNode=true;

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
		this.state=new char[size][size];
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				this.state[i][j]=state[i][j];
			}
		}
	}

	public void getChildNodes(){
		component=new byte[size][size];
		this.childNodes=new ArrayList<Node>();
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				if(state[i][j]=='*'){
					component[i][j]=-1;
				}
				if(component[i][j]!=-1){
					Node node=new Node(size);
					node.parentNode=this;
					node.state=this.stateClone(this.state);
					node.lastChoice=new int[2];
					node.lastChoice[0]=i;
					node.lastChoice[1]=j;
					dfs(i,j,component,node);
					node.Gain=node.Gain*node.Gain;
					node.delCompo();

					if(this.maxNode==true)
						node.maxNode=false;
					this.childNodes.add(node);
				}
			}
		}
	}

	public void dfs(int x, int y, byte[][] component, Node node){
		component[x][y]=-1;
		node.state[x][y]='X';
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
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				if(state[i][j]=='X'){
					for(int k=0;k<i;k++){
						state[i-k][j]=state[i-k-1][j];
					}
					state[0][j]='*';
				}
			}
		}
	}
}
