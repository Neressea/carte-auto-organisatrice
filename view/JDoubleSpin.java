package view;

import java.awt.Dimension;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.NumberFormatter;

/**
 * This class represents a JSpinner that holds values between two bounds with a defined step 
 * and that prevents wrong values in the editor.
 * 
 * @author valbert
 *
 */

@SuppressWarnings("serial")
class JDoubleSpin extends JSpinner{
	/**
	 * @param width The width of the spin
	 * @param height The height of the spin
	 * @param min The min bound of the spin
	 * @param max The max bound of the spin
	 * @param value The first value of the spin
	 * @param step The step of the spin
	 */
	public JDoubleSpin(int width, int height, Double min, Double max, Double value, Double step){
		super();
		
		setPreferredSize(new Dimension(width, height));
		
		//We change the model of the spin (for arrows)
		setModel(new SpinnerNumberModel(value,min,max,step));
		
		//We change the model of the text editor 
		setEditor(new JSpinner.NumberEditor(this, getStringModel(min, max, step)));
		
		//We forbid wrong values (you don't say ^^')
		JFormattedTextField txt = ((JSpinner.NumberEditor) getEditor()).getTextField();
		((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);
	}
	
	/**
	 * A spin constructor. Heigth and width are predefined :
	 * 		w -> 50px
	 * 		h -> 50px
	 * 
	 * @param min The min bound of the spin
	 * @param max The max bound of the spin
	 * @param value The first value of the spin
	 * @param step The step of the spin
	 */
	public JDoubleSpin(Double min, Double max, Double value, Double step){
		this(50, 50, min, max, value, step);
	}
	
	public void setValue(Double v){
		getModel().setValue(v);
		((JSpinner.NumberEditor)getEditor()).getTextField().setText(new String(v+""));
	}
	
	/**
	 * A method that compute the string corresponding to the model we need for the editor
	 * @param max The maximal bound of the spin
	 * @param step The step of the spin 
	 * @return The string corresponding to the model
	 */
	public String getStringModel(Double min, Double max, Double step){
		//We create the model of the spinner (values that he can take)
		String model = "";
		
		/*We compute the max of values that there can be in the editor.
		  We just verify the integer part, the decimal part will be
		  compute with the step*/
		String s_max = max.toString();
		String[] split_max = s_max.split("\\.");
		for(int i=0; i<split_max[0].length(); i++)
			model = model.concat("#");
		
		//Then we compute the number of decimals that there can be
		String s_step = step.toString();
		String[] split_step = s_step.split("\\.");
		
		String s_min = min.toString();
		String[] split_min = s_min.split("\\.");
		
		//We watch what has the more decimals
		int [] nb_decs = new int[3];
		
		if(split_max.length>1)
			nb_decs[0] = split_max[1].length();
		
		if(split_step.length>1)
			nb_decs[1] = split_step[1].length();
		
		if(split_min.length>1)
			nb_decs[2] = split_min[1].length();
		
		int nb_dec=0;
		for (int i = 0; i < nb_decs.length; i++) {
			if(nb_decs[i]>nb_dec)
				nb_dec = nb_decs[i];
		}
		
		//We verify that there are decimals
		if(nb_dec>0){
			//Then we compute the number of decimals
			model = model.concat(".");
			for(int i=0; i<nb_dec; i++)
				model = model.concat("#");
		}
		
		System.out.println(model);
		
		return model;
	}
}
