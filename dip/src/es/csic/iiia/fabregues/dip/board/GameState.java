package es.csic.iiia.fabregues.dip.board;

import java.util.List;
import java.util.Vector;

import es.csic.iiia.fabregues.dip.board.Game;
import es.csic.iiia.fabregues.dip.board.Power;
import es.csic.iiia.fabregues.dip.orders.Order;

/**
 * GameState
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class GameState {
	
	private State state;
	
	private Game game;
	private Power me;
	private List<Order> orders;
	private List<Order> myOrders;
	
	public GameState(Game game) {
		this(game, null);
	}
	
	public GameState(Game game, Power me) {
		this.state = State.EMPTY;
		this.game = game;
		this.me = me;
		this.orders = new Vector<Order>(getTotalUnits());
		this.myOrders = new Vector<Order>(getTotalUnits());
	}

	public void addOrder(Order order){
		orders.add(order);
	}
	
	public boolean isAnyOrderMissing(){
		return orders.size()<getTotalExpectedOrders();
	}
	
	public void setMyOrders(List<Order> myOrders){
		this.myOrders = myOrders;
		this.state = State.INITIALIZED;
	}
	
	public void setState(State state){
		this.state = state;
	}
	
	public State getState(){
		return state;
	}

	public Game getGame() {
		return game;
	}

	public Power getMe() {
		return me;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public List<Order> getMyOrders() {
		return myOrders;
	}
	
	//TODO moure a Game o algun lloc de dip
	private int getTotalUnits() {
		int sum = 0;
		for(Power power: game.getPowers()){
			sum += power.getControlledRegions().size();
		}
		return sum;
	}
	
	private int getTotalExpectedOrders() {
		switch (game.getPhase()) {
		case SPR:
		case FAL:
			return getTotalUnits();
		case AUT:
		case SUM:
			return game.getDislodgedRegions().size();
		case WIN:
			int sum = 0;
			for(Power power: game.getPowers()){
				sum += Math.abs(power.getOwnedSCs().size()-power.getControlledRegions().size());
			}
			return sum;

		default:
			return 0;
		}
	}


}
