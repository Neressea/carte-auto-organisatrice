package model;

import java.util.ArrayList;

/**
 * A class that represents each piece of data
 * @author valbert
 *
 */

public class DataPoint {
	private ArrayList<Float> poids;	//ici x,y
	
	public DataPoint(int cardinal,int echelle){

		poids = new ArrayList<Float>();
		for(int i=0;i<cardinal;i++){
			poids.add(new Float(Math.random()));
		}
	}
	
	public DataPoint(ArrayList<Float> poids2) {

		this.poids=poids2;
	}

	public ArrayList<Float> getPoids() {
		return poids;
	}

	public void setPoids(ArrayList<Float> poids) {
		this.poids = poids;
	}
}
