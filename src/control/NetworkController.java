package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JRadioButton;

import model.Environment;
import model.NetworkFactory;

public class NetworkController implements ActionListener{
	Environment world;
	
	public NetworkController(Environment w){
		world = w;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		//We get the shape the data must have
		JRadioButton source=(JRadioButton)arg0.getSource();
		String net = source.getText();
				
		//Then we modify the data
		NetworkFactory nf = new NetworkFactory(world);
		world.setNet(nf.getNetwork(net));	
	}
	
}
