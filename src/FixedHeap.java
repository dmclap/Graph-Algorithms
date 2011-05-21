/*
 * A special implementation of the Heap datastructure, optimized for Dijkstra's 
 * algorithm.  It provides O(log n) operations for enqueueing, dequeueing, and
 * updating keys.  This is an improvement over Java's PriorityQueue, which does
 * not have the last operation, and an attempt to perform a similar operation would
 * take O(n) time.  It supports a key/value pair system, where the keys are stored
 * in the heap itself, but are ordered by the values they map to.  This is used for
 * Dijkstra's algorithm by having keys correspond to vertices, and values
 * correspond to the distance from the source.
 */
public class FixedHeap implements Heap {
	private int[] items; // the actual heap, which orders vertices
	private int[] locations; // maps vertices to their location in the heap
	private int end; // one past the last real value in our array
	// maps vertices to their hashed values; in this case, the distances they are from
	// the original source vertex.
	private int[] distances; 
	
	/* This version requires that you know the largest number that will be used as 
	 * a key.  For the purposes of Dijkstra's algorithm, this is just the number of
	 * vertices. 
	 */
	public FixedHeap(int num) {
		items = new int[num+1];
		locations = new int[num+1];
		distances = new int[num+1];
		for(int i = 0; i < num+1; ++i) {
			items[i] = -4; // a nice sentinel value, since our values have to be positive
			locations[i] = -4;
			distances[i] = -4;
		}
		end = 1;
	}
	
	/*
	 * Removes the minimum element from the heap, re-asserts the heap property
	 * among the remaining elements, and returns the minimum value.
	 */
	public int dequeue() {
		if(end > 2) {
			// Get the item currently at the top of the heap
			int top = items[1];
			// Move the currently last item to the top
			items[1] = items[end-1];
			// Clean up the data from the removed item
			items[end-1] = -4;
			end--;
			locations[top] = -4;
			distances[top] = -4;
			// Restore the heap property, centering around the item we moved
			heapify(1);
			return top;
		}
		// A special case if this empties the heap, since we don't need to heapify.
		else if(end == 2) {
			int top = items[1];
			items[1] = -4;
			locations[top] = -4;
			distances[top] = -4;
			end = 1;
			return top;
		}
		return -1;
	}
	
	/*
	 * A helper function that restores the heap property of the array.  It assumes
	 * that only one item, the one at index, is out of place.  The value of the heap
	 * is that this only takes O(log n) time.
	 * 
	 * index: The location of the item that is potentially out of place.
	 */
	private void heapify(int index) { 
		int leftChild = Integer.MAX_VALUE;
		int rightChild = Integer.MAX_VALUE;
		int curr = 0;
		if(index < end)
			curr = distances[items[index]];
		else
			return;
		// Get the candidate values that this might switch with.
		if(2*index < end)
			leftChild = distances[items[2*index]];
		if((2*index+1) < end)
			try {
				rightChild = distances[items[2*index+1]];
			}catch(Exception e) {} // we already have a default value
		int parent;
		if(index > 1)
			parent = distances[items[index/2]];
		else
			parent = -1;
		if(parent > curr) { // this item needs to be promoted
			swap(index, index/2);
			heapify(index/2); // this item may still need to move
		}
		// this item needs to be pushed down the heap
		else if(curr > leftChild || curr > rightChild) {
			// swap with the smaller child
			if(rightChild < leftChild) {
				swap(index, 2*index+1);
				heapify(2*index+1);
			}
			// swap with the larger child, if it's not a sentinel
			else if(leftChild < Integer.MAX_VALUE/2) {
				swap(index, 2*index);
				heapify(2*index);
			}
		}
		// If it's in the right place now, we're done 
	}
	
	/*
	 * Adds a new item to this queue, maintaining the heap property.
	 * 
	 * newItem: The key of the item to be added; in this case, the vertex.
	 * value: The value of the item to be added, the distance.
	 */
	public void add(int newItem, int value) {
		items[end] = newItem; // add it to the end of the list
		locations[newItem] = end;
		distances[newItem] = value;
		end++;
		heapify(end-1); // maintain the heap property
	}
	
	/*
	 * Updates the value behind the given key, changing the structure of
	 * the heap if necessary.  It's for this function that we had to write
	 * our own priority queue implementation; since this will actually
	 * run in O(log n) time.
	 * 
	 * key: The key of the item whose value needs to be updated (in our case,
	 * 		the vertex to be updated).
	 * newVal: The new value for that key.
	 */
	public void updateKey(int key, int newVal) {
		distances[key] = newVal;
		int index = locations[key];
		heapify(index);
	}
	
	/*
	 * Swaps the items at the two given indices in the heap.  This is more
	 * complicated than just swapping the elements, since we also need to 
	 * update our hash table that tracks where every item is.
	 * 
	 * firstIndex: One of the indices to be swapped.
	 * secondIndex: The other index.
	 */
	private void swap(int firstIndex, int secondIndex) {
		int firstVert = items[firstIndex];
		int secondVert = items[secondIndex];
		locations[firstVert] = secondIndex;
		locations[secondVert] = firstIndex;
		items[firstIndex] = secondVert;
		items[secondIndex] = firstVert;
	}
	
	/*
	 * A function that returns whether or not the heap is empty.  It's correct,
	 * because we start indexing at 1 for the heap.  As such, end will be 1 when
	 * there's nothing there, since end is one after the first element, and there
	 * will never be an element in position 0.
	 */
	public boolean isEmpty() {
		return end < 2;
	}
	
	/*
	 * String representation of the heap; just prints out the array.
	 */
	public String toString() {
		String ret = "";
		for(int i = 1; i < end+1; ++i)
			ret += items[i] + " ";
		return ret;
	}
	
	/*
	 * Indicates whether or not the given key is being stored in the
	 * heap.
	 */
	public boolean hasKey(int key) {
		return distances[key] >= 0;
	}
	
	/*
	 * Gives the value of the item currently at the top of the heap.
	 */
	public int peekTopValue() {
		return distances[items[1]];
	}
	
	/*
	 * Gives the key at the top of the heap.
	 */
	public int peekTop() {
		return items[1];
	}
	
	/*
	 * Gets the value for a particular key.
	 * 
	 * key: The key to get the value.
	 */
	public int getValue(int key) {
		if(distances[key] >= 0)
			return distances[key];
		else
			return Integer.MAX_VALUE/4;
	}
}
