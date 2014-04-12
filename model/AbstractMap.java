package model;

import java.util.ArrayList;

public abstract class AbstractMap extends AbstractNetwork {
	protected double [][] distancesTopo;
	protected int colNumber,rowNumber;

	public AbstractMap(Environment h) {
		super(h);
		
		colNumber = Options.getOptions().getNbColumns();
		rowNumber = Options.getOptions().getNbRows();
		
		// Memorisation des distances topologiques
		distancesTopo = new double [colNumber*rowNumber][colNumber*rowNumber];	
	}
    
    public abstract double distance_voisins(Neuron currentBest,Neuron n);
    public abstract boolean getAleatory();
    
    @Override
	public void fill() {
    	nb_neurons = rowNumber * colNumber;
	
		for(int k=0;k<rowNumber;k++) {
    		neurons.add(new ArrayList<Neuron>());
    		for(int k1=0;k1<colNumber;k1++){
    			neurons.get(k).add(new Neuron(2, k, neurons.get(k).size(), getAleatory()));
    		}
    	}
	
		// We create the topology
		setVoisins();
	
		for (int i=0;i<neurons.size();i++) {
		    for (int j=0 ;j<neurons.get(i).size();j++) {
				for(int k = 0 ; k < neurons.size() ; k ++) {
				    for(int l = 0 ; l <neurons.get(k).size(); l++) {
				    	distancesTopo[i*colNumber +j][k*colNumber +l] = distance_voisins(neurons.get(i).get(j), neurons.get(k).get(l));
				    }
				}
		    }
		}
	}
	
	public void setVoisins () {
		// Attribut a chaque Neuron du reseau ses voisins directs
		ArrayList<Neuron> voisins;
	
		for (int col = 0 ; col < colNumber ; col ++) {
		    for (int row = 0 ; row < rowNumber ; row ++) {
				voisins = new ArrayList<Neuron>();
				
				if(col < colNumber -1){
				    voisins.add(neurons.get(row).get(col + 1));
				}
				if(col > 0){
				    voisins.add(neurons.get(row).get(col - 1));
				}
				
				if(row == 0 && neurons.size() > 1){
				    voisins.add(neurons.get(row + 1).get(col));
				}else if(row == neurons.size() -1 && neurons.size()!= 1){
				    voisins.add(neurons.get(row - 1).get(col));
				}else if(neurons.size()!= 1){
				    voisins.add(neurons.get(row + 1).get(col));
				    voisins.add(neurons.get(row - 1).get(col));
				}
				
				neurons.get(row).get(col).setNeighbors(voisins);		
		    }
		}		
    }
}
