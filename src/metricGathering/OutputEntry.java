package metricGathering;

import java.util.ArrayList;
import java.util.List;

public class OutputEntry {

	private int commitNumber;
	private int caseNumber;
	private List<String> filesAssociatedWithCaseNumber;

	public OutputEntry(XMLLogEntry xmlLogEntry, int caseNumber) {
		this.commitNumber = xmlLogEntry.getCommitNumber();
		this.caseNumber = caseNumber;

		this.filesAssociatedWithCaseNumber = filesAssociatedWithCaseNumber;
	}

	public OutputEntry(int caseNumber) {
		this.commitNumber = -1;
		this.caseNumber = caseNumber;
	}

	public void setCommitNumber(int commitNumber) {
		this.commitNumber = commitNumber;
	}

	public void setFilesAssociatedWithCaseNumber(List<String> filesAssociatedWithCaseNumber) {
		this.filesAssociatedWithCaseNumber = filesAssociatedWithCaseNumber;
	}

	public int getCommitNumber() {
		return commitNumber;
	}

	public int getCaseNumber() {
		return caseNumber;
	}

	public List<String> getFilesAssociatedWithCaseNumber() {
		return filesAssociatedWithCaseNumber;
	}

	public void addFileToCase(String s) {
		if (filesAssociatedWithCaseNumber == null) {
			filesAssociatedWithCaseNumber = new ArrayList<>();
		}
		filesAssociatedWithCaseNumber.add(s);
	}

	public String toString() {
		String toRet = "";
		toRet += caseNumber + ",";
		if (commitNumber > 0) {
			toRet += commitNumber + ",[";

			for (String s : filesAssociatedWithCaseNumber) {
				toRet += s;
			}
			toRet += "]";
		}
		return toRet;
	}
}
