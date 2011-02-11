import java.util.HashMap;

/*
 * A special implementation of the Heap datastructure, optimized for Dijkstra's 
 * algorithm.  It provides O(log n) operations for enqueueing, dequeueing, and
 * updating keys.  This is an improvement over Java's PriorityQueue, which does
 * not have the last operation, and an attempt to perform a similar operation would
 * take O(n) time.  It supports a key/value pair system, where the keys are stored
 * in the heap itself, but ordered by the values they map to.  This is used for
 * Dijkstra's algorithm by having keys correspond to vertices, and values
 * correspond to 
 */
public class Heap {
	private int[] items; // the actual heap, which orders vertices
	private HashMap<Integer,Integer> locations; // maps vertices to their location in the heap
	private int end;
	// maps vertices to their hashed values; in this case, the distances they are from
	// the original source vertex.
	private HashMap<Integer,Integer> distances; 
	
	// want: the array to be vertices, locations maps vertices to locations,
	// need something that maps vertices to distances
	// TODO: change these maps to arrays
	public Heap(int num) {
		items = new int[num];
		for(int i = 0; i < num; ++i)
			items[i] = -4;
		locations = new HashMap<Integer,Integer>(num);
		distances = new HashMap<Integer,Integer>(num);
		end = 0;
	}
	
	public int dequeue() {
		if(end > 1) {
			int top = items[0];
			items[0] = items[end-1];
			items[end-1] = -4;
			end--;
			locations.remove(top);
			distances.remove(top);
			heapify(0);
			return top;
		}
		else if(end == 1) {
			int top = items[0];
			items[0] = -4;
			locations.remove(top);
			distances.remove(top);
			end = 0;
			return top;
		}
		return -1;
	}
	
	private void heapify(int index) { 
		int parent = distances.get(items[index/2]); 
		int leftChild = Integer.MAX_VALUE;
		int rightChild = Integer.MAX_VALUE;
		int curr = 0;
		if(index < end)
			curr = distances.get(items[index]);
		else
			return;
		if(2*index < end)
			leftChild = distances.get(items[2*index]);
		if((2*index+1) < end)
			try {
				rightChild = distances.get(items[2*index+1]);
			}catch(Exception e) {}
		if(parent > curr) { // this item needs to be promoted
			swap(index, index/2);
			heapify(index/2);
		}
		// this item needs to be pushed down the heap
		else if(curr > leftChild || curr > rightChild) {
			// swap with the smaller child
			if(rightChild < leftChild) {
				swap(index, 2*index+1);
				heapify(2*index+1);
			}
			else if(leftChild < Integer.MAX_VALUE/2) {
				swap(index, 2*index);
				heapify(2*index);
			}
		}
		else { // it's in the right place
			//locations.put(items[index], index);
			// don't need to do this; that happens in the various swaps
		}
	}
	
	public void add(int newItem, int value) {
		items[end] = newItem;
		locations.put(newItem,end);
		distances.put(newItem,value);
		end++;
		heapify(end-1);
	}
	
	public void updateKey(int key, int newVal) {
		distances.put(key, newVal);
		int index = locations.get(key);
		heapify(index);
	}
	
	// swap the two values in the heap; involves swapping all their hash data, too
	private void swap(int firstIndex, int secondIndex) {
		int firstVert = items[firstIndex];
		int secondVert = items[secondIndex];
		locations.put(firstVert, secondIndex);
		locations.put(secondVert, firstIndex);
		items[firstIndex] = secondVert;
		items[secondIndex] = firstVert;
	}
	
	public boolean isEmpty() {
		return end < 1;
	}
	
	public String toString() {
		String ret = "";
		for(int i = 0; i < end; ++i)
			ret += items[i] + " ";
		return ret;
	}
	
	public boolean hasKey(int key) {
		return locations.containsKey(key);
	}
	
	public int peekTopValue() {
		return distances.get(items[0]);
	}
	public int peekTop() {
		return items[0];
	}
	
	public int getValue(int index) {
		return distances.get(index);
	}
}
