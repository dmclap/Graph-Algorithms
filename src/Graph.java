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
	/*
	 * Makes a copy of the input graph element by element.
	 */
	public Graph(Graph g) {
		numVerts = g.numVertices();
		int[][] mat = g.getAdjMatrix();
		adjMatrix = new int[numVerts][numVerts];
		for(int i = 0; i < numVerts; ++i)
			for(int j = 0; j < numVerts; ++j)
				adjMatrix[i][j] = mat[i][j];
	}
	protected Graph() {
		// Only included to appease the subclass.
	}
	
	/*
	 * A method to add an edge to the graph, which does nothing if the edge
	 * already exists in the graph.  This version has all of the possible
	 * inputs, and all other versions just call this one with default values.
	 * 
	 * u: One endpoint of the edge.  If it's directed, this is the beginning
	 * 		of the edge.
	 * v: The other endpoint, and the end of the edge if it's directed.
	 * weight: The weight of the edge, default 1.
	 * dir: A boolean indicating if the edge is directed (true) or not (false).
	 * 		  Defaults to false.
	 */
	public void addEdge(int u, int v, int weight, boolean dir) {
		if(adjMatrix[u][v] != 0)
			return;
		adjMatrix[u][v] = weight;
		if(!dir) {
			adjMatrix[v][u] = weight;
		}
	}
	// these functions will do nothing if the edge already exists
	public void addEdge(int u, int v) { // assumes undirected, unweighted
		this.addEdge(u,v,1,false);
	}	
	public void addEdge(int u, int v, boolean dir) {
		this.addEdge(u,v,1,dir);
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
	
	/*
	 * Returns an adjacency list representation of the graph.  To parse the return
	 * value (which we'll call edges), the directed edge u->v with weight w is
	 * represented as edges[u].get(v) returning value w. 
	 */
	public Map<Integer,Integer>[] getAdjList() {
		Map<Integer,Integer>[] ret = new Map[numVerts];
		for(int i = 0; i < numVerts; ++i) {
			ret[i] = new LinkedHashMap<Integer,Integer>();
			for(int j = 0; j < numVerts; ++j) {
				if(adjMatrix[i][j] != 0) {
					ret[i].put(j, adjMatrix[i][j]);
				}
			}
		}
		return ret;
	}
	
	/*
	 * Changes the weight of a particular edge.  If the boolean dir is true,
	 * changes the weight in both directions.
	 * 
	 *  u: One endpoint of the edge to change; if the change is uni-directional,
	 *  	the beginning of the edge to change.
	 *  v: The other endpoint of the edge to change.
	 *  weight: The new weight of the edge.
	 *  dir: Whether or not the new edge weight should occur in both directions
	 *  	(true means yes).  Usually used to indicate whether or not the
	 *  	original edge was directed.
	 */
	public void changeEdge(int u, int v, int weight, boolean dir) {
		adjMatrix[u][v] = weight;
		if(!dir)
			adjMatrix[v][u] = weight;
	}
	public void changeEdge(int u, int v, int weight) {
		this.changeEdge(u,v,weight,false);
	}
	
	/*
	 * Modifies the weight of the specified edge by the specified delta.  It's
	 * mostly a shorthand for algorithms that iteratively alter edge weights,
	 * like Ford-Fulkerson.
	 * 
	 * u: One endpoint of the edge to change; if the change is uni-directional,
	 *  	the beginning of the edge to change.
	 *  v: The other endpoint of the edge to change.
	 *  delta: The amount by which to change the edge weight.  A negative number
	 *  	means that the weight should decrease.
	 *  dir:  Whether or not the change in edge weight should occur in both 
	 *  	directions (true means yes).
	 */
	public void modifyWeight(int u, int v, int delta, boolean dir) {
		this.changeEdge(u,v,edgeWeight(u,v) + delta, dir);
	}
	
	/*
	 * Determines if this graph equals another one by comparing their adjacency
	 * matrices.
	 */
	public boolean equals(Object o) {
		Graph other;
		try {
			other = (Graph)o;
		}
		catch(ClassCastException e) {
			// This shouldn't happen, but if it does, just return false.
			return false;
		}
		if(other.numVerts != numVerts)
			return false;
		int[][] adj = other.getAdjMatrix();
		for(int i = 0; i < numVerts; ++i)
			for(int j = 0; j < numVerts; ++j)
				if(adj[i][j] != adjMatrix[i][j])
					return false;
		return true;
	}
	/*
	 * Builds a string representation of the adjacency matrix.  A future
	 * avenue of improvement is to build this as a format string.
	 */
	public String toString() {
		String ret = "";
		for(int i = 0; i < numVerts; ++i) {
			for(int j = 0; j < numVerts; ++j)
				ret += adjMatrix[i][j] + " ";
			ret += "\n";
		}
		return ret;
	}
	
	/*
	 * Returns the number of vertices in the graph.
	 */
	public int numVertices() {
		return numVerts;
	}
}
