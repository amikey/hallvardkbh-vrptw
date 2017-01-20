import java.io.File;
import java.util.ArrayList;

import Models.*;
import GRASP.*;
import BRKGA.*;


public class Main {
	


	public static void main(String[] args) {
		
		String path = args[0];
		String algorithm = args[1];
		File file = new File(path);
		
		long start = System.currentTimeMillis();;
				
		ArrayList<Location> locations = FileReaderClass.read(file);
		
		double threshold = 0.5;
		
		if(algorithm.equals("brkga")){
			BRKGA brkga = new BRKGA(locations, threshold);
			System.out.println(brkga);
		}
		else if(algorithm.equals("grasp")){
			GRASP grasp = new GRASP(locations, threshold);
			System.out.println(grasp);
		}
		else{
			System.out.println("No algorithm chosen. Chose either BRKGA or GRASP.");
		}
		
		long end = System.currentTimeMillis();;
		System.out.println("Execution time: " + (end - start) + " ms");

	}

}
