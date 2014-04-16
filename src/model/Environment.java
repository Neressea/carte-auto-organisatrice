package model;

import java.util.Observable;

public class Environment extends Observable{
	
	private AbstractNetwork network;
	private Data data;
	
	public Environment(){
		
		//We create the object that holds all options
		data = Data.getData();
		
		//We create a void network
		network = new SOM(this);
		network.clear();
	}
	
	public void clear(){
    	data.clear();
    	network.clear();
    	
    	setChanged();
    	notifyObservers();
    }
	
	public void setData(char c){
    	data.setData(c);
    	
    	setChanged();
    	notifyObservers();
    }
	
    public Data getData() {
    	return data;
    }
    
    public AbstractNetwork getNet(){
    	return network;
    }
    
    public void setNet(AbstractNetwork net){
    	network = net;
    	
    	setChanged();
    	notifyObservers();
    }
    
    public void change(){
    	setChanged();
    	notifyObservers();
    }
}
