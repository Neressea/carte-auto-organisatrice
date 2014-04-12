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
	private double elasticity;
	private double elasticity_min;
	private double elasticity_max;
	private double elasticity_step;
    private int rows, cols;
    private int nb_neuronsDep;
    private int nb_epochs;
    private double voisinage;
    private double learning;
    private boolean voisinage_const;
    private boolean learning_const;
    private int refresh;
    
    //Volatile for threads
    private volatile boolean stopped;
    private volatile boolean paused;
    
    //A static method to access the singleton
    public static Options getOptions(){
    	return OPTIONS;
    }
    
    private Options(){
    	nb_data = 100000;
    	elasticity = 2.5;
    	elasticity_min = 0.05;
    	elasticity_max = 0.5;
    	elasticity_step = 0.1;
    	rows = 10;
    	cols = 10;
    	nb_neuronsDep = 2;
    	nb_epochs = 10000;
    	voisinage = 0.1;
    	learning = 0.1;
    	voisinage_const = false;
    	learning_const = false;
    	stopped = true;
    	paused = false;
    	refresh = 50;
    }
    
    public double getElasticityMin(){
    	return elasticity_min;
    }
    
    public void setElasticityMin(double min){
    	elasticity_min = min;
    }
    
    public double getElasticityMax(){
    	return elasticity_max;
    }
    
    public void setElasticityMax(double max){
    	elasticity_max = max;
    }
    
    public double getElasticityStep(){
    	return elasticity_step;
    }
    
    public void setElasticityStep(double step){
    	elasticity_step = step;
    }
    
    public int getRefresh(){
    	return refresh;
    }
    
    public void setRefresh(int r){
    	refresh = r;
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
    	return learning_const;
    }
    
    public void setLearningCst(boolean b){
    	learning_const = b;
    }
    
    public boolean getNeighborhoodCst(){
    	return voisinage_const;
    }
    
    public void setNeighborhoodCst(boolean v_c){
    	voisinage_const = v_c;
    }
    
    public double getNeighborhood(){
    	return voisinage;
    }
    
    public void setNeighborhood(double v){
    	voisinage = v;
    }
    
    public double getLearning(){
    	return learning;
    }
    
    public void setLearning(double appr){
    	learning = appr;
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
}
