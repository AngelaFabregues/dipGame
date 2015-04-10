package org.dipgame.gameManager;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * DescriptionFrame
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class DescriptionFrame extends JFrame{

	public DescriptionFrame(int applicationHeight){
		this.setBackground(Utils.CYAN);
		
		JPanel moreInfoPanel = new JPanel();
		moreInfoPanel.setBackground(Utils.CYAN);
		BoxLayout boxLayout = new BoxLayout(moreInfoPanel, BoxLayout.Y_AXIS);
		moreInfoPanel.setLayout(boxLayout);
		
		moreInfoPanel.add(MainPanel.createHeader());
		Component desc = MainPanel.createDescription();
		desc.setBackground(Utils.CYAN);
		moreInfoPanel.add(desc);
		moreInfoPanel.add(MainPanel.createDiplomacyImage());
		moreInfoPanel.add(MainPanel.createGameSettings());
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Utils.CYAN);
		JButton close = new JButton("Close");
		close.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				close();
			}
		});
		close.setEnabled(true);
		buttonPanel.add(close);
		moreInfoPanel.add(buttonPanel);
		
		moreInfoPanel.add(MainPanel.createFooter(320));

		
		JScrollPane scroll = new JScrollPane(moreInfoPanel);
		scroll.setPreferredSize(new Dimension(400, applicationHeight));
		this.setContentPane(scroll);
	}
	
	private void close() {
		this.setVisible(false);
	}
}