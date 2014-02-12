package metricGathering;

import java.util.HashMap;
import java.util.Map;

public class SourceFile {

	private String name;
	private Map<Integer, Integer> fileSize;
	private Map<Integer, Integer> loc;
	private Map<Integer, Integer> fanIn;
	private Map<Integer, Integer> fanOut;
	private Map<Integer, Integer> changeFrequency;
	private Map<Integer, Integer> ticketFrequency;
	private Map<Integer, Integer> bugChangeFrequency;
	private Map<Integer, Integer> pairChangeFrequency;

	public SourceFile(String name) {
		this.name = name;
		fileSize = new HashMap<>();
		loc = new HashMap<>();
		fanIn = new HashMap<>();
		fanOut = new HashMap<>();
		changeFrequency = new HashMap<>();
		ticketFrequency = new HashMap<>();
		bugChangeFrequency = new HashMap<>();
		pairChangeFrequency = new HashMap<>();
	}

	public void addFileSize(int key, int value) {
		fileSize.put(key, value);
	}

	public void addLOC(int key, int value) {
		loc.put(key, value);
	}

	public void addFanIn(int key, int value) {
		fanIn.put(key, value);
	}

	public void addFanOut(int key, int value) {
		fanOut.put(key, value);
	}

	public void addChangeFrequency(int key, int value) {
		changeFrequency.put(key, value);
	}

	public void addTicketFrequency(int key, int value) {
		ticketFrequency.put(key, value);
	}

	public void addBugChangeFrequency(int key, int value) {
		bugChangeFrequency.put(key, value);
	}

	public void addPairChangeFrequency(int key, int value) {
		pairChangeFrequency.put(key, value);
	}

	public String getName() {
		return name;
	}

	public Map<Integer, Integer> getFileSize() {
		return fileSize;
	}

	public Map<Integer, Integer> getFanIn() {
		return fanIn;
	}

	public Map<Integer, Integer> getFanOut() {
		return fanOut;
	}

	public Map<Integer, Integer> getChangeFrequency() {
		return changeFrequency;
	}

	public Map<Integer, Integer> getTicketFrequency() {
		return ticketFrequency;
	}

	public Map<Integer, Integer> getBugChangeFrequency() {
		return bugChangeFrequency;
	}

	public Map<Integer, Integer> getPairChangeFrequency() {
		return pairChangeFrequency;
	}

	public Map<Integer, Integer> getLOC() {
		return loc;
	}

	public String toString() {
		String toRet = "";
		String deliminater = ",";

		for (int i = 0; i < 9; i++) {
			toRet += name;
			toRet += deliminater;
			toRet += i;
			toRet += deliminater;
			if (fileSize.get(i) == null) {
				fileSize.put(i, -99);
			}
			toRet += fileSize.get(i);
			toRet += deliminater;
			if (loc.get(i) == null) {
				loc.put(i, -99);
			}
			toRet += loc.get(i);
			toRet += deliminater;

			if (fanIn.get(i) == null) {
				fanIn.put(i, -99);
			}
			toRet += fanIn.get(i);
			toRet += deliminater;
			if (fanOut.get(i) == null) {
				fanOut.put(i, -99);
			}
			toRet += fanOut.get(i);
			toRet += deliminater;
			if (changeFrequency.get(i) == null) {
				changeFrequency.put(i, -99);
			}
			toRet += changeFrequency.get(i);
			toRet += deliminater;
			if (ticketFrequency.get(i) == null) {
				ticketFrequency.put(i, -99);
			}
			toRet += ticketFrequency.get(i);
			toRet += deliminater;
			if (bugChangeFrequency.get(i) == null) {
				bugChangeFrequency.put(i, -99);
			}
			toRet += bugChangeFrequency.get(i);
			toRet += deliminater;
			if (pairChangeFrequency.get(i) == null) {
				pairChangeFrequency.put(i, -99);
			}
			toRet += pairChangeFrequency.get(i);
			toRet += "\n";
		}

		return toRet;
	}
}
