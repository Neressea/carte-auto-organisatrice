package model;

/**
 * A Factory to create algos automatically
 * @author valbert
 */

public class NetworkFactory {
	private Environment world;
	
	public NetworkFactory(Environment w){
		world = w;
	}
	
	public AbstractNetwork getNetwork(String s_network){
		AbstractNetwork network;

		switch(s_network){
			case "Self Organizing Map":
				network = new SOM(world);
			break;
			
			case "Dynamic Self Organizing Map":
				network = new DSOM(world);
			break;
			
			case "Growing Neural Gas":
				network = new GNG(world);
			break;
			
			case "Multiple Self Organizing Map":
				network = new MultipleNetwork(new SOM(world));
			break;
			
			case "Multiple Growing Neural Gas":
				network = new MultipleNetwork(new GNG(world));
			break;
			
			case "Multiple Dynamic Self Organizing Map":
				network = new MultipleNetwork(new DSOM(world));
			break;
			
			case "Random Self Organizing Map":
				network = new RandomNetwork(new SOM(world));
			break;
				
			case "Random Dynamic Self Organizing Map":
				network = new RandomNetwork(new DSOM(world));
			break;
			
			case "Random Growing Neural Gas":
				network = new RandomNetwork(new GNG(world));
			break;

			default:
				network = new DSOM(world);
		}
		
		return network;
	}
	
	public AbstractNetwork getNetwork(AbstractNetwork n){
		AbstractNetwork network;

		if(n instanceof SOM)
			network = new SOM(world);
		else if(n instanceof DSOM)
			network = new DSOM(world);
		else if(n instanceof GNG)
			network = new GNG(world);
		else if(n instanceof MultipleNetwork)
			network = new MultipleNetwork(((MultipleNetwork) n).network);
		else if(n instanceof RandomNetwork)
			network = new RandomNetwork(((RandomNetwork) n).network);
		else
			network = new DSOM(world);
		
		return network;
	}
}
