package src.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import src.model.Board;
import src.model.PlayerToken;

public class cemeteryPanel extends AbstractGamePanel{
	private static final long serialVersionUID = 1L;
	private PlayerToken player;
	private Board myModel;

	public cemeteryPanel(Board model, String player) {
		myModel = model;
		if(player.equals("p1")) {
			this.player = model.getP1();
		}
		else {
			this.player = model.getP2();
		}
	}
	
	@Override
	protected Color getBGColour() {
		return Color.GRAY;
	}

	@Override
	protected void paintComponent(Graphics _g) {
		super.superPaint(_g);
		Graphics2D g = (Graphics2D) _g;
		g.setColor(getBGColour());
		g.fillRect(0, 0, getWidth(), getHeight());
		drawGrid(g, myModel.getCemetery(player));
	}

}
