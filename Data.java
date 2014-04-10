import java.util.ArrayList;


public class Data {
	ArrayList<Float> poids;	//ici x,y
	
	public Data(int cardinal,int echelle){
		super();
		poids = new ArrayList<Float>();
		for(int i=0;i<cardinal;i++){
			poids.add(new Float(Math.random()));
		}
		//point = new Point2D.Double(Math.random()*echelle,Math.random()*echelle);
	}

	public Data(ArrayList<Float> poids2) {
		super();
		this.poids=poids2;
	}

	public ArrayList<Float> getPoids() {
		return poids;
	}

	public void setPoids(ArrayList<Float> poids) {
		this.poids = poids;
	}
}
