package model;

import java.util.ArrayList;

public class Neuron {
    private ArrayList<Float>     poids;	// ici x,y
    private ArrayList<Neuron> neighbors; // voisins directs
    private int row,col;
    

    public Neuron(int cardinal, int r, int c, boolean aleatoire){
		poids   = new ArrayList<Float>();
		row = r;
		col = c;
		for (int i=0;i<cardinal;i++){
		    if (aleatoire) {
		    	poids.add(new Float(Math.random()));
		    } else {
		    	poids.add(new Float(.5));
		    }
		}
    }
    
    public ArrayList<Neuron> getNeighbors(){
    	return neighbors;
    }
    
    public void setNeighbors(ArrayList<Neuron> n){
    	neighbors = n;
    }

    public int getRow() {
    	return this.row;
    }
    
    public void setRow(int row) {
    	this.row = row;
    }

    public int getCol() {
    	return col;
    }
    
    public void setCol(int col) {
    	this.col = col;
    }

    public ArrayList<Float> getPoids() {
    	return poids;
    }

    public void setPoids(ArrayList<Float> poids) {
    	this.poids = poids;
    }

    public void setPoids(double e, double h, DataPoint data) {
		double wi;
		for(int i=0;i<poids.size();i++){
		    wi = poids.get(i);
		    
		    //e-> apprentissage, h->voisinage
		    wi = wi + e*h*(data.getPoids().get(i) - wi);
		    poids.set(i, new Float(wi));
		}
    }
  
    public void setPoids(double taux, DataPoint data) {
    	setPoids(taux, 1, data);
    }

    public double distance(DataPoint data) {
		//distance de manatthan
		double res = 0.0;
		for(int i=0;i<poids.size();i++){
		    res += Math.abs((poids.get(i) - data.getPoids().get(i)));
		}
		return res;
    }
   
	@Override
	public boolean equals(Object o){
		Neuron n = (Neuron) o;		
		return row == n.row && col == n.col;
	}
	
}
