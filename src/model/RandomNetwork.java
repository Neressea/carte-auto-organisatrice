package model;

import java.util.ArrayList;

public class RandomNetwork extends SpecialNetwork{

	public RandomNetwork(BasicNetwork n) {
		super(n);
	}

	@Override
	public void learn() {

		world.setData(Data.SHAPES[6]);	
		
		network.learn();

		int i=0;
		while (i < 100 && !Options.getOptions().getStopped()) {
		    // recuperer un certain  nombre de donnees
		    int nbNew = (int)(Math.random() * world.getData().size());
		    double lx = Math.random();
		    double ly = Math.random();
		    
		    System.out.println("  lx = " + lx + "  ly = " + ly);
		    for (int j = 0 ; j < nbNew ; j++) {
				int index = (int)(Math.random() * world.getData().size());
				DataPoint data = world.getData().get(index);
				double nbdim = Math.random();
				double x,y;
				if (nbdim < .333) {				    
				    // modification de la premiere dimension
				    x = Math.random() * lx;
				    y = (double) data.getPoids().get(1);
				} else if (nbdim < .666) {
				    // modification de la seconde dimension
				    x = (double) data.getPoids().get(1);
				    y = Math.random() * ly;
	
				} else {
				    // modification des deux dimensions
				    x = Math.random() * lx;
				    y = Math.random() * ly;
				}
				
				ArrayList<Float> poids = new ArrayList<Float>();
				poids.add(new Float(x));
				poids.add(new Float(y));
				
				world.getData().set(index,new DataPoint(poids));				    				    				
		    }
		    
		    i++;
		    network.learn();
		}

	}
	
}
