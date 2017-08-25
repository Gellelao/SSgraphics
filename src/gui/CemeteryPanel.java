package src.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import src.model.Board;
import src.model.PlayerToken;

public class CemeteryPanel extends AbstractGamePanel{
	private static final long serialVersionUID = 1L;
	private PlayerToken player;
	private Board myModel;

	public CemeteryPanel(Board model) {
		myModel = model;
	}
	
	@Override
	protected Color getBGColour() {
		return Color.GRAY;
	}
	
	@Override
	protected void drawAll(Graphics2D g) {
		super.drawGrid(g, myModel.getCemetery());
	}

}
