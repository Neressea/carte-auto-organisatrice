package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JRadioButton;
import model.Environment;

public class DataController implements ActionListener{
	private static HashMap<String, Character> correspondences = new HashMap<String, Character>();
	private Environment world;
	
	//We intitialize the Hashmap
	static{
		correspondences.put("Square", 'S');
		correspondences.put("Moon", 'M');
		correspondences.put("Ring", 'R');
		correspondences.put("Circle", 'C');
		correspondences.put("Two densities", '2');
		correspondences.put("Three densities", '3');
		correspondences.put("Little square", 's');
		correspondences.put("Little circle", 'c');
		correspondences.put("Two distinct densities", 'D');
	}
	
	public DataController(Environment w){
		world = w;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		//We get the shape the data must have
		JRadioButton source=(JRadioButton)arg0.getSource();
		char shape = correspondences.get(source.getText());
		
		//Then we modify the data
		world.setData(shape);
	}
	
}
