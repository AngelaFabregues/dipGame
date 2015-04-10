package es.csic.iiia.fabregues.dip.comm;

import java.util.Hashtable;

import es.csic.iiia.fabregues.dip.board.Game;
import es.csic.iiia.fabregues.dip.board.Phase;
import es.csic.iiia.fabregues.dip.board.Power;
import es.csic.iiia.fabregues.dip.board.Province;
import es.csic.iiia.fabregues.dip.board.Region;

/**
 * Builds a game from scratch or from a MDF
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class GameBuilder {
	
	public final static String DEFAULT_MAP = "'STANDARD'";
	public final static String[] DEFAULT_HOMES = {"SCO", "(", "AUS", "BUD", "VIE", "TRI", ")", "(", "ENG", "EDI", "LON", "LVP", ")", "(", "FRA", "PAR", "BRE", "MAR", ")", "(", "GER", "MUN", "BER", "KIE", ")", "(", "ITA", "NAP", "ROM", "VEN", ")", "(", "RUS", "MOS", "WAR", "SEV", "STP", ")", "(", "TUR", "ANK", "CON", "SMY", ")", "(", "UNO", "SER", "BEL", "DEN", "GRE", "HOL", "NWY", "POR", "RUM", "SWE", "TUN", "BUL", "SPA", ")"};
	public final static String[] DEFAULT_MDF = {"MDF", 
		"(", "AUS", "ENG", "FRA", "GER", "ITA", "RUS", "TUR", ")", "(", "(", "(", "AUS", "BUD", "TRI", "VIE", ")", "(", "ENG", "EDI", 
		"LON", "LVP", ")", "(", "FRA", "BRE", "MAR", "PAR", ")", "(", "GER", "BER", "KIE", "MUN", ")", "(", "ITA", "NAP", "ROM", "VEN", 
		")", "(", "RUS", "MOS", "SEV", "STP", "WAR", ")", "(", "TUR", "ANK", "CON", "SMY", ")", "(", "UNO", "BEL", "BUL", "DEN", "GRE", 
		"HOL", "NWY", "POR", "RUM", "SER", "SPA", "SWE", "TUN", ")", ")", "(", "ALB", "APU", "ARM", "BOH", "BUR", "CLY", "FIN", "GAL", "GAS", 
		"LVN", "NAF", "PIC", "PIE", "PRU", "RUH", "SIL", "SYR", "TUS", "TYR", "UKR", "WAL", "YOR", "ADR", "AEG", "BAL", "BAR", "BLA", "GOB", "EAS", 
		"ECH", "HEL", "ION", "IRI", "GOL", "MAO", "NAO", "NTH", "NWG", "SKA", "TYS", "WES", ")", ")", "(", "(", "ADR", "(", "FLT", "ALB", 
		"APU", "VEN", "TRI", "ION", ")", ")", "(", "AEG", "(", "FLT", "GRE", "(", "BUL", "SCS", ")", "CON", "SMY", "EAS", "ION", ")", 
		")", "(", "ALB", "(", "AMY", "TRI", "GRE", "SER", ")", "(", "FLT", "ADR", "TRI", "GRE", "ION", ")", ")", "(", "ANK", "(", 
		"AMY", "ARM", "CON", "SMY", ")", "(", "FLT", "BLA", "ARM", "CON", ")", ")", "(", "APU", "(", "AMY", "VEN", "NAP", "ROM", ")", 
		"(", "FLT", "VEN", "ADR", "ION", "NAP", ")", ")", "(", "ARM", "(", "AMY", "SMY", "SYR", "ANK", "SEV", ")", "(", "FLT", "ANK", 
		"SEV", "BLA", ")", ")", "(", "BAL", "(", "FLT", "LVN", "PRU", "BER", "KIE", "DEN", "SWE", "GOB", ")", ")", "(", "BAR", "(", 
		"FLT", "NWG", "(", "STP", "NCS", ")", "NWY", ")", ")", "(", "BEL", "(", "AMY", "HOL", "PIC", "RUH", "BUR", ")", "(", "FLT", 
		"ECH", "NTH", "HOL", "PIC", ")", ")", "(", "BER", "(", "AMY", "KIE", "PRU", "SIL", "MUN", ")", "(", "FLT", "KIE", "BAL", "PRU", 
		")", ")", "(", "BLA", "(", "FLT", "RUM", "SEV", "ARM", "ANK", "CON", "(", "BUL", "ECS", ")", ")", ")", "(", "BOH", "(", 
		"AMY", "MUN", "SIL", "GAL", "VIE", "TYR", ")", ")", "(", "GOB", "(", "FLT", "SWE", "FIN", "(", "STP", "SCS", ")", "LVN", "BAL", 
		")", ")", "(", "BRE", "(", "AMY", "PIC", "GAS", "PAR", ")", "(", "FLT", "MAO", "ECH", "PIC", "GAS", ")", ")", "(", "BUD", 
		"(", "AMY", "VIE", "GAL", "RUM", "SER", "TRI", ")", ")", "(", "BUL", "(", "(", "FLT", "ECS", ")", "CON", "BLA", "RUM", ")", 
		"(", "AMY", "GRE", "CON", "SER", "RUM", ")", "(", "(", "FLT", "SCS", ")", "GRE", "AEG", "CON", ")", ")", "(", "BUR", "(", 
		"AMY", "MAR", "GAS", "PAR", "PIC", "BEL", "RUH", "MUN", ")", ")", "(", "CLY", "(", "AMY", "EDI", "LVP", ")", "(", "FLT", "EDI", 
		"LVP", "NAO", "NWG", ")", ")", "(", "CON", "(", "AMY", "BUL", "ANK", "SMY", ")", "(", "FLT", "(", "BUL", "SCS", ")", "(", 
		"BUL", "ECS", ")", "BLA", "ANK", "SMY", "AEG", ")", ")", "(", "DEN", "(", "AMY", "SWE", "KIE", ")", "(", "FLT", "HEL", "NTH", 
		"SWE", "BAL", "KIE", "SKA", ")", ")", "(", "EAS", "(", "FLT", "SYR", "SMY", "AEG", "ION", ")", ")", "(", "EDI", "(", "AMY", 
		"LVP", "YOR", "CLY", ")", "(", "FLT", "NTH", "NWG", "CLY", "YOR", ")", ")", "(", "ECH", "(", "FLT", "MAO", "IRI", "WAL", "LON", 
		"NTH", "BEL", "PIC", "BRE", ")", ")", "(", "FIN", "(", "AMY", "SWE", "STP", "NWY", ")", "(", "FLT", "SWE", "(", "STP", "SCS", 
		")", "GOB", ")", ")", "(", "GAL", "(", "AMY", "WAR", "UKR", "RUM", "BUD", "VIE", "BOH", "SIL", ")", ")", "(", "GAS", "(", 
		"AMY", "PAR", "BUR", "MAR", "SPA", "BRE", ")", "(", "FLT", "(", "SPA", "NCS", ")", "MAO", "BRE", ")", ")", "(", "GRE", "(", 
		"AMY", "BUL", "ALB", "SER", ")", "(", "FLT", "(", "BUL", "SCS", ")", "AEG", "ION", "ALB", ")", ")", "(", "HEL", "(", "FLT", 
		"NTH", "DEN", "KIE", "HOL", ")", ")", "(", "HOL", "(", "AMY", "BEL", "KIE", "RUH", ")", "(", "FLT", "BEL", "NTH", "HEL", "KIE", 
		")", ")", "(", "ION", "(", "FLT", "TUN", "TYS", "NAP", "APU", "ADR", "ALB", "GRE", "AEG", "EAS", ")", ")", "(", "IRI", "(", 
		"FLT", "NAO", "LVP", "WAL", "ECH", "MAO", ")", ")", "(", "KIE", "(", "AMY", "HOL", "DEN", "BER", "MUN", "RUH", ")", "(", "FLT", 
		"HOL", "HEL", "DEN", "BAL", "BER", ")", ")", "(", "LON", "(", "AMY", "YOR", "WAL", ")", "(", "FLT", "YOR", "NTH", "ECH", "WAL", 
		")", ")", "(", "LVN", "(", "AMY", "PRU", "STP", "MOS", "WAR", ")", "(", "FLT", "PRU", "BAL", "GOB", "(", "STP", "SCS", ")", 
		")", ")", "(", "LVP", "(", "AMY", "WAL", "EDI", "YOR", "CLY", ")", "(", "FLT", "WAL", "IRI", "NAO", "CLY", ")", ")", "(", 
		"GOL", "(", "FLT", "(", "SPA", "SCS", ")", "MAR", "PIE", "TUS", "TYS", "WES", ")", ")", "(", "MAO", "(", "FLT", "NAO", "IRI", 
		"ECH", "BRE", "GAS", "(", "SPA", "NCS", ")", "POR", "(", "SPA", "SCS", ")", "NAF", "WES", ")", ")", "(", "MAR", "(", "AMY", 
		"SPA", "PIE", "GAS", "BUR", ")", "(", "FLT", "(", "SPA", "SCS", ")", "GOL", "PIE", ")", ")", "(", "MOS", "(", "AMY", "STP", 
		"LVN", "WAR", "UKR", "SEV", ")", ")", "(", "MUN", "(", "AMY", "BUR", "RUH", "KIE", "BER", "SIL", "BOH", "TYR", ")", ")", "(", 
		"NAF", "(", "AMY", "TUN", ")", "(", "FLT", "MAO", "WES", "TUN", ")", ")", "(", "NAO", "(", "FLT", "NWG", "LVP", "IRI", "MAO", 
		"CLY", ")", ")", "(", "NAP", "(", "AMY", "ROM", "APU", ")", "(", "FLT", "ROM", "TYS", "ION", "APU", ")", ")", "(", "NWY", 
		"(", "AMY", "FIN", "STP", "SWE", ")", "(", "FLT", "SKA", "NTH", "NWG", "BAR", "(", "STP", "NCS", ")", "SWE", ")", ")", "(", 
		"NTH", "(", "FLT", "YOR", "EDI", "NWG", "NWY", "SKA", "DEN", "HEL", "HOL", "BEL", "ECH", "LON", ")", ")", "(", "NWG", "(", "FLT", 
		"NAO", "BAR", "NWY", "NTH", "CLY", "EDI", ")", ")", "(", "PAR", "(", "AMY", "BRE", "PIC", "BUR", "GAS", ")", ")", "(", "PIC", 
		"(", "AMY", "BUR", "PAR", "BRE", "BEL", ")", "(", "FLT", "BRE", "ECH", "BEL", ")", ")", "(", "PIE", "(", "AMY", "MAR", "TUS", 
		"VEN", "TYR", ")", "(", "FLT", "MAR", "GOL", "TUS", ")", ")", "(", "POR", "(", "AMY", "SPA", ")", "(", "FLT", "MAO", "(", 
		"SPA", "NCS", ")", "(", "SPA", "SCS", ")", ")", ")", "(", "PRU", "(", "AMY", "WAR", "SIL", "BER", "LVN", ")", "(", "FLT", 
		"BER", "BAL", "LVN", ")", ")", "(", "ROM", "(", "AMY", "TUS", "NAP", "VEN", "APU", ")", "(", "FLT", "TUS", "TYS", "NAP", ")", 
		")", "(", "RUH", "(", "AMY", "BUR", "BEL", "HOL", "KIE", "MUN", ")", ")", "(", "RUM", "(", "AMY", "SER", "BUD", "GAL", "UKR", 
		"SEV", "BUL", ")", "(", "FLT", "SEV", "BLA", "(", "BUL", "ECS", ")", ")", ")", "(", "SER", "(", "AMY", "TRI", "BUD", "RUM", 
		"BUL", "GRE", "ALB", ")", ")", "(", "SEV", "(", "AMY", "UKR", "MOS", "RUM", "ARM", ")", "(", "FLT", "RUM", "BLA", "ARM", ")", 
		")", "(", "SIL", "(", "AMY", "MUN", "BER", "PRU", "WAR", "GAL", "BOH", ")", ")", "(", "SKA", "(", "FLT", "NTH", "NWY", "DEN", 
		"SWE", ")", ")", "(", "SMY", "(", "AMY", "SYR", "CON", "ANK", "ARM", ")", "(", "FLT", "SYR", "EAS", "AEG", "CON", ")", ")", 
		"(", "SPA", "(", "AMY", "GAS", "POR", "MAR", ")", "(", "(", "FLT", "NCS", ")", "GAS", "MAO", "POR", ")", "(", "(", "FLT", 
		"SCS", ")", "POR", "WES", "GOL", "MAR", "MAO", ")", ")", "(", "STP", "(", "AMY", "FIN", "LVN", "NWY", "MOS", ")", "(", "(", 
		"FLT", "NCS", ")", "BAR", "NWY", ")", "(", "(", "FLT", "SCS", ")", "FIN", "LVN", "GOB", ")", ")", "(", "SWE", "(", "AMY", 
		"FIN", "DEN", "NWY", ")", "(", "FLT", "FIN", "GOB", "BAL", "DEN", "SKA", "NWY", ")", ")", "(", "SYR", "(", "AMY", "SMY", "ARM", 
		")", "(", "FLT", "EAS", "SMY", ")", ")", "(", "TRI", "(", "AMY", "TYR", "VIE", "BUD", "SER", "ALB", "VEN", ")", "(", "FLT", 
		"ALB", "ADR", "VEN", ")", ")", "(", "TUN", "(", "AMY", "NAF", ")", "(", "FLT", "NAF", "WES", "TYS", "ION", ")", ")", "(", 
		"TUS", "(", "AMY", "ROM", "PIE", "VEN", ")", "(", "FLT", "ROM", "TYS", "GOL", "PIE", ")", ")", "(", "TYR", "(", "AMY", "MUN", 
		"BOH", "VIE", "TRI", "VEN", "PIE", ")", ")", "(", "TYS", "(", "FLT", "WES", "GOL", "TUS", "ROM", "NAP", "ION", "TUN", ")", ")", 
		"(", "UKR", "(", "AMY", "RUM", "GAL", "WAR", "MOS", "SEV", ")", ")", "(", "VEN", "(", "AMY", "TYR", "TUS", "ROM", "PIE", "APU", 
		"TRI", ")", "(", "FLT", "APU", "ADR", "TRI", ")", ")", "(", "VIE", "(", "AMY", "TYR", "BOH", "GAL", "BUD", "TRI", ")", ")", 
		"(", "WAL", "(", "AMY", "LVP", "LON", "YOR", ")", "(", "FLT", "LVP", "IRI", "ECH", "LON", ")", ")", "(", "WAR", "(", "AMY", 
		"SIL", "PRU", "LVN", "MOS", "UKR", "GAL", ")", ")", "(", "WES", "(", "FLT", "MAO", "(", "SPA", "SCS", ")", "GOL", "TYS", "TUN", 
		"NAF", ")", ")", "(", "YOR", "(", "AMY", "EDI", "LON", "LVP", "WAL", ")", "(", "FLT", "EDI", "NTH", "LON", ")", ")", ")"};
	public static final String[] INITIAL_SCO = "SCO ( AUS BUD VIE TRI ) ( ENG EDI LON LVP ) ( FRA PAR BRE MAR ) ( GER MUN BER KIE ) ( ITA NAP ROM VEN ) ( RUS MOS WAR SEV STP ) ( TUR ANK CON SMY ) ( UNO SER BEL DEN GRE HOL NWY POR RUM SWE TUN BUL SPA )".split(" ");
	public static final String[] INITIAL_NOW = "NOW ( SPR 1901 ) ( AUS AMY BUD ) ( AUS AMY VIE ) ( AUS FLT TRI ) ( ENG FLT EDI ) ( ENG FLT LON ) ( ENG AMY LVP ) ( FRA AMY PAR ) ( FRA FLT BRE ) ( FRA AMY MAR ) ( GER AMY MUN ) ( GER AMY BER ) ( GER FLT KIE ) ( ITA FLT NAP ) ( ITA AMY ROM ) ( ITA AMY VEN ) ( RUS AMY MOS ) ( RUS AMY WAR ) ( RUS FLT SEV ) ( RUS FLT ( STP SCS ) ) ( TUR FLT ANK ) ( TUR AMY CON ) ( TUR AMY SMY )".split(" ");
	
	protected Hashtable<String, Province> provinces;
	protected Hashtable<String, Power> powers;
	protected Hashtable<String, Region> regions;
	
	public GameBuilder(String[] mdf, String[] homes) {
		if(mdf == null){
			mdf = DEFAULT_MDF;
		}
		if(homes == null){
			homes = DEFAULT_HOMES;
		}
		//powers
		String[] msgs = ParseService.takeRight(mdf,1);
		int powerEnds = ParseService.findClose(msgs);
		String strPowers[] = ParseService.takeLeft(msgs, powerEnds);
		createPowers(strPowers);
		
		//provinces
		String provAndAdj[] = ParseService.takeRight(msgs, powerEnds);
		int provinceEnds = ParseService.findClose(provAndAdj);
		String strProvinces[] = ParseService.takeLeft(provAndAdj, provinceEnds);
		createProvinces(strProvinces);
		
		//adjacencies
		String adjacencies[] = ParseService.takeRight(provAndAdj, provinceEnds);
		createRegions(adjacencies);
		processAdjacencies(adjacencies);
		
		powers.remove("UNO");
		
		setHomes(DEFAULT_HOMES);
	}
	
	public GameBuilder(String mdf[]) {	
		this(mdf, DEFAULT_HOMES);
	}
	
	public GameBuilder(){
		this(DEFAULT_MDF, DEFAULT_HOMES);
		
	}
	
	/**
	 * Sets the homes
	 * 
	 * @param scoMessage
	 */
	private void setHomes(String scoMessage[]) {
		int closingBracket;
		scoMessage = ParseService.takeRight(scoMessage, 1);
		while (scoMessage.length > 0) {
			closingBracket = ParseService.findClose(scoMessage);
			String singlePower[] = ParseService.takeLeft(scoMessage, closingBracket);
			Power power =  powers.get(singlePower[1]);
			if(power!=null){
				for (int i = 2; i < singlePower.length - 1; i++) {
					Province home = (Province)provinces.get(singlePower[i]);
					power.addHome(home);
				}
			}
			scoMessage = ParseService.takeRight(scoMessage,closingBracket);
		}
	}

	
	public void createPowers(String powersMsg[]) {
		powers = new Hashtable<String, Power>();
		for (int i = 1; !powersMsg[i].equals(")"); i++){
			powers.put(powersMsg[i], createPower(powersMsg[i]));
		}
		powers.put("UNO", createPower("UNO")); // UNO nom�s ho fem servir al builder, despr�s Game queda normalitzat
	}

	public void createProvinces(String provincesMsg[]) {
		provinces = new Hashtable<String, Province>();
		provincesMsg = ParseService.takeRight(provincesMsg, 1);
		int closingBracket = ParseService.findClose(provincesMsg);
		String supplyCentres[] = ParseService.takeLeft(provincesMsg, closingBracket);
		String nonSupplyCentres[] = ParseService.takeRight(provincesMsg, closingBracket);
		createSupplyCentres(supplyCentres);
		createNonSupplyCentres(nonSupplyCentres);
	}
	
	public void createRegions(String adjacenciesMsg[]) {
		regions = new Hashtable<String, Region>();
		String inputRemaining[] = ParseService.takeRight(adjacenciesMsg, 1);
		int closingBracket = ParseService.findClose(inputRemaining);
		while (closingBracket != -1) {
			Province workingProvince = provinces.get(inputRemaining[1]);
			String twoCoasts[] = new String[2];
			twoCoasts = hasCoasts(inputRemaining);
			if (twoCoasts[0] == null) {
				if (isCoastal(inputRemaining)) {
					String node0str = inputRemaining[1]+"AMY";
					String node1str = inputRemaining[1]+"FLT";
					Region node0 = createRegion(node0str);
					Region node1 = createRegion(node1str);
					regions.put(node0str, node0);
					regions.put(node1str, node1);
					node0.setProvince(workingProvince);
					workingProvince.addRegion(node0);
					node1.setProvince(workingProvince);
					workingProvince.addRegion(node1);
				} else if (inputRemaining[3].equals("AMY")) {
					Region node0 = createRegion(inputRemaining[1]+"AMY");
					regions.put(inputRemaining[1]+"AMY", node0);
					node0.setProvince(workingProvince);
					workingProvince.addRegion(node0);
				} else {
					Region node1 = createRegion(inputRemaining[1]+"FLT");
					regions.put(inputRemaining[1]+"FLT", node1);
					node1.setProvince(workingProvince);
					workingProvince.addRegion(node1);
				}
			} else {
				String node0str = inputRemaining[1]+"AMY";
				String node1str = inputRemaining[1]+twoCoasts[0];
				String node2str = inputRemaining[1]+twoCoasts[1];
				Region node0 = createRegion(node0str);
				Region node1 = createRegion(node1str);
				Region node2 = createRegion(node2str);
				regions.put(node0str, node0);
				regions.put(node1str, node1);
				regions.put(node2str, node2);
				node0.setProvince(workingProvince);
				workingProvince.addRegion(node0);
				node1.setProvince(workingProvince);
				workingProvince.addRegion(node1);
				node2.setProvince(workingProvince);
				workingProvince.addRegion(node2);
			}
			inputRemaining = ParseService.takeRight(inputRemaining, closingBracket);
			
			closingBracket = ParseService.findClose(inputRemaining);
		}

	}

	public void processAdjacencies(String adjacenciesMsg[]) {
		adjacenciesMsg = ParseService.takeRight(adjacenciesMsg, 1);
		for (int provinceClosingBracket = ParseService.findClose(adjacenciesMsg); provinceClosingBracket != -1; provinceClosingBracket = ParseService.findClose(adjacenciesMsg)) {
			processProvinceAdjacencies(ParseService.takeLeft(adjacenciesMsg,
					provinceClosingBracket));
			adjacenciesMsg = ParseService.takeRight(adjacenciesMsg, provinceClosingBracket);
		}

	}
	
	public void processProvinceAdjacencies(String provinceAdjacencies[]) {
		Province province = provinces
				.get(provinceAdjacencies[1]);
		provinceAdjacencies = ParseService.takeRight(provinceAdjacencies, 2);
		for (int movementClosingBracket = ParseService.findClose(provinceAdjacencies); movementClosingBracket != -1; movementClosingBracket = ParseService.findClose(provinceAdjacencies)) {
			int i = 2;
			Region workingRegion;
			if (provinceAdjacencies[1].equals("AMY")) {
				workingRegion = regions
						.get((new StringBuilder(String.valueOf(province
								.getName()))).append("AMY").toString());
			} else if (provinceAdjacencies[1].equals("FLT")) {
				workingRegion = regions
						.get((new StringBuilder(String.valueOf(province
								.getName()))).append("FLT").toString());
			} else {
				workingRegion = regions.get((new StringBuilder(String
						.valueOf(province.getName()))).append(
						provinceAdjacencies[3]).toString());
				i = 5;
			}
			for (; i < movementClosingBracket - 1; i++) {
				if (provinceAdjacencies[i].equals("(")) {
					workingRegion.addAdjacentRegion(regions.get((new StringBuilder(String
									.valueOf(provinceAdjacencies[i + 1])))
									.append(provinceAdjacencies[i + 2])
									.toString()));
					i += 3;
				} else {
					String nodeName = provinceAdjacencies[1].equals("AMY") ? (new StringBuilder(
							String.valueOf(provinceAdjacencies[i]))).append(
							"AMY").toString()
							: (new StringBuilder(String
									.valueOf(provinceAdjacencies[i]))).append(
									"FLT").toString();
					workingRegion.addAdjacentRegion(regions.get(nodeName));
				}
			}

			provinceAdjacencies = ParseService.takeRight(provinceAdjacencies,
					movementClosingBracket);
		}

	}
	
	public void createNonSupplyCentres(String nonSupplyCentres[]) {
		int closingBracket = ParseService.findClose(nonSupplyCentres);
		for (int i = 1; i < closingBracket - 1; i++) {
			provinces.put(nonSupplyCentres[i], createProvince(nonSupplyCentres[i]));
		}
	}

	public void createSupplyCentres(String supplyCentres[]) {
		supplyCentres = ParseService.takeRight(supplyCentres, 1);
		int closingBracket = ParseService.findClose(supplyCentres);
		String[] singlePower;
		Province owned;
		Power tempPower;
		while (closingBracket != -1) {
			singlePower = ParseService.takeLeft(supplyCentres, closingBracket);
			tempPower =  powers.get(singlePower[1]);
			for (int i = 2; i < singlePower.length - 1; i++) {
				owned = createProvince(singlePower[i]);
				owned.markAsSC();
				tempPower.addOwn(owned);
				provinces.put(singlePower[i], owned);
			}
			supplyCentres = ParseService.takeRight(supplyCentres, closingBracket);
			closingBracket = ParseService.findClose(supplyCentres);
		}
	}

	public boolean isCoastal(String inputRemaining[]) {
		int closingBracket = ParseService.findClose(inputRemaining);
		String testProvince[] = ParseService.takeLeft(inputRemaining, closingBracket);
		int i = 0;
		for (i = 0; i < testProvince.length - 1; i++) {
			if (testProvince[i].equals(")") && testProvince[i + 1].equals("(")) {
				return true;
			}
		}

		return false;
	}

	public String[] hasCoasts(String inputRemaining[]) {
		int closingBracket = ParseService.findClose(inputRemaining);
		String testProvince[] = ParseService.takeLeft(inputRemaining, closingBracket);
		int i = 0;
		String coasts[] = new String[2];
		for (i = 0; i < testProvince.length - 2; i++) {
			if (testProvince[i].equals("(") && testProvince[i + 1].equals("(")
					&& testProvince[i + 2].equals("FLT")) {
				if (coasts[0] == null) {
					coasts[0] = testProvince[i + 3];
				} else {
					coasts[1] = testProvince[i + 3];
				}
			}
		}

		return coasts;
	}
	
	public Game getGame(){
		Game map = new Game(powers, provinces, regions);
		return map;
	}

	public Hashtable<String, Province> getProvinces() {
		return provinces;
	}

	public Hashtable<String, Power> getPowers() {
		return powers;
	}

	public Hashtable<String, Region> getRegions() {
		return regions;
	}
	
	protected Region createRegion(String name){
		return new Region(name);
	}
	
	protected Power createPower(String name){
		return new Power(name);
	}
	
	protected Province createProvince(String name){
		return new Province(name);
	}
	
	
	/**
	 * Creates a game state
	 * @param MDF
	 * @param homes
	 * @param SCO
	 * @param NOW
	 * @return
	 */
	public static Game createGame(String[] MDF, String[] homes, String[] SCO, String[] NOW){
		GameBuilder mdf = new GameBuilder(MDF, homes);
		Game game = mdf.getGame();
		game.setPhase(Phase.valueOf(NOW[2]));
		game.setYear(NOW[3]);
		Parser.updateOwnedSCs(SCO, game);
		Parser.updateControlledRegions(NOW, game);
		return game;
	}

	/**
	 * Creates a game state of the standard Diplomacy game
	 * @param sco
	 * @param now
	 * @return
	 */
	public static Game createGame(String[] sco, String[] now) {
		return createGame(GameBuilder.DEFAULT_MDF, GameBuilder.DEFAULT_HOMES, sco, now);
	}
	
	/**
	 * Creates the first game state of the standard Diplomacy game
	 * @return
	 */
	public static Game createGame() {
		return createGame(GameBuilder.INITIAL_SCO, GameBuilder.INITIAL_NOW);
	}
}
