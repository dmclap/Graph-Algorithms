import java.util.LinkedList;
import java.util.Queue;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;

// TODO: TSP?  Graph coloring (using maximal independent sets)?
// TODO: Abstract away the process of getting the list of edges
public class GraphAlgs {
	/*
	 * Uses Dijkstra's algorithm to find the shortest paths from a particular
	 * vertex to all other vertices.  Can return either an array of the shortest
	 * path values, or an array that can be used to reconstruct the path in the
	 * original graph.  It keeps track of the vertices to be explored using a heap
	 * as its priority queue.  
	 *  
	 * g: The graph on which to compute the shortest paths.
	 * source: The index of the source vertex from which to compute paths.
	 * dists: A flag determining whether to return the distances or the path reconstruction.
	 */
	public static int[] dijkstra(Graph g, int source, boolean dists) {
		int numVerts = g.numVertices();
		int[] dist = new int[numVerts];
		int[] prev = new int[numVerts];
		boolean[] visited = new boolean[numVerts];
		// initialize the dists and prev arrays
		for(int i = 0; i < dist.length; ++i) {
			visited[i] = false;
			prev[i] = -1;
			if(i == source) {
				dist[i] = 0;
				prev[i] = source;
			}
			else
				dist[i] = Integer.MAX_VALUE;
		}
		Heap h = new FixedHeap(numVerts);
		h.add(source, 0);
		while(!h.isEmpty()) {
			int current;
			int val;
			// Get the next closest vertex to the source from our heap
			try {
				val = h.peekTopValue();
				current = h.dequeue();
			}
			catch(Exception e) {
				System.out.println("Done!  Too early!");
				break;
			}
			// This check should always pass, but just in case.
			if(!visited[current]) {
				dist[current] = val;
				Map<Integer, Integer>[] edgeSet = g.getAdjList();
				Map<Integer, Integer> edges = edgeSet[current];
				// check all edges adjacent to that vertex, updating distances if necessary
				for(int e : edges.keySet()) {
					if(!visited[e]) {
						if(h.hasKey(e) && !visited[e] && (h.getValue(e) > (val + edges.get(e)))) {
							h.updateKey(e, val + edges.get(e));
						}
						else if(!h.hasKey(e) && !visited[e]) {
							h.add(e, dist[current] + edges.get(e));
						}
					}
				}
				visited[current] = true;
			}
		}
		// return the right value
		if(dists)
			return dist;
		else
			return prev;
	}
	
	/*
	 * Same as above, but returns the distances array by default.
	 */
	public static int[] dijkstra(Graph g, int source) {
		return dijkstra(g, source, true);
	}
	
	
	/*
	 * Uses Dijkstra's algorithm to find the shortest paths from a particular
	 * vertex to all other vertices.  Can return either an array of the shortest
	 * path values, or an array that can be used to reconstruct the path in the
	 * original graph.  This is now deprecated, since dijkstra, which uses an
	 * efficient priority queue instead of a linear search, can run the algorithm
	 * more quickly.
	 * 
	 * g: The graph on which to compute the shortest paths.
	 * source: The index of the source vertex from which to compute paths.
	 * dists: A flag determining whether to return the distances or the path reconstruction.
	 */
	public static int[] slowDijkstra(Graph g, int source, boolean dists) {
		int numVerts = g.numVertices();
		int[] dist = new int[numVerts];
		int[] prev = new int[numVerts];
		boolean[] visited = new boolean[numVerts];
		// initialize the dists and prev arrays
		for(int i = 0; i < dist.length; ++i) {
			visited[i] = false;
			prev[i] = -1;
			if(i == source) {
				dist[i] = 0;
				prev[i] = source;
			}
			else
				dist[i] = Integer.MAX_VALUE;
		}
		while(true) {
			int current = -1;
			int min =  Integer.MAX_VALUE;
			// find the next closest vertex not yet explored
			for(int i = 0; i < numVerts; ++i) {
				if(!visited[i] && dist[i] < min) {
					current = i;
					min = dist[i];
				}
			}
			// if we didn't find one, we've found every vertex reachable from the source
			if(current == -1)
				break;
			Map<Integer, Integer>[] edgeSet = g.getAdjList();
			Map<Integer, Integer> edges = edgeSet[current];
			// check all edges adjacent to that vertex, updating distances if necessary
			for(int e : edges.keySet()) {
				if(!visited[e]) {
					if(dist[e] > (dist[current] + edges.get(e))) {
						dist[e] = dist[current] + edges.get(e);
						prev[e] = current;
					}
				}
			}
			visited[current] = true;
		}
		// return the right value
		if(dists)
			return dist;
		else
			return prev;
	}
	
	/*
	 * Same as above, but defaults to asking for the distances.
	 */
	public static int[] slowDijkstra(Graph g, int source) {
		return slowDijkstra(g, source, true);
	}
	
	
	/*
	 * Uses Breadth-First Search to determine if there exists a path from
	 * source to sink in the given graph.
	 * 
	 * g: The graph to determine the presence of a path on.
	 * source: The source, or starting vertex for the path.
	 * sink: The sink, or ending vertex.
	 */
	public static boolean path(Graph g, int source, int sink) {
		Queue<Integer> q = new LinkedList<Integer>();
		q.add(source);
		Map<Integer, Integer>[] edges = g.getAdjList();
		boolean[] visited = new boolean[g.numVertices()];
		while(!q.isEmpty()) {
			int current = q.poll();
			Map<Integer,Integer> e = edges[current];
			for(int vert : e.keySet()) {
				if(vert == sink)
					return true;
				if(!visited[vert])
					q.add(vert);
			}
			visited[current] = true;
		}
		return false;
	}
	
	/*
	 * Uses Dijkstra's algorithm to determine the shortest path between two
	 * vertices.  It's just a shorthand for accessing the appropriate element
	 * of the array returned by dijkstra.
	 * 
	 * g: As usual, the graph to operate on.
	 * source: The starting vertex for the path.
	 * sink: The ending vertex.
	 */
	public static int slowDijkstraSP(Graph g, int source, int sink) {
		int[] dists = slowDijkstra(g, source, true);
		return dists[sink];
	}
	
	/*
	 * Finds a path between two particular vertices in a graph.  Returns the
	 * path as an array whose first value is source, final value is sink,
	 * and all the other numbers are the vertices to visit, in the order
	 * they should be visited by the path.  Since this uses Dijkstra's
	 * algorithm, it should return the shortest path, assuming no negative
	 * edge weights.
	 * 
	 * g: Same as always.
	 * source: The starting vertex of the path.
	 * sink: The ending vertex.
	 */
	public static int[] findPath(Graph g, int source, int sink) {
		ArrayList<Integer> path = new ArrayList<Integer>();
		int[] prev = slowDijkstra(g,source,false);
		int current = sink;
		path.add(sink);
		while(current != source) {
			current = prev[current];
			path.add(current);
			if(current == -1) // if there's not actually a path
				return null;
		}
		Collections.reverse(path);
		int[] ret = new int[path.size()];
		for(int i = 0; i < path.size(); ++i)
			ret[i] = path.get(i);
		return ret;
	}
	
	/* Checks if a graph is a tree by determining if it has n-1 edges and is connected
	 * (it checks the latter by seeing if there is a path from vertex 0 to all others).
	 * Only works for undirected graphs.
	 * 
	 * g: You know the drill.
	 */
	public static boolean isTree(Graph g) {
		// step one: count the edges
		Map<Integer, Integer>[] edges = g.getAdjList();
		int count = 0;
		for(int i = 0; i < edges.length; ++i) {
			count += edges[i].keySet().size();
			if(count > 2*g.numVertices()) // shortcut early if it's clearly not
				return false;
		}
		if(count != 2*g.numVertices() - 2)
			return false;
		// step two: make sure there's a path from 0 to all the others
		int[] prevs = slowDijkstra(g,0,false);
		for(int i = 0; i < prevs.length; ++i)
			if(prevs[i] < 0)
				return false;
		return true;
	}
	
	/*
	 * Returns a new graph that represents the minimum spanning tree of the input
	 * graph.  Does so using Kruskal's algorithm.
	 * 
	 * g: See above.
	 */
	public static Graph minSpanningTree(Graph g) {
		Graph mst = new Graph(g.numVertices());
		// A private class that makes it easier to sort the edges.
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
		}
		ArrayList<Edge> allEdges = new ArrayList<Edge>();
		// Use the adjacency list, since it will involve less wasted work
		Map<Integer, Integer>[] edges = g.getAdjList();
		for(int u = 0; u < g.numVertices(); ++u) {
			for(int v : edges[u].keySet()) {
				int weight = edges[u].get(v);
				Edge e = new Edge(u,v,weight);
				// only add this edge if it's not there yet
				if(!allEdges.contains(e))
					allEdges.add(e);
			}
		}
		// Now, add edges in increasing order of weight, but only if they don't
		// form a cycle in the graph being constructed.
		Collections.sort(allEdges);
		int count = 0;
		int target = g.numVertices() - 1;
		for(Edge e : allEdges) {
			int u = e.u;
			int v = e.v;
			int weight = e.weight;
			if(path(g,u,v)) { // only add this edge if you can't reach u from v
				mst.addEdge(u, v, weight);
				++count;
			}
			if(count == target) // if it's already a tree, we're done
				break;
		}
		return mst;
	}
	
	/*
	 * Finds the value of the max flow from one vertex to another in the given
	 * graph, which is assumed to be directed.  Uses the Ford-Fulkerson algorithm
	 * to compute this flow, and includes a capacity for debug statements that
	 * will show work along the way.
	 * 
	 * g: Really?  Still need to know?
	 * source: The starting vertex for the flow.
	 * sink: The ending vertex for the flow.
	 * debug: Whether or not to print intermediate values.
	 */
	public static int maxFlow(Graph g, int source, int sink, boolean debug) {
		Graph resid = new Graph (g); // the residual graph for the algorithm
		int flow = 0;
		while(true) {
			// Find a path on the residual graph
			int[] path = findPath(resid, source, sink);
			if(path == null)
				break;
			else if(debug) {
				System.out.print("Found augmenting path: ");
				for(int i = 0; i < path.length; ++i)
					System.out.print(path[i] + " ");
				System.out.println();
			}
			// Find the minimum amount of remaining capacity on the path
			int min = Integer.MAX_VALUE;
			for(int i = 0; i < path.length - 1; ++i) {
				int weight = resid.edgeWeight(path[i], path[i+1]);
				if(weight < min)
					min = weight;
			}
			// Add flow to the path, adjusting the residual graph to reflect this
			for(int i = 0; i < path.length - 1; ++i) {
				resid.modifyWeight(path[i], path[i+1], -1*min, true);
				resid.modifyWeight(path[i+1], path[i], min, true);
			}
			flow += min;
			if(debug) {
				System.out.println("Flow increased by " + min);
				System.out.println("New Residual Graph:");
				System.out.println(resid);
			}
		}
		return flow;
	}
	
	/*
	 * A simpler version of the max flow function that disables debug statements.
	 * All variables are the same as above.
	 */
	public static int maxFlow(Graph g, int source, int sink) {
		return maxFlow(g,source,sink,false);
	}
	
	/*
	 * Uses the Floyd-Warshall algorithm to determine the shortest path between
	 * all pairs of vertices in a given graph.
	 * 
	 * g: The same graph as all the others.
	 */
	public static int[][] allPairsSP(Graph g) {
		int n = g.numVertices();
		// initializing the array
		int[][] path = new int[n][n];
		for(int i = 0; i < n; ++i) {
			for(int j = 0; j < n; ++j) {
				if(g.edgeWeight(i, j) > 0)
					path[i][j] = g.edgeWeight(i, j);
				else if(i != j)
					path[i][j] = Integer.MAX_VALUE/2;
				else
					path[i][j] = 0;
			}
		}
		// running the algorithm
		for(int k = 0; k < n; ++k) {
			for(int j = 0; j < n; ++j) {
				for(int i = 0; i < n; ++i) {
					path[i][j] = Math.min(path[i][j], path[i][k] + path[k][j]);
				}
			}
		}
		return path;
	}
	
	/*
	 * A shorthand for finding the shortest path between two vertices using Dijkstra's algorithm.
	 * It just calls the algorithm that computes all shortest paths, and extracts the one value
	 * we want.
	 * 
	 * g: Same as always.
	 * source: The starting vertex for the path.
	 * sink: The ending vertex for the path.
	 */
	public static int dijkstraSP(Graph g, int source, int sink) {
		int[] dists = dijkstra(g, source, true);
		return dists[sink];
	}
	
	/*
	 * Finds the shortest path from a particular source vertex to all other vertices using
	 * the Bellman-Ford algorithm.  This algorithm is slower, but also works in the presence
	 * of negative edge weights, and so has value in those situations.  It uses a similar
	 * calling convention as Dijkstra, but has an additional boolean to check for negative
	 * cost cycles.  Also similar to Dijksra, there are other versions that give default
	 * values for the boolean arguments, as well as a shorthand for finding the shortest path
	 * between just two points.
	 * 
	 * g: The graph, as usual.
	 * source: The vertex from which to find the shortest path to all other vertices.
	 * dist: A boolean indicating whether to return the length of all the shortest paths (if
	 * 		the variable is true) or an array that can be used to reconstruct the paths (if
	 * 		the variable is false)
	 * check: Whether or not to check for a negative cost cycle at the end.  It's an O(|E|)
	 * 		operation, so it's not expensive asymptotically, but there's no point in doing it
	 * 		if you know there isn't one.
	 */
	public static int[] bellmanFord(Graph g, int source, boolean dist, boolean check) {
		int n = g.numVertices();
		int[] dists = new int[n];
		int[] prev=  new int[n];
		// Initialize the arrays
		for(int i = 0; i < n; ++i) {
			prev[i] = -1;
			if(i == source)
				dists[i] = 0;
			else
				dists[i] = Integer.MAX_VALUE/2;
		}
		// Collect all the edges in the graph using the same process as in MST
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
		}
		ArrayList<Edge> allEdges = new ArrayList<Edge>();
		// Use the adjacency list, since it will involve less wasted work
		Map<Integer, Integer>[] edges = g.getAdjList();
		for(int u = 0; u < g.numVertices(); ++u) {
			for(int v : edges[u].keySet()) {
				int weight = edges[u].get(v);
				Edge e = new Edge(u,v,weight);
				allEdges.add(e);
			}
		}
		// Compute the distances
		for(int i = 0; i < n-1; ++i) {
			for(Edge e : allEdges) {
				if(dists[e.u] + e.weight < dists[e.v]) {
					dists[e.v] = dists[e.u] + e.weight;
					prev[e.v] = e.u;
				}
			}
		}
		// check for negative cost cycles, if asked
		if(check) {
			for(Edge e : allEdges) {
				if(dists[e.u] + e.weight < dists[e.v]) {
					System.out.println("Negative cost cycle!");
					return null;
				}
			}
		}
		if(dist)
			return dists;
		else
			return prev;
	}
	public static int[] bellmanFord(Graph g, int source, boolean dist) {
		return bellmanFord(g,source,dist,false);
	}
	public static int[] bellmanFord(Graph g, int source) {
		return bellmanFord(g,source,true,false);
	}
	
	public static int bellmanFordSP(Graph g, int source, int sink) {
		int[] dists = bellmanFord(g,source);
		return dists[sink];
	}
	
	public static void main(String[] args) {
		System.out.println("See GraphAlgTests.java");
	}
}
