package src.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import src.model.Board;
import src.model.PlayerToken;

public class PlayerPanel extends AbstractGamePanel{
	private static final long serialVersionUID = 1L;
	private PlayerToken player;
	private Color colour;
	private Board myModel;

	public PlayerPanel(Board model, String player) {
		myModel = model;
		if(player.equals("p1")) {
			this.player = model.getP1();
			this.colour = new Color(255, 249, 81);
		}
		else {
			this.player = model.getP2();
			this.colour = new Color(86, 196, 64);
		}
	}

	@Override
	protected Color getBGColour() {
		return colour;
	}

	@Override
	protected void drawAll(Graphics2D g) {
		super.drawGrid(g, myModel.getAvailable(player));
	}
}
