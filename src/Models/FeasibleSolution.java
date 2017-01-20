package Models;

import java.util.ArrayList;

public class FeasibleSolution {
	

	private ArrayList<Route> vehicles;
	
	public FeasibleSolution(){
		this.vehicles = new ArrayList<>();
	}
	
	public ArrayList<Route> getVehicles() {
		return vehicles;
	}

	public void setVehicles(ArrayList<Route> vehicles) {
		this.vehicles = vehicles;
	}

	public int getLatestTime(){
		
		int latest = 0;
		
		for(Route route: vehicles){
			if(route.getTotalTime() > latest){
				latest = route.getTotalTime();
			}
		}
		
		return latest;
	}
	
	public ArrayList<Location> getAllLocations() {
		
		ArrayList<Location> locations = new ArrayList<>();
		ArrayList<Location> locations2 = new ArrayList<>();
		
		for(Route route: vehicles){
			locations.addAll(route.getLocations());
		}
		
		for(Location location: locations){
			if(location.getId() != 0){
				locations2.add(location);
			}
		}
		
		return locations2;
		
	}

}
