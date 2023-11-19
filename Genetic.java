import java.util.Random;

public class Genetic {
    public static final double MUTATION_RATE = 0.2;

    Population population = new Population();
    Individual fittest;
    Individual secondFittest;
    int generationCount = 0;    

    public static void main(String[] args) {

        Random rn = new Random();

        Genetic ge = new Genetic();

        //Initialize population
        ge.population.initializePopulation(10);

        //Calculate fitness of each individual
        ge.population.calculateFitness();

        System.out.print("Generation: " + ge.generationCount + " Fittest: " + ge.population.fittest);
		System.out.print("   >>> ");
		for (int i = 0; i < ge.population.getFittest().geneLength; i++) {
			System.out.print(ge.population.getFittest().genes[i]);
		}
		System.out.println();

        //While population gets an individual with maximum fitness
        while (ge.population.fittest < 5) {
            ++ge.generationCount;

            // SELECTION
            ge.selection();

            // CROSSOVER
            ge.crossover();

            // MUTATION
            if (rn.nextDouble() < MUTATION_RATE) {
				ge.mutation();
			}

            //Add fittest offspring to population
            ge.addFittestOffspring();

            //Calculate new fitness value
            ge.population.calculateFitness();

            System.out.print("Generation: " + ge.generationCount + " Fittest: " + ge.population.fittest);
			System.out.print("   >>> ");
			for (int i = 0; i < ge.population.getFittest().geneLength; i++) {
				System.out.print(ge.population.getFittest().genes[i]);
			}
			System.out.println();
        }

        System.out.println("\nSolution found in generation " + ge.generationCount);
        System.out.println("Fitness: " + ge.population.getFittest().fitness);
        System.out.print("Genes: ");
        for (int i = 0; i < ge.population.getFittest().geneLength; i++) {
            System.out.print(ge.population.getFittest().genes[i]);
        }
        System.out.println("");

    }

    //Selection
    void selection() {
        //Select the most fittest individual
        fittest = population.getFittest();

        //Select the second most fittest individual
        secondFittest = population.getSecondFittest();
    }

    //Crossover
    void crossover() {
	Random rn = new Random();
		
	// Select a random crossover point
	int crossOverPoint = rn.nextInt(population.individuals[0].geneLength);
	    
        //Swap values among parents
        for (int i = 0; i < crossOverPoint; i++) {
            int temp = fittest.genes[i];
            fittest.genes[i] = secondFittest.genes[i];
            secondFittest.genes[i] = temp;

        }
    }

    //Mutation
    void mutation() {
        Random rn = new Random();

        //Select a random mutation point
        int mutationPoint = rn.nextInt(population.individuals[0].geneLength);

        //Flip values at the mutation point
        if (fittest.genes[mutationPoint] == 0) {
            fittest.genes[mutationPoint] = 1;
        } else {
            fittest.genes[mutationPoint] = 0;
        }

        mutationPoint = rn.nextInt(population.individuals[0].geneLength);

        if (secondFittest.genes[mutationPoint] == 0) {
            secondFittest.genes[mutationPoint] = 1;
        } else {
            secondFittest.genes[mutationPoint] = 0;
        }
    }

    //Get fittest offspring
    Individual getFittestOffspring() {
        if (fittest.fitness > secondFittest.fitness) {
            return fittest;
        }
        return secondFittest;
    }


    //Replace least fittest individual from most fittest offspring
    void addFittestOffspring() {

        //Update fitness values of offspring
        fittest.calcFitness();
        secondFittest.calcFitness();

        //Get index of least fit individual
        int leastFittestIndex = population.getLeastFittestIndex();

        //Replace least fittest individual from most fittest offspring
        population.individuals[leastFittestIndex] = getFittestOffspring();
    }

}


//Individual class
class Individual {

	final int GENLEN = 12;
    int fitness = 0;
    int[] genes = new int[GENLEN];
    int geneLength = GENLEN;

    public Individual() {
        Random rn = new Random();

        //Set genes randomly for each individual
        for (int i = 0; i < genes.length; i++) {
            genes[i] = Math.abs(rn.nextInt() % 2);
        }

        fitness = 0;
    }

    //Calculate fitness
    public void calcFitness() {

        fitness = 0;
        for (int i = 0; i < genes.length; i+=2) {
            if (genes[i] == 1 && genes[i+1] == 0) {
                ++fitness;
            }
        }
    }

}

//Population class
class Population {

    int popSize = 10;
    Individual[] individuals = new Individual[10];
    int fittest = 0;

    //Initialize population
    public void initializePopulation(int size) {
        for (int i = 0; i < individuals.length; i++) {
            individuals[i] = new Individual();
        }
    }

    //Get the fittest individual
    public Individual getFittest() {
        int maxFit = Integer.MIN_VALUE;
        int maxFitIndex = 0;
        for (int i = 0; i < individuals.length; i++) {
            if (maxFit <= individuals[i].fitness) {
                maxFit = individuals[i].fitness;
                maxFitIndex = i;
            }
        }
        fittest = individuals[maxFitIndex].fitness;
        return individuals[maxFitIndex];
    }

    //Get the second most fittest individual
    public Individual getSecondFittest() {
        int maxFit1 = 0;
        int maxFit2 = 0;
        for (int i = 0; i < individuals.length; i++) {
            if (individuals[i].fitness > individuals[maxFit1].fitness) {
                maxFit2 = maxFit1;
                maxFit1 = i;
            } else if (individuals[i].fitness > individuals[maxFit2].fitness) {
                maxFit2 = i;
            }
        }
        return individuals[maxFit2];
    }

    //Get index of least fittest individual
    public int getLeastFittestIndex() {
        int minFitVal = Integer.MAX_VALUE;
        int minFitIndex = 0;
        for (int i = 0; i < individuals.length; i++) {
            if (minFitVal >= individuals[i].fitness) {
                minFitVal = individuals[i].fitness;
                minFitIndex = i;
            }
        }
        return minFitIndex;
    }

    //Calculate fitness of each individual
    public void calculateFitness() {

        for (int i = 0; i < individuals.length; i++) {
            individuals[i].calcFitness();
        }
        getFittest();
    }

}

