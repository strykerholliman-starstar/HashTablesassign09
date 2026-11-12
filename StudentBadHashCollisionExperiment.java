package assign09;

import java.util.ArrayList;

import timing.ArrayListGenerator;  // created for Assignment 5 analysis

/**
 * Experiment to measure the number of collisions incurred for the put method in  
 * a hash table of students using a bad hash function, for various problem sizes.
 *
 * @author CS 2420 course staff
 * @version November 5, 2026
 */
public class StudentBadHashCollisionExperiment extends CollisionExperiment<StudentBadHash, Object> {
    private static String problemSizeDescription = "element count";
    private static int problemSizeMin = 1000;
    private static int problemSizeCount = 10;
    private static int problemSizeStep = 1000;
    
    // Counting collisions is more precise than collecting running times; therefore,
    // we need only a few iterations for each problem size.
    private static int experimentIterationCount = 2; 
    	
	private StudentBadHash keyToPut;
    
    public static void main(String[] args) {
    	CollisionExperiment<StudentBadHash, Object> collisionExperiment = new StudentBadHashCollisionExperiment();
    	collisionExperiment.printResults();
    }

    public StudentBadHashCollisionExperiment() {
        super(problemSizeDescription, problemSizeMin, problemSizeCount, problemSizeStep, experimentIterationCount);
    }

	/**
	 * Creates a hash table of problemSize StudentBadHash elements.
	 * 
	 * @param problemSize - the number of unique students to fill the hash table
	 */
    protected void setupExperiment(int problemSize) {
    	// Generate enough unique integers to use for each student uid, firstName, lastName 
    	// to put in a hash table of problemSize, with one additional set for put in runComputation.
    	ArrayList<Integer> uniqueIntegers = ArrayListGenerator.generateShuffledArrayList(3 * (problemSize + 1));

    	table = new HashTable<StudentBadHash, Object>();
    	for(int i = 0; i < 3 * problemSize; i += 3) {
    		int uid = uniqueIntegers.get(i);
    		String firstName = "Abcde" + uniqueIntegers.get(i + 1);
    		String lastName = "Zyxwvut" + uniqueIntegers.get(i + 2);
    		table.put(new StudentBadHash(uid, firstName, lastName), null);
    	}
    	
    	int uid = uniqueIntegers.get(3 * problemSize);
		String firstName = "Abcde" + uniqueIntegers.get(3 * problemSize + 1);
		String lastName = "Zyxwvut" + uniqueIntegers.get(3 * problemSize + 2);
    	keyToPut = new StudentBadHash(uid, firstName, lastName);
    }
    
	/**
	 * Runs the put method for a hash table containing problemSize elements.
	 */
    protected void runComputation() {
    	table.put(keyToPut, null);
    }
}