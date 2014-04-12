package view;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;


@SuppressWarnings("serial")
public class JSpinSlider extends JPanel{
	private JSlider slider;
	private JSpinner spin;
	
	public JSpinSlider(int max, int min, int step){
		super();
		
		slider = new JSlider();
		spin = new JSpinner();
		
		this.setLayout(new GridLayout(2, 1));
		this.add(slider);
		this.add(spin);
	}
}
