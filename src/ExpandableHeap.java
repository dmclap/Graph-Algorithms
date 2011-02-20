import java.util.HashMap;

/*
 * An alternative implementation of the Heap that only needs to know the maximum size the heap
 * will ever be in order to function.  It was created first, and then replaced by the fixed-size
 * Heap, which is more memory-efficient.  However, it is fully-functional, and works for a much
 * more general Heap implementation, since the other version requires that the keys be integers
 * from 1 to n for an input n.  This version also runs in O(log n) for enqueueing, dequeueing and
 * updating keys.  Since most of the algorithms are the same, the other version is more
 * thoroughly commented.
 */
public class ExpandableHeap implements Heap {
	private int[] items; // the actual heap, which orders vertices
	private HashMap<Integer,Integer> locations; // maps vertices to their location in the heap
	private int end;
	// maps vertices to their hashed values; in this case, the distances they are from
	// the original source vertex.
	private HashMap<Integer,Integer> distances; 

	/*
	 * This version requires inputting the maximum size that the heap will ever be.  It
	 * can be trivially improved to not require this by changing the datastructure that
	 * stores the heap from an array to an ArrayList.
	 */
	public ExpandableHeap(int num) {
		items = new int[num];
		for(int i = 0; i < num; ++i)
			items[i] = -4;
		locations = new HashMap<Integer,Integer>(num);
		distances = new HashMap<Integer,Integer>(num);
		end = 1;
	}

	public int dequeue() {
		if(end > 2) {
			int top = items[1];
			items[1] = items[end-1];
			items[end-1] = -4;
			end--;
			locations.remove(top);
			distances.remove(top);
			heapify(1);
			return top;
		}
		else if(end == 2) {
			int top = items[1];
			items[1] = -4;
			locations.remove(top);
			distances.remove(top);
			end = 1;
			return top;
		}
		return -1;
	}

	private void heapify(int index) {  
		int leftChild = Integer.MAX_VALUE;
		int rightChild = Integer.MAX_VALUE;
		int curr = 0;
		int parent;
		if(index < end)
			curr = distances.get(items[index]);
		else
			return;
		if(index > 1)
			parent = distances.get(items[index/2]);
		else
			parent = -1;
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
		return end < 2;
	}

	public String toString() {
		String ret = "";
		for(int i = 1; i < end+1; ++i)
			ret += items[i] + " ";
		return ret;
	}

	public boolean hasKey(int key) {
		return locations.containsKey(key);
	}

	public int peekTopValue() {
		return distances.get(items[1]);
	}
	public int peekTop() {
		return items[1];
	}

	public int getValue(int index) {
		return distances.get(index);
	}
}
