package model;

public class DSOM extends AbstractMap {

    public DSOM (Environment h) {
		super(h);
    }
    
    @Override
    public String toString(){
    	return "DSOM";
    }
    
    @Override
    public double distance_voisins(Neuron currentBest,Neuron n) {
    	return Math.abs(currentBest.getCol()-n.getCol())+Math.abs(currentBest.getRow()-n.getRow());
    }
    
    @Override
    public boolean getAleatory(){
    	return false;
    }
    
    @Override
    public String printState(Object[] to_print){
    	String s="";
    	
    	s+="DSOM : "+to_print[0] + " epochs on "+to_print[1]+" ";
    	s+="Elasticity="+to_print[2]+" ";
    	s+="Epsilon="+to_print[3]+" ";
    	
    	
    	System.out.println(s);
    	
    	return s;
    }
    
    @Override
    public void learn(){
		
		nb_neurons = colNumber*rowNumber;
		int examples = 0;
		
		while (examples < epochs && !Options.getOptions().getStopped()) {
			
			//We verify the pause
			checkPause();
			
			//We print the state
			printState(new Object[]{examples, epochs, Options.getOptions().getElasticity(), Options.getOptions().getEpsilon()});
			 
			examples++;
	
		    //We pick up a piece of data
		    int index_data = (int) (Math.random()*Data.getData().size());
		    
		    //We get the neuron which is the nearest to this data
		    Neuron current_best = getNearest(index_data);
		    
		    //We compute the distance between this neuron and the data
		    double distance_best = current_best.distance(Data.getData().get(index_data));
		    
		    //And we get the number of the column in the topological distances' array
		    int num_best = current_best.getRow() * colNumber + current_best.getCol();
		    
		    //We update the weights of all neurons
		    for(int i = 0 ; i < neurons.size() ; i++) {
				for(int k=0;k<neurons.get(i).size();k++) {
					
					//We get the current neuron...
				    Neuron target  = neurons.get(i).get(k);
				    
				    //... and we get its topological distance to the nearest
				    int num_target_topo =  target.getRow() * colNumber + target.getCol();
				    
				    //We compute the topological distance between the two neurons
				    double distance_topo = distancesTopo[num_target_topo][num_best]/(colNumber + rowNumber);
				    
				    //We compute the neighborhood
				    double neighb = Math.exp(-1* Math.pow(distance_topo, 2)/Math.pow(Options.getOptions().getElasticity(), 2)/Math.pow(distance_best, 2));
				    
				    //We update the weight of the target    				    
				    for(int l = 0; l < target.getPoids().size() ; l++) {
				    	
				    	//We get the current weight
    					double current_weight = target.getPoids().get(l);
    					
    					//We get the weight of the data
    					double data_weight = Data.getData().get(index_data).getPoids().get(l);
    					
    					//We compute the variations 
    					double delta = Math.abs(current_weight - data_weight) * neighb * (data_weight - current_weight);
    					
    					//We change the value of the weight
    					current_weight += Options.getOptions().getEpsilon()*delta;
    					
    					//We set the new weight
    					target.getPoids().set(l, new Float(current_weight));
    					
				    }
				}
			  
		    }	
		    
		    //We wait a little because it's too fast in other cases
		    sleep();
		    
		    world.change();
		}
		
		world.change();
    }
}