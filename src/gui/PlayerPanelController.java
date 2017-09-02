package src.gui;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Stack;

import src.model.Board;
import src.model.Token;

public class PlayerPanelController implements Controller, KeyListener, MouseListener{
	private Board myModel;
	private PlayerPanel panel;
	private SuperController superC;

	public PlayerPanelController(Board board, PlayerPanel panel) {
		myModel = board;
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

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
	
	public void setSuper(SuperController s){
		superC = s;
	}

	@Override
	public AbstractGamePanel getPanel() {
		return panel;
	}

}
