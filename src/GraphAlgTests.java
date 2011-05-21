import junit.framework.TestCase;

// Tests for GraphAlgs.java.  Test names indicate what method they're designed to test.
public class GraphAlgTests extends TestCase {

	Graph g, g2, g3, g4, g5, g6, g7, g8;
	Heap h, h2;
	
	protected void setUp() {
		g = new FixedGraph(6);
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
		g2 = new FixedGraph(8);
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
		g3 = new FixedGraph(8);
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
		g5 = new FixedGraph(12);
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
		g7 = new FixedGraph(4);
		g7.addDirectedEdge(0,1,10);
		g7.addDirectedEdge(0,2,5);
		g7.addDirectedEdge(1,2,15);
		g7.addDirectedEdge(1,3,5);
		g7.addDirectedEdge(2,3,10);
		h = new FixedHeap(13);
		h.add(0,10);
		h.add(1,5);
		h.add(2,13);
		h.add(3,7);
		h.add(4,25);
		h.add(5,15);
		h.add(6,20);
		h.add(7,45);
		h.add(8,30);
		h.add(9,2);
		h2 = new ExpandableHeap(13);
		h2.add(0,10);
		h2.add(1,5);
		h2.add(2,13);
		h2.add(3,7);
		h2.add(4,25);
		h2.add(5,15);
		h2.add(6,20);
		h2.add(7,45);
		h2.add(8,30);
		h2.add(9,2);
		// http://www.cs.princeton.edu/~wayne/cs423/lectures/bellman-ford-4up.pdf
		g8 = new FixedGraph(8);
		g8.addEdge(0, 1, 9, true);
		g8.addEdge(0, 6, 15, true);
		g8.addEdge(0, 5, 6, true);
		g8.addEdge(1, 2, 10, true);
		g8.addEdge(2, 4, -16, true);
		g8.addEdge(2, 7, 19, true);
		g8.addEdge(3, 2, 6, true);
		g8.addEdge(3, 7, 6, true);
		g8.addEdge(4, 3, 11, true);
		g8.addEdge(4, 7, 16, true);
		g8.addEdge(5, 2, 18, true);
		g8.addEdge(5, 4, 30, true);
		g8.addEdge(5, 6, -8, true);
		g8.addEdge(6, 4, 20, true);
		g8.addEdge(6, 7, 44, true);
	}
	
	public void test_slowDijkstra() {
		int[] dists = GraphAlgs.slowDijkstra(g, 0);
		int[] expected = {0,7,9,20,20,11};
		assertEquals(dists.length, expected.length);
		for(int i = 0; i < dists.length; ++i)
			assertEquals(dists[i], expected[i]);
		// tests for g2
		assertEquals(GraphAlgs.slowDijkstraSP(g,1,4), 21);
		dists = GraphAlgs.slowDijkstra(g2,7);
		int[]expected2 = {2,4,4,8,7,14,13,0};
		assertEquals(dists.length, expected2.length);
		for(int i = 0; i < dists.length; ++i)
			assertEquals(dists[i], expected2[i]);
		dists = GraphAlgs.slowDijkstra(g6,0);
		int[] expected3 = {0,3,3,6,5,8};
		assertEquals(dists.length, expected3.length);
		for(int i = 0; i < dists.length; ++i)
			assertEquals(dists[i], expected3[i]);
		dists = GraphAlgs.slowDijkstra(g7,0);
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
		FixedGraph expected = new FixedGraph(6);
		expected.addEdge(2,5,2);
		expected.addEdge(3,4,6);
		expected.addEdge(0,1,7);
		expected.addEdge(4,5,9);
		expected.addEdge(2,0,9);
		mst = GraphAlgs.minSpanningTree(g);
		assertEquals(mst,expected);
		expected = new FixedGraph(8);
		expected.addEdge(1,2,1);
		expected.addEdge(3,4,1);
		expected.addEdge(1,0,2);
		expected.addEdge(0,7,2);
		expected.addEdge(5,6,3);
		expected.addEdge(1,4,3);
		expected.addEdge(3,6,5);
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
	
	public void test_fixedHeapBasic() {
		int curr = 0;
		int weight = h.peekTopValue();
		assertEquals(weight,2);
		int val = h.dequeue();
		assertEquals(val,9);
		val = h.dequeue();
		assertEquals(val,1);
		int[] expected = {3,0,2,5,10,6,4,11,8,7};
		h.add(10,17);
		h.add(11,27);
		while(!h.isEmpty()) {
			int top = h.dequeue();
			assertEquals(top, expected[curr]);
			++curr;
		}
	}
	
	public void test_fixedHeapUpdateKey() {
		h.updateKey(9, 110);
		h.updateKey(8, 0);
		h.updateKey(2, 1);
		int[] expected = {8,2,1,3,0,5,6,4,7,9};
		int curr = 0;
		while(!h.isEmpty()) {
			int top = h.dequeue();
			assertEquals(top, expected[curr]);
			++curr;
		}
	}
	
	public void test_expandableHeapBasic() {
		int curr = 0;
		int weight = h2.peekTopValue();
		assertEquals(weight,2);
		int val = h2.dequeue();
		assertEquals(val,9);
		val = h2.dequeue();
		assertEquals(val,1);
		int[] expected = {3,0,2,5,10,6,4,11,8,7};
		h2.add(10,17);
		h2.add(11,27);
		while(!h2.isEmpty()) {
			int top = h2.dequeue();
			assertEquals(top, expected[curr]);
			++curr;
		}
	}
	
	public void test_expandableHeapUpdateKey() {
		h2.updateKey(9, 110);
		h2.updateKey(8, 0);
		h2.updateKey(2, 1);
		int[] expected = {8,2,1,3,0,5,6,4,7,9};
		int curr = 0;
		while(!h2.isEmpty()) {
			int top = h2.dequeue();
			assertEquals(top, expected[curr]);
			++curr;
		}
	}

	public void test_fasterDijkstra() {
		int[] dists = GraphAlgs.dijkstra(g, 0);
		int[] expected = {0,7,9,20,20,11};
		assertEquals(dists.length, expected.length);
		for(int i = 0; i < dists.length; ++i) {
			assertEquals(dists[i], expected[i]);
		}
		// tests for g2
		assertEquals(GraphAlgs.dijkstraSP(g,1,4), 21);
		dists = GraphAlgs.dijkstra(g2,7);
		int[] expected2 = {2,4,4,8,7,14,13,0};
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
	
	public void test_bellmanFord() {
		int[] dists = GraphAlgs.bellmanFord(g, 0);
		int[] expected = {0,7,9,20,20,11};
		assertEquals(dists.length, expected.length);
		for(int i = 0; i < dists.length; ++i) {
			assertEquals(dists[i], expected[i]);
		}
		// tests for g2
		assertEquals(GraphAlgs.bellmanFordSP(g,1,4), 21);
		dists = GraphAlgs.bellmanFord(g2,7);
		int[] expected2 = {2,4,4,8,7,14,13,0};
		assertEquals(dists.length, expected2.length);
		for(int i = 0; i < dists.length; ++i)
			assertEquals(dists[i], expected2[i]);
		dists = GraphAlgs.bellmanFord(g6,0);
		int[] expected3 = {0,3,3,6,5,8};
		assertEquals(dists.length, expected3.length);
		for(int i = 0; i < dists.length; ++i)
			assertEquals(dists[i], expected3[i]);
		dists = GraphAlgs.bellmanFord(g7,0);
		int[] expected4 = {0,10,5,15};
		assertEquals(dists.length, expected4.length);
		for(int i = 0; i < dists.length; ++i)
			assertEquals(dists[i], expected4[i]);
	}
	
	public void test_negativeEdges() {
		FixedGraph gt = new FixedGraph(g8);
		assertTrue(g8.hasNegativeEdges());
		assertFalse(g4.hasNegativeEdges());
		g8.changeEdge(2, 4, 5);
		g8.changeEdge(5, 6, 10);
		assertFalse(g8.hasNegativeEdges());
		g4.changeEdge(6,7,-2);
		assertTrue(g4.hasNegativeEdges());
		assertTrue(gt.hasNegativeEdges());
		gt.changeEdge(2, 4, 5);
		gt.changeEdge(5, 6, 10);
		assertFalse(gt.hasNegativeEdges());
	}
	
	public void test_shortestPath() {
		int[] expected = {0,9,19,14,3,6,-2,19};
		int[] bf = GraphAlgs.bellmanFord(g8, 0);
		assertEquals(g8.hasNegativeEdges(), true);
		int[] sp = GraphAlgs.shortestPath(g8, 0);
		assertEquals(expected.length, bf.length);
		assertEquals(expected.length, sp.length);
		Exception exp = null;
		try {
			GraphAlgs.dijkstra(g8, 0);
		}
		catch(IllegalArgumentException e) {
			exp = e;
		}
		assertNotNull(exp);
		for(int i = 0; i < sp.length; ++i) {
			assertEquals(expected[i], sp[i]);
			assertEquals(expected[i], bf[i]);
		}
	}
	
	// Tests the consistency between dijkstra's algorithm, Floyd-Warshall, and Bellman-Ford
	// on all of our graphs.  Helps to show more rigorously that they're all working right,
	// since it's unlikely that they would all fail in the same way
	public void testConsistency() {
		int[][] fwSPs = GraphAlgs.allPairsSP(g);
		for(int i = 0; i < g.numVertices(); ++i) {
			for(int j = 0; j < g.numVertices(); ++j) {
				assertEquals(GraphAlgs.dijkstraSP(g, i, j), fwSPs[i][j]);
				assertEquals(GraphAlgs.bellmanFordSP(g, i, j), fwSPs[i][j]);
			}
		}
		fwSPs = GraphAlgs.allPairsSP(g2);
		for(int i = 0; i < g2.numVertices(); ++i) {
			for(int j = 0; j < g2.numVertices(); ++j) {
				assertEquals(GraphAlgs.dijkstraSP(g2, i, j), fwSPs[i][j]);
				assertEquals(GraphAlgs.bellmanFordSP(g2, i, j), fwSPs[i][j]);
			}
		}
		fwSPs = GraphAlgs.allPairsSP(g3);
		for(int i = 0; i < g3.numVertices(); ++i) {
			for(int j = 0; j < g3.numVertices(); ++j) {
				assertEquals(GraphAlgs.dijkstraSP(g3, i, j), fwSPs[i][j]);
				assertEquals(GraphAlgs.bellmanFordSP(g3, i, j), fwSPs[i][j]);
			}
		}
		fwSPs = GraphAlgs.allPairsSP(g4);
		for(int i = 0; i < g4.numVertices(); ++i) {
			for(int j = 0; j < g4.numVertices(); ++j) {
				assertEquals(GraphAlgs.dijkstraSP(g4, i, j), fwSPs[i][j]);
				assertEquals(GraphAlgs.bellmanFordSP(g4, i, j), fwSPs[i][j]);
			}
		}
		fwSPs = GraphAlgs.allPairsSP(g5);
		for(int i = 0; i < g5.numVertices(); ++i) {
			for(int j = 0; j < g5.numVertices(); ++j) {
				assertEquals(GraphAlgs.dijkstraSP(g5, i, j), fwSPs[i][j]);
				assertEquals(GraphAlgs.bellmanFordSP(g5, i, j), fwSPs[i][j]);
			}
		}
		fwSPs = GraphAlgs.allPairsSP(g6);
		for(int i = 0; i < g6.numVertices(); ++i) {
			for(int j = 0; j < g6.numVertices(); ++j) {
				assertEquals(GraphAlgs.dijkstraSP(g6, i, j), fwSPs[i][j]);
				assertEquals(GraphAlgs.bellmanFordSP(g6, i, j), fwSPs[i][j]);
			}
		}
		fwSPs = GraphAlgs.allPairsSP(g7);
		for(int i = 0; i < g7.numVertices(); ++i) {
			for(int j = 0; j < g7.numVertices(); ++j) {
				assertEquals(GraphAlgs.dijkstraSP(g7, i, j), fwSPs[i][j]);
				assertEquals(GraphAlgs.bellmanFordSP(g7, i, j), fwSPs[i][j]);
			}
		}
		fwSPs = GraphAlgs.allPairsSP(g8);
		for(int i = 0; i < g8.numVertices(); ++i) {
			for(int j = 0; j < g8.numVertices(); ++j) {
				// negative edge weights warning!
				//assertEquals(GraphAlgs.dijkstraSP(g8, i, j), fwSPs[i][j]);
				assertEquals(GraphAlgs.bellmanFordSP(g8, i, j), fwSPs[i][j]);
			}
		}
	}
}
