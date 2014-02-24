package metricGathering;

public class FaultImpactRecallInstance {

	private String file;
	private int changeID;
	private int bugTicketID;

	public FaultImpactRecallInstance(String file, int changeID, int bugTicketID) {
		this.file = file;
		this.changeID = changeID;
		this.bugTicketID = bugTicketID;
	}

	public String getFile() {
		return file;
	}

	public int getChangeID() {
		return changeID;
	}

	public int getBugTicketID() {
		return bugTicketID;
	}

	public String toString() {
		String delim = ",";
		return file + delim + changeID + delim + bugTicketID + "\n";
	}

}
