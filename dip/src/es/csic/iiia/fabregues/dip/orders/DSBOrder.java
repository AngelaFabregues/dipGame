package es.csic.iiia.fabregues.dip.orders;

import es.csic.iiia.fabregues.dip.board.Power;
import es.csic.iiia.fabregues.dip.board.Region;

/**
 * Disband order
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class DSBOrder extends Order {

	public DSBOrder(Region region, Power power) {
		super(power, region);
	}

}
