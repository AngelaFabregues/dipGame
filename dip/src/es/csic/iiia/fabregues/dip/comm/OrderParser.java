package es.csic.iiia.fabregues.dip.comm;

import es.csic.iiia.fabregues.dip.board.Game;
import es.csic.iiia.fabregues.dip.board.Power;
import es.csic.iiia.fabregues.dip.board.Province;
import es.csic.iiia.fabregues.dip.board.Region;
import es.csic.iiia.fabregues.dip.orders.BLDOrder;
import es.csic.iiia.fabregues.dip.orders.DSBOrder;
import es.csic.iiia.fabregues.dip.orders.HLDOrder;
import es.csic.iiia.fabregues.dip.orders.MTOOrder;
import es.csic.iiia.fabregues.dip.orders.Order;
import es.csic.iiia.fabregues.dip.orders.REMOrder;
import es.csic.iiia.fabregues.dip.orders.RTOOrder;
import es.csic.iiia.fabregues.dip.orders.SUPMTOOrder;
import es.csic.iiia.fabregues.dip.orders.SUPOrder;
import es.csic.iiia.fabregues.dip.orders.WVEOrder;

/**
 * OrderParser
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class OrderParser {
	
	public enum OrderType {HLD, MTO, SUP, CVY, CTO, RTO, DSB, BLD, REM, WVE};
	public enum ResultType {SUC, BNC, CUT, DSR, NSO, RET};
	
	protected Game game;
	
	public OrderParser(Game game){
		this.game = game;
	}
	
	public boolean isSUPorder(String[] order) {
		if(order.length < 6){
			return false;
		}
		int index = ParseService.findClose(ParseService.substring(order, 6));
		if(order.length < 6+index+1){
			return false;
		}
		try{
			OrderType orderType = OrderType.valueOf(order[6+index]);
			if(OrderType.SUP.equals(orderType)){
				return true;
			}
		}catch (IllegalArgumentException e) {
			
		}
		return false;
	}
	
	/**
	 * Pot processar ordres en el seg�ents formats:
	 * ( AUS AMY BUD ) HLD )
	 * SUB ( ( AUS AMY BUD ) HLD )
	 * ORD ( SPR 1901 ) ( ( AUS AMY BUD ) HLD ) ( SUC )
	 * 
	 * @param orderMsg
	 * @return
	 */
	public Order processOrder(String[] orderMsg){
		String result = "";
		if(orderMsg[0].equals("ORD")){
			orderMsg = ParseService.substring(orderMsg, 5);
			int beginIndex = ParseService.findClose(orderMsg);
			for(int i=beginIndex+1; i < orderMsg.length-1; i++){
				result+=" "+orderMsg[i];
			}
			result = result.substring(1, result.length());
			orderMsg = ParseService.substring(orderMsg, 1);
		}else if(orderMsg[0].equals("SUB")){
			orderMsg = ParseService.substring(orderMsg, 2);
		}
		
		Power power;
		OrderType order;
		if(!orderMsg[0].equals("(")){ //power WVE
			order = OrderType.valueOf(orderMsg[1]);
			power = game.getPower(orderMsg[0]);
			//waive
			return handleWVE(power, result);
		}else{
			Region region = ParseService.getRegion(game, orderMsg);
			int index = ParseService.findClose(orderMsg);
			order = OrderType.valueOf(orderMsg[index]);
			index++;
			Province province;
			String[] regionMsg;
			Region regionSupported;
			Region destRegion;
			power = ParseService.getPower(game, orderMsg);
			switch(order){
			case HLD:
				return handleHLD(power, region, result);
			case MTO:
				String[] regionStr = ParseService.substring(orderMsg, index);
				
				if(regionStr[0].equals("(")){
					destRegion = ParseService.getDestinationRegion(game, region.getName().substring(3, 6), regionStr);
				}else{
					//busquem la regi� de la provincia que tingui el tipus mateix de la regi� que es mou
					destRegion = game.getAdjacentRegionIn(game.getProvince(regionStr[0]), region);
				}
				return handleMTO(power, region, destRegion, result);
			case SUP:
				regionMsg = ParseService.substring(orderMsg, index);
				regionSupported = ParseService.getRegion(game, regionMsg);
				Power supportedPower = ParseService.getPower(game, regionMsg);
				index += ParseService.findClose(regionMsg);
				if(orderMsg.length > index && orderMsg[index].equals("MTO")){ // no MTO
					province = game.getProvince(orderMsg[index+1]);
					destRegion = game.getAdjacentRegionIn(province, regionSupported);
					return handleSUP(power, region,
							supportedPower, regionSupported,
							destRegion, result);
				}else{
					return handleSUP(power, region, supportedPower, regionSupported, result);
				}
			case CVY:
				regionMsg = ParseService.substring(orderMsg, index);
				regionSupported = ParseService.getRegion(game, regionMsg);
				index += ParseService.findClose(regionMsg);
				//CTO
				province = game.getProvince(orderMsg[index+1]);
				// no estem preparats per fer un convoy
				return handleCVY(region, regionSupported, province, result);
			case CTO:
				province = game.getProvince(orderMsg[index+1]);
				index += 3;
				//VIA
				String[] seasString = ParseService.substring(orderMsg, index);
				int nSeas = ParseService.findClose(seasString)-2;
				Province[] seas = new Province[nSeas];
				index++;
				for(int i=0; i<nSeas; i++){
					seas[i] = game.getProvince(orderMsg[index+i]);
				}
				// no estem preparats per fer un convoy
				return handleCTO(region, province, seas, result);
			case RTO:
				//Power powerDislodged = ParseService.getPower(game, orderMsg);
				String[] regionString = ParseService.substring(orderMsg, index);
				if(regionString[0].equals("(")){
					destRegion = ParseService.getRegion(game, regionString);
				}else{
					//busquem la regi� de la provincia que tingui el tipus mateix de la regi� que es mou
					String type = region.getName().substring(3,6);
					destRegion = game.getRegion(game.getProvince(regionString[0]), type);
				}
				return handleRTO(region, power, destRegion, result);
			case DSB:
				//Power powerDisbanding = ParseService.getPower(game, orderMsg);
				return handleDSB(region, power, result);
			case BLD:
				//build
				return handleBLD(power, region, result);
			case REM:
				return handleREM(power, region, result);
			case WVE:
				//waive
				// No hauria d'arribar aqu�, aquest cas es tracte m�s amunt perqu� no t� (region) al davant
				//log.printError("ups");
				break;
			default:
				//log.printError("ups");
			}
		}
		return null;
	}

	private static Order handleREM(Power power, Region region, String result) {
		Order order = new REMOrder(power, region);
		order.setResult(result);
		// Preparat per a ser sobreescrit
		return order;
	}

	private static Order handleBLD(Power power, Region region, String result) {
		// Preparat per a ser sobreescrit
		Order order = new BLDOrder(power, region); 
		order.setResult(result);
		return order;
	}

	private static Order handleDSB(Region region, Power power, String result) {
		// Preparat per a ser sobreescrit
		Order order = new DSBOrder(region, power);
		order.setResult(result);
		return order;
	}

	private static Order handleRTO(Region region, Power power, Region rtoRegion, String result) {
		// Preparat per a ser sobreescrit
		Order order = new RTOOrder(region, power, rtoRegion);
		order.setResult(result);
		return order;
	}

	private static Order handleCTO(Region region, Province province, Province[] seas, String result) {
		// Preparat per a ser sobreescrit
		return null;//new CTOOrder(region);
	}

	private static Order handleCVY(Region region, Region regionSupported, Province province, String result) {
		// Preparat per a ser sobreescrit
		return null;//new CVYOrder(region);
	}

	protected Order handleSUP(Power power, Region region, Power supportedPower, Region regionSupported, Region destination, String result) {
		Order order = new SUPMTOOrder(power, region, new MTOOrder(supportedPower, regionSupported, destination));
		order.setResult(result);
		// Preparat per a ser sobreescrit
		return order;
	}

	protected Order handleSUP(Power power, Region region, Power supportedPower, Region regionSupported, String result) {
		Order order = new SUPOrder(power, region, new HLDOrder(supportedPower, regionSupported));
		order.setResult(result);
		// Preparat per a ser sobreescrit
		return order;
	}

	protected Order handleMTO(Power power, Region region, Region mtoRegion, String result) {
		Order order = new MTOOrder(power, region, mtoRegion);
		order.setResult(result);
		// Preparat per a ser sobreescrit
		return order;
	}

	private static Order handleWVE(Power power, String result) {
		Order order = new WVEOrder(power);
		order.setResult(result);
		// Preparat per a ser sobreescrit
		return order;
	}

	private static Order handleHLD(Power power, Region region, String result) {
		Order order = new HLDOrder(power, region);
		order.setResult(result);
		// Preparat per a ser sobreescrit
		return order;
	}

}
