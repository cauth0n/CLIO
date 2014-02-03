package metricGathering;

import java.util.HashMap;
import java.util.Map;

public class SourceFile {

	private String name;
	private Map<Integer, Integer> fileSize;
	private Map<Integer, Integer> fanIn;
	private Map<Integer, Integer> fanOut;
	private Map<Integer, Integer> changeFrequency;
	private Map<Integer, Integer> ticketFrequency;
	private Map<Integer, Integer> bugChangeFrequency;
	private Map<Integer, Integer> pairChangeFrequency;

	public SourceFile(String name) {
		this.name = name;
		fileSize = new HashMap<>();
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

	public String toString() {
		String toRet = "";
		String deliminater = ", ";

		for (int i = 0; i < 8; i++) {
			toRet += name;
			toRet += deliminater;
			toRet += (i + 1);
			toRet += deliminater;
			toRet += fileSize.get(i);
			toRet += deliminater;
			toRet += fanIn.get(i);
			toRet += deliminater;
			toRet += fanOut.get(i);
			toRet += deliminater;
			toRet += changeFrequency.get(i);
			toRet += deliminater;
			toRet += ticketFrequency.get(i);
			toRet += deliminater;
			toRet += bugChangeFrequency.get(i);
			toRet += deliminater;
			toRet += pairChangeFrequency.get(i);
			toRet += "\n";
		}

		return toRet;
	}
}
