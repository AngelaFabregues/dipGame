package es.csic.iiia.fabregues.dip.util;
import es.csic.iiia.fabregues.dip.board.Game;
import es.csic.iiia.fabregues.dip.comm.GameBuilder;
import es.csic.iiia.fabregues.dip.comm.Parser;

/**
 * Shows how to clone games.
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 * 
 * You can type {@code Game clon = GameCloner.clone(game);} to get a cloned version of game. 
 */
public class GameCloner {
	
	public static final String SCO = "SCO ( AUS BUD VIE NAP TRI ) ( ENG EDI LON LVP ) ( FRA PAR BRE VEN ) ( GER MUN BER KIE MAR ) ( ITA ROM ) ( RUS MOS WAR SEV STP ) ( TUR ANK CON SMY ) ( UNO SER BEL DEN GRE HOL NWY POR RUM SWE TUN BUL SPA )";
	public static final String NOW = "NOW ( AUT 1911 ) ( AUS AMY BUD ) ( AUS AMY VIE ) ( AUS FLT ADR ) ( AUS FLT ALB ) ( ENG FLT SKA ) ( ENG AMY WAL ) ( ENG FLT LVP ) ( FRA FLT BAR ) ( FRA AMY PIC ) ( FRA AMY PIE ) ( GER AMY BUR ) ( GER AMY GAS ) ( GER AMY MAR ) ( GER FLT LVN MRT ( BAL ( STP SCS ) ) ) ( ITA AMY RUH ) ( RUS FLT GOB ) ( RUS FLT ARM ) ( RUS AMY LVN ) ( RUS AMY PRU ) ( TUR FLT ANK ) ( TUR AMY CON ) ( TUR AMY SMY )";

	public static void main(String[] args){
				
		//Creating a parser
		Parser p = new Parser();
		
		//Creating a game. (you don't need to do this because you already have a Game object, the one you want to clone)
		Game game = GameBuilder.createGame(SCO.split(" "), NOW.split(" "));
		
		//Getting the essential info from game using the parser
		String[] now = p.getNOW(game);
		String[] sco = p.getSCO(game);
		
		//printing it
		print(now);
		print(sco);
		
		//Creating a new Game object using the information about the sco and now messages obtained from the original game
		Game game2 = GameBuilder.createGame(sco, now);
		
		//You already have the clonned game but I proof you that it is a clone printing the content and comparing the objects with '==' (this compare by reference, it is true iff both references are form the same object)
		
		//Getting the essential info from game2 using the parser
		String[] now2 = p.getNOW(game2);
		String[] sco2 = p.getSCO(game2);

		//printing the info of game2.
		print(now2);
		print(sco2);
		
		//game and game2 are different objects
		if(game == game2){
			System.out.println("game and game2 are the same object.");
		}else{
			System.out.println("game and game2 are different objects.");
		}
	}
	
	/**
	 * Prints arrays of Strings
	 * @param arr
	 */
	private static void print(String[] arr){
		String message = "";
		for(String a: arr){
			message+=a+" ";
		}
		System.out.println(message.substring(0, message.length()-1));
	}
	
	/**
	 * Clones a game
	 * @param game
	 * @return the cloned game
	 */
	public static Game clone(Game game){
		Parser p = new Parser();
		String[] now = p.getNOW(game);
		String[] sco = p.getSCO(game);
		return GameBuilder.createGame(sco, now);
	}
}
