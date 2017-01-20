package BRKGA;

import generators.SolutionGenerator;

public class Decoder {

	public Population decodeFitness(Population current, int nrGenes) {
		
		for(Chromosome chromosome: current.getChromosomeSet()){
			chromosome.setFitness(SolutionGenerator.calculateFitness(chromosome.getSolution(), nrGenes));
		}
		
		return current;
	}

}
