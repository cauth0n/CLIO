package metricGathering;

import java.util.ArrayList;
import java.util.List;

import metricGathering.XMLLogEntry.PathActionFile;

public class FaultImpactRecall {

	private List<XMLLogEntry> xmlLogEntry;
	private List<BugTicketEntry> bugTickets;
	private List<FaultImpactRecallInstance> faultImpactRecall;
	private int release = 8;

	public FaultImpactRecall(List<BugTicketEntry> bugTickets, List<XMLLogEntry> xmlLogEntry) {
		this.bugTickets = bugTickets;
		this.xmlLogEntry = xmlLogEntry;

		process();
	}

	public void process() {
		faultImpactRecall = new ArrayList<>();
		for (XMLLogEntry xmlLog : xmlLogEntry) {
			for (PathActionFile paf : xmlLog.getFilesInCommit()) {
				if (paf.getRelease() == release) {
					// only consider one release
					for (Integer ticketNumber : xmlLog.getTicketReferences()) {
						// loop through all tickets
						for (BugTicketEntry bugTicket : bugTickets) {
							if (ticketNumber == bugTicket.getCaseNumber()) {
								// the ticket we found matches with a recorded
								// bug ticket
								boolean duplicateFile = false;
								for (FaultImpactRecallInstance fir : faultImpactRecall) {
									if (fir.getFile().equals(paf.getFileName())
											&& fir.getChangeID() == xmlLog.getCommitNumber()) {
										// we already have a file recorded for
										// this commit number..
										// basically, a .cpp and .h can be
										// duplicated and this is how we avoid
										// counting them twice

										duplicateFile = true;
									}
								}
								if (!duplicateFile) {
									faultImpactRecall.add(new FaultImpactRecallInstance(paf.getFileName(), xmlLog
											.getCommitNumber(), ticketNumber));
								}
							}
						}
					}
				}
			}
		}
	}

	public List<FaultImpactRecallInstance> getFaultImpactRecall() {
		return faultImpactRecall;
	}

	public String faultImpactRecallAsString() {
		String toRet = "";

		for (FaultImpactRecallInstance fir : faultImpactRecall) {
			toRet += fir.toString();
		}

		return toRet;

	}

}
