package es.csic.iiia.fabregues.dip.orders;

import es.csic.iiia.fabregues.dip.board.Power;
import es.csic.iiia.fabregues.dip.board.Province;
import es.csic.iiia.fabregues.dip.board.Region;

/**
 * Support move order
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class SUPMTOOrder extends Order {

	private MTOOrder mtoOrder;
	
	
	public SUPMTOOrder(Power power, Region region, MTOOrder mtoOrder) {
		super(power, region);
		this.mtoOrder = mtoOrder;
	}

	public Region getSupportedRegion() {
		return mtoOrder.getLocation();
	}

	public Province getDestination() {
		return mtoOrder.getDestination().getProvince();
	}
	
	public MTOOrder getSupportedOrder() {
		return mtoOrder;
	}
}
