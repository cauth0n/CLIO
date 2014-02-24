package metricGathering;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import metricGathering.XMLLogEntry.PathActionFile;

public class BugHandling {

	private final int releases = 9;

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

		// CommitLogPreprocessor clp = new CommitLogPreprocessor();

		readInExistingData();

		// FaultyFileRecall ffr = new FaultyFileRecall(sourceFiles);

		// Still need to:
		// get pair change frequency

		// done with the following method
		// if we need the data again, we can run it.
		commitLogAndBugLogParser();

		readInExistingData();

		figure4();

		// getFileSize();

		// figure3();

		// getFanInFanOut();

		// printSevenAndEight();

		// printTotalsPerRelease();

	}

	private void figure4() {
		String delim = ",";
		FaultImpactRecall fir = new FaultImpactRecall(bugTickets, xmlLogs);
		int release = 7;
		try {
			File outFile = new File("C:/Users/cauth0n/Documents/research/clio/figure4/figure4Values.csv");
			PrintWriter pw = new PrintWriter(outFile);
			String outputter = "File" + delim + "File size" + delim + "R7 Bug Changes" + delim + "Changes" + delim
					+ "Bug Tickets" + delim + "Fan-out" + delim + "R7.5 Bug changes\n";

			for (SourceFile sf : sourceFiles) {
				if (sf.getFileSize().get(release) != 0 && sf.getBugChangeFrequency().get(release + 1) > 0) {

					outputter += sf.getName() + delim;
					outputter += sf.getFileSize().get(release) + delim;
					outputter += sf.getBugChangeFrequency().get(release) + delim;
					outputter += sf.getChangeFrequency().get(release) + delim;
					outputter += sf.getBugChangeFrequency().get(release) + delim;
					outputter += sf.getFanOut().get(release) + delim;
					outputter += sf.getBugChangeFrequency().get(release + 1) + "\n";
				}

			}
			pw.println(outputter);

			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void printTotalsPerRelease() {
		String delim = ",";
		try {
			File outFile = new File("C:/Users/cauth0n/Documents/research/clio/metrics across releases/metrics.csv");
			PrintWriter pw = new PrintWriter(outFile);

			for (int i = 0; i < releases; i++) {
				int fileSize = 0;
				int loc = 0;
				int fanin = 0;
				int fanout = 0;
				int change = 0;
				int tickets = 0;
				int bugTick = 0;
				for (SourceFile sf : sourceFiles) {
					fileSize += sf.getFileSize().get(i);
					loc += sf.getLOC().get(i);
					fanin += sf.getFanIn().get(i);
					fanout += sf.getFanOut().get(i);
					change += sf.getChangeFrequency().get(i);
					tickets += sf.getTicketFrequency().get(i);
					bugTick += sf.getBugChangeFrequency().get(i);
				}
				String outputter = "Release " + i + delim + fileSize + delim + loc + delim + fanin + delim + fanout
						+ delim + change + delim + tickets + delim + bugTick;
				pw.println(outputter);
			}

			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void printSevenAndEight() {
		int r1 = 7;
		int r2 = 8;
		String outputter = "";
		String delim = ",";
		for (SourceFile sf : sourceFiles) {
			outputter += sf.getName() + delim;
			outputter += (sf.getFileSize().get(r1) + sf.getFileSize().get(r2)) + delim;
			outputter += (sf.getFanIn().get(r1) + sf.getFanIn().get(r2)) + delim;
			outputter += (sf.getFanOut().get(r1) + sf.getFanOut().get(r2)) + delim;
			outputter += (sf.getChangeFrequency().get(r1) + sf.getChangeFrequency().get(r2)) + delim;
			outputter += (sf.getTicketFrequency().get(r1) + sf.getTicketFrequency().get(r2)) + delim;
			outputter += (sf.getBugChangeFrequency().get(r1) + sf.getBugChangeFrequency().get(r2)) + "\n";

		}

		try {
			File outFile = new File("C:/Users/cauth0n/Documents/research/clio/table1.csv");
			PrintWriter pw = new PrintWriter(outFile);

			pw.print(outputter);

			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void figure3() {
		String outputter = "";
		for (SourceFile sf : sourceFiles) {
			if (sf.getChangeFrequency().get(8) != 0) {
				// concerned only with future changed files
				outputter += sf.getName() + "," + sf.getFanOut().get(7) + ", " + sf.getChangeFrequency().get(8) + "\n";
			}
		}
		try {
			File outFile = new File("C:/Users/cauth0n/Documents/research/clio/figure3.csv");
			PrintWriter pw = new PrintWriter(outFile);

			pw.print(outputter);

			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getFanInFanOut() {
		String defaultDir = "C:/Users/cauth0n/Documents/research/clio/understand/svs7_7-";
		String defaultEnd = "MissingCommitFilesRemovedUnified.csv";

		for (int i = 0; i < releases; i++) {
			String filePointer = defaultDir + i + "/svs7_7-" + i + defaultEnd;
			FanInFanout fifo = new FanInFanout(filePointer);
			// a new object each iteration means fan-in/fan-out is unique for
			// each version

			Map<String, Integer> fanIn = fifo.getFanIn();
			Map<String, Integer> fanOut = fifo.getFanOut();

			for (SourceFile sf : sourceFiles) {
				if (!fanIn.containsKey(sf.getName())) {
					// that file is not included in the dep lists
					fanIn.put(sf.getName(), 0);
				}
				if (!fanOut.containsKey(sf.getName())) {
					// that file is not included in the dep lists
					fanOut.put(sf.getName(), 0);
				}

				sf.getFanIn().put(i, fanIn.get(sf.getName()));
				sf.getFanOut().put(i, fanOut.get(sf.getName()));
			}
		}
		printAll();

	}

	private void readInExistingData() {
		try {
			Scanner input = new Scanner(new File(commonFormat));
			String temp = input.nextLine();// first line is just a formatting
											// helper for
			// humans

			String lagPointer = "";
			while (input.hasNext()) {
				String line = input.nextLine();
				String splitter[] = line.split(",");

				if (splitter[0].equals(lagPointer)) {
					// the file we found already has a sourceFile object

					for (SourceFile sf : sourceFiles) {
						if (splitter[0].equals(sf.getName())) {
							int releaseNum = Integer.parseInt(splitter[1]);
							sf.addFileSize(releaseNum, Integer.parseInt(splitter[2]));
							sf.addLOC(releaseNum, Integer.parseInt(splitter[3]));
							sf.addFanIn(releaseNum, Integer.parseInt(splitter[4]));
							sf.addFanOut(releaseNum, Integer.parseInt(splitter[5]));
							sf.addChangeFrequency(releaseNum, Integer.parseInt(splitter[6]));
							sf.addTicketFrequency(releaseNum, Integer.parseInt(splitter[7]));
							sf.addBugChangeFrequency(releaseNum, Integer.parseInt(splitter[8]));
							sf.addPairChangeFrequency(releaseNum, Integer.parseInt(splitter[9]));
						}
					}

				} else {
					SourceFile sf = new SourceFile(splitter[0]);
					int releaseNum = Integer.parseInt(splitter[1]);
					sf.addFileSize(releaseNum, Integer.parseInt(splitter[2]));
					sf.addLOC(releaseNum, Integer.parseInt(splitter[3]));
					sf.addFanIn(releaseNum, Integer.parseInt(splitter[4]));
					sf.addFanOut(releaseNum, Integer.parseInt(splitter[5]));
					sf.addChangeFrequency(releaseNum, Integer.parseInt(splitter[6]));
					sf.addTicketFrequency(releaseNum, Integer.parseInt(splitter[7]));
					sf.addBugChangeFrequency(releaseNum, Integer.parseInt(splitter[8]));
					sf.addPairChangeFrequency(releaseNum, Integer.parseInt(splitter[9]));

					sourceFiles.add(sf);
					lagPointer = splitter[0];
				}

			}

		} catch (FileNotFoundException e) {
			System.out.println("Could not find existing data");
			e.printStackTrace();
		}
	}

	private void getFileSize() {
		String defaultDir = "C:/Users/cauth0n/Documents/research/clio/file size/svs7-7.";
		String defaultEnd = ".txt";
		for (int i = 0; i < releases; i++) {
			String filePointer = defaultDir + i + defaultEnd;

			FileSizeLOC fsl = new FileSizeLOC(filePointer);

			for (SourceFile sf : sourceFiles) {
				sf.getFileSize().put(i, fsl.getFileSize().get(sf.getName()));
				sf.getLOC().put(i, fsl.getLoc().get(sf.getName()));
				// perhaps consider putting a 'DNE' tag for files which do not
				// have a size
			}

		}

		printAll();

	}

	private void commitLogAndBugLogParser() {
		sourceControlLog = "C:/Users/cauth0n/Documents/research/clio/clio tool/Clio-Organize/svs7_all_asc_edited_for_parsing.log";
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

			// System.out.println("\n\n\nFinding Figure 2");
			// figure2();

			// fill in all null values with 0
			for (SourceFile sf : sourceFiles) {
				for (int i = 0; i < releases; i++) {
					if (sf.getChangeFrequency().get(i) == null) {
						sf.addChangeFrequency(i, new Integer(0));
						// this file changed 0 times
					}
					if (sf.getTicketFrequency().get(i) == null) {
						sf.addTicketFrequency(i, new Integer(0));
						// this file had 0 bug tickets
					}
				}
			}

			// System.out.println("\n\n\nPrinting to file...");
			// printAll();

			// two and output to file.

		} catch (FileNotFoundException e) {
			System.out.println("Could not find file)");
			e.printStackTrace();
		}
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
			for (Integer i : log.getTicketReferences()) {
				// this outside loop guarantees the ticket frequency per file
				// will increase per bug ticket. In other words, we need it.

				// I need to check here if the ticket references a bug, a
				// feature, or unknown
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
			ps.println("File, Release Number, File Size, LOC, Fan-in, Fan-out, Change Frequency, Ticket Frequency, Bug Change Frequency, Pair Change Frequency");
			for (SourceFile sf : sourceFiles) {
				ps.print(sf.toString());
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
			for (int caseNumber : xmlEntry.getTicketReferences()) {
				totalReferencedTickets++;
				boolean caseNumberIsABug = false;
				for (BugTicketEntry bugTicket : bugTickets) {
					if (caseNumber == bugTicket.getCaseNumber()) {
						caseNumberIsABug = true;
						totalFoundBugTickets++;
						// output.add(new OutputEntry(xmlEntry, caseNumber));
					}
				}
				if (!caseNumberIsABug) {
					// the hg logs referenced a ticket that is not a bug.
				}

				for (PathActionFile paf : xmlEntry.getFilesInCommit()) {
					for (SourceFile sf : sourceFiles) {
						if (sf.getName().equals(paf.getFileName())) {
							if (caseNumberIsABug) {
								if (sf.getBugChangeFrequency().get(paf.getRelease()) == null) {
									sf.getBugChangeFrequency().put(paf.getRelease(), new Integer(0));
								}
								int bugsAlreadyFound = sf.getBugChangeFrequency().get(paf.getRelease());
								bugsAlreadyFound++;
								sf.getBugChangeFrequency().put(paf.getRelease(), bugsAlreadyFound);
							}
							if (sf.getTicketFrequency().get(paf.getRelease()) == null) {
								sf.getTicketFrequency().put(paf.getRelease(), new Integer(0));
							}
							int casesFound = sf.getTicketFrequency().get(paf.getRelease());
							casesFound++;
							sf.getTicketFrequency().put(paf.getRelease(), casesFound);
						}
					}
				}

			}
		}

		System.out.println("Total Referenced Tickets: " + totalReferencedTickets);
		System.out.println("Total Found Bug Tickets: " + totalFoundBugTickets);

	}
}
