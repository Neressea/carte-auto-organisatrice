package model;

/**
 * The class that holds all options of the software. 
 * It's a singleton to access it everywhere and to prevent multiple instances. 
 * @author valbert
 *
 */

public class Options {
	
	//The instance
	private static Options OPTIONS = new Options();
	
	private int nb_data; //number of data
    private int rows, cols;
    private int nb_neuronsDep;
    private int nb_epochs;
    private int multidata;
    private int max_age;
    private int max_neurons;
    private int time_new;

	//Volatile for threads
    private volatile boolean stopped;
    private volatile boolean paused;
    private volatile boolean stepped;
    private volatile boolean const_learning;
    private volatile boolean const_neighborhood;
    private volatile double learning_som;
    private volatile double neighborhood_som;
    private volatile double elasticity; //We can change value of elasticity during a thread
    private volatile double epsilon;
    private volatile double neighborhood_gng;
    private volatile double winner_gng;
    
    //A static method to access the singleton
    public static Options getOptions(){
    	return OPTIONS;
    }
    
    private Options(){
    	nb_data = 100000;
    	elasticity = 2.5;
    	epsilon = 0.5;
    	rows = 10;
    	cols = 10;
    	nb_neuronsDep = 2;
    	nb_epochs = 10000;
    	const_neighborhood = false;
    	const_learning = false;
    	learning_som = 0.1;
    	neighborhood_som = 0.1;
    	stopped = true;
    	paused = false;
    	stepped = false;
    	multidata = 10;
    	max_age = 40;
        max_neurons = 100;
        time_new = 300;
        neighborhood_gng = 0.00006;
        winner_gng = 0.05;
    }
    
    public double getWinnerDistGng(){
    	return winner_gng;
    }
    
    public void setWinnerDistGng(double dist){
    	winner_gng = dist;
    }
    
    public int getMaxAge() {
		return max_age;
	}

	public void setMaxAge(int max_age) {
		this.max_age = max_age;
	}

	public int getMaxNeurons() {
		return max_neurons;
	}

	public void setMaxNeurons(int max_neurons) {
		this.max_neurons = max_neurons;
	}

	public int getTimeNew() {
		return time_new ;
	}

	public void setTimeNew(int time_nouveau) {
		this.time_new = time_nouveau;
	}
    
    public void setMultidata(int md){
    	multidata = md;
    }
    
    public int getMultidata(){
    	return multidata;
    }
    
    public double getEpsilon(){
    	return epsilon;
    }
    
    public void setEpsilon(double e){
    	epsilon = e;
    }
    
    public boolean getStepped(){
    	return stepped;
    }
    
    public void setStep(boolean s){
    	stepped = s;
    }

    public int getNbData(){
    	return nb_data;
    }
    
    public void setNbData(int nb){
    	nb_data = nb;
    }
    
    public boolean getPaused(){
    	return paused;
    }
    
    public void setPause(boolean p){
    	paused = p;
    	synchronized (this) {
    		notify();
		}
    }
    
    public boolean getLearningCst(){
    	return const_learning;
    }
    
    public void setLearningCst(boolean b){
    	const_learning = b;
    }
    
    public boolean getNeighborhoodCst(){
    	return const_neighborhood;
    }
    
    public void setNeighborhoodCst(boolean c_n){
    	const_neighborhood = c_n;
    }
    
    public int getNbEpochs(){
    	return nb_epochs;
    }
    
    public void setNbEpochs(int nb){
    	nb_epochs = nb;
    }
    
    public int getNbDep(){
    	return nb_neuronsDep;
    }
    
    public void setNbDep(int nb_neur){
    	nb_neuronsDep = nb_neur;
    }

    public boolean getStopped(){
    	return stopped;
    }
    
    public void setStopped(boolean b){
    	stopped = b;
    }
    
    public void setNbColumns(int nb){
    	cols = nb;
    }
    
    public void setNbRows(int nb){
    	rows = nb;
    }
    
    public int getNbColumns(){
    	return cols;
    }
    
    public int getNbRows(){
    	return rows;
    }
    
    public double getElasticity(){
    	return this.elasticity;
    }
    
    public void setElasticity(double e){
    	elasticity = e;
    }

	public double getNeighborhoodGng() {
		return neighborhood_gng;
	}

	public void setNeighborhoodGng(double neighborhood_gng) {
		this.neighborhood_gng = neighborhood_gng;
	}

	public double getNeighborhoodSom() {
		return neighborhood_som;
	}

	public void setNeighborhoodSom(double neighborhood_som) {
		this.neighborhood_som = neighborhood_som;
	}

	public double getLearningSom() {
		return learning_som;
	}

	public void setLearningSom(double learing_som) {
		this.learning_som = learing_som;
	}
}
