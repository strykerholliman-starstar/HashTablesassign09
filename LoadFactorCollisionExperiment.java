package assign09;

/**
 * Experiment to measure collisions across different maximum load factor settings.
 * 
 * @author CS 2420 course staff
 * @version November 5, 2026
 */
public class LoadFactorCollisionExperiment extends CollisionExperiment<Integer, Integer> {

	private double loadFactor;

	public LoadFactorCollisionExperiment(String problemSizeDescription, int problemSizeMin, int problemSizeCount,
			int problemSizeStep, int experimentIterationCount, double loadFactor) {
		super(problemSizeDescription, problemSizeMin, problemSizeCount, problemSizeStep, experimentIterationCount);
		this.loadFactor = loadFactor;
	}

	@Override
	protected void setupExperiment(int problemSize) {
		table = new HashTable<>(loadFactor);
		for (int i = 0; i < problemSize; i++) {
			table.put(i, i);
		}
	}

	@Override
	protected void runComputation() {
		table.put(-1, -1);
	}

	public static void main(String[] args) {
		double[] loadFactors = {0.1, 0.25, 0.5, 0.75, 0.9};
		for (double lf : loadFactors) {
			System.out.println("=== Experiment for Load Factor: " + lf + " ===");
			LoadFactorCollisionExperiment exp = new LoadFactorCollisionExperiment(
					"element count", 10000, 10, 10000, 100, lf);
			exp.printResults();
			System.out.println();
		}
	}
}
