
public interface Heap {
	/*
	 * Get the item with the highest priority and remove it from
	 * the Heap.
	 */
	public int dequeue();
	
	/*
	 * Add a new key to the heap with the given value, updating the
	 * structure if necessary.
	 * 
	 * key: The key of the item to be added.
	 * value: The value of the item to be added.
	 */
	public void add(int key, int value);
	
	/*
	 * Updates the value associated with this particular key, once
	 * again updating the structure if necessary.
	 * 
	 * key: The key of the item whose value we want to update.
	 * value: The new value of this item.
	 */
	public void updateKey(int key, int newValue);
	
	/*
	 * Indicates whether or not the given heap has any items in it.
	 */
	public boolean isEmpty();
	
	/*
	 * Does the heap have this key?
	 */
	public boolean hasKey(int key);
	
	/*
	 * Returns the value at the top of the heap without removing it.  This
	 * is used for Dijkstra's algorithm, where we need both the key and
	 * value.
	 */
	public int peekTopValue();
	
	/*
	 * Returns the key that represents the value at the top of the heap, once
	 * again without removing it.
	 */
	public int peekTop();
	
	/*
	 * Get the value associated with the given key.
	 */
	public int getValue(int key);

}
