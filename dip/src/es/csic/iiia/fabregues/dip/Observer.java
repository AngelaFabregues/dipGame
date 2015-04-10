package es.csic.iiia.fabregues.dip;

import java.io.IOException;

import es.csic.iiia.fabregues.dip.board.Game;
import es.csic.iiia.fabregues.dip.board.GameState;
import es.csic.iiia.fabregues.dip.comm.Comm;
import es.csic.iiia.fabregues.dip.comm.GameBuilder;
import es.csic.iiia.fabregues.dip.comm.IComm;
import es.csic.iiia.fabregues.dip.comm.MessageBuilder;
import es.csic.iiia.fabregues.dip.comm.CommException;
import es.csic.iiia.fabregues.dip.comm.Parser;
import es.csic.iiia.fabregues.dip.orders.Order;
import es.csic.iiia.fabregues.utilities.Interface;

/**
 * Observer of the game. It cannot play.
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public abstract class Observer{

	protected Comm comm;
	protected Game game;
	
	protected String mapName;
	protected String name="client";
	protected String version="0";
	
	protected int pressLevel;
	
	public Interface log;
	public String logPath="";
	
	public String winner;

	/**
	 * Observer constructor
	 */
	public Observer() {
		winner = null;
	}
	
	public Observer(String logPath) {
		this();
		//checking that the path is correctly introduced
		logPath = logPath.trim();
		if(logPath!=null && logPath.length()>0){
			if(logPath.charAt(logPath.length()-1) != '/'){
				logPath +="/";
			}
		}
		this.logPath = logPath;
	}
	
	/**
	 * Observer start
	 * 
	 * @param ip
	 * @param port
	 * @param name
	 * @throws IOException
	 * @throws CommException
	 * @throws CommException
	 */
	public void start(IComm comm) throws CommException{
		Interface log = new Interface(logPath+"dip_"+name);
		start(comm, log);
	}
	
	/**
	 * Observer start reusing log file
	 * 
	 * @param ip of the server
	 * @param port of the server
	 * @param name of the observer
	 * @param log file
	 * @throws CommException 
	 * @throws IOException 
	 * @throws CommException 
	 */
	public void start(IComm commImpl, Interface log) throws CommException{
		this.name = commImpl.getName();
		this.log = log;
		comm = new Comm(commImpl, this);
		comm.start();
	}
	
	/**
	 * saves the name of the map. If the map is the standard, it builds the game.
	 * Otherwise, it requests a map definition, MDF.
	 * 
	 * @param map
	 * @throws CommException 
	 */
	public void setMap(String map) throws CommException {
		this.mapName = map;
		if(mapName.equals(GameBuilder.DEFAULT_MAP)){
			GameBuilder mdf = Parser.createGameBuilder();
			this.game = mdf.getGame();
			mapConfirmation();
		}else{
			this.comm.sendMessage(MessageBuilder.MSG_MDF);
		}
	}

	/**
	 * ends the game when someone has won.
	 * 
	 * @param winner
	 */
	public void handleSlo(String winner) {
		this.winner = winner;
		log.printMessage("The game is over !. " + winner + " won.");
	}
	
	/**
	 * ends the game when the server is off
	 */
	public void handleServerOFF(){
		log.printMessage("The server is off, sorry.");
		exit();
	}
	
	/**
	 * ends the game
	 */
	public void exit(){
		log.close();
	}
	
	public void mapConfirmation() throws CommException {
		this.comm.sendMessage(MessageBuilder.fill(MessageBuilder.MSG_YES_MAP, new String[] { this.mapName }));
	}
	
	/**
	 * does something before the game has started
	 */
	public abstract void init();

	/**
	 * does something with every received last phase written order
	 */
	public abstract void receivedOrder(Order order);

	/**
	 * Does something when the phase ends and before a new one starts
	 * @deprecated use phaseEnd instead
	 */
	public abstract void afterOldPhase();
		
	/**
	 * does something when the phase starts
	 */
	public abstract void beforeNewPhase() throws CommException;
	
	public void setGame(Game game){
		this.game = game;
	}
	
	public Game getGame(){
		return game;
	}
	
	public String getName(){
		return this.name;
	}

	public String getVersion(){
		return this.version;
	}

	public void handleSMR(String[] message) {

	}

	/**
	 * Does something when the phase ends and before a new one starts
	 * @param gameState
	 */
	public void phaseEnd(GameState gameState) {
		
	}

	public void handleCCD(String string) {
		
	}

}