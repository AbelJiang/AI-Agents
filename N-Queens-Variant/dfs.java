import java.util.*;

public class dfs{
	static int N; //width and height of nursery
	static int p; //number of lizards
	static int treeCount=0;
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
	static boolean Action(Node node){
		Node curNode=new Node(N);
		curNode=node.clone();
		frontier.push(curNode);
		int boardSize=N*N;
		int bound=boardSize;
		while(true){
			for(int i=curNode.lastLizPos+1;i<bound;i++){//Find a position for new lizard
				
				if(Valid(curNode.state, i/N, i%N)){
					curNode=curNode.clone();
					curNode.state[i/N][i%N]=1;
					curNode.lastLizPos=i;
					frontier.push(curNode);
					lizardsCount++;
					if(treeCount==0){
						bound=(curNode.lastLizPos/N+2)*N;
						//System.out.println(bound);
					}
					if(lizardsCount==p){
						for(int p=0;p<N;p++){
							for(int q=0;q<N;q++){
								System.out.print(curNode.state[p][q]);
							}
							System.out.print("\n");
						}
						return true;
					}
					break;//Find a position for new lizard, jump out for loop
				}else{
					//go on for loop but if it reaches the boardSize, it fails to find sucessor
					if(i==bound-1){
						curNode=frontier.pop();
						lizardsCount--;
						curNode.state[curNode.lastLizPos/N][curNode.lastLizPos%N]=0;
						if(treeCount==0){
							bound=(curNode.lastLizPos/N+1)*N;
						}
						
						if(frontier.size()==0){
							System.out.println("failed!");
							return false;
						}

						// System.out.print("\n......................Result..................\n");
						// for(int p=0;p<N;p++){
						// 	for(int q=0;q<N;q++){
						// 		System.out.print(curNode.state[p][q]);
						// 	}
						// 	System.out.print("\n");
						// }
						break;
					}
				}
			}
			if(lizardsCount!=p&&curNode.lastLizPos==bound-1){
				curNode=frontier.pop();
				lizardsCount--;
				curNode.state[curNode.lastLizPos/N][curNode.lastLizPos%N]=0;
				if(treeCount==0){
					bound=(curNode.lastLizPos/N+1)*N;
				}
				if(frontier.size()==0){
					System.out.println("failed!");
					return false;
				}
			}
		}
		
	}
	public static void main(String[] args){
		N=p=30;
		Node InitNode=new Node(N);
		treeCount=0;
		//InitNode.state[4][9]=2;
		//InitNode.state[5][7]=2;
		Action(InitNode);
	}
}
class Node{
	public byte[][] state=null;
	public int size=0;
	public int lastLizPos=-1;
	
	public Node(int N){
		this.state=new byte[N][N];
		this.size=this.state.length;

		for(int i=0;i<this.size;i++){
			for(int j=0;j<this.size;j++){
				this.state[i][j]=0;
			}
		}
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

	@Override public Node clone(){
		Node node=new Node(this.size);
		for(int i=0;i<this.size;i++){
			for(int j=0;j<this.size;j++){
				node.state[i][j]=this.state[i][j];
			}
		}
		node.size=this.size;
		node.lastLizPos=this.lastLizPos;
		return node;
	}
}
