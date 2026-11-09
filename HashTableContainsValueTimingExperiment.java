package assign09;

import java.util.ArrayList;

import timing.ArrayListGenerator;
import timing.TimingExperiment;

/**
 * Experiment to measure the running times of the containsValue method in  
 * a hash table of Integer objects, for various problem sizes.
 *
 * @author CS 2420 course staff
 * @version November 5, 2026
 */
public class HashTableContainsValueTimingExperiment extends TimingExperiment {
    private static String problemSizeDescription = "element count";
    private static int problemSizeMin = 10000;
    private static int problemSizeCount = 10;
    private static int problemSizeStep = 10000;
    private static int experimentIterationCount = 100; 
    	
	protected HashTable<Integer, Integer> table;
    
    public static void main(String[] args) {
        TimingExperiment timingExperiment = new HashTableContainsValueTimingExperiment();
        timingExperiment.printResults();
    }

    public HashTableContainsValueTimingExperiment() {
        super(problemSizeDescription, problemSizeMin, problemSizeCount, problemSizeStep, experimentIterationCount);
    }

	/**
	 * Creates a hash table of problemSize Integer elements.
	 * 
	 * @param problemSize - the number of unique students to fill the hash table
	 */
    protected void setupExperiment(int problemSize) {
    	// Generate problemSize unique integers to put in a hash table of problemSize.
    	ArrayList<Integer> uniqueIntegers = ArrayListGenerator.generateShuffledArrayList(problemSize);

    	table = new HashTable<Integer, Integer>();
    	for(int i = 0; i < problemSize; i++) 
    		table.put(uniqueIntegers.get(i), 0);
      }
    
	/**
	 * Runs the containsValue method for a hash table containing problemSize elements.
	 */
    protected void runComputation() {
    	table.containsValue(1);
    }
}