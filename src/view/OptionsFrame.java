package view;

import java.awt.BorderLayout;
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

import model.Data;
import model.Options;

@SuppressWarnings("serial")
public class OptionsFrame extends JFrame{
	private WorldInterface holder;
	private JPanel container;
	
	/*--- Options for AbstractNetwork ---*/
		private JFormattedTextField field_nb_data;
		private JFormattedTextField field_nb_epochs;
		private JFormattedTextField field_time_to_wait;
		
	/*--- Options for AbstractMap (SOM, DSOM) ---*/
		private JSlider slider_width;
		private JSlider slider_height;
		
	/*--- Options for SOM ---*/
		private JCheckBox choice_learning;
		private JCheckBox choice_neighbor;
		private JSpinSliderWrapper wrapper_neighborhood_som;
		private JSpinSliderWrapper wrapper_learning_som;
		private JCheckBox choice_continuous;
		
	/*--- Options for DSOM ---*/
		private JSpinSliderWrapper wrapper_elasticity;
		private JSpinSliderWrapper wrapper_epsilon;
		
	/*--- Options for GNG ---*/
		private JSlider slider_nb_dep;
		private JFormattedTextField field_max_age;
		private JFormattedTextField field_max_neurons;
		private JFormattedTextField field_time_new;
		private JSpinSliderWrapper wrapper_neighborhood_gng;
		private JSpinSliderWrapper wrapper_winner_gng;
		
		
	public OptionsFrame(WorldInterface h){
		super();
		
		holder=h;
		
		//We initialize all attributes
		field_nb_data = new JFormattedTextField(new Integer(Options.getOptions().getNbData()));
		field_nb_epochs = new JFormattedTextField(new Integer(Options.getOptions().getNbEpochs()));
		field_time_to_wait = new JFormattedTextField(new Integer(Options.getOptions().getTimeToWait()));
		
		slider_width = getIntSlider(1, 10, 1);
		slider_height = getIntSlider(1, 10, 1);
		
		choice_learning = new JCheckBox();
		choice_neighbor = new JCheckBox();
		
		choice_continuous = new JCheckBox();
		
		Options o = Options.getOptions();
		Double ela_min, ela_max, ela_step, ela_val;
		
		ela_min = new Double(0.05);
		ela_max = new Double(5);
		ela_step = new Double(0.01);
		ela_val = new Double(o.getElasticity());
		
		wrapper_elasticity = new JSpinSliderWrapper(ela_min, ela_max, ela_val, ela_step);
		wrapper_elasticity.setNotNull(true);
		
		Double epsi_min, epsi_max, epsi_step, epsi_val;
		
		epsi_min = new Double(0);
		epsi_max = new Double(1);
		epsi_step = new Double(0.1);
		epsi_val = new Double(o.getEpsilon());
		
		wrapper_epsilon = new JSpinSliderWrapper(epsi_min, epsi_max, epsi_val, epsi_step);
		
		slider_nb_dep = getIntSlider(2, 10, 1);
		
		field_max_age = new JFormattedTextField(new Integer(Options.getOptions().getMaxAge()));
		field_max_neurons = new JFormattedTextField(new Integer(Options.getOptions().getMaxNeurons()));
		field_time_new = new JFormattedTextField(new Integer(Options.getOptions().getTimeNew()));
		
		Double neigh_min, neigh_max, neigh_step, neigh_val;
		
		neigh_min = new Double(0.00001);
		neigh_max = new Double(0.0001);
		neigh_val = new Double(o.getNeighborhoodGng());
		neigh_step = new Double(0.00001);
		
		wrapper_neighborhood_gng = new JSpinSliderWrapper(neigh_min, neigh_max, neigh_val, neigh_step);
		
		Double win_min, win_max, win_val, win_step;
		
		win_min = new Double(0.01);
		win_max = new Double(1);
		win_val = new Double(o.getWinnerDistGng());
		win_step = new Double(0.01);
		
		wrapper_winner_gng = new JSpinSliderWrapper(win_min, win_max, win_val, win_step);
		
		neigh_min = new Double(0.05);
		neigh_max = new Double(0.2);
		neigh_val = new Double(o.getNeighborhoodSom());
		neigh_step = new Double(0.01);
		
		wrapper_neighborhood_som = new JSpinSliderWrapper(neigh_min, neigh_max, neigh_val, neigh_step);
		wrapper_neighborhood_som.setEnabled(false);
		
		Double learn_min, learn_max, learn_val, learn_step;
		
		learn_min = new Double(0.05);
		learn_max = new Double(2);
		learn_val = new Double(o.getLearningSom());
		learn_step = new Double(0.01);
		
		wrapper_learning_som = new JSpinSliderWrapper(learn_min, learn_max, learn_val, learn_step);
		wrapper_learning_som.setEnabled(false);
			
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
			slider_nb_dep.setEnabled(false);
			field_max_age.setEnabled(false);
			field_max_neurons.setEnabled(false);
			field_time_new.setEnabled(false);
			
		}else{
			//In other case we enable all components
			field_nb_data.setEnabled(true);
			field_nb_epochs.setEnabled(true);
			slider_width.setEnabled(true);
			slider_height.setEnabled(true);
			slider_nb_dep.setEnabled(true);
			field_max_age.setEnabled(true);
			field_max_neurons.setEnabled(true);
			field_time_new.setEnabled(true);
		}
	}
	
	/**
	 * This method switch the interface depending on the network held by the world
	 * @param s Name of the network (get with its toString())
	 */
	public void switcher(String s){
		container = new JPanel();
		//We set 0 to have an infinite possibility of rows
		container.setLayout(new GridLayout(0, 1, 30, 30)); 
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
		add("Time to wait (for the speed) : ", field_time_to_wait);
	}
	
	public void setAbstractMapFrame(){
		slider_width.setValue(Options.getOptions().getNbColumns());
		slider_height.setValue(Options.getOptions().getNbRows());
		
		add("Width of the grid : ", slider_width);
		add("Height of the grid : ", slider_height);
	}
	
	public void setSOMFrame(){
		setAbstractMapFrame();
		
		choice_continuous.setSelected(Options.getOptions().getContinuous() == 1);
		choice_learning.setSelected(Options.getOptions().getLearningCst());
		choice_neighbor.setSelected(Options.getOptions().getNeighborhoodCst());
		
		add("Continuous : ", choice_continuous);
		
		//We add the two checkboxes on the same line
		String[] labels = {"Constant learning : \t", "Constant neighborhood : "};
		JCheckBox[] boxes = {choice_learning, choice_neighbor};
		add(labels, boxes);

		add("Learning :           ", wrapper_learning_som);
		add("Neighborhood : ", wrapper_neighborhood_som);
		
		setTitle("Options - SOM");
	}
	
	public void setDSOMFrame(){
		setAbstractMapFrame();
		
		setTitle("Options - DSOM");
		
		add("Elasticity : ", wrapper_elasticity);
		add("Epsilon : ", wrapper_epsilon);
	}
	
	public void setGNGFrame(){
		setTitle("Options - GNG");
		
		slider_nb_dep.setValue(Options.getOptions().getNbDep());
		add("Number of neurons : ", slider_nb_dep);
		add("Nb max of neurons : ", field_max_neurons);
		add("Age max of connexions : ", field_max_age);
		add("Time of new neuron : ", field_time_new);
		add("Neighborhood : ", wrapper_neighborhood_gng);
		add("Winner distance", wrapper_winner_gng);
	}
	
	/**
	 * A method to add a new row with a label and a component
	 * @param label
	 * @param component
	 */
	public void add(String label, JComponent component){
		JPanel new_row = new JPanel();
		new_row.setLayout(new BorderLayout());
		
		new_row.add(new JLabel(label), BorderLayout.WEST);
		new_row.add(component, BorderLayout.CENTER);
		
		container.add(new_row);
	}
	
	/**
	 * A method to add a new row with a few labels and components
	 * @param labels
	 * @param components
	 */
	public void add(String[] labels, JComponent[] components){
		JPanel new_row = new JPanel();
		new_row.setLayout(new GridLayout(1, 0));
		
		for (int i = 0; i < labels.length && i < components.length; i++) {
			JPanel sub_row = new JPanel();
			sub_row.setLayout(new BorderLayout());
			
			sub_row.add(new JLabel(labels[i]), BorderLayout.WEST);
			sub_row.add(components[i],BorderLayout.CENTER);
			
			new_row.add(sub_row);
		}
		
		container.add(new_row);
	}
	
	public void addListeners(){
		final Options o = Options.getOptions();
		
		field_nb_data.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				o.setNbData((int) field_nb_data.getValue());
				Data.getData().resetData();
				holder.getWorld().change();
			}
		});
		
		field_nb_epochs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				o.setNbEpochs((int) field_nb_epochs.getValue());
			}
		});
		
		field_time_to_wait.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				o.setTimeToWait((int) field_time_to_wait.getValue());
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
		
		choice_continuous.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(choice_continuous.isSelected())
					o.setContinuous(1);
				else
					o.setContinuous(0);

			}
		});
		
		choice_learning.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				o.setLearningCst(choice_learning.isSelected());
				wrapper_learning_som.setEnabled(choice_learning.isSelected());
			}
		});
		
		choice_neighbor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				o.setNeighborhoodCst(choice_neighbor.isSelected());
				wrapper_neighborhood_som.setEnabled(choice_neighbor.isSelected());
			}
		});
		
		wrapper_neighborhood_som.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				o.setNeighborhoodSom(((JSpinSlider)e.getSource()).getValue());
			}
		});
		
		wrapper_learning_som.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				o.setLearningSom(((JSpinSlider)e.getSource()).getValue());
			}
		});
		
		wrapper_elasticity.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				o.setElasticity(((JSpinSlider)e.getSource()).getValue());
			}
		});
		
		wrapper_epsilon.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				o.setEpsilon(((JSpinSlider)e.getSource()).getValue());
			}
		});
		
		slider_nb_dep.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				o.setNbDep(slider_nb_dep.getValue());
			}
		});
		
		field_max_age.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				o.setMaxAge((int) field_max_age.getValue());
			}
		});
		
		field_max_neurons.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				o.setMaxNeurons((int) field_max_neurons.getValue());
			}
		});
		
		field_time_new.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				o.setTimeNew((int) field_time_new.getValue());
			}
		});
		
		wrapper_neighborhood_gng.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				o.setNeighborhoodGng(((JSpinSlider)e.getSource()).getValue());
			}
		});
		
		wrapper_winner_gng.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				o.setWinnerDistGng(((JSpinSlider)e.getSource()).getValue());
			}
		});
	}
}
