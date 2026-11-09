package assign09;

import java.util.ArrayList;



import java.util.List;

public class HashTable<K, V> implements Map<K, V> {
	
	
	private Object[] table;
	private int size;
	private int capacity;
	private double maxLoadFactor;
	
	private final MapEntry<K, V> DELETED = new MapEntry<>(null, null);
	
	public HashTable() {
		this.capacity = 101;
		this.maxLoadFactor = 0.5;
		this.table = new Object[this.capacity];
		this.size = 0;
		
	}
	
	
	public HashTable(double maxLoadFactor) {
		if(maxLoadFactor <= 0.0 || maxLoadFactor >= 1.0) {
			throw new IllegalArgumentException("Max load factor must be between 0.0 and 1.0.");
		}
	}
	
	
	
	public int capacity() {
		return this.capacity;
		
	}
	
	
	public int size() {
		return this.size;
	}
	
	
	public boolean isEmpty() {
		return this.size == 0;
	}
	
	public V get(K key) {
		if (key == null) {
			throw new NullPointerException("Key cannot be null");
		}
		
		int initialIndex = Math.abs(key.hashCode()) % capacity;
		int index = initialIndex;
		int i = 1;
		
		while (table[index] != null) {
			MapEntry<K, V> entry = (MapEntry<K, V>) table[index];
			
			if(entry != DELETED && entry.getKey().equals(key)) {
				return entry.getValue();
			}
			
			index = (initialIndex + i * i) % capacity;
			i++;
			
			if (i > capacity) {
				break;
			}
		}
		return null;
	}



	public V put(K key, V value) {
		if (key == null) {
			throw new NullPointerException("Key cannot be null.");
		}
		
		if ((double) (size + 1) / capacity >= maxLoadFactor) {
			resizeAndRehash();
		}
		
	}
	public void resizeAndRehash() {
		Object[] oldTable = table;
		capacity = nextPrime(capacity * 2);
		table = new Object[capacity];
		size = 0;
		
		for (object obj : oldTable) {
			if (obj != null && obj != DELETED) {
				@SuppressWarnings("unchecked")
				MapEntry<K, V> entry = (MapEntry<K V>) obj;
				put(entry.getKey(), entry.getValue());
			}
		}
	}
	
	private int nextPrime( int n) {
		if(n % 2 == 0) {
			n++;
			
		}
		while (!isPrime(n)) {
			n += 2;
		}
		return n;
	}
	
	private boolean isPrime(int n) {
		if (n <= 1) return false;
		if (n <= 3) return true;
		if (n % 2 == 0 || n % 3 == 0) return false;
		for (int i = 5; i * i <= n; i += 6) {
			if (n % i == 0 || n % (i + 2) == 0) return false;
		}
		return true;
	}
	 
	public V remove(K key) {
		
	}
	
	public ArrayList<MapEntry<K, V>> entries() {
		
	}
	
	public boolean containsKey(K key) {
		return get(key) != null;
		
	}
	
	public boolean containsValue(V value) {
		
	}
	
	public void clear() {
		table = new Object[capacity];
		size = 0;
		
	}
	
	public void resetNumberOfCollisions() {
		
	}
	
	public void getNumberOfCollisions() {
		
	}

}
