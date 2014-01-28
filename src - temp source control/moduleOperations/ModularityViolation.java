package moduleOperations;

public class ModularityViolation {

	private String fromDependentFileName;
	private String toDependentFileName;
	private int numModularityViolations;
	private int clusterId;

	public ModularityViolation(String fromDependentFileName, String toDependentFileName, int numModularityViolations,
			int clusterId) {
		super();
		this.fromDependentFileName = fromDependentFileName;
		this.toDependentFileName = toDependentFileName;
		this.numModularityViolations = numModularityViolations;
		this.clusterId = clusterId;
	}

	public String getFromDependentFileName() {
		return fromDependentFileName;
	}

	public String getToDependentFileName() {
		return toDependentFileName;
	}

	public int getNumModularityViolations() {
		return numModularityViolations;
	}

	public int getClusterId() {
		return clusterId;
	}

	public boolean equalsDependencies(ModularityViolation newMV) {
		boolean toRet = false;

		if (this.getFromDependentFileName().equals(newMV.getFromDependentFileName())) {
			if (this.getToDependentFileName().equals(newMV.getToDependentFileName())) {
				toRet = true;
			}
		}

		return toRet;
	}

}
