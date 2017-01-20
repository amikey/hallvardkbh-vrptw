package BRKGA;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import Models.Location;
import Models.Route;
import generators.GenerateSolutionString;
import generators.SolutionGenerator;

public class BRKGA {
	
	public final static int maxRouteTime = 720;
	
	//Counter for current generation
	private int currGen = 0;
	
	//maximum number of generations we can run for
	private final int maxGen = 50000;
	
	//Number of genes in each chromosome
	private int nrGenes; 
	
	private ArrayList<Location> locations;
	
	//Number of chromosomes in a population
	private final int pSize = 10; 
	
	//How many of each generation is elite
	private final int eSize = 3; 
	
	//How many of each generation is mutant
	private final int mSize = 3; 
	
	//Probability that a mutant inherits genes from the elite parent
	private final double probElite = 0.7; 
	
	//Current and previous population
	private Population current;
	private Population prev;
	
	//Our decoder 
	private Decoder decoder;
	
	private Chromosome bestChromosome;
	

	@SuppressWarnings("unchecked")
	public BRKGA(ArrayList<Location> locations, double threshold) {
		this.nrGenes = locations.size();
		this.locations = (ArrayList<Location>) locations.clone();
		
		
		
		decoder = new Decoder();
		
		current = new Population();
		prev = new Population();
		
		generatePopulation(locations);
		
		bestChromosome = new Chromosome();
		
		do{
			current = decoder.decodeFitness(current, nrGenes);
			
			sortAndClassify();
			bestChromosome = getBestChromosome();
			makeNextGeneration(locations);
			currGen++;
		}
		while(currGen < maxGen && bestChromosome.getFitness() < threshold);	
	}


	private void makeNextGeneration(ArrayList<Location> locations) {
		cloneElites();
		generateMutants(locations);
		crossoverPhase();
		
	}


	private void crossoverPhase() {
		
		int sizeOfChromosomeSet = current.getChromosomeSet().size();
		
		while(sizeOfChromosomeSet < pSize){
			
			Chromosome elite = randomSelection(prev.getEliteSet());
			Chromosome other = randomSelection(prev.getNonEliteSet());
			
			Chromosome chromosomeCross = cross(elite, other);
			
			ArrayList<Location> locsNotUsed = new ArrayList<>();
			
			
			locsNotUsed.add(chromosomeCross.getSolution().getVehicles().get(0).getLocations().get(0));
			
			for(Route route: chromosomeCross.getSolution().getVehicles()){
				for(Location location: route.getLocations()){
					
					if(location.getId() != 0){
						locsNotUsed.add(location);
					}
				}
			}
			
			chromosomeCross.setSolution(SolutionGenerator.genereateSolution(locsNotUsed));
			current.getChromosomeSet().add(chromosomeCross);
			sizeOfChromosomeSet = current.getChromosomeSet().size();
			
		}
		
	}


	private Chromosome cross(Chromosome elite, Chromosome other) {
		Chromosome crossMember = new Chromosome();
		
		ArrayList<Route> candidate = new ArrayList<>();
		
		ArrayList<Location> locationsNotUsed = new ArrayList<>();
		locationsNotUsed.add(elite.getSolution().getVehicles().get(0).getLocations().get(0));
		
		for(Route route: elite.getSolution().getVehicles()){
			for(Location location: route.getLocations()){
				
				if(location.getId() == 0){
					continue;
				}
				locationsNotUsed.add(location);
				
			}
		}
		
		for(Route route: elite.getSolution().getVehicles()){
			Random r = new Random();
			double threshold = r.nextDouble()*1;
			
			if(threshold < probElite){
				candidate.add(route);
				
				for(Location location: route.getLocations()){
					locationsNotUsed.remove(location);
				}
			}
		}
		
		Route route = new Route();
		
		for(Route r: other.getSolution().getVehicles()){
			for(Location location: r.getLocations()){
				if(locationsNotUsed.contains(location)){
					route.getLocations().add(location);
					locationsNotUsed.remove(location);
				}
			}
		}

		candidate.add(route);
		
		crossMember.getSolution().setVehicles(candidate);
		
		return crossMember;
	}


	private Chromosome randomSelection(ArrayList<Chromosome> chromosomes) {
		Random random = new Random();
		return chromosomes.get(random.nextInt(chromosomes.size()));
	}


	private void generateMutants(ArrayList<Location> locations) {
		for(int i = 0; i < mSize; i++){
			Chromosome chromosome = new Chromosome(); 
			chromosome.setSolution(SolutionGenerator.genereateSolution(locations));
			current.getChromosomeSet().add(chromosome);
			current.getMutantSet().add(chromosome);
		}
		
	}


	private void cloneElites() {
		
		for(Chromosome elite: prev.getEliteSet()){
			current.getChromosomeSet().add(elite);
		}	
		
	}


	private Chromosome getBestChromosome() {
		return current.getChromosomeSet().get(0);
	}


	private void sortAndClassify() {
		
		Collections.sort(current.getChromosomeSet(), new Comparator<Chromosome>() {

			@Override
			public int compare(Chromosome o1, Chromosome o2) {
				double f1 = o1.getFitness();
				double f2 = o2.getFitness();
				
				if(f1 > f2){
					return -1;
				}
				else if(f1 < f2){
					return 1;
				}
				
				return 0;
			}
		});
		
		current.getEliteSet().clear();
		current.getNonEliteSet().clear();
		
		for(int i = 0; i<eSize; i++){
			Chromosome elite = current.getChromosomeSet().get(i);
			current.getEliteSet().add(elite);
		}
		
		for(int j = 0; j<current.getChromosomeSet().size(); j++){
			Chromosome chromosome = current.getChromosomeSet().get(j);
			
			if(!current.getEliteSet().contains(chromosome)){
				current.getNonEliteSet().add(chromosome);
			}
		}
		
		storePrev();
		
	}


	private void storePrev() {
		resetPopulation(prev);
		
		for(Chromosome chromosome: current.getChromosomeSet()){
			prev.getChromosomeSet().add(chromosome);
		}
		
		for(Chromosome chromosome: current.getEliteSet()){
			prev.getEliteSet().add(chromosome);
		}
		
		for(Chromosome chromosome: current.getNonEliteSet()){
			prev.getNonEliteSet().add(chromosome);
		}
		
	}


	private void resetPopulation(Population population) {
		
		population.getChromosomeSet().clear();
		population.getEliteSet().clear();
		population.getMutantSet().clear();
		population.getNonEliteSet().clear();
	}


	private void generatePopulation(ArrayList<Location> locations) {
		
		for(int i = 0; i < pSize; i++){
			Chromosome chromosome = new Chromosome();
			chromosome.setSolution(SolutionGenerator.genereateSolution(locations));
			
			if(current.getChromosomeSet().size() < pSize){
				current.getChromosomeSet().add(chromosome);
			}
		}
		
	}
	
	@Override
	public String toString() {
		
		try {
			return GenerateSolutionString.toString("BRKGA", bestChromosome.getSolution(), locations, bestChromosome.getFitness());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return "Solution not working"; 
	}

}
