package moduleOperations;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Operations {

	public void parseRelease(Release release) {
		try {
			File file = new File(release.getFileName());
			Scanner in = new Scanner(file);

			String fill = in.nextLine();

			while (in.hasNext()) {
				String[] line = in.nextLine().split(" ");

				if (line[6].toLowerCase().equals("false")) {
					release.addModularityViolation(new ModularityViolation(line[1], line[3], Integer.parseInt(line[4]),
							Integer.parseInt(line[5])));
				}
			}

			in.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
		}
	}
}
