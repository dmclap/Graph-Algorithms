
public interface Heap {
	/*
	 * Get the item with the highest priority and remove it from
	 * the Heap
	 */
	public int dequeue();
	
	/*
	 * Add a new key to the heap with the given value, updating the
	 * structure if necessary.
	 */
	public void add(int key, int value);
	
	/*
	 * Updates the value associated with this particular key, once
	 * again updating the structure if necessary.
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
	 * Returns the value at the top of the heap without removing it.
	 * 
	 * TODO: necessary?
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
