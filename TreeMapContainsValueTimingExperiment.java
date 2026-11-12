package assign09;

import java.util.TreeMap;
import timing.TimingExperiment;

/**
 * Experiment to collect worst-case execution times for TreeMap's containsValue method.
 * 
 * @author CS 2420 course staff
 * @version November 5, 2026
 */
public class TreeMapContainsValueTimingExperiment extends TimingExperiment {

	private TreeMap<Integer, Integer> map;

	public TreeMapContainsValueTimingExperiment(String problemSizeDescription, int problemSizeMin,
			int problemSizeCount, int problemSizeStep, int experimentIterationCount) {
		super(problemSizeDescription, problemSizeMin, problemSizeCount, problemSizeStep, experimentIterationCount);
	}

	@Override
	protected void setupExperiment(int problemSize) {
		map = new TreeMap<>();
		for (int i = 0; i < problemSize; i++) {
			map.put(i, i);
		}
	}

	@Override
	protected void runComputation() {
		// Search for a value that does not exist to ensure worst-case O(N) scan
		map.containsValue(-1);
	}

	public static void main(String[] args) {
		TreeMapContainsValueTimingExperiment exp = new TreeMapContainsValueTimingExperiment(
				"element count", 10000, 10, 10000, 100);
		exp.printResults();
	}
}
