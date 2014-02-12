package metricGathering;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FileSizeLOC {
	private Map<String, Integer> fileSize;
	private Map<String, Integer> loc;
	private String file;

	public FileSizeLOC(String file) {
		this.file = file;
		fileSize = new HashMap<>();
		loc = new HashMap<>();
		parse();
	}

	private void parse() {
		try {
			File f = new File(file);
			Scanner in = new Scanner(f);

			while (in.hasNext()) {
				String line = in.nextLine();
				String[] splitter = line.split(",");

				String sourceFile = splitter[0].substring(splitter[0].lastIndexOf("\\") + 1);

				switch (sourceFile.substring(sourceFile.lastIndexOf(".") + 1)) {
				case "h":
				case "cpp":
				case "c":
					String goodFile = sourceFile.substring(0, sourceFile.lastIndexOf("."));
					if (!fileSize.containsKey(goodFile)) {
						fileSize.put(goodFile, Integer.parseInt(splitter[1]));
					} else {
						int currentSize = fileSize.get(goodFile);
						currentSize += Integer.parseInt(splitter[1]);
						// add the additional size to the current size
						fileSize.put(goodFile, currentSize);
					}

					if (!loc.containsKey(goodFile)) {
						loc.put(goodFile, Integer.parseInt(splitter[2]));
					} else {
						int currentLOC = loc.get(goodFile);
						currentLOC += Integer.parseInt(splitter[2]);
						loc.put(goodFile, currentLOC);
					}

					break;
				default:
					System.out.println("Not a valid extension: "
							+ sourceFile.substring(sourceFile.lastIndexOf(".") + 1));
					break;
				}

			}

			in.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		}
	}

	public Map<String, Integer> getFileSize() {
		return fileSize;
	}

	public Map<String, Integer> getLoc() {
		return loc;
	}

}
