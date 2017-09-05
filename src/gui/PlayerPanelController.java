package src.gui;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Stack;

import src.model.Board;
import src.model.Token;

/**
 * Implementation of the Controller interface intended for responding to user actions on
 * the playerPanel panels. Those panels are controlled exclusively with the mouse
 * 
 * @author Deacon
 *
 */
public class PlayerPanelController implements Controller, KeyListener, MouseListener{
	private PlayerPanel panel;
	private SuperController superC;

	public PlayerPanelController(PlayerPanel panel) {
		this.panel = panel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int mouseX = e.getX();
		int mouseY = e.getY();
		Token t = panel.getToken(mouseX, mouseY);
		if(t != null){
			superC.playerSelect(t, panel.getPlayer());
		}
	}
	
	public void setSuper(SuperController s){
		superC = s;
	}

	@Override
	public AbstractGamePanel getPanel() {
		return panel;
	}

	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
}
