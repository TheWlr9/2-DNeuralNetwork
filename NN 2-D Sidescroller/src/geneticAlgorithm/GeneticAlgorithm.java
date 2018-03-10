package geneticAlgorithm;

import java.util.ArrayList;

public class GeneticAlgorithm{
 private ArrayList<Genome> population;
 private Genome bestOne;
 public double mutRate;
 public double crossRate;
 public int popSize;
 public int numWeights;
 
 final double MAX_PERTURBUTION= 0.3;
 
 public GeneticAlgorithm(int popSize, double mutRate, double crossRate, int numWeights) {
  population= new ArrayList<Genome>();
  bestOne= new Genome();
  this.mutRate= mutRate;
  this.crossRate= crossRate;
  this.popSize= popSize;
  this.numWeights= numWeights;
  
  for(int i= 0; i<popSize; i++) {
   population.add(new Genome(numWeights));
   
   for(int j= 0; j<numWeights; j++) {
    population.get(i).weights[j]= Math.random()*2-1; //-1<population.get(i).weights[j]<1
   }
  }
 }
 
 public void mutate(Genome g) {
  for(int i= 0; i<g.weights.length; i++)
   if(Math.random()<mutRate) {
    //if(g.weights[i]<bestOne.weights[i])
     //g.weights[i]+= Math.random()*(bestOne.weights[i]-g.weights[i]);
    //else
     //g.weights[i]-= Math.random()*(g.weights[i]-bestOne.weights[i]);
    //g.weights[i]= bestOne.weights[i];
    g.weights[i]= (Math.random()*2-1)*MAX_PERTURBUTION;
   }
 }
 
 private Genome getChromoRoulette() {
  int totalFitness= 0;
  
  for(int i= 0; i<population.size(); i++)
   totalFitness+= population.get(i).fitness;
  
  double slice= Math.random()*totalFitness; //The slice of the pie
  Genome theOne= new Genome(numWeights);
  int fitnessSoFar= 0;
  
  while(true) { //Keep going until we return something
   for(int i= 0; i<population.size(); i++) {
    fitnessSoFar+= population.get(i).fitness;
    
    if(fitnessSoFar>=slice) {
     theOne= population.get(i);
     
     return theOne;
    }
   }
  }
 }
 
 public void crossover(Genome baby1, Genome baby2, Genome mom, Genome dad) {
  if(Math.random()<=crossRate && mom!=dad) {
   int crossoverPoint= (int)Math.ceil(Math.random()*numWeights-1);
   
   System.out.println("Crossover!");
   
   for(int i= 0; i<crossoverPoint; i++) {
    baby1.weights[i]= mom.weights[i];
    baby2.weights[i]= dad.weights[i];
   }
   
   for(int i= crossoverPoint; i<mom.weights.length; i++) {
    baby1.weights[i]= dad.weights[i];
    baby2.weights[i]= mom.weights[i];
   }
  }
  else {
   baby1= mom;
   baby2= dad;
  }
 }
 
 public ArrayList<Genome> epoch(ArrayList<Genome> theList){
  for(Genome ge : theList) {
   if(ge.fitness>=bestOne.fitness)
    bestOne= ge;
  }
  
  //population= theList;
  
  ArrayList<Genome> newList= new ArrayList<Genome>();
  
  while(newList.size()<popSize) {
   Genome mom= getChromoRoulette();
   Genome dad= getChromoRoulette();
   
   Genome baby1= new Genome(mom.fitness, mom.weights);
   Genome baby2= new Genome(dad.fitness, dad.weights);
   
   crossover(baby1,baby2,mom,dad);
   
   mutate(baby1);
   mutate(baby2);
   
   newList.add(new Genome(0, baby1.weights));
   newList.add(new Genome(0, baby2.weights));
  }
  
  population= newList;
  
  return population;
 }
 
 public ArrayList<Genome> getChromos(){
  return population;
 }
 public double getBestScore() {
  return bestOne.fitness;
 }
}