package org.dipgame.gameManager;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;

import javax.swing.JPanel;

/**
 * BlueTextBar
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class BlueTextBar extends JPanel{
	
	private JPanel descriptionContent;
	private int hMargin = 30;
	
	public BlueTextBar(String text, MouseAdapter adapter){
		this.setBackground(Utils.CYAN);
		
		descriptionContent = new JPanel();
		descriptionContent.setBackground(Utils.BLUE);
		descriptionContent.add(Utils.getText(text, adapter));
		
		this.add(Utils.addBordersToLine(descriptionContent));
		
	}
	
	public BlueTextBar(String text){
		this(text, new MouseAdapter() {
		
		});
	}
	
	public BlueTextBar(){
		this("");
	}
	
	public void setMargins(int hMargin){
		if(hMargin>=0){
			this.hMargin = hMargin*2;
		}
	}
	
	public void revalidateAll(){
		descriptionContent.revalidate();
	}
	
	public void setSizes(){
		this.setPreferredSize(new Dimension(getParent().getWidth()-hMargin, 20));
		descriptionContent.setPreferredSize(getPreferredSize());
	}

}
