package bugHandling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BugHandling {

	private String sourceControlLog;
	private String bugTicketLog;
	private List<XMLLogEntry> xmlLogs;
	private List<BugTicketEntry> bugTickets;

	public BugHandling() {
		xmlLogs = new ArrayList<>();
		bugTickets = new ArrayList<>();
		mainLoop();
	}

	public void mainLoop() {
		sourceControlLog = "C:/Users/cauth0n/Documents/research/clio/source control logs/svs7_all_asc formatted for data mining.log";
		bugTicketLog = "C:/Users/cauth0n/Documents/research/clio/bug_tickets.csv";
		String outFileName = "C:/Users/cauth0n/Documents/research/clio/bugTicketOutput.csv";

		try {
			File sourceControlFile = new File(sourceControlLog);
			File bugTicketFile = new File(bugTicketLog);
			File outFile = new File(outFileName);

			Scanner sourceControlScanner = new Scanner(sourceControlFile);
			Scanner bugTicketScanner = new Scanner(bugTicketFile);

			while (sourceControlScanner.hasNext()) {
				String line = sourceControlScanner.nextLine();
				if (line.contains("<logentry")) { // new logentry
					StringBuilder logEntry = new StringBuilder();
					logEntry.append(line + "\n");
					while (!line.contains("</logentry>")) {
						line = sourceControlScanner.nextLine();
						logEntry.append(line + "\n");
					}
					xmlLogs.add(new XMLLogEntry(logEntry)); // xml log files are
															// built.
				}
			}

			while (bugTicketScanner.hasNext()) {
				String line = bugTicketScanner.nextLine();
				bugTickets.add(new BugTicketEntry(line));
			}

			sourceControlScanner.close();
			bugTicketScanner.close();

			establishRelationship();

			// two and output to file.

		} catch (FileNotFoundException e) {
			System.out.println("Could not find file)");
			e.printStackTrace();
		}
	}

	private void establishRelationship() {

		Map<Integer, Integer> unfoundBugTickets = new HashMap<>();

		List<OutputEntry> output = new ArrayList<>();

		for (BugTicketEntry bugTicket : bugTickets) {
			output.add(new OutputEntry(bugTicket.getCaseNumber()));
		}
		int totalReferencedBugTickets = 0;
		int totalFoundBugTickets = 0;

		for (XMLLogEntry xmlEntry : xmlLogs) {
			for (int caseNumber : xmlEntry.getBugTicketReferences()) {
				totalReferencedBugTickets++;
				boolean foundCaseNumber = false;
				for (OutputEntry allBugs : output) {
					if (caseNumber == allBugs.getCaseNumber()) {
						foundCaseNumber = true;
						totalFoundBugTickets++;
					}
				}
				
				
				if (!foundCaseNumber) {
					boolean uniqueCase = true;
					for (Integer i : unfoundBugTickets.keySet()){
						if (caseNumber == i){
							uniqueCase = false;
						}
					}
					if (uniqueCase){
						unfoundBugTickets.put(caseNumber, xmlEntry.getCommitNumber());
					}
					
					// the hg logs referenced an inexistant bug.
					// System.out.println("The hg logs reference an nonexistant bug. Commit: "
					// + xmlEntry.getRevisionNumber());
				}
			}
		}
		
		System.out.println("Total Referenced Bug Tickets: " + totalReferencedBugTickets);
		System.out.println("Total Found Bug Tickets: " + totalFoundBugTickets);
		System.out.println("Total Unique Unreferenced bug tickets: " + unfoundBugTickets.size());
		
		
		try{
			String s = "C:/Users/cauth0n/Documents/research/clio/b.csv";
			File f = new File(s);
			
			PrintStream ps = new PrintStream(f);
			
			for (Integer caseNumber : unfoundBugTickets.keySet()){
				ps.println();
			}
			ps.close();
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
//		try{
//			String s = "C:/Users/cauth0n/Documents/research/clio/unreferenced bug tickets.csv";
//			File f = new File(s);
//			
//			PrintStream ps = new PrintStream(f);
//			
//			for (Integer caseNumber : unfoundBugTickets.keySet()){
//				ps.println("Commit number: " + unfoundBugTickets.get(caseNumber) + "  and case number: " + caseNumber);
//			}
//			ps.close();
//			
//			
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		
	}
}
