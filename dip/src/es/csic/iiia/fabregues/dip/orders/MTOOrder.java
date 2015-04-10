package es.csic.iiia.fabregues.dip.orders;

import es.csic.iiia.fabregues.dip.board.Power;
import es.csic.iiia.fabregues.dip.board.Region;

/**
 * Move order
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class MTOOrder extends Order {

	private Region destination;
	
	public MTOOrder(Power power, Region region, Region dest) {
		super(power, region);
		destination = dest;
	}
	
	public Region getDestination(){
		return this.destination;
	}
}
