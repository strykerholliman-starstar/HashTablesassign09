package assign09;

import java.util.Arrays;

import timing.TimingExperiment;

/**
 * Abstract subclass for hash table collision counting experiments.
 * 
 * @author CS 2420 course staff
 * @version November 5, 2026
 */
public abstract class CollisionExperiment<K, V> extends TimingExperiment {
	protected HashTable<K, V> table;
	
	/**
	 * Constructs a hash table collision counting experiment.
	 * 
	 * @param problemSizeDescription   - description of the problem size for the experiment
	 * @param problemSizeMin           - minimum hash table size
	 * @param problemSizeCount         - number of hash table sizes to use in the experiment
	 * @param problemSizeStep          - step size between consecutive hash table sizes
	 * @param experimentIterationCount - number of times to run computation for a given hash table size
	 */
	public CollisionExperiment(String problemSizeDescription, int problemSizeMin, int problemSizeCount,
			int problemSizeStep, int experimentIterationCount) {
        super(problemSizeDescription, problemSizeMin, problemSizeCount, problemSizeStep, experimentIterationCount);
	}
	
	/**
	 * Runs the collision counting experiment and prints the results.
	 */
	public void printResults() {
		System.out.println(problemSizeDescription + "\tcollision count");
		int size = problemSizeMin;
		for(int i = 0; i < problemSizeCount; i++) {
			long medianCollisionCount = computeMedianCollisionCount(size);
			System.out.println(size + "\t\t" + medianCollisionCount);
			size += problemSizeStep;
		}
	}
	
	/**
	 * Computes the median number of collisions incurred to run the computation for 
	 * a given problem size.
	 * 
	 * @param problemSize - the problem size for one experiment
	 * @return the median collision count of the experiment iterations
	 */
	protected long computeMedianCollisionCount(int problemSize) {
		long[] collisionCounts = new long[experimentIterationCount];
		for(int i = 0; i < experimentIterationCount; i++) 
			collisionCounts[i] = getCollisionCount(problemSize);
		Arrays.sort(collisionCounts);
		return collisionCounts[experimentIterationCount / 2];
	}
	
	/**
	 * Computes the number of collisions incurred to run the computation once for a 
	 * given problem size.
	 * 
	 * @param problemSize - the problem size for one experiment
	 * @return the number of collisions
	 */
	protected long getCollisionCount(int problemSize) {
		setupExperiment(problemSize);
		table.resetNumberOfCollisions();
		runComputation();
		return this.table.getNumberOfCollisions();
	}
}