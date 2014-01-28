package moduleOperations;

import java.util.ArrayList;
import java.util.List;

import driver.Driver;

public class ModuleOperations {
	private final int clusterSize = 10;

	private String releaseFourToFiveFilePath = "outputs/cluster_out_4-5.txt";
	private String releaseFiveToSixFilePath = "outputs/cluster_out_5-6.txt";
	private String releaseSixToSevenFilePath = "outputs/cluster_out_6-7.txt";
	private String releaseSevenToCurrentFilePath = "outputs/cluster_out_7-current.txt";

	private Release fourToFive;
	private Release fiveToSix;
	private Release sixToSeven;
	private Release sevenToCurrent;
	private Operations ops;

	private List<Release> releases;

	public static void main(String[] args) {
		new Driver();
	}

	public ModuleOperations() {
		releases = new ArrayList<>();
		fourToFive = new Release(releaseFourToFiveFilePath);
		fiveToSix = new Release(releaseFiveToSixFilePath);
		sixToSeven = new Release(releaseSixToSevenFilePath);
		sevenToCurrent = new Release(releaseSevenToCurrentFilePath);

		releases.add(fourToFive);
		releases.add(fiveToSix);
		releases.add(sixToSeven);
		releases.add(sevenToCurrent);

		ops = new Operations();

		for (Release r : releases) {
			ops.parseRelease(r);
		}
		printNetModularityViolations();

		printNewModularityViolations();

		printClusterSize();
		
		printFixedModularityViolations();
	}
	
	public void printFixedModularityViolations(){
		//TODO
	}

	public void printClusterSize() {
		System.out.println("Cluster Size: ");
		for (int i = 0; i < releases.size(); i++) {
			System.out.println("Release " + i + ": ");
			for (int j = 0; j < clusterSize; j++) {
				int localClusterSize = 0;
				for (ModularityViolation mv : releases.get(i).getModularityViolationInThisRelease()) {
					if (mv.getClusterId() == j) {
						localClusterSize++;
					}
				}
				System.out.println("	Cluster " + (j + 1) + ": " + localClusterSize);
			}
		}
		System.out.println();
	}

	public void printNetModularityViolations() {
		System.out.println("Total Modularity Violations: ");
		for (int i = 0; i < releases.size(); i++) {
			System.out.println("Release " + i + ": " + releases.get(i).getModularityViolationInThisRelease().size());
		}
		System.out.println();
	}

	public void printNewModularityViolations() {
		List<ModularityViolation> existingModularityViolations = new ArrayList<>();
		System.out.println("Total New Violations: ");
		int totalMVCounter = 0;
		for (int i = 0; i < releases.size(); i++) {

			if (i == 0) {
				for (ModularityViolation mv : releases.get(i).getModularityViolationInThisRelease()) {
					existingModularityViolations.add(mv);
				}
			} else {

				for (ModularityViolation mvToAdd : releases.get(i).getModularityViolationInThisRelease()) {
					boolean doesMVAlreadyExist = false;
					for (int j = 0; j < existingModularityViolations.size(); j++) {
						ModularityViolation mvExisting = existingModularityViolations.get(j);
						if (mvToAdd.equalsDependencies(mvExisting)) {
							doesMVAlreadyExist = true;
							break;
						}
					}
					if (doesMVAlreadyExist) {
						existingModularityViolations.add(mvToAdd);
					}
				}
			}
			int newViolations = existingModularityViolations.size();
			newViolations -= totalMVCounter;
			totalMVCounter = existingModularityViolations.size();
			System.out.println("Release " + i + ": " + newViolations);
		}
		System.out.println();
	}
}
