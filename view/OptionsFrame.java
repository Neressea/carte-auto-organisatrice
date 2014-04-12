package view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.Options;

@SuppressWarnings("serial")
public class OptionsFrame extends JFrame{
	private JPanel container;
	
	/*--- Options for AbstractNetwork ---*/
		private JFormattedTextField field_nb_data;
		private JFormattedTextField field_nb_epochs;
		
	/*--- Options for AbstractMap (SOM, DSOM) ---*/
		private JSlider slider_width;
		private JSlider slider_height;
		
	/*--- Options for SOM ---*/
		private JCheckBox choice_learning;
		private JCheckBox choice_neighbor;
		
	/*--- Options for DSOM ---*/
		private JSpinSliderWrapper wrapper_elasticity;
		
	/*--- Options for GNG ---*/
		private JSlider slider_nb_dep;
		
		
	public OptionsFrame(){
		super();
		
		//We initialize all attributes
		field_nb_data = new JFormattedTextField(new Integer(Options.getOptions().getNbData()));
		field_nb_epochs = new JFormattedTextField(new Integer(Options.getOptions().getNbEpochs()));
		
		slider_width = getIntSlider(1, 10, 1);
		slider_height = getIntSlider(1, 10, 1);
		
		choice_learning = new JCheckBox();
		choice_neighbor = new JCheckBox();
		
		wrapper_elasticity = new JSpinSliderWrapper(400, 80);
		
		slider_nb_dep = getIntSlider(2, 10, 1);
			
		//We add them listeners
		addListeners();
		
		//We will add components later with switcher()
		container = new JPanel();
		
		setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	
	public JSlider getIntSlider(int begin, int end, int step){
		JSlider slider = new JSlider(begin, end);
		
		Hashtable<Integer, JLabel> table = new Hashtable<Integer, JLabel>();
		for(int i=begin; i<=end; i++) table.put(i, new JLabel(i+""));
		
		slider.setLabelTable(table);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		
		return slider;
	}
	
	/**
	 * This method disable elements that can't be used during a process but don't change
	 * those which must be enabled during processes (elasticity, ...)
	 * @param occurring_process True if there is a process
	 */
	public void disable(boolean occurring_process){
		
		if(occurring_process){
			
			//In this case, we freeze what should be frozen
			field_nb_data.setEnabled(false);
			field_nb_epochs.setEnabled(false);
			slider_width.setEnabled(false);
			slider_height.setEnabled(false);
			choice_learning.setEnabled(false);
			choice_neighbor.setEnabled(false);
			slider_nb_dep.setEnabled(false);
			
		}else{
			//In other case we enable all components
			for(int i=0; i<container.getComponentCount(); i++){
				container.getComponent(i).setEnabled(true);
			}
		}
	}
	
	/**
	 * This method switch the interface depending on the network held by the world
	 * @param s Name of the network (get with its toString())
	 */
	public void switcher(String s){
		container = new JPanel();
		//We set 0 to have an infinite possibility of rows
		container.setLayout(new GridLayout(0, 2, 30, 30)); 
		setAbstractNetworkFrame();
		
		switch(s){
			case "SOM":
				setSOMFrame();
			break;
			
			case "DSOM":
				setDSOMFrame();
			break;
				
			case "GNG":
				setGNGFrame();
			break;
		}
		
		setContentPane(container);
		pack();
		disable(false);
	}
	
	public void setAbstractNetworkFrame(){
		field_nb_data.setValue(new Integer(Options.getOptions().getNbData()));
		field_nb_epochs.setValue(new Integer(Options.getOptions().getNbEpochs()));
		
		add("Number of data : ", field_nb_data);
		add("Number of epochs : ", field_nb_epochs);
	}
	
	public void setAbstractMapFrame(){
		slider_width.setValue(Options.getOptions().getNbColumns());
		slider_height.setValue(Options.getOptions().getNbRows());
		
		add("Width of the grid : ", slider_width);
		add("Height of the grid : ", slider_height);
	}
	
	public void setSOMFrame(){
		setAbstractMapFrame();
		
		choice_learning.setSelected(Options.getOptions().getLearningCst());
		choice_neighbor.setSelected(Options.getOptions().getNeighborhoodCst());
		
		add("Constant learning : ", choice_learning);
		add("Constant neighborhood : ", choice_neighbor);
		
		setTitle("Options - SOM");
	}
	
	public void setDSOMFrame(){
		setAbstractMapFrame();
		
		setTitle("Options - DSOM");
		
		wrapper_elasticity = new JSpinSliderWrapper(300, 80);
		add("Elasticity : ", wrapper_elasticity);
	}
	
	public void setGNGFrame(){
		
		slider_nb_dep.setValue(Options.getOptions().getNbDep());
		add("Number of neurons : ", slider_nb_dep);
		
		setTitle("Options - GNG");
	}
	
	public void add(String label, JComponent component){
		container.add(new JLabel(label));
		container.add(component);
	}
	
	
	public void addListeners(){
		final Options o = Options.getOptions();
		
		field_nb_data.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				o.setNbData((int) field_nb_data.getValue());
			}
		});
		
		field_nb_epochs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				o.setNbEpochs((int) field_nb_epochs.getValue());
			}
		});
		
		slider_width.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				o.setNbColumns(slider_width.getValue());
			}
		});
		
		slider_height.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				o.setNbRows(slider_height.getValue());
			}
		});
		
		choice_learning.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				o.setLearningCst(choice_learning.isSelected());
			}
		});
		
		choice_neighbor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				o.setNeighborhoodCst(choice_neighbor.isSelected());
			}
		});
		
		slider_nb_dep.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				o.setNbDep(slider_nb_dep.getValue());
			}
		});
	}
}
