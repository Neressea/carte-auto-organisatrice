package model;

import java.util.ArrayList;
/*
  algo : http://www.neuroinformatik.ruhr-uni-bochum.de/VDM/research/gsn/JavaPaper/node19.html
*/

public class GNG extends AbstractNetwork {

    private ArrayList<Double> erreurs;
    private ArrayList<ArrayList<Integer>> ages;// age des connexion par neurons

    private int max       =  100;   
    private int nouveau   =  300; // nb iteration avant creation
    private int agemax    =  40;
    private double txVain = .05;
    private double txVois = .00006;
    private double alpha  = .5;
    private double beta   = .0005;

    public GNG (Environment h) {
		super(h);

		neurons.add(new ArrayList<Neuron>());
		
		this.erreurs = new ArrayList<Double>();
		this.ages    = new ArrayList<ArrayList<Integer>>();
    }
    
    @Override
    public String toString(){
    	return "GNG";
    }
    
    @Override
    public void fill(){
    	nb_neurons = Options.getOptions().getNbDep();
    	
    	for(int i = 0 ; i < nb_neurons ; i++) {
		    neurons.get(0).add(new Neuron(2, i, 0, true));	    
		    erreurs.add(i,0.0);
		}
		
		// connexion entre les neurons de depart et age des connexions a 0
		for(int i = 0 ; i < nb_neurons ; i++) {
			ArrayList<Neuron> neighbors = new ArrayList<Neuron>();
		    ages.add(i, new ArrayList<Integer>()); 
		    for(int j = 0 ; j < nb_neurons; j ++) {
				if (i!= j) {
				    neighbors.add(neurons.get(0).get(j));
				    ages.get(i).add(0);
				}
		    }
		    
		    neurons.get(0).get(i).setNeighbors(neighbors);
		}	
    }
    	
	@Override
	public void run(){
		int           exemples;
		Neuron vainq1, vainq2;
		double    dist1, dist2;
		int         num1, num2;
		double        distance = 0;
		int                ind;
		Neuron          cible;
		int            i, k;
		int              index;
		double          erreur;
		int           creation;
		
		exemples = 0;
		creation = max + neurons.get(0).size();
	
		while (( neurons.get(0).size() < creation) && (exemples < epochs) && !Options.getOptions().getStopped())  {
			
			//We check the pause
			synchronized (holder) {
				while(Options.getOptions().getPaused() && !Options.getOptions().getStopped()){
					try {
						holder.wait();
						
						if(Options.getOptions().getStepped()){
							
							//If we "step", we go out for one round
							Options.getOptions().setStep(false);
							break;
						}
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
			
		    ind = 0;
		    for(i = 0 ; i < neurons.get(0).size() ; i ++) {
		    	ind = ind + ages.get(i).size();
		    }
		    System.out.printf(" GNG \t %5d sur %5d \t %3d neurons sur %5d \t %4d connexions \r", exemples, epochs, neurons.size(), creation, (int)(ind/2)); 
		    exemples ++;
		    
	
		    // Tirage de l'exemple courant
		    index = (int) (Math.random()*Data.getData().size());
		    
		    // Recherche des deux neurons les plus proches
		    vainq1  = neurons.get(0).get(0);
		    dist1 = 10;
		    num1 = 0;
		    vainq2  = neurons.get(0).get(1);
		    dist2 = 20;
		    num2 = 0;
		    
		    for(i = 0 ; i < neurons.get(0).size() ; i ++) {
				distance = neurons.get(0).get(i).distance(Data.getData().get(index));
				
			if(distance < dist1) {
			    dist2  = dist1;
			    vainq2 = vainq1;
			    num2   = num1;
	
			    dist1  = distance;
			    vainq1 = neurons.get(0).get(i);
			    num1   = i;
			} else if (distance < dist2) {
			    dist2  = distance;
			    vainq2 = neurons.get(0).get(i);
			    num2 = i;
			}
		    }
	
		    // augmentation de l'erreur du Neuron vainqueur
		    erreurs.set(num1, erreurs.get(num1) + distance); 
	
		    // Incrementation des ages des connexions de vainq1
		    int   indexCible;
		    int indexCurrent;
		    
		    for (i = 0; i < ages.get(num1).size() ; i++) {
				ages.get(num1).set(i, ages.get(num1).get(i).intValue()+ 1);
				// Neuron cible de la connexion
				cible = vainq1.getNeighbors().get(i);
				// index du Neuron cible dans la liste de neurons
				indexCible = neurons.get(0).indexOf(cible);
				// index de la connexion dans le voisinage du Neuron cible.
				indexCurrent = cible.getNeighbors().indexOf(vainq1);
				    
				ArrayList<Integer> test = ages.get(indexCible);
				Integer testi =  ages.get(indexCible).get(indexCurrent);
				test.set(indexCurrent, testi.intValue()+1);
		    }
	
		    // mise a zero, ou creation, du lien entre les deux neurons vainqueurs
		    indexCible = vainq1.getNeighbors().indexOf(vainq2);
		    if (indexCible == -1) { // creation de la connexion
				vainq1.getNeighbors().add(vainq2);
				ages.get(num1).add(0);
				neurons.get(0).get(num2).getNeighbors().add(vainq1);
				ages.get(num2).add(0);
		    } else { 
				ages.get(num1).set(indexCible, 0);
				
				indexCurrent = vainq2.getNeighbors().indexOf(vainq1);
				ages.get(num2).set(indexCurrent, 0);
	
		    }
		    
		    // incrementation des ages des cnx du vainqueur.
		    for(i=0; i < vainq1.getNeighbors().size(); i++) {
				cible     = vainq1.getNeighbors().get(i);
				int princ = ages.get(num1).get(i);
				ages.get(num1).set(i, princ ++);
		
				indexCurrent = cible.getNeighbors().indexOf(vainq1);
				ind          = neurons.get(0).indexOf(cible);
				int sym   = ages.get(ind).get(indexCurrent);
				ages.get(ind).set(indexCurrent, sym ++);   
			    
		    }
		    // Approcher le vainqueur de l'exemple courant
		    vainq1.setPoids(txVain, Data.getData().get(index));
	
		    // Approcher les neurons getNeighbor direct du vainqueur 
		    for (i = 0 ; i < vainq1.getNeighbors().size(); i++) {
			vainq1.getNeighbors().get(i).setPoids(txVois, Data.getData().get(index));
		    }
		    
		    // eliminer les connexion trop agees
		    i = 0;
		    while (i < neurons.get(0).size()) {
				cible      = neurons.get(0).get(i);
				k = 0;
				while (k < cible.getNeighbors().size()) {
				   
				    if (ages.get(i).get(k) > agemax) {			
					cible.getNeighbors().remove(k);
					ages.get(i).remove(k);
				    } else {k++;}
				}
				// eliminer les neurons sans connexions
				if (cible.getNeighbors().size() == 0) {
				    neurons.get(0).remove(i);
				    ages.remove(i);
				    erreurs.remove(i);
				    ////System.out.println("\n Neuron " + i + " tue\n");
				    //editerCnx();		    
				} else i++;
		    }
	
		    // Si le temps est venu, ajouter un Neuron a la plus lourde erreur
		    if ((exemples % nouveau) == 0) {
				
				// vainq 1 : Neuron a erreur maximale
				dist1  =  erreurs.get(0);
				vainq1 = neurons.get(0).get(0);
				num1   =               0;
				for (i = 1 ; i < neurons.get(0).size() ; i++) {
				    if (erreurs.get(i) > dist1) {
					dist1 = erreurs.get(i);
					vainq1 = neurons.get(0).get(i);
					num1 = i;
				    }		    
				}
				// vainq 2 : voisin de vainq1 a erreur maximale
				dist2  = 0;
				
				for (i = 0 ; i < vainq1.getNeighbors().size() ; i++) {
				    cible = vainq1.getNeighbors().get(i);
				    if (erreurs.get(neurons.get(0).indexOf(cible)) > dist2) {
						num2   = neurons.get(0).indexOf(cible);
						dist2  =        erreurs.get(num2);
						vainq2 =      neurons.get(0).get(num2);
				    }
				}
			    
				// Nouveau Neuron a mi-chemin entre les deux vainqueurs
				ind = neurons.get(0).size();
				cible = new Neuron(2, ind, 0, true);
				neurons.get(0).add(cible);
				
				
				ArrayList<Float> poidsN = new ArrayList<Float>();
				ArrayList<Float> poids1 = vainq1.getPoids();
				ArrayList<Float> poids2 = vainq2.getPoids();
		
				for (i = 0 ; i < poids1.size() ; i++)
				    poidsN.add((poids1.get(i)+poids2.get(i))/2);
		
				cible.setPoids(poidsN);
				
				// Mise a jour des erreurs des neurons
				erreurs.add(ind,  (dist1+dist2)/2);
				erreurs.set(num1, dist1  - alpha * dist1);
				erreurs.set(num2, dist2  - alpha * dist2);
				
		
				// Mise a jour des erreurs de tous les neurons.
				for (i = 0 ; i < neurons.get(0).size() ; i++) {
				    erreur = erreurs.get(i);
				    erreurs.set(i, erreur -beta*erreur);
				}
				
				// Connexion du nouveau Neuron aux deux vainqueurs
				cible.setNeighbors(new ArrayList<Neuron>());
				ages.add(ind, new ArrayList<Integer>()); 
				cible.getNeighbors().add(vainq1);
				ages.get(ind).add(0);
				cible.getNeighbors().add(vainq2);
				ages.get(ind).add(0);
		
				// Remplacer la connexion vainq1 -> vainq2 par vainq1 -> cible
				ind = vainq1.getNeighbors().indexOf(vainq2);
				vainq1.getNeighbors().set(ind, cible);
				ages.get(num1).set(ind, 0);
		
				// Remplacer la connexion vainq2 -> vainq1 par vainq2 -> cible
				ind = vainq2.getNeighbors().indexOf(vainq1);
				vainq2.getNeighbors().set(ind, cible);
				ages.get(num2).set(ind, 0);

		    } //nv Neuron
		    
		    //We wait a little because it's too fast in other cases
		    try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		    holder.change();
		}
		
		Options.getOptions().setStopped(true);
		holder.change();
	}
}