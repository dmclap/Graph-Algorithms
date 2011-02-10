import java.util.LinkedHashMap;
import java.util.Map;
// TODO: add an Expandable Graph that stores, as an arraylist of arraylists, a graph whose size need not be specified
public class Graph {
	private int[][] adjMatrix;
	private int numVerts;
	public Graph(int n) {
		adjMatrix = new int[n][n];
		numVerts = n;
	}
	public Graph(Graph g) {
		numVerts = g.numVertices();
		int[][] mat = g.getAdjMatrix();
		adjMatrix = new int[numVerts][numVerts];
		for(int i = 0; i < numVerts; ++i)
			for(int j = 0; j < numVerts; ++j)
				adjMatrix[i][j] = mat[i][j];
	}
	public Graph() {
	}
	// these functions will do nothing if the edge already exists
	public void addEdge(int u, int v) { // assumes undirected, unweighted
		this.addEdge(u,v,1,false);
	}	
	public void addEdge(int u, int v, boolean dir) {
		this.addEdge(u,v,1,dir);
	}
	public void addEdge(int u, int v, int weight, boolean dir) {
		if(adjMatrix[u][v] != 0)
			return;
		adjMatrix[u][v] = weight;
		if(!dir) {
			adjMatrix[v][u] = weight;
		}
	}
	public void addEdge(int u, int v, int weight) { // assumes undirected
		this.addEdge(u,v,weight,false);
	}
	public void addDirectedEdge(int u, int v, int weight) {
		this.addEdge(u,v,weight,true);
	}
	public void addDirectedEdge(int u, int v) {
		this.addEdge(u,v,1,true);
	}
	public int[][] getAdjMatrix() {
		return adjMatrix;
	}
	public int edgeWeight(int u, int v) {
		return adjMatrix[u][v];
	}
	public boolean edgeExists(int u, int v) {
		return adjMatrix[u][v] != 0;
	}
	// Adjacency lists will be Map<int,int>[]
	// The first int is the vertex, the int it maps to is the weight
	public Map<Integer,Integer>[] getAdjList() {
		Map<Integer,Integer>[] ret = new Map[numVerts];
		for(int i = 0; i < numVerts; ++i) {
			ret[i] = new LinkedHashMap<Integer,Integer>();
			//HashMap<Integer,Integer> currentVert = new HashMap<Integer,Integer>();
			for(int j = 0; j < numVerts; ++j) {
				if(adjMatrix[i][j] != 0) {
					ret[i].put(j, adjMatrix[i][j]);
				}
			}
		}
		return ret;
	}
	public void changeEdge(int u, int v, int weight, boolean dir) {
		adjMatrix[u][v] = weight;
		if(!dir)
			adjMatrix[v][u] = weight;
	}
	public void changeEdge(int u, int v, int weight) {
		this.changeEdge(u,v,weight,false);
	}
	public void modifyWeight(int u, int v, int delta, boolean dir) {
		this.changeEdge(u,v,edgeWeight(u,v) + delta, dir);
	}
	// this would probably be more efficient using the adjacency lists...
	public boolean equals(Object o) {
		Graph other = (Graph)o;
		if(other.numVerts != numVerts)
			return false;
		int[][] adj = other.getAdjMatrix();
		for(int i = 0; i < numVerts; ++i)
			for(int j = 0; j < numVerts; ++j)
				if(adj[i][j] != adjMatrix[i][j])
					return false;
		return true;
	}
	public String toString() {
		String ret = "";
		for(int i = 0; i < numVerts; ++i) {
			for(int j = 0; j < numVerts; ++j)
				ret += adjMatrix[i][j] + " ";
			ret += "\n";
		}
		return ret;
	}
	public int numVertices() {
		return numVerts;
	}
}
