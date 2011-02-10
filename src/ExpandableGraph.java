import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* 
 * A version of the Graph class that doesn't need to know how many vertices there
 * are in the graph when it begins.  It keeps track of the adjacency list as a
 * resizable data structure to allow the graph to expand to the number of
 * distinct vertices that are input.
*/
public class ExpandableGraph extends Graph {
	private ArrayList<Map<Integer, Integer>> adjList;
	private int numVerts;
	public ExpandableGraph(int n) {
		adjList = new ArrayList<Map<Integer,Integer>>(n);
		numVerts = n;
		for(int i = 0; i < n; ++i)
			adjList.add(new HashMap<Integer,Integer>());
	}
	
	public ExpandableGraph() {
		adjList = new ArrayList<Map<Integer,Integer>>();
		numVerts = 0;
	}

	public ExpandableGraph(Graph g) {
		numVerts = g.numVertices();
		Map<Integer,Integer>[] other = g.getAdjList();
		adjList = new ArrayList<Map<Integer,Integer>>(numVerts);
		for(int i = 0; i < other.length; ++i) {
			HashMap<Integer,Integer> next = new HashMap<Integer,Integer>();
			for(int key : other[i].keySet()) {
				next.put(key, other[i].get(key));
			}
			adjList.add(next);
		}
	}
	public Map<Integer,Integer>[] getAdjList() {
		HashMap<Integer,Integer>[] t = new HashMap[1]; 
		return adjList.toArray(t);
	}
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
		if(!dir && !adjList.get(v).containsKey(u))
			adjList.get(v).put(u, weight);
	}
	public boolean edgeExists(int u, int v) {
		return adjList.get(u).containsKey(v);
	}
	public int edgeWeight(int u, int v) {
		return adjList.get(u).get(v);
	}
	public void changeEdge(int u, int v, int weight, boolean dir) {
		adjList.get(u).put(v, weight);
		if(!dir)
			adjList.get(v).put(u, weight);
	}
	public boolean equals(Object o) {
		Graph other = (Graph)o;
		int[][] otherMat = other.getAdjMatrix();
		int[][] thisMat = getAdjMatrix();
		if(numVerts != other.numVertices())
			return false;
		for(int i = 0; i < numVerts; ++i) {
			for(int j = 0; j < numVerts; ++j)
				if(otherMat[i][j] != thisMat[i][j])
					return false;
		}
		return true;
	}
	public String toString() {
		String ret = "";
		int[][] adjMatrix = getAdjMatrix();
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
