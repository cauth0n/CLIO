package metricGathering;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class CommitLogPreprocessor {
	private final String commitLogPaths = "C:/Users/cauth0n/Documents/research/clio/understand/commitLogPaths.txt";
	private final String release0FileDeps = "C:/Users/cauth0n/Documents/research/clio/understand/svs7_7.0/svs7_7-0_FileDependencies_simple.csv";
	private final String release1FileDeps = "C:/Users/cauth0n/Documents/research/clio/understand/svs7_7-1/svs7_7-1_FileDependencies_simple.csv";
	private final String release2FileDeps = "C:/Users/cauth0n/Documents/research/clio/understand/svs7_7-2/svs7_7-2_FileDependencies_simple.csv";
	private final String release3FileDeps = "C:/Users/cauth0n/Documents/research/clio/understand/svs7_7-3/svs7_7-3_FileDependencies_simple.csv";
	private final String release4FileDeps = "C:/Users/cauth0n/Documents/research/clio/understand/svs7_7-4/svs7_7-4_FileDependencies_simple.csv";
	private final String release5FileDeps = "C:/Users/cauth0n/Documents/research/clio/understand/svs7_7-5/svs7_7-5_FileDependencies_simple.csv";
	private final String release6FileDeps = "C:/Users/cauth0n/Documents/research/clio/understand/svs7_7-6/svs7_7-6_FileDependencies_simple.csv";
	private final String release7FileDeps = "C:/Users/cauth0n/Documents/research/clio/understand/svs7_7-7/svs7_7-7_FileDependencies_simple.csv";
	private final String releaseCurrentFileDeps = "C:/Users/cauth0n/Documents/research/clio/understand/svs7_7-8/svs7_7-8_FileDependencies_simple.csv";

	public CommitLogPreprocessor() {
		String genericDirectory = "C:/Users/cauth0n/Documents/research/clio/understand/";
		String genericFileEnding = "MissingCommitFilesRemovedUnified.csv";
		checkRevision(release0FileDeps, genericDirectory + "svs7_7.0/svs7_7-0" + genericFileEnding);
		System.out.println("Done with release 0");
		checkRevision(release1FileDeps, genericDirectory + "svs7_7-1/svs7_7-1" + genericFileEnding);
		System.out.println("Done with release 1");
		checkRevision(release2FileDeps, genericDirectory + "svs7_7-2/svs7_7-2" + genericFileEnding);
		System.out.println("Done with release 2");
		checkRevision(release3FileDeps, genericDirectory + "svs7_7-3/svs7_7-3" + genericFileEnding);
		System.out.println("Done with release 3");
		checkRevision(release4FileDeps, genericDirectory + "svs7_7-4/svs7_7-4" + genericFileEnding);
		System.out.println("Done with release 4");
		checkRevision(release5FileDeps, genericDirectory + "svs7_7-5/svs7_7-5" + genericFileEnding);
		System.out.println("Done with release 5");
		checkRevision(release6FileDeps, genericDirectory + "svs7_7-6/svs7_7-6" + genericFileEnding);
		System.out.println("Done with release 6");
		checkRevision(release7FileDeps, genericDirectory + "svs7_7-7/svs7_7-7" + genericFileEnding);
		System.out.println("Done with release 7");
		checkRevision(releaseCurrentFileDeps, genericDirectory + "svs7_7-current/svs7_7-8" + genericFileEnding);
	}

	private void checkRevision(String releaseFile, String outFile) {
		try {
			String output = outFile;
			File outputFile = new File(output);
			File commitLog = new File(commitLogPaths);
			File release = new File(releaseFile);

			Scanner releaseIn = new Scanner(release);

			String temp = releaseIn.nextLine();
			PrintWriter pw = new PrintWriter(outputFile);

			while (releaseIn.hasNext()) {
				String line = releaseIn.nextLine();
				String[] splitter = line.split(",");
				String fromFileWithExtension = splitter[0];
				String toFileWithExtension = splitter[1];

				String fromFile = fromFileWithExtension.substring(fromFileWithExtension.lastIndexOf('\\') + 1,
						fromFileWithExtension.lastIndexOf("."));
				String toFile = toFileWithExtension.substring(toFileWithExtension.lastIndexOf('\\') + 1,
						toFileWithExtension.lastIndexOf("."));

				// below differentiates between .h and .cpp files
				// String fromFile =
				// fromFileWithExtension.substring(fromFileWithExtension.lastIndexOf('\\')
				// + 1);
				// String toFile =
				// toFileWithExtension.substring(toFileWithExtension.lastIndexOf('\\')
				// + 1);

				boolean fromFileFound = false;
				boolean toFileFound = false;

				Scanner commitIn = new Scanner(commitLog);

				while (commitIn.hasNext()) {
					String commitLine = commitIn.nextLine();
					String commitFile = "";
					if (commitLine.contains(".")) { // it is a file we found
						if (commitLine.contains("/")) {
							commitFile = commitLine.substring(commitLine.lastIndexOf('/') + 1,
									commitLine.lastIndexOf("."));

							// below differentiates between .h and .cpp files.
							// commitFile =
							// commitLine.substring(commitLine.lastIndexOf('/')+1,
							// commitLine.length() - 1);
						} else {
							commitFile = commitLine.substring(1, commitLine.lastIndexOf("."));
							// below differentiates between .h and .cpp files.
							// commitFile = commitLine.substring(1,
							// commitLine.length() - 1);
						}
					}

					if (!fromFileFound) {

						if (fromFile.equals(commitFile)) {
							fromFileFound = true;
						}
					}
					if (!toFileFound) {
						if (toFile.equals(commitFile)) {
							toFileFound = true;
						}
					}
				}
				commitIn.close();

				if (fromFileFound && toFileFound) {
					// both need to be found to
					// constitute carrying this
					// dependency forward.

					pw.append(line + "\n");

				}
				//
				// if (!fromFileFound) {
				// System.out.println(fromFile +
				// " was not found in the commit logs");
				// }
				// if (!toFileFound) {
				// System.out.println(toFile +
				// " was not found in the commit logs");
				// }

			}
			releaseIn.close();
			pw.close();

		} catch (FileNotFoundException e) {

		}
	}
}
