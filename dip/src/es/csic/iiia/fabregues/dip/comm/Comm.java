package es.csic.iiia.fabregues.dip.comm;

import java.io.IOException;
import java.util.List;

import es.csic.iiia.fabregues.dip.Observer;
import es.csic.iiia.fabregues.dip.Player;
import es.csic.iiia.fabregues.dip.board.Game;
import es.csic.iiia.fabregues.dip.board.GameState;
import es.csic.iiia.fabregues.dip.board.Power;
import es.csic.iiia.fabregues.dip.board.State;
import es.csic.iiia.fabregues.dip.orders.Order;
import es.csic.iiia.fabregues.utilities.Interface;

/**
 * Deals with high level communication (String[]) from the DAIDE language
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class Comm {

	private Observer client;
	private GameState gameState;
	private IComm commImpl;
	
	/**
	 * Communication. Notice that an IComm like DaideComm is necessary to stablish communication.
	 * @param game manager ip
	 * @param game manager port
	 * @param client name
	 * @param client reference
	 * @throws CommException 
	 * @throws IOException 
	 */
	public Comm(IComm commImpl, Observer client){
		this.commImpl = commImpl;
		this.client = client;
	}
	
	/**
	 * Connecting to a new game as either a player or an observer
	 * @throws CommException 
	 * @throws StartingCommException 
	 */
	public void start() throws CommException{
		commImpl.init(this, client.log);
		if(client instanceof Player){
			// Requesting to join the game as a player
			sendMessage(MessageBuilder.fill(MessageBuilder.MSG_NME, new String[] {client.getName(), client.getVersion()}));
		}else{
			// Requesting to join the game as an observer
			sendMessage(MessageBuilder.fill(MessageBuilder.MSG_OBS, new String[] {}));
		}
	}

	/**
	 * Reconnecting to a game as the player with the following powerName and passcode
	 * @param powerName
	 * @param passcode
	 * @throws CommException 
	 */
	public void restart(String powerName, String passcode) throws CommException{
		try{
			commImpl.init(this, client.log);
			sendMessage(MessageBuilder.fill(MessageBuilder.MSG_IAM, new String[] {powerName, passcode}));
		}catch (StartingCommException e) {
			client.log.printError("Starting communication failed.");
		}
	}

	/**
	 * Sends a message to the game manager
	 * @param msg
	 * @throws CommException 
	 */
	public void sendMessage(String msg[]) throws CommException {
		client.log.printMessage(Interface.sentMessage(msg));
		commImpl.sendMessage(msg);
	}
	
	/**
	 * Receives a message from the game manager
	 */
	public void handleReceivedMessage(String[] message) {
		client.log.printMessage(Interface.receivedMessage(message));
		try{
			if (message[0].equals("YES")){
				handleYES(message);
			} else if (message[0].equals("REJ")){
				handleREJ(message);
			}else if (message[0].equals("MAP")){
				handleMAP(message);
			} else if(message[0].equals("MDF")) {
				handleMDF(message);
			} else if (message[0].equals("HLO")) {
				handleHLO(message);
			} else if (message[0].equals("SCO")) {
				handleSCO(message);
			} else if (message[0].equals("NOW")) {
				handleNOW(message);
			} else if (message[0].equals("THX")){
				handleTHX(message);
			} else if (message[0].equals("MIS")) {
				handleMIS(message);
			} else if (message[0].equals("ORD")) {
				handleORD(message);
			} else if(message[0].equals("OFF")) {
				handleOFF(message);
			} else if(message[0].equals("PRN")) {
				handlePRN(message);
			} else if(message[0].equals("HUH")) {
				handleHUH(message);
			} else if (message[0].equals("OUT")) {
				handleOUT(message);
			} else if (message[0].equals("SLO")) {
				handleSLO(message);
			} else if(message[0].equals("SMR")){
				handleSMR(message);
			} else if(message[0].equals("CCD")){
				handleCCD(message);
			} else {
				handleOtherMessages(message);
			}
		}catch (CommException e) {
			client.log.printError("No longer connected to server. Exiting.");
			client.exit();
		}
	}

	/**
	 * MAP('name')
	 * Specify the map which is to be used: 'standard'
	 * @param message
	 * @throws CommException 
	 */
	private void handleMAP(String[] message) throws CommException {
		client.setMap(message[2]);
	}

	/**
	 * MDF ((powers)(provinces)(adjacencies))
	 * Map definition sent in response of MDF call
	 * @param message
	 * @throws CommException 
	 */
	private void handleMDF(String[] message) throws CommException {		
		GameBuilder mdf = Parser.createGameBuilder(message);
		client.setGame(mdf.getGame());
		client.mapConfirmation();
	}

	/**
	 * HLO ((power)(passcode)(variant))
	 * power, password and press level are aknowledged by the server to the AI.
	 * @param message
	 */
	private void handleHLO(String[] message) {
		if(client instanceof Player){
			if(message[2].equals("UNO")){
				client.log.printMessage("The game manager does not lead us connect as a player.");
				return;
			}
			((Player)client).startPlaying(message[2], Integer.parseInt(message[5]), Integer.parseInt(message[10]));
		}
		client.log.resetTime();
		client.init();
	}

	/**
	 * SCO (power centre centre...)(power centre centre...)...
	 * Server sends it to each AI at the start of the game and after each Fall Retreat turn (or Fall Movement turn if there are no retreats). It indicates the current supply centre ownership. Unowned centres are listed against the power name UNO.
	 * @param message
	 */
	private void handleSCO(String[] message) {
		Game game = client.getGame();
		waitsForOrders(game);
		
		//updatesSCs
		Parser.updateOwnedSCs(message, game);
		
		//update the non dead list of powers
		List<Power> nonDeadPowers = game.getNonDeadPowers();
		for(int i= 0; i<nonDeadPowers.size(); i++){
			Power power = nonDeadPowers.get(i);
			if(power.getOwnedSCs().size()==0){
				game.killPower(power);
				i--;
			}
		}
	}

	/**
	 * NOW (turn)(unit)(unit)...
	 * received at the start of the game and after every turn. Indicates the current turn, and the current unit positions.
	 * initial turn is 'SPR 1901'
	 * @param message
	 * @throws CommException 
	 */
	private void handleNOW(String[] message) throws CommException {
		Game game = client.getGame();
		waitsForOrders(game);

		//updates info about controlled and dislodged regions
		Parser.updateControlledRegions(message, game);

		client.beforeNewPhase();
	}
	
	private void waitsForOrders(Game game) {
		if(gameState!=null){
			if(gameState.getState().equals(State.EMPTY)){
				//if we waited before, we do nothing now
				return;
			}
			//if we didn't wait before
			while(!gameState.getState().equals(State.DONE)){
				//waits for orders to arrive
				client.log.printMessage("Waiting for update to be done");
				long time = System.currentTimeMillis();
				while(System.currentTimeMillis() < time+1000); //Waits one second
			}
		}
		
		//restarts the state
		if(client instanceof Player){
			Power me = ((Player)client).getMe();
			gameState = new GameState(game, me);
		}else{
			gameState = new GameState(game);
		}
	}

	/**
	 * THX (order) (note)
	 * response to a submmited order.
	 * @param message
	 */
	private void handleTHX(String[] message) {
		//if note = MBV, then everything is ok.
		String response = message[message.length-2];
		if(!response.equals("MBV")){
			((Player)client).submissionError(message);
		}
	}

	/**
	 * MIS (unit) (unit), movement phase
	 * MIS (unit MRT (province list)) (unit MRT (province list))..., retreat phase
	 * MIS (number), adjustments phase
	 * indicates the list of units that have not been ordered. Syntax similar to NOW.
	 * For adjustments phase, if the number is positive, it indicates that you must order that many more disbands. If number is negative, it indicates that you must order that many more builds.
	 * Responds after a MIS if there are no outstanding orders.
	 * If arguments are present, it is a request for orders. Following messages are sent after THX if the server does not have a full set of orders:
	 * @param message
	 */
	private void handleMIS(String[] message) {
		((Player) client).missingOrder(message);
	}

	/**
	 * ORD (turn) (order) (result)
	 * Sent by the Server when the turn has processed. turn is the turn which has just processed.
	 * Order is an order submitted by a player. One ORD message is sent per unit for a movement phase, one per retreat for a retreat
phase, and one per build/disband/waive for a build phase . It has exactly the same format as in the SUB and THX commands.
result is the result of the order.
	 * @param message
	 */
	private void handleORD(String[] message) {
		StringA2Order orderParser = new StringA2Order(client.getGame());
		Order order = orderParser.processOrder(message);
		client.receivedOrder(order);
		
		gameState.addOrder(order);
		if(!gameState.isAnyOrderMissing()){
			//update everything
			gameState.setState(State.UPDATED);
			client.afterOldPhase();
			client.phaseEnd(gameState);
			gameState.setState(State.DONE);
		}
	}

	/**
	 * Server orders us to exit without replying
	 * @param message
	 */
	private void handleOFF(String[] message) {
		client.handleServerOFF();
		stop();
	}

	public void stop() {
		commImpl.stop();
	}

	/**
	 * This message is sent from the server to the client, and indicates that message was received from the client by the server, but does not have a correct set of parentheses. The PRN message will also not have matching parentheses. The client should cope with this. No reply should be sent to the server.
	 * @param message
	 */
	private void handlePRN(String[] message) {
		client.log.printError("Non correct parenthesis in the message: "+Interface.arrToString(message));
	}

	/**
	 * This message is sent from the server to the client, and means that the server determined that message had a syntax error in it. The token ERR is inserted into the message immediately before the first offending token. This error can also be caused by trying to use a form of the Diplomacy AI Development Environment syntax which is not available at the syntax level for the current game.
	 * @param message
	 */
	private void handleHUH(String[] message) {
		client.log.printError("Sintax error in message: "+Interface.arrToString(message));
	}

	/**
	 * 
	 * @param message
	 */
	private void handleOUT(String[] message) {
		client.exit();
	}

	/**
	 * SLO (power)
	 * The game has ended due to a solo by the specified power. It is send after the SCO and NOW messages.
	 * @param message
	 */
	private void handleSLO(String[] message) {
		client.handleSlo(message[2]);
	}

	/**
	 * After the game has ended, some information is received from the server
	 * @param message
	 */
	private void handleSMR(String[] message) {
		client.handleSMR(message);
	}

	private void handleOtherMessages(String[] message) {
		client.log.printMessage("Negotiation:");
		client.log.printMessage(message);
	}

	/**
	 * REJ(NME('name')('version'))
	 * REJ(HLO), in response to a HLO when game hasn't started
	 * REJ(SCO), in response to a SCO when game hasn't started
	 * REJ(HST(turn)), in response of a HST(turn) of a non existing turn
	 * REJ(SUB(order)(order)...), in response of a SUB(order)(order)... if the game has not started
	 * REJ(NOT(SUB (order))) en resposta a NOT(SUB(order)) per a anular un build o retreat quan la comanda no pot ser cancelada.
	 * REJ(MIS), in response to a MIS if the game has not started or has ended.
	 * REJ(NOT(GOF)) en resposta a NOT(GOF) si el joc no s'ha iniciat, el power esta eliminat o el joc ha acabat.
	 * REJ(ORD) en resposta a ORD si el joc no s'ha iniciat o el primer torn no s'ha completat.
	 * REJ(GOF) en resposta a GOF si el joc no s'ha iniciat, ha acabat.
	 * @param message
	 */
	private void handleREJ(String[] message) {
		client.log.printError(message);
	}

	/**
	 * YES(NOT(SUB (order))) en resposta a NOT(SUB(order)) per a anular un build o retreat.
	 * YES(NOT(SUB)) en resposta a NOT(SUB) per a anular les comandes enviades en aquest torn.
	 * YES(NOT(GOF)) en resposta a NOT(GOF) si el servidor preten esperar-se fins el deadline
	 * YES(GOF) in response of GOF if everything is ok
	 * @param message
	 * @throws CommException 
	 */
	private void handleYES(String[] message) throws CommException {
		if(message[2].equals("IAM")){
			if(client instanceof Player){
				((Player)client).reconnectionConfirmation(message[4], Integer.parseInt(message[7]));
				//Assuming the map is the standard one. We need the map before initiating the client.
				//sendMessage(MessageBuilder.fill(MessageBuilder.MSG_MAP, null)); //to ask for the map
				client.setMap("'STANDARD'");
				((Player) client).setMe(message[4]);//TODO verificar si cal
				client.init();
			}else{
				client.log.printError(message);
			}
		}else if(message[2].equals("OBS")){
			if(client instanceof Player){
				client.log.printError(message);
			}
		}else if(message[2].equals("NME")){
			if(! (client instanceof Player)){
				client.log.printError(message);
			}
		}
	}
	
	private void handleCCD(String[] message) {
		client.handleCCD(message[2]);
	}
	
	public GameState getGameState(){
		return gameState;
	}
}