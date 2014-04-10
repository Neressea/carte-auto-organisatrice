import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;


public class NetworkDraw extends JPanel implements Observer {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Visuel nw ;
    private int echelle = 500;
    
    public NetworkDraw(Visuel nw2, int echelle) {
    	super();
		this.nw=nw2;
		this.echelle = echelle;
    }
    
    @Override
    public void paintComponent(Graphics g) {
    	
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Neurone neur;

        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());
	
        g2.setPaint(Color.lightGray);
        //g2.setPaint(Color.gray);
        for(int i=0;i<nw.getData().size();i=i+2){
	    Ellipse2D.Double d = 
		new Ellipse2D.Double(nw.getData().get(i).getPoids().get(0)*echelle,
			nw.getData().get(i).getPoids().get(1)*echelle,.5,.5);	  
	    	g2.draw(d);
        }      


        for(int i=0;i<nw.getNeurones().size();i++){
	    for(int k=0;k<nw.getNeurones().get(i).size();k++){
		g2.setPaint(Color.red);
		neur = nw.getNeurones().get(i).get(k);		    
		
		g2.fillOval ((int)(neur.getPoids().get(0)*echelle), 
			     (int)(neur.getPoids().get(1)*echelle),
			     5,5) ;

		g2.setPaint(Color.BLUE);

		for(int j=0;j<neur.voisins.size();j++){
		    Line2D.Double l = 
			new Line2D.Double(new Point2D.Double(neur.getPoids().get(0)*echelle,
							     neur.getPoids().get(1)*echelle),
					  new Point2D.Double(neur.voisins.get(j).getPoids().get(0)*echelle,
							     neur.voisins.get(j).getPoids().get(1)*echelle)
					  );
		    g2.draw(l);
		}
	    }
        }
        
        /*g2.setPaint(Color.BLUE);
	  for(int i=0;i<nw.getNeurones().size();i++){
	  for(int k=0;k<nw.getNeurones().get(i).size();k++){
	  Neurone n = nw.getNeurones().get(i).get(k);        	
	  ArrayList<Neurone> voisins = nw.network.getConnectedNeurons(n);
	  for(int j=0;j<voisins.size();j++){
		  Line2D.Double l = 
		  new Line2D.Double(new Point2D.Double(n.getPoids().get(0)*echelle,
		  n.getPoids().get(1)*echelle),
		  new Point2D.Double(voisins.get(j).getPoids().get(0)*echelle,
		  voisins.get(j).getPoids().get(1)*echelle)
		  );
		  g2.draw(l);
	   }
	  }
        }*/
        
        revalidate();
        

    }


	@Override
	public void update(Observable arg0, Object arg1) {
		this.setNw((NeuralNetwork) arg1);
		repaint();
	}


	public NeuralNetwork getNw() {
		return nw.network;
	}


	public void setNw(NeuralNetwork network) {
		this.nw.network = network;
	}	
}
