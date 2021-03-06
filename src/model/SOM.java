package model;

public class SOM extends AbstractMap {

    double sigma_i =  .25;//.20;//.3;
    double sigma_f =  .03;

    Boolean decrease_lrate = true;
    Boolean decrease_sigma = true;
    
    double lrate_i =   0.5;
    double lrate_f = 0.005;
    
    private int totalEpochs;

    public SOM (Environment h) {
		super(h);
		this.totalEpochs = 1;
    }
    
    @Override
    public String toString(){
    	return "SOM";
    }
    
    @Override
    public boolean getAleatory(){
    	return true;
    }

    @Override
	public void learn() {
    	
		double lrate, sigma;
		
		nb_neurons = colNumber*rowNumber;
	
		if (decrease_lrate) 
		    lrate = lrate_i;
		else 
		    lrate = (lrate_f + lrate_i)/4;
		
		if (decrease_sigma) 
		    sigma = sigma_i;
		else
		    sigma = (sigma_f + sigma_i)/3;
		
		int exemples = 0;
		while (exemples < epochs  && !Options.getOptions().getStopped()) {	
			
			//We verify the pause
			checkPause();
			
			//We print the state 
			printState(new Object[]{exemples, epochs, lrate, lrate_i, lrate_f, sigma, sigma_i, sigma_f});
			
		    //We update the learning rate
		    double t = ((Options.getOptions().getContinuous() * epochs)+exemples) / (float)(totalEpochs * epochs);

		    if (decrease_lrate)
		    	lrate = lrate_i*Math.pow((lrate_f/lrate_i), Math.pow(t, 1.2));
		    if (decrease_sigma)
		    	sigma = sigma_i*Math.pow((sigma_f/sigma_i), Math.pow(t, 1.2));
		    
		    //We pick up a piece of data
		    int index_data = (int) (Math.random()*Data.getData().size());
		    
		    //We search for the nearest neuron
		    Neuron current_best = getNearest(index_data);
		    
		    int num_nearest_topo = current_best.getRow() * colNumber + current_best.getCol();
		    
		    //We update all weights 
		    for(int i = 0 ; i < neurons.size() ; i++){
				for(int k = 0 ; k < neurons.get(i).size() ; k++){

				    Neuron cible = neurons.get(i).get(k);
				    int Numcible =  cible.getRow() * colNumber + cible.getCol();
				    
				    //We verify if learning rate and neighborhood are fixed or not
				    double learning_rate = 0;
				    double neighborhood = 0;
				    
				    if(Options.getOptions().getNeighborhoodCst()){
				    	neighborhood = Options.getOptions().getNeighborhoodSom();
				    }else{
				    	double distance_w = distancesTopo[Numcible][num_nearest_topo];
    				    neighborhood = Math.exp(-1.0*distance_w/sigma);				   
				    }
				    
				    if(Options.getOptions().getLearningCst()){
				    	learning_rate = Options.getOptions().getLearningSom();
				    }else{
				    	learning_rate = lrate;
				    }
				    
				    //Update
				    cible.setPoids(learning_rate, neighborhood, Data.getData().get(index_data));
				}
		    }	
		    
		    //We wait a little because it's too fast in other cases
		    sleep();
		    
		    world.change();
		    exemples++;
		}
		
		world.change();
	}
    
    @Override
    public String printState(Object[] to_print){
    	String s="";
    	
    	s+="SOM : "+to_print[0] + " epochs on "+to_print[1]+" ";
    	s+="learning rate="+to_print[2]+" (lower="+to_print[3]+"/top="+to_print[4]+") ";
    	s+="sigma="+to_print[5]+" (lower="+to_print[6]+"/top="+to_print[7]+") ";
    	
    	System.out.println(s);
    	
    	return s;
    }

    @Override
    //distance topologique normalisee entre deux neurons (voisinage)
    public double distance_voisins(Neuron current_best, Neuron n2) {
		double dCol;
		double dRow;
		
		dCol = Math.pow(((double) current_best.getCol() - (double) n2.getCol())/colNumber,2);
		dRow = Math.pow(((double) current_best.getRow() - (double) n2.getRow())/rowNumber,2);
	
		return Math.sqrt(dCol + dRow);
    }

    public void setContinu(int continu, int totalEpochs) {
    	Options.getOptions().setContinuous(continu);
		this.totalEpochs = totalEpochs;
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
}