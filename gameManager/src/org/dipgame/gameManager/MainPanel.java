package org.dipgame.gameManager;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.dipgame.gameManager.utils.BareBonesBrowserLaunch;
import org.dipgame.gameManager.utils.TextBox;

/**
 * MainPanel
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class MainPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_AISERVER_PATH = "PROGRAM_FILES//daide//aiserver";
	private static final String DEFAULT_AIMAPPER_PATH = "PROGRAM_FILES//daide//aimapper";
	
	private GameManager app;
	
	private HashMap<String, String> loadedPaths;
	private HashMap<String, String> loadedPlayers;
	private Vector<String> availablePlayerNames;
	
	private List<String[]> players;
	
	private List<Choice> choices = new Vector<Choice>();
	private List<JTextField> playerTextFields = new Vector<JTextField>();
	private Choice allChoice;
	private JButton setAllPlayers;
	private JButton newGame;
	
	private List<LogFrame> logFrames;

	private List<String> alerts;

	private boolean canRun = true;

	public MainPanel(GameManager app) throws Exception {
		this.app = app;
		alerts = new Vector<String>();
		logFrames = new Vector<LogFrame>();
		loadPaths();
		checkPaths();
		loadPlayers();

		this.setBackground(Utils.CYAN);
		
		JPanel container = new JPanel();
		container.setBackground(Utils.CYAN);
		BoxLayout boxLayout = new BoxLayout(container, BoxLayout.Y_AXIS);
		container.setLayout(boxLayout);
		container.add(createHeader());
		if(app.getHeight()==Utils.FULL_SIZE){
			container.add(createDescription());
		}else{
			container.add(createShortDescription(app.getHeight()));
		}
		container.add(createContent(app.getHeight()));
		if(app.getHeight()==Utils.FULL_SIZE){
			container.add(createFooter(320));
		}
		this.add(container);
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
				loadedPaths.put("AISERVER_PATH", fixProgramFiles(words[1]));
			}else if(words[0].equals("AIMAPPER_PATH")){
				loadedPaths.put("AIMAPPER_PATH", fixProgramFiles(words[1]));
			}
			linea = ln.readLine();
		}
		fr.close();
	}
	
	private String fixProgramFiles(String line){
		if(line.contains("PROGRAM_FILES")){
			try{
				String correctName = System.getenv("ProgramFiles");
				correctName = correctName.substring(2);
				correctName = "C://"+correctName;
				return line.replaceAll("PROGRAM_FILES", correctName);
			}catch (Exception e) {
			}
		}
		return line;
	}

	private void checkPaths() {
		List<String> pathAlerts = new Vector<String>();
		
		if(!loadedPaths.containsKey("JAVA_ENV")){
			String path = System.getProperty("java.home")+"/bin/java";
			loadedPaths.put("JAVA_ENV", path);
		}
		
		if(!loadedPaths.containsKey("PARLANCE_PATH") && !loadedPaths.containsKey("AISERVER_PATH")){
			String path = fixProgramFiles(DEFAULT_AISERVER_PATH);
			if (new File(path).isDirectory()) {
				loadedPaths.put("AISERVER_PATH", path);
				pathAlerts.add("'AISERVER_PATH' is set to '"+path+"'");
			}else{
				loadedPaths.put("PARLANCE_PATH", "parlance-server");
				pathAlerts.add("'PARLANCE_PATH' is set to 'parlance-server'");
			}
		}
		if(pathAlerts.size()>0){
			String alertMessage = "Paths to the java and game server (parlance or AiServer) should be provided in the file 'paths.txt'. The application will be executed with the following paths set to default:\n";
			for(String pathAlert: pathAlerts){
				alertMessage+=pathAlert+"\n";
			}
			alertMessage+="If Game Manager cannot find java and the game server, the execution of the application will fail.";
			alerts.add(alertMessage);
		}
		
	    String vers = System.getProperty("os.name").toLowerCase();
		if(vers.contains("windows")){
			if(!loadedPaths.containsKey("AIMAPPER_PATH")){
				String path = fixProgramFiles(DEFAULT_AIMAPPER_PATH);
				if (new File(path).isDirectory()) {
					loadedPaths.put("AIMAPPER_PATH", path);
					alerts.add("'AIMAPPER_PATH' is set to '"+path+"'.\nIf Game Manager cannot find the AiMapper, the human player will not launch any mapper.");
				}
			}
			
			if(!loadedPaths.containsKey("AISERVER_PATH") && !loadedPaths.containsKey("PARLANCE_PATH")){
				String path = fixProgramFiles(DEFAULT_AISERVER_PATH);
				if (new File(path).isDirectory()) {
					loadedPaths.put("AISERVER_PATH", path);
					alerts.add("'AISERVER_PATH' is set to '"+path+"'.\nIf Game Manager cannot find a game server, it will not be able to run games.");
				}else{
					loadedPaths.put("PARLANCE_PATH", "parlance-server");
					alerts.add("'PARLANCE_PATH' is set to '"+path+"'.\nIf Game Manager cannot find a game server, it will not be able to run games.");
				}
			}
			
		}else{
			
			for(String key: new String[]{"JAVA_ENV", "PARLANCE_PATH"}){
				if(loadedPaths.containsKey(key)){
					String path = fixProgramFiles(loadedPaths.get(key));
					File filPath = new File(path);
					if(!filPath.exists() || !filPath.canExecute()){
						alerts.add("Game Manager cannot find '"+key+"' that is set to '"+path+"'. This should be fixed in order to be able to run games.");
						canRun  = false;
					}
				}
			}
		}
		
	}
	
	private void loadPlayers() throws IOException {
		loadedPlayers = new HashMap<String, String>();
		availablePlayerNames = new Vector<String>(); //to keep the order of players in the file
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
					availablePlayerNames.add(key);
				}
				line = ln.readLine();
			}
		}catch (StringIndexOutOfBoundsException e) {
			alerts.add("Syntax error in file 'files/availablePlayers.txt' line: '"+line+"'.");
			canRun = false;
		}
	}

	static Component createHeader() {
		JPanel headerPanel = new JPanel();
		headerPanel.setBackground(Utils.CYAN);
		ImageIcon icon = new ImageIcon("files/img/dipgame.png");
		JLabel label = new JLabel();
		label.setIcon(icon);
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				BareBonesBrowserLaunch.openURL("http://www.dipgame.org");
			}
		});
		headerPanel.add(label, BorderLayout.CENTER);
		return headerPanel;
	}
	
	static JPanel createFooter(int width) {
		JPanel footer = new JPanel();
		footer.setBackground(Utils.CYAN);
		
		JPanel footerContent = new JPanel();
		footerContent.setBackground(Utils.BLUE);
		footerContent.setPreferredSize(new Dimension(width, 20));
		
		footerContent.add(Utils.getText("http://www.dipgame.org", "http://www.dipgame.org"));
		
		footer.add(Utils.addBordersToLine(footerContent));
		return footer;
	}
	
	static Component createDescription() {			
		JPanel descriptionPanel = new JPanel();
		descriptionPanel.setBackground(Utils.CYAN);

		JPanel descriptionContent = new JPanel();
		descriptionContent.setBackground(Utils.BLUE);

		int size = 200;
		TextBox text = new TextBox(Utils.BLUE);
		text.setPreferredSize(new Dimension(325, size));
		text.append(getDescriptionTxt(), Utils.CYAN, true);
		descriptionContent.add(text);
		descriptionContent.setPreferredSize(new Dimension(325, size));

		descriptionPanel.add(Utils.addBorders(descriptionContent));
		return descriptionPanel;
	}
	
	private static String getDescriptionTxt() {
		try {
			return  "Welcome to the DipGame Game Manager, the program for running Diplomacy games.\n" +
				"Select the desired players and press the 'New Game' button.\n" +
				"To take part of the game select a 'human' player. To connect an external client " +
				"select 'empty'. The server locations are :\ngame server\t\t\t\t\t  "
				+InetAddress.getLocalHost().getHostAddress()+":16713\nnegotiation server\t\t"
				+InetAddress.getLocalHost().getHostAddress()+":16714\n";
		} catch (UnknownHostException e) {
			//Never going to execute
			e.printStackTrace();
		}
		return null;
	}

	private Component createShortDescription(final int applicationHeight) {
		MouseAdapter mouseAdapter = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				DescriptionFrame descriptionFrame = new DescriptionFrame(applicationHeight);
				
				descriptionFrame.pack();
				descriptionFrame.setVisible(true);
			}
		};
		
		JPanel shortDescriptionPanel = new JPanel();
		shortDescriptionPanel.setBackground(Utils.CYAN);
		
		JPanel shortDescriptionContent = new JPanel();
		shortDescriptionContent.setBackground(Utils.BLUE);
		shortDescriptionContent.setPreferredSize(new Dimension(325, 20));
		
		shortDescriptionContent.add(Utils.getText("Game Manager. For more info click here.", mouseAdapter));
		
		shortDescriptionPanel.add(Utils.addBordersToLine(shortDescriptionContent));
		return shortDescriptionPanel;
	}
	
	void popupAlerts(int applicationHeight) {
		if(alerts.size()==0){
			return;
		}
		if(!canRun){
			this.setEnabledAll(false);
		}
		AlertsFrame alertsFrame = new AlertsFrame(alerts, applicationHeight);
		alertsFrame.pack();
		alertsFrame.setVisible(true);
	}

	private Component createContent(int applicationHeight) {
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBackground(Utils.CYAN);

		contentPanel.add(createAllPlayerSettings());
		contentPanel.add(createPlayerSettings());
		if(applicationHeight==Utils.FULL_SIZE){
			//contentPanel.add(createGameSettings());
		}
		contentPanel.add(createButtons(applicationHeight));
		return contentPanel;
	}

	private Component createPlayerSettings() {
		JPanel playerSettingsContainer = new JPanel();
		playerSettingsContainer.setBackground(Utils.CYAN);
		
		JPanel playerSettings = new JPanel();
		playerSettings.setPreferredSize(new Dimension(290,295));
		playerSettings.setBorder(BorderFactory.createTitledBorder("Players"));
		BoxLayout boxLayout = new BoxLayout(playerSettings, BoxLayout.Y_AXIS);
		playerSettings.setLayout(boxLayout);
		playerSettings.setBackground(Utils.CYAN);
		
		for (int i = 0; i < 7; i++) {
			playerSettings.add(createPlayerSetting(availablePlayerNames, i));
		}
		playerSettingsContainer.add(playerSettings);
		return playerSettingsContainer;
	}
	
	private JPanel createPlayerSetting(List<String> availablePlayerNames, int i) {
		JPanel playerSetting = new JPanel();
		playerSetting.setBackground(Utils.CYAN);
		
		Choice playerMenu = createPlayerMenu(availablePlayerNames);
		choices.add(playerMenu);
		playerSetting.add(playerMenu);
		
		JTextField playerName = new JTextField("player"+(i+1), 10);
		playerTextFields.add(playerName);
		playerSetting.add(playerName);
		return playerSetting;
	}
	
	private Choice createPlayerMenu(List<String> availablePlayerNames) {
		Choice playerMenu = new Choice();
		playerMenu.setPreferredSize(new Dimension(130, 30));
		playerMenu.setBackground(Color.WHITE);
		int index = 0;
		for(String line: availablePlayerNames){
			playerMenu.insert(line, index++);
		}
		playerMenu.insert(Utils.LEAVE_EMPTY, index++);
		return playerMenu;
	}

	private Component createAllPlayerSettings() {
		JPanel allPlayerSettings = new JPanel();
		allPlayerSettings.setBackground(Utils.CYAN);
		
		JPanel border = new JPanel();
		border.setBorder(BorderFactory.createTitledBorder("Set the same type to all players"));
		border.setPreferredSize(new Dimension(290,65));
		border.setBackground(Utils.CYAN);
		
		allChoice = createPlayerMenu(availablePlayerNames);
		border.add(allChoice);
		
		setAllPlayers = new JButton("Set all players");
		setAllPlayers.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				for(Choice menu: choices){
					menu.select(allChoice.getSelectedItem());
				}
			}
		});
		
		border.add(setAllPlayers);
		
		allPlayerSettings.add(border);
		return allPlayerSettings;
	}

	static Component createGameSettings() {
		JPanel gameSettingsContainer = new JPanel();
		gameSettingsContainer.setBackground(Utils.CYAN);
		JPanel gameSettings = new JPanel();
		gameSettings.setPreferredSize(new Dimension(290,70));
		BoxLayout boxLayout1 = new BoxLayout(gameSettings, BoxLayout.Y_AXIS);
		gameSettings.setLayout(boxLayout1);
		
		JPanel serverPanel = new JPanel();
		serverPanel.setBackground(Utils.CYAN);
		JLabel serverLabel = new JLabel("Server:",SwingConstants.RIGHT);
		serverLabel.setPreferredSize(new Dimension(127,10));
		serverPanel.add(serverLabel);
		JTextField serverInput = new JTextField(Utils.IP + ":" + Utils.PORT, 10);
		serverInput.setEnabled(false);
		serverPanel.add(serverInput);
		gameSettings.add(serverPanel);

		JPanel logFolderPanel = new JPanel(); //TODO decide whether to keep it or remove it from view. It is quite confusing right now. The 'g' of Log is not visible.
		logFolderPanel.setBackground(Utils.CYAN);
		JLabel logFolderLabel = new JLabel("Log folder:",SwingConstants.RIGHT);
		logFolderLabel.setPreferredSize(new Dimension(127,10));
		logFolderPanel.add(logFolderLabel);
		JTextField logFolderInput = new JTextField(Utils.LOG_FOLDER, 10);
		logFolderInput.setEnabled(false);
		logFolderPanel.add(logFolderInput);
		gameSettings.add(logFolderPanel);
		
		gameSettingsContainer.add(gameSettings);
		return gameSettingsContainer;
	}

	private Component createButtons(final int applicationHeight) {
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBackground(Utils.CYAN);
		buttonsPanel.setPreferredSize(new Dimension(325, 35));
		
		JButton exit = new JButton("Quit GameManager");
		exit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				app.exit();
			}
		});
		exit.setEnabled(true);

		buttonsPanel.add(exit);
		
		newGame = new JButton("New Game");
		newGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(!newGame.isEnabled()){
					return;
				}
				newGame.setEnabled(false);
				runNewGame(applicationHeight);
			}
		});
		buttonsPanel.add(newGame);
		
		return buttonsPanel;
	}
	

	
	static Component createDiplomacyImage() {
		JPanel imagePanelContainer = new JPanel();
		imagePanelContainer.setPreferredSize(new Dimension(320, 280));
		imagePanelContainer.setBackground(Utils.CYAN);
		JPanel imagePanel = new JPanel();
		imagePanel.setBackground(Utils.CYAN);
		ImageIcon icon = new ImageIcon("files/img/diplomacy.png");
		JLabel imageLabel = new JLabel();
		imageLabel.setIcon(icon);
		
		imageLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				BareBonesBrowserLaunch.openURL("http://www.dipgame.org/browse/diplomacy");
			}
		});
		imagePanel.add(imageLabel);
		imagePanel.setBorder(BorderFactory.createTitledBorder("The Diplomacy Game"));
		
		imagePanelContainer.add(imagePanel);
		return imagePanelContainer;
	}
	
	
	
	private void runNewGame(int applicationHeight) {
		players = new Vector<String[]>(7);
		for(int i=0; i<7;i++){
			players.add(new String[]{choices.get(i).getSelectedItem(), playerTextFields.get(i).getText()});
		}
		
		final LogFrame logFrame = new LogFrame(app, applicationHeight);
		logFrames.add(logFrame);
		logFrame.pack();
		logFrame.setVisible(true);
		this.setEnabledAll(false);
		
		new Thread(){
			public void run(){
				logFrame.run(loadedPaths, loadedPlayers, players);
				setEnabledAll(true);
			}
		}.start();
	}
	
	void repaintAll(){
		for(Choice choice: choices){
			choice.repaint();
		}
		for(JTextField textField: playerTextFields){
			textField.repaint();
		}
		allChoice.repaint();
		setAllPlayers.repaint();
		newGame.repaint();
	}
	
	void setEnabledAll(boolean value){
		for(Choice choice: choices){
			choice.setEnabled(value);
		}
		for(JTextField textField: playerTextFields){
			textField.setEnabled(value);
		}
		allChoice.setEnabled(value);
		setAllPlayers.setEnabled(value);
		newGame.setEnabled(value);
	}

	public List<LogFrame> getLogFrames() {
		return logFrames;
	}
}