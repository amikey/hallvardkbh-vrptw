package Models;


import java.util.ArrayList;

public class Route {
	
	private ArrayList<Location> locations;
	private int totalTime;
	
	public Route(){
		this.locations = new ArrayList<>();
		this.totalTime = 0;
	}

	public ArrayList<Location> getLocations() {
		return locations;
	}

	public void setLocations(ArrayList<Location> locations) {
		this.locations = locations;
	}

	public int getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}

	
	
	

}
