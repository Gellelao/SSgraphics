package src.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import src.model.Board;

/**
 * Panel to represent the cemetery of the game
 * The cemetery contains pieces of both players, so only one cemetery is required
 * 
 * @author Deacon
 *
 */
public class CemeteryPanel extends AbstractGamePanel{
	private static final long serialVersionUID = 1L;

	private Board myModel;

	public CemeteryPanel(Board model) {
		myModel = model;
		this.setFocusable(false);
	}

	@Override
	protected Color getBGColour() {
		return Color.GRAY;
	}

	@Override
	protected void drawAll(Graphics2D g) {
		g.setColor(getBGColour());
		g.fillRect(0, 0, getWidth(), getHeight());
		// Main point of difference: this panel draws "myModel.getCemetery()" as its grid
		super.drawGrid(g, myModel.getCemetery());
	}

	@Override
	public Controller getController() {
		// The cemetery does not need a controller, because it is not interacted with
		return null;
	}

	@Override
	public void addRegion(TokenRegion r) {
		// Do nothing ,the cemetery does not need TokenReigons
	}

}
