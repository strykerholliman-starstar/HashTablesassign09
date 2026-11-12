package assign09;

import java.util.ArrayList;



import java.util.List;


/**
 * This class is a hash table data strucure, it implemennts a map intereace, it has quadratic probing when there is collision, it has lazy deletion, also dynamic resizing on a user specified maximum load factor. 
 * 
 * @author Lune Holliman & Juncheng Zhou
 * @version November 9th, 2026
 */

public class HashTable<K, V> implements Map<K, V> {

	private int numOfItems;
	private Object[] table;
	private int capacity;          
	private double maxLoadFactor;  
	private static final Object DELETED = new Object();
	private int collisionCount;

	
	/**
	 * makes a new hashtable with the inital capacity of 101, then it also has a specific maximum load factor (for this its 0.5)
	 */
	public HashTable() {
		this(0.5);
	}
    /**
     * makes a new hash table, it has a capacity of 101, and the user specific laod facotr,
     * @param maxLoadFactor - the maximum load factor before it rehashes
     * @throws IllegalArgumentException - if the max load facotr is not between 0 and 1.
     */
	public HashTable(double maxLoadFactor) {
	    if (maxLoadFactor <= 0 || maxLoadFactor >= 1)
	        throw new IllegalArgumentException("LoadFactor must between 0 and 1");
	    
	    this.maxLoadFactor = maxLoadFactor;
	    this.capacity = 101;  
	    this.table = new Object[capacity];
	    this.numOfItems = 0;
	    this.collisionCount = 0;
	    }
	/**
	 * returns the current capacity of the hash table
	 * 
	 * @return the capacity of the table
	 */
	public int capacity() {
		return table.length;
	}

	/**
	 * returns the number of items in the hash table, ones that have the active key value pairs
	 * 
	 * @return the size of the map
	 */
	public int size() {
		return numOfItems;
	}
	/**
	 * removes and resets the mappigns and table
	 */
	@Override
	public void clear() {
		table = new Object[capacity];
        numOfItems = 0;		
        collisionCount = 0;
	}
	/**
	 * determines if one or more than one key is mapped ot a specific value from the param
	 * 
	 * @param value - if the value being searched for
	 * @param true if the value exists, and false if not
	 */
	@Override
	public boolean containsValue(V value) {
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null && table[i] != DELETED) {
                @SuppressWarnings("unchecked")
                MapEntry<K, V> entry = (MapEntry<K, V>) table[i];
                if (value == null ? entry.getValue() == null : value.equals(entry.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

	/**
	 * this returns a list of all the mappings in the map
	 * 
	 * @return the list of mapentry values represent all acitve mappings.
	 */
	@Override
	public List<MapEntry<K, V>> entries() {
        List<MapEntry<K, V>> result = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null && table[i] != DELETED) {
                @SuppressWarnings("unchecked")
                MapEntry<K, V> entry = (MapEntry<K, V>) table[i];
                result.add(entry);
            }
        }
        return result;
    }
	/**
	 * gets the value which is attached to the specific key which it is mapped to
	 * @param key - the key whose associated value is meant to be returned
	 * @return the mapped value, or null if the key is not present
	 * @throws NullPointerException if said key is actually null
	 */
	@Override
	public V get(K key) {
        int[] result = findIndex(key);
        if (result[1] == 0) {  // Found existing key
            @SuppressWarnings("unchecked")
            MapEntry<K, V> entry = (MapEntry<K, V>) table[result[0]];
            return entry.getValue();
        }
        return null;
    }
	/**
	 * checks if the map is empty
	 * 
	 * @return returns true if empty, and vice versa
	 */
	@Override
	public boolean isEmpty() {
		return numOfItems == 0;
	}
	/**
	 * assosicated a param value with a param key, then if its in tree it overwrites the old one
	 * 
	 * @param key - the param key to be assoicated with the value
	 * @param value - the param value to be assoicated with the key
	 * @return the previous value that was assoicated with the key, or if there was no mapping, its null
	 * @throws NullPointerException if the param key is null
	 */
	@Override
	public V put(K key, V value) {
        int[] result = findIndex(key);
        int pos = result[0];
        int flag = result[1];
        
        if (flag == 0) {  // Key already exists, update value
            @SuppressWarnings("unchecked")
            MapEntry<K, V> entry = (MapEntry<K, V>) table[pos];
            V oldValue = entry.getValue();
            entry.setValue(value);
            return oldValue;
        } else {  // New entry
            table[pos] = new MapEntry<>(key, value);
            numOfItems++;
            
            // Check if rehashing is needed
            if ((double) numOfItems / capacity > maxLoadFactor) {
                rehash();
            }
            return null;
        }
    }
	/**
	 * removes a key from the the map, uses lazy deletion
	 * 
	 * @param key - key whose is meant to be removed
	 * @return the preevious value with the key, or null if nothing
	 * @throws NullPointerException if the key is null
	 */
	@Override
	public V remove(K key) {
        int[] result = findIndex(key);
        if (result[1] != 0) {  // Key not found
            return null;
        }
        
        @SuppressWarnings("unchecked")
        MapEntry<K, V> entry = (MapEntry<K, V>) table[result[0]];
        V oldValue = entry.getValue();
        table[result[0]] = DELETED;  // Lazy deletion
        numOfItems--;
        return oldValue;
    }

	/**
	 * this method checks if the mapping has a specific value
	 * 
	 * @param key - the key who is being checked
	 * @return true if it is in the mapping, false if not
	 * @throws NullPointerException if the key is null
	 */
	@Override
	public boolean containsKey(K key) {
		return findIndex(key)[1] == 0;
	}
	
	
	/**
	 * this is a helper method to find the table index for a key, it makes use of quadratic probing
	 * @param key - the key that is being searched for
	 * 
	 * @return returns an integer array where index 0 is the target array spot and index 1 is the status flag
	 */
	private int[] findIndex(K key) {
        int start = Math.abs(key.hashCode()) % capacity;
        int firstDeleted = -1;
        
        for (int i = 0; i < capacity; i++) {
            int pos = (start + i * i) % capacity;
            
            if (table[pos] == null) {
                // Never used slot - stop searching
                if (firstDeleted == -1) {
                    return new int[]{pos, 1};
                } else {
                    return new int[]{firstDeleted, 2};
                }
            }
            
            if (table[pos] == DELETED) {
                // Deleted slot - record first occurrence and continue
                if (firstDeleted == -1) {
                    firstDeleted = pos;
                }
                continue;
            }
            
            // Occupied slot - check if key matches
            @SuppressWarnings("unchecked")
            MapEntry<K, V> entry = (MapEntry<K, V>) table[pos];
            if (entry.getKey().equals(key)) {
                return new int[]{pos, 0};  // Found existing key
            }
        }
        
        // Scanned entire table without finding key or empty slot
        // Use first deleted slot if available
        return new int[]{firstDeleted != -1 ? firstDeleted : -1, 2};
    }
	
	/**
	 * this method doubles the capacity of the hash table to the next prime number, also re inserts all the entries
	 */
    private void rehash() {
        Object[] oldTable = table;
        int oldCapacity = capacity;
        
        // Calculate new capacity: next prime after doubling
        capacity = nextPrime(capacity * 2);
        table = new Object[capacity];
        numOfItems = 0;
        
        // Reinsert all active entries
        for (int i = 0; i < oldCapacity; i++) {
            if (oldTable[i] != null && oldTable[i] != DELETED) {
                @SuppressWarnings("unchecked")
                MapEntry<K, V> entry = (MapEntry<K, V>) oldTable[i];
                // Use direct insertion to avoid triggering another rehash
                int[] result = findIndex(entry.getKey());
                table[result[0]] = entry;
                numOfItems++;
            }
        }
    }
    
    /**
     * Finds the smallest prime number greater than or equal to n
     * 
     * @param n the starting point
     * @return the next prime number >= n
     */
    private int nextPrime(int n) {
        if (n < 2) return 2;
        while (!isPrime(n)) {
            n++;
        }
        return n;
    }
    
    /**
     * Checks if a number is prime
     * 
     * @param n the number to check
     * @return true if n is prime, false otherwise
     */
    private boolean isPrime(int n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }
    /**
     * returns the number of collisions to zero
     */
    public void resetNumberOfCollisions() {
    	this.collisionCount = 0;
    }
    /**
     * gets the number of collisions
     * @return returns the number of collisions.
     */
    public int getNumberOfCollisions() {
    	return this.collisionCount;
    }
}
