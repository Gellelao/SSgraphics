package src.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import src.model.Board;
import src.model.Token;

/**
 * Implementation of the Controller interface intended for responding to user actions on
 * the TokenSelectionPanel panels. Those panels are controlled exclusively with the mouse
 * 
 * @author Deacon
 *
 */
public class TokenSelectionPanelController implements Controller, KeyListener, MouseListener{
	private TokenSelectionPanel panel;
	private SuperController superC;

	public TokenSelectionPanelController(TokenSelectionPanel panel) {
		this.panel = panel;
	}
	
	@Override
	public AbstractGamePanel getPanel() {
		return panel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int mouseX = e.getX();
		int mouseY = e.getY();
		TokenRegion r = panel.getRegion(mouseX, mouseY);
		if(r != null){
			superC.selectRotation(r);
		}
		superC.setSidesToDefault();
	}

	@Override
	public void setSuper(SuperController s) {
		this.superC = s;
	}

	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
}
