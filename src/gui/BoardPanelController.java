package src.gui;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Stack;

import src.model.Board;
import src.model.Token;

/**
 * Controller for the boardPanel class. Takes care of mouse and key interactions with the boardPanel
 * 
 * @author Deacon
 *
 */
public class BoardPanelController implements Controller, KeyListener, MouseListener {
	private BoardPanel panel;
	private SuperController superC;

	public BoardPanelController(BoardPanel panel) {
		this.panel = panel;
	}

	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	
	/**
	 * Calls the move method of the superController, providing the appropriate direction
	 */
	public void keyTyped(KeyEvent e) {
		switch (e.getKeyChar()) {
		case 'w': superC.moveSelected("up"); return;
		case 'a': superC.moveSelected("left"); return;
		case 's': superC.moveSelected("down"); return;
		case 'd': superC.moveSelected("right"); return;
		}
	}

	public Token getSuperSelected() {
		return superC.getSelected();
	}

	@Override
	public AbstractGamePanel getPanel(){
		return panel;
	}

	/**
	 * Passes a string identifying the edge a user has clicked on and the TokenRegions they have clicked
	 * to the superController, which processes that information before calling its own move method
	 * If the user has not clicked an edge, the r.checkSubregions() method will return null, and that is handled by the superController
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println(e.getX() + ", " + e.getY());
		processClick(e.getX(), e.getY());
	}
	
	/**
	 * I've separated this code out from the mouseClicked method so that it can be called
	 * externally (e.g from a test), without having to click the mouse
	 * 
	 * @param x
	 * @param y
	 */
	@Override
	public void processClick(int x, int y){
		TokenRegion r = panel.getRegion(x, y);
		if(r != null && r.getToken() != null) {
			String edge = r.checkSubregions(x, y);
			superC.boardSelect(r, edge);
		}
	}
	
	@Override
	public void setSuper(SuperController s) {
		this.superC = s;
	}

	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}