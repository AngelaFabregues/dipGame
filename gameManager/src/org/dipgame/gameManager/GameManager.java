package org.dipgame.gameManager;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Runs the application as a desktop application loading files contained into the project folder
 *
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class GameManager {

	private MainFrame mainFrame;
	
	
	private int applicationHeight = Utils.FULL_SIZE;


	private boolean isBotNegotiation = false;
	
	public GameManager() throws Exception {
		removeOldLogs();
		checkScreenResolution();
		
		mainFrame = new MainFrame(this);
		WindowListener wle = new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				
			}
		};
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mainFrame.addWindowListener(wle);
	}

	private void removeOldLogs() {
		for(File file: (new File("logs")).listFiles()){
			file.delete();
		}
	}

	private void popupAlerts() {
		mainFrame.getMainPanel().popupAlerts(applicationHeight);
	}
	
	private void checkScreenResolution() {
		// Get the default toolkit
		Toolkit toolkit = Toolkit.getDefaultToolkit();

		// Get the current screen size
		Dimension scrnsize = toolkit.getScreenSize();
		if(scrnsize.getHeight() < Utils.FULL_SIZE){
			applicationHeight = Utils.MIN_SIZE;
		}
	}
	
	public void exit() {
		
		String text = "";
		if(isRunningGames()){
			text+="Game Manager is currently running games. Exiting will cancel those games. ";
		}
		text+="Do you really want to exit?";
		int exitApp= JOptionPane.showConfirmDialog(null,
				text, "Please Confirm", JOptionPane.YES_NO_OPTION);
		if (exitApp==JOptionPane.YES_OPTION){
			for(LogFrame logFrame: mainFrame.getMainPanel().getLogFrames()){
				logFrame.close();
			}
			System.exit(0);
		}
	}
	
	public boolean isRunningGames() {
		for(LogFrame logFrame : mainFrame.getMainPanel().getLogFrames()){
			if(logFrame.isRunning()){
				return true;
			}
		}
		return false;
	}

	public int getHeight() {
		return applicationHeight;
	}
	
	static public void main(String argv[]) throws Exception {
		GameManager gameManager;
		switch (argv.length){
		case 0:
			gameManager = new GameManager();
			gameManager.run();
			break;
		case 1:
			gameManager = new GameManager();
			gameManager.setBotNegotiation(true);
			gameManager.run();
			break;
		default:
			if(argv[0].contains("help") || argv[0].equals("-h")){
				System.out.println("Usage:\n\tjava -jar gameManager-1.0.jar [bot]");
			}
		}
		
	}

	private void setBotNegotiation(boolean b) {
		isBotNegotiation = b;
	}
	
	public boolean isBotNegotiation(){
		return isBotNegotiation;
	}

	private void run() {
		mainFrame.pack();
		mainFrame.show();
		popupAlerts();
	}
}