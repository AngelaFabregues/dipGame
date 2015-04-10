package org.dipgame.gameManager.utils;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * TextBox
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class TextBox extends JTextPane {

	public TextBox(Color background) {
		this.setBackground(background);
		this.setAutoscrolls(true);
		this.setEditable(false);
	}

	public TextBox(String description, Color background) {
		this(background);
		append(description, Color.black, false);
	}

	public String getText() {
		return this.getText();
	}

	public void append(String s, Color color, Boolean bold) {
		try {
			StyledDocument doc = super.getStyledDocument();
			SimpleAttributeSet sa = new SimpleAttributeSet();
			StyleConstants.setAlignment(sa, StyleConstants.ALIGN_JUSTIFIED);
			StyleConstants.setLeftIndent(sa, 10);
			StyleConstants.setRightIndent(sa, 10);
			StyleConstants.setSpaceAbove(sa, 5);
			StyleConstants.setSpaceBelow(sa, 5);
			Style style = doc.addStyle("name", null);
			StyleConstants.setForeground(style, color);
			StyleConstants.setBold(style, bold);
			doc.insertString(doc.getLength(), s, style);
			doc.setParagraphAttributes(0, doc.getLength(), sa, false);
		} catch (BadLocationException exc) {
			exc.printStackTrace();
		}
	}

	public void revalidateAll() {
		this.revalidate();
	}
}
