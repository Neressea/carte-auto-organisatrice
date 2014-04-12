package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

import model.Data;
import model.Environment;
import model.Neuron;


@SuppressWarnings("serial")
public class NetworkDraw extends JPanel{
	
    private Environment world ;
    private int echelle;
    
    public NetworkDraw(int echelle) {
    	
    	super();
		world = new Environment();
		this.echelle = echelle;
		this.setPreferredSize(new Dimension(600, 600));
		
    }
    
    public Environment getWorld(){
    	return world;
    }
    
    @Override
    public void paintComponent(Graphics g) {
    	
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Neuron neur;

        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());
	
        g2.setPaint(Color.lightGray);
        
        for(int i=0;i<Data.getData().size();i+=2){
        	Ellipse2D.Double d = new Ellipse2D.Double(Data.getData().get(i).getPoids().get(0)*echelle,
			Data.getData().get(i).getPoids().get(1)*echelle,.5,.5);	  
	    	g2.draw(d);
        }      

        if(world.getNet() != null && world.getNet().size() != 0){
	        for(int i=0;i<world.getNet().getNeurons().size();i++){
			    for(int k=0;k<world.getNet().getNeurons().get(i).size();k++){
			    	
					g2.setPaint(Color.red);
					neur = world.getNet().getNeurons().get(i).get(k);	
					
					//System.out.print("("+neur.getPoids().get(0)*echelle+";"+neur.getPoids().get(1)*echelle+")"+"|");
					
					g2.fillOval ((int)(neur.getPoids().get(0)*echelle), 
						     (int)(neur.getPoids().get(1)*echelle),
						     5,5) ;
			
					g2.setPaint(Color.BLUE);
			
					for(int j=0;j<neur.getNeighbors().size();j++){
					    Line2D.Double l = 
						new Line2D.Double(new Point2D.Double(neur.getPoids().get(0)*echelle,
										     neur.getPoids().get(1)*echelle),
								  new Point2D.Double(neur.getNeighbors().get(j).getPoids().get(0)*echelle,
										     neur.getNeighbors().get(j).getPoids().get(1)*echelle)
								  );
					    g2.draw(l);
					}
			    }
	        }
        }

        revalidate();
    }
}
