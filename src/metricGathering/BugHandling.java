package metricGathering;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import metricGathering.XMLLogEntry.PathActionFile;

public class BugHandling {

	private final String commonFormat = "C:/Users/cauth0n/Documents/research/clio/commonFormat.csv";

	private String sourceControlLog;
	private String bugTicketLog;
	private String tracTickets;
	private List<XMLLogEntry> xmlLogs;
	private List<BugTicketEntry> bugTickets;

	private List<SourceFile> sourceFiles;

	public BugHandling() {
		xmlLogs = new ArrayList<>();
		bugTickets = new ArrayList<>();
		sourceFiles = new ArrayList<>();

		readInExistingData();

		// Still need to:

		getFileSize();

		// commitLogAndBugLogParser();

	}

	public void readInExistingData() {
		
	}

	public void getFileSize() {

	}

	public void commitLogAndBugLogParser() {
		sourceControlLog = "C:/Users/cauth0n/Documents/research/clio/source control logs/svs7_all_asc formatted for data mining.log";
		bugTicketLog = "C:/Users/cauth0n/Documents/research/clio/bug_tickets.csv";
		tracTickets = "C:/Users/cauth0n/Documents/research/clio/track-ticket-dump.csv";

		try {
			File sourceControlFile = new File(sourceControlLog);
			File bugTicketFile = new File(bugTicketLog);
			File tracBugTicketFile = new File(tracTickets);

			Scanner sourceControlScanner = new Scanner(sourceControlFile);
			Scanner bugTicketScanner = new Scanner(bugTicketFile);
			Scanner tracBugTicketScanner = new Scanner(tracBugTicketFile);

			/*
			 * hg log parser
			 */
			while (sourceControlScanner.hasNext()) {
				String line = sourceControlScanner.nextLine();
				if (line.contains("<logentry")) { // new logentry
					StringBuilder logEntry = new StringBuilder();
					logEntry.append(line + "\n");
					while (!line.contains("</logentry>")) {
						line = sourceControlScanner.nextLine();
						logEntry.append(line + "\n");
					}
					XMLLogEntry currentLog = new XMLLogEntry(logEntry);
					xmlLogs.add(currentLog); // xml log files are
												// built.
					for (PathActionFile paf : currentLog.getFilesInCommit()) {
						String file = paf.getFileName();
						boolean isFileAlreadyRecorded = false;
						for (SourceFile sf : sourceFiles) {
							if (file.equals(sf.getName())) {
								isFileAlreadyRecorded = true;
							}
						}
						if (!isFileAlreadyRecorded) {

							sourceFiles.add(new SourceFile(file));
						}
					}
				}
			}

			/*
			 * FOGBUGZ parser
			 */
			while (bugTicketScanner.hasNext()) {
				String line = bugTicketScanner.nextLine();
				bugTickets.add(new BugTicketEntry(line));
			}

			/*
			 * Trac parser
			 */
			String line = tracBugTicketScanner.nextLine();
			while (tracBugTicketScanner.hasNext()) {
				line = tracBugTicketScanner.nextLine();
				String[] splitter = line.split(",");

				int caseNumber = Integer.parseInt(splitter[0]);

				boolean bugAlreadyExists = false;
				for (BugTicketEntry bugTicket : bugTickets) {
					if (caseNumber == bugTicket.getCaseNumber()) {
						bugAlreadyExists = true;
					}
				}
				if (!bugAlreadyExists) {
					String combination = "don't care";
					combination += "," + splitter[2];
					combination += ",don't care";
					combination += ",don't care";
					combination += "," + splitter[0];
					combination += ",don't care";
					bugTickets.add(new BugTicketEntry(combination));
				}
			}

			sourceControlScanner.close();
			bugTicketScanner.close();
			tracBugTicketScanner.close();

			establishRelationship();

			System.out.println("Finding change frequency...");
			changeFrequency();

			System.out.println("\n\n\nFinding ticket frequency...");
			ticketFrequency();

			getFileMetrics();

			// System.out.println("\n\n\nFinding Figure 2");
			// figure2();

			System.out.println("\n\n\nPrinting to file...");
			printAll();

			// two and output to file.

		} catch (FileNotFoundException e) {
			System.out.println("Could not find file)");
			e.printStackTrace();
		}
	}

	private void getFileMetrics() {
		String metricsFile7_0 = "C:/Users/cauth0n/Documents/research/clio/understand/svs7_7.0/svs7_7-0.csv";
		String metricsFile7_1 = "C:/Users/cauth0n/Documents/research/clio/understand/svs7_7.0/svs7_7-0.csv";

	}

	private void figure2() {

		Map<Integer, Integer> release;

		for (int i = 0; i < 8; i++) {
			release = new HashMap<>();

			for (SourceFile sf : sourceFiles) {
				if (sf.getChangeFrequency().get(i) == null) {
					sf.getChangeFrequency().put(i, new Integer(0));
				}
				int numberOfChangesForThisFile = sf.getChangeFrequency().get(i);
				int numberOfFilesWithThisManyChanges;

				if (release.get(numberOfChangesForThisFile) == null) {
					numberOfFilesWithThisManyChanges = 0;
				} else {
					numberOfFilesWithThisManyChanges = release.get(numberOfChangesForThisFile);
				}
				numberOfFilesWithThisManyChanges++;
				release.put(numberOfChangesForThisFile, numberOfFilesWithThisManyChanges);
			}

			try {
				String s = "C:/Users/cauth0n/Documents/research/clio/figure2/figure2_r" + i + ".csv";
				File f = new File(s);
				PrintStream ps = new PrintStream(f);
				for (int maxChanges = 0; maxChanges < 400; maxChanges++) {
					if (release.get(maxChanges) == null) {
						release.put(maxChanges, 0);
					}
					ps.println(maxChanges + ", " + release.get(maxChanges));
				}
				ps.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void ticketFrequency() {
		for (XMLLogEntry log : xmlLogs) {
			for (Integer i : log.getBugTicketReferences()) {
				// this outside loop guarantees the ticket frequency per file
				// will increase per bug ticket. In other words, we need it.
				for (PathActionFile paf : log.getFilesInCommit()) {
					String fileName = paf.getFileName();
					int releaseNumber = paf.getRelease();
					for (SourceFile sf : sourceFiles) {
						if (sf.getName().equals(fileName)) {
							if (sf.getTicketFrequency().get(releaseNumber) == null) {
								sf.getTicketFrequency().put(releaseNumber, new Integer(1));
							} else {
								int frequency = sf.getTicketFrequency().get(releaseNumber);
								frequency++;
								sf.getTicketFrequency().put(releaseNumber, frequency);
							}
						}
					}
				}
			}
		}

	}

	private void printAll() {

		try {
			String s = "C:/Users/cauth0n/Documents/research/clio/commonFormat.csv";
			File f = new File(s);
			PrintStream ps = new PrintStream(f);
			ps.println("File, Release Number, File Size, Fan-in, Fan-out, Change Frequency, Ticket Frequency, Bug Change Frequency, Pair Change Frequency");
			for (SourceFile sf : sourceFiles) {
				ps.println(sf.toString());
			}
			ps.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Method to find the frequency of changes that files encountered.
	 * 
	 */
	private void changeFrequency() {

		for (XMLLogEntry log : xmlLogs) {

			// if we encounter a file in the commit log, up the number of
			// changes of that file by 1.
			for (PathActionFile paf : log.getFilesInCommit()) {

				int thisRelease = paf.getRelease();
				for (SourceFile sf : sourceFiles) {

					// update file change frequency
					if (paf.getFileName().equals(sf.getName())) {
						if (sf.getChangeFrequency().get(thisRelease) == null) {
							sf.getChangeFrequency().put(thisRelease, new Integer(1));
						} else {
							int totalChangesThusFar = sf.getChangeFrequency().get(thisRelease);
							totalChangesThusFar++;
							sf.addChangeFrequency(thisRelease, totalChangesThusFar);
						}
					}
				}
			}
		}
	}

	private void establishRelationship() {

		int totalReferencedTickets = 0;
		int totalFoundBugTickets = 0;

		for (XMLLogEntry xmlEntry : xmlLogs) {
			for (int caseNumber : xmlEntry.getBugTicketReferences()) {
				totalReferencedTickets++;
				boolean foundCaseNumber = false;
				for (BugTicketEntry bugTicket : bugTickets) {
					if (caseNumber == bugTicket.getCaseNumber()) {
						foundCaseNumber = true;
						totalFoundBugTickets++;
						// output.add(new OutputEntry(xmlEntry, caseNumber));
					}
				}

				if (!foundCaseNumber) {

					// the hg logs referenced a ticket that is not a bug.
				}
			}
		}

		System.out.println("Total Referenced Tickets: " + totalReferencedTickets);
		System.out.println("Total Found Bug Tickets: " + totalFoundBugTickets);

	}
}
