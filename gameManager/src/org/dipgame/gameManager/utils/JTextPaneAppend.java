package org.dipgame.gameManager.utils;
import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * JTextPaneAppend
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class JTextPaneAppend extends JTextPane{
	
	private static final long serialVersionUID = 1L;

	public void append(String s,Color color, Boolean bold) {
		   try {
		      StyledDocument doc = (StyledDocument) this.getDocument();
		      Style style=doc.addStyle("name", null);
		      StyleConstants.setForeground(style, color);
		      StyleConstants.setBold(style, bold);
		      doc.insertString(doc.getLength(), s, style);
		   } catch(BadLocationException exc) {
		      exc.printStackTrace();
		   }
		}
	
	public void append2(String s, Color color, Boolean bold){
		StyledDocument doc = super.getStyledDocument();
		SimpleAttributeSet sa = new SimpleAttributeSet();
		StyleConstants.setAlignment(sa, StyleConstants.ALIGN_JUSTIFIED);
		StyleConstants.setLeftIndent(sa, 5);
		StyleConstants.setRightIndent(sa, 5);
		Style style = doc.addStyle("name", null);
		StyleConstants.setForeground(style, color);
		StyleConstants.setBold(style, bold);
		try {
			doc.insertString(doc.getLength(), s, style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		doc.setParagraphAttributes(0, doc.getLength(), sa, false);
	}
	
	  }