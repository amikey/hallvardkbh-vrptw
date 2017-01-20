package generators;
import java.util.ArrayList;

import BRKGA.BRKGA;
import Models.FeasibleSolution;
import Models.Location;
import Models.Route;

public class SolutionGenerator {
	
	public final static int maxRouteTime = 720;

	
	public static FeasibleSolution genereateSolution(ArrayList<Location> locations) {
		FeasibleSolution solution = new FeasibleSolution();

		
		while(locations.size() -1 != 0){
			
			
			Route route = new Route();
			int timeFromLastNodeToHome = 0;
						
			ArrayList<Location> usedLocations = new ArrayList<>();
			
			
			for(Location location: locations){
				
				if(location.getId() == 0){
					route.getLocations().add(location);
					continue;
				}
				
				int travelTimeBetweenNodes = calculateTravelTime(route, location);
				int nodeTime = travelTimeBetweenNodes + location.getT();
				int arrivalTime = route.getTotalTime() + travelTimeBetweenNodes;
				int timeFromNodeToHome = calculateTimeHome(route, location);
				timeFromLastNodeToHome = calculateLastLocationHome(route);
				
				if(arrivalTime < location.getA()){
					int waitingTime = location.getA()-arrivalTime;
					arrivalTime = arrivalTime + waitingTime;
					nodeTime = nodeTime + waitingTime;
				}
				
				if(isFeasible(route, location, nodeTime, arrivalTime, timeFromNodeToHome)){
					route.getLocations().add(location);
					route.setTotalTime(route.getTotalTime() + nodeTime);
					
					usedLocations.add(location);
				}
				
			}
			
			route.setTotalTime(route.getTotalTime() + timeFromLastNodeToHome);
			solution.getVehicles().add(route);
			for(Location location: usedLocations){
				locations.remove(location);
			}
		}
		
		return solution;
	}

	private static boolean isFeasible(Route route, Location location, int nodeTime, int arrivalTime, int timeFromNodeToHome) {
		if(route.getTotalTime() + nodeTime >= maxRouteTime){
			return false;
		}
		if(arrivalTime > location.getB()){
			return false;
		}
		if(arrivalTime < location.getA()){
			return false;
		}
		if(route.getTotalTime() + nodeTime + timeFromNodeToHome >= maxRouteTime){
			return false;
		}
		
		return true;
	}

	private static int calculateLastLocationHome(Route route) {
		return route.getLocations().get(route.getLocations().size()-1).getDist().get(0);
	}

	private static int calculateTimeHome(Route route, Location location) {
		return route.getLocations().get(0).getDist().get(location.getId());
	}

	private static int calculateTravelTime(Route route, Location location) {
		return route.getLocations().get(route.getLocations().size()-1).getDist().get(location.getId());
	}

	public static double calculateFitness(FeasibleSolution solution, int counter) {
		
		ArrayList<Route> vehicles = solution.getVehicles();
		
		if(vehicles.size() == 0){
			return 0.0;
		}
	
		double fitness = 1;
		double numberOfCars = vehicles.size();
		
		double latestArrivalTime = 0;
		
		for(Route route : vehicles){
			if(route.getTotalTime() > latestArrivalTime){
				latestArrivalTime = route.getTotalTime();
			}
		}
		
		double timeValue = (latestArrivalTime/BRKGA.maxRouteTime);
		
		fitness = fitness - (numberOfCars/counter) - (timeValue/counter);
		
		return fitness;
		
	}

}
