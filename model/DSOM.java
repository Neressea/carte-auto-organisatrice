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
    public void run(){
    	int          exemples;
		int    NumCurrentBest;
		double  distance_best;
		double       distance;
		double  distance_topo;
		double         neighb;
		Neuron  current_best;
		int             index;
		int i, k, l;
		
		nb_neurons = colNumber*rowNumber;
		exemples = 0;
		
		while (exemples < epochs && !Options.getOptions().getStopped()) {
			
			//On vérifie si on est en pause
			synchronized (holder) {
				while(Options.getOptions().getPaused() && !Options.getOptions().getStopped()){
					try {
						holder.wait();
						
						if(Options.getOptions().getStepped()){
							
							//If we "step", we go out for one round
							Options.getOptions().setStep(false);
							break;
						}
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
		    System.out.printf(" DSom \t %5d sur %5d \r", exemples, epochs); 
		    exemples ++;
	
		    // Tirage de l'exemple courant
		    index = (int) (Math.random()*Data.getData().size());
		    
		    // Recherche du Neuron le plus proche
		    current_best = neurons.get(0).get(0);
		    distance_best = current_best.distance(Data.getData().get(index));
		    
		    for(i=0;i<neurons.size();i++) {
				for(k=0;k<neurons.get(i).size();k++) {
				    distance = neurons.get(i).get(k).distance(Data.getData().get(index));
				    if(distance < distance_best) {
				    	distance_best = distance;
				    	current_best = neurons.get(i).get(k);
				    }
				}
		    }
		     
		    NumCurrentBest = current_best.getRow() * colNumber + current_best.getCol();
		    double db = Math.pow(distance_best, 2);
		    
		    // Mise a jour des poids de tous les neurons
		    for(i = 0 ; i < neurons.size() ; i++) {
				for(k=0;k<neurons.get(i).size();k++) {
				    Neuron cible  =                      neurons.get(i).get(k);
				    int Numcible   =  cible.getRow() * colNumber + cible.getCol();
				    distance_topo  =     distancesTopo[Numcible][NumCurrentBest]/(colNumber + rowNumber);
				    
				    neighb = Math.exp(-1* Math.pow(distance_topo, 2)/Math.pow(Options.getOptions().getElasticity(), 2)/db);
				    System.out.println("Dist topo : "+distance_topo);
				    System.out.println("DB Carré : "+db );
				    // Mise a jour des poids
				    double wl, dl; 		    
				    double delta;
				    
				    for(l = 0; l < cible.getPoids().size() ; l++) {
    					wl = cible.getPoids().get(l);
    					dl = Data.getData().get(index).getPoids().get(l);
    					delta = Math.abs(wl - dl) * neighb * (dl - wl);
    					System.out.print("\t w"+l+"  " + wl + "("+ delta +")" + " d"+l+"  " + dl);
    					wl += Options.getOptions().getEpsilon()*delta;
    					cible.getPoids().set(l, new Float(wl));		
				    }
				}
			  
		    }	
		    
		    //We wait a little because it's too fast in other cases
		    try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    holder.change();
		}
		
		Options.getOptions().setStopped(true);
		holder.change();
    }
}