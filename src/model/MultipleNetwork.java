package model;

public class MultipleNetwork extends SpecialNetwork{

	public MultipleNetwork(BasicNetwork n) {
		super(n);
	}

	@Override
	public void learn() {
		
		int i=0;
		Options options = Options.getOptions();
		
		while(i<options.getMultidata() && !options.getStopped()){
			
			//We choose a shape for data
			int shape = (int)(Math.random() * Data.SHAPES.length);
			world.setData(Data.SHAPES[shape]);

			network.learn();
			
			i++;
		}
		
	}
}
