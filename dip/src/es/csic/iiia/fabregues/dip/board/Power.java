package es.csic.iiia.fabregues.dip.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Power
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class Power {
	
	private String name;
	
	private List<Province> own;
	private List<Province> homes;
	private List<Region> control;
	
	public Power(String name) {
		this.name = name;

		resetControl();
		
		resetOwn();
		homes = new ArrayList<Province>();
		
	}
	
	public String getName() {
		return name;
	}
	
	public void addControlledRegion(Region region){
		control.add(region);
	}
	
	public List<Region> getControlledRegions() {
		return control;
	}
	
	public boolean isControlling(Region region){
		return control.contains(region);
	}
	
	/**
	 * Returns true if the province is a SC owned by the power or false otherwise.
	 * @param Province
	 * @return
	 */
	public boolean isOwning(Province Province){
		if(Province instanceof Province){
			return own.contains((Province)Province);
		}
		return false;
	}

	public List<Province> getOwnedSCs() {
		return own;
	}
	
	public void resetControl() {
		control = new Vector<Region>();
	}

	public void addOwn(Province sc) {
		own.add(sc);
	}
	
	public void resetOwn() {
		own = new Vector<Province>();
	}
	
	public void addHome(Province province) {
		homes.add(province);
	}

	public List<Province> getHomes() {
		return homes;
	}
	
	public String toString(){
		return name;
	}

	public boolean isControlling(Province province) {
		for(Region region: province.getRegions()){
			if(isControlling(region)){
				return true;
			}
		}
		return false;
	}
	
	public boolean equals(Object o){
		if(o instanceof Power){
			Power power = (Power) o;
			return name.equals(power.getName());
		}
		return super.equals(o);
	}
}
