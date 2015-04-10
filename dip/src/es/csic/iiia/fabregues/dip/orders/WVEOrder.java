package es.csic.iiia.fabregues.dip.orders;

import es.csic.iiia.fabregues.dip.board.Power;

/**
 * Waive order
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class WVEOrder extends Order {

	public WVEOrder(Power power) {
		super(power, null);
		this.power = power;
	}

}
