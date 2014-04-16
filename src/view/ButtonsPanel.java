package view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import model.AbstractNetwork;
import model.Environment;
import model.NetworkFactory;
import model.Options;

@SuppressWarnings("serial")
public class ButtonsPanel extends JPanel{
	private JButton btn_clear, btn_options, btn_launch, btn_stop, btn_pause, btn_step;
	private Environment world;
	private WorldInterface holder;
	
	//A boolean to keep the previous state of the world (occuring -> true, else -> false)
	private boolean previous_state;
	
	public ButtonsPanel(WorldInterface parent, Environment w){
		
		holder = parent;
		world = w;
		
		//We create buttons
		btn_clear = new JButton("Clear");
		btn_options = new JButton("Options");
		btn_launch = new JButton("Launch");
		btn_stop = new JButton("Stop");
		btn_pause = new JButton("Pause");
		btn_step = new JButton("Step");
		
		//We add them listeners
		addListeners();
		
		//We add them to the object
		setLayout(new GridLayout(1, getClass().getDeclaredFields().length));
		add(btn_clear);
		add(btn_options);
		add(btn_launch);
		add(btn_pause);
		add(btn_step);
		add(btn_stop);
	}
	
	public void change(boolean current_state){
			btn_clear.setEnabled(!current_state);
			btn_launch.setEnabled(!current_state);
			btn_stop.setEnabled(current_state);
			btn_pause.setEnabled(current_state);
			btn_step.setEnabled(current_state);
			
			previous_state = !previous_state;
	}
	
	public void addListeners(){
		
		btn_clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				world.clear();
			}
		});
		
		btn_options.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				holder.launchOptions();
			}
		});
		
		btn_stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized (world) {
					Options.getOptions().setStopped(true);
					Options.getOptions().setPause(false);
					btn_pause.setText("Pause");
					world.notifyAll();
				}
			}
		});
		
		btn_pause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized (world) {
					if(Options.getOptions().getPaused()){
						Options.getOptions().setPause(false);
						btn_pause.setText("Pause");
					}else{
						Options.getOptions().setPause(true);
						btn_pause.setText("Resume");
					}
					world.notifyAll();
				}
			}
		});
		
		btn_step.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Options.getOptions().setStep(true);
				synchronized (world) {
					world.notifyAll();
				}
			}
		});
		
		btn_launch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				//We clear the world
				world.clear();
				
				//Then we create it again, juste like god
				world.getData().resetData();
				
				/*We create a new network based on the previous network because if we want to 
				  launch the same network without clicking on it in tje list_network, there is 
				  a Thread error*/
				NetworkFactory nf = new NetworkFactory(world);
				AbstractNetwork n = nf.getNetwork(world.getNet());
				
				//We fill the network
				n.fill();
				
				world.setNet(n);
				
				world.getNet().start();
				
			}
		});

	}
}
