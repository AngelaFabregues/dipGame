package es.csic.iiia.fabregues.dip.comm;

import java.util.List;
import java.util.Vector;

import es.csic.iiia.fabregues.dip.board.Dislodgement;
import es.csic.iiia.fabregues.dip.board.Game;
import es.csic.iiia.fabregues.dip.board.Phase;
import es.csic.iiia.fabregues.dip.board.Power;
import es.csic.iiia.fabregues.dip.board.Province;
import es.csic.iiia.fabregues.dip.board.Region;
import es.csic.iiia.fabregues.dip.orders.Order;

/**
 * Modifies the game and get info from it
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class Parser {
	
	/**
	 * Fica les homes a cada potencia
	 * 
	 * @param scoMessage
	 * 
	 * TODO move to another place
	 */
	public static void setHomes(String scoMessage[], Game game) {
		int closingBracket;
		scoMessage = ParseService.takeRight(scoMessage, 1);
		while (scoMessage.length > 0) {
			closingBracket = ParseService.findClose(scoMessage);
			String singlePower[] = ParseService.takeLeft(scoMessage, closingBracket);
			Power power =  game.getPower(singlePower[1]);
			if(power!=null){
				for (int i = 2; i < singlePower.length - 1; i++) {
					Province home = game.getProvince(singlePower[i]);
					power.addHome(home);
				}
			}
			scoMessage = ParseService.takeRight(scoMessage,closingBracket);
		}
	}
	
	/**
	 * Update info about ownded SCs
	 * @param scoMessage
	 * 
	 * TODO move to another place
	 */
	public static void updateOwnedSCs(String scoMessage[], Game game) {
		// remove old SC ownership info
		for(Power power: game.getPowers()){
			power.resetOwn();
		}

		// set new SC ownership info
		int closingBracket;
		scoMessage = ParseService.takeRight(scoMessage, 1);
		while (scoMessage.length > 0) {
			closingBracket = ParseService.findClose(scoMessage);
			String singlePower[] = ParseService.takeLeft(scoMessage, closingBracket);
			Power power =  game.getPower(singlePower[1]);
			if(power!=null){
				for (int i = 2; i < singlePower.length - 1; i++) {
					Province owned = game.getProvince(singlePower[i]);
					power.addOwn(owned);
				}
			}
			scoMessage = ParseService.takeRight(scoMessage,closingBracket);
		}
	}
	
	
	/**
	 * Update controlled regions info
	 * @param message
	 * 
	 * TODO move to another place
	 */
	public static void updateControlledRegions(String message[], Game game) {
		game.setPhase(Phase.valueOf(message[2]));
		game.setYear(Integer.valueOf(message[3]));
		// unoccupy all units
		for(Power power: game.getPowers()){
			power.resetControl();
		}
		game.resetDislodgedRegions();
		
		// occupy new units
		int closingBracketIndex;
		message = ParseService.takeRight(message, 5);
		while (message.length > 0) {
			closingBracketIndex = ParseService.findClose(message);
			handleUnit(ParseService.takeLeft(message, closingBracketIndex), game);
			message = ParseService.takeRight(message, closingBracketIndex);
		}
	}

	//( | AUS | AMY | RUM | )
	private static void handleUnit(String msg[], Game game) {
		Region region = ParseService.getRegion(game, msg);
		Power controller = ParseService.getPower(game, msg);
		controller.addControlledRegion(region);
		updateRetreatements(msg, region, controller, game);
	}
	
	//( | RUS | AMY | RUM | MRT | ( | GAL | ) | )
	//( | RUS | FLT | ( | STP | SCS | ) | MRT | ( | FIN | LVN | ) | )
	private static void updateRetreatements(String msg[], Region region, Power controller, Game game) {
		if(controller!=null && !controller.isControlling(region)){
			return;
		}
		int i=0;
		if(msg[4].equals(")")){
			return;
		}
		if(msg[4].equals("MRT")){
			i = 5;
		}else if (msg[7].equals("MRT")){
			i = 8;
		}else{
			return;
		}	
		Dislodgement dislodgement = new Dislodgement(controller, region);
		String[] regionsStr = ParseService.substring(msg, i);
		int nregions = ParseService.findClose(regionsStr);
		for(int j=1; j<nregions-1; j++){
			String type = region.getName().substring(3,6);
			String name;
			if(regionsStr[j].equals("(")){  //( GER FLT LVN MRT ( BAL ( STP SCS ) ) )
				name = regionsStr[++j];
				type = regionsStr[++j];
				j++;
			}else{
				name = regionsStr[j];
				if(!type.equals("AMY") && !type.equals("FLT")){
					type = "FLT";
				}
			}
			
			Region rtoRegion = game.getRegion(name+type); 
			dislodgement.addRetreateToRegion(rtoRegion);
		}
		game.addDislodgedRegion(region, dislodgement);
		if(controller.getControlledRegions().contains(region)){
			controller.getControlledRegions().remove(region);
		}
		
	}
	
	public static String[] getSCO(Game game){
		List<Province> ownedSCs = new Vector<Province>();
		List<String> sco = new Vector<String>(59);//new String[59];
		sco.add("SCO");
		for(Power power: game.getPowers()){
			sco.add("(");
			sco.add(power.getName());
			for(Province sc: power.getOwnedSCs()){
				sco.add(sc.getName());
				ownedSCs.add(sc);
			}
			sco.add(")");
		}
		sco.add("(");
		sco.add("UNO");
		for(Province province: game.getProvinces()){
			if(province.isSC()){
				if(!ownedSCs.contains((Province)province)){
					sco.add(province.getName());
				}
			}
		}
		sco.add(")");
		return sco.toArray(new String[sco.size()]);
	}
	
	/**
	 * Generate the current NOW message
	 * NOW (turn) (unit) (unit) ?
	 * This is sent from the server to the AI at the start of the game, and after every turn. It indicates the current turn, and the current unit
	 * positions. E.g. at the start of the game: NOW (SPR 1901) (AUS FLT TRI) (AUS AMY BUD) (AUS AMY VIE) (ENG FLT LON) ?
	 * Units in bicoastal provinces have a province and coast bracketed together. E.g. : (RUS FLT (STP SCS))
	 * Before a retreat turn, units may have a list of retreat options, prefixed by MRT (Must retreat to). E.g. (ENG FLT NTH MRT (LON YOR EDI NWG))
	 * If you have no possible retreats, then the unit will still be listed, and you still must order the disband. E.g.(ENG FLT NTH MRT ())
	 * Retreat options will include a coast if a fleet can retreat to a multi-coastal province. e.g.(TUR FLT CON MRT (BLA SMY (BUL ECS) (BUL SCS)))
	 * The AI can send the command NOW to the server with no arguments at any time. The server will reply by sending another copy of the latest NOW message, or REJ(NOW) if the game hasn?t started.
	 * @return
	 * TODO move to another place
	 */
	public static String[] getNOW(Game game){
		List<String> now = new Vector<String>();
		
		now.add("NOW");
		
		//turn
		for(String str: getTurn(game)){
			now.add(str);
		}
		
		//units
		for(Power power: game.getPowers()){			
			for(Region region: power.getControlledRegions()){
				for(String str: ParseService.toUnitOrderStringArr(region, power)){
					now.add(str);
				}
			}
		}
		
		//mrt
		for(Region region: game.getDislodgedRegions().keySet()){
			Dislodgement dislodgement = game.getDislodgedRegions().get(region);
			//(ENG FLT NTH)
			for(String str: ParseService.toUnitOrderStringArr(region, dislodgement.getPower())){
				now.add(str);
			}
			now.set(now.size()-1, "MRT");
			now.add(now.size(), "(");
			for(Region mrt: dislodgement.getRetreateTo()){
				for(String str: ParseService.toRegionStringArr(mrt)){
					now.add(str);
				}
			}
			now.add(")");
			now.add(")");
		}
		
		return now.toArray(new String[now.size()]);
	}
	
	private static String[] getTurn(Game game) {
		List<String> turn = new Vector<String>();
		turn.add("(");
		turn.add(game.getPhase().toString());
		turn.add(""+game.getYear());
		turn.add(")");
		return turn.toArray(new String[turn.size()]);
	}
	
	
	
	public static GameBuilder createGameBuilder(String[] mdf, String[] homes){
		GameBuilder builder = new GameBuilder(mdf, homes);
		return builder;
	}

	public static GameBuilder createGameBuilder(String[] mdf) {
		return createGameBuilder(mdf, null);
	}
	
	public static GameBuilder createGameBuilder() {
		return createGameBuilder(null, null);
	}

	public static String[] formatOrders(List<Order> orders) {
		Vector<String> orderStrV = new Vector<String>();
		String[] orderStrStr;
		orderStrV.add("SUB");
		
		for (Order order : orders) {
			orderStrV.add("(");
			orderStrStr = Order2StringA.getOrderMsg(order);
			for (String str : orderStrStr) {
				orderStrV.add(str);
			}
			orderStrV.add(")");
		}
		
		String[] orderStr = orderStrV.toArray(new String[orderStrV.size()]);
		
		return orderStr;

	}
}

