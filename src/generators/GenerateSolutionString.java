package generators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import Models.FeasibleSolution;
import Models.Location;
import Models.Route;

public class GenerateSolutionString {
	
	private static String getArrivalTime(int arrivalTime) throws ParseException{
		 String myTime = "08:00";
		 SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		 Date d = df.parse(myTime); 
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(d);
		 cal.add(Calendar.MINUTE, arrivalTime);
		 String newTime = df.format(cal.getTime());
		 
		 return newTime;
	}

	public static String toString(String string, FeasibleSolution solutionObject, ArrayList<Location> locations, double fitness) throws ParseException {
		
		
		String solution = "For " + locations.size() + " locations, I could find " + solutionObject.getVehicles().size() + " routes using: " + string + "\n";
		solution += "Fitness: " + fitness + "\n";
		solution += "Arrival time at home for latest car: " + getArrivalTime((int) solutionObject.getLatestTime()) + "\n\n";

		for(int i = 0; i<solutionObject.getVehicles().size(); i++){
			solution += "Route " + (i+1) + " covers locations with ID: ";
			Route route = solutionObject.getVehicles().get(i);
			
			for(Location location: route.getLocations()){
				
				if(location.getId() == 0){
					continue;
				}
				
				solution += location.getId() + ", ";
			}
			
			solution = solution.substring(0, solution.length()-2);
			
			solution += "\n";
		}
		
		return solution;
	}

}
