package src.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import src.model.Board;
import src.model.PlayerToken;

public class playerPanel extends AbstractGamePanel{
	private static final long serialVersionUID = 1L;
	private PlayerToken player;
	private Color colour;
	private Board myModel;

	public playerPanel(Board model, String player) {
		myModel = model;
		if(player.equals("p1")) {
			this.player = model.getP1();
			this.colour = Color.YELLOW.darker();
		}
		else {
			this.player = model.getP2();
			this.colour = Color.GREEN.darker();
		}
	}

	public void paintComponent(Graphics _g) {
		super.superPaint(_g);
		Graphics2D g = (Graphics2D) _g;
		g.setColor(getBGColour());
		g.fillRect(0, 0, getWidth(), getHeight());
		drawGrid(g, myModel.getAvailable(player));
	}

	@Override
	protected Color getTileColour() {
		return colour;
	}

	@Override
	protected Color getBGColour() {
		return colour;
	}
}
