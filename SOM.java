import java.util.ArrayList;

public class SOM extends NeuralNetwork {
    private ArrayList<ArrayList<Neurone>> neurones;
    private ArrayList<Data> data;
    private Visuel visuel;
    int colNumber,rowNumber;

    double sigma_i =  .25;//.20;//.3;
    double sigma_f =  .03;

    Boolean decrease_lrate = true;
    Boolean decrease_sigma = true;
    
    double lrate_i =   0.5;
    double lrate_f = 0.005;
    
    double [][] distancesTopo;
    int refresh = 30;
    private int continu; // permet les donnees dynamiques
    private int totalEpochs;

    public SOM (Visuel nw) {
		super();
		this.visuel     = nw;
		this.neurones   = nw.neurones;
		this.data       = nw.data;
		this.epochs     = visuel.getEpochs();
		colNumber = nw.getNbColumns();
		rowNumber = nw.getNbRows();
		this.nbNeurones = colNumber*rowNumber;
		
		this.continu     = 0;
		this.totalEpochs = 1;
		
		// creation des neurones
		neurones.removeAll(neurones);
	    	for(int k=0;k<rowNumber;k++) {
	    		neurones.add(new ArrayList<Neurone>());
	    		for(int k1=0;k1<colNumber;k1++){
	    			neurones.get(k).add(new Neurone(2, k, neurones.get(k).size(), true));
	    			//neurones.get(k).add(new Neurone(2, k, neurones.get(k).size(), false));
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
				    	distancesTopo[i*colNumber +j][k*colNumber +l] = distanceNorm(neurones.get(i).get(j), neurones.get(k).get(l));
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
    		@Override
    		public void run(){
    			int exemples;
    			int index;
    			double t;
    			double lrate, sigma;
    		
    			int i, k;
    			Neurone current_best;
    			double distance_best;
    			double distance;
    			double distance_v;
    			double distance_w;
    		
    			if (decrease_lrate) 
    			    lrate = lrate_i;
    			else 
    			    lrate = (lrate_f + lrate_i)/4;
    			if (decrease_sigma) 
    			    sigma = sigma_i;
    			else
    			    sigma = (sigma_f + sigma_i)/3;
    			
    			for (exemples = 0; exemples < epochs; exemples ++) {
    				
    				//Si on arrête l'algo, on sort de la boucle
    				if(!visuel.getGoOn()) break;
    				
    				synchronized (visuel) {
    					while(visuel.getPause()){
    						try {
    							visuel.wait();
    						} catch (InterruptedException e1) {

    							e1.printStackTrace();
    						}
        				}
					}
    				
    				
    			    // Mise a jour des voisinages et tx apprentissage
    			    t = ((continu * epochs)+exemples) / (float)(totalEpochs * epochs);
    		            //lrate = lrate_i*Math.pow((lrate_f/lrate_i), t);
    		            //sigma = sigma_i*Math.pow((sigma_f/sigma_i), t);
    			    if (decrease_lrate)
    			    	lrate = lrate_i*Math.pow((lrate_f/lrate_i), Math.pow(t, 1.2));
    			    if (decrease_sigma)
    			    	sigma = sigma_i*Math.pow((sigma_f/sigma_i), Math.pow(t, 1.2));
    			    
    			    System.out.printf(" SOM    %5d sur %5d   lrate : %1.3f [%1.2f:%1.3f]   sigma: %1.3f [%1.1f:%1.2f]\r", 
    					      exemples, epochs, lrate, lrate_i, lrate_f, sigma, sigma_i, sigma_f); 
    			    //System.out.println("decrease_lrate = " + decrease_lrate + "   decrease_sigma = " +decrease_sigma);
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
    			    
    			    int NumCurrentBest = current_best.getRow() *rowNumber + current_best.getCol();
    			    
    			    for(i = 0 ; i < neurones.size() ; i++){
	    				for(k = 0 ; k < neurones.get(i).size() ; k++){
	    				    // Distance au neurone vainqueur
	    				    // distance_w = distance(neurones.get(i).get(k), current_best);
	    				    Neurone cible = neurones.get(i).get(k);
	    				    int Numcible =  cible.getRow() *rowNumber + cible.getCol();
	    				    
	    				    //On distingue deux cas : si le voisinage est constant ou non
	    				    double e = 0;
	    				    double h = 0;
	    				    
	    				    if(visuel.getVoisinageCst()){
	    				    	h = visuel.getVoisinage();
	    				    }else{
	    				    	distance_w = distancesTopo[Numcible][NumCurrentBest];
	    				    	
		    				    // Influence du voisinage
		    				    distance_v = Math.exp(-1.0*distance_w/sigma);
		    				    h = distance_v;				   
	    				    }
	    				    
	    				    if(visuel.getApprentissageCst()){
	    				    	e = visuel.getApprentissage();
	    				    }else{
	    				    	e = lrate;
	    				    }
	    				    
	    				    // cible.setPoids(lrate, distance_v, data.get(index));
	    				    
	    				    // Mise a jour des poids du neurone
	    				    // TODO : lrate -> tx apprentissage, distance_v -> voisinage
	    				    System.out.println("APPRENTISSAGE !!!!!!!!!! : "+e);
	    				    System.out.println("VOISINAGE !!!!!!!!!! : "+h);
	    				    cible.setPoids(e, h, data.get(index));
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

    /*	private double h(int iter, Neurone neurone, Neurone currentBest) {
      return Math.exp(-1.0*distance(neurone,currentBest)/sigma_f(iter));
      }
  
      
      private double sigma_f(int iter) {
      return 2*Math.pow((sigma_init*Math.pow((sigma_final /sigma_init), ((double)iter/(double)max_iter))),2);
	}
	
	private double epsilon_f(int iter){
	return e_init*Math.pow((e_final /e_init), ((double)iter/(double)max_iter));
	}
    */
    //distance topologique normalisee entre deux neurones (voisinage)
    private double distanceNorm(Neurone n1, Neurone n2) {
	double dCol;
	double dRow;
	
	dCol = Math.pow(((double) n1.getCol() - (double) n2.getCol())/colNumber,2);
	dRow = Math.pow(((double) n1.getRow() - (double) n2.getRow())/rowNumber,2);

	return Math.sqrt(dCol + dRow);	
    }
    
    //distance entre deux neurones (voisinage)
    private double distance(Neurone n1, Neurone currentBest) {
	return Math.pow(distance_voisins(currentBest,n1),2);
    }
    
    public double distance_voisins(Neurone currentBest,Neurone n) {
	return Math.abs(currentBest.getCol()-n.getCol())+Math.abs(currentBest.getRow()-n.getRow());
    }
    

    public void setContinu(int continu, int totalEpochs) {
	this.continu     =     continu;
	this.totalEpochs = totalEpochs;
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




    /*public ArrayList<Neurone> getConnectedNeurons(Neurone neurone) {    	
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
    
    
    public void setDecrease_lrate (Boolean decrease_lrate) {
	this.decrease_lrate = decrease_lrate;
    }

    public Boolean getDecrease_lrate () {
	return this.decrease_lrate;
    }
	
    public void setDecrease_sigma (Boolean decrease_sigma) {
	this.decrease_sigma = decrease_sigma;
    }

    public Boolean getDecrease_sigma () {
	return this.decrease_sigma;
    }
  
    public void onLineSom() {
    	this.setDecrease_sigma(false);
    	this.setDecrease_lrate(false);
    }
    
    public void getEpochs(int epochs) {
    	this.epochs= epochs;
    }
}