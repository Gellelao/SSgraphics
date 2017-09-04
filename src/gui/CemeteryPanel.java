package src.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import src.model.Board;
import src.model.Player;

public class CemeteryPanel extends AbstractGamePanel{
	private static final long serialVersionUID = 1L;
	private Player player;
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
		super.drawGrid(g, myModel.getCemetery());
	}

	@Override
	protected Controller getController() {
		System.out.println("getController() method of CemeteryPanel should not be called/should do nothing. Returned null");
		return null;
	}

	@Override
	public void addRegion(TokenRegion r) {
		// Do nothing ,the cemetery does not need TokenReigons
	}

}
