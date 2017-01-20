import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import Models.Location;

public class FileReaderClass {

	public static ArrayList<Location> read(File file) {
		
		ArrayList<Location> locations = new ArrayList<>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
		    
			String line;
		    
		    
		    while ((line = reader.readLine()) != null) {
		    	Location location = new Location();
		    	ArrayList<Integer> dist = new ArrayList<>();
		    	
		    	
		    	String[] variables = line.split("Distance:");
		    	String[] nodeValues = variables[0].split(" ");
		    	
		    	int id = Integer.valueOf(nodeValues[0]);
		    	String timeWindow = nodeValues[1];
		    	int t = Integer.valueOf(nodeValues[2]);
		    	timeWindow = timeWindow.substring(1, timeWindow.length()-1);
		    	String[] timeValues = timeWindow.split(",");
		    	int a = Integer.valueOf(timeValues[0]);
		    	int b = Integer.valueOf(timeValues[1]);

		    	String[] distanceMatrix = variables[1].split(",");
		    	
		    	for(String s: distanceMatrix){
		    		
		    		if(s.contains("[")){
		    			s = s.substring(2, s.length());
		    		}
		    		
		    		else if(s.contains("]")){
		    			s = s.substring(1, s.length()-1);
		    		}
		    		else{
		    			s = s.substring(1, s.length());
		    		}
		    		
		    		dist.add(Integer.valueOf(s));
		    		
		    	}
		    	
		    	location.setId(id);
		    	location.setA(a);
		    	location.setB(b);
		    	location.setT(t);
		    	location.setDist(dist);
		    	
		    	locations.add(location);
		    	
		    	
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return locations;
	}

}
