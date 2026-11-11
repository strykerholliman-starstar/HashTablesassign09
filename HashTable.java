package assign09;

import java.util.ArrayList;
import java.util.List;

public class HashTable<K, V> implements Map<K, V> {

	// number of items in the hash table
	private int numOfItems;
	private Object[] table;
	private int capacity;          
	private double maxLoadFactor;  
	private static final Object DELETED = new Object();

	
	/**
	 * Creates a new hash table with a default capacity.
	 */
	public HashTable() {
		this(0.5);
	}
    
	public HashTable(double maxLoadFactor) {
	    if (maxLoadFactor <= 0 || maxLoadFactor >= 1)
	        throw new IllegalArgumentException("LoadFactor must between 0 and 1");
	    
	    this.maxLoadFactor = maxLoadFactor;
	    this.capacity = 101;  
	    this.table = new Object[capacity];
	    this.numOfItems = 0;
	}
	/**
	 * @return the (current) capacity of this hash table
	 */
	public int capacity() {
		return table.length;
	}

	/**
	 * @return the number of items stored in this hash table
	 */
	public int size() {
		return numOfItems;
	}

	@Override
	public void clear() {
		table = new Object[capacity];
        numOfItems = 0;		
	}

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

	@Override
	public boolean isEmpty() {
		return numOfItems == 0;
	}

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

	@Override
	public boolean containsKey(K key) {
		return findIndex(key)[1] == 0;
	}
	
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
     * Rehashes the hash table by doubling the capacity (to the next prime)
     * and reinserting all active entries.
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
     * Finds the smallest prime number greater than or equal to n.
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
     * Checks if a number is prime.
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
}
