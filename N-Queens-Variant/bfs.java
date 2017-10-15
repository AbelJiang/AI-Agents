import java.util.*;

public class bfs{
	static int N; //width and height of nursery
	static int p; //number of lizards
	static int treeCount;
	static int lizardsCount=0;
	static byte[][] solution=null;
	static LinkedList<Node> frontier=new LinkedList<Node>();
	static HashSet<Node> explored=new HashSet<Node>();

	static boolean Valid(byte[][] state, int x, int y){
		if(state[x][y]==1||state[x][y]==2)
			return false;
		//up
		for(int i=x-1;i>=0;i--){
			if(state[i][y]==1){
				return false;
			}else if(state[i][y]==2){
				break;
			}
		}

		//down
		for(int i=x+1;i<N;i++){
			if(state[i][y]==1){
				return false;
			}else if(state[i][y]==2){
				break;
			}
		}

		//left
		for(int j=y-1;j>=0;j--){
			if(state[x][j]==1){
				return false;
			}else if(state[x][j]==2){
				break;
			}
		}

		//right
		for(int j=y+1;j<N;j++){
			if(state[x][j]==1){
				return false;
			}else if(state[x][j]==2){
				break;
			}
		}

		//up left
		for(int i=x-1,j=y-1;i>=0&&j>=0;i--,j--){
			if(state[i][j]==1){
				return false;
			}else if(state[i][j]==2){
				break;
			}
		}

		//down right
		for(int i=x+1,j=y+1;i<N&&j<N;i++,j++){	
			if(state[i][j]==1){
				return false;
			}else if(state[i][j]==2){
				break;
			}
		}

		//up right
		for(int i=x-1,j=y+1;i>=0&&j<N;i--,j++){
			if(state[i][j]==1){
				return false;
			}else if(state[i][j]==2){
				break;
			}
		}

		//down left
		for(int i=x+1,j=y-1;i<N&&j>=0;i++,j--){
			if(state[i][j]==1){
				return false;
			}else if(state[i][j]==2){
				break;
			}
		}
		return true;
	}

	static boolean goalTest(Node node){

		if(node.lizardsCount()==p){
			return true;
		}else{
			return false;
		}
	}

	static boolean Action(Node InitNode){
		frontier.add(InitNode);
		Node node=new Node(N);
		while(frontier.size()!=0){
			node=frontier.pop();
			if(node.lizardsCount()>lizardsCount){
				explored.clear();
				lizardsCount=node.lizardsCount();
			}
			for(int i=0;i<N;i++){
				for(int j=0;j<N;j++){
					if(Valid(node.state,i,j)){
						if(node.lizardsCount()==i||treeCount>0){
							Node childNode=new Node(N);
							for(int m=0;m<N;m++){
								for(int n=0;n<N;n++){
									childNode.state[m][n]=node.state[m][n];
								}
							}
							childNode.state[i][j]=1;
							//Rotate
							if(!explored.contains(childNode)){
								frontier.add(childNode);
								explored.add(childNode);
							}
							if(goalTest(childNode)){
								for(int p=0;p<N;p++){
									for(int q=0;q<N;q++){
										System.out.print(childNode.state[p][q]);
									}
									System.out.print("\n");
								}
								return true;
							}
						}	
					}
				}
			}	
		}
		return false;
		
	}

	public static void main(String[] args){
		N=p=13;
		treeCount=1;
	    Node InitNode=new Node(N);
		Action(InitNode);
	}
}

class Node{
	public byte[][] state=null;
	public int size=0;
	
	public Node(int N){
		this.state=new byte[N][N];
		this.size=this.state.length;

		for(int i=0;i<this.size;i++){
			for(int j=0;j<this.size;j++){
				this.state[i][j]=0;
			}
		}
	}

	@Override public boolean equals(Object o){
		Node node=(Node)o;
		byte[][] state=node.state;
		for(int i=0;i<this.size;i++){
			for(int j=0;j<this.size;j++){
				if(state[i][j]!=this.state[i][j]){
					return false;
				}
			}
		}
		return true;
	}

	@Override public int hashCode(){
		StringBuilder sb=new StringBuilder();

		for(int i=0;i<this.size;i++){
			for(int j=0;j<this.size;j++){
				sb.append(this.state[i][j]);
			}
		}
		return sb.toString().hashCode();
	}

	public int lizardsCount(){
		int lizardsCount=0;
		
		for(int i=0;i<this.size;i++){
			for(int j=0;j<this.size;j++){
				if(this.state[i][j]==1)
					lizardsCount++;
			}
		}
		return lizardsCount;
	}
}
