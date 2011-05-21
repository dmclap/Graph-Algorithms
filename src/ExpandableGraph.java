import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* 
 * A version of the Graph class that doesn't need to know how many vertices there
 * are in the graph when it begins.  It keeps track of the adjacency list as a
 * resizable data structure to allow the graph to expand to the number of
 * distinct vertices that are input.  Specifically, if there is an edge u->v
 * with weight w, then adjList.get(u).get(v) will return w.
*/
public class ExpandableGraph extends Graph {
	private ArrayList<Map<Integer, Integer>> adjList;
	private int numVerts;
	private int numNegEdges = 0;
	// Gives an initial number of vertices to start with
	public ExpandableGraph(int n) {
		adjList = new ArrayList<Map<Integer,Integer>>(n);
		numVerts = n;
		for(int i = 0; i < n; ++i)
			adjList.add(new HashMap<Integer,Integer>(n/4)); // a guess at the connectedness
	}

	// Sets this graph to have the same structure as the input graph
	public ExpandableGraph(Graph g) {
		numVerts = g.numVertices();
		Map<Integer,Integer>[] other = g.getAdjList();
		adjList = new ArrayList<Map<Integer,Integer>>(numVerts);
		for(int i = 0; i < other.length; ++i) {
			HashMap<Integer,Integer> next = new HashMap<Integer,Integer>();
			for(int key : other[i].keySet()) {
				if(other[i].get(key) < 0)
					numNegEdges++;
				next.put(key, other[i].get(key));
			}
			adjList.add(next);
		}
	}
	
	// Default constructor
	public ExpandableGraph() {
		adjList = new ArrayList<Map<Integer,Integer>>();
		numVerts = 0;
	}
	
	/*
	 * Makes a copy of this graph to be used elsewhere.
	 * (non-Javadoc)
	 * @see Graph#getAdjList()
	 */
	public Map<Integer,Integer>[] getAdjList() {
		HashMap<Integer,Integer>[] t = new HashMap[1]; 
		return adjList.toArray(t);
	}
	/*
	 * Converts the graph to an adjacency matrix representation.
	 * (non-Javadoc)
	 * @see Graph#getAdjMatrix()
	 */
	public int[][] getAdjMatrix() {
		int[][] ret = new int[numVerts][numVerts];
		for(int i = 0; i < numVerts; ++i) {
			for(int j = 0; j < numVerts; ++j) {
				if(adjList.get(i).containsKey(j))
					ret[i][j] = adjList.get(i).get(j);
				else
					ret[i][j] = 0;
			}
		}
		return ret;
	}
	
	/*
	 * Adds the edge, keeping track of all necessary bookkeeping.
	 * (non-Javadoc)
	 * @see Graph#addEdge(int, int, int, boolean)
	 */
	public void addEdge(int u, int v, int weight, boolean dir) {
		if(u >= numVerts) {
			for(int i = numVerts; i <= u; ++i)
				adjList.add(new HashMap<Integer,Integer>());
			numVerts = u + 1;
		}
		if(v >= numVerts) {
			for(int i = numVerts; i <= v; ++i)
				adjList.add(new HashMap<Integer,Integer>());
			numVerts = v + 1;
		}
		
		if(!adjList.get(u).containsKey(v))
			adjList.get(u).put(v, weight);
		else
			return;
		
		if(weight < 0)
			numNegEdges++;
		
		if(!dir && !adjList.get(v).containsKey(u))
			addEdge(v,u,weight,true);
	}
	
	/*
	 * (non-Javadoc)
	 * @see Graph#edgeExists(int, int)
	 */
	public boolean edgeExists(int u, int v) {
		return adjList.get(u).containsKey(v);
	}
	
	/*
	 * (non-Javadoc)
	 * @see Graph#edgeWeight(int, int)
	 */
	public int edgeWeight(int u, int v) {
		if(edgeExists(u,v))
			return adjList.get(u).get(v);
		else
			return 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see Graph#changeEdge(int, int, int, boolean)
	 */
	public void changeEdge(int u, int v, int weight, boolean dir) {
		if(weight < 0 && edgeWeight(u,v) >= 0)
			numNegEdges++;
		else if(weight >= 0 && edgeWeight(u,v) < 0)
			numNegEdges--;
		adjList.get(u).put(v, weight);
		if(!dir)
			changeEdge(v,u,weight,true);
	}
	
	/*
	 * (non-Javadoc)
	 * @see Graph#numVertices()
	 */
	public int numVertices() {
		return numVerts;
	}
	
	/*
	 * (non-Javadoc)
	 * @see Graph#hasNegativeEdges()
	 */
	public boolean hasNegativeEdges() {
		return numNegEdges > 0;
	}

}
