package src.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import src.model.Board;
import src.model.Token;

public class TokenSelectionPanelController implements Controller, KeyListener, MouseListener{
	private Board myModel;
	private TokenSelectionPanel panel;
	private SuperController superC;

	public TokenSelectionPanelController(Board board, TokenSelectionPanel panel) {
		myModel = board;
		this.panel = panel;
	}
	
	@Override
	public AbstractGamePanel getPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int mouseX = e.getX();
		int mouseY = e.getY();
		Token t = panel.getToken(mouseX, mouseY);
		if(t != null){
			if(superC == null)System.out.println("shit");
			superC.selectRotation(t);
			myModel.spawnToken(t);
			System.out.println(t);
		}
		superC.setSidesToDefault();
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
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSuper(SuperController s) {
		this.superC = s;
	}

}
