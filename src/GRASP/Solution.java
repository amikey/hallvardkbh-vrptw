package GRASP;

import java.util.ArrayList;

import Models.FeasibleSolution;
import Models.Location;

public class Solution {
	

	private FeasibleSolution feasibleSolution;
	private int cost;
	private double fitness = 0.0;
	private ArrayList<Location> locsInSolution;

	public Solution() {
		feasibleSolution = new FeasibleSolution();
		locsInSolution = new ArrayList<>();
		cost = 100000;
	}
	
	
	public FeasibleSolution getFeasibleSolution() {
		return feasibleSolution;
	}
	public void setFeasibleSolution(FeasibleSolution feasibleSolution) {
		this.feasibleSolution = feasibleSolution;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public double getFitness() {
		return fitness;
	}
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	public ArrayList<Location> getLocsInSolution() {
		return locsInSolution;
	}
	public void setLocsInSolution(ArrayList<Location> locsInSolution) {
		this.locsInSolution = locsInSolution;
	}
	
	

}
