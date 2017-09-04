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

	public BoardPanelController(Board board, BoardPanel panel) {
		this.panel = panel;
	}

	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
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

	@Override
	public void mouseClicked(MouseEvent e) {
		int mouseX = e.getX();
		int mouseY = e.getY();

		TokenRegion r = panel.getRegion(mouseX, mouseY);
		if(r != null && r.getToken() != null) {
			String edge = r.checkSubregions(mouseX, mouseY);
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