package assign09;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * does tests to check hashtable and makes sure its working properly. 
 * 
 * @authorLune Holliman and Juncheng Zhou
 * @version November 9 2026
 */

public class HashTableTest {
	
	
	@Test
	public void testPutAndGetSingleElement() {
		HashTable<String, String> map = new HashTable<>();
		assertNull(map.put("Key1", "Value1"));
		assertEquals("Value1", map.get("Key1"));
		assertEquals(1, map.size());
		assertFalse(map.isEmpty());
	}
	
	@Test
	public void testPutAndGetMulitpleElement() {
		HashTable<String, String> map = new HashTable<>();
		map.put("10",  "Ten");
		map.put("20",  "Twenty");
		map.put("30", "Thirty");
		
		assertEquals("Ten", map.get("10"));
		assertEquals("Twenty", map.get("20"));
		assertEquals("Thirty", map.get("30"));
		assertEquals(3, map.size());
		
	}
	
	@Test
	public void testGetNonExistentKey() {
		HashTable<String, String> map = new HashTable<>();
		map.put("A", "Alpha");
		assertNull(map.get("B"));
		assertThrows(NullPointerException.class, () -> map.get(null));
		
	}
	
	@Test
	public void testRemoveExistingKey() {
		HashTable<String, String> map = new HashTable<>();
		map.put("A", "Alpha");
		assertEquals("Alpha", map.remove("A"));
		assertNull(map.get("A"));
		assertEquals(0, map.size());
		assertTrue(map.isEmpty());
	}
	
	@Test
	public void testClearResetsTable() {
		HashTable<String, String> map = new HashTable<>();
		map.put("A", "Alpha");
		map.put("B", "Beta");
		map.clear();
		assertEquals(0, map.size());
		assertTrue(map.isEmpty());
		assertNull(map.get("A"));
	}
	
	@Test
	public void testEntriesReturnsAllActiveEntries() {
		HashTable<String, String> map = new HashTable<>();
		map.put("A", "Alpha");
		map.put("B", "Beta");
		
		List<MapEntry<String, String>> entries = map.entries();
		assertEquals(2, entries.size());
		
		
		
		
	}
	
	@Test
	public void testDifferentKeyTypes() {
		HashTable<Object, String> map = new HashTable<>();
		map.put("TextKey", "Val1");
		map.put("100", "Val2");
		map.put("2.5", "Val3");
		
		assertEquals("Val1", map.get("TextKey"));
		assertEquals("Val2", map.get("100"));
		assertEquals("Val3", map.get("2.5"));
		
	}
	
	@Test
	public void testPutUpdateExistingKey() {
		HashTable<String, String> map = new HashTable<>();
		assertNull(map.put("A", "First"));
		assertEquals("First", map.put("A", "Updated"));
		assertEquals("Updated", map.get("A"));
		assertEquals(1, map.size());
		
	}
	
	@Test
	public void testConstructorDefaults() {
		HashTable<String, Integer> map = new HashTable<>();
		assertEquals(101, map.capacity());
		assertEquals(0, map.size());
		assertTrue(map.isEmpty());
		
	}
	
	@Test
	public void testIllegalArgumentExceptionForInvalidLoadFactor() {
		assertThrows(IllegalArgumentException.class, () -> new HashTable<>(0.0));
		assertThrows(IllegalArgumentException.class, () -> new HashTable<>(1.0));
		assertThrows(IllegalArgumentException.class, () -> new HashTable<>(-0.2));
		assertThrows(IllegalArgumentException.class, () -> new HashTable<>(1.2));
	}
	
	@Test
	public void testLazyDeletionAndProbingContinuity() {
		HashTable<Integer, String> map = new HashTable<>(0.8);
		map.put(1,  "One");
		map.put(102, "One-hundred-two");
		
		assertEquals("One", map.remove(1));
		
		assertEquals("One-hundred-two", map.get(102));
	}

}
