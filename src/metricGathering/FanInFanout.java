package metricGathering;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FanInFanout {

	private Map<String, Integer> fanIn;
	private Map<String, Integer> fanOut;
	private String file;

	public FanInFanout(String file) {
		this.file = file;
		fanIn = new HashMap<>();
		fanOut = new HashMap<>();
		parse();
	}

	private void parse() {
		try {
			File f = new File(file);
			Scanner in = new Scanner(f);

			while (in.hasNext()) {
				String line = in.nextLine();
				String[] splitter = line.split(",");

				String fromFile = splitter[0]
						.substring(splitter[0].lastIndexOf("\\") + 1, splitter[0].lastIndexOf("."));
				String toFile = splitter[1].substring(splitter[1].lastIndexOf("\\") + 1, splitter[1].lastIndexOf("."));

				if (!fromFile.equals(toFile)) {
					// make sure the .cpp/.c .h pair does not count as fan-in or
					// fan-out.

					if (!fanOut.containsKey(fromFile)) {
						fanOut.put(fromFile, new Integer(0));
					}
					if (!fanIn.containsKey(toFile)) {
						fanIn.put(toFile, new Integer(0));
					}
					int fanOutValue = fanOut.get(fromFile);
					fanOutValue++;
					fanOut.put(fromFile, fanOutValue);

					int fanInValue = fanIn.get(toFile);
					fanInValue++;
					fanIn.put(toFile, fanInValue);

				}
			}
			in.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		}
	}

	public Map<String, Integer> getFanIn() {
		return fanIn;
	}

	public Map<String, Integer> getFanOut() {
		return fanOut;
	}

}
