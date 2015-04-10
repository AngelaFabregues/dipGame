package org.dipgame.gameManager;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/** 
 * Runs games as GameManager does but without a GUI. It is very useful for running experiments that consist on several game executions.
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class ConsoleGameManager {
	
	private HashMap<String, String> loadedPaths;
	private HashMap<String, String> loadedPlayers;
	private List<String[]> players;
	private List<Process> processes;
	private List<String> alerts;
	private boolean canRun = true;
	private boolean isPlaying;
	
	private static final int GAME_PORT = 16713;

	private static final int MTL = 300;

	private static final int RTL = 120;

	private static final int BTL = 120;
	

	private String gameId = "ConsoleGameManager";

	private int time = 12000;

	public ConsoleGameManager(List<String[]> players) throws IOException {
		this.players = players;
		alerts = new Vector<String>();
		loadPaths();
		checkPaths();
		loadPlayers();
	}
	
	private void loadPaths() throws IOException {
		loadedPaths = new HashMap<String, String>();
		FileReader fr = new FileReader(new File("files/paths.txt"));
		LineNumberReader ln = new LineNumberReader(fr);
		String linea = ln.readLine();
		while (linea != null) {
			String[] words = linea.split(";");
			if(words[0].equals("JAVA_ENV")){
				loadedPaths.put("JAVA_ENV", words[1]);
			}else if(words[0].equals("PARLANCE_PATH")){
				loadedPaths.put("PARLANCE_PATH", words[1]);
			}else if(words[0].equals("AISERVER_PATH")){
				loadedPaths.put("AISERVER_PATH", words[1]);
			}else if(words[0].equals("AIMAPPER_PATH")){
				loadedPaths.put("AIMAPPER_PATH", words[1]);
			}
			linea = ln.readLine();
		}
	}

	private void checkPaths() {
		List<String> pathAlerts = new Vector<String>();
		if(!loadedPaths.containsKey("JAVA_ENV")){
			loadedPaths.put("JAVA_ENV", "java");
			pathAlerts.add("'JAVA_ENV' is set to 'java'");
		}
		if(!loadedPaths.containsKey("PARLANCE_PATH")){
			loadedPaths.put("PARLANCE_PATH", "parlance-server");
			pathAlerts.add("'PARLANCE_PATH' is set to 'parlance-server'");
		}
		if(pathAlerts.size()>0){
			String alertMessage = "Paths to the java and parlance-server should be provided in the file 'paths.txt'. The application is being executed with the following paths set to default:\n";
			for(String pathAlert: pathAlerts){
				alertMessage+=pathAlert+"\n";
			}
			alertMessage+="If Game Manager cannot find java and parlance-server, the execution of the application fails.";
			alerts.add(alertMessage);
		}
		
	    String vers = System.getProperty("os.name").toLowerCase();
		if(vers.contains("windows")){
			//Path checking work for linux and unix. TODO implement a path checking for windows. 
		}else{
			for(String key: loadedPaths.keySet()){
				File path = new File(loadedPaths.get(key));
				if(!path.exists() || !path.canExecute()){
					alerts.add("Game Manager cannot find '"+key+"' that is set to '"+path+"'. This should be fixed in order to be able to run games.");
					canRun  = false;
				}
			}
		}
	}
	
	private void loadPlayers() throws IOException {
		loadedPlayers = new HashMap<String, String>();
		FileReader fr = new FileReader(new File("files/availablePlayers.txt"));
		LineNumberReader ln = new LineNumberReader(fr);
		String line = ln.readLine();
		try{
			while (line != null) {
			
				line = line.trim();
				if(line.length()!=0 && !line.startsWith("//")){ //ignoring empty lines and comments
					String key = line.substring(0, line.indexOf(';'));
					String value = line.substring(line.indexOf(';'+1));
					loadedPlayers.put(key, value);
				}
				line = ln.readLine();
			}
		}catch (StringIndexOutOfBoundsException e) {
			alerts.add("Syntax error in file 'files/availablePlayers.txt' line: '"+line+"'.");
			canRun = false;
		}
	}
	
	private void run() {
		System.out.println();
		for(String alert: alerts){
			System.out.println(alert);
		}
		System.out.println();
		if(!canRun){
			System.out.println("It is not possible to run ConsoleGameManager. You should set it up first.");
			return;
		}
		
		
		
		//If the application is killed, it stopes all processes belonging to it.
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run() {
			    destroyProcesses();
			}
		});
		
		processes = new Vector<Process>(9);
		try{

			if(loadedPaths.containsKey("AISERVER_PATH")){
				//Runs AiServer
		        String[] cmd = {"cmd", "/c", loadedPaths.get("AISERVER_PATH")+"//AiServer",  "-var=standard",  "-port="+GAME_PORT,
						"-start", "-exit=60", "-h", "-lvl=0", "-mtl="+MTL, "-rtl="+RTL, "-btl="+BTL, "-npr", "-npb", "-ptl=0"};
		        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
		        processBuilder.directory(new File(loadedPaths.get("AISERVER_PATH")));
		        processes.add(processBuilder.start());				
			}else{
				//Runs parlance-server
				String[] cmd = { loadedPaths.get("PARLANCE_PATH"), "-g1","standard" };
				processes.add(Runtime.getRuntime().exec(cmd));	
			}
			
			String call = processProgramCall(loadedPaths, "<JAVA_ENV> -jar programs/negoServer-2.0.3-full.jar <nego_port> <ip> <port> <game_id> STDOUT <time>", "negoServer");
			Process observer = Runtime.getRuntime().exec(call);
			processes.add(observer);
	
			isPlaying = true;
			boolean isFinished = false;
			boolean isNegoStarted = false;
			InputStreamReader reader = new InputStreamReader(observer.getInputStream());
			int n;
			String readText="";
			char[] buffer;
			do{
				buffer = new char[512];
				n = reader.read(buffer);
				if(n>0){
					readText = new String(buffer,0,n);
					//An observer has connected. Have 0 players and 1 observer. Need 7 to start
					if(!isNegoStarted && (readText.contains("An observer has connected.") || readText.contains("1 observer."))){
						isNegoStarted = true;
						//Run players
						for (String[] player : players) {
							if (!player[0].equals(Utils.LEAVE_EMPTY)) {
								String programCall = processProgramCall(loadedPaths,
										loadedPlayers.get(player[0]), player[1]);
								processes.add(Runtime.getRuntime()
										.exec(programCall));
							}
						}
					}else{
						if(!isNegoStarted && (readText.contains(" YES ( MAP (") || readText.contains(" 'standard' ) )"))){ 
							isFinished = true;
						}
					}
					System.out.println(readText);
				}
				Thread.currentThread().sleep(100);
			}while(isPlaying && (n==buffer.length || !isFinished)); //Reads until cancel button is pressed or incomplete buffer is read after finding a "Winner" string.
			reader.close();
			if(isFinished){
				destroyProcesses();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Looks or tags and translates them to their values
	 * @param loadedPaths 
	 * @param programCallTemplate
	 * @param playerName
	 * @return
	 */
	private String processProgramCall(HashMap<String, String> loadedPaths, String programCallTemplate, String playerName) {
		String programCall = "";
		String tag = "";
		int i=0;
		try{
			boolean isTranslating = false;
			for(; i<programCallTemplate.length(); i++){
				char c = programCallTemplate.charAt(i);
				if(c=='<'){
					if(!isTranslating){
						isTranslating = true;
					}else{
						System.err.println("Syntax error in "+programCallTemplate.substring(0, i));
					}
				}else if(c=='>'){
					if(isTranslating){
						programCall += translateTag(loadedPaths, tag, playerName);
						tag = "";
						isTranslating = false;
					}else{
						System.err.println("Syntax error in "+programCallTemplate.substring(0, i));
					}
				}else{
					if(isTranslating){
						tag += c;
					}else{
						programCall += c;
					}
				}
			}
		}catch (Exception e) {
			System.err.println("Syntax error in "+programCallTemplate.substring(0, i)+". "+e.getMessage());
		}
		return programCall;
	}
	
	/**
	 * Translates tags to their value
	 * @param loadedPaths 
	 * @param tag
	 * @param playerName
	 * @return
	 * @throws ParseException
	 */
	private String translateTag(HashMap<String,String> loadedPaths, String tag, String playerName) throws Exception{
		if(tag.equals("ip")){
			return "localhost";
		}else if(tag.equals("port")){
			return GAME_PORT+"";
		}else if(tag.equals("name")){
			return playerName;
		}else if(tag.equals("log_folder")){
			return Utils.LOG_FOLDER;
		}else if(tag.equals("nego_ip")){
			return "localhost";
		}else if(tag.equals("nego_port")){
			return (GAME_PORT+1)+"";
		}else if(tag.equals("game_id")){
			return gameId;
		}else if(tag.equals("time")){
			return time+"";
		}else if(loadedPaths.containsKey(tag)){
			return loadedPaths.get(tag);
		}else{
			throw new Exception("Unknown tag '"+tag+"'.");
		}
	}
	
	private void destroyProcesses() {
		isPlaying = false;
		for (Process process: processes) {
			process.destroy();
		}
	}
	
	static public void main(String argv[]) throws Exception {
		if(argv.length == 0){
			System.out.println("Usage:\n ConsoleGameManager [<player> <player> <player> <player> <player> <player> <player>]");
			System.out.println("By default, ConsoleGameManager runs a game with 7 dumbbots.");
			List<String[]> players = new Vector<String[]>();
			for(int i=0; i<7; i++){
				players.add(new String[]{"dumbbot","player_"+i});
			}
			ConsoleGameManager a = new ConsoleGameManager(players);
			a.run();
		}else if(argv.length==7){
			List<String[]> players = new Vector<String[]>();
			for(int i=0; i<7; i++){
				players.add(new String[]{argv[i],"player_"+i});
			}
			ConsoleGameManager a = new ConsoleGameManager(players);
			a.run();
		}else{
			System.err.println("Usage:\n ConsoleGameManager [<player> <player> <player> <player> <player> <player> <player>]");
		}
	}
}