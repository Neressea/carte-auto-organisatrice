package view;

public class MapInterface {

    /*private static final long serialVersionUID= 1L;
    
    //The content pane will contain a panel with all options and a NetworkDraw object
    private NetworkDraw jContentPane = null;
    private JMenuBar    jJMenuBar = null;
    private JButton 		jOptions = null;
    private JMenu           jMenu = null;
    private JMenuItem   jMenuItem = null;
	    private JMenuItem  jMenuItem1 = null;
	    private JMenuItem  jMenuItem2 = null;
	    private JMenuItem  jMenuItem3 = null;
	    private JMenuItem jMenuItem31 = null;
	    private JMenuItem jMenuItem32 = null;
	    private JMenuItem jMenuItem33 = null;
	    private JMenuItem jMenuItem34 = null;
	    private JMenuItem jMenuItem35 = null;
	    private JMenuItem jMenuItem36 = null;
    
    private boolean  somInitialize = false;
    private boolean dSomInitialize = false;
    private boolean  gngInitialize = false;
    
    private JMenu           nMenu = null;
    	private ButtonGroup buttons_algo;
    	private JRadioButton algo_som;
    	private JRadioButton algo_dsom;
    	private JRadioButton algo_gng;
    	private JRadioButton algo_msom;
    	private JRadioButton algo_mgng;
    	private JRadioButton algo_randomdsom;
    
	    private JMenuItem    nMenuSom = null;
	    private JMenuItem    nMenuGng = null;
	    private JMenuItem   nMenuDSom = null;
	    private JMenuItem    nMenuMSom = null;
	    private JMenuItem    nMenuMGng = null;
	    private JMenuItem   nMenuMDSom = null;
	    private JMenuItem   NMenuMRandomDSom = null;
	    private JMenuItem       nInit = null;  
    
    //Graphical objects for options
    private JButton btn_stop_algo = null;
    private JButton btn_clear = null;
    private JButton btn_pause = null;
    private JDialog 	options = null;
    	private JFormattedTextField nb_data = null;
    	private JSlider	slider_epochs = null;
	    private JSlider slider_elastic = null;
	    private JSlider grid_width = null;
	    private JSlider grid_height = null;
	    private JSlider slider_nb_dep = null;
	    private JCheckBox choice_learning = null;
	    private JCheckBox choice_neighbor = null;
	    	

    char[]     dataSet;	
    int      multidata = 10;

    private int echelle = 500;
    private int fenetre = 600;



    public MapInterface() {
    	super();
    	initialize();
    }

 
    private void initialize() {
			nw = new Visuel();
			//nw = new NeuralNetwork(10,10);
			this.setSize(fenetre, fenetre);
			this.setLocation(500,100);
			this.setJMenuBar(getJJMenuBar());
			this.setContentPane(getJContentPane());
			this.setTitle("JFrame");
		
			nw.addObserver(this);
		
			dataSet = new char[]{'C', 'L', 'A', 'c', 'D', 'd', 'P', 'p', 's'};
			//dataSet = new char[]{'L', 'A', 'P', 'p', 's'};
    }
	    
	
	    private NetworkDraw getJContentPane() {
	    	
			if (jContentPane == null) {
				jContentPane = new NetworkDraw(nw, echelle);
			    jContentPane.setLayout(new BorderLayout());		
			    jContentPane.setMinimumSize(new Dimension(fenetre, fenetre));
			}
		
			return jContentPane;
	    }
    

    private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
		    jJMenuBar = new JMenuBar();
		    jJMenuBar.add(getJMenu()); // data
		    jJMenuBar.add(getnMenu()); // networks
		    jJMenuBar.add(getJOptionsButton());
		    jJMenuBar.add(getJClearButton());
		    jJMenuBar.add(getJPauseButton());
		    jJMenuBar.add(getJStopButton());
		}
		return jJMenuBar;
    }
    
    public JButton getJPauseButton(){
    	if(btn_pause == null){
    		btn_pause = new JButton("pause");
    		btn_pause.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					//On inverse la pause
					nw.setPause(!nw.getPause());
					
					//On change l'affichage du bouton
					if(nw.getPause())		
						btn_pause.setText("Reprendre");
					else
						btn_pause.setText("Pause");
				}
			});
    		
    		btn_pause.setEnabled(false);
    	}
    
    	
    	return btn_pause;
    }
    
  
    public JButton getJClearButton(){
    	
    	if(btn_clear == null){
    		btn_clear = new JButton("Clear");
    		btn_clear.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					nw.clear();
				}
			});
    	}
    	
    	return btn_clear;
    }
    
    public JButton getJStopButton(){
    	if(btn_stop_algo == null){
    		btn_stop_algo = new JButton("Stop");
    		btn_stop_algo.setEnabled(false);
    		btn_stop_algo.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					nw.setGoOn(false);
					
					//On elève la pause si jamais elle a été mise
					nw.setPause(false);
				}
			});
    	}
    	
    	return btn_stop_algo;
    }
    
    public JButton getJOptionsButton(){
    	if(jOptions == null){
    		jOptions = new JButton("Options");
    		jOptions.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					launchJOptionsPane();
				}
			});
    	}
    	
    	return jOptions;
    }
    
    

    private void launchJOptionsPane(){
    	if(options == null){
    		options = new JDialog(this, "Options", false);
    		
    		JPanel cont = new JPanel();
    		cont.setLayout(new GridLayout(10, 2));
    		
    		cont.add(new JLabel("Nb données (TOUS) : "));
    		cont.add(getNbData());
    		cont.add(new JLabel("Nb époques x 1000 (TOUS) : "));
    		cont.add(getEpochs());
    		cont.add(new JLabel("Elasticité (DSOM) : "));
    		cont.add(getSliderElastic());
    		cont.add(new JLabel("Width (SOM, DSOM) : "));
    		cont.add(getGridWidth());
    		cont.add(new JLabel("Height (SOM, DSOM) : "));
    		cont.add(getGridHeight());
    		cont.add(new JLabel("Nb départ (GNG) : "));
    		cont.add(getNbDep());
    		cont.add(new JLabel("Apprentissage constant : "));
    		cont.add(getLearningChoice());
    		cont.add(new JLabel("Voisinage constant : "));
    		cont.add(getNeighborChoice());
    		
    		options.setContentPane(cont);
    		options.pack();
    		
    	}
    	
    	options.setVisible(true);
    }
    
    
    private JFormattedTextField getNbData(){
    	if(nb_data == null){
    		nb_data = new JFormattedTextField(new Integer(100000));
    		nb_data.addActionListener(new ActionListener() {	
				@Override
				public void actionPerformed(ActionEvent e) {
					nw.setNbData((Integer)nb_data.getValue()); 
				}
			});
    	}
    	
    	return nb_data;
    }
    
    private JCheckBox getNeighborChoice(){
    	
    	if(choice_neighbor == null){
    		choice_neighbor = new JCheckBox();
    		choice_neighbor.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					nw.setVoisinageCst(choice_neighbor.isSelected());
				}
			});
    	}
    	
    	return choice_neighbor;
    }
    
    private JCheckBox getLearningChoice(){
    	
    	if(choice_learning == null){
    		choice_learning = new JCheckBox();
    		choice_learning.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					nw.setApprentissageCst(choice_learning.isSelected());
				}
			});
    	}
    	
    	return choice_learning;
    }
    
    private JSlider getEpochs(){
    	if(slider_epochs == null){
    		slider_epochs = getSlider(1, 10, 1);
    		slider_epochs.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					nw.setEpochs(slider_epochs.getValue() * 1000);
				}
			});
    	}
    	
    	return slider_epochs;
    }
    
    private JSlider getNbDep(){
    	if(slider_nb_dep == null){
    		slider_nb_dep = getSlider(2, 6, 1);
    		slider_nb_dep.setValue(2);
    		
    		slider_nb_dep.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					nw.setNbDep(slider_nb_dep.getValue());
				}
			});
    	}
    	
    	return slider_nb_dep;
    }
    
    private JSlider getGridWidth(){
    	if(grid_width == null){
    		grid_width = getSlider(1, 10, 1);
    		grid_width.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					nw.setNbColumns(grid_width.getValue());
				}
				
			});
    	}
    	
    	return grid_width;
    }
    
    private JSlider getGridHeight(){
    	if(grid_height == null){
    		grid_height = getSlider(1, 10, 1);
    		
    		grid_height.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					nw.setNbRows(grid_height.getValue());
				}
				
			});
    	}
    	
    	return grid_height;
    }
    
    private JSlider getSliderElastic(){
    	if(slider_elastic == null){
    		slider_elastic = getSlider(1, 5, 0.5);
    		
    		slider_elastic.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					nw.setElasticity(slider_elastic.getValue() * 0.5);
				}
				
			});
    	}
    	
    	return slider_elastic;
    }
    
    private JSlider getSlider(int beg, int end, double pas){
    	JSlider slider = new JSlider();
    		
    		slider.setMinimum(beg);
    		slider.setMaximum(end);
    		
    		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
    		
    		for(int i=0; i<slider.getMaximum(); i++)
    			labelTable.put( new Integer( i ), new JLabel(i * pas + "") );
    		
    		
    		slider.setLabelTable( labelTable );
    		
    		slider.setPaintLabels(true);
    		slider.setPaintTicks(true);
    		slider.setPaintTrack(true);
    		
    		return slider;
    		
    }
    

    private JMenu getJMenu() {
		if (jMenu == null) {
		    jMenu = new JMenu();
		    jMenu.setText("Data");
		    jMenu.add(getJMenuItem());
		    jMenu.add(getJMenuItem1());
		    jMenu.add(getJMenuItem2());
		    jMenu.add(getJMenuItem3());
		    //jMenu.add(getJMenuItem31());
		    jMenu.add(getJMenuItem32());
		    jMenu.add(getJMenuItem33());
		    jMenu.add(getJMenuItem34());
		    jMenu.add(getJMenuItem35());
		    jMenu.add(getJMenuItem36());	    
		}
		
		return jMenu;
    }

    private JMenuItem getJMenuItem() {
	if (jMenuItem == null) {
	    jMenuItem = new JMenuItem();
	    jMenuItem.setText("Carre");
	    jMenuItem.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		    	nw.clear();
		    	nw.setData('C');
		    }
		});
	    
	}
	return jMenuItem;
	}


    private JMenuItem getJMenuItem1() {
	if (jMenuItem1 == null) {
	    jMenuItem1 = new JMenuItem();
	    jMenuItem1.setText("Lune");
	    jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		    	nw.clear();
		    	nw.setData('L');
		    }
		});
	}
	return jMenuItem1;
    }


    private JMenuItem getJMenuItem2() {
	if (jMenuItem2 == null) {
	    jMenuItem2 = new JMenuItem();
	    jMenuItem2.setText("Anneau");
	    jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		    	nw.clear();
		    	nw.setData('A');
			//jContentPane.paintImmediately(0, 0, jContentPane.getWidth(), jContentPane.getHeight());					
		    }
		});
	}
	return jMenuItem2;
    }
    

    private JMenuItem getJMenuItem3() {
	if (jMenuItem3 == null) {
	    jMenuItem3 = new JMenuItem();
	    jMenuItem3.setText("Cercle");
	    jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		    	nw.clear();
		    	nw.setData('c');
			//jContentPane.paintImmediately(0, 0, jContentPane.getWidth(), jContentPane.getHeight());									
		    }
		});
	}
	return jMenuItem3;
    }
    
    
    private JMenuItem getJMenuItem32() {
		if (jMenuItem32 == null) {
		    jMenuItem32 = new JMenuItem();
		    jMenuItem32.setText("Deux densites");
		    jMenuItem32.addActionListener(new java.awt.event.ActionListener() {
			    public void actionPerformed(java.awt.event.ActionEvent e) {
			    	nw.clear();
			    	nw.setData('D');
			    }
			});
		}
		return jMenuItem32;
    }

    private JMenuItem getJMenuItem33() {
	if (jMenuItem33 == null) {
	    jMenuItem33 = new JMenuItem();
	    jMenuItem33.setText("Trois densites");
	    jMenuItem33.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		    	nw.clear();
		    	nw.setData('d');
		    }
		});
	}
	return jMenuItem33;
    }
 
   private JMenuItem getJMenuItem34() {
	if (jMenuItem34 == null) {
	    jMenuItem34 = new JMenuItem();
	    jMenuItem34.setText("Petit carre seul");
	    jMenuItem34.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		    	nw.clear();
		    	nw.setData('p');
		    }
		});
	}
	return jMenuItem34;
    }
    private JMenuItem getJMenuItem35() {
	if (jMenuItem35 == null) {
	    jMenuItem35 = new JMenuItem();
	    jMenuItem35.setText("Petit cercle seul");
	    jMenuItem35.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
			    	nw.clear();
			    	nw.setData('P');
		    }
		});
	}
	return jMenuItem35;
    }

   private JMenuItem getJMenuItem36() {
	if (jMenuItem36 == null) {
	    jMenuItem36 = new JMenuItem();
	    jMenuItem36.setText("Deux densites distinctes");
	    jMenuItem36.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		    	nw.clear();
		    	nw.setData('s');
		    }
		});
	}
	return jMenuItem36;
    }

   	

    // Networks
   
   private void launch(){
   		String selected = ((JRadioButton) buttons_algo.getSelection()).getText();
   		
   	
   }
    
    private JMenu getnMenu() {
		if (nMenu == null) {
		    nMenu = new JMenu();
		    nMenu.setText("Algorithms");
		    
		    buttons_algo = new ButtonGroup();
		    
		    algo_som = new JRadioButton("Self Organizing Map");
		    algo_dsom = new JRadioButton("Dynamic Self Organizing Map");
		    algo_gng = new JRadioButton("Growing Neural Gas");
		    algo_msom= new JRadioButton("Multiple Self Organizing Map");
		    algo_mgng= new JRadioButton("Multiple Growing Neural Gas");
		    
		    buttons_algo.add(algo_som);
		    buttons_algo.add(algo_dsom);
		    buttons_algo.add(algo_gng);
		    buttons_algo.add(algo_msom);
		    buttons_algo.add(algo_mgng);
		    
		    JPanel algos = new JPanel();
		    algos.setLayout(new GridLayout(5, 1));
		    algos.add(algo_som);
		    algos.add(algo_dsom);
		    algos.add(algo_gng);
		    algos.add(algo_msom);
		    algos.add(algo_mgng);
	
		    nMenu.add(algos);
		    
		    
		    /*nMenu.add(getNMenuSom()); 
		    nMenu.add(getNMenuGng()); 	   
		    nMenu.add(getNMenuDSom()); 	   
		    nMenu.add(getNInit());
		    nMenu.add(getNMenuMSom()); 
		    nMenu.add(getNMenuMGng()); 	   
		    nMenu.add(getNMenuMDSom()); 
		    nMenu.add(getNMenuMRandomDSom()); 
		}
		return nMenu;
    }
    
    

    private JMenuItem getNMenuSom() {
	if (nMenuSom == null) {
	    nMenuSom = new JMenuItem();
	    nMenuSom.setText("Self Organizing Map");
	    nMenuSom.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
			//teste l'existence de donnees
			if (nw.data.size() == 0) {
				
				//JOptionPane.showConfirmDialog(parentComponent, message, title, optionType, messageType)
				JOptionPane.showMessageDialog(null, "Initialisez les données !", "Erreur", JOptionPane.ERROR_MESSAGE);
			    //System.out.println(" Initialisez les donnees\r");
			    
			} else {
				
			    if (!somInitialize) {
					nw.network = new SOM(nw);
					somInitialize = true;
					dSomInitialize = false;
					gngInitialize  = false;
			    }
			    nw.network.learn();
			    
			}				
		    }
		});
	}
	return nMenuSom;
    }
    
    private JMenuItem getNMenuGng() {
	if (nMenuGng == null) {
	    nMenuGng = new JMenuItem();
	    nMenuGng.setText("Growing Neural Gas");
	    nMenuGng.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
			//teste l'existence de donnees
			if (nw.data.size() == 0) {
				JOptionPane.showMessageDialog(null, "Initialisez les données !", "Erreur", JOptionPane.ERROR_MESSAGE);
			    //System.out.println(" Initialisez les donnees\r");
			} else {
			    if (!gngInitialize) {
			    	nw.network = new GNG(2, 100, nw);
			    	gngInitialize = true;
			    	somInitialize = false;
			    	dSomInitialize = false;
			    }
			    nw.network.learn();
			}				
		    }
		});
	}
	return nMenuGng;
    }

    private JMenuItem getNMenuDSom() {
		if (nMenuDSom == null) {
		    nMenuDSom = new JMenuItem();
		    nMenuDSom.setText("Dynamic Self-Organizing Map");
		    nMenuDSom.addActionListener(new java.awt.event.ActionListener() {
			    public void actionPerformed(java.awt.event.ActionEvent e) {
				if (nw.data.size() == 0) {
					JOptionPane.showMessageDialog(null, "Initialisez les données !", "Erreur", JOptionPane.ERROR_MESSAGE);
				    //System.out.println(" Initialisez les donnees\r");
				} else {
				    if (!dSomInitialize) {
						nw.network = new DSOM(nw);
						dSomInitialize = true;
						somInitialize  = false;
						gngInitialize  = false;
				    }
				    nw.network.learn();
				}
							
			    }
			});
		}
		return nMenuDSom;
    }
    private JMenuItem getNMenuMSom() {
	if (nMenuMSom == null) {
	    nMenuMSom = new JMenuItem();
	    nMenuMSom.setText("Multiple Self Organizing Map");
	    nMenuMSom.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
				//teste l'existence de donnees
				SOM som = new SOM(nw);
				//nw.network = new SOM(10,10, 5000, nw);
				som.onLineSom();
				nw.network = som;
					for (int i = 0; i < multidata; i++) {
					    //nw.network.setContinu(i,multidata);
					    nw.setData(dataSet[(int)(Math.random() * dataSet.length)]);
					    nw.network.learn();
					}
				//nw.network.setContinu(0,1);
		    }
		});
	}
	return nMenuMSom;
    }
    
    private JMenuItem getNMenuMGng() {
		if (nMenuMGng == null) {
		    nMenuMGng = new JMenuItem();
		    nMenuMGng.setText("Multiple Growing Neural Gas");
		    nMenuMGng.addActionListener(new java.awt.event.ActionListener() {
			    public void actionPerformed(java.awt.event.ActionEvent e) {
					nw.network = new GNG(2, 10, nw);
					for (int i = 0; i < multidata; i++) {
					    nw.setData(dataSet[(int)(Math.random() * dataSet.length)]);
					    nw.network.learn();
					}
			    }
			});
		}
		return nMenuMGng;
    }
	
    private JMenuItem getNMenuMDSom() {
	if (nMenuMDSom == null) {
	    nMenuMDSom = new JMenuItem();
	    nMenuMDSom.setText("Multiple Dynamic Self-Organizing Map");
	    nMenuMDSom.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
			nw.network = new DSOM(nw);
			
			for (int i = 0; i < multidata; i++) {
			    int dataset = (int)(Math.random() * dataSet.length);
			    ////System.out.println("DataSet["+dataset+"]  ");
			    nw.setData(dataSet[dataset]);
			    nw.network.learn();
			}
						
		    }
		});
	}
	return nMenuMDSom;
    }



       private JMenuItem getNMenuMRandomDSom() {
	if (NMenuMRandomDSom == null) {
	    NMenuMRandomDSom = new JMenuItem();
	    NMenuMRandomDSom.setText("Random Dynamic Self-Organizing Map");
	    NMenuMRandomDSom.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
			nw.network = new DSOM(nw);
			int dataset = (int)(Math.random() * dataSet.length);
			nw.setData(dataSet[6]);	
			for (int v = 0; v < 2; v++)
			    nw.network.learn();

			for (int i = 0; i < 100; i++) {
			    // recuperer un certain  nombre de donnees
			    int nbNew = (int)(Math.random() * nw.getData().size()/5);
			    double lx = Math.random();
			    double ly = Math.random();
			    //System.out.println("  lx = " + lx + "  ly = " + ly);
			    for (int j = 0 ; j < nbNew ; j++) {
				int index = (int)(Math.random() * nw.getData().size());
				Data data = nw.getData().get(index);
				double nbdim = Math.random();
				double x,y;
				if (nbdim < .333) {				    
				    // modification de la premiere dimension
				    x = Math.random() * lx;
				    y = (double) data.getPoids().get(1);
				} else if (nbdim < .666) {
				    // modification de la seconde dimension
				    x = (double) data.getPoids().get(1);
				    y = Math.random() * ly;

				} else {
				    // modification des deux dimensions
				    x = Math.random() * lx;
				    y = Math.random() * ly;
				}
				
				ArrayList<Float> poids = new ArrayList<Float>();
				poids.add(new Float(x));
				poids.add(new Float(y));
				
				nw.getData().set(index,new Data(poids));				    				    				
			    }
			    // changer un certain nombre de parametres sur ces donnees
			    nw.network.learn();
			}	
		    }
		});
	}
	return NMenuMRandomDSom;
    }






    
    
    @Override
    public void update(Observable arg0, Object arg1) {
    	//main.repaint();
    	//jContentPane.paintImmediately(0, 0, jContentPane.getWidth(), jContentPane.getHeight());
    	
    	//On vérifie si un algo a été lancé
    	if(nw.getGoOn()){
    		
    		//Dans ce cas on désactive les options
    		jOptions.setEnabled(false);
    		jMenu.setEnabled(false);
    		nMenu.setEnabled(false);
    		btn_clear.setEnabled(false);
    		btn_stop_algo.setEnabled(true);
    		btn_pause.setEnabled(true);
    		
    	}else{
    		//Autrement on les réactive toutes
    		jOptions.setEnabled(true);
    		jMenu.setEnabled(true);
    		nMenu.setEnabled(true);
    		btn_clear.setEnabled(true);
    		btn_stop_algo.setEnabled(false);
    		btn_pause.setEnabled(false);
    		
    		gngInitialize = false;
    		somInitialize = false;
    		dSomInitialize = false;
    	}
    	
    	jContentPane.paintImmediately(0, 0, jContentPane.getWidth(), jContentPane.getHeight());
    	////System.out.println(jContentPane.getLocation().getX() +"   "+ jContentPane.getLocation().getY());
    }*/
}

