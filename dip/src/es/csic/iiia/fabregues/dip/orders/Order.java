package es.csic.iiia.fabregues.dip.orders;

import es.csic.iiia.fabregues.dip.board.Power;
import es.csic.iiia.fabregues.dip.board.Region;
import es.csic.iiia.fabregues.dip.comm.Order2StringA;
import es.csic.iiia.fabregues.utilities.Interface;
import es.csic.iiia.fabregues.utilities.Valuable;

/**
 * Order
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public abstract class Order implements Valuable<Float>{
	
	protected Region region;
	protected Power power;
	
	private Float orderValue = 0F;
	private String result;
	
	public Order(Power power, Region region){
		this.region = region;
		this.power = power;
	}
	
	@Override
	public int compareTo(Valuable<Float> o) {
		return getValue().compareTo(o.getValue());
	}
	
	public Region getLocation(){
		return region;
	}

	public Float getValue() {
		return orderValue;
	}

	public void setOrderValue(Float orderValue) {
		this.orderValue = orderValue;
	}

	public void setResult(String result){
		this.result = result;
	}

	public String getResult(){
		return this.result;
	}
	
	public Power getPower(){
		return power;
	}
	
	public boolean equals(Object order2){
		if(order2 instanceof Order){
			return order2.toString().equals(toString());
		}
		return false;
	}
	
	public String toString(){
		return Interface.arrToString(Order2StringA.getOrderMsg(this));
	}
}
