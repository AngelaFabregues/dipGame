package es.csic.iiia.fabregues.dip.board;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

/**
 * Stores information about the game. That is: the board (provinces, regions, ...),
 * the powers and the time (phase and year).
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class Game {

	private Hashtable<String, Province> provinces;
	private Hashtable<String, Power> powers;
	private Hashtable<String, Region> regions;

	private int year;
	private Phase phase;

	private HashMap<Region,Dislodgement> dislodgedRegions;
	
	private List<Power> nonDeadPowers;
	
	public Game() {
		year = 1901;
		phase = Phase.SPR;
	}
	
	public Game(Hashtable<String,Power> powers, Hashtable<String,Province> provinces, Hashtable<String,Region> regions){
		this();
		this.powers = powers;
		this.provinces = provinces;
		this.regions = regions;
		
		this.nonDeadPowers = new Vector<Power>(powers.values());
	}

	public void addDislodgedRegion(Region region, Dislodgement dislodgement){
		dislodgedRegions.put(region, dislodgement);
	}
	
	public Region getRegion(String name){
		return regions.get(name.toUpperCase());
	}
	
	public Region getRegion(Province province, String type){
		return regions.get(province.getName()+type);
	}
	
	public Vector<Region> getRegions(){
		return new Vector<Region>(regions.values());
	}

	public List<Power> getPowers(){
		return new Vector<Power>(powers.values());
	}
	
	public Power getPower(String powerString) {
		return  powers.get(powerString.toUpperCase());
	}
	
	public Vector<Province> getProvinces() {
		return new Vector<Province>(provinces.values());
	}

	public Province getProvince(String string) {
		return provinces.get(string.toUpperCase());
	}

	public HashMap<Region, Dislodgement> getDislodgedRegions() {
		return dislodgedRegions;
	}
	
	public List<Region> getDislodgedRegions(Power power) {
		List<Region> dislodged = new Vector<Region>();
		for(Dislodgement d: dislodgedRegions.values()){
			if(d.getPower().equals(power)){
				dislodged.add(d.getRegion());
			}
		}
		return dislodged;
	}

	
	public void resetDislodgedRegions(){
		dislodgedRegions = new HashMap<Region, Dislodgement>();
	}
	
	public Phase getPhase(){
		return phase;
	}
	
	public void setPhase(Phase phase){
		this.phase = phase;
	}
	
	public int getYear(){
		return year;
	}

	public void setYear(String year) {
		setYear(Integer.valueOf(year));		
	}
	
	public void setYear(int year) {
		this.year = year;		
	}
	
	/**
	 * Retorns supplycenter owner 
	 * @param supplyCenter
	 * @return
	 */
	public Power getOwner(Province supplyCenter){
		if(!supplyCenter.isSC()){
			return null;
		}
		for(Power power: powers.values()){
			if(power.getOwnedSCs().contains(supplyCenter)){
				return power;
			}
		}
		return null;
	}
	
	/**
	 * Retorns region controller 
	 * @param region
	 * @return
	 */
	public Power getController(Region region){
		for(Power power: powers.values()){
			if(power.getControlledRegions().contains(region)){
				return power;
			}
		}
		return null;
	}
	
	/**
	 * Returns the power that controls one of the province regions. If no one is controlled, returns null.
	 * @param province
	 * @return
	 */
	public Power getController(Province province){
		Power power;
		for(Region region: province.getRegions()){
			power = getController(region);
			if(power != null){
				return power;
			}
		}
		return null;
	}
	
	/**
	 * Retorna la regi� adjacent de adjacentFrom que 
	 * @param province where
	 * @param adjacentFrom moving from
	 * @return
	 */
	public Region getAdjacentRegionIn(Province province, Region adjacentFrom) {
		if(!adjacentFrom.getName().endsWith("AMY")){//si �s una flota la que es mou 
			for(Region region: adjacentFrom.getAdjacentRegions()){
				if(region.getProvince().equals(province)){
					return region;
				}
			}
		}else{ //si �s una armada la que es mou
			return getRegion(province.getName()+"AMY");
		}
		return null;
	}
	
	public List<Power> getNonDeadPowers(){
		return nonDeadPowers;
	}
	
	public boolean isDead(Power power){
		return !nonDeadPowers.contains(power);
	}
	
	public void killPower(Power power){
		nonDeadPowers.remove(power);
	}
	
	/**
	 * Gets units located in adjacent provinces to province
	 * Gets units that can move into province
	 */
	public List<Region> getAdjacentUnits(Province province){
		List<Region> units = new Vector<Region>();
		for(Region region: province.getRegions()){
			for(Region unit: region.getAdjacentRegions()){
				if(!units.contains(unit)){
					if(getController(unit)!=null){
						if(!Collections.disjoint(unit.getAdjacentRegions(), province.getRegions())){
							units.add(unit);
						}
					}
				}
			}
		}
		return units;
	}
	
	/**
	 * Gets adjacent provinces
	 * @param game
	 * @param province
	 * @return
	 */
	public List<Province> getAdjacentProvinces(Province province){
		List<Province> adjProvinces = new Vector<Province>();
		for(Region region: province.getRegions()){
			for(Region adj: region.getAdjacentRegions()){
				Province adjProvince = adj.getProvince();
				if(!adjProvinces.contains(adjProvince)){
					adjProvinces.add(adjProvince);
				}
			}
		}
		return adjProvinces;
	}
	
	/**
	 * Gets the list of owned but unoccupied homes of a power
	 * @param power
	 * @return
	 */
	public List<Region> getBuildHomeList(Power power) {
		List<Region> homeRegions = new Vector<Region>();
		for (Province province : power.getHomes()) {
			if (power.isOwning(province) && getController(province) == null) {
				homeRegions.addAll(province.getRegions());
			}
		}
		return homeRegions;
	}


}
