package BRKGA;

import Models.FeasibleSolution;

public class Chromosome {
	
	private double fitness;
	private FeasibleSolution solution;
	
	public Chromosome(){
		this.fitness = 0.0;
		solution = new FeasibleSolution();
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public FeasibleSolution getSolution() {
		return solution;
	}

	public void setSolution(FeasibleSolution solution) {
		this.solution = solution;
	}

	
	


}
