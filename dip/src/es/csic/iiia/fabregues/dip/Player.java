package es.csic.iiia.fabregues.dip;

import java.io.IOException;
import java.util.List;

import es.csic.iiia.fabregues.dip.board.Phase;
import es.csic.iiia.fabregues.dip.board.Power;
import es.csic.iiia.fabregues.dip.comm.Comm;
import es.csic.iiia.fabregues.dip.comm.IComm;
import es.csic.iiia.fabregues.dip.comm.MessageBuilder;
import es.csic.iiia.fabregues.dip.comm.CommException;
import es.csic.iiia.fabregues.dip.comm.Parser;
import es.csic.iiia.fabregues.dip.orders.Order;
import es.csic.iiia.fabregues.utilities.Interface;

/**
 * Player of the game.
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public abstract class Player extends Observer{

	protected Power me;
	private boolean dead = false;
	
	public Power getMe() {
		return me;
	}

	protected int passcode;
	private String powerName; //necessary for reconnection process

	/**
	 * Player constructor
	 */
	public Player() {
		super();
	}
	
	public Player(String logPath){
		super(logPath);
	}
	
	/**
	 * Player constructor for reconnection
	 * @param ip of the server
	 * @param port of the server
	 * @param name of the player
	 * @param powerName performed by the player in the previous connection
	 * @param password of the player in the previous connection
	 * @throws CommException 
	 * @throws CommException 
	 * @throws IOException 
	 * @throws CommException 
	 */
	public void restart(IComm commImpl, String powerName, String password) throws CommException{
		log = new Interface("dip_"+name);
		restart(commImpl, powerName, password, log);
	}
	
	private void restart(IComm commImpl, String powerName, String password, Interface log) throws CommException{
		this.name = commImpl.getName();
		this.log = log;
		comm = new Comm(commImpl, this);
		//comm.start();
		comm.restart(powerName, password);
	}

	/**
	 * confirms the reconnection
	 * @param powerName
	 * @param passcode
	 */
	public void reconnectionConfirmation(String powerName, int passcode) {
		this.powerName = powerName;
		this.passcode = passcode;
	}
	
	/**
	 * confirms the map
	 * @throws CommException 
	 */
	public void mapConfirmation() throws CommException {
		if(powerName!=null){ //reconnecting
			me = game.getPower(powerName);
			comm.sendMessage(MessageBuilder.fill(MessageBuilder.MSG_SCO, null));
			comm.sendMessage(MessageBuilder.fill(MessageBuilder.MSG_ORD, null));
			comm.sendMessage(MessageBuilder.fill(MessageBuilder.MSG_NOW, null));
		}else{
			super.mapConfirmation();
		}
	}
	
	/**
	 * starts playing
	 * @param power
	 * @param passcode
	 * @param pressLevel
	 */
	public void startPlaying(String power, int passcode, int pressLevel) {
		this.me = game.getPower(power);
		this.passcode = passcode;
		this.pressLevel = pressLevel;
	}

	/**
	 * plays and sends the orders to the server
	 * @throws CommException 
	 */
	public void beforeNewPhase() throws CommException {
		if(dead){
			return;
		}
		if(game.getYear()==1901 && game.getPhase().equals(Phase.SPR)){
			start();
		}else if(super.winner!=null){
			return;
		}
		if(game.isDead(me)){
			dead = true;
		}
		
		List<Order> orders = play();
		if(orders != null){
			sendOrders(orders);
		}
	}

	/**
	 * sends all orders to the server
	 * @param orders
	 * @throws CommException 
	 */
	public void sendOrders(List<Order> orders) throws CommException {
		if(orders.size()==0){
			return;
		}
		comm.getGameState().setMyOrders(orders);
		comm.sendMessage(Parser.formatOrders(orders));
	}
	
	/**
	 * ends the game when someone has won.
	 * @param winner
	 */
	public void handleSlo(String winner) {
		super.handleSlo(winner);
		if (winner.equals(me.getName())) {
			log.printMessage("WE WON.");
		}else{
			log.printMessage("WE LOSE.");
		}		
	}
	
	/**
	 * does something afterOldPhase
	 */
	public void afterOldPhase(){
	}
		
	/**
	 * plays selecting the orders to write
	 * if returned orders == null, orders are not send. Then you should send them using sendOrders()
	 * @return list of orders
	 */
	public abstract List<Order> play();
	
	/**
	 * starts the player when the powers are assigned 
	 */
	public abstract void start();

	public void setMe(String power) {
		me = game.getPower(power);
	}
	
	public void submissionError(String[] message) {
		log.printError(message);
	}

	public void missingOrder(String[] message) {
		log.printError(message);
	}	
	
	public boolean isDead(){
		return dead;
	}
}