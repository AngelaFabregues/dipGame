package es.csic.iiia.fabregues.dip.comm;

import es.csic.iiia.fabregues.dip.board.Game;
import es.csic.iiia.fabregues.dip.board.Power;
import es.csic.iiia.fabregues.dip.board.Province;
import es.csic.iiia.fabregues.dip.board.Region;

/**
 * Parsing utility. Helps on dealing with String[] data representation
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class ParseService {
	
	public static Province getProvince(Game game, String[] message){
		Province province;
		if(message[0].equals("(")){
			province = game.getProvince(message[1]);
		}else{
			province = game.getProvince(message[0]);
		}
		return province;
	}

	/**
	 * ( | RUS | AMY | MOS | )
	 * @param message
	 * @return
	 */
	public static Region getRegion(Game game, String[] message){
		String name;
		String unitType = message[2];
		if(unitType.equals("FLT") && message[3].equals("(")){ //region0
			name = message[4]+message[5];
		}else{
			name = message[3]+unitType.toString();
		}
		return game.getRegion(name);
	}
	
	public static Region getDestinationRegion(Game game, String unitType, String[] message){
		if(message[0].equals("(")){
			return game.getRegion(message[1]+message[2]);
		}
		return game.getRegion(message[0]+unitType);
	}
	
	public static Region getUnit(Game game, String[] message){
		String[] regionStr = substring(message, 3);
		return getRegion(game, regionStr);
	}
	
	public static Power getPower(Game game, String[] message){
		return game.getPower(message[1]);
	}
	
	/**
	 * Retorna un array amb tots els elements amb index menor que end
	 * @param tokens
	 * @param end
	 * @return
	 */
	public static String[] takeLeft(String tokens[], int end) {
		int i = 0;
		String left[] = new String[end];
		for (i = 0; i < end; i++) {
			left[i] = tokens[i];
		}
	
		return left;
	}

	/**
	 * Retorna un array amb tots els elemens amb index superior o igual a start
	 * @param tokens
	 * @param start
	 * @return
	 */
	public static String[] takeRight(String tokens[], int start) {
		int length = tokens.length;
		String right[] = new String[length - start];
		for (int i = 0; i < length - start; i++) {
			right[i] = tokens[start + i];
		}
	
		return right;
	}

	/**
	 * Retorna el nombre de tokens que separen el "("
	 * del primer token del seu corresponent ")" + 2.
	 * ( | GER | AMY | BUR | )  ----> 5
	 * @param tokens
	 * @return
	 */
	public static int findClose(String tokens[]) {
		if (!tokens[0].equals("(")) {
			return -1;
		}
		int bracketCount = 0;
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].equals("(")) {
				bracketCount++;
			}else if (tokens[i].equals(")")) {
				bracketCount--;
				if (bracketCount == 0) {
					return i + 1;
				}
			}
		}
	
		return -1;
	}

	/**
	 * Retorna un nou array amb els elements del primer excepte els index primers.
	 * @param message
	 * @param index
	 * @return
	 */
	public static String[] substring(String[] message, int index) {
		String[] strs = new String[message.length-index];
		for(int i = 0; i<strs.length; i++){
			strs[i] = message[index+i]; 
		}
		return strs;
	}

	/**
	 * Creates a string array specifying the region
	 * @param region
	 * @return
	 */
	public static String[] toRegionStringArr(Region region) {
		if(region.getName().endsWith("FLT") || region.getName().endsWith("AMY")){
			return new String[]{region.getProvince().getName()};
		}
		return new String[]{"(",region.getName().substring(0,3),region.getName().substring(3,6),")"};
	}

	/**
	 * Creates an array that represents a unit
	 * @param unit
	 * @return
	 */
	public static String[] toUnitOrderStringArr(Region region, Power power){
		String[] regionStrArr = toRegionStringArr(region);
		String[] order = new String[4+regionStrArr.length];
		String type = region.getName().substring(3,6);
		if(!type.equals("AMY") && !type.equals("FLT")){
			type = "FLT";
		}
		order[0] = "(";
		order[1] = power.getName();

		order[2] = type;
		int i=3;
		for(String str: regionStrArr){
			order[i++] = str;
		}
		order[i++] = ")";
		return order;
	}

	public static String[] toStringArr(String msg) {
		return msg.split(" ");
	}

}
