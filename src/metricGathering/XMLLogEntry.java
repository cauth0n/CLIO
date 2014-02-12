package metricGathering;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class XMLLogEntry {

	private int commitNumber;
	private String author;
	private String date;
	private List<PathActionFile> commit;
	private String message;
	private List<Integer> bugTicketReferences;
	private static int releaseNumber = 0;

	private final int beginRelease0 = 6;
	private final int beginRelease1 = 5660;
	private final int beginRelease2 = 5970;
	private final int beginRelease3 = 7341;
	private final int beginRelease4 = 9184;
	private final int beginRelease5 = 10717;
	private final int beginRelease6 = 12188;
	private final int beginRelease7 = 14428;

	public XMLLogEntry(StringBuilder logInfo) {
		String log = logInfo.toString();
		commit = new ArrayList<>();
		bugTicketReferences = new ArrayList<>();
		assignInfo(log);
	}

	private void assignInfo(String log) {
		Scanner input = new Scanner(log);
		assignRevisionNumber(input.nextLine());
		assignAuthor(input.nextLine());
		assignDate(input.nextLine());

		for (int i = 0; i < 2; i++) { // paths or msg can be flipped. So we only
										// loop in this subsection twice.
			String pathsOrMsg = input.nextLine();
			if (pathsOrMsg.contains("<paths>")) {
				String commit = input.nextLine();
				while (!commit.contains("</paths>")) {
					assignCommit(commit);
					commit = input.nextLine();
				}
			} else if (pathsOrMsg.contains("<msg>")) {
				String msg = pathsOrMsg;

				while (!msg.equals("</msg>")) {
					assignMessage(msg);
					msg = input.nextLine();

				}
			} else {
				// not a big deal if we get here. It just means a commit had no
				// message.

				// System.out.println("Commit number: " + commitNumber +
				// " gave no message.");
				// message = "";
			}
		}
		input.close();

	}

	private void assignMessage(String messageLine) {
		message += messageLine;
		if (messageLine.toLowerCase().contains("case") || messageLine.toLowerCase().contains("toqa")
				|| messageLine.toLowerCase().contains("re #") || messageLine.contains("#")
				|| messageLine.toLowerCase().contains("to qa")) {

			System.out.println(commitNumber);

			String[] splitter = messageLine.split(" ");
			for (String s : splitter) {
				if (s.matches("[#\\d]+")) {
					String regex = s;
					if (s.charAt(0) == '#') {
						regex = s.substring(1);
					}
					System.out.println(commitNumber);
					bugTicketReferences.add(Integer.parseInt(regex));
				}
			}
		}
	}

	private void assignCommit(String commitLine) {

		String[] splitter = commitLine.split("\"");

		if (splitter[0].contains("copyfrom-path=")) {
			splitter[1] = splitter[5];
			splitter[2] = splitter[6];
		} else if (splitter[2].contains("copyfrom-path=")) {
			splitter[2] = splitter[6];
		}

		if (splitter[1].length() != 1) {
			System.out.println("commit action is not a char. Commit number:" + commitNumber);
		} else {
			char action = splitter[1].charAt(0);
			int endingIndex = splitter[2].indexOf("<");

			String tempFile = splitter[2].substring(1, endingIndex);

			String file = tempFile;

			if (tempFile.contains("/")) {
				file = tempFile.substring(tempFile.lastIndexOf("/") + 1);
			} else {
				file = tempFile;
			}
			// adding the following if statement to make sure we only look at .h
			// and .cpp files

			if (file.contains(".")) {
				// only consider .cpp and .h files
				// I should consider using .c**, .h**, .c, etc files...
				String extension = file.substring(file.lastIndexOf("."));
				if (extension.equals(".unified")) {// this change is made
													// because I unified all
													// files.
					int temp = releaseNumber;
					int extensionBegins = file.lastIndexOf(".");
					String noExtension = file.substring(0, extensionBegins);
					// commit.add(new PathActionFile(action, file, temp));
					commit.add(new PathActionFile(action, noExtension, temp));
				}
			}

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

		switch (commitNumber) {
		case beginRelease0:
			this.releaseNumber = 1;
			break;
		case beginRelease1:
			this.releaseNumber = 2;
			break;
		case beginRelease2:
			this.releaseNumber = 3;
			break;
		case beginRelease3:
			this.releaseNumber = 4;
			break;
		case beginRelease4:
			this.releaseNumber = 5;
			break;
		case beginRelease5:
			this.releaseNumber = 6;
			break;
		case beginRelease6:
			this.releaseNumber = 7;
			break;
		case beginRelease7:
			this.releaseNumber = 8;
			break;
		}

	}

	public int getReleaseNumber() {
		return releaseNumber;
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

	public List<PathActionFile> getFilesInCommit() {
		return commit;
	}

	public List<Integer> getBugTicketReferences() {
		return bugTicketReferences;
	}

	public class PathActionFile {
		private int release;
		private char action;
		private String file;

		public PathActionFile(char action, String file, int release) {
			this.action = action;
			this.file = file;
			this.release = release;
		}

		public char getAction() {
			return action;
		}

		public String getFileName() {
			return file;
		}

		public int getRelease() {
			return release;
		}

	}

}