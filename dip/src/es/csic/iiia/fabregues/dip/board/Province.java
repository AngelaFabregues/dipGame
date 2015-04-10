package es.csic.iiia.fabregues.dip.board;

import java.util.Vector;

/**
 * Province
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class Province{

	protected String name;
	protected Vector<Region> regions;
	private boolean isSC;
	private Float provinceValue; 	//TODO treure-ho d'aqui
	
	public Province(String name) {
		this.name = name;
		this.regions = new Vector<Region>();
		isSC = false;
	}
	
	public void addRegion(Region region) {
		regions.add(region);
	}

	public String getName() {
		return name;
	}
	
	public boolean isSC() {
		return isSC;
	}
	
	public Vector<Region> getRegions(){
		return regions;
	}

	public void markAsSC() {
		isSC = true;
	}

	public Float getValue() {
		return provinceValue;
	}

	public void setValue(Float orderValue) {
		this.provinceValue = orderValue;
	}
	
	public String toString(){
		return name;
	}
	
}
