package es.csic.iiia.fabregues.dip.comm;

/**
 * Builds messages to be sent from the client to the server.
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class MessageBuilder {

	public static final String MSG_OBS[] = {"OBS"};
	public static final String MSG_MAP[] = {"MAP"}; // AI requests map name at any time
	public static final String MSG_NOW[] = {"NOW"}; // AI requests map name at any time
	public static final String MSG_YES_MAP[] = {"YES","(","MAP","(","?",")",")" }; // AI accepts the map. name
	public static final String MSG_REJ_MAP[] = {"REJ","(","MAP","(","?",")",")" }; // AI rejects the map. name
	public static final String MSG_MDF[] = {"MDF"}; // AI requests map definition
	public static final String MSG_HLO[] = {"HLO"}; // AI requests game start info
	public static final String MSG_SCO[] = {"SCO"}; // AI requests current supply centre ownerships
	public static final String MSG_HST[] = {"HST", "(", "'?'", ")"}; // AI requests previous results {turn}
	public static final String MSG_NOT_SUB[] = {"NOT","(","SUB", "?",")"}; // AI cancels a build or retreat {order}.
	public static final String MSG_NOT_SUB_ALL[] = {"NOT","(","SUB",")"}; // AI cancels all turn orders
	public static final String MSG_MIS[] = {"MIS"}; // AI requests a copy of the current MIS command.
	public static final String MSG_NOT_GOF[] = {"NOT", "(", "GOF", ")"}; // AI requests the server non process orders until the deadline. It is equivalent of set wait on the judges.
	public static final String MSG_GOF[] = {"GOF"}; // AI requests the server to go ahead and process orders when everybody is in. It is assumed when you submit all orders required.
	public static final String MSG_ORD[] = {"ORD"}; // AI requests the server for the last orders executed.
	public static final String MSG_NME[] = {"NME",	"(","'?'",")","(","'?'",")"};
	public static final String MSG_IAM[] = {"IAM",	"(","?",")","(","?",")"};
	
	/**
	 * Fills a message with some parameters placed instead of ? characters in the message.
	 * @param msg
	 * @param params
	 * @return a new string with ? characters replaced by attached parameters
	 */
	public static String[] fill(String[] vell, String[] params) {
		String[] msg = vell.clone();
		int i = -1;
		int j = 0;
		int index = 0;
		if(params == null){
			params = new String[]{""};
		}
		for(String param: params){
			do{
				i++;
				if(i == msg.length){
					return msg;
				}
				j = 0;
				index = msg[i].indexOf('?', j);
			}while(index == -1);
			msg[i] = msg[i].substring(0, index)+param+msg[i].substring(index+1, msg[i].length());
			j = j + param.length()-1;
		}
		return msg;
	}

}
