package es.csic.iiia.fabregues.dip.board;

import java.util.Vector;

/**
 * Stores the dislodgement information that is the list of
 * units that are dislodged and where they can be retreated to.
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class Dislodgement {
	private Power power;
	private Region region;
	private Vector<Region> retreateTo;
	
	public Dislodgement(Power power, Region region){
		this.power = power;
		this.region = region;
		this.retreateTo = new Vector<Region>();
	}
	
	public void addRetreateToRegion(Region region){
		retreateTo.add(region);
	}

	public Power getPower() {
		return power;
	}

	public Vector<Region> getRetreateTo() {
		return retreateTo;
	}
	
	public Region getRegion(){
		return region;
	}
}
