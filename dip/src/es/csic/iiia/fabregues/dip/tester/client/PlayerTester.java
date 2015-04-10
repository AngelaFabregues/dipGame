package es.csic.iiia.fabregues.dip.tester.client;

import java.util.List;

import es.csic.iiia.fabregues.dip.Player;
import es.csic.iiia.fabregues.dip.comm.GameBuilder;
import es.csic.iiia.fabregues.dip.comm.Order2StringA;
import es.csic.iiia.fabregues.dip.orders.Order;
import es.csic.iiia.fabregues.utilities.Interface;

public class PlayerTester {
	
	private Player player;
	
	private final static String DEFAULT_SCO = "SCO ( AUS BUD VIE NAP TRI ) ( ENG EDI LON LVP ) ( FRA PAR BRE VEN ) ( GER MUN BER KIE MAR ) ( ITA ROM ) ( RUS MOS WAR SEV STP ) ( TUR ANK CON SMY ) ( UNO SER BEL DEN GRE HOL NWY POR RUM SWE TUN BUL SPA )";
	private final static String DEFAULT_NOW = "NOW ( AUT 1911 ) ( AUS AMY BUD ) ( AUS AMY VIE ) ( AUS FLT ADR ) ( AUS FLT ALB ) ( ENG FLT SKA ) ( ENG AMY WAL ) ( ENG FLT LVP ) ( FRA FLT BAR ) ( FRA AMY PIC ) ( FRA AMY PIE ) ( GER AMY BUR ) ( GER AMY GAS ) ( GER AMY MAR ) ( GER FLT LVN MRT ( BAL ( STP SCS ) ) ) ( ITA AMY RUH ) ( RUS FLT GOB ) ( RUS FLT ARM ) ( RUS AMY LVN ) ( RUS AMY PRU ) ( TUR FLT ANK ) ( TUR AMY CON ) ( TUR AMY SMY )";
	private final static String DEFAULT_POWER = "FRA";
	
	private String[] sco;
	private String[] now;
	private String power;
	
	public PlayerTester(Player player){
		this.player = player;
		this.sco = DEFAULT_SCO.split(" ");
		this.now = DEFAULT_NOW.split(" ");
		this.power = DEFAULT_POWER;
	}
	
	public void setPower(String power){
		this.power = power;
	}
	
	public void setSCO(String sco){
		setSCO(sco.split(" "));
	}
	
	public void setSCO(String[] sco){
		this.sco = sco;
	}
	
	public void setNOW(String now){
		setNOW(now.split(" "));
	}
	
	public void setNOW(String[] now){
		this.now = now;
	}
	
	public void run(){

		try{
			player.log = new Interface("playerTester");
			player.log.enableDebug();
			player.init();
			
			player.setGame(GameBuilder.createGame(sco, now));
			player.setMe(power);
			player.log.printMessage(now);
			player.log.printMessage(sco);
			player.start();
			
			List<Order> orders = player.play();
			
			player.log.printMessage(power+" orders are:");
			for(Order order: orders){
				player.log.printMessage(Order2StringA.getOrderMsg(order));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}