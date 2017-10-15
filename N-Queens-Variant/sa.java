import java.util.*;

public class sa{
	static int N; //width and height of nursery
	static int p; //number of lizards
	static int treeCount=1;
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

	static int cost(byte[][] state, int x, int y){
		int cost=0;
		//up
		for(int i=x-1;i>=0;i--){
			if(state[i][y]==1){
				cost++;
			}else if(state[i][y]==2){
				break;
			}
		}

		//down
		for(int i=x+1;i<N;i++){
			if(state[i][y]==1){
				cost++;
			}else if(state[i][y]==2){
				break;
			}
		}

		//left
		for(int j=y-1;j>=0;j--){
			if(state[x][j]==1){
				cost++;
			}else if(state[x][j]==2){
				break;
			}
		}

		//right
		for(int j=y+1;j<N;j++){
			if(state[x][j]==1){
				cost++;
			}else if(state[x][j]==2){
				break;
			}
		}

		//up left
		for(int i=x-1,j=y-1;i>=0&&j>=0;i--,j--){
			if(state[i][j]==1){
				cost++;
			}else if(state[i][j]==2){
				break;
			}
		}

		//down right
		for(int i=x+1,j=y+1;i<N&&j<N;i++,j++){	
			if(state[i][j]==1){
				cost++;
			}else if(state[i][j]==2){
				break;
			}
		}

		//up right
		for(int i=x-1,j=y+1;i>=0&&j<N;i--,j++){
			if(state[i][j]==1){
				cost++;
			}else if(state[i][j]==2){
				break;
			}
		}

		//down left
		for(int i=x+1,j=y-1;i<N&&j>=0;i++,j--){
			if(state[i][j]==1){
				cost++;
			}else if(state[i][j]==2){
				break;
			}
		}
		return cost;
	}


	static boolean SA(Node InitNode) {
		if(treeCount==0&&p>N){
			return false;
		}

		int totalCost=0;
		int boardSize=N*N;
		int lizardPos=0;
		int move=0;
		int delta=0;
		int curCost=0;
		int nextCost=0;
		int saCount=0;
		int T0=100;
		int saBound=10000;
		double prob=1;
		Random generator=new Random();
		int[] lizardsPos=new int[p];
		Node curNode=InitNode.clone();
		
		//generate first node and calculate current cost
		for(int i=0;i<p;i++){
			do{
				if(treeCount==0){
					lizardPos=i*N+generator.nextInt(N);
				}else{
					lizardPos=generator.nextInt(boardSize);
				}
			}
			while(curNode.state[lizardPos/N][lizardPos%N]!=0);

			lizardsPos[i]=lizardPos;
			curNode.state[lizardPos/N][lizardPos%N]=1;
		}
		for(int i=0;i<p;i++){
			totalCost+=cost(curNode.state, lizardsPos[i]/N, lizardsPos[i]%N);
		}
		totalCost/=2;

		Node backUpNode=curNode.clone();
		int backUpTotalCost=totalCost;
		int[] backUplizsPos=lizardsPos.clone();

		if(N>=10&&N<15){
			T0=5000;
			saBound=5000;
		}else if(N>=15&&N<25){
			T0=100000;
			saBound=2000;
		}else if(N>=25){
			T0=100000;
			saBound=10000;
		}
		
		//Annealing Starts
		while(totalCost!=0){
			saCount++;
			//curNode=backUpNode.clone();
			//totalCost=backUpTotalCost;
			//lizardsPos=backUplizsPos.clone();
			prob=1;
			//System.out.println(saCount);
			for(int T=T0;T>0;T--){
				move=generator.nextInt(p);
				do{
					if(treeCount==0){
						lizardPos=(lizardsPos[move]/N)*N+generator.nextInt(N);
					}else{
						if(generator.nextBoolean()==true){
							//System.out.println("A");
							lizardPos=(lizardsPos[move]/N)*N+generator.nextInt(N);
						}else{
							//System.out.println("B");
							lizardPos=generator.nextInt(N)*N+lizardsPos[move]%N;
						}
					}
				}
				while(curNode.state[lizardPos/N][lizardPos%N]!=0);
				curCost=cost(curNode.state, lizardsPos[move]/N, lizardsPos[move]%N);
				curNode.state[lizardsPos[move]/N][lizardsPos[move]%N]=0;//changed
				
				
				nextCost=cost(curNode.state, lizardPos/N, lizardPos%N);
				delta=nextCost-curCost;
	
				if(delta<0){
					lizardsPos[move]=lizardPos;
					totalCost+=delta;
					curNode.state[lizardPos/N][lizardPos%N]=1;
				}else if(prob>generator.nextDouble()){
					lizardsPos[move]=lizardPos;
					totalCost+=delta;
					curNode.state[lizardPos/N][lizardPos%N]=1;
					prob=Math.exp(-delta*T0/T);
				}else{
					curNode.state[lizardsPos[move]/N][lizardsPos[move]%N]=1;
				}
								
				if(totalCost==0){
					break;
				}
				
			}
			if(saCount>saBound|totalCost==0){
				System.out.println(saCount);
				break;
			}
		}	
		if(totalCost==0){
			for(int p=0;p<N;p++){
				for(int q=0;q<N;q++){
					System.out.print(curNode.state[p][q]);
				}
				System.out.print("\n");
			}
			
			return true;
		}else{
			return false;
		}
	}
	public static void main(String[] args){
		//if p>N and treeCount=0, return false;
		N=100;
		p=100;
		treeCount=0;
		Node InitNode=new Node(N);
		//for(int i=0;i<p;i++){
		//	InitNode.state[i][p-1]=2;
		//}
		// InitNode.state[2][3]=2;
		// InitNode.state[15][8]=2;
		SA(InitNode);
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