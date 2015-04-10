package org.dipgame.gameManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.dipgame.gameManager.utils.BareBonesBrowserLaunch;
import org.dipgame.gameManager.utils.JTextPaneAppend;

/**
 * Utils
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class Utils {
	
	final static Color CYAN = new Color(99, 158, 255);
	final static String IP = "localhost";
	final static int PORT = 16713;
	final static String LOG_FOLDER = "logs";
	public final static Color BLUE = new Color(49, 97, 206);
	static final int FULL_SIZE = 768;
	static final int MIN_SIZE = 550;
	public static final int NEGO_PORT = 16714;
	public static final String LEAVE_EMPTY = "empty";
	public static final String URL = "http://www.dipgame.org";
	
	static JTextPaneAppend getText(String text, final String url) {
		MouseAdapter mouseAdapter = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				BareBonesBrowserLaunch.openURL(url);
			}
		};
		return getText(text, mouseAdapter);
	}
	
	static JTextPaneAppend getText(String text, MouseAdapter mouseAdapter) {
		JTextPaneAppend textPanel = new JTextPaneAppend();
		textPanel.setBackground(BLUE);
		textPanel.setEditable(false);
		textPanel.append(text, CYAN, true);
		textPanel.addMouseListener(mouseAdapter);
		return textPanel;
	}

	static Component addBordersToLine(Component content) {
		// Crea el panel principal con la imagen de login
		JPanel rightFooterbelly = new JPanel();
		BoxLayout boxLayout3 = new BoxLayout(rightFooterbelly, BoxLayout.X_AXIS);
		rightFooterbelly.setLayout(boxLayout3);
		rightFooterbelly.setBackground(CYAN);

		JLabel leftbar2 = new JLabel();
		ImageIcon leftbariconp = new ImageIcon("files/img/latizqp.jpg");
		leftbar2.setIcon(leftbariconp);
		rightFooterbelly.add(leftbar2);

		
		rightFooterbelly.add(content);

		JLabel rightbar2 = new JLabel();
		ImageIcon rightbariconp = new ImageIcon("files/img/latderp.jpg");
		rightbar2.setIcon(rightbariconp);
		rightFooterbelly.add(rightbar2);

		return rightFooterbelly;
	}
	
	static Component addBorders(Component content){
		JPanel panel = new JPanel();
		BoxLayout boxLayout1 = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(boxLayout1);
		panel.setBackground(CYAN);
		
		JPanel topPanel = new JPanel();
		topPanel.setPreferredSize(new Dimension((int)content.getPreferredSize().getWidth()+5, 5));
		topPanel.setLayout(new BorderLayout());
		topPanel.setBackground(BLUE);

		JLabel nwCorner = new JLabel();
		ImageIcon nwIcon = new ImageIcon("files/img/nw.png");
		nwCorner.setIcon(nwIcon);
		topPanel.add(nwCorner, BorderLayout.WEST);
		
		JLabel neCorner = new JLabel();
		ImageIcon neIcon = new ImageIcon("files/img/ne.png");
		neCorner.setIcon(neIcon);
		topPanel.add(neCorner, BorderLayout.EAST);
		panel.add(topPanel);

		JPanel contentPanel = new JPanel();
		contentPanel.setBackground(BLUE);
		contentPanel.add(content);
		panel.add(content);

		JPanel downPanel = new JPanel();
		downPanel.setPreferredSize(new Dimension((int)content.getPreferredSize().getWidth(), 5));
		downPanel.setLayout(new BorderLayout());
		downPanel.setBackground(BLUE);

		JLabel swCorner = new JLabel();
		ImageIcon swIcon = new ImageIcon("files/img/sw.png");
		swCorner.setIcon(swIcon);
		downPanel.add(swCorner, BorderLayout.WEST);
		
		JLabel seCorner = new JLabel();
		ImageIcon seIcon = new ImageIcon("files/img/se.png");
		seCorner.setIcon(seIcon);
		downPanel.add(seCorner, BorderLayout.EAST);
		panel.add(downPanel);
		return panel;
	}

	public static MouseAdapter createLink(final String url) {
		return new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				BareBonesBrowserLaunch.openURL(url);
			}
		};
	}
}
