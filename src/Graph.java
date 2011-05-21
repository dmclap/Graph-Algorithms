import java.util.Map;
import java.util.ArrayList;

/*
 * A datastructure for representing an edge.  This is used by a few of our algorithms,
 * and so it's easiest to put it all in one place.  
 */
class Edge implements Comparable<Edge> {
	int u;
	int v;
	int weight = 1;
	public Edge(int u, int v, int weight) {
		this.u = u;
		this.v = v;
		this.weight = weight;
	}
	public int compareTo(Edge other) {
		return this.weight - other.weight;
	}
	public boolean equals(Object o) {
		Edge other = (Edge)o;
		return ((this.u == other.u && this.v == other.v) || 
				(this.u == other.v && this.v == other.u));
	}
	public String toString() {
		return u + " " + v + " " + weight;
	}
}

public abstract class Graph {
	/*
	 * A method to add an edge to the graph, which does nothing if the edge
	 * already exists in the graph.  This version has all of the possible
	 * inputs, and all other versions just call this one with default values.
	 * It also does nothing if the edge already exists (to change an edge's
	 * weight, use changeEdge).
	 * 
	 * u: One endpoint of the edge.  If it's directed, this is the beginning
	 * 		of the edge.
	 * v: The other endpoint, and the end of the edge if it's directed.
	 * weight: The weight of the edge, default 1.
	 * dir: A boolean indicating if the edge is directed (true) or not (false).
	 * 		  Defaults to false.
	 */
	public abstract void addEdge(int u, int v, int weight, boolean dir);
	
	// The rest of these are just shorthands for adding certain kinds of edges
	public void addEdge(int u, int v, int weight) {
		addEdge(u,v,weight,false);
	}
	public void addEdge(int u, int v, boolean dir) {
		addEdge(u,v,1,dir);
	}
	public void addEdge(int u, int v) {
		addEdge(u,v,1,false);
	}
	
	/*
	 * A shorthand for adding directed edges, so you don't need to remember which
	 * way the booleans work.
	 */
	public void addDirectedEdge(int u, int v, int weight) {
		addEdge(u,v,weight,true);
	}
	public void addDirectedEdge(int u, int v) {
		addEdge(u,v,1,true);
	}
	
	/*
	 * Converts this graph and the other one to an adjacency matrix representation,
	 * then checks to see if every element in each matrix is the same.
	 * 
	 * o: The other object, hopefully a graph.
	 */
	public boolean equals(Object o) {
		Graph other;
		try {
			other = (Graph)o;
		}
		catch(ClassCastException e) {
			return false;
		}
		int numVerts = numVertices();
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
	
	/*
	 * Returns whether or not the graph has negative edges.
	 */
	public abstract boolean hasNegativeEdges();
	
	/*
	 * These functions allow us to get different representations of the graph, allowing
	 * different algorithms to choose their representation, where they may operate more
	 * efficiently.
	 */
	public abstract Map<Integer,Integer>[] getAdjList(); 
	public abstract int[][] getAdjMatrix();
	
	/*
	 * Returns the number of vertices in this graph.
	 */
	public abstract int numVertices();
	
	/*
	 * Gets the weight of the edge going from u to v in this graph.  It's abstract
	 * because it will depend upon how the graph is represented internally.
	 * 
	 * u: The starting point of the edge whose weight we want
	 * v: The ending point of the edge whose weight we want
	 */
	public abstract int edgeWeight(int u, int v);
	
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
	public abstract void changeEdge(int u, int v, int weight, boolean dir);
	
	/*
	 * A shorthand for changing a directed edge's weight in both directions.
	 */
	public void changeEdge(int u, int v, int weight) {
		changeEdge(u,v,weight,false);
	}
	
	/*
	 * Returns whether or not an edge exists between u and v, accounting for directedness.
	 * 
	 * u: Starting point of the edge
	 * v: Ending point of the edge
	 */
	public abstract boolean edgeExists(int u, int v);
	
	/*
	 * Returns a string representation of the graph, displaying it as the 
	 * adjacency matrix.
	 */
	public String toString() {
		String ret = "";
		int numVerts = numVertices();
		int[][] adjMatrix = getAdjMatrix();
		for(int i = 0; i < numVerts; ++i) {
			for(int j = 0; j < numVerts; ++j)
				ret += adjMatrix[i][j] + " ";
			ret += "\n";
		}
		return ret;
	}
		
	/*
	 * A method that helps with some of our algorithms, by providing a list of all
	 * of the edges in the graph.  It uses the data structure at the top of the file
	 * to store an edge.  An undirected edge is just represented by having the edge
	 * appear twice; once for u->v and once for v->u.
	 */
	public ArrayList<Edge> getEdgeList() {
		ArrayList<Edge> allEdges = new ArrayList<Edge>();
		// Use the adjacency list, since it will involve less wasted work
		Map<Integer, Integer>[] edges = getAdjList();
		for(int u = 0; u < numVertices(); ++u) {
			for(int v : edges[u].keySet()) {
				int weight = edges[u].get(v);
				Edge e = new Edge(u,v,weight);
				allEdges.add(e);
			}
		}
		return allEdges;
	}
}
