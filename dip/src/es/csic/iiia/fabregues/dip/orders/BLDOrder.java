package es.csic.iiia.fabregues.dip.orders;

import es.csic.iiia.fabregues.dip.board.Power;
import es.csic.iiia.fabregues.dip.board.Region;

/**
 * Build order
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class BLDOrder extends Order{

	public BLDOrder(Power power, Region region){
		super(power, null);
		this.power = power;
		super.region = region;
	}
	
	public Region getDestination(){
		return region;
	}
	
	public void setDestination(Region destination){
		this.region = destination;
	}
}
