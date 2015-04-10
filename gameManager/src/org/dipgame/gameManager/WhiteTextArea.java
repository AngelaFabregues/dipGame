package org.dipgame.gameManager;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * WhiteTextArea
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class WhiteTextArea extends JPanel{
	
	private int hMargin = 30;
	private int vMargin = 230;
	
	private JTextPane text;
	private JScrollPane scrollpane;
	
	public WhiteTextArea(){

		this.setBackground(Utils.CYAN);
		text = new JTextPane();
		text.setBackground(Color.white);
		text.setAutoscrolls(true);
		text.setEditable(false);
		
		scrollpane = new JScrollPane(text);
		this.add(scrollpane);
	}
	
	public void setMargins(int hMargin){
		if(hMargin>=0){
			this.hMargin = hMargin*2;
		}
	}
	
	public void setSizes() {
		scrollpane.setPreferredSize(new Dimension(getParent().getWidth()-hMargin, getParent().getHeight()-vMargin));
	}

	public String getText() {
		return text.getText();
	}

	public void append(String s, Color color, Boolean bold) {
		try {
			StyledDocument doc = (StyledDocument) text.getDocument();
			Style style = doc.addStyle("name", null);
			StyleConstants.setForeground(style, color);
			StyleConstants.setBold(style, bold);
			doc.insertString(doc.getLength(), s, style);

			// Determine whether the scrollbar is currently at the very bottom
			// position.
			JScrollBar vbar = scrollpane.getVerticalScrollBar();
			boolean autoScroll = ((vbar.getValue() + vbar.getVisibleAmount()) == vbar
					.getMaximum());

			// append to the JTextArea (that's wrapped in a JScrollPane named
			// 'scrollPane'
			// now scroll if we were already at the bottom.
			if (autoScroll) {
				text.setCaretPosition(text.getDocument().getLength());
			}
		} catch (BadLocationException exc) {
			exc.printStackTrace();
		}
	}

	public void revalidateAll() {
		text.revalidate();
		scrollpane.revalidate();
	}
}
