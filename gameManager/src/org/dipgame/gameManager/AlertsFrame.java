package org.dipgame.gameManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.dipgame.gameManager.utils.JTextPaneAppend;
import org.dipgame.gameManager.utils.TextBox;

/**
 * AlertsFrame
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class AlertsFrame extends JFrame {
	
	private List<String> alerts;

	public AlertsFrame(List<String> alerts, int applicationHeight){
		this.alerts = alerts;
		
		this.setTitle("Game Manager alerts");
		this.setBackground(Utils.CYAN);
		
		JPanel alertMainPanel = new JPanel();
		alertMainPanel.setBackground(Utils.CYAN);
		BoxLayout boxLayout = new BoxLayout(alertMainPanel, BoxLayout.Y_AXIS);
		alertMainPanel.setLayout(boxLayout);
		alertMainPanel.add(MainPanel.createHeader());
		
		
		
		
		JPanel footer = new JPanel();
		footer.setBackground(Utils.CYAN);
		
		JPanel footerContent = new JPanel();
		footerContent.setBackground(Utils.BLUE);
		footerContent.setPreferredSize(new Dimension(325, 20));
		footerContent.add(Utils.getText("Game Manager outputs the following alerts:", new MouseAdapter() {
		
		}));
		
		footer.add(Utils.addBordersToLine(footerContent));
		alertMainPanel.add(footer);
		
		
		
			
		JPanel descriptionPanel = new JPanel();
		descriptionPanel.setBackground(Utils.CYAN);

		String description="";
		for(String alert: this.alerts){
			description+=alert+"\n\n";
		}
		
		TextBox text = new TextBox(Color.white);
		text.setPreferredSize(new Dimension(325, 380));
		text.append(description, Color.BLACK, false);
		text.addMouseListener(new MouseAdapter() {
		});
		
		JScrollPane scrollbar = new JScrollPane(text);
		descriptionPanel.add(scrollbar);
		alertMainPanel.add(descriptionPanel);
		
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
		alertMainPanel.add(buttonPanel);
		
		alertMainPanel.add(MainPanel.createFooter(320));

		JScrollPane scroll = new JScrollPane(alertMainPanel);
		scroll.setPreferredSize(new Dimension(400, applicationHeight));
		this.setContentPane(scroll);
	}
	
	private void close() {
		this.setVisible(false);
	}
}
