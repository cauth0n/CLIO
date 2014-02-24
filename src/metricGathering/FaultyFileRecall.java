package metricGathering;

import java.util.ArrayList;
import java.util.List;

public class FaultyFileRecall {

	private final int r1 = 7;
	private List<Integer> futureReleases;
	private List<SourceFile> sourceFiles;
	private List<String> faultyFileRecall;

	public FaultyFileRecall(List<SourceFile> sourceFiles) {
		this.sourceFiles = sourceFiles;

		process();
	}

	public void process() {
		futureReleases = new ArrayList<>();
		faultyFileRecall = new ArrayList<>();
		for (int i = 8; i > r1; i--) {
			futureReleases.add(new Integer(i));
		}
		// all files afer r1 are future releases

		for (SourceFile sf : sourceFiles) {
			for (Integer i : futureReleases) {
				if (sf.getBugChangeFrequency().get(i) > 0) {

					faultyFileRecall.add(sf.getName());

					break;// accounts for the 'don't count extra'
				}
			}
		}
		for (String s : faultyFileRecall) {
			System.out.println(s);
		}
	}

	public List<String> getFaultyFileRecall() {
		return faultyFileRecall;
	}

}
