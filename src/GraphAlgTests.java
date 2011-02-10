import junit.framework.TestCase;

// Tests for GraphAlgs.java.  Test names indicate what method they're designed to test.
public class GraphAlgTests extends TestCase {

	Graph g, g2, g3, g4, g5, g6, g7;
	
	protected void setUp() {
		g = new Graph(6);
		// wikipedia's article on dijkstra
		g.addEdge(0,1,7);
		g.addEdge(0,5,14);
		g.addEdge(0,2,9);
		g.addEdge(1,2,10);
		g.addEdge(1,3,15);
		g.addEdge(2,3,11);
		g.addEdge(2,5,2);
		g.addEdge(3,4,6);
		g.addEdge(4,5,9);
		g2 = new Graph(8);
		// http://optlab-server.sce.carleton.ca/POAnimations2007/DijkstrasAlgo.html
		g2.addEdge(0,1,2);
		g2.addEdge(0,3,7);
		g2.addEdge(0,5,12);
		g2.addEdge(0,7,2);
		g2.addEdge(1,3,4);
		g2.addEdge(1,2,1);
		g2.addEdge(1,7,5);
		g2.addEdge(1,4,3);
		g2.addEdge(2,7,4);
		g2.addEdge(2,4,4);
		g2.addEdge(3,4,1);
		g2.addEdge(3,6,5);
		g2.addEdge(4,6,7);
		g2.addEdge(5,6,3);
		// a pair of disconnected K_3's, plus one vert in each component
		g3 = new Graph(8);
		g3.addEdge(0,1);
		g3.addEdge(0,2);
		g3.addEdge(2,1);
		g3.addEdge(1,3);
		g3.addEdge(4,5);
		g3.addEdge(4,6);
		g3.addEdge(6,5);
		g3.addEdge(6,7);
		// a forest
		g4 = new ExpandableGraph();
		g4.addEdge(0,1);
		g4.addEdge(1,2);
		g4.addEdge(1,3);
		g4.addEdge(0,4);
		g4.addEdge(1,5);
		g4.addEdge(6,7);
		g4.addEdge(6,8);
		g4.addEdge(6,9);
		g4.addEdge(9,10);
		g4.addEdge(9,11);
		// same as g4, but add an edge to make it a tree
		g5 = new Graph(12);
		g5.addEdge(0,1);
		g5.addEdge(1,2);
		g5.addEdge(1,3);
		g5.addEdge(0,4);
		g5.addEdge(1,5);
		g5.addEdge(5,6);
		g5.addEdge(6,7);
		g5.addEdge(6,8);
		g5.addEdge(6,9);
		g5.addEdge(9,10);
		g5.addEdge(9,11);
		// wikipedia for max flow problem
		g6 = new ExpandableGraph();
		g6.addDirectedEdge(2,4,2);
		g6.addDirectedEdge(3,4,4);
		g6.addDirectedEdge(3,5,2);
		g6.addDirectedEdge(4,5,3);
		g6.addDirectedEdge(0,1,3);
		g6.addDirectedEdge(0,2,3);
		g6.addDirectedEdge(1,2,2);
		g6.addDirectedEdge(1,3,3);
		// further down that same page
		g7 = new Graph(4);
		g7.addDirectedEdge(0,1,10);
		g7.addDirectedEdge(0,2,5);
		g7.addDirectedEdge(1,2,15);
		g7.addDirectedEdge(1,3,5);
		g7.addDirectedEdge(2,3,10);
	}
	
	public void test_dijkstra() {
		int[] dists = GraphAlgs.dijkstra(g, 0);
		int[] expected = {0,7,9,20,20,11};
		assertEquals(dists.length, expected.length);
		for(int i = 0; i < dists.length; ++i)
			assertEquals(dists[i], expected[i]);
		// tests for g2
		assertEquals(GraphAlgs.dijkstraSP(g,1,4), 21);
		dists = GraphAlgs.dijkstra(g2,7);
		int[]expected2 = {2,4,4,8,7,14,13,0};
		assertEquals(dists.length, expected2.length);
		for(int i = 0; i < dists.length; ++i)
			assertEquals(dists[i], expected2[i]);
		dists = GraphAlgs.dijkstra(g6,0);
		int[] expected3 = {0,3,3,6,5,8};
		assertEquals(dists.length, expected3.length);
		for(int i = 0; i < dists.length; ++i)
			assertEquals(dists[i], expected3[i]);
		dists = GraphAlgs.dijkstra(g7,0);
		int[] expected4 = {0,10,5,15};
		assertEquals(dists.length, expected4.length);
		for(int i = 0; i < dists.length; ++i)
			assertEquals(dists[i], expected4[i]);
	}
	
	public void test_path() {
		assertTrue(GraphAlgs.path(g2,0,4));
		assertTrue(GraphAlgs.path(g,4,2));
		assertTrue(GraphAlgs.path(g3,0,3));
		assertFalse(GraphAlgs.path(g3,0,5));
		assertFalse(GraphAlgs.path(g3,6,2)); 
		assertTrue(GraphAlgs.path(g3,4,7));
		assertTrue(GraphAlgs.path(g4,0, 3));
		assertTrue(GraphAlgs.path(g4,6,11));
		assertTrue(GraphAlgs.path(g4, 0, 1));
		assertFalse(GraphAlgs.path(g4, 0, 8));
		assertFalse(GraphAlgs.path(g4, 4, 10));
		assertFalse(GraphAlgs.path(g4, 7, 3));
		assertFalse(GraphAlgs.path(g4, 10, 4));
		assertFalse(GraphAlgs.path(g4, 11, 5));
		assertTrue(GraphAlgs.path(g5, 0, 8));
		assertTrue(GraphAlgs.path(g6, 1, 3));
		assertFalse(GraphAlgs.path(g6, 3,1));
	}
	
	public void test_tree() {
		assertFalse(GraphAlgs.isTree(g));
		assertFalse(GraphAlgs.isTree(g2));
		assertFalse(GraphAlgs.isTree(g3));
		assertFalse(GraphAlgs.isTree(g4));
		assertTrue(GraphAlgs.isTree(g5));
	}
	
	public void test_findPath() {
		int[] expected1 = {4,5,2,0};
		int[] res = GraphAlgs.findPath(g, 4, 0);
		for(int i = 0; i < expected1.length; ++i) {
			assertEquals(res[i], expected1[i]);
		}
		assertEquals(res.length, expected1.length);
		int[] expected2 = {2,5,4};
		res = GraphAlgs.findPath(g, 2, 4);
		assertEquals(res.length, expected2.length);
		for(int i = 0; i < res.length; ++i)
			assertEquals(res[i], expected2[i]);
		int[] expected3 = {0,1,3,6};
		res = GraphAlgs.findPath(g2,0,6);
		assertEquals(res.length, expected3.length);
		for(int i = 0; i < res.length; ++i)
			assertEquals(res[i], expected3[i]);
		int[] expected4 = {4,3,6,5};
		res = GraphAlgs.findPath(g2,4,5);
		assertEquals(res.length, expected4.length);
		for(int i = 0; i < res.length; ++i)
			assertEquals(res[i], expected4[i]);
		int[] expected5 = null;
		res = GraphAlgs.findPath(g3,0,6);
		assertEquals(expected5,res);
		int[] expected6 = {2,7};
		res = GraphAlgs.findPath(g2,2,7);
		assertEquals(res.length, expected6.length);
		for(int i = 0; i < res.length; ++i)
			assertEquals(res[i], expected6[i]);
		int[] expected7 = {1,2,4};
		res = GraphAlgs.findPath(g6,1,4);
		assertEquals(res.length, expected7.length);
		for(int i = 0; i < res.length; ++i)
			assertEquals(res[i], expected7[i]);
	}
	
	public void test_mst() {
		// since g5 is a tree, it should be the same
		Graph mst = GraphAlgs.minSpanningTree(g5);
		assertEquals(mst,g5);
		Graph expected = new Graph(6);
		expected.addEdge(2,5,2);
		expected.addEdge(3,4,6);
		expected.addEdge(0,1,7);
		expected.addEdge(4,5,9);
		expected.addEdge(2,0,9);
		mst = GraphAlgs.minSpanningTree(g);
		assertEquals(mst,expected);
		expected = new Graph(8);
		expected.addEdge(1,2,1);
		expected.addEdge(3,4,1);
		expected.addEdge(1,0,2);
		expected.addEdge(0,7,2);
		expected.addEdge(5,6,3);
		expected.addEdge(1,4,3);
		expected.addEdge(1,3,4);
		mst = GraphAlgs.minSpanningTree(g2);
		assertEquals(mst,expected);
	}
	
	public void test_maxflow() {
		int res = GraphAlgs.maxFlow(g6, 0, 5);
		assertEquals(res,5);
		res = GraphAlgs.maxFlow(g7,0,3);
		assertEquals(res,15);
	}
	
	public void test_allPairsSP() {
		int[][] expected = {{0,7,9,20,20,11},
				{7,0,10,15,21,12},
				{9,10,0,11,11,2},
				{20,15,11,0,6,13},
				{20,21,11,6,0,9},
				{11,12,2,13,9,0}};
		int[][] res = GraphAlgs.allPairsSP(g);
		int n = g.numVertices();
		for(int i = 0; i < n; ++i) {
			for(int j = 0; j < n; ++j)
				assertEquals(expected[i][j], res[i][j]);
		}
	}

}
