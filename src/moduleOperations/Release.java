package moduleOperations;

import java.util.ArrayList;
import java.util.List;

public class Release {

	private List<ModularityViolation> modularityViolationInThisRelease;
	private String fileName;

	public Release(String fileName) {
		this.fileName = fileName;
		modularityViolationInThisRelease = new ArrayList<>();
	}

	public void addModularityViolation(ModularityViolation mv) {
		modularityViolationInThisRelease.add(mv);
	}

	public List<ModularityViolation> getModularityViolationInThisRelease() {
		return modularityViolationInThisRelease;
	}

	public String getFileName() {
		return fileName;
	}

}
