import java.util.ArrayList;
/*
  algo : http://www.neuroinformatik.ruhr-uni-bochum.de/VDM/research/gsn/JavaPaper/node19.html
*/


public class GNG extends NeuralNetwork {
    private ArrayList<Neurone> neurones;
    private ArrayList<Data> data;
    private ArrayList<Double> erreurs;
    private ArrayList<ArrayList<Integer>> ages;// age des connexion par neurones
    private Visuel visuel;

    int max       =  100;   
    int nouveau   =  300; // nb iteration avant creation
    int agemax    =  40;
    double txVain = .05;
    double txVois = .00006;
    double alpha  = .5;
    double beta   = .0005;
    int nbNeurones;
    int dimension;
    int refresh = 30;

    public GNG (int dimension, int max, Visuel nw) {
		super();
		this.visuel     = nw;
		this.neurones   = new ArrayList<Neurone>();
		
		this.data       = nw.data;
		this.epochs     = visuel.getEpochs();
		this.nbNeurones = visuel.getNbDep();
		this.dimension  = dimension;
		this.max        = max;
		
		nw.neurones.removeAll(nw.neurones);
		neurones.removeAll(neurones);
		
		this.erreurs = new ArrayList<Double>();
		this.ages    = new ArrayList<ArrayList<Integer>>();
		
		
		nw.neurones.add(this.neurones);
		for(int i = 0 ; i < this.nbNeurones ; i++) {
		    neurones.add(new Neurone(2, i, 0, true));	    
		    erreurs.add(i,0.0);
		}
		
		// connexion entre les neurones de depart 
		// et age des connexions a 0
		for(int i = 0 ; i < this.nbNeurones ; i++) {
		    neurones.get(i).voisins = new ArrayList<Neurone>();
		    ages.add(i, new ArrayList<Integer>()); 
		    for(int j = 0 ; j < this.nbNeurones ; j ++) {
				if (i!= j) {
				    neurones.get(i).voisins.add(neurones.get(j));
				    ages.get(i).add(0);
				}
		    }
		}	
    }
    

    	public void learn(){
    		//Si un autre algo est déjà en cours, on quitte
        	if(visuel.getGoOn()) return; 	
        	visuel.setGoOn(true);
    		
        	Thread th = new Thread(){
        		
        		public void run(){
	        		int           exemples;
	    			Neurone vainq1, vainq2;
	    			double    dist1, dist2;
	    			int         num1, num2;
	    			double        distance = 0;
	    			double   distance_topo;
	    			double  distance_poids;
	    			int                ind;
	    			Neurone          cible;
	    			int            i, k, l;
	    			int              index;
	    			double          erreur;
	    			int           creation;
	    			
	    			exemples = 0;
	    			creation = max + neurones.size();
	    			
	    			/*editerCnx();
	    			  System.out.println("================================================");
	    			*/
	    		
	    			while (( neurones.size() < creation) && (exemples < epochs) && visuel.getGoOn())  {
	    				
	    				//On regarde si lka pause a été mise
	    				synchronized (visuel) {
	    					while(visuel.getPause()){
	    						try {
	    							visuel.wait();
	    						} catch (InterruptedException e1) {
	    							// TODO Auto-generated catch block
	    							e1.printStackTrace();
	    						}
	        				}
						}
	    				
	    			    ind = 0;
	    			    for(i = 0 ; i < neurones.size() ; i ++) {
	    				ind = ind + ages.get(i).size();
	    			    }
	    			    System.out.printf(" GNG \t %5d sur %5d \t %3d neurones sur %5d \t %4d connexions \r", 
	    					      exemples, epochs, neurones.size(), creation, (int)(ind/2)); 
	    			    exemples ++;
	    			    
	    		
	    			    // Tirage de l'exemple courant
	    			    index = (int) (Math.random()*data.size());
	    			    
	    			    // Recherche des deux neurones les plus proches
	    			    vainq1  = neurones.get(0);
	    			    dist1 = 10;
	    			    num1 = 0;
	    			    vainq2  = neurones.get(1);
	    			    dist2 = 20;
	    			    num2 = 0;
	    			    
	    			    for(i = 0 ; i < neurones.size() ; i ++) {
		    				distance = neurones.get(i).distance(data.get(index));
		    				
	    				if(distance < dist1) {
	    				    dist2  = dist1;
	    				    vainq2 = vainq1;
	    				    num2   = num1;
	    		
	    				    dist1  = distance;
	    				    vainq1 = neurones.get(i);
	    				    num1   = i;
	    				} else if (distance < dist2) {
	    				    dist2  = distance;
	    				    vainq2 = neurones.get(i);
	    				    num2 = i;
	    				}
	    			    }
	    		
	    			    if (num1 == num2) {
		    				System.out.println("  [0]  Erreur Deux vainqueurs identiques");
		    				System.out.println("  \tvainqueur 1 \t neurone " +num1 +"\t distance "+ dist1);
		    				System.out.println("  \tvainqueur 2 \t neurone " +num2 +"\t distance "+ dist2);
		    				System.exit(12);
	    			    }
	    			    
	    			    // augmentation de l'erreur du neurone vainqueur
	    			    erreurs.set(num1, erreurs.get(num1) + distance); 
	    		
	    		
	    			    // Incrementation des ages des connexions de vainq1
	    			    int   indexCible;
	    			    int indexCurrent;
	    			    
	    			    for (i = 0; i < ages.get(num1).size() ; i++) {
	    				ages.get(num1).set(i, ages.get(num1).get(i).intValue()+ 1);
	    				// neurone cible de la connexion
	    				cible = vainq1.voisins.get(i);
	    				// index du neurone cible dans la liste de neurones
	    				indexCible = neurones.indexOf(cible);
	    				// index de la connexion dans le voisinage du neurone cible.
	    				indexCurrent = cible.voisins.indexOf(vainq1);
	    				if (cible.voisins.size() != ages.get(indexCible).size()) {
	    				    System.out.println("  [1] Erreur de symetrie des ages des connexions");
	    				    System.out.println(" voisins = " +  cible.voisins.size()+ "\t ages = " + ages.get(indexCible).size());
	    				    System.out.println("================================================");
	    				    editerCnx();
	    				    System.out.println("================================================");
	    		
	    				    System.exit(12);
	    				}
	    				    
	    				ArrayList<Integer> test = ages.get(indexCible);
	    				Integer testi =  ages.get(indexCible).get(indexCurrent);
	    				test.set(indexCurrent, testi.intValue()+1);
	    			    }
	    		
	    			    // mise a zero, ou creation, du lien entre les deux neurones vainqueurs
	    			    indexCible = vainq1.voisins.indexOf(vainq2);
	    			    if (indexCible == -1) { // creation de la connexion
	    				vainq1.voisins.add(vainq2);
	    				ages.get(num1).add(0);
	    				neurones.get(num2).voisins.add(vainq1);
	    				ages.get(num2).add(0);
	    			    } else { // Mise a zero de la cnx
	    				int princ = ages.get(num1).get(indexCible);
	    				ages.get(num1).set(indexCible, 0);
	    				
	    				indexCurrent = vainq2.voisins.indexOf(vainq1);
	    				int sym   = ages.get(num2).get(indexCurrent);
	    				ages.get(num2).set(indexCurrent, 0);
	    		
	    			
	    				if (sym != princ) {
	    				    System.out.println("  [12] Erreur de symetrie des ages des connexions");
	    				    
	    				    System.out.println(" exemples : "+ exemples +" \tprinc = " + princ + "\t Sym = " + sym);
	    				    System.out.println("================================================");
	    				    editerCnx();
	    				    System.out.println("================================================");
	    				    System.exit(12);		    		    		    
	    				} 
	    			    }
	    			    
	    			    // incrementation des ages des cnx du vainqueur.
	    			    for(i=0; i < vainq1.voisins.size(); i++) {
	    				cible     = vainq1.voisins.get(i);
	    				int princ = ages.get(num1).get(i);
	    				ages.get(num1).set(i, princ ++);
	    		
	    				indexCurrent = cible.voisins.indexOf(vainq1);
	    				ind          = neurones.indexOf(cible);
	    				int sym   = ages.get(ind).get(indexCurrent);
	    				ages.get(ind).set(indexCurrent, sym ++);
	    			   
	    				if (sym != princ) {
	    				    System.out.println("  [2] Erreur de symetrie des ages des connexions");
	    		
	    				    System.out.println(" exemples : "+ exemples +" \tprinc = " + princ + "\t Sym = " + sym);
	    				    System.out.println("================================================");
	    				    editerCnx();
	    				    System.out.println("================================================");
	    				    System.exit(12);
	    		
	    				    		    
	    				}		   
	    			    
	    			    }
	    			    // Approcher le vainqueur de l'exemple courant
	    			    vainq1.setPoids(txVain, data.get(index));
	    		
	    			    // Approcher les neurones voisins direct du vainqueur 
	    			    for (i = 0 ; i < vainq1.voisins.size(); i++) {
	    				vainq1.voisins.get(i).setPoids(txVois, data.get(index));
	    			    }
	    			    
	    			    // eliminer les connexion trop agees
	    			    i = 0;
	    			    while (i < neurones.size()) {
	    				cible      = neurones.get(i);
	    				k = 0;
	    				while (k < cible.voisins.size()) {
	    				    /*if (cible.voisins.size() !=  ages.get(i).size()) {
	    				      System.out.println("  [2] Erreur de symetrie des ages des connexions");
	    					
	    				      System.out.println(" neurone : "+ i +" \t = " + cible.voisins.size() + "\t Sym = " +  ages.get(i).size());
	    				      System.out.println("================================================");
	    				      editerCnx();
	    				      System.out.println("================================================");
	    				      System.exit(12);
	    		
	    				      }
	    				    */
	    				    if (ages.get(i).get(k) > agemax) {			
	    					cible.voisins.remove(k);
	    					ages.get(i).remove(k);
	    				    } else {k++;}
	    				}
	    				// eliminer les neurones sans connexions
	    				if (cible.voisins.size() == 0) {
	    				    neurones.remove(i);
	    				    ages.remove(i);
	    				    erreurs.remove(i);
	    				    //System.out.println("\n neurone " + i + " tue\n");
	    				    //editerCnx();		    
	    				} else i++;
	    			    }
	    		
	    			    // Si le temps est venu, ajouter un neurone a la plus lourde erreur
	    			    if ((exemples % nouveau) == 0) {
	    				/*editerCnx();
	    				  System.out.println("_____________________________________________________");
	    				*/
	    				
	    				// vainq 1 : neurone a erreur maximale
	    				dist1  =  erreurs.get(0);
	    				vainq1 = neurones.get(0);
	    				num1   =               0;
	    				for (i = 1 ; i < neurones.size() ; i++) {
	    				    if (erreurs.get(i) > dist1) {
	    					dist1 = erreurs.get(i);
	    					vainq1 = neurones.get(i);
	    					num1 = i;
	    				    }		    
	    				}
	    				// vainq 2 : voisin de vainq1 a erreur maximale
	    				dist2  = 0;
	    				
	    				for (i = 0 ; i < vainq1.voisins.size() ; i++) {
	    				    cible = vainq1.voisins.get(i);
	    				    if (erreurs.get(neurones.indexOf(cible)) > dist2) {
	    					num2   = neurones.indexOf(cible);
	    					dist2  =        erreurs.get(num2);
	    					vainq2 =      neurones.get(num2);
	    				    }
	    				}
	    			    
	    				// Nouveau neurone a mi-chemin entre les deux vainqueurs
	    				ind = neurones.size();
	    				cible = new Neurone(2, ind, 0, true);
	    				neurones.add(cible);
	    				
	    				
	    				ArrayList<Float> poidsN = new ArrayList<Float>();
	    				ArrayList<Float> poids1 = vainq1.getPoids();
	    				ArrayList<Float> poids2 = vainq2.getPoids();
	    		
	    				for (i = 0 ; i < poids1.size() ; i++)
	    				    poidsN.add((poids1.get(i)+poids2.get(i))/2);
	    		
	    				cible.setPoids(poidsN);
	    				
	    				// Mise a jour des erreurs des neurones
	    				erreurs.add(ind,  (dist1+dist2)/2);
	    				erreurs.set(num1, dist1  - alpha * dist1);
	    				erreurs.set(num2, dist2  - alpha * dist2);
	    				
	    		
	    				// Mise a jour des erreurs de tous les neurones.
	    				for (i = 0 ; i < neurones.size() ; i++) {
	    				    erreur = erreurs.get(i);
	    				    erreurs.set(i, erreur -beta*erreur);
	    				}
	    				
	    				// Connexion du nouveau neurone aux deux vainqueurs
	    				cible.voisins = new ArrayList<Neurone>();
	    				ages.add(ind, new ArrayList<Integer>()); 
	    				cible.voisins.add(vainq1);
	    				ages.get(ind).add(0);
	    				cible.voisins.add(vainq2);
	    				ages.get(ind).add(0);
	    		
	    				// Remplacer la connexion vainq1 -> vainq2 par vainq1 -> cible
	    				ind = vainq1.voisins.indexOf(vainq2);
	    				vainq1.voisins.set(ind, cible);
	    				ages.get(num1).set(ind, 0);
	    		
	    				// Remplacer la connexion vainq2 -> vainq1 par vainq2 -> cible
	    				ind = vainq2.voisins.indexOf(vainq1);
	    				vainq2.voisins.set(ind, cible);
	    				ages.get(num2).set(ind, 0);
	    		
	    				/*System.out.println();System.out.println();
	    				  editerCnx();
	    				  System.out.println("###################################################");
	    				*/
	    			    } //nv neurone
	    		
	    			    if ((exemples % refresh) ==0)	      	 
	    				visuel.majGr();
	    		
	    		
	    			    // TEST
	    			    /*for (i = 0 ; i < neurones.size() ; i++) {
	    				cible      = neurones.get(i);
	    				if (cible.voisins.size() !=  ages.get(i).size()) {
	    				    System.out.println("  [TEST 1] Erreur de symetrie des ages des connexions");
	    				    
	    				    System.out.println(" neurone : "+ i +" \t = " + cible.voisins.size() + "\t Sym = " +  ages.get(i).size());
	    				    System.out.println("================================================");
	    				    editerCnx();
	    				    System.out.println("================================================");
	    				    System.exit(12);
	    				    
	    				}	
	    		
	    				Neurone source;
	    				k = 0;
	    				int indSource;
	    				int indAgeCible;
	    				boolean test = false; 
	    				while (k < cible.voisins.size()) {
	    				    source = cible.voisins.get(k);
	    				    indSource= neurones.indexOf(source);
	    				    indAgeCible = source.voisins.indexOf(cible);
	    				    if (ages.get(i).get(k) != ages.get(indSource).get(indAgeCible)) {
	    					test = true;
	    					System.out.println(" [TEST 2] probleme de symetrie " + i + "<->" + indSource);
	    				    }
	    				    k++;
	    				}
	    				if (test) {
	    				    editerCnx();
	    				     System.exit(12);
	    				}
	    				}
	    			    */
	    				// TEST
	    		
	    			}
	    			visuel.majGr();	  
	    			System.out.println();
        		}
        	};

			th.start();
    }


    public void editerCnx() {	
	  int i, k;
	  for (i=0 ; i < neurones.size(); i ++) {
	    ArrayList<Neurone> voisins = neurones.get(i).voisins;
	    ArrayList<Integer> age    = ages.get(i);
	    System.out.println("\n neurone  " +i+"\t " +age.size()+" cnx \t voisins.size() " + voisins.size());
	    
	    if (age.size() != voisins.size()) {
		System.out.println("\t ages.size() = "+ age.size()+"  voisins.size()"+voisins.size());
	    } else {
		for (k = 0; k < age.size(); k++) {
		    Neurone cib = voisins.get(k);
		    int indec   = neurones.indexOf(cib);
		    int agec    = age.get(k);
		    System.out.println("\t cible :" + indec+ "\t age : "+ agec);
		}
	    }
	}	
    }



    //distance entre deux neurones (voisinage)
    private double distance(Neurone n1, Neurone currentBest) {
	return Math.pow(distance_voisins(currentBest,n1),2);
    }
    
    public double distance_voisins(Neurone currentBest,Neurone n) {
	return Math.abs(currentBest.getCol()-n.getCol())+Math.abs(currentBest.getRow()-n.getRow());
    }


}