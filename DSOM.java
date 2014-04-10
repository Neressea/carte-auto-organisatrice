import java.util.ArrayList;

public class DSOM extends NeuralNetwork {
    private ArrayList<ArrayList<Neurone>> neurones;
    private ArrayList<Data> data;
    private Visuel visuel;

    int colNumber,rowNumber;
    double elasticity = 2.5;//2;
    double elasticityCarre = Math.pow(elasticity, 2);

    double [][] distancesTopo;
    int refresh = 30;
    
    

    public DSOM (Visuel nw) {
		super();
		this.visuel     = nw;
		this.neurones   = nw.neurones;
		this.data       = nw.data;
		this.epochs     = visuel.getEpochs();
		colNumber = nw.getNbColumns();
		rowNumber = nw.getNbRows();
		this.nbNeurones = colNumber*rowNumber;
		elasticity = visuel.getElasticity();
		
		neurones.removeAll(neurones);
	    	
		// creation des neurones
		for(int k=0;k<colNumber;k++) {
		    	neurones.add(new ArrayList<Neurone>());
		    for(int k1=0;k1<colNumber;k1++){
		    	neurones.get(k).add(new Neurone(2, k, neurones.get(k).size()));
		    }
		}
	
		// Creation de la topologie
		setVoisins ();
	
		// Memorisation des distances topologiques
		distancesTopo = new double [colNumber*rowNumber][colNumber*rowNumber];	
		
		for (int i=0;i<neurones.size();i++) {
		    for (int j=0 ;j<neurones.get(i).size();j++) {
				for(int k = 0 ; k < neurones.size() ; k ++) {
				    for(int l = 0 ; l <neurones.get(k).size(); l++) {		       
				    	distancesTopo[i*rowNumber +j][k*rowNumber +l] = distance(neurones.get(i).get(j), 
						     neurones.get(k).get(l));
				    }
				}
		    }
		}
    }
	


    public void learn(){
    	//Si un autre algo est déjà en cours, on quitte
    	if(visuel.getGoOn()) return;
    	
    	visuel.setGoOn(true);
    	
    	Thread th = new Thread(){
		
    		public void run(){
	    		int          exemples;
			int    NumCurrentBest;
			double  distance_best;
			double       distance;
			double  distance_topo;
			double distance_poids;
			double         neighb;
			Neurone  current_best;
			int             index;
			int i, k, l;
		
			exemples = 0;
			while (exemples < epochs && visuel.getGoOn()) {
				
				synchronized (visuel) {
					while(visuel.getPause()){
						try {
							visuel.wait();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
    				}
				}
				
			    System.out.printf(" DSom \t %5d sur %5d \r", exemples, epochs); 
			    exemples ++;
		
			    // Tirage de l'exemple courant
			    index = (int) (Math.random()*data.size());
			    
			    // Recherche du neurone le plus proche
			    current_best = neurones.get(0).get(0);
			    distance_best = current_best.distance(data.get(index));
			    
			    for(i=0;i<neurones.size();i++) {
					for(k=0;k<neurones.get(i).size();k++) {
					    distance = neurones.get(i).get(k).distance(data.get(index));
					    if(distance < distance_best) {
					    	distance_best = distance;
					    	current_best = neurones.get(i).get(k);
					    }
					}
			    }
			     
		
			    NumCurrentBest = current_best.getRow() * rowNumber + current_best.getCol();
			    double db = Math.pow(distance_best, 2);
			    
			    
			    // Mise a jour des poids de tous les neurones
			    for(i = 0 ; i < neurones.size() ; i++) {
					for(k=0;k<neurones.get(i).size();k++) {
					    Neurone cible  = neurones.get(i).get(k);
					    int Numcible   = cible.getRow() *rowNumber + cible.getCol();
					    distance_topo  = distancesTopo[Numcible][NumCurrentBest]/20;
					    //distance_topo  = 1.0;
					    neighb = Math.exp(-1* Math.pow(distance_topo, 2)/elasticityCarre/db);
					    // Mise a jour des poids
					    double wl, dl; 		    
					    double delta;
					    //System.out.print(" neurone" + Numcible + "("+ NumCurrentBest +")\t neighb : "+ neighb); 
					    for(l = 0; l < cible.poids.size() ; l++) {
							wl = cible.poids.get(l);
							dl = data.get(index).getPoids().get(l);
							delta = Math.abs(wl - dl) * neighb * (dl - wl);
							//System.out.print("\t w"+l+"  " + wl + "("+ delta +")" + " d"+l+"  " + dl);
							wl = wl + delta;
							cible.poids.set(l, new Float(wl));		  
					    }
					    //System.out.println();
					}
			    }	
			    if ((exemples % refresh) ==0)
				visuel.majGr();	    
			}
			visuel.majGr();	    
			System.out.println();	
	
    		}
    	};
    	
    	th.start();
    }




    //distance entre deux neurones (voisinage)
    private double distance(Neurone n1, Neurone currentBest) {
    	return Math.pow(distance_voisins(currentBest,n1),2);
    }
    
    public double distance_voisins(Neurone currentBest,Neurone n) {
    	return Math.abs(currentBest.getCol()-n.getCol())+Math.abs(currentBest.getRow()-n.getRow());
    }


    public void setVoisins () {
		// Attribut a chaque neurone du reseau ses voisins directs
		int col, row;
		ArrayList<Neurone> voisins;
	
		for (col = 0 ; col < colNumber ; col ++) {
		    for (row = 0 ; row < rowNumber ; row ++) {
			voisins = new ArrayList<Neurone>();
			
			if(col < colNumber -1){
			    voisins.add(neurones.get(row).get(col + 1));
			}
			if(col > 0){
			    voisins.add(neurones.get(row).get(col - 1));
			}
			
			if(row == 0 && neurones.size() > 1){
			    voisins.add(neurones.get(row + 1).get(col));
			}else if(row == neurones.size() -1 && neurones.size()!= 1){
			    voisins.add(neurones.get(row - 1).get(col));
			}else if(neurones.size()!= 1){
			    voisins.add(neurones.get(row + 1).get(col));
			    voisins.add(neurones.get(row - 1).get(col));
			}
			
			neurones.get(row).get(col).voisins = voisins;		
		    }
		}		
    }    



   /* public ArrayList<Neurone> getConnectedNeurons(Neurone neurone) {    	
    	int index_ligne = getIndex(neurone);
    	int index_col = neurones.get(index_ligne).indexOf(neurone);
        ArrayList<Neurone> connectedNeurons = new ArrayList<Neurone>();
        
        
    	if(index_col < neurones.get(index_ligne).size() -1){
    		connectedNeurons.add(neurones.get(index_ligne).get(index_col + 1));
    	}
       	if(index_col > 0){
    		connectedNeurons.add(neurones.get(index_ligne).get(index_col - 1));
    	}
       	
        if(index_ligne == 0 && neurones.size() > 1){
        	connectedNeurons.add(neurones.get(index_ligne + 1).get(index_col));
        }else if(index_ligne == neurones.size() -1 && neurones.size()!=1){
        	connectedNeurons.add(neurones.get(index_ligne - 1).get(index_col));
        }else if(neurones.size()!=1){
        	connectedNeurons.add(neurones.get(index_ligne + 1).get(index_col));
        	connectedNeurons.add(neurones.get(index_ligne - 1).get(index_col));
        }
        
      return connectedNeurons;   
    }*/



	private int getIndex(Neurone neurone) {
	    	int index_ligne = -1;
			for(int i=0;i<neurones.size();i++){
			    if(neurones.get(i).contains(neurone)){
			    	index_ligne = i;
			    }
			}
			return index_ligne;
	    }
    
}