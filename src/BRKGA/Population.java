package BRKGA;

import java.util.ArrayList;

public class Population {
	
	private ArrayList<Chromosome> chromosomeSet;
	private ArrayList<Chromosome> eliteSet;
	private ArrayList<Chromosome> nonEliteSet;
	private ArrayList<Chromosome> mutantSet;
	
	public Population(){
		chromosomeSet = new ArrayList<>();
		eliteSet = new ArrayList<>();
		nonEliteSet = new ArrayList<>();
		mutantSet = new ArrayList<>();
	}

	public ArrayList<Chromosome> getChromosomeSet() {
		return chromosomeSet;
	}

	public void setChromosomeSet(ArrayList<Chromosome> chromosomeSet) {
		this.chromosomeSet = chromosomeSet;
	}

	public ArrayList<Chromosome> getEliteSet() {
		return eliteSet;
	}

	public void setEliteSet(ArrayList<Chromosome> eliteSet) {
		this.eliteSet = eliteSet;
	}

	public ArrayList<Chromosome> getNonEliteSet() {
		return nonEliteSet;
	}

	public void setNonEliteSet(ArrayList<Chromosome> nonEliteSet) {
		this.nonEliteSet = nonEliteSet;
	}

	public ArrayList<Chromosome> getMutantSet() {
		return mutantSet;
	}

	public void setMutantSet(ArrayList<Chromosome> mutantSet) {
		this.mutantSet = mutantSet;
	}

}
