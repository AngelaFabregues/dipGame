package es.csic.iiia.fabregues.dip.orders;

import es.csic.iiia.fabregues.dip.board.Power;
import es.csic.iiia.fabregues.dip.board.Region;

/**
 * Retreate to order
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class RTOOrder extends Order {

	private Region destination;
	
	public RTOOrder(Region region, Power power, Region destination) {
		super(power, region);
		this.destination = destination;
	}
	
	public Region getDestination(){
		return this.destination;
	}

}
