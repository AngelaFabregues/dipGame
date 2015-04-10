package es.csic.iiia.fabregues.dip.board;

import java.util.Vector;

/**
 * Region
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class Region {

	private String name;
	private Province province;
	private Vector<Region> adjacentRegions;
	
	public Region(String name) {
		adjacentRegions = new Vector<Region>();
		this.name = name;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public String getName() {
		return name;
	}

	public void addAdjacentRegion(Region adjNode) {
		adjacentRegions.add(adjNode);
	}

	public Vector<Region> getAdjacentRegions() {
		return adjacentRegions;
	}
	
	public String toString() {
		return name;
	}
	
	public boolean equals(Object o){
		if(o instanceof Region){
			Region region = (Region) o;
			return name.equals(region.getName());
		}
		return super.equals(o);
	}
}
