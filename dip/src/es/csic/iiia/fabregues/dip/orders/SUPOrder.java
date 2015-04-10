package es.csic.iiia.fabregues.dip.orders;

import es.csic.iiia.fabregues.dip.board.Power;
import es.csic.iiia.fabregues.dip.board.Region;

/**
 * Support hold order
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class SUPOrder extends Order {
	
	private Order supportedOrder;
	
	public SUPOrder(Power power, Region region, Order supportedOrder) {
		super(power, region);
		this.supportedOrder = supportedOrder;
		if(supportedOrder instanceof MTOOrder){
			//throw new Exception("Invalid Support");//TODO
		}
	}

	public Region getSupportedRegion() {
		return supportedOrder.getLocation();
	}
	
	public Order getSupportedOrder() {
		return supportedOrder;
	}
}
