import java.util.ArrayList;

public class Neurone {
    ArrayList<Float>     poids;	// ici x,y
    ArrayList<Neurone> voisins; // voisins directs
    int row,col;
    

    public Neurone(int cardinal, int row, int col, boolean aleatoire){
	super();
	poids   = new ArrayList<Float>();
	this.row = row;
	this.col = col;
	for (int i=0;i<cardinal;i++){
	    if (aleatoire) {
	    	poids.add(new Float(Math.random()));
	    } else {
	    	poids.add(new Float(.5));
	    }
	}
    }

    public Neurone(int cardinal,int row,int col){
	
	this(cardinal, row, col, false);
	/*super();
	  poids = new ArrayList<Float>();
	  this.row = row;
	  this.col = col;
	  for(int i=0;i<cardinal;i++){
	  //poids.add(new Float(Math.random()));	    
	  poids.add(new Float(.5));
	  }
	*/
    }


    public int getRow() {
    	return this.row;
    }
    
    public void setRow(int row) {
    	this.row = row;
    }

    public int getCol() {
    	return col;
    }
    
    public void setCol(int col) {
    	this.col = col;
    }

    public ArrayList<Float> getPoids() {
	return poids;
    }

    public void setPoids(ArrayList<Float> poids) {
	this.poids = poids;
    }

    public void setPoids(double e, double h, Data data) {
		double wi;
		for(int i=0;i<poids.size();i++){
		    wi = poids.get(i);
		    
		    //TODO : e-> apprentissage, h->voisinage
		    wi = wi + e*h*(data.getPoids().get(i) - wi);
		    poids.set(i, new Float(wi));
		}
    }
  
    public void setPoids(double taux, Data data) {
    	setPoids(taux, 1, data);
    }

    public double distance(Data data) {
	//distance euclidienne
	/*double res = 0.0;
	  for(int i=0;i<poids.size();i++){
	  res += Math.pow(Math.abs((poids.get(i)-data.getPoids().get(i))),2);
	  }
	  return Math.sqrt(res);*/
	
	//distance de manatthan
	double res = 0.0;
	for(int i=0;i<poids.size();i++){
	    res += Math.abs((poids.get(i) - data.getPoids().get(i)));
	}
	return res;
	/*Point2D.Double p1 = new Point2D.Double(poids.get(0),poids.get(1));
	  Point2D.Double p2 = new Point2D.Double(data.getPoids().get(0),data.getPoids().get(1));
	  return p1.distance(p2);*/
    }
   
	@Override
	public boolean equals(Object o){
		Neurone n = (Neurone) o;		
		return row == n.row && col == n.col;
	}
	
}
