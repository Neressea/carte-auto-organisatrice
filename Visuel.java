import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Observable;

public class Visuel extends Observable {
	
    public ArrayList<ArrayList<Neurone>> neurones;
    public ArrayList<Data> data;
    public NeuralNetwork network;

    int nb_data = 100000; //nombre de données    

    // ------------------------------ OPTIONS -------------------------------------
    private double elasticity = 0;
    private int rows = 5, cols = 5;
    private volatile boolean goOn = false;
    private int nbNeuronesDep = 2;
    private int nb_epochs = 10000;
    private double voisinage = 0.1;
    private double apprentissage = 0.1;
    private boolean voisinage_const = false;
    private boolean apprentissage_const = false;
    // ------------------------------ FIN OPTIONS -------------------------------------

    public boolean getApprentissageCst(){
    	return apprentissage_const;
    }
    
    public void setApprentissageCst(boolean b){
    	apprentissage_const = b;
    	setChanged();
    	notifyObservers();
    }
    
    public boolean getVoisinageCst(){
    	return voisinage_const;
    }
    
    public void setVoisinageCst(boolean v_c){
    	voisinage_const = v_c;
    	setChanged();
    	notifyObservers();
    }
    
    public double getVoisinage(){
    	return voisinage;
    }
    
    public void setVoisinage(double v){
    	voisinage = v;
    	setChanged();
    	notifyObservers();
    }
    
    public double getApprentissage(){
    	return apprentissage;
    }
    
    public void setApprentissage(double appr){
    	apprentissage = appr;
    	setChanged();
    	notifyObservers();
    }
    
    public int getEpochs(){
    	return nb_epochs;
    }
    
    public void setEpochs(int nb){
    	nb_epochs = nb;
    	setChanged();
    	notifyObservers();
    }
    
    public int getNbDep(){
    	return nbNeuronesDep;
    }
    
    public void setNbDep(int nb_neur){
    	nbNeuronesDep = nb_neur;
    	setChanged();
    	notifyObservers();
    }
    
    public Visuel () {	
    	neurones = new ArrayList<ArrayList<Neurone>>();
	
		//création data
		data = new ArrayList<Data>();
    }
    
    public void clear(){
    	data.clear();
    	neurones.clear();
    	network = null;
    	
    	setChanged();
    	notifyObservers();
    }
    
    public boolean getGoOn(){
    	return goOn;
    }
    
    public void setGoOn(boolean b){
    	goOn = b;
    	setChanged();
    	notifyObservers();
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

    public void majGr() {
    	setChanged();
    	notifyObservers(this);
    }

    public ArrayList<ArrayList<Neurone>> getNeurones() {
    	return neurones;
    }
 
    public ArrayList<Data> getData() {
    	return data;
    }
    
    public NeuralNetwork getNetwork(){
    	return network;
    }
    
    
    public void setData(ArrayList<Data> data) {
    	this.data = data;
    }
 
   public void setData(char c) {
	switch(c){
	case 'C': //carre
	    data.removeAll(data);
	    for (int nb = 0; nb < nb_data ; nb ++) {
		double x = Math.random();
		double y = Math.random();
		Point2D.Double p = new Point2D.Double(x,y);
		ArrayList<Float> poids = new ArrayList<Float>();
		poids.add(new Float(x));
		poids.add(new Float(y));
		data.add(new Data(poids));
	    }
	    setChanged();
	    notifyObservers(this);
	    break;

	case 'L':			
	    //lune		
	    int nb = 0;
	    Ellipse2D.Double n = new Ellipse2D.Double(0,0, 1, 1);
	    Ellipse2D.Double n2 = new Ellipse2D.Double(0,0, .6, .6);
	    data.removeAll(data);
	    while(nb<nb_data){
		double x = Math.random();
		double y = Math.random();
		Point2D.Double p = new Point2D.Double(x,y);
		if(n.contains(p) && !n2.contains(p)){
		    ArrayList<Float> poids = new ArrayList<Float>();
		    poids.add(new Float(x));
		    poids.add(new Float(y));
		    data.add(new Data(poids));
		    nb++;
		}
	    }
	    setChanged();
	    notifyObservers(this);
	    break;
	case 'A':			
	    //anneau	
	    data.removeAll(data);
	    int nb2 = 0;
	    Ellipse2D.Double n1 = new Ellipse2D.Double(   0,  0, 1, 1);
	    Ellipse2D.Double n22 = new Ellipse2D.Double(.2,.2, .6, .6);
	    while(nb2<nb_data){
		double x = Math.random();
		double y = Math.random();
		Point2D.Double p = new Point2D.Double(x,y);
		if(n1.contains(p) && !n22.contains(p)){
		    ArrayList<Float> poids = new ArrayList<Float>();
		    poids.add(new Float(x));
		    poids.add(new Float(y));
		    data.add(new Data(poids));
		    nb2++;
		}
	    }
	    setChanged();
	    notifyObservers(this);
	    break;			
	case 'c':			
	    //cercle	
	    data.removeAll(data);
	    int nb3 = 0;
	    Ellipse2D.Double n3 = new Ellipse2D.Double(0,0, 1, 1);
	    while(nb3<nb_data){
		double x = Math.random();
		double y = Math.random();
		Point2D.Double p = new Point2D.Double(x,y);
		if(n3.contains(p)){
		    ArrayList<Float> poids = new ArrayList<Float>();
		    poids.add(new Float(x));
		    poids.add(new Float(y));
		    data.add(new Data(poids));
		    nb3++;
		}
	    }
	    setChanged();
	    notifyObservers(this);
	    break;
	case 'D': // Differentes densites
	    data.removeAll(data);
	    int nb4;
	    for (nb4 = 0; nb4 < (int) (nb_data/3) ; nb4 ++) {
		double x = Math.random()/3;
		double y = Math.random()/3;
		Point2D.Double p = new Point2D.Double(x,y);
		ArrayList<Float> poids = new ArrayList<Float>();
		poids.add(new Float(x));
		poids.add(new Float(y));
		data.add(new Data(poids));
	    }
	    while (nb4 < nb_data) {
		nb4 ++;
		double x = Math.random();
		double y = Math.random();
		Point2D.Double p = new Point2D.Double(x,y);
		ArrayList<Float> poids = new ArrayList<Float>();
		poids.add(new Float(x));
		poids.add(new Float(y));
		data.add(new Data(poids));
	    }
	    setChanged();
	    notifyObservers(this);	    	    	    
	    break;
	case 'd': // Differentes densites
	    data.removeAll(data);
	    int nb5 = 0;
	    Ellipse2D.Double n5 = new Ellipse2D.Double(.1,.1, .4, .4);
	    while (nb5 < (int)(nb_data/3)) {
		double x = Math.random();
		double y = Math.random();
		Point2D.Double p = new Point2D.Double(x,y);
		if(n5.contains(p)){
		    ArrayList<Float> poids = new ArrayList<Float>();
		    poids.add(new Float(x));
		    poids.add(new Float(y));
		    data.add(new Data(poids));
		    nb5++;
		}
	    }
	    n5 = new Ellipse2D.Double(.7,.4, .2, .2);
	    while (nb5 < (int)(nb_data/2)) {
		double x = Math.random();
		double y = Math.random();
		Point2D.Double p = new Point2D.Double(x,y);
		if(n5.contains(p)){
		    ArrayList<Float> poids = new ArrayList<Float>();
		    poids.add(new Float(x));
		    poids.add(new Float(y));
		    data.add(new Data(poids));
		    nb5++;
		}
	    }
	    while (nb5 < nb_data) {
		nb5 ++;
		double x = Math.random();
		double y = Math.random();
		Point2D.Double p = new Point2D.Double(x,y);
		ArrayList<Float> poids = new ArrayList<Float>();
		poids.add(new Float(x));
		poids.add(new Float(y));
		data.add(new Data(poids));
	    }	    	    	    
	    setChanged();
	    notifyObservers(this);
	    break;
	
	case 'p': // petit carre seul
	    data.removeAll(data);
	    int nb6;
	    for (nb6 = 0; nb6 <nb_data ; nb6 ++) {
		double x = Math.random()/2;
		double y = Math.random()/2;
		Point2D.Double p = new Point2D.Double(x,y);
		ArrayList<Float> poids = new ArrayList<Float>();
		poids.add(new Float(x));
		poids.add(new Float(y));
		data.add(new Data(poids));
	    }
	    setChanged();
	    notifyObservers(this);	    	    	    
	    break;


	case 'P': // petit cercle seul
	    data.removeAll(data);
	    int nb7= 0;
	    Ellipse2D.Double n7 = new Ellipse2D.Double(.6,.6, .4, .4);
	    while (nb7 < nb_data/2) {
		double x = Math.random();
		double y = Math.random();
		Point2D.Double p = new Point2D.Double(x,y);
		if(n7.contains(p)){
		    ArrayList<Float> poids = new ArrayList<Float>();
		    poids.add(new Float(x));
		    poids.add(new Float(y));
		    data.add(new Data(poids));
		    nb7++;
		}
	    }
	    setChanged();
	    notifyObservers(this);	    	    	    
	    break;
	case 's': // Differentes densites separees
	    data.removeAll(data);
	    int nb8 = 0;
	    Ellipse2D.Double n8 = new Ellipse2D.Double(.5,.1, .45, .45);
	    while (nb8 < (int)(nb_data/3)) {
		double x = Math.random();
		double y = Math.random();
		Point2D.Double p = new Point2D.Double(x,y);
		if(n8.contains(p)){
		    ArrayList<Float> poids = new ArrayList<Float>();
		    poids.add(new Float(x));
		    poids.add(new Float(y));
		    data.add(new Data(poids));
		    nb8++;
		}
	    }
	    n8 = new Ellipse2D.Double(.2,.7, .25, .25);
	    while (nb8 < (int)(nb_data)) {
		double x = Math.random();
		double y = Math.random();
		Point2D.Double p = new Point2D.Double(x,y);
		if(n8.contains(p)){
		    ArrayList<Float> poids = new ArrayList<Float>();
		    poids.add(new Float(x));
		    poids.add(new Float(y));
		    data.add(new Data(poids));
		    nb8++;
		}
	    }
	    setChanged();
	    notifyObservers(this);
	    break;
	    
	   
	}
	
	setChanged();
    notifyObservers(this);
	
   }
}
    
	
