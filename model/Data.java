package model;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * A class that represents all the map of data
 * It's a singleton to access it everywhere and to prevent multiple instances. 
 * @author valbert
 *
 */

public class Data {
	private static Data DATA = new Data();
	private ArrayList<DataPoint> data;
	private char shape;
	
	private Data(){
		
		//We create a fake data object which is empty
		data = generateData('C');
		data.clear();
		shape = 'C';
	}
	
	public static Data getData(){
		return DATA;
	}
	
	public int size(){
		return data.size();
	}
	
	public DataPoint get(int i){
		return data.get(i);
	}
	
	public void setData(char c){
		data = Data.generateData(c);
		shape = c;
	}
	
	public void resetData(){
		data = Data.generateData(shape);
	}
	
	public void clear(){
		data.clear();
	}
	
	/**
	 * Generates datas
	 * @param c The shape of datas
	 * @param data.size() The number of datas
	 * @return An arrayList containing datas
	 */
	
	public static ArrayList<DataPoint> generateData(char c){
		ArrayList<DataPoint> data = new ArrayList<DataPoint>();
		
		switch(c){
		case 'S': //Square
		    for (int nb = 0; nb < Options.getOptions().getNbData() ; nb ++) {
				double x = Math.random();
				double y = Math.random();
				ArrayList<Float> poids = new ArrayList<Float>();
				poids.add(new Float(x));
				poids.add(new Float(y));
				data.add(new DataPoint(poids));
		    }
		    break;

		case 'M': //Moon	
		    int nb = 0;
		    Ellipse2D.Double n = new Ellipse2D.Double(0,0, 1, 1);
		    Ellipse2D.Double n2 = new Ellipse2D.Double(0,0, .6, .6);
		    while(nb<Options.getOptions().getNbData()){
				double x = Math.random();
				double y = Math.random();
				Point2D.Double p = new Point2D.Double(x,y);
				if(n.contains(p) && !n2.contains(p)){
				    ArrayList<Float> poids = new ArrayList<Float>();
				    poids.add(new Float(x));
				    poids.add(new Float(y));
				    data.add(new DataPoint(poids));
				    nb++;
				}
		    }
		    break;
		case 'R': // Ring
		    int nb2 = 0;
		    Ellipse2D.Double n1 = new Ellipse2D.Double(   0,  0, 1, 1);
		    Ellipse2D.Double n22 = new Ellipse2D.Double(.2,.2, .6, .6);
		    while(nb2<Options.getOptions().getNbData()){
				double x = Math.random();
				double y = Math.random();
				Point2D.Double p = new Point2D.Double(x,y);
				if(n1.contains(p) && !n22.contains(p)){
				    ArrayList<Float> poids = new ArrayList<Float>();
				    poids.add(new Float(x));
				    poids.add(new Float(y));
				    data.add(new DataPoint(poids));
				    nb2++;
				}
		    }
		    break;			
		case 'C': //Circle
		    int nb3 = 0;
		    Ellipse2D.Double n3 = new Ellipse2D.Double(0,0, 1, 1);
		    while(nb3<Options.getOptions().getNbData()){
				double x = Math.random();
				double y = Math.random();
				Point2D.Double p = new Point2D.Double(x,y);
				if(n3.contains(p)){
				    ArrayList<Float> poids = new ArrayList<Float>();
				    poids.add(new Float(x));
				    poids.add(new Float(y));
				    data.add(new DataPoint(poids));
				    nb3++;
				}
		    }
		    break;
		case '2': // Two densities
		    int nb4;
		    for (nb4 = 0; nb4 < (int) (Options.getOptions().getNbData()/3) ; nb4 ++) {
				double x = Math.random()/3;
				double y = Math.random()/3;
				ArrayList<Float> poids = new ArrayList<Float>();
				poids.add(new Float(x));
				poids.add(new Float(y));
				data.add(new DataPoint(poids));
		    }
		    while (nb4 < Options.getOptions().getNbData()) {
				nb4 ++;
				double x = Math.random();
				double y = Math.random();
				ArrayList<Float> poids = new ArrayList<Float>();
				poids.add(new Float(x));
				poids.add(new Float(y));
				data.add(new DataPoint(poids));
		    }    	    	    
		    break;
		case '3': // Three densities
		    int nb5 = 0;
		    Ellipse2D.Double n5 = new Ellipse2D.Double(.1,.1, .4, .4);
		    while (nb5 < (int)(Options.getOptions().getNbData()/3)) {
				double x = Math.random();
				double y = Math.random();
				Point2D.Double p = new Point2D.Double(x,y);
				if(n5.contains(p)){
				    ArrayList<Float> poids = new ArrayList<Float>();
				    poids.add(new Float(x));
				    poids.add(new Float(y));
				    data.add(new DataPoint(poids));
				    nb5++;
				}
		    }
		    n5 = new Ellipse2D.Double(.7,.4, .2, .2);
		    while (nb5 < (int)(Options.getOptions().getNbData()/2)) {
			double x = Math.random();
			double y = Math.random();
				Point2D.Double p = new Point2D.Double(x,y);
				if(n5.contains(p)){
				    ArrayList<Float> poids = new ArrayList<Float>();
				    poids.add(new Float(x));
				    poids.add(new Float(y));
				    data.add(new DataPoint(poids));
				    nb5++;
				}
		    }
		    while (nb5 < Options.getOptions().getNbData()) {
				nb5 ++;
				double x = Math.random();
				double y = Math.random();
				ArrayList<Float> poids = new ArrayList<Float>();
				poids.add(new Float(x));
				poids.add(new Float(y));
				data.add(new DataPoint(poids));
		    }	    	    	    
		    break;
		
		case 's': // Little square
		    int nb6;
		    for (nb6 = 0; nb6 <Options.getOptions().getNbData() ; nb6 ++) {
				double x = Math.random()/2;
				double y = Math.random()/2;
				ArrayList<Float> poids = new ArrayList<Float>();
				poids.add(new Float(x));
				poids.add(new Float(y));
				data.add(new DataPoint(poids));
		    }	    	    	    
		    break;


		case 'c': // Little circle
		    int nb7= 0;
		    Ellipse2D.Double n7 = new Ellipse2D.Double(.6,.6, .4, .4);
		    while (nb7 < Options.getOptions().getNbData()/2) {
				double x = Math.random();
				double y = Math.random();
				Point2D.Double p = new Point2D.Double(x,y);
				if(n7.contains(p)){
				    ArrayList<Float> poids = new ArrayList<Float>();
				    poids.add(new Float(x));
				    poids.add(new Float(y));
				    data.add(new DataPoint(poids));
				    nb7++;
				}
		    }	    	    	    
		    break;
		    
		case 'D': // Distinct densities
		    int nb8 = 0;
		    Ellipse2D.Double n8 = new Ellipse2D.Double(.5,.1, .45, .45);
		    while (nb8 < (int)(Options.getOptions().getNbData()/3)) {
				double x = Math.random();
				double y = Math.random();
				Point2D.Double p = new Point2D.Double(x,y);
				if(n8.contains(p)){
				    ArrayList<Float> poids = new ArrayList<Float>();
				    poids.add(new Float(x));
				    poids.add(new Float(y));
				    data.add(new DataPoint(poids));
				    nb8++;
				}
		    }
		    n8 = new Ellipse2D.Double(.2,.7, .25, .25);
		    while (nb8 < (int)(Options.getOptions().getNbData())) {
				double x = Math.random();
				double y = Math.random();
				Point2D.Double p = new Point2D.Double(x,y);
				if(n8.contains(p)){
				    ArrayList<Float> poids = new ArrayList<Float>();
				    poids.add(new Float(x));
				    poids.add(new Float(y));
				    data.add(new DataPoint(poids));
				    nb8++;
				}
		    }

		    break;
		}
		
		
		return data;
	}
	
	
}
