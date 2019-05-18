package siz.terry.linker;

import java.io.File;

import siz.terry.reader.LayerReader;

public class Main {
	public static LayerReader reader;

	public static void main(String[] args) {
		// read input args
		if (args.length > 0) {
			System.out.println(args[0]);
			File input = new File(args[0]);
			if (input.isFile()) {
				// input is a file; process it
				ToBuildingLinker.process(input);
			}
			if (input.isDirectory()) {
				// input is a directory; process each file
				for (File file : input.listFiles()) {
					if (file.isFile() && file.getName().substring(file.getName().length() - 5, file.getName().length())
							.equals("layer")) {
						ToBuildingLinker.process(file);
					}
				}
			}
		} else {
			// no input; process working directory
			File workingDir = new File(".");
			if (workingDir.isDirectory()) {
				for (File file : workingDir.listFiles()) {
					if (file.isFile() && file.getName().substring(file.getName().length() - 5, file.getName().length())
							.equals("layer")) {
						ToBuildingLinker.process(file);
					}
				}
			}
		}
		
	}
}
