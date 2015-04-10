package org.dipgame.gameManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.dipgame.gameManager.utils.BareBonesBrowserLaunch;

/**
 * LogFrame
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class LogFrame extends JFrame{
	
	private static final int GAME_PORT = 16713;
	private static final int NEGO_PORT = 16714;

	private static final int MTL = 300;

	private static final int RTL = 120;

	private static final int BTL = 120;
	
	private int time = 7200000; //2h

	private int applicationHeight;
	
	private JButton cancel;
	private JButton save;
	
	private JPanel gameMainPanel;
	private BlueTextBar description;
	private BlueTextBar footer;
	private WhiteTextArea textArea;
	
	private List<Process> processes = new Vector<Process>();
	private List<Process> humans = new Vector<Process>(); //We don't close ChatApps automatically
	private boolean isPlaying;
	
	private GameManager app;

	private String gameId = "gameManager";

	public LogFrame(GameManager app, int applicationHeight){
		this.app = app;
		this.applicationHeight = applicationHeight;
		this.setTitle("gameManager - The DipGame Game Manager output log");
		
		this.setBackground(Utils.CYAN);
		this.setSize(new Dimension(800, this.applicationHeight));
		
		WindowListener wle = new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				closing();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(wle);
		
		this.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
			}

			@Override
			public void componentResized(ComponentEvent e) {
				setSizes();
				revalidateAll();
			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});
		
		gameMainPanel = new JPanel();
		gameMainPanel.setPreferredSize(this.getSize());
		gameMainPanel.setBackground(Utils.CYAN);
		BoxLayout boxLayout = new BoxLayout(gameMainPanel, BoxLayout.Y_AXIS);
		gameMainPanel.setLayout(boxLayout);
		gameMainPanel.add(MainPanel.createHeader());
		
		
		MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				BareBonesBrowserLaunch.openURL("http://www.dipgame.org/browse/gameManager");
			}
		};
		description = new BlueTextBar("GameManager output log following messages of DAIDE language at level 0 and L negotiation language", adapter);
		gameMainPanel.add(description);
		
		
		
		textArea = new WhiteTextArea();
		gameMainPanel.add(textArea);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Utils.CYAN);
		cancel = new JButton("Cancel game");
		cancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(!cancel.isEnabled()){
					return;
				}
				isPlaying = false;
				destroyProcesses();
			}
		});
		cancel.setEnabled(true);
		buttonPanel.add(cancel);
		
		save = new JButton("Save game");
		save.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e){
				if(!save.isEnabled()){
					return;
				}
				boolean shouldRepeat;
				do{
					shouldRepeat = false;
					JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
					int returnVal = fc.showSaveDialog(gameMainPanel);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						if(file.exists()){
							shouldRepeat = true;
							JOptionPane.showMessageDialog(null, "The specified file name already exists. Please try again with a valid name or a different path.", "File name already exists", JOptionPane.ERROR_MESSAGE);
						}else{
							try {
								file.createNewFile();
								BufferedWriter output = new BufferedWriter(new FileWriter(file));
								output.write(textArea.getText());
								output.close();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}
				}while(shouldRepeat);
			}
		});
		save.setEnabled(false);
		buttonPanel.add(save);
		
		JButton close = new JButton("Close");
		close.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				closing();
			}
		});
		close.setEnabled(true);
		buttonPanel.add(close);
		
		gameMainPanel.add(buttonPanel);
		
		footer = new BlueTextBar(Utils.URL, Utils.createLink(Utils.URL));//MainPanel.createFooter(800-30);
		gameMainPanel.add(footer);

		JScrollPane scroll = new JScrollPane(gameMainPanel);
		scroll.setMinimumSize(new Dimension(this.getWidth(), applicationHeight));
		this.setContentPane(scroll);
		setSizes();
	}

	public void run(HashMap<String, String> loadedPaths,
			HashMap<String, String> loadedPlayers, List<String[]> players) {
		
		//If the application is killed, it stopes all processes belonging to it.
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run() {
			    destroyProcesses();
			}
		});
		
		processes = new Vector<Process>(8);
		try{
			if(loadedPaths.containsKey("AISERVER_PATH")){
				//Runs AiServer
		        String[] cmd = {"cmd", "/c", loadedPaths.get("AISERVER_PATH")+"//AiServer",  "-var=standard",  "-port="+GAME_PORT,
						"-start", "-exit=60", "-h", "-lvl=0", "-mtl="+MTL, "-rtl="+RTL, "-btl="+BTL, "-npr", "-npb", "-ptl=0"};
		        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
		        processBuilder.directory(new File(loadedPaths.get("AISERVER_PATH")));
		        processes.add(processBuilder.start());				
			}else{
				//Runs parlance-server setup. Generates ~/.config/parlance.cfg file
				Runtime.getRuntime().exec("programs/launch_parlance -m "+MTL+" -r "+RTL+" -b "+BTL+" -l 0");
				//Runs parlance-server for one only standard game
				String[] cmd = { loadedPaths.get("PARLANCE_PATH"), "-g1","standard" };
				processes.add(Runtime.getRuntime().exec(cmd));	
			}

			String call;
			if(app.isBotNegotiation()){
				call = processProgramCall(loadedPaths, "<JAVA_ENV> -jar programs/negoServer-2.1-full.jar bot <nego_port> <ip> <port> STDOUT "+ time, "negoServer");
			}else{
				call = processProgramCall(loadedPaths, "<JAVA_ENV> -jar programs/negoServer-2.1-full.jar <nego_port> <ip> <port> STDOUT "+ time, "negoServer");
			}
			Process negoServer = Runtime.getRuntime().exec(call);
			processes.add(negoServer);
	
			boolean isFinished = false;
			try{
				isPlaying = true;
				boolean isNegoStarted = false;
				InputStreamReader reader = new InputStreamReader(negoServer.getInputStream());
				int n;
				String readText="";
				char[] buffer;
				do{
					buffer = new char[512];
					n = reader.read(buffer);
					if(n>0){
						readText = new String(buffer,0,n);
						//An observer has connected. Have 0 players and 1 observer. Need 7 to start
						if(!isNegoStarted && (readText.contains(" YES ( MAP (") || readText.contains(" 'standard' ) )"))){
							isNegoStarted = true;
							//Run players
							for (String[] player : players) {
								if (!player[0].equals(Utils.LEAVE_EMPTY)) {
									String programCall = processProgramCall(loadedPaths,
											loadedPlayers.get(player[0]), player[1]);
									if(player[0].equals("human") || player[0].equals("testBot")){
										humans.add(Runtime.getRuntime().exec(programCall));
									}else{
										processes.add(Runtime.getRuntime().exec(programCall));
									}
								}
							}
						}else{
							if(readText.contains("The game is over !.") || readText.contains("Stoping the server")|| readText.contains("Server off")){ //TODO made it redundant to avoid problems of buffering cutting. 
								isFinished = true;
							}
						}
						textArea.append(readText, Color.black, false);
					}
					Thread.currentThread().sleep(100);
				}while(isPlaying && (n==buffer.length || !isFinished)); //Reads until cancel button is pressed or incomplete buffer is read after finding a "Winner" string.
				reader.close();
			}catch (IOException e) {
				isFinished = true;
			}
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
			return (NEGO_PORT)+"";
		}else if(tag.equals("game_id")){
			return gameId;
		}else if(tag.equals("time")){
			return String.valueOf(time);
		}else if(loadedPaths.containsKey(tag)){
			return loadedPaths.get(tag);
		}else{
			throw new Exception("Unknown tag '"+tag+"'.");
		}
	}
	
	private void destroyProcesses() {
		isPlaying = false;
		cancel.setEnabled(false);
		save.setEnabled(true);
		for (Process process: processes) {
			process.destroy();
		}
	}
	
	void repaintAll(){
		cancel.repaint();		
	}

	public void closing(){
		if(cancel.isEnabled()){ //The game didn't end
			int closingWindow= JOptionPane.showConfirmDialog(null,
				"Closing this window you will cancel the game. Do you really wish to cancel the game?", "Please Confirm", JOptionPane.YES_NO_OPTION);
			if (closingWindow!=JOptionPane.YES_OPTION){
				return;
			}
		}
		close();
	}
	public void close() {
		destroyProcesses();
		for(Process human: humans){
			human.destroy();
		}
		this.setVisible(false);
	}

	public boolean isRunning() {
		return !save.isEnabled();
	}
	
	private void setSizes() {
		int width = getWidth()-40;
		description.setSizes();
		textArea.setSizes();
		footer.setSizes();
	}
	
	private void revalidateAll() {
		description.revalidateAll();
		textArea.revalidateAll();
		footer.revalidateAll();
		gameMainPanel.revalidate();
	}

}