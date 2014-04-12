package model;

import java.util.ArrayList;

/**
 * A super-class that represents each network in the project
 * @author valbert
 *
 */

public abstract class AbstractNetwork extends Thread{
	
	protected Environment holder;
	protected ArrayList<ArrayList<Neuron>> neurons;
	protected int nb_neurons;
    protected int epochs;
    protected int refresh;
    
    public AbstractNetwork(Environment h){
    	super();
    	
    	holder = h;
    	nb_neurons = 0;
    	refresh = Options.getOptions().getRefresh();
    	epochs = Options.getOptions().getNbEpochs();
    	neurons = new ArrayList<ArrayList<Neuron>>();
    }
    
    @Override
    public abstract String toString();
    
    @Override
    public abstract void run();
    
    /**
     * A method that fills the network with neurons (because it's empty at the beginning)
     */
    public abstract void fill();

    public ArrayList<ArrayList<Neuron>> getNeurons(){
    	return neurons;
    }
    
    public void learn(){
		//We verify that there is no other process occuring
    	if(Options.getOptions().getStopped()){ 	
    		Options.getOptions().setStopped(false);
    		start();
    	}
	}
    
    public int size(){
    	return nb_neurons;
    }
    
    public void clear(){
    	neurons.clear();
    	nb_neurons = 0;
    }
    
}