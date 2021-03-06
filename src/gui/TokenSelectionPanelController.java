package src.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Implementation of the Controller interface intended for responding to user actions on
 * the TokenSelectionPanel panels. Those panels are controlled exclusively with the mouse
 * 
 * @author Deacon
 *
 */
public class TokenSelectionPanelController implements Controller, MouseListener{
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
	public void processClick(int x, int y) {
		TokenRegion r = panel.getRegion(x, y);
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
}
