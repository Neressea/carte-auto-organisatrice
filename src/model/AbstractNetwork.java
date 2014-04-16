package model;

import java.util.ArrayList;

/**
 * A super-class that represents each network in the project
 * @author valbert
 *
 */

public abstract class AbstractNetwork extends Thread{
	
	protected Environment world;
    
    public AbstractNetwork(Environment h){
    	super();
    	
    	world = h;
    }
    
    @Override
    public abstract String toString();
    
    public abstract void learn();
    public abstract int size();
    public abstract void clear();
    
    /**
     * A method that fills the network with neurons (because it's empty at the beginning)
     */
    public abstract void fill();

    public abstract ArrayList<ArrayList<Neuron>> getNeurons();
    
    public void run(){
    	//We verify that there is no other process occuring
    	if(Options.getOptions().getStopped()){ 	
    		Options.getOptions().setStopped(false);
    		learn();
    		Options.getOptions().setStopped(true);
    		world.change();
    	}
    }
    
}