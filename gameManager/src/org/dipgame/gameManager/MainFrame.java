package org.dipgame.gameManager;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * MainFrame
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class MainFrame extends JFrame{
	
	private GameManager app;
	private MainPanel mainpanel;
	
	public MainFrame(GameManager app) throws Exception{
		this.app = app;
		
		this.setTitle("gameManager - The DipGame Game Manager");
		mainpanel = new MainPanel(this.app);
		JScrollPane scroll = new JScrollPane(mainpanel);
		scroll.setMinimumSize(new Dimension(400, this.app.getHeight()));
		this.setContentPane(scroll);
	}

	public MainPanel getMainPanel() {
		return mainpanel;
	}

	

}
