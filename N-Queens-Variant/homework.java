import java.io.*;
import java.util.*;

public class homework{
	static int N; //width and height of nursery
	static int p; //number of lizards
	static int treeCount=0;
	static int lizardsCount=0;
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

	static void output(Node node) throws IOException{
		BufferedWriter bw=new BufferedWriter(new FileWriter("./output.txt"));
		bw.write("OK\n");
		bw.write(node.getString());
		bw.close();
	}
	//If there is no tree and p>n, there is no solution
	//If there is no tree and p<=n, because there won't be two lizards in the same row or col
	//so, there must be a solution in which each row has a lizard.
	//Based on this idea, we expand frontier node and prune all branches that add new lizard to non-target row.
	static boolean BFS(Node InitNode) throws IOException{
		if(treeCount==0&&p>N){
			return false;
		}

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
					if(node.lizardsCount()==i||treeCount>0){
						//If there exists trees, every childNode with new lizard is added to frontier queue.
						//If there is no tree, only consider the branches that add new lizard to new line.
						if(Valid(node.state,i,j)){   
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
								output(childNode);
								return true;
							}
						}	
					}
				}
			}	
		}
		return false;
		
	}
	

	//Backtracking used to solve this problem
	//If there is no tree and p>n, there is no solution
	//If there is no tree and p<=n, because there won't be two lizards in the same row or col
	//so, there must be a solution in which each row has a lizard.
	//Therefore the bound for a new lizard is within the following row from last lizard.
	//This is the idea I based on when developing pruning branch technique regarding no tree condition.
	static boolean DFS(Node node) throws IOException{
		if(treeCount==0&&p>N){
			return false;
		}

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
					}
					if(lizardsCount==p){
						output(curNode);
						return true;
					}
					break;//Find a position for new lizard, jump out for loop
				}else{
					//go on for loop but if it reaches the bound, it fails to find sucessor
					if(i==bound-1){
						curNode=frontier.pop();
						if(frontier.size()==0){
							return false;
						}
						lizardsCount--;
						curNode.state[curNode.lastLizPos/N][curNode.lastLizPos%N]=0;
						if(treeCount==0){
							bound=(curNode.lastLizPos/N+1)*N;
						}
						break;
					}
				}
			}
			if(lizardsCount!=p&&curNode.lastLizPos==bound-1){
				curNode=frontier.pop();
				if(frontier.size()==0){
					return false;
				}
				lizardsCount--;
				curNode.state[curNode.lastLizPos/N][curNode.lastLizPos%N]=0;
				if(treeCount==0){
					bound=(curNode.lastLizPos/N+1)*N;
				}
				
			}
		}
		
	}


	static boolean SA(Node InitNode) throws IOException{
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
			saBound=500;
		}else if(N>=25){
			T0=1000000;
			saBound=500;
		}
		
		//Annealing Starts
		while(totalCost!=0){
			saCount++;
			curNode=backUpNode.clone();
			totalCost=backUpTotalCost;
			lizardsPos=backUplizsPos.clone();
			prob=1;
			for(int T=T0;T>0;T--){
				move=generator.nextInt(p);
				curCost=cost(curNode.state, lizardsPos[move]/N, lizardsPos[move]%N);
				curNode.state[lizardsPos[move]/N][lizardsPos[move]%N]=0;//changed
				do{
					if(treeCount==0){
						lizardPos=(lizardsPos[move]/N)*N+generator.nextInt(N);
					}else{
						lizardPos=generator.nextInt(boardSize);
					}
				}
				while(curNode.state[lizardPos/N][lizardPos%N]!=0);
				
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
				break;
			}
		}	
		if(totalCost==0){
			output(curNode);
			return true;
		}else{
			return false;
		}
	}


	public static void main(String[] args) throws IOException{
		BufferedReader br=new BufferedReader(new FileReader("./input.txt"));
		String type=br.readLine();
		N=Integer.parseInt(br.readLine());
		p=Integer.parseInt(br.readLine());
		Node InitNode=new Node(N);
		String row=null;
		for(int i=0;i<N;i++){
			row=br.readLine();
			for(int j=0;j<N;j++){
				if(row.charAt(j)=='2'){
					InitNode.state[i][j]=2;
					treeCount++;
				}
			}
		}
		br.close();

		boolean result=false;
		if(type.equals("BFS")){
			result=BFS(InitNode);
		}else if(type.equals("DFS")){
			result=DFS(InitNode);
		}else{
			result=SA(InitNode);
		}

		if(result==false){
			BufferedWriter bw=new BufferedWriter(new FileWriter("./output.txt"));
			bw.write("FAIL\n");
			bw.close();
		}
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