package model;

import java.util.ArrayList;

public abstract class BasicNetwork extends AbstractNetwork{
	protected ArrayList<ArrayList<Neuron>> neurons;
	protected int nb_neurons;
    protected int epochs;

	public BasicNetwork(Environment h) {
		super(h);
		nb_neurons = 0;
		neurons = new ArrayList<ArrayList<Neuron>>();
    	epochs = Options.getOptions().getNbEpochs();
	}
	
	public void checkPause(){
		synchronized (world) {
			while(Options.getOptions().getPaused() && !Options.getOptions().getStopped()){
				try {
					world.wait();
					
					if(Options.getOptions().getStepped()){
						
						//If we "step", we go out for one round
						Options.getOptions().setStep(false);
						break;
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public abstract String printState(Object[] to_print);
	
	public void sleep(){
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public int size(){
    	return nb_neurons;
    }
	
	public void clear(){
    	neurons.clear();
    	nb_neurons = 0;
    }
	
	public ArrayList<ArrayList<Neuron>> getNeurons(){
    	return neurons;
    }

}
