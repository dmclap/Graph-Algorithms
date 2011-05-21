import java.util.ArrayList;
import java.util.HashMap;

/*
 * An alternative implementation of the Heap that doesn't need to know anything about the data.  
 * It was created first, and then replaced by the fixed-size Heap, which is more memory-efficient.  
 * However, it is fully-functional, and works for a much more general Heap implementation, 
 * since the other version requires that the keys be integers from 1 to n for an input n.  
 * This version also runs in O(log n) for enqueueing, dequeueing and updating keys.  Since most 
 * of the algorithms are the same, the other version is more thoroughly commented.
 */

public class ExpandableHeap implements Heap {
	private ArrayList<Integer> items; // the actual heap, which orders vertices
	private HashMap<Integer,Integer> locations; // maps vertices to their location in the heap
	private int end;
	// maps vertices to their hashed values; in this case, the distances they are from
	// the original source vertex.
	private HashMap<Integer,Integer> distances; 

	/*
	 * This is for cases where we know how big we expect the heap to be.
	 */
	public ExpandableHeap(int num) {
		items = new ArrayList<Integer>(num);
		for(int i = 0; i < num; ++i)
			items.add(-4);
		locations = new HashMap<Integer,Integer>(num);
		distances = new HashMap<Integer,Integer>(num);
		end = 1;
	}
	
	public ExpandableHeap() {
		this(10); // This is what the data structures use anyway
	}

	/*
	 * (non-Javadoc)
	 * @see Heap#dequeue()
	 */
	public int dequeue() {
		if(end > 2) {
			int top = items.get(1);
			items.set(1, items.get(end-1));
			items.set(end-1, -4);
			end--;
			locations.remove(top);
			distances.remove(top);
			heapify(1);
			return top;
		}
		// A special case for when there's only one item in the heap.  These could
		// probably be merged better, but this works, and doesn't exact much of a cost.
		else if(end == 2) {
			int top = items.get(1);
			items.set(1, -4);
			locations.remove(top);
			distances.remove(top);
			end = 1;
			return top;
		}
		return -1;
	}

	/*
	 * Restores the heap invariant, given the index of the value that's out
	 * of place (which occurs after enqueueing or dequeueing).  For more details,
	 * see the comments in FixedHeap.
	 */
	private void heapify(int index) {  
		int leftChild = Integer.MAX_VALUE;
		int rightChild = Integer.MAX_VALUE;
		int curr = 0;
		int parent;
		if(index < end)
			curr = distances.get(items.get(index));
		else
			return;
		if(index > 1)
			parent = distances.get(items.get(index/2));
		else
			parent = -1;
		if(2*index < end)
			leftChild = distances.get(items.get(2*index));
		if((2*index+1) < end)
			try {
				rightChild = distances.get(items.get(2*index+1));
			}catch(Exception e) {}
		if(parent > curr) { // if this item needs to be promoted
			swap(index, index/2);
			heapify(index/2);
		}
		// if this item needs to be pushed down the heap
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

	/*
	 * (non-Javadoc)
	 * @see Heap#add(int, int)
	 */
	public void add(int newItem, int value) {
		items.set(end, newItem);
		locations.put(newItem,end);
		distances.put(newItem,value);
		end++;
		heapify(end-1);
	}

	/*
	 * (non-Javadoc)
	 * @see Heap#updateKey(int, int)
	 */
	public void updateKey(int key, int newVal) {
		distances.put(key, newVal);
		int index = locations.get(key);
		heapify(index);
	}

	/* Swap the two values in the heap; involves swapping all their 
	 * hash data, too.  This is used for restoring the heap invariant
	 * after adding or removing an item from heap.  It's private because
	 * not only does it swap based on indices in the heap's implementation,
	 * but allowing the end user to use it could break the heap invariant.
	 */
	private void swap(int firstIndex, int secondIndex) {
		int firstVert = items.get(firstIndex);
		int secondVert = items.get(secondIndex);
		locations.put(firstVert, secondIndex);
		locations.put(secondVert, firstIndex);
		items.set(firstIndex, secondVert);
		items.set(secondIndex, firstVert);
	}

	/*
	 * (non-Javadoc)
	 * @see Heap#isEmpty()
	 */
	public boolean isEmpty() {
		return end < 2;
	}

	/*
	 * 
	public String toString() {
		String ret = "";
		for(int i = 1; i < end+1; ++i)
			ret += items.get(i) + " ";
		return ret;
	}*/

	/*
	 * Returns whether or not the heap has the given key.
	 */
	public boolean hasKey(int key) {
		return locations.containsKey(key);
	}

	/*
	 * (non-Javadoc)
	 * @see Heap#peekTopValue()
	 */
	public int peekTopValue() {
		return distances.get(items.get(1));
	}
	
	/*
	 * (non-Javadoc)
	 * @see Heap#peekTop()
	 */
	public int peekTop() {
		return items.get(1);
	}

	/*
	 * (non-Javadoc)
	 * @see Heap#getValue(int)
	 */
	public int getValue(int index) {
		return distances.get(index);
	}
}
