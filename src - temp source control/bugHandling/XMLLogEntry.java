package bugHandling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class XMLLogEntry {

	private int commitNumber;
	private String author;
	private String date;
	private Map<Character, String> commit;
	private String message;
	private List<Integer> bugTicketReferences;

	public XMLLogEntry(StringBuilder logInfo) {
		String log = logInfo.toString();
		commit = new HashMap<>();
		bugTicketReferences = new ArrayList<>();
		assignInfo(log);
	}

	private void assignInfo(String log) {
		Scanner input = new Scanner(log);
		assignRevisionNumber(input.nextLine());
		assignAuthor(input.nextLine());
		assignDate(input.nextLine());

		for (int i = 0; i < 1; i++) { // paths or msg can be flipped. So we only
										// loop in this subsection twice.
			String pathsOrMsg = input.nextLine();
			if (pathsOrMsg.equals("<paths>")) {
				String commit = input.nextLine();
				while (!commit.equals("</paths>")) {
					assignCommit(commit);
					commit = input.nextLine();
				}
			} else if (pathsOrMsg.contains("<msg>")) {
				String msg = input.nextLine();
				while (!msg.equals("</msg>")) {
					assignMessage(msg);
					msg = input.nextLine();
				}
			} else {
				//not a big deal if we get here. It just means a commit had no message.
				
//				System.out.println("Commit number: " + commitNumber + " gave no message.");
//				message = "";
			}
		}
		input.close();

	}

	private void assignMessage(String messageLine) {
		message += messageLine;
		if (messageLine.toLowerCase().contains("case") || messageLine.toLowerCase().contains("toqa")
				|| messageLine.toLowerCase().contains("re #")) {

			String[] splitter = messageLine.split(" ");
			for (String s : splitter) {
				if (s.matches("[#\\d]+")) {
					String regex = s;
					if (s.charAt(0) == '#') {
						regex = s.substring(1);
					}
					bugTicketReferences.add(Integer.parseInt(regex));
				}
			}
		}
	}

	private void assignCommit(String commitLine) {
		String[] splitter = commitLine.split("\"");
		if (splitter[1].length() != 1) {
			System.out.println("commit action is not a char." + commitNumber);
		} else {
			char key = splitter[1].charAt(0);
			int endingIndex = splitter[2].indexOf('<');
			String file = splitter[2].substring(1, endingIndex);
			commit.put(key, file);
		}
	}

	private void assignDate(String dateLine) {
		date = dateLine.replaceAll("<[^>]*>", "");
	}

	private void assignAuthor(String authorLine) {
		author = authorLine.replaceAll("<[^>]*>", "");
	}

	private void assignRevisionNumber(String revisionLine) {
		String[] splitter = revisionLine.split("\"");
		commitNumber = Integer.parseInt(splitter[1]);
	}

	public String getDate() {
		return date;
	}

	public int getCommitNumber() {
		return commitNumber;
	}

	public String getAuthor() {
		return author;
	}

	public Map<Character, String> getCommit() {
		return commit;
	}

	public String getMessage() {
		return message;
	}

	public boolean hasBugReference() {
		boolean toRet = false;
		if (bugTicketReferences.isEmpty()) {
			toRet = true;
		}
		return toRet;
	}

	public List<Integer> getBugTicketReferences() {
		return bugTicketReferences;
	}

}