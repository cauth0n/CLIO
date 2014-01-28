package bugHandling;

public class BugTicketEntry {

	private String status;
	private String priority;
	private String mileStone;
	private int caseNumber;
	private String project;

	public BugTicketEntry(String log) {
		String[] splitter = log.split(",");
		status = splitter[1];
		priority = splitter[2];
		mileStone = splitter[3];
		caseNumber = Integer.parseInt(splitter[4]);
		project = splitter[5];
	}

	public String getStatus() {
		return status;
	}

	public String getPriority() {
		return priority;
	}

	public String getMileStone() {
		return mileStone;
	}

	public int getCaseNumber() {
		return caseNumber;
	}

	public String getProject() {
		return project;
	}

}
