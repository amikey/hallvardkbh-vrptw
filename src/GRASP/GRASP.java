package GRASP;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import Models.Location;
import generators.GenerateSolutionString;
import generators.SolutionGenerator;

public class GRASP {
	
	public final static int maxRouteTime = 720;
	
	private Solution bestSolution;
	private ArrayList<Location> locations;
	private double alpha = 0.1; //alpha threshold


	public GRASP(ArrayList<Location> locations, double threshold) {
		this.locations = locations;
		
		doGRASP(locations, alpha, threshold);
		
	}


	@SuppressWarnings("unchecked")
	private void doGRASP(ArrayList<Location> locations, double alpha, double threshold) {
		
		ArrayList<Location> locationsNotUsed = (ArrayList<Location>) locations.clone();
		
		ArrayList<Location> listOfLocationsToVisit = getLocationsToVisit(locationsNotUsed);
		
		for(Location location: listOfLocationsToVisit){
			locationsNotUsed.remove(location);
		}
		
		for(Location location: listOfLocationsToVisit){
			locationsNotUsed.add(location);
		}
		
		bestSolution = new Solution();
		bestSolution.setFeasibleSolution(SolutionGenerator.genereateSolution(locationsNotUsed));
		int counter = 0;
		
		while(counter < 5000 && bestSolution.getFitness() < threshold){ 
			
			locationsNotUsed.clear();
			locationsNotUsed = (ArrayList<Location>) locations.clone();
			
			Solution candidate = GRC(locationsNotUsed, alpha);
			candidate = localSearch(locations, candidate);
			
			int cost = calculateCost(candidate);
			candidate.setCost(cost);
			
			if(candidate.getCost() < bestSolution.getCost()){
				bestSolution = candidate;
				bestSolution.setFitness(SolutionGenerator.calculateFitness(bestSolution.getFeasibleSolution(), locations.size()));
			}
			
			
			counter++;
		}
		
	}

	private int calculateCost(Solution candidate) {
		int cost = 0;
		cost += candidate.getFeasibleSolution().getVehicles().size()*1000; //cost of 1000 for every car
		cost += candidate.getFeasibleSolution().getLatestTime()*10; //10 for every minute
		return cost;
	}


	//Greedy Randomized Construction
	@SuppressWarnings("unchecked")
	private Solution GRC(ArrayList<Location> locations, double alpha) {
		
		Solution candidate = new Solution();
		
		Location initialNode = locations.get(0);
		
		candidate.getLocsInSolution().add(initialNode);
		
		boolean done = false;
				
		while(!done){
			
			double seed = alpha;
						
			HashMap<Integer, Integer> fc = new HashMap<>();
			
			ArrayList<Integer> usedLocations = addLocations(candidate);

			fc = generateFC(locations, usedLocations, candidate);

			
			ArrayList<Location> RCL = new ArrayList<>();
			
			int minCost = findMin(fc);
			int maxCost = findMax(fc);
			
			HashMap<Integer, Integer> fC2 = (HashMap<Integer, Integer>) fc.clone();
			

			while(!fC2.isEmpty()){
				
				int keyMin = findMinKey(fC2);

				
				for(int i = keyMin; i< keyMin + fC2.size(); i++){
					
					if(fC2.get(i) == null){
						continue;
					}
					
					if((double)fC2.get(i) <= (minCost + (seed * (maxCost - minCost)))){
						Location locationToAdd = locations.get(i);
						RCL.add(locationToAdd);
						fC2.remove(i);
					}
				}
				
				
				seed = generateNewSeedValue(seed);
				
			}
			
			
			if(!fc.isEmpty() && !RCL.isEmpty()){
				Random r = new Random();
				int random = r.nextInt(RCL.size());
				
				Location location = RCL.get(random);
				
				candidate.getLocsInSolution().add(location);
			}
			
			if(fc.isEmpty()){
				
				ArrayList<Location> routeToEvaluate = new ArrayList<>();
				
				for(Location location: candidate.getLocsInSolution()){
					routeToEvaluate.add(location);
				}
				
				candidate.setFeasibleSolution(SolutionGenerator.genereateSolution(routeToEvaluate));
				done = true;
				
			}
			
		}
		
		return candidate;
		
	}


	private double generateNewSeedValue(double seed) {
		seed = seed + 0.1;
		return seed > 1 ? 1 : seed;
	}


	private int findMinKey(HashMap<Integer, Integer> fC2) {
		int keyMin = 100000;
		
		for(Integer key: fC2.keySet()){
			if(keyMin > key){
				keyMin = key;
			}
		}
		
		return keyMin;
	}


	private int findMax(HashMap<Integer, Integer> fc) {
		int maxCost = 0;
		
		for(Integer i: fc.values()){
			if(i > maxCost){
				maxCost = i;
			}
		}
		
		return maxCost;
	}


	private int findMin(HashMap<Integer, Integer> fc) {
		int minCost = 10000000;
		
		for(Integer i: fc.values()){
			if(i < minCost){
				minCost = i;
			}
		}
		
		return minCost;
	}


	private HashMap<Integer, Integer> generateFC(ArrayList<Location> locations, ArrayList<Integer> usedLocations,
			Solution candidate) {
		
		HashMap<Integer, Integer> featureCost = new HashMap<>();
		
		for(Location location: locations){
			if(usedLocations.contains(location.getId())){
				continue;
			}
			else{
				Integer cost = costLocToSolution(candidate, location);
				featureCost.put(location.getId(), cost);	
			}
		}
		
		return featureCost;
		
	}


	private Integer costLocToSolution(Solution candidate, Location location) {
		int travelTimeBetweenNodes = candidate.getLocsInSolution().get(candidate.getLocsInSolution().size()-1).getDist().get(location.getId());
		int nodeCost = travelTimeBetweenNodes + location.getT();
		
		return nodeCost;
	}


	private Solution localSearch(ArrayList<Location> locations, Solution candidate) {
		
		Solution bestLocal = new Solution();
		
		int counter = 0;
		
		while(counter < 8){
			Solution solution = new Solution();
			
			ArrayList<Location> locInCandidate = new ArrayList<>();
			locInCandidate.addAll(candidate.getFeasibleSolution().getAllLocations());
			
			Random r = new Random();
			int r1 = r.nextInt(locInCandidate.size());
			int r2 = r.nextInt(locInCandidate.size());

			Collections.swap(locInCandidate, r1, r2);
			
			locInCandidate.add(0, locations.get(0));

			solution.setFeasibleSolution(SolutionGenerator.genereateSolution(locInCandidate));
			solution.setCost(calculateCost(solution));
			
			if(solution.getCost() < bestLocal.getCost()){
				bestLocal = solution;
			}
			
			counter++;
		}
		
		return bestLocal;
	}


	private ArrayList<Location> getLocationsToVisit(ArrayList<Location> locationsNotUsed) {
		
		ArrayList<Location> locations = new ArrayList<>();
		
		for(Location location: locationsNotUsed){
			
			if(location.getId() == 0){
				continue;
			}
			
			locations.add(location);
		}
		
		
		Collections.shuffle(locations);
		
		return locations;
	}
	
	private ArrayList<Integer> addLocations(Solution candidate) {
		
		ArrayList<Integer> usedLocations = new ArrayList<>();
		
		for(Location location: candidate.getLocsInSolution()){
			usedLocations.add(location.getId());
		}
		
		return usedLocations;
	}
	


	@Override
	public String toString() {
		
		try {
			return GenerateSolutionString.toString("GRASP", bestSolution.getFeasibleSolution(), locations, bestSolution
					.getFitness());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return "Solution not working"; 
		
	}

}
