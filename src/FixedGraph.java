import java.util.LinkedHashMap;
import java.util.Map;

public class FixedGraph extends Graph {
	private int[][] adjMatrix;
	private int numVerts;
	private int numNegEdges = 0;
	public FixedGraph(int n) {
		adjMatrix = new int[n][n];
		numVerts = n;
	}
	/*
	 * Makes a copy of the input graph element by element.
	 */
	public FixedGraph(Graph g) {
		numVerts = g.numVertices();
		int[][] mat = g.getAdjMatrix();
		adjMatrix = new int[numVerts][numVerts];
		for(int i = 0; i < numVerts; ++i) {
			for(int j = 0; j < numVerts; ++j) {
				adjMatrix[i][j] = mat[i][j];
				if(mat[i][j] < 0) {
					numNegEdges++;
				}
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see Graph#addEdge(int, int, int, boolean)
	 */
	public void addEdge(int u, int v, int weight, boolean dir) {
		if(adjMatrix[u][v] != 0 || weight == 0)
			return;
		if(weight < 0)
			numNegEdges++;
		adjMatrix[u][v] = weight;
		if(!dir) {
			addEdge(v,u,weight,true);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see Graph#getAdjMatrix()
	 */
	public int[][] getAdjMatrix() {
		int[][] ret = new int[numVerts][numVerts];
		for(int i = 0; i < numVerts; ++i) {
			for(int j = 0; j < numVerts; ++j)
				ret[i][j] = adjMatrix[i][j];
		}
		return ret;
	}
	
	/*
	 * (non-Javadoc)
	 * @see Graph#edgeWeight(int, int)
	 */
	public int edgeWeight(int u, int v) {
		return adjMatrix[u][v];
	}
	
	/*
	 * (non-Javadoc)
	 * @see Graph#edgeExists(int, int)
	 */
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
	 * (non-Javadoc)
	 * @see Graph#changeEdge(int, int, int, boolean)
	 */
	public void changeEdge(int u, int v, int weight, boolean dir) {
		if(weight < 0 && adjMatrix[u][v] >= 0)
			numNegEdges++;
		else if(weight >= 0 && adjMatrix[u][v] < 0)
			numNegEdges--;
		adjMatrix[u][v] = weight;
		if(!dir)
			changeEdge(v,u,weight,true);
	}
	public void changeEdge(int u, int v, int weight) {
		this.changeEdge(u,v,weight,false);
	}
	
	/*
	 * (non-Javadoc)
	 * @see Graph#modifyWeight(int, int, int, boolean)
	 */
	public void modifyWeight(int u, int v, int delta, boolean dir) {
		this.changeEdge(u,v,edgeWeight(u,v) + delta, dir);
	}
	
	/*
	 * Returns the number of vertices in the graph.
	 */
	public int numVertices() {
		return numVerts;
	}
	
	/*
	 * Return whether there are any negative edges in the graph.  Useful to know if
	 * we're trying to decide what shortest path algorithm to use.
	 */
	public boolean hasNegativeEdges() {
		return numNegEdges > 0;
	}
}
